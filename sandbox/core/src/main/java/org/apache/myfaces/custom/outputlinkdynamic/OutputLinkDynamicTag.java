/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.outputlinkdynamic;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.html.HtmlOutputLinkTag;

/**
 * @author Sylvain Vieujot, Mathias Br&ouml;kelmann
 * @version $Revision$ $Date$
 */

public class OutputLinkDynamicTag extends HtmlOutputLinkTag
{
    private String _resourceRendererClass;

    public String getComponentType()
    {
        return "org.apache.myfaces.OutputLinkDynamic";
    }

    public String getRendererType()
    {
        return OutputLinkDynamicRenderer.RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
        _resourceRendererClass = null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "resourceRendererClass", _resourceRendererClass);
    }

    public void setResourceRendererClass(String resourceRendererClass)
    {
        _resourceRendererClass = resourceRendererClass;
    }
}