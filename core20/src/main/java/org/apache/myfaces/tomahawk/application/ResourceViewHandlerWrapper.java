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
package org.apache.myfaces.tomahawk.application;

import java.io.IOException;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class ResourceViewHandlerWrapper extends ViewHandlerWrapper
{
    private ViewHandler _delegate;

    public ResourceViewHandlerWrapper(ViewHandler delegate)
    {
        super();
        _delegate = delegate;
    }

    @Override
    public ViewHandler getWrapped()
    {
        return _delegate;
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender)
            throws IOException, FacesException
    {
        _publishPreRenderViewAddResourceEvent(context, viewToRender);
        super.renderView(context, viewToRender);
    }
    
    private void _publishPreRenderViewAddResourceEvent(FacesContext context, UIComponent component)
    {
        context.getApplication().publishEvent(context,  PreRenderViewAddResourceEvent.class, UIComponent.class, component);
        
        //Scan children
        if (component.getChildCount() > 0)
        {
            for (UIComponent child : component.getChildren())
            {
                _publishPreRenderViewAddResourceEvent(context, child);
            }
        }

        //Scan facets
        if (component.getFacetCount() > 0)
        {
            for (Map.Entry<String, UIComponent> entry : component.getFacets().entrySet())
            {
                _publishPreRenderViewAddResourceEvent(context, entry.getValue());
            }
        }
    }
}
