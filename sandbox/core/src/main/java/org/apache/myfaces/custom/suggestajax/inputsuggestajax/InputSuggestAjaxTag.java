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
package org.apache.myfaces.custom.suggestajax.inputsuggestajax;


import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.suggestajax.SuggestAjaxTag;


/**
 * @author Gerald Muellan
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 */

public class InputSuggestAjaxTag extends SuggestAjaxTag
{
    private String _listId;
    private String _listStyleClass;
    private String _listStyle;

    private String _listItemStyleClass;
    private String _listItemStyle;

    public String getComponentType() {
        return InputSuggestAjax.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return InputSuggestAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release() {

        super.release();

       _listId = null;
       _listStyleClass = null;
       _listStyle = null;
       _listItemStyleClass = null;
       _listItemStyle = null;
    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        setStringProperty(component,"listId",_listId);
        setStringProperty(component,"listStyleClass",_listStyleClass);
        setStringProperty(component,"listStyle",_listStyle);
        setStringProperty(component,"listItemStyleClass",_listItemStyleClass);
        setStringProperty(component,"listItemStyle",_listItemStyle);
    }

    // setter methodes to populate the components properites
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

}
