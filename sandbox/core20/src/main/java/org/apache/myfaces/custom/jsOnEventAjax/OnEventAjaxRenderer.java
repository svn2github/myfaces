/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.jsOnEventAjax;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFRenderer;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils.ScriptContext;

@JSFRenderer(family="org.apache.myfaces.custom.jsOnEventAjax.OnEventAjax",
             renderKitId="HTML_BASIC",
             type="org.apache.myfaces.custom.jsOnEventAjax.OnEventAjax")
public class OnEventAjaxRenderer extends Renderer
{

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        //check for NP
        super.encodeEnd(facesContext, component);
        
        AbstractOnEventAjaxComponent onEventAjaxComponent = (AbstractOnEventAjaxComponent) component;
        
        //UIComponent target = ( onEventAjaxComponent.getForId() != null ) ? 
        //        onEventAjaxComponent.findComponent(onEventAjaxComponent.getForId()) : 
        //            onEventAjaxComponent;

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR,  onEventAjaxComponent.getClientId(facesContext), null);
        
        Map<String,List<ClientBehavior>> clientBehaviors = onEventAjaxComponent.getClientBehaviors();

        String eventName = onEventAjaxComponent.getEventName();
        String functionName = onEventAjaxComponent.getFunctionName(facesContext);
        ClientBehaviorContext cbcContext = ClientBehaviorContext
            .createClientBehaviorContext(facesContext, onEventAjaxComponent,
                    eventName, onEventAjaxComponent.getClientId(facesContext),
                    null);
        
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.writeText(getOnEventFunction(facesContext, cbcContext, onEventAjaxComponent, clientBehaviors, eventName, onEventAjaxComponent, functionName), null);        
        writer.endElement(HTML.SCRIPT_ELEM);
        
        writer.endElement(HTML.SPAN_ELEM);
    }
    
    protected String getOnEventFunction(FacesContext facesContext, ClientBehaviorContext cbcContext, AbstractOnEventAjaxComponent c, 
                                        Map<String,List<ClientBehavior>> clientBehaviors, String eventName, 
                                        UIComponent target, String functionName)
    {
        ScriptContext script = new ScriptContext(
                MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isPrettyHtml());

        script.append(functionName);
        script.append(" = ");
        script.append(" function(e)");
        script.append('{');

        if (c.getJsStartContent() != null)
        {
            script.append(c.getJsStartContent());
        }
        
        script.append("if(e.status == '");
        script.append(c.getStatus() == null ? "success" : c.getStatus());
        script.append("'){");
        
        if (c.getJsBeforeClientBehaviors() != null)
        {
            script.append(c.getJsBeforeClientBehaviors());
        }
        
        for (ClientBehavior cb : clientBehaviors.get(eventName))
        {
            script.append(cb.getScript(cbcContext));
            script.append(';');
        }

        if (c.getJsAfterClientBehaviors() != null)
        {
            script.append(c.getJsAfterClientBehaviors());
        }
        
        script.append('}');

        if (c.getJsEndContent() != null)
        {
            script.append(c.getJsEndContent());
        }
        
        script.append('}');
        
        return script.toString();
    }
}
