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
package org.apache.myfaces.component.html.ext;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.ListDataModel;


import org.apache.myfaces.component.html.ext.HtmlDataTablePreserveRowComponentStateTest.RowData;
import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.TestUtils;

/**
 * @author Mathias Br�kelmann (latest modification by $Author: paulsp $)
 * @version $Revision: 491829 $ $Date: 2007-01-02 11:13:29 -0500 (mar, 02 ene 2007) $
 */
public class HtmlDataTableTest extends AbstractTomahawkViewControllerTestCase
{

    private HtmlDataTable _dataTable;

    public HtmlDataTableTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        _dataTable = new HtmlDataTable();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        _dataTable = null;
    }

    /*
     * Test method for
     * 'org.apache.myfaces.component.html.ext.HtmlDataTable.getClientId(FacesContext)'
     */
    public void testGetClientIdFacesContext()
    {
        assertEquals(-1, _dataTable.getRowIndex());
        String baseClientId = _dataTable.getClientId(facesContext);
        assertNotNull(baseClientId);
        Collection rowClientIds = new HashSet();
        for (int i = 0; i < 10; i++)
        {
            _dataTable.setRowIndex(i);
            assertTrue("Duplicate client id while iterating rows", rowClientIds
                    .add(_dataTable.getContainerClientId(facesContext)));
        }
        _dataTable.setRowIndex(-1);
        assertEquals(baseClientId, _dataTable.getContainerClientId(facesContext));
    }

    /*
     * Test method for
     * 'org.apache.myfaces.component.html.ext.HtmlDataTable.findComponent(String clientId)'
     */
    public void testFindComponent()
    {
        UIInput input = createInputInTree(facesContext);

        UIData data = (UIData) input.getParent().getParent();

        UIComponent comp = data.findComponent(":data:1:input");

        assertTrue(comp instanceof UIInput);

        UIInput uiInput = (UIInput) comp;

        assertEquals(uiInput.getValue(), "test2");

    }

    /**
     * Verify component renders with the default renderer.
     */
    public void testDefaultRenderer()
    {
        // Define the component
        UIData component = new HtmlDataTable();
        component.setId("TestComponent");

        // Add rows to the table
        List rows = new ArrayList();
        rows.add(new TestData("test1", "test1"));
        rows.add(new TestData("test2", "test2"));
        rows.add(new TestData("test3", "test3"));
        component.setValue(new ListDataModel(rows));

        // Render the component
        try
        {
            TestUtils.renderComponent(facesContext, component);
        }
        catch (IOException e)
        {
            fail(e.getMessage());
        }

        // Verify component was rendered
        assertIdExists(component.getId());
    }

    private UIInput createInputInTree(FacesContext context)
    {

        UIData uiData = new HtmlDataTable();
        uiData.setId("data");

        List li = new ArrayList();
        li.add(new TestData("test1", "test1"));
        li.add(new TestData("test2", "test2"));
        li.add(new TestData("test3", "test3"));

        uiData.setValue(new ListDataModel(li));
        uiData.setVar("detail");

        UIColumn column = new UIColumn();

        uiData.getChildren().add(column);

        UIInput input = new UIInput();
        input.setId("input");
        input.setValueBinding("value",
                createValueBinding("#{detail.description}"));

        column.getChildren().add(input);

        facesContext.getViewRoot().getChildren().add(uiData);

        return input;
    }

    private ValueBinding createValueBinding(String expr)
    {
        return facesContext.getApplication().createValueBinding(expr);
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

    public void testDetailStampRowState1() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataTable table = new HtmlDataTable();
        UIColumn column = new UIColumn();
        UIPanel detailStampPanel = new UIPanel();
        UIOutput text = new UIOutput();
        UIOutput detailStampText = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        detailStampPanel.setId("detailStamp");
        column.setId("column");
        text.setId("text");
        detailStampText.setId("detailStampText");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));

        detailStampText.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(column);
        table.getFacets().put(AbstractHtmlDataTable.DETAIL_STAMP_FACET_NAME, detailStampPanel);
        column.getChildren().add(text);
        detailStampPanel.getChildren().add(detailStampText);

        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            assertEquals(rowData.getText(), detailStampText.getValue());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getValue());
            assertEquals(model.get(i).getText(), detailStampText.getValue());
        }
        
    }
    
    public void testDetailStampRowState2() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataTable table = new HtmlDataTable();
        UIColumn column = new UIColumn();
        UIPanel detailStampPanel = new UIPanel();
        UIOutput text = new UIOutput();
        UIOutput detailStampText = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        detailStampPanel.setId("detailStamp");
        column.setId("column");
        text.setId("text");
        detailStampText.setId("detailStampText");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));

        detailStampText.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(column);
        table.getFacets().put(AbstractHtmlDataTable.DETAIL_STAMP_FACET_NAME, detailStampPanel);
        column.getChildren().add(text);
        detailStampPanel.getChildren().add(detailStampText);

        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            assertEquals(rowData.getText(), detailStampText.getValue());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Remove a row
        table.deleteRowStateForRow(1);
        model.remove(1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getValue());
            assertEquals(model.get(i).getText(), detailStampText.getValue());
        }
    }
    
    public void testDetailStampRowState3() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataTable table = new HtmlDataTable();
        UIColumn column = new UIColumn();
        UIPanel detailStampPanel = new UIPanel();
        UIOutput text = new UIOutput();
        UIOutput detailStampText = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        detailStampPanel.setId("detailStamp");
        column.setId("column");
        text.setId("text");
        detailStampText.setId("detailStampText");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));

        detailStampText.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(column);
        table.getFacets().put(AbstractHtmlDataTable.DETAIL_STAMP_FACET_NAME, detailStampPanel);
        column.getChildren().add(text);
        detailStampPanel.getChildren().add(detailStampText);

        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            assertEquals(rowData.getText(), detailStampText.getValue());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Remove a row
        table.deleteRowStateForRow(1);
        model.remove(1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getValue());
            assertEquals(model.get(i).getText(), detailStampText.getValue());
        }
        
        table.expandAllDetails();
        
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertTrue(table.isDetailExpanded());
        }
        
        table.collapseAllDetails();
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertFalse(table.isDetailExpanded());
        }        
    }
    
    public void testDetailStampRowState4() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataTable table = new HtmlDataTable();
        UIColumn column = new UIColumn();
        UIPanel detailStampPanel = new UIPanel();
        UIOutput text = new UIOutput();
        UIOutput detailStampText = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        detailStampPanel.setId("detailStamp");
        column.setId("column");
        text.setId("text");
        detailStampText.setId("detailStampText");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        table.setRows(2);
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));

        detailStampText.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(column);
        table.getFacets().put(AbstractHtmlDataTable.DETAIL_STAMP_FACET_NAME, detailStampPanel);
        column.getChildren().add(text);
        detailStampPanel.getChildren().add(detailStampText);

        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            assertEquals(rowData.getText(), detailStampText.getValue());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Remove a row
        table.deleteRowStateForRow(1);
        model.remove(1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getValue());
            assertEquals(model.get(i).getText(), detailStampText.getValue());
        }
        
        table.expandAllPageDetails();
        
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            if (i < 2)
            {
                assertTrue(table.isDetailExpanded());
            }
            else
            {
                assertFalse(table.isDetailExpanded());
            }
        }
        
        table.collapseAllPageDetails();
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertFalse(table.isDetailExpanded());
        }        
    }
}
