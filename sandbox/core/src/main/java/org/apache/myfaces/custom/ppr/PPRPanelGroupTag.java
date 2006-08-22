package org.apache.myfaces.custom.ppr;

import org.apache.myfaces.taglib.html.ext.HtmlPanelGroupTag;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 20.06.2006
 * Time: 03:25:46
 * To change this template use File | Settings | File Templates.
 */
public class PPRPanelGroupTag extends HtmlPanelGroupTag
{
    private String _partialTriggers;

    public String getComponentType()
    {
        return PPRPanelGroup.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return PPRPanelGroup.DEFAULT_RENDERER_TYPE;
    }


    public void release()
    {
        super.release();
        _partialTriggers = null;
    }
     protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, "partialTriggers", _partialTriggers);
    }

    public String getPartialTriggers()
    {
        return _partialTriggers;
    }

    public void setPartialTriggers(String partialTriggers)
    {
        this._partialTriggers = partialTriggers;
    }

}
