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
package org.apache.myfaces.custom.collapsiblemenu;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsibleIcon extends HtmlCommandLink {
    
    public static final String COMPONENT_TYPE = "org.apache.myfaces.CollapsibleIcon";
    public static final String COMPONENT_FAMILY = "javax.faces.GraphicImage";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.CollapsibleIcon";    
    
    private String _url;
    private String _title;
    
    public CollapsibleIcon()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }
        
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public String getTitle() {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }
    
    public void setTitle(String title) {
        _title = title;
    }
    public String getUrl() {
        return _url;
    }
    public void setUrl(String url) {
        _url = url;
    }
        
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _title;
        values[2] = _url;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _title = (String) values[1];
        _url = (String) values[2];
    }
}

