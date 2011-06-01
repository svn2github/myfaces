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
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared_tomahawk.resource.ResourceLoader;
import org.apache.myfaces.shared_tomahawk.resource.ResourceHandlerSupport;
import org.apache.myfaces.shared_tomahawk.resource.ResourceLoaderUtils;
import org.apache.myfaces.shared_tomahawk.resource.ResourceMeta;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelResource extends Resource
{
    private ResourceMeta _resourceMeta;
    private ResourceHandlerSupport _resourceHandlerSupport;
    private ResourceLoader _resourceLoader;

    public RoundedPanelResource(ResourceMeta resourceMeta,
            ResourceLoader resourceLoader, ResourceHandlerSupport support)
    {
        _resourceMeta = resourceMeta;
        _resourceLoader = resourceLoader;
        _resourceHandlerSupport = support;
        setLibraryName(resourceMeta.getLibraryName());
        setResourceName(resourceMeta.getResourceName());
        setContentType("image/png");
    }
    
    @Override
    public InputStream getInputStream() throws IOException
    {
        return getResourceLoader().getResourceInputStream(_resourceMeta);
    }

    @Override
    public URL getURL()
    {
        return getResourceLoader().getResourceURL(_resourceMeta);
    }
    
    public ResourceLoader getResourceLoader()
    {
        return _resourceLoader;
    }  

    @Override
    public String getRequestPath()
    {
        String path;
        if (_resourceHandlerSupport.isExtensionMapping())
        {
            path = ResourceHandler.RESOURCE_IDENTIFIER + '/' + 
                getResourceName() + _resourceHandlerSupport.getMapping();
        }
        else
        {
            String mapping = _resourceHandlerSupport.getMapping(); 
            path = ResourceHandler.RESOURCE_IDENTIFIER + '/' + getResourceName();
            path = (mapping == null) ? path : mapping + path;
        }
 
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String metadata = null;
        if (getLibraryName() != null)
        {
            metadata = "?ln=" + getLibraryName();
            path = path + metadata;
        }
        
        return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, path);
    }
    
    @Override
    public Map<String, String> getResponseHeaders()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (facesContext.getApplication().getResourceHandler().isResourceRequest(facesContext))
        {
            Map<String, String> headers = new HashMap<String, String>();
            
            long lastModified;
            try
            {
                URL url = this.getURL();
                
                if (url != null)
                {
                    lastModified = ResourceLoaderUtils.getResourceLastModified(url);
                }
                else
                {
                    lastModified = -1L;
                }
            }
            catch (IOException e)
            {
                lastModified = -1L;
            }
            
            if (lastModified < _resourceHandlerSupport.getStartupTime())
            {
                lastModified = _resourceHandlerSupport.getStartupTime();
            }            

            if (lastModified >= 0L)
            {
                headers.put("Last-Modified", ResourceLoaderUtils.formatDateHeader(lastModified));
                
                long expires;
                if (facesContext.isProjectStage(ProjectStage.Development))
                {
                    // Force to expire now to prevent caching on development time.
                    expires = System.currentTimeMillis();
                }
                else
                {
                    expires = System.currentTimeMillis() + _resourceHandlerSupport.getMaxTimeExpires();
                }
                headers.put("Expires", ResourceLoaderUtils.formatDateHeader(expires));
            }
            
            return headers;
        }
        else
        {
            //No need to return headers 
            return Collections.emptyMap();
        }
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context)
    {
        // RFC2616 says related to If-Modified-Since header the following:
        //
        // "... The If-Modified-Since request-header field is used with a method to 
        // make it conditional: if the requested variant has not been modified since 
        // the time specified in this field, an entity will not be returned from 
        // the server; instead, a 304 (not modified) response will be returned 
        // without any message-body..."
        // 
        // This method is called from ResourceHandlerImpl.handleResourceRequest and if
        // returns false send a 304 Not Modified response.
        
        String ifModifiedSinceString = context.getExternalContext().getRequestHeaderMap().get("If-Modified-Since");
        
        if (ifModifiedSinceString == null)
        {
            return true;
        }
        
        Long ifModifiedSince = ResourceLoaderUtils.parseDateHeader(ifModifiedSinceString);
        
        if (ifModifiedSince == null)
        {
            return true;
        }
        
        Long lastModified;
        try
        {
            URL url = this.getURL();
            if (url != null)
            {
                lastModified = ResourceLoaderUtils.getResourceLastModified(url);
            }
            else
            {
                //If url is null, set the lastModified time to the startup time.
                lastModified = _resourceHandlerSupport.getStartupTime();
            }
        }
        catch (IOException exception)
        {
            lastModified = -1L;
        }
        
        if (lastModified >= 0L)
        {
            // this.couldResourceContainValueExpressions() &&
            // Note this resource should be recreated each time the application is started
            if (lastModified < _resourceHandlerSupport.getStartupTime())
            {
                lastModified = _resourceHandlerSupport.getStartupTime();
            }
            
            // If the lastModified date is lower or equal than ifModifiedSince,
            // the agent does not need to update.
            // Note the lastModified time is set at milisecond precision, but when 
            // the date is parsed and sent on ifModifiedSince, the exceding miliseconds
            // are trimmed. So, we have to compare trimming this from the calculated
            // lastModified time.
            if ( (lastModified-(lastModified % 1000)) <= ifModifiedSince)
            {
                return false;
            }
        }
        
        return true;
    }

}
