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
package org.apache.myfaces.custom.suggestajax.inputsuggestajax;

import javax.faces.application.Application;
import javax.faces.el.ValueBinding;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.custom.facelets.tag.MethodRule;
import org.apache.myfaces.custom.suggestajax.AbstractSuggestAjaxTag;
import org.apache.myfaces.custom.suggestajax.SuggestAjax;

public class InputSuggestAjaxTagHandler extends ComponentHandler
{
    
    private static final Class[] itemLabelMethodParamList = new Class[]{Object.class};
    
    private static final String ITEM_LABEL_METHOD = "itemLabelMethod";
    private static final String SUGGESTED_ITEMS_METHOD = "suggestedItemsMethod";
    private static final String MAX_SUGGESTED_ITEMS = "maxSuggestedItems";

    private TagAttribute suggestedItemsMethodAttr;
    private TagAttribute maxSuggestedItemsAttr;
    
    public InputSuggestAjaxTagHandler(ComponentConfig config)
    {
        super(config);
        suggestedItemsMethodAttr = getAttribute(SUGGESTED_ITEMS_METHOD);
        maxSuggestedItemsAttr = getAttribute(MAX_SUGGESTED_ITEMS);
    }
    
    public void setAttributes(FaceletContext ctx, Object instance)
    {
        super.setAttributes(ctx, instance);

        Application app = ctx.getFacesContext().getApplication();
        
        SuggestAjax comp = (SuggestAjax) instance;
        
        if (maxSuggestedItemsAttr != null){
            String maxSuggestedItems = maxSuggestedItemsAttr.getValue();
            
            if (maxSuggestedItems != null)
            {
                if (UIComponentTag.isValueReference(maxSuggestedItems))
                {
                    ValueBinding vb = app.createValueBinding(maxSuggestedItems);
                    comp.setValueBinding(MAX_SUGGESTED_ITEMS, vb);
                }
                else
                {
                    comp.getAttributes().put(MAX_SUGGESTED_ITEMS, Integer.valueOf(maxSuggestedItems));
                }
            } 
        }
        
        if (suggestedItemsMethodAttr != null){
            String suggestedItemsMethod = suggestedItemsMethodAttr.getValue();
            if (suggestedItemsMethod != null)
            {
                AbstractSuggestAjaxTag.setSuggestedItemsMethodProperty(ctx.getFacesContext(),
                        comp,suggestedItemsMethod);
            }
        }
    }
    
    protected MetaRuleset createMetaRuleset(Class type)
    {
        return super.createMetaRuleset(type).alias("class", "styleClass").addRule(
                new MethodRule(ITEM_LABEL_METHOD, 
                        String.class, itemLabelMethodParamList));        
    }
    
}
