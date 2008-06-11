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
package org.apache.myfaces.custom.ajaxchildcombobox;

import javax.el.MethodExpression;
import javax.faces.application.Application;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.html.HtmlComponentHandler;

public class AjaxChildComboBoxTagHandler extends HtmlComponentHandler {
 
    private static final String AJAX_SELECT_ITEMS_METHOD = "ajaxSelectItemsMethod";
    
    private static final Class [] ajaxSelectItemsMethodParamList = new Class[]{String.class}; 

    private TagAttribute ajaxSelectItemsMethodAttr;
    
    public AjaxChildComboBoxTagHandler(ComponentConfig tagConfig) {
        super(tagConfig);
        ajaxSelectItemsMethodAttr = getAttribute(AJAX_SELECT_ITEMS_METHOD);
    }

    protected void setAttributes(FaceletContext ctx, Object instance)
    {
        super.setAttributes(ctx, instance);
        
        Application app = ctx.getFacesContext().getApplication();
        
        AjaxChildComboBox comp = (AjaxChildComboBox) instance;
        
        if (ajaxSelectItemsMethodAttr != null){
            String _ajaxSelectItemsMethod = ajaxSelectItemsMethodAttr.getValue();
            if (_ajaxSelectItemsMethod != null)
            {
                MethodExpression mb = app.getExpressionFactory().createMethodExpression(
                        ctx.getFacesContext().getELContext(),
                    _ajaxSelectItemsMethod,javax.faces.model.SelectItem[].class, ajaxSelectItemsMethodParamList);
                comp.setAjaxSelectItemsMethod(mb);
            }
        }        
    }
}
