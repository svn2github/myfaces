/*
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
package org.apache.myfaces.custom.tree.renderkit.html;

import org.apache.myfaces.custom.tree.HtmlTreeNode;
import org.apache.myfaces.renderkit.html.HtmlLinkRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 */
public class HtmlTreeNodeRenderer
        extends HtmlLinkRendererBase
{


    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
        String clientId = component.getClientId(facesContext);
        String reqValue = (String)facesContext.getExternalContext().getRequestParameterMap().get(HtmlRendererUtils.getHiddenCommandLinkFieldName(HtmlRendererUtils.getFormName(component, facesContext)));
        if (reqValue != null && reqValue.equals(clientId))
        {
            HtmlTreeNode node = (HtmlTreeNode)component;

            node.setSelected(true);
        }
    }
}
