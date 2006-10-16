/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.custom.dynaForm.jsfext;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.EnumSet;

/**
 * converts an enum in a way that makes the conversion reversible (sometimes)
 * <ul>
 * <li>input: uses its classname and ordinal, reversible<li>
 * <li>else: uses its name, non reversible<li>
 * </ul>
 */
public class EnumConverter implements Converter
{
	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException
	{
		if (value == null || value.length() < 1)
		{
			return null;
		}
		
		int pos = value.indexOf('@');
		if (pos < 0)
		{
			throw new IllegalArgumentException(value + " do not point to an enum");
		}
		
		String clazz = value.substring(0, pos);
		int ordinal = Integer.parseInt(value.substring(pos+1), 10);
		
		try
		{
			Enum e = ordinalToEnum(Class.forName(clazz), ordinal);
			if (e != null)
			{
				return e;
			}
		}
		catch (ClassNotFoundException e1)
		{
			throw new RuntimeException(e1);
		}
		
		throw new IllegalArgumentException("ordinal " + ordinal + " not found in enum " + clazz);
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException
	{
		if (value == null)
		{
			return "";
		}

		Enum e = (Enum) value;
		
		if (component instanceof UIInput)
		{
			return e.getClass().getName() + "@" + Integer.toString(e.ordinal(), 10);
		}
		
		return e.name();
	}

	@SuppressWarnings("unchecked")
	protected Enum stringToEnum(Class type, String string)
	{
		EnumSet es = EnumSet.allOf(type);
        for (Object e1 : es)
        {
            Enum e = (Enum) e1;
            if (string.equals(e.name()))
            {
                return e;
            }
        }

        return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Enum ordinalToEnum(Class type, int ordinal)
	{
		EnumSet es = EnumSet.allOf(type);
        for (Object e1 : es)
        {
            Enum e = (Enum) e1;
            if (e.ordinal() == ordinal)
            {
                return e;
            }
        }

        return null;
	}
	
	public String fixString(Class type, String string)
	{
		Enum e = stringToEnum(type, string);
		if (e == null)
		{
			throw new IllegalArgumentException("cant find enum for string '" + string + "' type '" + type.getName() + "'");
		}
		
		String prefix = type.getName() + "@";
		
		if (!string.startsWith(prefix))
		{
			return prefix + e.ordinal();
		}
		
		return string;
	}
}