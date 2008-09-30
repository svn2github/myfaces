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
package org.apache.myfaces.custom.roundeddiv;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.myfaces.test.utils.MockTestExternalContext;
import org.apache.myfaces.test.utils.MockTestViewHandler;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockResponseWriter;
import org.apache.shale.test.mock.MockServletContext;

public class HtmlRoundedDivRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private MockResponseWriter writer;
    private HtmlRoundedDiv roundedDiv;
    
    public HtmlRoundedDivRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(HtmlRoundedDivRendererTest.class);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        application.setViewHandler(new MockTestViewHandler());
        writer = (MockResponseWriter)facesContext.getResponseWriter();
        roundedDiv = new HtmlRoundedDiv();
        
        MockTestExternalContext extCtx = 
            new MockTestExternalContext(
                (ServletContext)new MockServletContext(), 
                (HttpServletRequest)new MockHttpServletRequest(), 
                (HttpServletResponse)new MockHttpServletResponse());
        extCtx.getApplicationMap().put(
                MyfacesConfig.class.getName(), new MyfacesConfig());
        facesContext.setExternalContext(extCtx);
    }
    
    public void tearDown() {
        writer = null;
        roundedDiv = null;
    }    
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = {
            //_UniversalProperties
            new HtmlRenderedAttr("dir"), 
            new HtmlRenderedAttr("lang"), 
            new HtmlRenderedAttr("title"),
            //_EventProperties
            new HtmlRenderedAttr("onclick"), 
            new HtmlRenderedAttr("ondblclick"), 
            new HtmlRenderedAttr("onkeydown"), 
            new HtmlRenderedAttr("onkeypress"),
            new HtmlRenderedAttr("onkeyup"), 
            new HtmlRenderedAttr("onmousedown"), 
            new HtmlRenderedAttr("onmousemove"), 
            new HtmlRenderedAttr("onmouseout"),
            new HtmlRenderedAttr("onmouseover"), 
            new HtmlRenderedAttr("onmouseup"),
            //_StyleProperties
            new HtmlRenderedAttr("style", "style", "style=\"style;position: relative;\""), 
            new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\""),
        };

        try {
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                roundedDiv, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }    
}
