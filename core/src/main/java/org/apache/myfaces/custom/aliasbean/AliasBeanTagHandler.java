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
package org.apache.myfaces.custom.aliasbean;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;

/**
 * Tag handler used in facelets
 * 
 * @since 1.1.7
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 */
public class AliasBeanTagHandler extends ComponentHandler
{
    private TagAttribute valueAttr;
    private TagAttribute aliasAttr;

    public AliasBeanTagHandler(ComponentConfig tagConfig)
    {
        super(tagConfig);

        valueAttr = getRequiredAttribute("value");
        aliasAttr = getRequiredAttribute("alias");
    }

    protected void setAttributes(FaceletContext ctx, Object instance)
    {
        super.setAttributes(ctx, instance);
        
        Application app = ctx.getFacesContext().getApplication();
        
        AliasBean aliasBean = (AliasBean) instance;

        String value = valueAttr.getValue();
        if (UIComponentTag.isValueReference(value))
        {
            aliasBean.setValueBinding("value", app.createValueBinding(valueAttr
                    .getValue()));
        }
        else
        {
            aliasBean.setValue(value);
        }

        String alias = aliasAttr.getValue();
        if (UIComponentTag.isValueReference(alias))
        {
            aliasBean.setValueBinding("alias", app.createValueBinding(aliasAttr
                    .getValue()));
        }
        else
        {
            aliasBean.setAlias(alias);
        }
    }
    
    protected void applyNextHandler(FaceletContext ctx, UIComponent component)
            throws IOException, FacesException, ELException
    {
        AliasBean aliasBean = (AliasBean) component;
        aliasBean.makeAlias(ctx.getFacesContext());
        super.applyNextHandler(ctx, component);
        aliasBean.removeAlias(ctx.getFacesContext());
    }
    
    /**
     * We have to add the children to the parent, for ensure proper
     * behavior between aliasBean and aliasBeansScope.
     * 
     */
    protected void onComponentCreated(FaceletContext faceletcontext,
            UIComponent component, UIComponent parent)    
    {
        parent.getChildren().add(component);
    }

}
