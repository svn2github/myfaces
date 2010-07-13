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
package org.apache.myfaces.custom.calendar;

import java.io.IOException;

import javax.faces.FacesException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.mock.MockResponseWriter;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;

public class HtmlCalendarRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private HtmlInputCalendar inputCalendar;
    
    public HtmlCalendarRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(HtmlCalendarRendererTest.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        inputCalendar = new HtmlInputCalendar();
        facesContext.getApplication().addComponent("org.apache.myfaces.HtmlInputTextHelp", "org.apache.myfaces.custom.inputTextHelp.HtmlInputTextHelp");
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        inputCalendar = null;
    }
    
    public void testRenderedAttributes() throws IOException {
        System.out.println("Testing rendered attributes");
        inputCalendar.setOnfocus("onfocusTest()");
        inputCalendar.setOnmouseover("onMouseOverTest()");
        inputCalendar.setRenderAsPopup(true);
        
        try {
        inputCalendar.encodeEnd(facesContext);
        } catch(FacesException fe) {
            fe.printStackTrace();
        }
        facesContext.renderResponse();
        System.out.println("---------------------------------------");
        MockResponseWriter writer = (MockResponseWriter) facesContext.getResponseWriter();
        System.out.println(writer.getWriter().toString());
        System.out.println("\n---------------------------------------");
    }
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = HtmlCheckAttributesUtil.generateBasicAttrs(); 
        
        inputCalendar.setRenderAsPopup(true);
        
        MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                inputCalendar, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
}
