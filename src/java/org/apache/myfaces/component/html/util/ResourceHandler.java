/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.component.html.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Mathias Broekelmann
 *
 */
public interface ResourceHandler
{
    /**
     * Returns the resource loader class which is used to load the resource 
     * 
     * @return
     */
    Class getResourceLoaderClass(); 
    
    /**
     * Returns the uri part which is used by the resourceloader to identify the resource to load. 
     * 
     * @param context
     * @return the returned resource uri will be passed as the resourceUri parameter for 
     * the ResourceLoader.serveResource method (omitting request parameters) 
     * @see ResourceLoader#serveResource(HttpServletRequest, HttpServletResponse, String)
     */
    String getResourceUri(FacesContext context);
    
    /**
     * must be implemented to avoid loading the same resource multiple times.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode();
    
    /**
     * must be implemented to avoid loading the same resource multiple times.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj);
}
