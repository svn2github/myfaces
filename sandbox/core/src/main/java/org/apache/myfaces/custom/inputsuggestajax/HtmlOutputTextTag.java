package org.apache.myfaces.custom.inputsuggestajax;

import org.apache.myfaces.renderkit.JSFAttr;
import javax.faces.component.UIComponent;

/**
 * @author Gerald Müllan
 *         Date: 15.02.2006
 *         Time: 13:40:43
 */
public class HtmlOutputTextTag extends org.apache.myfaces.taglib.html.HtmlOutputTextTag
{
    private String _for;
    private String _label;

    public String getComponentType() {
        return HtmlOutputText.COMPONENT_TYPE;
    }

    public void release() {

        super.release();

        _for = null;
        _label = null;
    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        setStringProperty(component, JSFAttr.FOR_ATTR, _for);
        setStringProperty(component, "label", _label);
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setLabel(String label)
    {
        _label = label;
    }
}
