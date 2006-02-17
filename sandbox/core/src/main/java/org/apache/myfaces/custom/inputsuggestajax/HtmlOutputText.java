package org.apache.myfaces.custom.inputsuggestajax;


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Gerald Müllan
 *         Date: 15.02.2006
 *         Time: 13:30:43
 */
public class HtmlOutputText extends javax.faces.component.html.HtmlOutputText
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlOutputTextFor";

    private String _for;

    public HtmlOutputText()
    {
    }

     public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _for;

        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
    }

    public String getFor()
    {
         if (_for != null)
            return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }
}
