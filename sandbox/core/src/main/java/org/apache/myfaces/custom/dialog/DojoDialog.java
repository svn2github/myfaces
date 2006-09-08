package org.apache.myfaces.custom.dialog;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class DojoDialog extends UIPanel {

    public static final String COMPONENT_TYPE = "org.apache.myfaces.DojoDialog";

    private String _dialogVar;
    private String _dialogId;
    private String _dialogAttr;
    private String _hiderIds;

    private String _style;
    private String _styleClass;
    
    //@Override
	public boolean getRendersChildren() {
    	return true;
	}

	public String getDialogAttr() {
        if(_dialogAttr != null)
            return _dialogAttr;
        ValueBinding vb = getValueBinding("dialogAttr");
        return vb != null ? (String)vb.getValue(getFacesContext()) :null;
	}

	public void setDialogAttr(String dialogAttr) {
		_dialogAttr = dialogAttr;
	}

	public String getDialogId() {
        if(_dialogId != null)
            return _dialogId;
        ValueBinding vb = getValueBinding("dialogId");
        return vb != null ? (String)vb.getValue(getFacesContext()) :null;
	}

	public void setDialogId(String dialogId) {
		_dialogId = dialogId;
	}

	public String getDialogVar() {
        if(_dialogVar != null)
            return _dialogVar;
        ValueBinding vb = getValueBinding("dialogVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) :null;
	}

	public void setDialogVar(String dialogVar) {
		_dialogVar = dialogVar;
	}

	public String getHiderIds() {
        if(_hiderIds != null)
            return _hiderIds;
        ValueBinding vb = getValueBinding("hiderIds");
        return vb != null ? (String)vb.getValue(getFacesContext()) :null;
	}

	public void setHiderIds(String hiderIds) {
		_hiderIds = hiderIds;
	}

    public String getStyle() {
        if(_style != null)
            return _style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyle(String style) {
        _style = style;
    }

    public String getStyleClass() {
        if(_styleClass != null)
            return _styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyleClass(String styleClass) {
        _styleClass = styleClass;
    }

    public Object saveState(FacesContext facesContext) {
        Object[] values = new Object[7];
        values[0] = super.saveState(facesContext);
        values[1] = _dialogVar;
        values[2] = _dialogId;
        values[3] = _dialogAttr;
        values[4] = _hiderIds;
        values[5] = _style;
        values[6] = _styleClass;
        return values;
    }

    public void restoreState(FacesContext facesContext, Object object) {
        Object[] values = (Object[]) object;
        super.restoreState(facesContext, values[0]);
        _dialogVar = (String) values[1];
        _dialogId = (String) values[2];
        _dialogAttr = (String) values[3];
        _hiderIds = (String) values[4];
        _style = (String) values[5];
        _styleClass = (String) values[6];
    }
}
