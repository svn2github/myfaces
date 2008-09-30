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

import java.util.Map;

import javax.el.MethodExpression;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.shale.test.mock.MockResponseWriter;

public class HtmlAjaxChildComboBoxRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private MockResponseWriter writer;
    private AjaxChildComboBox childComboBox;
    
    public HtmlAjaxChildComboBoxRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(HtmlAjaxChildComboBoxRendererTest.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        writer = (MockResponseWriter)facesContext.getResponseWriter();
        childComboBox = new AjaxChildComboBox();
        
        HtmlSelectOneMenu parentMenu = new HtmlSelectOneMenu();
        parentMenu.setId("parentCombo");
        childComboBox.setParentComboBox("parentCombo");
        childComboBox.setParent(parentMenu);
        MethodExpression mExp = facesContext.getApplication().getExpressionFactory().createMethodExpression(
                facesContext.getELContext(), "#{someBean.getItems}", Map.class, null);
        childComboBox.setAjaxSelectItemsMethod(mExp);
    }
    
    public void tearDown() {
        writer = null;
        childComboBox = null;
    }      
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = HtmlCheckAttributesUtil.generateBasicAttrs();

        HtmlCheckAttributesUtil.checkRenderedAttributes(
                childComboBox, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }   
}
