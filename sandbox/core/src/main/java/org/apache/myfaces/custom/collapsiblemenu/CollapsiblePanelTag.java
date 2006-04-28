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

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;
import javax.faces.component.UIComponent;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsiblePanelTag extends UIComponentTagBase
{

    private String _title;
    private String _displayed;
        
    public void release() {
        super.release();
        _title = null;
        _displayed = null;
    }

    public String getComponentType()
    {
        return CollapsiblePanel.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "title", _title);
        setBooleanProperty(component, "displayed", _displayed);
    }
       
    public String getTitle() {
        return _title;
    }
    public void setTitle(String _title) {
        this._title = _title;
    }
    
    public String getDisplayed() {
        return _displayed;
    }
    
    public void setDisplayed(String displayed) {
        _displayed = displayed;
    }
}
