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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.application.ComponentNotFoundException;
import org.apache.myfaces.application.jsp.JspStateManagerImpl;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * This short circuits the life cycle and applies updates to affected components only
 *
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
        if(externalRequestMap.containsKey("affectedAjaxComponent"))
        {
            UIViewRoot root = context.getViewRoot();
            //DebugUtils.printView(root, System.out);
            String affectedAjaxComponent = (String) context.getExternalContext().getRequestParameterMap().get("affectedAjaxComponent");

            UIComponent ajaxComponent = root.findComponent(affectedAjaxComponent);
            if (ajaxComponent == null)
            {
                String msg = "Component with id [" + affectedAjaxComponent + "] not found in view tree.";
                log.error(msg);
                throw new ComponentNotFoundException(msg);
            }
            log.debug("affectedAjaxComponent: " + ajaxComponent + " - " + ajaxComponent.getId());
            if (ajaxComponent instanceof AjaxComponent)
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



            // NOW TRYING TO DO THE ENCODE THAT WAS IN AJAXPHASELISTENER RIGHT HERE, THEN ENDING RESPONSE
            if (ajaxComponent instanceof AjaxComponent)
            {
                try
                {
                    if (context.getResponseWriter() == null)
                    {
                        ServletResponse response = (ServletResponse) context.getExternalContext().getResponse();
                        Writer htmlResponseWriter = response.getWriter();
                        context.setResponseWriter(new HtmlResponseWriterImpl(htmlResponseWriter, "text/html", "UTF-8"));
                    }
                    ((AjaxComponent) ajaxComponent).encodeAjax(context);
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
