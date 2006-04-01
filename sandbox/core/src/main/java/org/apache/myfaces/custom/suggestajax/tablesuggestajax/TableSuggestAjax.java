package org.apache.myfaces.custom.suggestajax.tablesuggestajax;

import org.apache.myfaces.custom.suggestajax.SuggestAjax;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

/**
 * @author Gerald Müllan
 *         Date: 25.03.2006
 *         Time: 17:04:58
 */
public class TableSuggestAjax extends SuggestAjax
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.TableSuggestAjax";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.TableSuggestAjax";

    private String _tableStyleClass;
    private String _nextPageFieldClass;

    private String _columnHoverClass;
    private String _columnOutClass;

    private Integer _betweenKeyUp;
    private Integer _startRequest;

    private String _var;

    public TableSuggestAjax()
    {
        super();

        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[8];
        values[0] = super.saveState(context);
        values[1] = _var;
        values[2] = _columnHoverClass;
        values[3] = _columnOutClass;
        values[4] = _betweenKeyUp;
        values[5] = _startRequest;
        values[6] = _tableStyleClass;
        values[7] = _nextPageFieldClass;

        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _var = (String) values[1];
        _columnHoverClass = (String) values[2];
        _columnOutClass = (String) values[3];
        _betweenKeyUp = (Integer) values[4];
        _startRequest = (Integer) values[5];
        _tableStyleClass = (String) values[6];
        _nextPageFieldClass = (String) values[7];
    }

    public boolean getRendersChildren()
    {
        if(getVar()!=null)
            return true;
        else
            return super.getRendersChildren();
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        super.encodeChildren(context);
    }

    public Integer getBetweenKeyUp()
    {
        if (_betweenKeyUp != null)
            return _betweenKeyUp;
        ValueBinding vb = getValueBinding("delay");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setBetweenKeyUp(Integer betweenKeyUp)
    {
        _betweenKeyUp = betweenKeyUp;
    }

    public Integer getStartRequest()
    {
        if (_startRequest != null)
            return _startRequest;
        ValueBinding vb = getValueBinding("startRequest");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setStartRequest(Integer startRequest)
    {
        _startRequest = startRequest;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public String getVar()
    {
        if (_var != null) return _var;
        ValueBinding vb = getValueBinding("var");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public String getColumnHoverClass()
    {
        return _columnHoverClass;
    }

    public void setColumnHoverClass(String columnHoverClass)
    {
        _columnHoverClass = columnHoverClass;
    }

    public String getColumnOutClass()
    {
        return _columnOutClass;
    }

    public void setColumnOutClass(String columnOutClass)
    {
        _columnOutClass = columnOutClass;
    }

    public String getTableStyleClass()
    {
        return _tableStyleClass;
    }

    public void setTableStyleClass(String tableStyleClass)
    {
        _tableStyleClass = tableStyleClass;
    }

    public String getNextPageFieldClass()
    {
        return _nextPageFieldClass;
    }

    public void setNextPageFieldClass(String nextPageFieldClass)
    {
        _nextPageFieldClass = nextPageFieldClass;
    }
}