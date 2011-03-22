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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;
import org.apache.myfaces.custom.radio.HtmlRadio;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.mock.MockResponseWriter;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;

public class HtmlRadioRendererTest extends AbstractTomahawkViewControllerTestCase
{
    private HtmlSelectOneRadio selectOneRadio;

    public HtmlRadioRendererTest(String name)
    {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(HtmlRadioRendererTest.class);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        selectOneRadio = new HtmlSelectOneRadio();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        selectOneRadio = null;
    }
    
    public void testHtmlPropertyPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = {
            //_AccesskeyProperty
            new HtmlRenderedAttr("accesskey"),
            //_UniversalProperties
            new HtmlRenderedAttr("dir"), 
            new HtmlRenderedAttr("lang"), 
            new HtmlRenderedAttr("title"),
            //_FocusBlurProperties
            new HtmlRenderedAttr("onfocus"), 
            new HtmlRenderedAttr("onblur"),
            //_ChangeSelectProperties
            new HtmlRenderedAttr("onchange"), 
            new HtmlRenderedAttr("onselect"),
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
            new HtmlRenderedAttr("style", 1), 
            new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\"", 1),
            //_TabindexProperty
            new HtmlRenderedAttr("tabindex")
        };
        
        List items = new ArrayList();
        items.add(new SelectItem("mars"));

        UISelectItems selectItems = new UISelectItems();
        selectItems.setValue(items);

        selectOneRadio.getChildren().add(selectItems);
        
        MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                selectOneRadio, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
    
    
    public void testHtmlPropertyRadioPassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = {
            //_AccesskeyProperty
            new HtmlRenderedAttr("accesskey"),
            //_UniversalProperties
            new HtmlRenderedAttr("dir"), 
            new HtmlRenderedAttr("lang"), 
            new HtmlRenderedAttr("title"),
            //_FocusBlurProperties
            new HtmlRenderedAttr("onfocus"), 
            new HtmlRenderedAttr("onblur"),
            //_ChangeSelectProperties
            new HtmlRenderedAttr("onchange"), 
            new HtmlRenderedAttr("onselect"),
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
            new HtmlRenderedAttr("style", 1), 
            new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\"", 1),
            //_TabindexProperty
            new HtmlRenderedAttr("tabindex"),
            new HtmlRenderedAttr("id","option1", "id=\"option1\"", 1)
        };
        
        List items = new ArrayList();
        items.add(new SelectItem("mars"));

        UISelectItems selectItems = new UISelectItems();
        selectItems.setValue(items);

        selectOneRadio = new HtmlSelectOneRadio();
        
        selectOneRadio.setId("selectOneRadio");
        selectOneRadio.setLayout("spread");
        
        facesContext.getViewRoot().getChildren().add(selectOneRadio);
        
        selectOneRadio.getChildren().add(selectItems);
        
        HtmlRadio radio = new HtmlRadio();
        radio.setId("option1");
        radio.setIndex(0);
        radio.setFor("selectOneRadio");
        
        facesContext.getViewRoot().getChildren().add(radio);
        
        MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
        HtmlCheckAttributesUtil.checkRenderedAttributes(
                radio, facesContext, writer, attrs);
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }
    
    public void testHtmlPropertyRadioSelectOnePassTru() throws Exception
    {
        HtmlRenderedAttr[] attrs = {
            //_AccesskeyProperty
            new HtmlRenderedAttr("accesskey"),
            //_UniversalProperties
            new HtmlRenderedAttr("dir"), 
            new HtmlRenderedAttr("lang"), 
            new HtmlRenderedAttr("title"),
            //_FocusBlurProperties
            new HtmlRenderedAttr("onfocus"), 
            new HtmlRenderedAttr("onblur"),
            //_ChangeSelectProperties
            new HtmlRenderedAttr("onchange"), 
            new HtmlRenderedAttr("onselect"),
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
            new HtmlRenderedAttr("style", 1), 
            new HtmlRenderedAttr("styleClass", "styleClass", "class=\"styleClass\"", 1),
            //_TabindexProperty
            new HtmlRenderedAttr("tabindex")
        };
        
        List items = new ArrayList();
        items.add(new SelectItem("mars"));

        UISelectItems selectItems = new UISelectItems();
        selectItems.setValue(items);

        selectOneRadio = new HtmlSelectOneRadio();
        
        selectOneRadio.setId("selectOneRadio");
        selectOneRadio.setLayout("spread");
        
        facesContext.getViewRoot().getChildren().add(selectOneRadio);
        
        selectOneRadio.getChildren().add(selectItems);
        
        HtmlRadio radio = new HtmlRadio();
        radio.setIndex(0);
        radio.setFor("selectOneRadio");
        
        facesContext.getViewRoot().getChildren().add(radio);
        
        MockResponseWriter writer = (MockResponseWriter)facesContext.getResponseWriter();
        HtmlCheckAttributesUtil.addBaseAttributes(selectOneRadio, attrs);
        radio.encodeBegin(facesContext);
        radio.encodeChildren(facesContext);
        radio.encodeEnd(facesContext);
        facesContext.renderResponse();
        HtmlCheckAttributesUtil.checkRenderedAttributes(attrs, writer.getWriter().toString());        
        
        if(HtmlCheckAttributesUtil.hasFailedAttrRender(attrs)) {
            fail(HtmlCheckAttributesUtil.constructErrorMessage(attrs, writer.getWriter().toString()));
        }
    }    

}
