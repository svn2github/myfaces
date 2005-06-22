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
package org.apache.myfaces.renderkit.html.ext;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.DisplayValueOnlyCapable;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.renderkit.html.HtmlTextRendererBase;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTextRenderer
        extends HtmlTextRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        if(HtmlRendererUtils.isDisplayValueOnly(component))
        {
            renderInputValueOnly(facesContext, component);
        }
        else
        {
            renderNormal(facesContext, component);
        }
    }

    protected void renderInputValueOnly(FacesContext facesContext, UIComponent component)
        throws IOException
    {
            HtmlRendererUtils.renderDisplayValueOnly(facesContext,
                    (UIInput) component);
    }

    protected void renderNormal(FacesContext facesContext, UIComponent component)
        throws IOException
    {
            super.encodeEnd(facesContext, component);
    }
}
