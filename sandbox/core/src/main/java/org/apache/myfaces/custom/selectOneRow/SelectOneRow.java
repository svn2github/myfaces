package org.apache.myfaces.custom.selectOneRow;

import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 01.02.2006
 * Time: 15:55:59
 * To change this template use File | Settings | File Templates.
 */
public class SelectOneRow extends UIInput
{
    private String groupName;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.SelectOneRow";

    public static final String COMPONENT_FAMILY = "org.apache.myfaces.SelectOneRow";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SelectOneRow";

    public SelectOneRow()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public void restoreState(FacesContext context, Object state)
    {

        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        groupName = (String) values[1];

    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = groupName;
        return values;
    }

}
