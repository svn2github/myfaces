package org.apache.myfaces.custom.table;

import javax.faces.component.UIColumn;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Spiegl
 */
public class SortableColumn extends UIColumn {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.SortableColumn";

    private String _field;
    private String _dataType;
    private String _sort;
    private String _format;
    private String _align;
    private String _valign;
    
    public String getField() {
        if (_field != null) return _field;
        ValueBinding vb = getValueBinding("field");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setField(String field) {
        _field = field;
    }

    public String getDataType() {
        if (_dataType != null) return _dataType;
        ValueBinding vb = getValueBinding("dataType");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setDataType(String dataType) {
        _dataType = dataType;
    }

    public String getSort() {
        if (_sort != null) return _sort;
        ValueBinding vb = getValueBinding("sort");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSort(String sort) {
        _sort = sort;
    }

    public String getFormat() {
        if (_format != null) return _format;
        ValueBinding vb = getValueBinding("format");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFormat(String format) {
        _format = format;
    }

    public String getAlign() {
        if (_align != null) return _align;
        ValueBinding vb = getValueBinding("align");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setAlign(String align) {
        _align = align;
    }

    public String getValign() {
        if (_valign != null) return _valign;
        ValueBinding vb = getValueBinding("valign");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setValign(String valign) {
        _valign = valign;
    }

    public Object saveState(FacesContext context) {
        Object[] obj = new Object[7];
        obj[0] = super.saveState(context);
        obj[1] = _field;
        obj[2] = _dataType;
        obj[3] = _sort;
        obj[4] = _format;
        obj[5] = _align;
        obj[6] = _valign;
        return obj;
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] obj = (Object[]) state;
        super.restoreState(context, obj[0]);
        _field = (String) obj[1];
        _dataType = (String) obj[2];
        _sort = (String) obj[3];
        _format = (String) obj[3];
        _align = (String) obj[3];
        _valign = (String) obj[3];
    }
}
