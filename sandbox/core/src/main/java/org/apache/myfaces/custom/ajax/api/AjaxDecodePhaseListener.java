/*
 * Copyright 2004-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.myfaces.custom.ajax.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.ajax.util.AjaxRendererUtils;
import org.apache.myfaces.custom.inputAjax.HtmlCommandButtonAjax;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 * This short circuits the life cycle and applies updates to affected components only
 * <p/>
 * User: treeder
 * Date: Oct 26, 2005
 * Time: 6:03:21 PM
 */
public class AjaxDecodePhaseListener
        implements PhaseListener
{
    private static Log log = LogFactory.getLog(AjaxDecodePhaseListener.class);


    /**
     * Some Ajax components might do special decoding at apply request values phase
     *
     * @return PhaseId The AJAX decode phase listener will be invoked before apply request values phase.
     */
    public PhaseId getPhaseId()
    {
        return PhaseId.APPLY_REQUEST_VALUES;

    }

    public void beforePhase(PhaseEvent event)
    {
        log.debug("In AjaxDecodePhaseListener beforePhase");
        FacesContext context = event.getFacesContext();
        Map externalRequestMap = context.getExternalContext().getRequestParameterMap();
        if (externalRequestMap.containsKey("affectedAjaxComponent"))
        {
            UIViewRoot root = context.getViewRoot();
            //DebugUtils.printView(root, System.out);
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            //System.out.println("REQUEST MAP: " + mapToString(requestMap));
            String affectedAjaxComponent = (String) requestMap.get("affectedAjaxComponent");

            UIComponent ajaxComponent = root.findComponent(affectedAjaxComponent);
            if (ajaxComponent == null)
            {
                String msg = "Component with id [" + affectedAjaxComponent + "] not found in view tree.";
                log.error(msg);
                throw new ComponentNotFoundException(msg);
            }
            log.debug("affectedAjaxComponent: " + ajaxComponent + " - " + ajaxComponent.getId());
            if (ajaxComponent instanceof HtmlCommandButtonAjax)
            {
                // special treatment for this one, it will try to update the entire form
                // 1. get surrounding form
                //String elname = (String) requestMap.get("elname");
                FormInfo fi = _ComponentUtils.findNestingForm(ajaxComponent, context);
                UIComponent form = fi.getForm();
                //System.out.println("FOUND FORM: " + form);
                if (form != null)
                {
                    form.processDecodes(context);
                    form.processValidators(context);
                    form.processUpdates(context);
                    //System.out.println("DONE!");
                }

            }
            else if (ajaxComponent instanceof AjaxComponent)
            {
                try
                {
                    // Now let the component decode this request
                    ((AjaxComponent) ajaxComponent).decodeAjax(context);
                }
                catch (Exception e)
                {
                    log.error("Exception while decoding ajax-request", e);
                }
            }
            else
            {
                log.error("Found component is no ajaxComponent : " + RendererUtils.getPathToComponent(ajaxComponent));
            }

            context.getViewRoot().processApplication(context);

            // NOW TRYING TO DO THE ENCODE THAT WAS IN AJAXPHASELISTENER RIGHT HERE, THEN ENDING RESPONSE
            if (ajaxComponent instanceof AjaxComponent)
            {
                try
                {
                    ServletResponse response = (ServletResponse) context.getExternalContext().getResponse();
                    //context.getResponseWriter();
                    /*if (response == null)
                    {
                        ServletResponse servletResponse = (ServletResponse) context.getExternalContext().getResponse();
                        Writer htmlResponseWriter = servletResponse.getWriter();
                        response = new HtmlResponseWriterImpl(htmlResponseWriter, "text/html", "UTF-8");
                        context.setResponseWriter(response);
                    }*/
                    //write wrapping output
                    //response.setContentType("application/xml");
                    response.reset();
                    //response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/xml");

                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

                    StringBuffer buff = new StringBuffer();
                    buff.append("<?xml version=\"1.0\"?>");
                    buff.append("<response>");
                    PrintWriter out = response.getWriter();
                    out.print(buff);

                    if (ajaxComponent instanceof HtmlCommandButtonAjax)
                    {
                        // special treatment for this one, it will try to update the entire form
                        // 1. get surrounding form
                        //String elname = (String) requestMap.get("elname");
                        FormInfo fi = _ComponentUtils.findNestingForm(ajaxComponent, context);
                        UIComponent form = fi.getForm();
                        //System.out.println("FOUND FORM: " + form);
                        if (form != null)
                        {
                            // special case, add responses from all components in form
                            encodeChildren(form, context, requestMap);
                        }
                    }
                    else
                    {
                        // let component render xml response
                        // NOTE: probably don't need an encodeAjax in each component, but leaving it in until that's for sure
                        ((AjaxComponent) ajaxComponent).encodeAjax(context);
                    }

                    // end response
                    out.print("</response>");
                    out.flush();
                }
                catch (IOException e)
                {
                    log.error("Exception while rendering ajax-response", e);
                }
            }
            else
            {
                log.error("Found component is no ajaxComponent : " + RendererUtils.getPathToComponent(ajaxComponent));
            }

            StateManager stateManager = context.getApplication().getStateManager();
            if (!stateManager.isSavingStateInClient(context))
            {
                stateManager.saveSerializedView(context);
            }
            context.responseComplete();
        }
    }

    private void encodeChildren(UIComponent form, FacesContext context, Map requestMap)
            throws IOException
    {
        List formChildren = form.getChildren();
        for (int i = 0; i < formChildren.size(); i++)
        {
            UIComponent uiComponent = (UIComponent) formChildren.get(i);
            //System.out.println("component id: " + uiComponent.getClientId(context));
            // only if it has a matching id in the request list
            if (requestMap.containsKey(uiComponent.getClientId(context)))
            {
                //System.out.println("FOUND COMPONENT SO ENCODING AJAX");
                AjaxRendererUtils.encodeAjax(context, uiComponent);
            }
            // recurse
            encodeChildren(uiComponent, context, requestMap);
        }
    }

    /**
     * spit out each name/value pair
     * THIS IS IN HASHMAPUTILS, BUT FOR SOME REASON, ISN'T GETTING INTO THE JARS
     */
    public static String mapToString(Map map)
    {
        Set entries = map.entrySet();
        Iterator iter = entries.iterator();
        StringBuffer buff = new StringBuffer();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();
            buff.append("[" + entry.getKey() + "," + entry.getValue() + "]\n");
        }
        return buff.toString();
    }

    public static Object getValueForComponent(FacesContext context, UIComponent component)
    {
        String possibleClientId = component.getClientId(context);

        if (context.getExternalContext().getRequestParameterMap().containsKey(possibleClientId))
        {
            return context.getExternalContext().getRequestParameterMap().get(possibleClientId);
        }
        else
        {
            possibleClientId = (String) context.getExternalContext().getRequestParameterMap().get(
                    "affectedAjaxComponent");
            log.debug("affectedAjaxComponent: " + possibleClientId);

            UIViewRoot root = context.getViewRoot();
            UIComponent ajaxComponent = root.findComponent(possibleClientId);
            if (ajaxComponent == component)
            {
                return context.getExternalContext().getRequestParameterMap().get(possibleClientId);
            }
            else
            {
                log.error("No value found for this component : " + possibleClientId);
                return null;
            }
        }
    }


    public void afterPhase(PhaseEvent event)
    {

    }


}
