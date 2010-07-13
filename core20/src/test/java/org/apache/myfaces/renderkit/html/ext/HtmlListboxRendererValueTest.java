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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UISelectItem;

import org.apache.myfaces.component.html.ext.HtmlSelectManyListbox;
import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.test.mock.MockResponseWriter;

/**
 * Test cases for HtmlListboxRenderer.
 * 
 * @author Jakob Korherr (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlListboxRendererValueTest extends AbstractJsfTestCase
{

    private HtmlListboxRenderer _renderer;
    private MockResponseWriter _writer;
    private StringWriter _stringWriter;
    
    public HtmlListboxRendererValueTest(String name)
    {
        super(name);
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        _renderer = new HtmlListboxRenderer();
        _stringWriter = new StringWriter();
        _writer = new MockResponseWriter(_stringWriter, "text/html", "utf-8");
        
        facesContext.setResponseWriter(_writer);
    }

    @Override
    protected void tearDown() throws Exception
    {
        _renderer = null;
        _stringWriter = null;
        _writer = null;
        
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void testValueTypeRender() throws IOException
    {
        MockBean bean = new MockBean();
        externalContext.getApplicationMap().put("bean", bean);
        ValueExpression beanVE = new MockValueExpression("#{bean.values}", Object.class);
        
        // create UISelectMany component
        HtmlSelectManyListbox selectMany = new HtmlSelectManyListbox();
        selectMany.setValueExpression("value", beanVE);
        selectMany.setValueType(Integer.class.getName());
        
        // create the select item
        UISelectItem item = new UISelectItem();
        item.setItemValue("1");
        selectMany.getChildren().add(item);
        
        // register the converter
        application.addConverter(Integer.class, MockIntegerConverter.class.getName());
        
        // Render the component (only encodeEnd is used in this renderer)
        _renderer.encodeEnd(facesContext, selectMany);
        final String output = _stringWriter.toString();
        
        // we expect a rendered value of 11, because the Converter adds 10 to
        // the given value in getAsString(). Thus we verify that the converter was called.
        assertTrue(output.contains("value=\"11\""));
    }
    
    @SuppressWarnings({ "unchecked", "serial" })
    public void testValueTypeSubmit() throws IOException
    {
        MockBean bean = new MockBean();
        externalContext.getApplicationMap().put("bean", bean);
        ValueExpression beanVE = new MockValueExpression("#{bean.values}", Object.class)
        {

            @Override
            public Class getType(ELContext context)
            {
                // to simulate the behavior when a bean property has a null value,
                // but the getter has a return value of Collection
                return Collection.class;
            }
              
        };
        
        // create UISelectMany component
        HtmlSelectManyListbox selectMany = new HtmlSelectManyListbox();
        selectMany.setValueExpression("value", beanVE);
        selectMany.setValueType(Integer.class.getName());
        
        // create the select item
        UISelectItem item = new UISelectItem();
        item.setItemValue("1");
        selectMany.getChildren().add(item);
        
        // get the converted value
        Object convertedValue = _renderer.getConvertedValue(facesContext, selectMany, new String[] {"1"});
        
        // the value must be a Collection
        assertTrue(convertedValue instanceof Collection);
        
        // the first element of the Collection must be the _Integer_ 1
        // (without the valueType attribute it would be the String "1")
        assertEquals(1, ((Collection) convertedValue).iterator().next());
    }
    
}
