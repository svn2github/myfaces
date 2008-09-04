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
package org.apache.myfaces.custom.tree.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.custom.tree.HtmlTree;
import org.apache.myfaces.custom.tree.model.DefaultTreeModel;
import org.apache.myfaces.custom.tree.model.TreeModel;
import org.apache.myfaces.custom.tree.model.TreePath;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.html.HtmlComponentHandler;

/**
 * 
 * @since 1.1.7
 */
public class TreeTagHandler extends HtmlComponentHandler
{

    private TagAttribute valueAttr;
    private TagAttribute expandRootAttr;
    
    private boolean expandRoot;
    
    public TreeTagHandler(ComponentConfig config)
    {
        super(config);
        valueAttr = getRequiredAttribute("value");
        expandRootAttr = getRequiredAttribute("expandRoot");
        expandRoot = false;
    }
    
    protected void setAttributes(FaceletContext ctx, Object instance)
    {
        super.setAttributes(ctx, instance);
        
        Application app = ctx.getFacesContext().getApplication();
        
        HtmlTree tree = (HtmlTree) instance;
        
        if(valueAttr != null)
        {
            String value = valueAttr.getValue();
            if (value != null && UIComponentTag.isValueReference(value))
            {
                tree.setValueBinding("model", app.createValueBinding(valueAttr
                        .getValue()));
            }
        }
        else
        {
            ValueBinding binding = tree.getValueBinding("model");
            if (binding == null) {
                binding = app.createValueBinding("#{sessionScope.tree}");
            }
            tree.setValueBinding("model", binding);            
        }
        
        if (expandRootAttr != null)
        {
            expandRoot = expandRootAttr.getBoolean(ctx);
        }
    }
    
    protected void onComponentCreated(FaceletContext ctx,
            UIComponent component, UIComponent parent)
    {
        FacesContext context = ctx.getFacesContext();
        Application app = ctx.getFacesContext().getApplication();
        
        if (valueAttr != null) {
            String value = valueAttr.getValue();
            if (value != null){
                ValueBinding valueBinding = app.createValueBinding(value);
                TreeModel treeModel = (TreeModel) (valueBinding.getValue(context));
        
                if (treeModel == null) {
                    // create default model
                    treeModel = new DefaultTreeModel();
                    valueBinding.setValue(context, treeModel);
                }
            }
        }
        
        HtmlTree tree = (HtmlTree) component;
        
        if (expandRoot)
        {
            // component was created, so expand the root node
            TreeModel model = tree.getModel(context);
    
            if (model != null) {
                tree.expandPath(new TreePath(new Object[] { model.getRoot() }),
                        context);
            }
        }
        tree.addToModelListeners();
    }
    
}
