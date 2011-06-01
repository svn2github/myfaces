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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared_tomahawk.resource.ResourceLoader;
import org.apache.myfaces.shared_tomahawk.resource.ResourceMeta;
import org.apache.myfaces.shared_tomahawk.resource.ResourceMetaImpl;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelResourceLoader extends ResourceLoader
{
    public final static String ROUNDED_PANEL_MAP = "oam.tomahawk.ROUNDED_PANEL_MAP";
    
    private volatile File _imagesDir;
    
    private RoundedPanelPropertiesBuilder _builder;
    
    private boolean _cacheDiskRoundedImages;
    
    public RoundedPanelResourceLoader(String prefix, RoundedPanelPropertiesBuilder builder, boolean cacheDiskRoundedImages)
    {
        super(prefix);

        // This is the best place for initialization. 
        // This class is created eagerly by RoundedPanelResouceHandlerSupport,
        // that is created too by RounderPanelResourceHandlerWrapper, so we
        // can be sure in this place we are on init time (no request has been sent).
        initialize(builder, cacheDiskRoundedImages);
    }
    
    protected void initialize(RoundedPanelPropertiesBuilder builder, boolean cacheDiskRoundedImages)
    {
        //Get startup FacesContext
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        _cacheDiskRoundedImages = cacheDiskRoundedImages;
        
        if (_cacheDiskRoundedImages)
        {
            //1. Create temporal directory for images
            Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
            File tempdir = (File) applicationMap.get("javax.servlet.context.tempdir");
            File imagesDir = new File(tempdir, getPrefix());
            if (!imagesDir.exists())
            {
                imagesDir.mkdirs();
            }
            _imagesDir = imagesDir;
        }
        
        _builder = builder;
        
        //2. Create map for register rendered rounded panel images
        Map<String, RoundedPanelProperties> roundedPanelMap = new ConcurrentHashMap<String, RoundedPanelProperties>();
        facesContext.getExternalContext().getApplicationMap().put(RoundedPanelResourceLoader.ROUNDED_PANEL_MAP, roundedPanelMap);
    }
    
    /*
    @SuppressWarnings("unchecked")
    public static final void registerImage(FacesContext facesContext, String name, RoundedPanelProperties properties)
    {
        Map<String, ImageCreator> map = (Map<String, ImageCreator>) 
            facesContext.getExternalContext().getApplicationMap().get(RoundedPanelResourceLoader.ROUNDED_PANEL_MAP);
        map.put(name, new ImageCreator(properties));
    }*/
    
    /*
    @SuppressWarnings("unchecked")
    public static final RoundedPanelProperties getPanelProperties(FacesContext facesContext, String imageName)
    {
        Map<String, ImageCreator> map = (Map<String, ImageCreator>) 
            facesContext.getExternalContext().getApplicationMap().get(RoundedPanelResourceLoader.ROUNDED_PANEL_MAP);
        
        ImageCreator creator = (ImageCreator) map.get(imageName);
        if (creator != null)
        {
            return (RoundedPanelProperties) creator.getProperties();
        }
        return null;
    }*/
    
    @Override
    public String getResourceVersion(String path)
    {
        return null;
    }

    @Override
    public String getLibraryVersion(String path)
    {
        return null;
    }
    
    private File getImagesDir(FacesContext facesContext)
    {
        return _imagesDir;
    }

    @Override
    public URL getResourceURL(ResourceMeta resourceMeta)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (_cacheDiskRoundedImages)
        {
            if (imageExists(facesContext, resourceMeta))
            {
                File file = createOrGetImage(facesContext, resourceMeta);
                
                try
                {
                    return file.toURL();
                }
                catch (MalformedURLException e)
                {
                    throw new FacesException(e);
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public InputStream getResourceInputStream(ResourceMeta resourceMeta)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (_cacheDiskRoundedImages)
        {
            if (imageExists(facesContext, resourceMeta))
            {
                File file = createOrGetImage(facesContext, resourceMeta);
                
                try
                {
                    return new BufferedInputStream(new FileInputStream(file));
                }
                catch (FileNotFoundException e)
                {
                    throw new FacesException(e);
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            if (imageExists(facesContext, resourceMeta))
            {
                //TODO: Implement me
                return null;
            }
            else
            {
                return null;
            }
        }
    }
    
    private File createOrGetImage(FacesContext facesContext, ResourceMeta resourceMeta)
    {
        File imagesDir = getImagesDir(facesContext);
        String name = resourceMeta.getResourceName();
        File file = new File(imagesDir, name);
        if (!file.exists())
        {
            Map<String, ImageFileProducer> map = (Map<String, ImageFileProducer>) 
                facesContext.getExternalContext().getApplicationMap().get(RoundedPanelResourceLoader.ROUNDED_PANEL_MAP);

            ImageFileProducer creator = map.get(name);
            
            if (creator == null)
            {
                synchronized(this)
                {
                    creator = map.get(name);
                    
                    if (creator == null)
                    {
                        creator = new ImageFileProducer();
                        map.put(name, creator);
                    }
                }
            }
            
            if (!creator.isCreated())
            {
                creator.createImage(facesContext, resourceMeta, file, this);
            }
        }
        return file;
    }
    
    protected void innerCreateImage(FacesContext facesContext, ResourceMeta resourceMeta, File file)
    {
        FileOutputStream fos = null;
        
        try
        {
            boolean success = file.createNewFile();
        }
        catch(IOException e)
        {
            throw new FacesException(e);
        }

        try
        {
            fos = new FileOutputStream(file);
            
            _builder.generateRoundedPanelImage(facesContext, fos, resourceMeta.getResourceName());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FacesException(ex);
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (Exception ex)
                {
                }
            }
        }        
    }
    
    private boolean imageExists(FacesContext facesContext, ResourceMeta resourceMeta)
    {
        return _builder.isValidResourceName(facesContext, resourceMeta.getResourceName());
    }
    
    @Override
    public ResourceMeta createResourceMeta(String prefix, String libraryName,
            String libraryVersion, String resourceName, String resourceVersion)
    {
        ResourceMeta resourceMeta =  new ResourceMetaImpl(prefix, libraryName, libraryVersion, resourceName, resourceVersion);
        
        if (imageExists(FacesContext.getCurrentInstance(), resourceMeta))
        {
            return resourceMeta;
        }
        return null;
    }

    @Override
    public boolean libraryExists(String libraryName)
    {
        return RoundedPanelResourceHandlerWrapper.ROUNDED_PANEL_LIBRARY.equals(libraryName);
    }
    
    public static class ImageFileProducer {
        
        public volatile boolean created = false;
        
        public ImageFileProducer()
        {
            super();
        }

        public boolean isCreated()
        {
            return created;
        }

        public synchronized void createImage(FacesContext facesContext, ResourceMeta resourceMeta, File file, RoundedPanelResourceLoader loader)
        {
            if (!created)
            {
                loader.innerCreateImage(facesContext, resourceMeta, file);
                created = true;
            }
        }
    }

}
