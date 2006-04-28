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


import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlPanelGroup;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsibleMenu extends HtmlPanelGroup implements NamingContainer {

    private static final Log log = LogFactory.getLog(CollapsibleMenu.class);
    
    public String getClientIdOfActivePanel(FacesContext facesContext) {
        Iterator it = this.getChildren().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!(o instanceof CollapsiblePanel)) continue;
            CollapsiblePanel panel = (CollapsiblePanel) o;
            if (panel.isDisplayed()) {
                return panel.getClientId(facesContext);
            }
        }
        return null;
    }   
        
    public void decode(FacesContext context)
    {
        return;
    }
        
    public static final String COMPONENT_TYPE = "org.apache.myfaces.CollapsibleMenu";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.CollapsibleMenu";
    
    private String _top = null;
    private String _left = null;
    private String _width = null;
    private String _height = null;
    private Boolean _preprocessed = Boolean.FALSE;
     
    public CollapsibleMenu()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }
    
    public String getHeight() {
        return _height;
    }
    public void setHeight(String height) {
        _height = height;
    }
    public String getLeft() {
        return _left;
    }
    public void setLeft(String left) {
        _left = left;
    }
    public String getTop() {
        return _top;
    }
    public void setTop(String top) {
        _top = top;
    }
    public String getWidth() {
        return _width;
    }
    public void setWidth(String width) {
        _width = width;
    }
    
    public void setPreprocessed(Boolean preprocessed) {
        _preprocessed = preprocessed;
    }
    
    public Boolean getPreprocessed() {
        return _preprocessed;
    }
    
    
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _top;
        values[2] = _left;
        values[3] = _height;
        values[4] = _width;
        values[5] = _preprocessed;
        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _top = (String)values[1];
        _left = (String)values[2];
        _height = (String)values[3];
        _width = (String)values[4];
        _preprocessed = (Boolean) values[5];
        
    }
}
