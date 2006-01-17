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
package org.apache.myfaces.custom.form;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.html.HtmlFormTagBase;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 */
public class HtmlFormTag extends HtmlFormTagBase
{
    private String _scheme;
    private String _serverName;
    private String _port;

    public String getComponentType()
    {
        return HtmlForm.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.Form";
    }

    /**
     * @see org.apache.myfaces.taglib.html.HtmlFormTagBase#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "scheme", _scheme);
        setStringProperty(component, "serverName", _serverName);
        setIntegerProperty(component, "port", _port);
    }

    /**
     * @see org.apache.myfaces.taglib.html.HtmlFormTagBase#release()
     */
    public void release()
    {
        super.release();
        _scheme = null;
        _serverName = null;
        _port = null;
    }

    public void setPort(String port)
    {
        _port = port;
    }

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

}
