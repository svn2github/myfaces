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
package org.apache.myfaces.custom.suggestajax.tablesuggestajax;

import org.apache.myfaces.custom.suggestajax.SuggestAjaxTag;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

import javax.faces.component.UIComponent;

/**
 * @author Gerald Muellan
 *         Date: 25.03.2006
 *         Time: 17:05:25
 */
public class TableSuggestAjaxTag extends SuggestAjaxTag
{
    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;
    private String _layout;

    private String _tableStyleClass;
    private String _nextPageFieldClass;

    private String _columnHoverClass;
    private String _columnOutClass;

    private String _betweenKeyUp;
    private String _startRequest;

    private String _acceptValueToField;

    private String _var;

    public String getComponentType() {
        return TableSuggestAjax.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return TableSuggestAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release() {

        super.release();

       _var = null;
       _columnHoverClass = null;
       _columnOutClass = null;
       _betweenKeyUp = null;
       _startRequest = null;
       _tableStyleClass = null;
       _nextPageFieldClass = null;
       _acceptValueToField = null;
       _popupId = null;
       _popupStyleClass = null;
       _popupStyle = null;
       _layout = null;
    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        setIntegerProperty(component,"betweenKeyUp",_betweenKeyUp);
        setIntegerProperty(component,"startRequest", _startRequest);

        setStringProperty(component,"columnHoverClass",_columnHoverClass);
        setStringProperty(component,"columnOutClass",_columnOutClass);
        setStringProperty(component,"tableStyleClass",_tableStyleClass);
        setStringProperty(component,"nextPageFieldClass",_nextPageFieldClass);
        setBooleanProperty(component,"acceptValueToField",_acceptValueToField);
        setStringProperty(component,"popupId",_popupId);
        setStringProperty(component,"popupStyleClass",_popupStyleClass);
        setStringProperty(component,"popupStyle",_popupStyle);
        setStringProperty(component,"layout",_layout);

        setStringProperty(component, JSFAttr.VAR_ATTR, _var);
    }

    // setter methodes to populate the components properites
    public void setTableStyleClass(String tableStyleClass)
    {
        _tableStyleClass = tableStyleClass;
    }

    public void setBetweenKeyUp(String betweenKeyUp)
    {
        _betweenKeyUp = betweenKeyUp;
    }

    public void setStartRequest(String startRequest)
    {
        _startRequest = startRequest;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public void setColumnHoverClass(String columnHoverClass)
    {
        _columnHoverClass = columnHoverClass;
    }

    public void setColumnOutClass(String columnOutClass)
    {
        _columnOutClass = columnOutClass;
    }

    public void setNextPageFieldClass(String nextPageFieldClass)
    {
        _nextPageFieldClass = nextPageFieldClass;
    }

    public void setAcceptValueToField(String acceptValueToField)
    {
        _acceptValueToField = acceptValueToField;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
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
}
