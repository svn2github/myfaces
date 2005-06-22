/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.custom.fileupload;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;

import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputFileUpload
        extends HtmlInputText
        implements UserRoleAware
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputFileUpload";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.FileUpload";

    private String _accept = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    private String _storage = null;

    public HtmlInputFileUpload()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public void setUploadedFile(UploadedFile upFile)
    {
        setValue(upFile);
    }

    public UploadedFile getUploadedFile()
    {
        return (UploadedFile)getValue();
    }
    
	public String getStorage() {
		if (_storage != null) return _storage;
		ValueBinding vb = getValueBinding("storage");
		return vb != null ? (String)vb.getValue(getFacesContext()) : null;

	}

	public void setStorage(String string) {
		_storage = string;
	}

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setAccept(String accept)
    {
        _accept = accept;
    }

    public String getAccept()
    {
        if (_accept != null) return _accept;
        ValueBinding vb = getValueBinding("accept");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _accept;
        values[2] = _enabledOnUserRole;
        values[3] = _visibleOnUserRole;
        values[4] = _storage;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _accept = (String)values[1];
        _enabledOnUserRole = (String)values[2];
        _visibleOnUserRole = (String)values[3];
        _storage = (String)values[4];
    }
}
