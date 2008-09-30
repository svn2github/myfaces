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

package org.apache.myfaces.custom.date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.shale.test.mock.MockResponseWriter;

public class HtmlDateRendererTest extends AbstractTomahawkViewControllerTestCase {
   
  public HtmlDateRendererTest(String name) {
    super(name);
  }
  
  public static Test suite() {
      return new TestSuite(HtmlDateRendererTest.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testHtmlPropertyPassTru() throws Exception
  {
      HtmlInputDate inputDate = new HtmlInputDate();
      
      HtmlRenderedAttr[] attrs = {
          //_UniversalProperties
          new HtmlRenderedAttr("dir", 3), 
          new HtmlRenderedAttr("lang", 3), 
          new HtmlRenderedAttr("title", 3),
          //_EventProperties 
          new HtmlRenderedAttr("ondblclick", 3), 
          new HtmlRenderedAttr("onkeydown", 3), 
          new HtmlRenderedAttr("onkeypress", 3),
          new HtmlRenderedAttr("onkeyup", 3), 
          new HtmlRenderedAttr("onmousedown", 3), 
          new HtmlRenderedAttr("onmousemove", 3), 
          new HtmlRenderedAttr("onmouseout", 3),
          new HtmlRenderedAttr("onmouseover", 3), 
          new HtmlRenderedAttr("onmouseup", 3),
          //_StyleProperties
          new HtmlRenderedAttr("style", 3), 
          new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\"", 3),
      };
      
      
      MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
      HtmlCheckAttributesUtil.checkRenderedAttributes(
              inputDate, facesContext, writer, attrs);
      if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) {
          fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
      }
  }
}
