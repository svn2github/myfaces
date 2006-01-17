/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.custom.newspaper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlDataTable;

/**
 * Model for a table in multiple balanced columns.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public class HtmlNewspaperTable
        extends HtmlDataTable
{
    /** the component's renderers and type */
    public static final String RENDERER_TYPE = "org.apache.myfaces.HtmlNewspaperTable";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlNewspaperTable";

    /** the property names */
    public static final String NEWSPAPER_COLUMNS_PROPERTY = "newspaperColumns";
    public static final String SPACER_FACET_NAME = "spacer";
    
    /** the value of the column count property */
    private int newspaperColumns = 1;

    public HtmlNewspaperTable() {
        setRendererType(RENDERER_TYPE);
    }

    /**
     * Set the number of columns the table will be divided over.
     */
    public int getNewspaperColumns() {
        return newspaperColumns;
    }
    public void setNewspaperColumns(int newspaperColumns) {
        this.newspaperColumns = newspaperColumns;
    }
    
    /**
     * Gets the spacer facet, between each pair of newspaper columns.
     */
    public UIComponent getSpacer() {
        return (UIComponent)getFacets().get(SPACER_FACET_NAME);
    }
    public void setSpacer(UIComponent spacer) {
        getFacets().put(SPACER_FACET_NAME, spacer);
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = new Integer(newspaperColumns);
        return values;
    }
    
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        newspaperColumns = ((Integer)values[1]).intValue();
    }
}