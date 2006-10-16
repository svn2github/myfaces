package org.apache.myfaces.custom.fisheye;

import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

import javax.faces.component.UICommand;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Spiegl
 */
public class FishEyeCommandLink extends UICommand {
    private String _caption;
    private String _iconSrc;
    private String _target;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.FishEyeCommandLink";
    public static final String RENDERER_TYPE  = "org.apache.myfaces.FishEyeCommandLink";

    public String getCaption() {
        if (_caption != null) return _caption;
        ValueBinding vb = getValueBinding("caption");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setCaption(String caption) {
        _caption = caption;
    }

    public String getIconSrc() {
        if (_iconSrc != null) return _iconSrc;
        ValueBinding vb = getValueBinding("iconSrc");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setIconSrc(String iconSrc) {
        _iconSrc = iconSrc;
    }

    public String getTarget() {
        if (_target != null) return _target;
        ValueBinding vb = getValueBinding("target");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setTarget(String target) {
        _target = target;
    }

    public Object saveState(FacesContext context) {
        Object[] state = new Object[4];
        state[0] = super.saveState(context);
        state[1] = _caption;
        state[2] = _iconSrc;
        state[3] = _target;
        return state;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _caption = (String)values[1];
        _iconSrc = (String)values[2];
        _target = (String)values[3];
    }
}
