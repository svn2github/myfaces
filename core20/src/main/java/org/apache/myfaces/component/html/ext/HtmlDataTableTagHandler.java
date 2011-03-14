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

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;

public class HtmlDataTableTagHandler extends ComponentHandler
{
    
    public HtmlDataTableTagHandler(ComponentConfig config)
    {
        super(config);
    }
    
    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c,
            UIComponent parent)
    {
         // Add a detailStampRow component, so detailStamp rendering will be delegated
         // to a component that can be used in ajax.
        UIComponent detailStampRow = ctx.getFacesContext().
            getApplication().createComponent(ctx.getFacesContext(),
                HtmlDetailStampRow.COMPONENT_TYPE, HtmlDetailStampRow.DEFAULT_RENDERER_TYPE);
        detailStampRow.setId("detailStampRow");
        c.getFacets().put(AbstractHtmlDataTable.DETAIL_STAMP_ROW_FACET_NAME, detailStampRow);
        
        // Add a special component that is used to render a row on an ajax operation.
        UIComponent row = ctx.getFacesContext().
            getApplication().createComponent(ctx.getFacesContext(),
                HtmlTableRow.COMPONENT_TYPE, HtmlTableRow.DEFAULT_RENDERER_TYPE);
        row.setId("row");
        c.getFacets().put(AbstractHtmlDataTable.TABLE_ROW_FACET_NAME, row);
    }

    @Override
    public void onComponentPopulated(FaceletContext ctx, UIComponent c,
            UIComponent parent)
    {
        //Check and remove detailStampRow component if there is no detailStamp
        UIComponent detailStamp = c.getFacet(AbstractHtmlDataTable.DETAIL_STAMP_FACET_NAME);
        if (detailStamp == null)
        {
            UIComponent detailStampRow = c.getFacet(AbstractHtmlDataTable.DETAIL_STAMP_ROW_FACET_NAME);
            if (detailStampRow != null)
            {
                c.getFacets().remove(AbstractHtmlDataTable.DETAIL_STAMP_ROW_FACET_NAME);
            }
        }
        
        HtmlDataTable dataTable = (HtmlDataTable) c;
        if (!dataTable.isAjaxRowRender())
        {
            c.getFacets().remove(AbstractHtmlDataTable.TABLE_ROW_FACET_NAME);
        }
    }

    protected MetaRuleset createMetaRuleset(Class type)
    {
        return super.createMetaRuleset(type).alias("class", "styleClass");
    }
}
