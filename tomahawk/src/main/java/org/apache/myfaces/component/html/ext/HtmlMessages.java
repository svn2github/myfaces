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
package org.apache.myfaces.component.html.ext;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.util.HtmlComponentUtils;
import org.apache.myfaces.util._ComponentUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class HtmlMessages
        extends javax.faces.component.html.HtmlMessages
        implements UserRoleAware
{
    public String getClientId(FacesContext context)
    {
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
        }

        return clientId;
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlMessages";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Messages";

    private String _summaryFormat = null;
    private String _globalSummaryFormat = null;
    private String _detailFormat = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private Boolean _replaceIdWithLabel = null;

    public HtmlMessages()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public String getSummaryFormat()
    {
        if (_summaryFormat != null) return _summaryFormat;
        ValueBinding vb = getValueBinding("summaryFormat");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setGlobalSummaryFormat(String globalSummaryFormat)
    {
        _globalSummaryFormat = globalSummaryFormat;
    }

    public String getGlobalSummaryFormat()
    {
        if (_globalSummaryFormat != null) return _globalSummaryFormat;
        ValueBinding vb = getValueBinding("globalSummaryFormat");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
    }

    public String getDetailFormat()
    {
        if (_detailFormat != null) return _detailFormat;
        ValueBinding vb = getValueBinding("detailFormat");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setReplaceIdWithLabel(boolean replaceIdWithLabel)
    {
        _replaceIdWithLabel = Boolean.valueOf(replaceIdWithLabel);
    }

    public boolean isReplaceIdWithLabel()
    {
        if (_replaceIdWithLabel != null) return _replaceIdWithLabel.booleanValue();
        ValueBinding vb = getValueBinding("replaceIdWithLabel");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = _summaryFormat;
        values[2] = _globalSummaryFormat;
        values[3] = _detailFormat;
        values[4] = _enabledOnUserRole;
        values[5] = _visibleOnUserRole;
        values[6] = _replaceIdWithLabel;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _summaryFormat = (String)values[1];
        _globalSummaryFormat = (String)values[2];
        _detailFormat = (String)values[3];
        _enabledOnUserRole = (String)values[4];
        _visibleOnUserRole = (String)values[5];
        _replaceIdWithLabel = (Boolean)values[6];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
