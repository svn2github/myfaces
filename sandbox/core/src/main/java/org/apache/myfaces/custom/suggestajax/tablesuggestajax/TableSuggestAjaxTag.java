package org.apache.myfaces.custom.suggestajax.tablesuggestajax;

import org.apache.myfaces.custom.suggestajax.SuggestAjaxTag;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

import javax.faces.component.UIComponent;

/**
 * @author Gerald Müllan
 *         Date: 25.03.2006
 *         Time: 17:05:25
 */
public class TableSuggestAjaxTag extends SuggestAjaxTag
{
    private String _tableStyleClass;
    private String _nextPageFieldClass;

    private String _columnHoverClass;
    private String _columnOutClass;

    private String _betweenKeyUp;
    private String _startRequest;

    private String _var;

    public String getComponentType() {
        return TableSuggestAjax.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return TableSuggestAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release() {

        super.release();

       _var = null;
       _columnHoverClass = null;
       _columnOutClass = null;
       _betweenKeyUp = null;
       _startRequest = null;
       _tableStyleClass = null;
       _nextPageFieldClass = null;

    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        setIntegerProperty(component,"betweenKeyUp",_betweenKeyUp);
        setIntegerProperty(component,"startRequest", _startRequest);

        setStringProperty(component,"columnHoverClass",_columnHoverClass);
        setStringProperty(component,"columnOutClass",_columnOutClass);
        setStringProperty(component,"tableStyleClass",_tableStyleClass);
        setStringProperty(component,"nextPageFieldClass",_nextPageFieldClass);

        setStringProperty(component, JSFAttr.VAR_ATTR, _var);
    }

    // setter methodes to populate the components properites
    public void setTableStyleClass(String tableStyleClass)
    {
        _tableStyleClass = tableStyleClass;
    }

    public void setBetweenKeyUp(String betweenKeyUp)
    {
        _betweenKeyUp = betweenKeyUp;
    }

    public void setStartRequest(String startRequest)
    {
        _startRequest = startRequest;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public void setColumnHoverClass(String columnHoverClass)
    {
        _columnHoverClass = columnHoverClass;
    }

    public void setColumnOutClass(String columnOutClass)
    {
        _columnOutClass = columnOutClass;
    }

    public void setNextPageFieldClass(String nextPageFieldClass)
    {
        _nextPageFieldClass = nextPageFieldClass;
    }
}
