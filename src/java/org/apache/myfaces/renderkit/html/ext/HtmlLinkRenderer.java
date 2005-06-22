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
import org.apache.myfaces.renderkit.html.HtmlLinkRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlLinkRenderer
        extends HtmlLinkRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlLinkRenderer.class);

    protected void renderCommandLinkStart(FacesContext facesContext,
                                          UIComponent component,
                                          String clientId,
                                          Object value,
                                          String style,
                                          String styleClass) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(component))
        {
            super.renderCommandLinkStart(facesContext, component, clientId, value, style, styleClass);
        }
    }

    protected void renderOutputLinkStart(FacesContext facesContext, UIOutput output) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(output))
        {
            super.renderOutputLinkStart(facesContext, output);
        }
    }

    protected void renderLinkEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(component))
        {
            super.renderLinkEnd(facesContext, component);
        }
    }

}
