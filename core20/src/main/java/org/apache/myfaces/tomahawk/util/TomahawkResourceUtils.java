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
package org.apache.myfaces.tomahawk.util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

public class TomahawkResourceUtils
{
    public static final String DEFAULT_SCRIPT_RENDERER_TYPE = "javax.faces.resource.Script";
    public static final String DEFAULT_STYLESHEET_RENDERER_TYPE = "javax.faces.resource.Stylesheet";
    public static final String HEAD_LOCATION = "head"; 
    
    public static void addOutputScriptResource(final FacesContext facesContext, final String libraryName, final String resourceName)
    {
        UIOutput outputScript = (UIOutput) facesContext.getApplication().
            createComponent(facesContext, "javax.faces.Output", DEFAULT_SCRIPT_RENDERER_TYPE);
        outputScript.getAttributes().put("library", libraryName);
        outputScript.getAttributes().put("name", resourceName);
        outputScript.setTransient(true);
        outputScript.setId(facesContext.getViewRoot().createUniqueId());
        facesContext.getViewRoot().addComponentResource(facesContext, outputScript);
    }
    
    public static void addOutputStylesheetResource(final FacesContext facesContext, final String libraryName, final String resourceName)
    {
        UIOutput outputStylesheet = (UIOutput) facesContext.getApplication().
            createComponent(facesContext, "javax.faces.Output", DEFAULT_STYLESHEET_RENDERER_TYPE);
        outputStylesheet.getAttributes().put("library", libraryName);
        outputStylesheet.getAttributes().put("name", resourceName);
        outputStylesheet.setTransient(true);
        outputStylesheet.setId(facesContext.getViewRoot().createUniqueId());
        facesContext.getViewRoot().addComponentResource(facesContext, outputStylesheet);
    }
    
    public static void addInlineOutputStylesheetResource(final FacesContext facesContext, Object value)
    {
        UIOutput stylesheet = (UIOutput) facesContext.getApplication().createComponent(facesContext, 
                "javax.faces.Output", "javax.faces.Text");
        UIOutput outputStylesheet = (UIOutput) facesContext.getApplication().
            createComponent(facesContext, "javax.faces.Output", DEFAULT_STYLESHEET_RENDERER_TYPE);
        stylesheet.setValue( value);
        stylesheet.setTransient(true);
        stylesheet.setId(facesContext.getViewRoot().createUniqueId());
        outputStylesheet.getChildren().add(stylesheet);
        outputStylesheet.setTransient(true);
        outputStylesheet.setId(facesContext.getViewRoot().createUniqueId());
        facesContext.getViewRoot().addComponentResource(facesContext, outputStylesheet);
    }
    
    public static void addInlineOutputScriptResource(final FacesContext facesContext, String target, Object value)
    {
        UIOutput script = (UIOutput) facesContext.getApplication().createComponent(facesContext, 
                "javax.faces.Output", "javax.faces.Text");
        UIOutput outputScript = (UIOutput) facesContext.getApplication().
            createComponent(facesContext, "javax.faces.Output", DEFAULT_SCRIPT_RENDERER_TYPE);
        if (target != null)
        {
            script.getAttributes().put("target", target);
        }
        script.setValue( value);
        script.setTransient(true);
        script.setId(facesContext.getViewRoot().createUniqueId());
        outputScript.getChildren().add(script);
        outputScript.setTransient(true);
        outputScript.setId(facesContext.getViewRoot().createUniqueId());
        facesContext.getViewRoot().addComponentResource(facesContext, outputScript);
    }
    
    public static String getIconSrc(final FacesContext facesContext, final String libraryName, final String resourceName)
    {
        final ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        final Resource resource;
        
        if ((libraryName != null) && (libraryName.length() > 0))
        {
            resource = resourceHandler.createResource(resourceName, libraryName);
        }
        else
        {
            resource = resourceHandler.createResource(resourceName);    
        }
        
        if (resource == null)
        {
            return "RES_NOT_FOUND";
        }
        else
        {
            return resource.getRequestPath();
        }
    }
}
