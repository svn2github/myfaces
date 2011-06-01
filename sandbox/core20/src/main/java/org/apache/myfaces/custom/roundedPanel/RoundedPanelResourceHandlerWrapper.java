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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.shared_tomahawk.resource.ResourceHandlerSupport;
import org.apache.myfaces.shared_tomahawk.resource.ResourceLoader;
import org.apache.myfaces.shared_tomahawk.resource.ResourceMeta;
import org.apache.myfaces.shared_tomahawk.util.ExternalContextUtils;
import org.apache.myfaces.shared_tomahawk.util.StringUtils;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelResourceHandlerWrapper extends ResourceHandlerWrapper
{
    public final static String ROUNDED_PANEL_LIBRARY = "oam.custom.roundedPanel";

    private RoundedPanelResouceHandlerSupport _resourceHandlerSupport;

    private ResourceHandler delegate;

    public RoundedPanelResourceHandlerWrapper(ResourceHandler delegate)
    {
        super();
        this.delegate = delegate;
        this._resourceHandlerSupport = new RoundedPanelResouceHandlerSupport();
    }

    @Override
    public ResourceHandler getWrapped()
    {
        return delegate;
    }
    
    @Override
    public Resource createResource(String resourceName, String libraryName)
    {
        if (libraryName != null && ROUNDED_PANEL_LIBRARY.equals(libraryName))
        {
            return internalCreateResource(resourceName, libraryName);
        }
        else
        {
            return super.createResource(resourceName, libraryName);
        }
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType)
    {
        if (libraryName != null && ROUNDED_PANEL_LIBRARY.equals(libraryName))
        {
            return internalCreateResource(resourceName, libraryName);
        }
        else
        {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }
    
    private Resource internalCreateResource(String resourceName, String libraryName)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Resource resource = null;
        
        //ResourceMeta resourceMeta = new ResourceMetaImpl(null, libraryName, null, resourceName, null);
        
        for (ResourceLoader loader : getResourceHandlerSupport().getResourceLoaders())
        {
            ResourceMeta resourceMeta = deriveResourceMeta(loader, resourceName, libraryName, null);
            
            if (resourceMeta != null)
            {
                resource = new RoundedPanelResource(resourceMeta, loader, getResourceHandlerSupport());
                
                break;
            }
        }
        
        //return new RoundedPanelResource(resourceMeta, support, properties, part)
        
        //TODO: Implement me
        return resource;
        /*
        if (isResourceRequest(facesContext))
        {
            //TODO: Implement me
            return null;
        }
        else
        {
            //TODO: Implement me
            return null;
        }*/
    }
    
    protected ResourceMeta deriveResourceMeta(ResourceLoader resourceLoader,
            String resourceName, String libraryName, String localePrefix)
    {
        ResourceMeta resourceId = resourceLoader.createResourceMeta(null, libraryName, null, resourceName, null);
        
        return resourceId;
    }
    
    @Override
    public void handleResourceRequest(FacesContext facesContext) throws IOException
    {
        final Map<String, String> requestMap = facesContext.getExternalContext().getRequestParameterMap();
        final String libraryName = requestMap.get("ln");
        
        if (libraryName != null && ROUNDED_PANEL_LIBRARY.equals(libraryName) && !_resourceHandlerSupport.isCacheDiskRoundedImages())
        {
            String resourceBasePath = getResourceHandlerSupport()
                .calculateResourceBasePath(facesContext);

            if (resourceBasePath == null)
            {
                // No base name could be calculated, so no further
                //advance could be done here. HttpServletResponse.SC_NOT_FOUND
                //cannot be returned since we cannot extract the 
                //resource base name
                return;
            }
        
            // We neet to get an instance of HttpServletResponse, but sometimes
            // the response object is wrapped by several instances of 
            // ServletResponseWrapper (like ResponseSwitch).
            // Since we are handling a resource, we can expect to get an 
            // HttpServletResponse.
            Object response = facesContext.getExternalContext().getResponse();
            HttpServletResponse httpServletResponse = ExternalContextUtils.getHttpServletResponse(response);
            if (httpServletResponse == null)
            {
                throw new IllegalStateException("Could not obtain an instance of HttpServletResponse.");
            }
        
            if (isResourceIdentifierExcluded(facesContext, resourceBasePath))
            {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        
            String resourceName = null;
            if (resourceBasePath.startsWith(ResourceHandler.RESOURCE_IDENTIFIER))
            {
                resourceName = resourceBasePath
                        .substring(ResourceHandler.RESOURCE_IDENTIFIER.length() + 1);
            }
            else
            {
                //Does not have the conditions for be a resource call
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        
            Resource resource = facesContext.getApplication().getResourceHandler().createResource(resourceName, libraryName);
        
            if (resource == null)
            {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        
            if (!resource.userAgentNeedsUpdate(facesContext))
            {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        
            httpServletResponse.setContentType(_getContentType(resource, facesContext.getExternalContext()));
        
            Map<String, String> headers = resource.getResponseHeaders();
        
            for (Map.Entry<String, String> entry : headers.entrySet())
            {
                httpServletResponse.setHeader(entry.getKey(), entry.getValue());
            }

            RoundedPanelPropertiesBuilder roundedPanelPropertiesBuilder = 
                _resourceHandlerSupport.getRoundedPanelPropertiesBuilder();
            
            if (!roundedPanelPropertiesBuilder.isValidResourceName(facesContext, resourceName))
            {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            //serve up the bytes (taken from trinidad ResourceServlet)
            try
            {
                OutputStream out = httpServletResponse.getOutputStream();
                try
                {
                    roundedPanelPropertiesBuilder.generateRoundedPanelImage(facesContext, out, resourceName); 
                    
                    //set the content lenght
                    //httpServletResponse.setContentLength(count);
                }
                finally
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                Logger log = Logger.getLogger(RoundedPanelResourceHandlerWrapper.class.getName());
                if (log.isLoggable(Level.SEVERE))
                    log.severe("Error trying to load resource " + resourceName
                            + " with library " + libraryName + " :"
                            + e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        else
        {
            super.handleResourceRequest(facesContext);
        }
    }
    
    protected boolean isResourceIdentifierExcluded(FacesContext context,
            String resourceIdentifier)
    {
        String value = context.getExternalContext().getInitParameter(
                RESOURCE_EXCLUDES_PARAM_NAME);
        if (value == null)
        {
            value = RESOURCE_EXCLUDES_DEFAULT_VALUE;
        }
        //TODO: optimize this code
        String[] extensions = StringUtils.splitShortString(value, ' ');
        for (int i = 0; i < extensions.length; i++)
        {
            if (resourceIdentifier.endsWith(extensions[i]))
            {
                return true;
            }
        }
        return false;
    }
    
    private String _getContentType(Resource resource, ExternalContext externalContext)
    {
        String contentType = resource.getContentType();

        // the resource does not provide a content-type --> determine it via mime-type
        if (contentType == null || contentType.length() == 0)
        {
            String resourceName = getWrappedResourceName(resource);

            if (resourceName != null)
            {
                contentType = externalContext.getMimeType(resourceName);
            }
        }

        return contentType;
    }

    /**
     * Recursively unwarp the resource until we find the real resourceName
     * This is needed because the JSF2 specced ResourceWrapper doesn't override
     * the getResourceName() method :(
     * @param resource
     * @return the first non-null resourceName or <code>null</code> if none set
     */
    private String getWrappedResourceName(Resource resource)
    {
        String resourceName = resource.getResourceName();
        if (resourceName != null)
        {
            return resourceName;
        }

        if (resource instanceof ResourceWrapper)
        {
            return getWrappedResourceName(((ResourceWrapper) resource).getWrapped());
        }

        return null;
    }


    @Override
    public boolean libraryExists(String libraryName)
    {
        if (libraryName != null && ROUNDED_PANEL_LIBRARY.equals(libraryName))
        {
            return true;
        }
        else
        {
            return super.libraryExists(libraryName);
        }
    }
    
    
    
    /**
     * @return the resourceHandlerSupport
     */
    protected ResourceHandlerSupport getResourceHandlerSupport()
    {
        return _resourceHandlerSupport;
    }
    
}
