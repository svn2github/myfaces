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
package org.apache.myfaces.renderkit.html.ext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.mock.MockResponseWriter;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;

public class HtmlImageRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private MockResponseWriter writer ;
    private HtmlGraphicImage graphicImage;
    
    public HtmlImageRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(HtmlImageRendererTest.class);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        graphicImage = new HtmlGraphicImage();
        writer = (MockResponseWriter)facesContext.getResponseWriter();

    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        graphicImage = null;
        writer = null;
    }

    public void testRenderDefault() throws Exception
    {
        graphicImage.setAlt("foo");
        graphicImage.setId("img1");
        graphicImage.setValue("http://myfaces.apache.org");
        graphicImage.encodeBegin(facesContext);
        graphicImage.encodeChildren(facesContext);
        graphicImage.encodeEnd(facesContext);
        facesContext.renderResponse();

        String output = writer.getWriter().toString();
        assertEquals("<img id=\"img1\" src=\"nullhttp://myfaces.apache.org\" alt=\"foo\"/>", output);
    }

    public void testHtmlPropertyPassTruNotRendered() throws Exception
    {
        HtmlRenderedAttr[] attrs = HtmlCheckAttributesUtil.generateAttrsNotRenderedForReadOnly();
        
        graphicImage.setId("img1");
        graphicImage.setValue("http://myfaces.apache.org");
        
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                graphicImage, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
    
    public void testHtmlPropertyPassTru() throws Exception
    { 
        HtmlRenderedAttr[] attrs = HtmlCheckAttributesUtil.generateBasicReadOnlyAttrs();
        
        graphicImage.setId("img1");
        graphicImage.setValue("http://myfaces.apache.org");
        
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                graphicImage, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) 
        {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
}
