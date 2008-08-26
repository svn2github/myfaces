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
package org.apache.myfaces.renderkit.template;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Martin Marinschek
 */
public class DefaultTemplateEncoder implements TemplateEncoder
{
    private static final Log log = LogFactory.getLog(DefaultTemplateEncoder.class);
    private static final String TEMPLATE_CACHE = "org.apache.myfaces.tomahawk.template.DefaultTemplateEncoder.CACHE";
    private static final String TEMPLATE_DIRECTORY = "template";

    public void encodeTemplate(FacesContext context, UIComponent component, Renderer renderer, String template, Object dataModel)
        throws IOException
    {
        if(log.isDebugEnabled())
        {
            log.debug("Encoding template : " + renderer.getClass().getResource(TEMPLATE_DIRECTORY+"/"+template));
        }
        Configuration cfg = getConfig(context, TEMPLATE_CACHE);
        //Get the template using absolute path
        Template temp = cfg.getTemplate('/'
                +renderer.getClass().getPackage().getName().replace('.','/')
                +'/'+TEMPLATE_DIRECTORY+'/'+template);
        try
        {
            temp.process(dataModel, context.getResponseWriter());
        }
        catch (TemplateException e)
        {
            throw new IOException(e.getMessage());
        }
    }
    
    /**
     * Retrieve the current configuration or if no instance exists create a new one. 
     * 
     * @param context The current FacesContext
     * @param cacheParamName the variable used to save and retrieve on application scope the current template configuration
     * @return
     */
    protected Configuration getConfig(FacesContext context, String cacheParamName)
    {
        Configuration config = 
            (Configuration) context.getExternalContext().getApplicationMap().get(cacheParamName);
        if(config == null)
        {
            config = createConfig(context);
            context.getExternalContext().getApplicationMap().put(cacheParamName, config);
        }
        return config;
    }
    
    protected Configuration createConfig(FacesContext context)
    {
        Configuration config = new Configuration();
        TemplateLoader templateLoader = new DefaultTemplateLoader();
        config.setObjectWrapper(new DefaultObjectWrapper());
        config.setTemplateLoader(templateLoader);
        return config;
    }
}
