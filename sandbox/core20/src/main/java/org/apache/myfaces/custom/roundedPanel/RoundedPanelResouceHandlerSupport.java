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

import javax.faces.context.FacesContext;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFWebConfigParam;
import org.apache.myfaces.shared_tomahawk.util.WebConfigParamUtils;
import org.apache.myfaces.shared_tomahawk.resource.BaseResourceHandlerSupport;
import org.apache.myfaces.shared_tomahawk.resource.ResourceLoader;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelResouceHandlerSupport extends BaseResourceHandlerSupport
{
    /**
     * 
     */
    @JSFWebConfigParam
    public final static String INIT_PARAM_CACHE_DISK_ROUNDED_IMAGES = "org.apache.myfaces.tomahawk.INIT_PARAM_CACHE_DISK_ROUNDED_IMAGES";
    
    private ResourceLoader[] _resourceLoaders;
    
    private RoundedPanelPropertiesBuilder _roundedPanelPropertiesBuilder;
    
    private boolean _cacheDiskRoundedImages;
    
    public RoundedPanelResouceHandlerSupport()
    {
        this._roundedPanelPropertiesBuilder = new RoundedPanelPropertiesBuilder();
        _cacheDiskRoundedImages = WebConfigParamUtils.getBooleanInitParameter(
                FacesContext.getCurrentInstance().getExternalContext(), 
                INIT_PARAM_CACHE_DISK_ROUNDED_IMAGES);
        this._resourceLoaders = new ResourceLoader[] {new RoundedPanelResourceLoader("oamRoundedPanel", this._roundedPanelPropertiesBuilder, this._cacheDiskRoundedImages)};
    }

    @Override
    public ResourceLoader[] getResourceLoaders()
    {
        return _resourceLoaders;
    }
    
    public RoundedPanelPropertiesBuilder getRoundedPanelPropertiesBuilder()
    {
        return _roundedPanelPropertiesBuilder;
    }

    public boolean isCacheDiskRoundedImages()
    {
        return _cacheDiskRoundedImages;
    }
}
