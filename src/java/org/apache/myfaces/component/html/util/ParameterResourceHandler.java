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

import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Mathias Broekelmann
 *
 */
public class ParameterResourceHandler implements ResourceHandler
{
    private final Map _parameters;
    private final Class _resourceLoaderClass;

    private Integer _hashCode;

    /**
     * @param resourceLoaderClass
     * @param parameters
     */
    public ParameterResourceHandler(Class resourceLoaderClass, Map parameters)
    {
        _resourceLoaderClass = resourceLoaderClass;
        _parameters = parameters;
    }

    /**
     * @see org.apache.myfaces.component.html.util.ResourceHandler#getResourceLoaderClass()
     */
    public Class getResourceLoaderClass()
    {
        return _resourceLoaderClass;
    }

    /**
     * @see org.apache.myfaces.component.html.util.ResourceHandler#getResourceUri(javax.faces.context.FacesContext)
     */
    public String getResourceUri(FacesContext context)
    {
        if (_parameters != null && !_parameters.isEmpty())
        {
            StringBuffer sb = new StringBuffer();
            sb.append("?");
            for (Iterator iter = _parameters.entrySet().iterator(); iter.hasNext();)
            {
                Map.Entry entry = (Map.Entry) iter.next();
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                if (iter.hasNext())
                {
                    sb.append("&");
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (obj instanceof ParameterResourceHandler)
        {
            ParameterResourceHandler other = (ParameterResourceHandler) obj;
            return new EqualsBuilder().append(_parameters, other._parameters).isEquals();
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        if (_hashCode == null)
        {
            _hashCode = new Integer(new HashCodeBuilder().append(_parameters).toHashCode());
        }
        return _hashCode.intValue();
    }
}
