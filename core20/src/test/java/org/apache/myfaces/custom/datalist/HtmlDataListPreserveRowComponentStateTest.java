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
package org.apache.myfaces.custom.datalist;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.utils.TestUtils;

public class HtmlDataListPreserveRowComponentStateTest extends AbstractJsfTestCase
{

    public HtmlDataListPreserveRowComponentStateTest(String name)
    {
        super(name);
    }
    
    @Override
    protected void setUpRenderKit() throws Exception
    {
        super.setUpRenderKit();
        TestUtils.addDefaultRenderers(facesContext);
    }

    public static class RowData
    {
        private String text;

        public RowData(String text, String style)
        {
            super();
            this.text = text;
            this.style = style;
        }

        private String style;
        
        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public String getStyle()
        {
            return style;
        }

        public void setStyle(String style)
        {
            this.style = style;
        }
    }
    
    public void testPreserveRowComponentState1() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataList table = new HtmlDataList();
        //UIColumn column = new UIColumn();
        UIOutput text = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        //column.setId("column");
        text.setId("text");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        //table.getChildren().add(column);
        //column.getChildren().add(text);
        table.getChildren().add(text);

        //Simulate markInitialState call.
        facesContext.getAttributes().put("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE", Boolean.TRUE);
        root.markInitialState();
        table.markInitialState();
        //column.markInitialState();
        text.markInitialState();
        facesContext.getAttributes().remove("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE");
        
        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            text.getAttributes().put("style", rowData.getStyle());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getStyle(), text.getAttributes().get("style"));
        }
        
    }
    
    public void testPreserveRowComponentState1Facet() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataList table = new HtmlDataList();
        UIPanel panel = new UIPanel();
        UIOutput text = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        panel.setId("column");
        text.setId("text");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(panel);
        panel.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, text);

        //Simulate markInitialState call.
        facesContext.getAttributes().put("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE", Boolean.TRUE);
        root.markInitialState();
        table.markInitialState();
        panel.markInitialState();
        text.markInitialState();
        facesContext.getAttributes().remove("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE");
        
        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            text.getAttributes().put("style", rowData.getStyle());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getAttributes().get("value"));
            assertEquals(model.get(i).getStyle(), text.getAttributes().get("style"));
        }
        
    }
    
    public void testState1Facet() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataList table = new HtmlDataList();
        UIPanel panel = new UIPanel();
        UIOutput text = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        panel.setId("column");
        text.setId("text");
        
        table.setVar("row");
        //table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(panel);
        panel.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, text);

        //Simulate markInitialState call.
        facesContext.getAttributes().put("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE", Boolean.TRUE);
        root.markInitialState();
        table.markInitialState();
        panel.markInitialState();
        text.markInitialState();
        facesContext.getAttributes().remove("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE");
        
        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            text.getAttributes().put("style", rowData.getStyle());
        }
        
        //Reset row index
        table.setRowIndex(-1);

        //Check the values were not lost
        for (int i = 0; i < model.size(); i++)
        {
            table.setRowIndex(i);
            assertEquals(model.get(i).getText(), text.getAttributes().get("value"));
        }
        
    }
    
    public void testPreserveRowComponentState2() throws Exception
    {
        List<RowData> model = new ArrayList<RowData>();
        model.add(new RowData("text1","style1"));
        model.add(new RowData("text1","style2"));
        model.add(new RowData("text1","style3"));
        model.add(new RowData("text1","style4"));
        
        //Put on request map to be resolved later
        request.setAttribute("list", model);
        
        UIViewRoot root = facesContext.getViewRoot();
        HtmlDataList table = new HtmlDataList();
        UIPanel column = new UIPanel();
        UIOutput text = new UIOutput();
        
        //This is only required if markInitiaState fix is not used 
        root.setId("root");
        table.setId("table");
        column.setId("column");
        text.setId("text");
        
        table.setVar("row");
        table.setPreserveRowComponentState(true);
        table.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{list}",List.class));
        
        text.setValueExpression("value", application.
                getExpressionFactory().createValueExpression(
                        facesContext.getELContext(),"#{row.text}",String.class));
        
        root.getChildren().add(table);
        table.getChildren().add(column);
        column.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, text);

        //Simulate markInitialState call.
        facesContext.getAttributes().put("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE", Boolean.TRUE);
        root.markInitialState();
        table.markInitialState();
        column.markInitialState();
        text.markInitialState();
        facesContext.getAttributes().remove("javax.faces.view.ViewDeclarationLanguage.IS_BUILDING_INITIAL_STATE");
        
        //Check the value expressions are working and change the component state 
        for (int i = 0; i < model.size(); i++)
        {
            RowData rowData = model.get(i); 
            table.setRowIndex(i);
            assertEquals(rowData.getText(), text.getValue());
            text.getAttributes().put("style", rowData.getStyle());
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
            assertEquals(model.get(i).getStyle(), text.getAttributes().get("style"));
        }
        
    }

}
