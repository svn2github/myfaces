package org.apache.myfaces.custom.requestParameterProvider;

/**
 * @author Thomas Oberder
 * @author Mario Ivankovits
 * @version 27.04.2006 22:41:11
 */
public interface RequestParameterProvider
{
    public String[] getFields();
    public String   getFieldValue(String field);
}
