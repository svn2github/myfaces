package org.apache.myfaces.custom.dojolayouts;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlOutputTextTagBase;

public class DojoContentPaneTag extends HtmlOutputTextTagBase {

    private static final String TAG_PARAM_STYLE_CLASS = "styleClass";

    private static final String TAG_PARAM_STYLE = "style";

    public static String TAG_PARAM_SIZESHARE = "sizeShare";

    private String       _sizeShare          = null;
    private String       _style              = null;
    private String       _styleClass         = null;

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        super.setIntegerProperty(component, TAG_PARAM_SIZESHARE, _sizeShare);
        super.setStringProperty(component, TAG_PARAM_STYLE, _style);
        super.setStringProperty(component, TAG_PARAM_STYLE_CLASS, _styleClass);
    }

    
    
    public void release() {
        super.release();
        _style = null;
        _styleClass = null;
        _sizeShare = null;
    }



    public void setSizeShare(String sizeShare) {
        this._sizeShare = sizeShare;
    }

    public String getRendererType() {
        return DojoContentPane.DEFAULT_RENDERER_TYPE;
    }

    public String getComponentType() {
        return DojoContentPane.DEFAULT_COMPONENT_TYPE;
    }

    public void setStyle(String style) {
        this._style = style;
    }

    public void setStyleClass(String styleClass) {
        this._styleClass = styleClass;
    }

}
