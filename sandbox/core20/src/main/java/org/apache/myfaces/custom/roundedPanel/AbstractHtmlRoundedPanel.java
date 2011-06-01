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
package org.apache.myfaces.custom.roundedPanel;

import javax.faces.component.UIPanel;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFComponent;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;

/**
 * 
 * @author Leonardo Uribe (parts taken from sandbox 1.1 s:roundedDiv by Andrew Robinson) 
 *
 */
@JSFComponent(name="s:roundedPanel",
    clazz = "org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel")
public abstract class AbstractHtmlRoundedPanel extends UIPanel
{
    public static final String COMPONENT_FAMILY = "org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel";
    
    @Override
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    /**
     * The radius of the corners in pixels. (Default: 8)
     * 
     * @JSFProperty
     *   defaultValue = "Integer.valueOf(8)"
     * @return the radius
     */
    public abstract Integer getRadius();
    
    /**
     * Background color to give the corners. Leave blank (null) to have a transparent 
     * background. If the user is using IE6, this has to be set, or the corners 
     * will not look good due to IE6's lack of PNG support.
     * 
     * @JSFProperty
     * @return the backgroundColor
     */
    public abstract String getBackgroundColor(); 
    
    /**
     * The foreground color of the DIV
     * 
     * @JSFProperty
     * @return the color
     */
    public abstract String getColor();
    
    /**
     * Either "table" or "div". Specifies how the output should be rendered. 
     * Size must be null if using "table" (if it is not, a div will be rendered). 
     * (Default: div)
     * 
     * @JSFProperty
     *   defaultValue = "div"
     * @return
     */
    public abstract String getLayout();
    
    /**
     * HTML: CSS styling instructions.
     * 
     */
    @JSFProperty
    public abstract String getStyle();

    /**
     * The CSS class for this element.  Corresponds to the HTML 'class' attribute.
     * 
     */
    @JSFProperty
    public abstract String getStyleClass();
    
    /**
     * HTML: CSS styling instructions.
     * 
     */
    @JSFProperty
    public abstract String getContentStyle();

    /**
     * The CSS class for this element.  Corresponds to the HTML 'class' attribute.
     * 
     */
    @JSFProperty
    public abstract String getContentStyleClass();
    
    /**
     * Defines an optional library to load already generated images to be used by this component.
     * The idea is just save the images with the same names on a library folder, so the component
     * can use it without trigger image generation. 
     * 
     * @return
     */
    @JSFProperty
    public abstract String getLibrary();

}
