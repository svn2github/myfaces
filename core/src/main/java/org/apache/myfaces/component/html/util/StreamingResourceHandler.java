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
package org.apache.myfaces.component.html.util;

import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.html.util.ResourceHandler;

/**
 * Class whose instances represent a virtual resource which will be collected by
 * the StreamingResourceHandler.
 * 
 * @author Mario Ivankovits
 */
public class StreamingResourceHandler implements ResourceHandler
{
    private final String _resource;

    /**
     * Constructor.
     * 
     * @param resourceName is the name of a file that can be found in dir
     *  "resource/{resourceName} relative to the location of the specified
     *  component class in the classpath.
     */
    public StreamingResourceHandler(String resourceName)
    {
        _resource = resourceName;
    }
    
    /**
     * Return a Class object which can decode the url generated by this
     * class in the getResourceUri method and use that info to locate
     * the resource data represented by this object.
     * 
     * @see org.apache.myfaces.shared.renderkit.html.util.ResourceHandler#getResourceLoaderClass()
     */
    public Class getResourceLoaderClass()
    {
        return StreamingResourceLoader.class;
    }

    /**
     * Return a URL that the browser can later submit to retrieve the resource
     * handled by this instance.
     * <p>
     * The emitted URL is of form:
     * <pre>
     *   {partial.class.name}/{resourceName}
     * </pre>
     * where partial.class.name is the name of the base class specified in the
     * constructor, and resourceName is the resource specified in the constructor.
     * 
     * @see org.apache.myfaces.shared.renderkit.html.util.ResourceHandler#getResourceUri(javax.faces.context.FacesContext)
     */
    public String getResourceUri(FacesContext context)
    {
    	return _resource;
    }
}
