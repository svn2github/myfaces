package org.apache.myfaces.custom.convertString;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Useful when a char(5) field ends up rendered as "abc  " .
 * 
 * @author Dennis Byrne
 */

public class TrimConverter implements Converter
{
    
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) throws ConverterException
    {
        return value == null ? null : value;
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) throws ConverterException
    {
        return value == null ? "" : value.toString().trim();
    }

}
