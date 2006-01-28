/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.component.html.ext;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.faces.FactoryFinder;
import javax.faces.el.ValueBinding;
import javax.faces.model.ListDataModel;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

import junit.framework.TestCase;

import org.apache.myfaces.mock.tomahawk.*;
import org.apache.myfaces.renderkit.RenderKitFactoryImpl;
import org.apache.myfaces.renderkit.html.HtmlRenderKitImpl;

/**
 * @author Mathias Brökelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTableTest extends TestCase
{
    private FacesContext _ctx;
    private HtmlDataTable _dataTable;

    protected void setUp() throws Exception
    {
        MockFacesContext mock = new MockFacesContext();
        mock.setViewRoot(new UIViewRoot());
        mock.setExternalContext(new MockExternalContext());
        _dataTable = new HtmlDataTable();
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                RenderKitFactoryImpl.class.getName());
        RenderKitFactory rkf = (RenderKitFactory) FactoryFinder
                .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        rkf.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                new HtmlRenderKitImpl());
        _ctx = mock;
    }

    protected void tearDown() throws Exception
    {
        _ctx = null;
        _dataTable = null;
    }

    /*
     * Test method for
     * 'org.apache.myfaces.component.html.ext.HtmlDataTable.getClientId(FacesContext)'
     */
    public void testGetClientIdFacesContext()
    {
        assertEquals(-1, _dataTable.getRowIndex());
        String baseClientId = _dataTable.getClientId(_ctx);
        assertNotNull(baseClientId);
        Collection rowClientIds = new HashSet();
        for (int i = 0; i < 10; i++)
        {
            _dataTable.setRowIndex(i);
            assertTrue("Duplicate client id while iterating rows",
                    rowClientIds.add(_dataTable.getClientId(_ctx)));
        }
        _dataTable.setRowIndex(-1);
        assertEquals(baseClientId, _dataTable.getClientId(_ctx));
    }


    /*
    * Test method for
    * 'org.apache.myfaces.component.html.ext.HtmlDataTable.findComponent(String clientId)'
    */
    public void testFindComponent()
    {
        UIInput input = createInputInTree(_ctx);

        UIData data = (UIData) input.getParent().getParent();

        UIComponent comp = data.findComponent(":data:1:input");

        assertTrue(comp instanceof UIInput);

        UIInput uiInput = (UIInput) comp;

        assertEquals(uiInput.getValue(),"test2");

    }

    private UIInput createInputInTree(FacesContext context)
    {
        UIViewRoot viewRoot = new UIViewRoot();
        viewRoot.setId("root");

        UIData uiData = new HtmlDataTable();
        uiData.setId("data");

        List li = new ArrayList();
        li.add(new TestData("test1","test1"));
        li.add(new TestData("test2","test2"));
        li.add(new TestData("test3","test3"));

        uiData.setValue(new ListDataModel(li));
        uiData.setVar("detail");

        UIColumn column = new UIColumn();

        uiData.getChildren().add(column);

        UIInput input = new UIInput();
        input.setId("input");
        input.setValueBinding("value",createValueBinding("#{detail.description}"));

        column.getChildren().add(input);

        viewRoot.getChildren().add(uiData);

        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
          MockApplicationFactory.class.getName());

        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
          MockRenderKitFactory.class.getName());

        context.setViewRoot(viewRoot);

        MockFacesContextHelper.setCurrentInstance(context);
        return input;
    }

    private ValueBinding createValueBinding(String expr)
    {
        return _ctx.getApplication().createValueBinding(expr);
    }

    public static class TestData implements Serializable
    {
        private String _description;
        private String _value;

        public TestData()
        {

        }

        TestData(String description, String value)
        {
            _description = description;
            _value = value;
        }

        public String getDescription()
        {
            return _description;
        }

        public void setDescription(String description)
        {
            _description = description;
        }

        public String getValue()
        {
            return _value;
        }

        public void setValue(String value)
        {
            _value = value;
        }
    }

}
