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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.RenderKitFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.application.ApplicationFactoryImpl;
import org.apache.myfaces.custom.date.AbstractHtmlInputDate.UserData;
import org.apache.myfaces.shared_tomahawk.util.MessageUtils;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.shale.test.mock.MockResponseWriter;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

public class HtmlDateRendererTest extends AbstractTomahawkViewControllerTestCase {
//  public static void main(String[] args) {
//    junit.textui.TestRunner.run(HtmlDateRendererTest.class);
//  }
   
  public HtmlDateRendererTest(String name) {
    super(name);
  }
  
  public static Test suite() {
      return new TestSuite(HtmlDateRendererTest.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        MockHtmlDateRendererTestRenderKitFactory.class.getName());
    FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        ApplicationFactoryImpl.class.getName());
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for
   * 'org.apache.myfaces.custom.date.HtmlDateRenderer.decode(FacesContext,
   * UIComponent)'
   */
  public void testDecodeDate() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    inputDate.setType("date");
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.day", "14");
    map.put("test.month", "1");
    map.put("test.year", "2005");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    assertEquals("14", data.getDay());
    assertEquals("1", data.getMonth());
    assertEquals("2005", data.getYear());
  }

  public void testDecodeWithSubmittedValue() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    inputDate.setType("date");
    Date today = new Date();
    inputDate.setSubmittedValue(new UserData(today, Locale.ENGLISH, null, true, "date"));
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.day", "14");
    map.put("test.month", "1");
    map.put("test.year", "2005");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    assertEquals("14", data.getDay());
    assertEquals("1", data.getMonth());
    assertEquals("2005", data.getYear());
  }

  public void testDecodeTime() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    inputDate.setType("time");
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.hours", "12");
    map.put("test.minutes", "15");
    map.put("test.seconds", "35");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    assertEquals("12", data.getHours());
    assertEquals("15", data.getMinutes());
    assertEquals("35", data.getSeconds());
  }

  public void testDecodeFull() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    inputDate.setType("full");
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.day", "14");
    map.put("test.month", "1");
    map.put("test.year", "2005");
    map.put("test.hours", "12");
    map.put("test.minutes", "15");
    map.put("test.seconds", "3");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    assertEquals("14", data.getDay());
    assertEquals("1", data.getMonth());
    assertEquals("2005", data.getYear());
    assertEquals("12", data.getHours());
    assertEquals("15", data.getMinutes());
    assertEquals("03", data.getSeconds());
  }

  public void testDecodeFlorp() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    // is this correct? Should it parse correctly if the type is not valid?
    inputDate.setType("florp");
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.day", "14");
    map.put("test.month", "1");
    map.put("test.year", "2005");
    map.put("test.hours", "12");
    map.put("test.minutes", "15");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    assertEquals("14", data.getDay());
    assertEquals("1", data.getMonth());
    assertEquals("2005", data.getYear());
    assertEquals("12", data.getHours());
    assertEquals("15", data.getMinutes());
  }

  public void testDecodeDisabled() throws Exception {
    HtmlInputDate inputDate = new HtmlInputDate();
    inputDate.setId("test");
    inputDate.setType("date");
    inputDate.setDisabled(true);
    HtmlDateRenderer subject = new HtmlDateRenderer();
    // setup the request map
    Map map = new HashMap();
    map.put("test.day", "14");
    map.put("test.month", "1");
    map.put("test.year", "2005");
    FacesContext facesContext = mockupForDecodeCall(map);
    // decode - when disabled currently defaults to today
    // JIRA Issue #233 requests that the default be null
    // and the control handle data that is not required
    subject.decode(facesContext, inputDate);
    UserData data = inputDate.getUserData(Locale.ENGLISH);
    Calendar cal = GregorianCalendar.getInstance();
    assertEquals(cal.get(Calendar.DATE) + "", data.getDay());
    // different bases - cal starts at 0 in January, UserData starts at 1 in
    // January
    assertEquals((cal.get(Calendar.MONTH) + 1) + "", data.getMonth());
    assertEquals(cal.get(Calendar.YEAR) + "", data.getYear());
  }

  public static class DateTestConverter implements Converter
  {

    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String submittedValue)
            throws ConverterException
    {
        HtmlInputDate inputDate = (HtmlInputDate) uiComponent;
        String type = inputDate.getType();
        Properties props = new Properties();
        try
        {
            props.load(new ByteArrayInputStream(submittedValue.getBytes()));
        }catch(IOException e)
        {
        }
        UserData userData = inputDate.getUserData(facesContext.getViewRoot().getLocale());
        if( ! (type.equals( "time" ) || type.equals( "short_time" )) )
        {
            userData.setYear(props.getProperty("year"));
            userData.setMonth(props.getProperty("month"));
            userData.setDay(props.getProperty("day"));
        }
        
        if( ! type.equals( "date" ) ){
            userData.setHours(props.getProperty("hours"));
            userData.setMinutes(props.getProperty("minutes"));
            if (type.equals("full") || type.equals("time"))
            {
                userData.setSeconds(props.getProperty("seconds"));
            }
            if (inputDate.isAmpm()) {
                userData.setAmpm(props.getProperty("ampm"));
            }
        }
        try {
            return userData.parse();
        } catch (ParseException e) {
            Object[] args = {uiComponent.getId()};
            throw new ConverterException("Error Parsing");
        }  
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object submitValue)
            throws ConverterException
    {
        HtmlInputDate inputDate = (HtmlInputDate) uiComponent;
        String type = inputDate.getType();
        UserData value = new UserData((Date) submitValue, 
                facesContext.getViewRoot().getLocale(), 
                inputDate.getTimeZone(), inputDate.isAmpm(), inputDate.getType());        
        
        StringBuffer submittedValue = new StringBuffer();
        if( ! (type.equals( "time" ) || type.equals( "short_time" )) )
        {
            submittedValue.append("year=");
            submittedValue.append((String) value.getYear() );
            submittedValue.append("\n");

            submittedValue.append("month=");
            submittedValue.append((String) value.getMonth());
            submittedValue.append("\n");
            
            submittedValue.append("day=");
            submittedValue.append((String) value.getDay() );
            submittedValue.append("\n");                
        }
        
        if( ! type.equals( "date" ) )
        {
            submittedValue.append("hours=");
            submittedValue.append((String) value.getHours() );
            submittedValue.append("\n");
            
            submittedValue.append("minutes=");
            submittedValue.append((String) value.getMinutes() );
            submittedValue.append("\n");

            if (type.equals("full") || type.equals("time"))
            {
                submittedValue.append("seconds=");
                submittedValue.append((String) value.getSeconds() );
                submittedValue.append("\n");                    
            }
            
            if (inputDate.isAmpm())
            {
                submittedValue.append("ampm=");
                submittedValue.append((String) value.getAmpm() );
                submittedValue.append("\n");
            }
        }
        if (submittedValue.charAt(submittedValue.length()-1) == '\n' )
        {
            submittedValue.deleteCharAt(submittedValue.length()-1);
        }
        
        return submittedValue.toString();
    }
      
  }
  
  public void testDecodeConverterDate() throws Exception {
      HtmlInputDate inputDate = new HtmlInputDate();
      inputDate.setId("test");
      inputDate.setType("date");
      inputDate.setConverter(new DateTestConverter());
      HtmlDateRenderer subject = new HtmlDateRenderer();
      // setup the request map
      Map map = new HashMap();
      map.put("test.day", "14");
      map.put("test.month", "1");
      map.put("test.year", "2005");
      FacesContext facesContext = mockupForDecodeCall(map);
      // decode
      subject.decode(facesContext, inputDate);
      //With converter, the submitted value is a String
      assertTrue(inputDate.getSubmittedValue() instanceof String);
      // converter
      inputDate.validate(facesContext);
      
      UserData data = inputDate.getUserData(Locale.ENGLISH);
      assertEquals("14", data.getDay());
      assertEquals("1", data.getMonth());
      assertEquals("2005", data.getYear());
    }
  
  public void testDecodeConverterWithSubmittedValue() throws Exception {
      HtmlInputDate inputDate = new HtmlInputDate();
      inputDate.setId("test");
      inputDate.setType("date");
      inputDate.setConverter(new DateTestConverter());
      Date today = new Date();
      inputDate.setSubmittedValue(new UserData(today, Locale.ENGLISH, null, true, "date"));
      HtmlDateRenderer subject = new HtmlDateRenderer();
      // setup the request map
      Map map = new HashMap();
      map.put("test.day", "14");
      map.put("test.month", "1");
      map.put("test.year", "2005");
      FacesContext facesContext = mockupForDecodeCall(map);
      // decode
      subject.decode(facesContext, inputDate);
      //With converter, the submitted value is a String
      assertTrue(inputDate.getSubmittedValue() instanceof String);
      // converter
      inputDate.validate(facesContext);
      
      UserData data = inputDate.getUserData(Locale.ENGLISH);
      assertEquals("14", data.getDay());
      assertEquals("1", data.getMonth());
      assertEquals("2005", data.getYear());
    }
  
  public void testDecodeConverterTime() throws Exception {
      HtmlInputDate inputDate = new HtmlInputDate();
      inputDate.setId("test");
      inputDate.setType("time");
      HtmlDateRenderer subject = new HtmlDateRenderer();
      // setup the request map
      Map map = new HashMap();
      map.put("test.hours", "12");
      map.put("test.minutes", "15");
      map.put("test.seconds", "35");
      FacesContext facesContext = mockupForDecodeCall(map);
      // decode
      subject.decode(facesContext, inputDate);
      //With converter, the submitted value is a String
      assertTrue(inputDate.getSubmittedValue() instanceof String);
      // converter
      inputDate.validate(facesContext);
      
      UserData data = inputDate.getUserData(Locale.ENGLISH);
      assertEquals("12", data.getHours());
      assertEquals("15", data.getMinutes());
      assertEquals("35", data.getSeconds());
    }

  public void testDecodeConverterFull() throws Exception {
      HtmlInputDate inputDate = new HtmlInputDate();
      inputDate.setId("test");
      inputDate.setType("full");
      HtmlDateRenderer subject = new HtmlDateRenderer();
      // setup the request map
      Map map = new HashMap();
      map.put("test.day", "14");
      map.put("test.month", "1");
      map.put("test.year", "2005");
      map.put("test.hours", "12");
      map.put("test.minutes", "15");
      map.put("test.seconds", "3");
      FacesContext facesContext = mockupForDecodeCall(map);
      // decode
      subject.decode(facesContext, inputDate);
      //With converter, the submitted value is a String
      assertTrue(inputDate.getSubmittedValue() instanceof String);
      // converter
      inputDate.validate(facesContext);
      UserData data = inputDate.getUserData(Locale.ENGLISH);
      assertEquals("14", data.getDay());
      assertEquals("1", data.getMonth());
      assertEquals("2005", data.getYear());
      assertEquals("12", data.getHours());
      assertEquals("15", data.getMinutes());
      assertEquals("03", data.getSeconds());
    }

    public void testDecodeConverterFlorp() throws Exception {
      HtmlInputDate inputDate = new HtmlInputDate();
      inputDate.setId("test");
      // is this correct? Should it parse correctly if the type is not valid?
      inputDate.setType("florp");
      HtmlDateRenderer subject = new HtmlDateRenderer();
      // setup the request map
      Map map = new HashMap();
      map.put("test.day", "14");
      map.put("test.month", "1");
      map.put("test.year", "2005");
      map.put("test.hours", "12");
      map.put("test.minutes", "15");
      FacesContext facesContext = mockupForDecodeCall(map);
      // decode
      subject.decode(facesContext, inputDate);
      //With converter, the submitted value is a String
      assertTrue(inputDate.getSubmittedValue() instanceof String);
      // converter
      inputDate.validate(facesContext);
      UserData data = inputDate.getUserData(Locale.ENGLISH);
      assertEquals("14", data.getDay());
      assertEquals("1", data.getMonth());
      assertEquals("2005", data.getYear());
      assertEquals("12", data.getHours());
      assertEquals("15", data.getMinutes());
    }

  /*
   * Test method for
   * 'org.apache.myfaces.custom.date.HtmlDateRenderer.encodeEnd(FacesContext,
   * UIComponent)'
   */
  public void testEncodeEnd() throws Exception {
    
  }

  /*
   * Test method for
   * 'org.apache.myfaces.custom.date.HtmlDateRenderer.getConvertedValue(FacesContext,
   * UIComponent, Object)'
   */
  public void testGetConvertedValue() throws Exception {
  }

  private FacesContext mockupForDecodeCall(Map requestParameterMap) {
    // mock FacesContext
    MockControl contextControl = MockClassControl
        .createControl(FacesContext.class);
    FacesContext facesContext = (FacesContext) contextControl.getMock();
    // mock UIViewRoot
    MockControl viewControl = MockClassControl.createControl(UIViewRoot.class);
    UIViewRoot viewRoot = (UIViewRoot) viewControl.getMock();
    // mock ExternalContext
    MockControl externalContextControl = MockClassControl
        .createControl(ExternalContext.class);
    ExternalContext externalContext = (ExternalContext) externalContextControl
        .getMock();
    // set the view root
    facesContext.getViewRoot();
    contextControl.setReturnValue(viewRoot);
    // called twice
    facesContext.getViewRoot();
    contextControl.setReturnValue(viewRoot);
    // set the external context
    facesContext.getExternalContext();
    contextControl.setReturnValue(externalContext);
    // set the locale
    viewRoot.getLocale();
    viewControl.setReturnValue(Locale.ENGLISH);
    // set the render kit id
    viewRoot.getRenderKitId();
    viewControl.setReturnValue(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    externalContext.getRequestParameterMap();
    externalContextControl.setReturnValue(requestParameterMap);
    // prepare
    contextControl.replay();
    viewControl.replay();
    externalContextControl.replay();
    return facesContext;
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
