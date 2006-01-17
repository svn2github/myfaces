/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.inputsuggestajax;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.taglib.html.ext.HtmlInputTextTag;


/**
 * @author Gerald Muellan
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 */

public class InputSuggestAjaxTag extends HtmlInputTextTag
{
    private static Log log = LogFactory.getLog(InputSuggestAjaxTag.class);

    private String _suggestedItemsMethod;
    private String _maxSuggestedItems;

    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;

    private String _listId;
    private String _listStyleClass;
    private String _listStyle;

    private String _listItemStyleClass;
    private String _listItemStyle;

    private String _layout;

    public String getComponentType() {
        return InputSuggestAjax.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return InputSuggestAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release() {

        super.release();

       _suggestedItemsMethod = null;
       _maxSuggestedItems = null;
       _popupId = null;
       _popupStyleClass = null;
       _popupStyle = null;
       _listId = null;
       _listStyleClass = null;
       _listStyle = null;
       _listItemStyleClass = null;
       _listItemStyle = null;
       _layout = null;

    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);        

        setIntegerProperty(component,"maxSuggestedItems", _maxSuggestedItems);
         setSuggestedItemsMethodProperty(getFacesContext(),component,_suggestedItemsMethod);
        setStringProperty(component,"popupId",_popupId);
        setStringProperty(component,"popupStyleClass",_popupStyleClass);
        setStringProperty(component,"popupStyle",_popupStyle);
        setStringProperty(component,"listId",_listId);
        setStringProperty(component,"listStyleClass",_listStyleClass);
        setStringProperty(component,"listStyle",_listStyle);
        setStringProperty(component,"listItemStyleClass",_listItemStyleClass);
        setStringProperty(component,"listItemStyle",_listItemStyle);
        setStringProperty(component,"layout",_layout);
    }

    public static void setSuggestedItemsMethodProperty(FacesContext context,
                                                       UIComponent component,
                                                       String suggestedItemsMethod)
    {
        if (suggestedItemsMethod != null)
        {
            if (!(component instanceof InputSuggestAjax))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no InputSuggestAjax");
            }
            if (isValueReference(suggestedItemsMethod))
            {
            	if (((InputSuggestAjax)component).getMaxSuggestedItems()!=null) {
                    MethodBinding mb = context.getApplication().createMethodBinding(suggestedItemsMethod,new Class[]{String.class, Integer.class});
                    ((InputSuggestAjax)component).setSuggestedItemsMethod(mb);
            	} else {
                    MethodBinding mb = context.getApplication().createMethodBinding(suggestedItemsMethod,new Class[]{String.class});
                    ((InputSuggestAjax)component).setSuggestedItemsMethod(mb);
            	}
            	
            	
            }
            else
            {
                log.error("Invalid expression " + suggestedItemsMethod);
            }
        }
    }
    
    // setter methodes to populate the components properites

    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public void setSuggestedItemsMethod(String suggestedItemsMethod)
    {
        _suggestedItemsMethod = suggestedItemsMethod;
    }

    public void setPopupId(String popupId)
    {
        _popupId = popupId;
    }

    public void setPopupStyleClass(String popupStyleClass)
    {
        _popupStyleClass = popupStyleClass;
    }

    public void setPopupStyle(String popupStyle)
    {
        _popupStyle = popupStyle;
    }

    public void setListId(String listId)
    {
        _listId = listId;
    }

    public void setListStyleClass(String listStyleClass)
    {
        _listStyleClass = listStyleClass;
    }

    public void setListStyle(String listStyle)
    {
        _listStyle = listStyle;
    }

    public void setListItemStyleClass(String listItemStyleClass)
    {
        _listItemStyleClass = listItemStyleClass;
    }

    public void setListItemStyle(String listItemStyle)
    {
        _listItemStyle = listItemStyle;
    }
    
    public void setMaxSuggestedItems(String maxSuggestedItems) {
    	_maxSuggestedItems = (maxSuggestedItems);
    }
}
