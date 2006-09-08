package org.apache.myfaces.custom.dialog;


import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Obereder
 * @version 02.09.2006 12:04:26
 */
public class DojoDialogTag extends HtmlComponentTagBase
{
    private static final String TAG_PARAM_DIALOG_VAR = "dialogVar";
    private static final String TAG_PARAM_DIALOG_ID = "dialogId";
    private static final String TAG_PARAM_DIALOG_ATTR = "dialogAttr";
    private static final String TAG_PARAM_HIDER_IDS = "hiderIds";

    private String _dialogVar;
    private String _dialogId;
    private String _dialogAttr;
    private String _hiderIds;

    public String getComponentType()
    {
        return DojoDialog.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return DojoDialogRenderer.RENDERER_TYPE;
    }

    public void setDialogVar(String dialogVar)
    {
        _dialogVar = dialogVar;
    }

    public void setDialogId(String dialogId)
    {
        _dialogId = dialogId;
    }

    public void setDialogAttr(String dialogAttr)
    {
        _dialogAttr = dialogAttr;
    }

    public void setHiderIds(String hiderIds)
    {
        _hiderIds = hiderIds;
    }

    protected void setProperties(UIComponent uiComponent)
    {
        super.setProperties(uiComponent);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_VAR, _dialogVar);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_ID, _dialogId);
        super.setStringProperty(uiComponent, TAG_PARAM_DIALOG_ATTR, _dialogAttr);
        super.setStringProperty(uiComponent, TAG_PARAM_HIDER_IDS, _hiderIds);
    }

    public void release()
    {
        super.release();
        _dialogVar = null;
        _dialogId = null;
        _dialogAttr = null;
        _hiderIds = null;
    }
}
