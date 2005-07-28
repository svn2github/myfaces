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
package org.apache.myfaces.component.html.ext;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlForm extends javax.faces.component.html.HtmlForm
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlForm";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Form";

    private String _scheme;
    private String _serverName;
    private Integer _port;

    public HtmlForm()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * @see javax.faces.component.html.HtmlForm#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _scheme;
        values[2] = _serverName;
        values[3] = _port;
        return values;
    }

    /**
     * @see javax.faces.component.html.HtmlForm#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        _scheme = (String) values[1];
        _serverName = (String) values[2];
        _port = (Integer) values[3];
    }

    /**
     * @param localValue
     * @param valueBindingName
     * @return
     */
    private Object getLocalOrValueBindingValue(Object localValue,
                    String valueBindingName)
    {
        if (localValue != null)
            return localValue;
        ValueBinding vb = getValueBinding(valueBindingName);
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    public Integer getPort()
    {
        return (Integer) getLocalOrValueBindingValue(_port, "port");
    }

    public void setPort(Integer port)
    {
        _port = port;
    }

    public String getScheme()
    {
        return (String) getLocalOrValueBindingValue(_scheme, "scheme");
    }

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    public String getServerName()
    {
        return (String) getLocalOrValueBindingValue(_serverName, "serverName");
    }

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

}
