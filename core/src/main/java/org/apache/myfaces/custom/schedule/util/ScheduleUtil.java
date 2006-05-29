/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.myfaces.custom.schedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * <p>
 * Some utility methods
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: jlust $)
 * @author Kito Mann (some methods taken from the source code of his book 'JavaServer Faces in Action', which is also released under the Apache License
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision: 387334 $
 */
public class ScheduleUtil
{
    //~ Class Variables --------------------------------------------------------
    private final static SimpleDateFormat DATE_ID_FORMATTER = new SimpleDateFormat("yyyyMMdd");
    
    //~ Methods ----------------------------------------------------------------

    /**
     * <p>
     * Check if the value of a UIComponent is a value or method binding
     * expression.
     * </p>
     *
     * @param value the value to check
     *
     * @return whether the value is a binding expression
     */
    public static boolean isBindingExpression(Object value)
    {
        boolean returnboolean =
            ((value != null) && value instanceof String &&
            ((String) value).startsWith("#{") &&
            ((String) value).endsWith("}"));

        return returnboolean;
    }

    /**
     * <p>
     * Get the boolean value of a UIComponent, even if it is a value
     * binding expression.
     * </p>
     *
     * @param component the component
     * @param property the property
     * @param key the key of the value binding
     * @param defaultValue the default value
     *
     * @return the boolean value
     */
    public static boolean getBooleanProperty(
        UIComponent component,
        Boolean property,
        String key,
        boolean defaultValue
    )
    {
        if (property != null) {
            return property.booleanValue();
        } else {
            ValueBinding binding =
                (ValueBinding) component.getValueBinding(key);

            if (binding != null) {
                Boolean value =
                    (Boolean) binding.getValue(
                        FacesContext.getCurrentInstance()
                    );

                if (value != null) {
                    return value.booleanValue();
                }
            }
        }

        return defaultValue;
    }

    /**
     * <p>
     * Get the boolean value of an attribute
     * </p>
     *
     * @param attributeValue the attribute value
     * @param valueIfNull the default value
     *
     * @return the boolean value
     */
    public static boolean getBooleanValue(
        Object attributeValue,
        boolean valueIfNull
    )
    {
        if (attributeValue == null) {
            return valueIfNull;
        }

        if (attributeValue instanceof String) {
            return ((String) attributeValue).equalsIgnoreCase("true");
        } else {
            return ((Boolean) attributeValue).booleanValue();
        }
    }

    /**
     * <p>
     * Get the float value of a UIComponent, even if it is a value
     * binding expression.
     * </p>
     *
     * @param component the component
     * @param property the property
     * @param key the key of the value binding
     * @param defaultValue the default value
     *
     * @return the float value
     */
    public static float getFloatProperty(
        UIComponent component,
        Float property,
        String key,
        float defaultValue
    )
    {
        if (property != null) {
            return property.floatValue();
        } else {
            ValueBinding binding =
                (ValueBinding) component.getValueBinding(key);

            if (binding != null) {
                Float value =
                    (Float) binding.getValue(FacesContext.getCurrentInstance());

                if (value != null) {
                    return value.floatValue();
                }
            }
        }

        return defaultValue;
    }

    /**
     * <p>
     * Get the hashcode for the truncated date
     * </p>
     *
     * @param date the date
     *
     * @return the hashCode of the truncated date
     */
    public static int getHashCodeForDay(Date date)
    {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	
    	return new Integer(calendar.get(Calendar.ERA)).hashCode() ^ 
    		new Integer(calendar.get(Calendar.YEAR)).hashCode() ^
    		new Integer(calendar.get(Calendar.DAY_OF_YEAR)).hashCode();
    }

    /**
     * <p>
     * Get the int value of a UIComponent, even if it is a value
     * binding expression.
     * </p>
     *
     * @param component the component
     * @param property the property
     * @param key the key of the value binding
     * @param defaultValue the default value
     *
     * @return the int value
     */
    public static int getIntegerProperty(
        UIComponent component,
        Integer property,
        String key,
        int defaultValue
    )
    {
        if (property != null) {
            return property.intValue();
        } else {
            ValueBinding binding =
                (ValueBinding) component.getValueBinding(key);

            if (binding != null) {
                Integer value =
                    (Integer) binding.getValue(
                        FacesContext.getCurrentInstance()
                    );

                if (value != null) {
                    return value.intValue();
                }
            }
        }

        return defaultValue;
    }

    /**
     * <p>
     * Get the object value of a UIComponent, even if it is a value
     * binding expression.
     * </p>
     *
     * @param component the component
     * @param property the property
     * @param key the key of the value binding
     * @param defaultValue the default value
     *
     * @return the object value
     */
    public static Object getObjectProperty(
        UIComponent component,
        Object property,
        String key,
        Object defaultValue
    )
    {
        if (property != null) {
            return property;
        } else {
            ValueBinding binding =
                (ValueBinding) component.getValueBinding(key);

            if (binding != null) {
                return binding.getValue(FacesContext.getCurrentInstance());
            }
        }

        return defaultValue;
    }

    /**
     * <p>
     * Check if the 2 dates are in the same day.
     * </p>
     *
     * @param date1 the first date
     * @param date2 the second date
     *
     * @return whether the dates are in the same day
     */
    public static boolean isSameDay(
        Date date1,
        Date date2
    )
    {
        if ((date1 == null) || (date2 == null)) {
            return false;
        }
        
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        
        return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA) &&
        		calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
        		calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>
     * Get the String value of a UIComponent, even if it is a value
     * binding expression.
     * </p>
     *
     * @param component the component
     * @param property the property
     * @param key the key of the value binding
     * @param defaultValue the default value
     *
     * @return the String value
     */
    public static String getStringProperty(
        UIComponent component,
        String property,
        String key,
        String defaultValue
    )
    {
        if (property != null) {
            return property;
        } else {
            ValueBinding binding =
                (ValueBinding) component.getValueBinding(key);

            if (binding != null) {
                return (String) binding.getValue(
                    FacesContext.getCurrentInstance()
                );
            }
        }

        return defaultValue;
    }

    /**
     * <p>
     * Check if the value of the given component can be modified
     * </p>
     *
     * @param component the component
     *
     * @return whether the value can be modified
     */
    public static boolean canModifyValue(UIComponent component)
    {
        boolean returnboolean =
            (component.isRendered() &&
            !getBooleanValue(component.getAttributes().get("readonly"), false) &&
            !getBooleanValue(component.getAttributes().get("disabled"), false));

        return returnboolean;
    }

    /**
     * <p>
     * Compare 2 dates after truncating them.
     * </p>
     *
     * @param date1 the first date
     * @param date2 the second date
     *
     * @return the comparison
     */
    public static int compareDays(
        Date date1,
        Date date2
    )
    {
        if (date1 == null) {
            return -1;
        }

        if (date2 == null) {
            return 1;
        }
        
        return truncate(date1).compareTo(truncate(date2));
    }
    
    /**
     * truncate the given Date to 00:00:00 that same day
     * 
     * @param date the date that should be truncated
     * @return the truncated date
     */
    public static Date truncate(Date date) {
        if (date == null) return null;
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * get a String identifying this date
     * 
     * @param date the date
     * @return the identifier for this date
     */
    public static String getDateId(Date date) {
        if (date == null) return "NULL";
        return DATE_ID_FORMATTER.format(date);
    }
    
    /**
     * get a date from the given date ID
     * 
     * @param id the date ID
     * @return the date
     */
    public static Date getDateFromId(String id) {
        if (id == null || id.length() == 0 || "null".equals(id)) return null;
        try
        {
            return DATE_ID_FORMATTER.parse(id);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
}
//The End
