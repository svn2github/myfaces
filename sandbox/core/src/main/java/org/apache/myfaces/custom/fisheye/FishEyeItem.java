package org.apache.myfaces.custom.fisheye;

/**
 * @author Thomas Spiegl
 */
public class FishEyeItem {
    private String _caption;
    private String _iconSrc;
    private String _target;

    public FishEyeItem(String caption, String iconSrc) {
        _caption = caption;
        _iconSrc = iconSrc;
    }

    public FishEyeItem(String caption, String iconSrc, String target) {
        _caption = caption;
        _iconSrc = iconSrc;
        _target = target;
    }

    public String getCaption() {
        return _caption;
    }

    public void setCaption(String caption) {
        _caption = caption;
    }

    public String getIconSrc() {
        return _iconSrc;
    }

    public void setIconSrc(String iconSrc) {
        _iconSrc = iconSrc;
    }

    public String getTarget() {
        return _target;
    }

    public void setTarget(String target) {
        _target = target;
    }
}
