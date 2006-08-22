package org.apache.myfaces.custom.ppr;

import org.apache.myfaces.component.html.ext.HtmlPanelGroup;

import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 20.06.2006
 * Time: 03:30:13
 * To change this template use File | Settings | File Templates.
 */
public class PPRPanelGroup extends HtmlPanelGroup
{
    private String _partialTriggers;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.PPRPanelGroup";

    public static final String COMPONENT_FAMILY = "org.apache.myfaces.PPRPanelGroup";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PPRPanelGroup";

    public PPRPanelGroup()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public String getPartialTriggers()
    {
        return _partialTriggers;
    }

    public void setPartialTriggers(String partialTriggers)
    {
        this._partialTriggers = partialTriggers;
    }

    public void restoreState(FacesContext context, Object state)
    {

        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        _partialTriggers = (String) values[1];

    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _partialTriggers;
        return values;
    }
}
