/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.selectOneRow;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * 
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "org.apache.myfaces.SelectOneRow"
 *   type = "org.apache.myfaces.SelectOneRow"
 * @since 1.1.7
 */
public class SelectOneRowRenderer extends HtmlRenderer
{

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        if ((component instanceof SelectOneRow) && component.isRendered())
        {
            SelectOneRow row = (SelectOneRow) component;
            String clientId = row.getClientId(facesContext);

            ResponseWriter writer = facesContext.getResponseWriter();

            writer.startElement(HTML.INPUT_ELEM, row);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
            writer.writeAttribute(HTML.NAME_ATTR, row.getGroupName(), null);

            if (isDisabled(facesContext, row))
            {
                writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
            }
            
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);

            if (isRowSelected(row))
            {
                writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
            }

            writer.writeAttribute(HTML.VALUE_ATTR, clientId, null);

            HtmlRendererUtils.renderHTMLAttributes(writer, row, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

            writer.endElement(HTML.INPUT_ELEM);
        }
    }
    
    /**
     * Check if the component is disabled or not, taking into account
     * the config init param org.apache.myfaces.READONLY_AS_DISABLED_FOR_SELECTS 
     */
    protected boolean isDisabled(FacesContext facesContext, SelectOneRow row)
    {
        boolean disabled = row.isDisabled();
        boolean readonly = row.isReadonly();
        if (!disabled && readonly)
        {
            disabled = MyfacesConfig.getCurrentInstance(facesContext
                            .getExternalContext()).isReadonlyAsDisabledForSelect();
        }
        return disabled;        
    }

    private boolean isRowSelected(UIComponent component)
    {
        UIInput input = (UIInput) component;
        Object value = input.getValue();

        int currentRowIndex = getCurrentRowIndex(component);

        return (value != null)
                && (currentRowIndex == ((Number) value).intValue());

    }

    private int getCurrentRowIndex(UIComponent component)
    {
        UIData uidata = findUIData(component);
        if (uidata == null)
            return -1;
        else
            return uidata.getRowIndex();
    }

    protected UIData findUIData(UIComponent uicomponent)
    {
        if (uicomponent == null)
            return null;
        if (uicomponent instanceof UIData)
            return (UIData) uicomponent;
        else
            return findUIData(uicomponent.getParent());
    }

    public void decode(FacesContext context, UIComponent uiComponent)
    {
        if (! (uiComponent instanceof SelectOneRow))
        {
            return;
        }

        if (!uiComponent.isRendered())
        {
            return;
        }
        SelectOneRow row = (SelectOneRow) uiComponent;

        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String postedValue;

        if (requestMap.containsKey(row.getGroupName()))
        {
            postedValue = (String) requestMap.get(row.getGroupName());
            String clientId = row.getClientId(context);
            if (clientId.equals(postedValue))
            {
                String[] postedValueArray = postedValue.split(":");
                String rowIndex = postedValueArray[postedValueArray.length - 2];

                ValueBinding vb = row.getValueBinding("value");
                Class type = vb.getType(context);
                if (type == null)
                {
                    type = (vb.getValue(context) != null) ? vb.getValue(context).getClass() : null;
                }
                Object newValue = null;
                if (type != null)
                {
                    if (type.isAssignableFrom(Long.class))
                    {
                        newValue = Long.valueOf(rowIndex);
                    }
                    else
                    {
                        newValue = Integer.valueOf(rowIndex);
                    }
                }
                else
                {
                    newValue = Integer.valueOf(rowIndex);
                }
                //the value to go in conversion&validation
                row.setSubmittedValue(newValue);
                row.setValid(true);
            }
        }
    }
}
