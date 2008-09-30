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
package org.apache.myfaces.custom.suggest;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UISelectItems;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.shale.test.mock.MockResponseWriter;

public class InputSuggestRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private MockResponseWriter writer;
    private InputSuggest inputSuggest;
    
    public InputSuggestRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(InputSuggestRendererTest.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        writer = (MockResponseWriter)facesContext.getResponseWriter();
        inputSuggest = new InputSuggest();
        
        UISelectItems items = new UISelectItems();
        
        Map choices = new HashMap();
        choices.put("mars", "mars");
        
        items.setValue(choices);        
        inputSuggest.getChildren().add(items);
    }
    
    public void tearDown() {
        writer = null;
        inputSuggest = null;
    }    
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = HtmlCheckAttributesUtil.generateBasicAttrs();


        HtmlCheckAttributesUtil.checkRenderedAttributes(
                inputSuggest, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }    
}
