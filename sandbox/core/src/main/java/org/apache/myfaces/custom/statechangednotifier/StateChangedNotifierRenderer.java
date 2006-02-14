/**
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.statechangednotifier;

import org.apache.myfaces.renderkit.html.HtmlHiddenRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.dojo.DojoConfig;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import java.io.IOException;

/**
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateChangedNotifierRenderer extends HtmlHiddenRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent,
                StateChangedNotifier.class);

        StateChangedNotifier notifier = (StateChangedNotifier) uiComponent;

        if (notifier.getDisabled() != null)
        {
            if (notifier.getDisabled().booleanValue())
                return;
        }

        String javascriptLocation = (String) notifier.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        DojoUtils.addMainInclude(facesContext, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(facesContext, "dojo.event.*");
        DojoUtils.addRequire(facesContext, "dojo.xml.Parse");

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN,
                StateChangedNotifierRenderer.class, "stateChangedNotifier.js");

        encodeJavascript(facesContext, notifier);

        super.encodeEnd(facesContext, uiComponent);
    }

    private void encodeJavascript(FacesContext facesContext, StateChangedNotifier notifier)
    {
        String notifierClientId = notifier.getClientId(facesContext);

        String replacedClientId = notifierClientId.replaceAll(":", "_");
        String initFunctionName = "init_"+replacedClientId;

        UIForm form = getParentForm(notifier);
        String formId = form.getClientId(facesContext);

        StringBuffer sb = new StringBuffer();
        sb.append("<!--\n");
        sb.append("dojo.addOnLoad(window, '"+initFunctionName+"');\n");
        sb.append("function "+initFunctionName+ "() {\n");
        sb.append(replacedClientId+"Notifier = new org.apache.myfaces.StateChangedNotifier('"+formId+"','"+notifierClientId+"','"+notifier.getConfirmationMessage()+"',");

        String excludedCommandIds = notifier.getExcludedIds();
        if (excludedCommandIds != null)
        {
            sb.append("'"+excludedCommandIds+"');\n");
        }
        else
        {
            sb.append("'');\n");
        }

        sb.append(replacedClientId+"Notifier.prepareNotifier();\n");

        sb.append("}\n");
        sb.append("-->\n");

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN,
                sb.toString());
    }

    /**
     * Get the parent UIForm. If no parent is a UIForm then returns null.
     *
     * @param component
     * @return UIForm
     */
    private UIForm getParentForm(UIComponent component) {
        // See if we are in a form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }
}
