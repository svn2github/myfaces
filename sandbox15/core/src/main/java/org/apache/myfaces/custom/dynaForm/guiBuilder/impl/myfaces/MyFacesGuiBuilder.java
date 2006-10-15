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
package org.apache.myfaces.custom.dynaForm.guiBuilder.impl.myfaces;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.convert.Converter;
import javax.persistence.TemporalType;

import org.apache.myfaces.custom.dynaForm.guiBuilder.impl.jsf.JsfGuiBuilder;
import org.apache.myfaces.custom.dynaForm.metadata.FieldInterface;

import org.apache.myfaces.custom.calendar.HtmlInputCalendar;
import org.apache.myfaces.custom.convertNumber.TypedNumberConverter;

import java.math.BigDecimal;

/**
 * use myfaces goodies
 */
public class MyFacesGuiBuilder extends JsfGuiBuilder
{
	public MyFacesGuiBuilder()
	{
		super();
	}

	@Override
	public Converter doCreateConverter(FieldInterface field)
	{
        Class type = field.getType();

        if (Float.class.isAssignableFrom(type)
            || Double.class.isAssignableFrom(type)
            || float.class.isAssignableFrom(type)
            || double.class.isAssignableFrom(type)
            || BigDecimal.class.isAssignableFrom(type))
		{
			return getContext().getApplication().createConverter(TypedNumberConverter.CONVERTER_ID);
		}

		return super.doCreateConverter(field);
	}

	@Override
	public HtmlInputText doCreateInputDateComponent(FieldInterface field)
	{
        if (!TemporalType.DATE.equals(field.getTemporalType()))
        {
            // we can select dates only - yet!
            return super.doCreateInputDateComponent(field);
        }

        HtmlInputCalendar cmp = (HtmlInputCalendar) getContext()
				.getApplication().createComponent(
						HtmlInputCalendar.COMPONENT_TYPE);
		cmp.setRenderAsPopup(true);

		if (Boolean.FALSE.equals(field.getCanWrite()))
		{
			cmp.setReadonly(true);
		}
		if (Boolean.TRUE.equals(field.getDisabled()))
		{
			cmp.setDisabled(true);
		}

		return cmp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createInputDate(FieldInterface field)
	{
		if (isDisplayOnly())
		{
			super.createInputDate(field);
			return;
		}

		UIComponent cmp = doCreateInputDate(field);

        // workaround to avoid wrap of date-component and its button ==>
        HtmlPanelGroup panel = doCreatePanelGroupComponent();
		panel.setId("pnl_" + cmp.getId());
		panel.getChildren().add(cmp);
		panel.setStyleClass("ff_nowrap");
        // <==

		fireNewComponent(field, panel);
	}
}
