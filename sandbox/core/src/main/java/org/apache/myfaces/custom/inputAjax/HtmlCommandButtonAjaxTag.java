package org.apache.myfaces.custom.inputAjax;

import org.apache.myfaces.taglib.html.ext.HtmlCommandButtonTag;

import javax.faces.component.UIComponent;

/**
 * User: Travis Reeder
 * Date: Mar 22, 2006
 * Time: 4:37:07 PM
 */
public class HtmlCommandButtonAjaxTag extends HtmlCommandButtonTag
{
    public String getComponentType()
    {
        return HtmlCommandButtonAjax.COMPONENT_TYPE;
    }

    /**
     * @return the RendererType String
     */
    public String getRendererType()
    {
        return HtmlCommandButtonAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
    }


}
