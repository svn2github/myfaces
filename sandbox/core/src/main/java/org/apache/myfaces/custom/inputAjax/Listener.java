package org.apache.myfaces.custom.inputAjax;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * On is the id of the component you want to listen on.
 * eventType is what happened to the component, for instance "onChange"
 * action is what to do if the event occurs, default is "update".  Will be able to call arbitrary javascript functions too.
 *
 * NOTE: eventType and action are NOT implemented yet
 *
 * User: Travis Reeder
 * Date: Apr 5, 2006
 * Time: 4:33:10 PM
 */
public class Listener extends UIComponentBase
{
    public static final String FAMILY = "org.apache.myfaces.Listener";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Listener";
    public static final String LISTENER_MAP_ENTRY = "org.apache.myfaces.Listener";

    private String _on;
    private String _eventType;
    private String _action;

    public Listener()
    {
    }

    public boolean isRendered()
    {
        return super.isRendered();
    }

    public String getFamily()
    {
        return FAMILY;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _on;
        values[2] = _eventType;
        values[3] = _action;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _on = (String) values[1];
        _eventType = (String) values[2];
        _action = (String) values[3];
    }

    public String getOn()
    {
        return _on;
    }

    public void setOn(String on)
    {
        _on = on;
    }

    public String getEventType()
    {
        return _eventType;
    }

    public void setEventType(String eventType)
    {
        _eventType = eventType;
    }

    public String getAction()
    {
        return _action;
    }

    public void setAction(String action)
    {
        _action = action;
    }
}
