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

/**
 * @author Mathias Broekelmann
 *
 */
public class MyFacesResourceHandler implements ResourceHandler
{
    private final Class _myfacesCustomComponent;
    private final String _resource;

    /**
     * @param myfacesCustomComponent
     * @param resource
     */
    public MyFacesResourceHandler(Class myfacesCustomComponent, String resource)
    {
        validateCustomComponent(myfacesCustomComponent);
        _myfacesCustomComponent = myfacesCustomComponent;
        _resource = resource;
    }
    
    /**
     * @see org.apache.myfaces.component.html.util.ResourceHandler#getResourceLoaderClass()
     */
    public Class getResourceLoaderClass()
    {
        return MyFacesResourceLoader.class;
    }

    protected void validateCustomComponent(Class myfacesCustomComponent)
    {
        if (!myfacesCustomComponent.getName().startsWith(
                MyFacesResourceLoader.ORG_APACHE_MYFACES_CUSTOM + "."))
        {
            throw new IllegalArgumentException(
                    "expected a myfaces custom component class in package "
                            + MyFacesResourceLoader.ORG_APACHE_MYFACES_CUSTOM);
        }
    }

    /**
     * @see org.apache.myfaces.component.html.util.ResourceHandler#getResourceUri(javax.faces.context.FacesContext)
     */
    public String getResourceUri(FacesContext context)
    {
        String className = _myfacesCustomComponent.getName();
        StringBuffer sb = new StringBuffer();
        sb
                .append(className.substring(MyFacesResourceLoader.ORG_APACHE_MYFACES_CUSTOM
                        .length() + 1));
        sb.append("/");
        if (_resource != null)
        {
            sb.append(_resource);
        }
        return sb.toString();
    }
}
