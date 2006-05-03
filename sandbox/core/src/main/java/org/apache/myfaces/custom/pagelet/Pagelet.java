package org.apache.myfaces.custom.pagelet;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.component.DisplayValueOnlyCapable;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 * @author Thomas Spiegl
 * minor additions and fixes done by Werner Punz
 *
 */
public class Pagelet extends HtmlInputTextarea implements DisplayValueOnlyCapable {

    //allowed control modes
    public static final String   CONTROL_MODE_TEXTAREA         = "textarea";
    public static final String   CONTROL_MODE_PAGELET          = "pagelet";
    public static final String   COMTROL_MODE_PAGELET_RAWTEXT  = "pagelet-rawtext";
    public static final String   CONTROL_MODE_PAGELET_TEXTAREA = "pagelet-richtext";
    public static final String   COMPONENT_TYPE                = "org.apache.myfaces.Pagelet";
    public static final String   DEFAULT_RENDERER_TYPE         = "org.apache.myfaces.Pagelet";
    private static final String  LINK_SPELLCHECKER_FACET_NAME  = "linkSpellchecker";
    private static final String  LINK_RESUME_FACET_NAME        = "linkResume";
    private static final String  LINK_ZOOM_FACET_NAME          = "linkZoom";
    private static final String  LINK_RESIZE_FACET_NAME        = "linkResize";
    private static final String  LINK_DOWNSIZE_FACET_NAME      = "linkDownsize";
    private static final String  LINK_POPUP_LABEL_FACET_NAME   = "popupLabel";
    private static final boolean DEFAULT_DISPLAYVALUEONLY      = false;

    private String        _controlMode;
    private Boolean       _displayValueOnly           = null;
    private String        _displayValueOnlyStyle      = null;
    private String        _displayValueOnlyStyleClass = null;
    private Integer       _height;
    private MethodBinding _spellchecker;
    private String        _textChecking;
    private String        _textNoMisspellings;
    private String        _textResume;
    private String        _textSpellcheck;
    private String        _textWorking;
    private Integer       _width;
    private Integer       _zoomHeight;
    private Integer       _zoomWidth;

    public Pagelet() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getControlMode() {
        return _controlMode;
    }

    public String getDisplayValueOnlyStyle() {

        if (_displayValueOnlyStyle != null)
            return _displayValueOnlyStyle;

        ValueBinding vb = getValueBinding("displayValueOnlyStyle");

        return (vb != null) ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public String getDisplayValueOnlyStyleClass() {

        if (_displayValueOnlyStyleClass != null)
            return _displayValueOnlyStyleClass;

        ValueBinding vb = getValueBinding("displayValueOnlyStyleClass");

        return (vb != null) ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public int getHeight() {

        if (_height != null)
            return _height.intValue();

        ValueBinding vb = getValueBinding("height");
        Number       v  = (vb != null) ? (Number) vb.getValue(getFacesContext()) : null;

        return (v != null) ? v.intValue() : 0;
    }

    public UIComponent getLinkDownsize() {
        return (UIComponent) getFacets().get(LINK_DOWNSIZE_FACET_NAME);
    }

    public UIComponent getLinkResize() {
        return (UIComponent) getFacets().get(LINK_RESIZE_FACET_NAME);
    }

    public UIComponent getLinkResume() {
        return (UIComponent) getFacets().get(LINK_RESUME_FACET_NAME);
    }

    public UIComponent getLinkSpellchecker() {
        return (UIComponent) getFacets().get(LINK_SPELLCHECKER_FACET_NAME);
    }

    public UIComponent getLinkZoom() {
        return (UIComponent) getFacets().get(LINK_ZOOM_FACET_NAME);
    }

    public UIComponent getLinkPopupLabel() {
    	 return (UIComponent) getFacets().get(LINK_POPUP_LABEL_FACET_NAME);
    }
    
    public MethodBinding getSpellchecker() {
        return _spellchecker;
    }

    public String getTextChecking() {

        if (_textChecking != null)
            return _textChecking;

        ValueBinding vb = getValueBinding("textChecking");

        return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getTextNoMisspellings() {

        if (_textNoMisspellings != null)
            return _textNoMisspellings;

        ValueBinding vb = getValueBinding("textNoError");

        return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getTextResume() {

        if (_textResume != null)
            return _textResume;

        ValueBinding vb = getValueBinding("textResume");

        return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getTextSpellcheck() {

        if (_textSpellcheck != null)
            return _textSpellcheck;

        ValueBinding vb = getValueBinding("textSpellcheck");

        return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getTextWorking() {

        if (_textWorking != null)
            return _textWorking;

        ValueBinding vb = getValueBinding("textError");

        return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
    }

    public int getWidth() {

        if (_width != null)
            return _width.intValue();

        ValueBinding vb = getValueBinding("width");
        Number       v  = (vb != null) ? (Number) vb.getValue(getFacesContext()) : null;

        return (v != null) ? v.intValue() : 0;
    }

    public int getZoomHeight() {

        if (_zoomHeight != null)
            return _zoomHeight.intValue();

        ValueBinding vb = getValueBinding("zoomHeight");
        Number       v  = (vb != null) ? (Number) vb.getValue(getFacesContext()) : null;

        return (v != null) ? v.intValue() : 0;
    }

    public int getZoomWidth() {

        if (_zoomWidth != null)
            return _zoomWidth.intValue();

        ValueBinding vb = getValueBinding("zoomWidth");
        Number       v  = (vb != null) ? (Number) vb.getValue(getFacesContext()) : null;

        return (v != null) ? v.intValue() : 0;
    }

    public boolean isDisplayValueOnly() {

        if (_displayValueOnly != null)
            return _displayValueOnly.booleanValue();

        ValueBinding vb = getValueBinding("displayValueOnly");
        Boolean      v  = (vb != null) ? (Boolean) vb.getValue(getFacesContext()) : null;

        return (v != null) ? v.booleanValue() : DEFAULT_DISPLAYVALUEONLY;
    }

    public boolean isSetDisplayValueOnly() {

        if (_displayValueOnly != null)
            return true;

        ValueBinding vb = getValueBinding("displayValueOnly");
        Boolean      v  = (vb != null) ? (Boolean) vb.getValue(getFacesContext()) : null;

        return v != null;
    }

    public void restoreState(FacesContext facesContext, Object object) {
        Object[] values = (Object[]) object;
        super.restoreState(facesContext, values[0]);
        _width                      = (Integer) values[1];
        _height                     = (Integer) values[2];
        _textSpellcheck             = (String) values[3];
        _textResume                 = (String) values[4];
        _textWorking                = (String) values[5];
        _textNoMisspellings         = (String) values[6];
        _zoomWidth                  = (Integer) values[7];
        _zoomHeight                 = (Integer) values[8];
        _spellchecker               = (MethodBinding) restoreAttachedState(facesContext, values[9]);
        _displayValueOnly           = (Boolean) values[10];
        _displayValueOnlyStyle      = (String) values[11];
        _displayValueOnlyStyleClass = (String) values[12];
        _controlMode                = (String) values[13];
    }

    public Object saveState(FacesContext facesContext) {
        Object[] values = new Object[14];
        values[0]  = super.saveState(facesContext);
        values[1]  = _width;
        values[2]  = _height;
        values[3]  = _textSpellcheck;
        values[4]  = _textResume;
        values[5]  = _textWorking;
        values[6]  = _textNoMisspellings;
        values[7]  = _zoomWidth;
        values[8]  = _zoomHeight;
        values[9]  = saveAttachedState(facesContext, _spellchecker);
        values[10] = _displayValueOnly;
        values[11] = _displayValueOnlyStyle;
        values[12] = _displayValueOnlyStyleClass;
        values[13] = _controlMode;

        return values;
    }

    public void setControlMode(String mode) {
        _controlMode = mode;
    }

    public void setDisplayValueOnly(boolean displayValueOnly) {
        _displayValueOnly = Boolean.valueOf(displayValueOnly);
    }

    public void setDisplayValueOnlyStyle(String displayValueOnlyStyle) {
        _displayValueOnlyStyle = displayValueOnlyStyle;
    }

    public void setDisplayValueOnlyStyleClass(String displayValueOnlyStyleClass) {
        _displayValueOnlyStyleClass = displayValueOnlyStyleClass;
    }

    public void setHeight(int height) {
        _height = new Integer(height);
    }

    public void setLinkDownsize(UIComponent first) {
        getFacets().put(LINK_DOWNSIZE_FACET_NAME, first);
    }

    public void setLinkResize(UIComponent first) {
        getFacets().put(LINK_RESIZE_FACET_NAME, first);
    }

    public void setLinkResume(UIComponent first) {
        getFacets().put(LINK_RESUME_FACET_NAME, first);
    }

    public void setLinkSpellchecker(UIComponent first) {
        getFacets().put(LINK_SPELLCHECKER_FACET_NAME, first);
    }

    public void setSpellchecker(MethodBinding spellchecker) {
        _spellchecker = spellchecker;
    }

    public void setTextChecking(String textChecking) {
        _textChecking = textChecking;
    }

    public void setTextNoMisspellings(String textNoMisspellings) {
        _textNoMisspellings = textNoMisspellings;
    }

    public void setTextResume(String textResume) {
        _textResume = textResume;
    }

    public void setTextSpellcheck(String textSpellcheck) {
        _textSpellcheck = textSpellcheck;
    }

    public void setTextWorking(String textWorking) {
        _textWorking = textWorking;
    }

    public void setWidth(int width) {
        _width = new Integer(width);
    }

    public void setZoomHeight(int zoomHeight) {
        this._zoomHeight = new Integer(zoomHeight);
    }

    public void setZoomWidth(int zoomWidth) {
        this._zoomWidth = new Integer(zoomWidth);
    }

}
