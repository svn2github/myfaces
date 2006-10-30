package org.apache.myfaces.examples.inputSuggestAjax;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.List;

/**
 * @author Gerald Müllan
 *         Date: 24.10.2006
 *         Time: 01:18:42
 */
public class InputSuggestAjaxConverter 
        implements Converter
{
    public Object getAsObject(FacesContext context,
                              UIComponent component,
                              String value) throws ConverterException
    {
        List addresses = InputSuggestAjaxBean.dummyDataBaseAddresses;

        if (value != null)
        {
            Integer newValue = new Integer(value);

            for (int i = 0; i < addresses.size(); i++)
            {
                Address o = (Address) addresses.get(i);
                if (o.getStreetNumber() == newValue.intValue())
                {
                    return o;
                }
            }
        }
        return null;
    }

    public String getAsString(FacesContext context,
                              UIComponent component,
                              Object value) throws ConverterException
    {
        if (value instanceof Address)
        {
            Address address = (Address) value;

            return Integer.toString(address.getStreetNumber());
        }
        else if (value instanceof String)
        {
            return (String) value;
        }
        else return null;
    }
}
