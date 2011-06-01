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
package org.apache.myfaces.custom.jsCallbackFunction;

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
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;

/**
 * 
 * @author Leonardo Uribe
 *
 */
@JSFRenderer(renderKitId="HTML_BASIC", 
        family="org.apache.myfaces.custom.jsCallbackFunction.JsCallbackFunction",
        type="org.apache.myfaces.custom.jsCallbackFunction.JsCallbackFunction")
public class JsCallbackFunctionRenderer extends Renderer
{
    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        AbstractJsCallbackFunctionComponent jsFunctionCallback = (AbstractJsCallbackFunctionComponent) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        
        writer.startElement(HTML.SPAN_ELEM, jsFunctionCallback);
        writer.writeAttribute(HTML.ID_ATTR,  jsFunctionCallback.getClientId(facesContext), null);
        writer.startElement(HTML.SCRIPT_ELEM, jsFunctionCallback);

        String functionName = jsFunctionCallback.getFunctionName(facesContext);
        
        Map<String,List<ClientBehavior>> clientBehaviors = jsFunctionCallback.getClientBehaviors();
        
        ClientBehaviorContext cbcContext = ClientBehaviorContext
            .createClientBehaviorContext(facesContext, jsFunctionCallback,
                jsFunctionCallback.getEventName(), jsFunctionCallback.getClientId(facesContext),
                null);

        ScriptContext script = new ScriptContext(
                MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isPrettyHtml());
        
        script.append(functionName);
        script.append(" = ");
        script.append(" function(");
        if (jsFunctionCallback.getArguments() != null)
        {
            script.append(jsFunctionCallback.getArguments());
        }
        script.append(')');
        script.append('{');
        
        if (jsFunctionCallback.getJsStartContent() != null)
        {
            script.append(jsFunctionCallback.getJsStartContent());
        }
        
        if (jsFunctionCallback.getEventName() != null)
        {
            List<ClientBehavior> cbList = clientBehaviors.get(jsFunctionCallback.getEventName());
            if (cbList != null)
            {
                for (ClientBehavior cb : cbList)
                {
                    script.append(cb.getScript(cbcContext));
                    script.append(';');
                }
            }
        }
        
        if (jsFunctionCallback.getJsEndContent() != null)
        {
            script.append(jsFunctionCallback.getJsEndContent());
        }
        
        script.append('}');
        
        writer.writeText(script.toString(), null);
        
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }
}
