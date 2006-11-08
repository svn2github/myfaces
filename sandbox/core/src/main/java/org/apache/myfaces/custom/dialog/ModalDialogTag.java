package org.apache.myfaces.custom.dialog;

import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Obereder
 * @version 02.09.2006 12:04:26
 */
public class ModalDialogTag extends HtmlComponentTagBase {
    private static final String TAG_PARAM_DIALOG_ATTR = "dialogAttr";

    private static final String TAG_PARAM_DIALOG_ID   = "dialogId";

    private static final String TAG_PARAM_DIALOG_VAR  = "dialogVar";

    private static final String TAG_PARAM_HIDER_IDS   = "hiderIds";

    public static final String  TAG_PARAM_WidgetId    = "widgetId";

    public static final String  TAG_PARAM_WidgetVar   = "widgetVar";

    private String              _dialogAttr;

    private String              _dialogId;

    private String              _dialogVar;

    private String              _hiderIds;

    private String              _widgetId             = null;

    private String              _widgetVar            = null;

    public String getComponentType() {
        return ModalDialog.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return ModalDialogRenderer.RENDERER_TYPE;
    }

    public void release() {
        super.release();
        _dialogVar = null;
        _dialogId = null;
        _dialogAttr = null;
        _hiderIds = null;
        _widgetVar = null;
        _widgetId = null;

    }

    public void setDialogAttr(String dialogAttr) {
        _dialogAttr = dialogAttr;
    }

    public void setDialogId(String dialogId) {
        _dialogId = dialogId;
    }

    public void setDialogVar(String dialogVar) {
        _dialogVar = dialogVar;
    }

    public void setHiderIds(String hiderIds) {
        _hiderIds = hiderIds;
    }

    public void setWidgetId(String widgetId) {
        _widgetId = widgetId;
    }

    public void setWidgetVar(String widgetVar) {
        _widgetVar = widgetVar;
    }

    
    protected void setProperties(UIComponent uiComponent) {
        super.setProperties(uiComponent);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_VAR, _dialogVar);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_ID, _dialogId);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_ATTR, _dialogAttr);
        super.setStringProperty(uiComponent, TAG_PARAM_HIDER_IDS, _hiderIds);
        super.setStringProperty(uiComponent, TAG_PARAM_WidgetVar, _widgetVar);
        super.setStringProperty(uiComponent, TAG_PARAM_WidgetId, _widgetId);

    }

}
