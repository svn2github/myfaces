package org.apache.myfaces.custom.subform;

import javax.faces.webapp.UIComponentTag;

/**
 * @author Gerald Muellan
 *         Date: 19.01.2006
 *         Time: 14:01:07
 */
public class SubFormTag extends UIComponentTag
{
    public String getComponentType()
    {
        return SubForm.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return SubForm.DEFAULT_RENDERER_TYPE;
    }
}
