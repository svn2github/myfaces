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
package org.apache.myfaces.webapp.filter;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.tomahawk.util.ExternalContextUtils;

public class TomahawkFacesContextFactory extends FacesContextFactory {

    private FacesContextFactory delegate;

    public TomahawkFacesContextFactory(FacesContextFactory delegate) {
        this.delegate = delegate;
    }

    public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle) throws FacesException {
        
        if(!ExternalContextUtils.getRequestType(context, request).isPortlet())
        {
            //This is servlet world
            //For handle buffered response we need to wrap response object here,
            //so all response will be written and then on facesContext
            //release() method write to the original response.
            //This could not be done on TomahawkFacesContextWrapper
            //constructor, because the delegate ExternalContext do
            //calls like dispatch, forward and redirect, that requires
            //the wrapped response instance to work properly.
            AddResource addResource = AddResourceFactory.getInstance((HttpServletRequest)request,(ServletContext)context);
            
            if (addResource.requiresBuffer())
            {
                ExtensionsResponseWrapper extensionsResponseWrapper = new ExtensionsResponseWrapper((HttpServletResponse)response);
                return new TomahawkFacesContextWrapper(delegate.getFacesContext(context, request, extensionsResponseWrapper, lifecycle),
                        extensionsResponseWrapper);
            }
        }
        return new TomahawkFacesContextWrapper(delegate.getFacesContext(context, request, response, lifecycle));
    }
}
