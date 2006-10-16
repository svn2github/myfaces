package org.apache.myfaces.custom.fisheye;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * JSP Tag for the FishEyeList component
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FishEyeCommandLinkTag extends UIComponentTagBase
{
    private static final String CAPTION_ATTR = "caption";
    private static final String ICONSRC_ATTR = "iconSrc";
    private static final String TARGET_ATTR = "target";

    private String _caption;
    private String _iconSrc;
    private String _target;
    private String _action;
    private String _actionListener;
    private String _immediate;

    public String getComponentType() {
        return FishEyeCommandLink.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return FishEyeCommandLink.RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setStringProperty(component, CAPTION_ATTR, _caption);
        setStringProperty(component, ICONSRC_ATTR, _iconSrc);
        setStringProperty(component, TARGET_ATTR, _target);
        setActionProperty(component, _action);
        setActionListenerProperty(component, _actionListener);
        setBooleanProperty(component, org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr.IMMEDIATE_ATTR, _immediate);
    }

    public void release() {
        super.release();
        _caption = null;
        _iconSrc = null;
        _target = null;
        _action = null;
        _actionListener = null;
        _immediate = null;
    }

    public void setCaption(String caption) {
        _caption = caption;
    }

    public void setIconSrc(String iconSrc) {
        _iconSrc = iconSrc;
    }

    public void setTarget(String target) {
        _target = target;
    }

    public void setAction(String action) {
        _action = action;
    }

    public void setActionListener(String actionListener) {
        _actionListener = actionListener;
    }

    public void setImmediate(String immediate) {
        _immediate = immediate;
    }
}
