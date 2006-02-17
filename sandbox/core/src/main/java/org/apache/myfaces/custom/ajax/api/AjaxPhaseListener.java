/**
 * Copyright 2004 The Apache Software Foundation.
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

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 *          <p/>
 *          $Log: $
 */
public class AjaxPhaseListener implements PhaseListener
{
    private static Log log = LogFactory.getLog(AjaxPhaseListener.class);

    public void beforePhase(PhaseEvent event)
    {

    }

    public static Object getValueForComponent(FacesContext facesContext, UIComponent component)
    {
        String possibleClientId = component.getClientId(facesContext);

        if (facesContext.getExternalContext().getRequestParameterMap().containsKey(possibleClientId))
        {
            return facesContext.getExternalContext().getRequestParameterMap().get(possibleClientId);
        }
        else
        {
            possibleClientId = (String) facesContext.getExternalContext().getRequestParameterMap().get(
                    "affectedAjaxComponent");

            UIViewRoot root = facesContext.getViewRoot();

            UIComponent ajaxComponent =
                    facesContext.getViewRoot().findComponent(possibleClientId);

            if (ajaxComponent == component)
            {
                return facesContext.getExternalContext().getRequestParameterMap().get(possibleClientId);
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
        log.debug("In AjaxPhaseListener afterPhase");
        /*FacesContext context = event.getFacesContext();

        if (context.getExternalContext().getRequestParameterMap().containsKey("affectedAjaxComponent"))
        {
            UIViewRoot root = context.getViewRoot();
            String clientId = (String)context.getExternalContext().getRequestParameterMap().get("affectedAjaxComponent");
            UIComponent ajaxComponent = ComponentUtils.findComponentFullTree(context, root, clientId); //root.findComponent(clientId);
            log.debug("affectedAjaxComponent: " + ajaxComponent + " - " + ajaxComponent.getId());
            
            if (ajaxComponent instanceof AjaxComponent)
            {
                try
                {
                    // TR - What is this HtmlBufferResponseWriterWrapper stuff for????  This caused me hours and hours of pain
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
        }*/
    }


    /**
     * We need to hang our AJAX phase listener in the invoke application phase as it is
     * impossible to stop rendering in the render response phase.
     *
     * @return PhaseId The AJAX phase listener will be invoked after the invoke application phase.
     */
    public PhaseId getPhaseId()
    {
        //return PhaseId.ANY_PHASE;
        //return PhaseId.RESTORE_VIEW;
        return PhaseId.INVOKE_APPLICATION;
    }

}
