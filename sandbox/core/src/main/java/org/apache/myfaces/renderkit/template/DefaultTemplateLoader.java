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

import java.net.URL;

import freemarker.cache.URLTemplateLoader;

public class DefaultTemplateLoader extends URLTemplateLoader
{

    protected URL getURL(String s)
    {
        return getCurrentLoader(this).getResource(s);
    }
    
    /**
     * Gets the ClassLoader associated with the current thread.  Returns the class loader associated with
     * the specified default object if no context loader is associated with the current thread.
     *
     * @param defaultObject The default object to use to determine the class loader (if none associated with current thread.)
     * @return ClassLoader
     */
    protected static ClassLoader getCurrentLoader(Object defaultObject)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(loader == null)
        {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }
}
