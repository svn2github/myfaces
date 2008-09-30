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
package org.apache.myfaces.custom.picklist;

import javax.faces.component.UISelectItem;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.shale.test.mock.MockResponseWriter;

public class HtmlPicklistRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private HtmlSelectManyPicklist picklist;
    
    public HtmlPicklistRendererTest(String name)
    {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(HtmlPicklistRendererTest.class);
    }
    
    public void setUp() throws Exception
    {
        super.setUp();
        picklist = new HtmlSelectManyPicklist();        
    }
    
    public void tearDown() throws Exception 
    {
        super.tearDown();
        picklist = null;
    }
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = {
            //_AccesskeyProperty
            new HtmlRenderedAttr("accesskey", 2),
            //_UniversalProperties
            new HtmlRenderedAttr("dir", 2), 
            new HtmlRenderedAttr("lang", 2), 
            new HtmlRenderedAttr("title", 2),
            //_FocusBlurProperties
            new HtmlRenderedAttr("onfocus", 2), 
            new HtmlRenderedAttr("onblur", 2),
            //_ChangeSelectProperties
            new HtmlRenderedAttr("onchange", 2), 
            new HtmlRenderedAttr("onselect", 2),
            //_EventProperties
            new HtmlRenderedAttr("onclick", 2), 
            new HtmlRenderedAttr("ondblclick", 2), 
            new HtmlRenderedAttr("onkeydown", 2), 
            new HtmlRenderedAttr("onkeypress", 2),
            new HtmlRenderedAttr("onkeyup", 2), 
            new HtmlRenderedAttr("onmousedown", 2), 
            new HtmlRenderedAttr("onmousemove", 2), 
            new HtmlRenderedAttr("onmouseout", 2),
            new HtmlRenderedAttr("onmouseover", 2), 
            new HtmlRenderedAttr("onmouseup", 2),
            //_StyleProperties
            new HtmlRenderedAttr("style", 2), 
            new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\"", 2),
            //_TabindexProperty
            new HtmlRenderedAttr("tabindex", 2)
        };
        
        UISelectItem item = new UISelectItem();
        item.setItemLabel("mars");
        item.setItemValue("mars");
        picklist.getChildren().add(item);
        picklist.setAddButtonStyle("btnStyle");
        picklist.setAddButtonStyleClass("btnStyleClass");
        picklist.setAddAllButtonStyle("btnStyle");
        picklist.setAddAllButtonStyleClass("btnStyleClass");
        picklist.setRemoveButtonStyle("btnStyle");
        picklist.setRemoveButtonStyleClass("btnStyleClass");
        picklist.setRemoveAllButtonStyle("btnStyle");
        picklist.setRemoveAllButtonStyleClass("btnStyleClass");
        
        MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                picklist, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
}
