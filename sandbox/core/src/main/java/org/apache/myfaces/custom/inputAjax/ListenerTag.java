package org.apache.myfaces.custom.inputAjax;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

import javax.servlet.jsp.tagext.Tag;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;

/**
 * User: Travis Reeder
 * Date: Apr 5, 2006
 * Time: 4:14:07 PM
 */
public class ListenerTag extends UIComponentTag
{
    private String _on;
    private String _eventType;
    private String _action;

    public void release()
    {
        super.release();
        _on = null;
        _eventType = null;
        _action = null;
    }

    public String getComponentType()
    {
        return Listener.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    protected void setProperties(UIComponent uiComponent)
    {
        super.setProperties(uiComponent);
        UIComponentTagUtils.setStringProperty(getFacesContext(), uiComponent, "on", _on);
        UIComponentTagUtils.setStringProperty(getFacesContext(), uiComponent, "eventType", _eventType);
        UIComponentTagUtils.setStringProperty(getFacesContext(), uiComponent, "action", _action);
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
