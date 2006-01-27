/*
 * Copyright 2006 The Apache Software Foundation.
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

package org.apache.myfaces.mock.tomahawk;

import org.apache.myfaces.el.ValueBindingImpl;
import org.apache.myfaces.el.PropertyResolverImpl;
import org.apache.myfaces.el.VariableResolverImpl;

import javax.faces.application.Application;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

public class MockApplication extends Application
{
    private PropertyResolver _propertyResolver;
    private VariableResolver _variableResolver;

    public javax.faces.event.ActionListener getActionListener()
    {
        return null;
    }

    public void setActionListener(javax.faces.event.ActionListener p1)
    {
    }

    public java.util.Locale getDefaultLocale()
    {
        return null;
    }

    public void setDefaultLocale(java.util.Locale p1)
    {
    }

    public java.lang.String getDefaultRenderKitId()
    {
        return null;
    }

    public void setDefaultRenderKitId(java.lang.String p1)
    {
    }

    public java.lang.String getMessageBundle()
    {
        return null;
    }

    public void setMessageBundle(java.lang.String p1)
    {
    }

    public javax.faces.application.NavigationHandler getNavigationHandler()
    {
        return null;
    }

    public void setNavigationHandler(javax.faces.application.NavigationHandler p1)
    {
    }

    public javax.faces.el.PropertyResolver getPropertyResolver()
    {

        if (_propertyResolver == null)
        {
            _propertyResolver = new PropertyResolverImpl();
        }
        return _propertyResolver;
    }

    public void setPropertyResolver(javax.faces.el.PropertyResolver propertyResolver)
    {
        _propertyResolver = propertyResolver;
    }

    public javax.faces.el.VariableResolver getVariableResolver()
    {
        if(_variableResolver == null)
        {
            _variableResolver = new VariableResolverImpl();
        }

        return _variableResolver;
    }

    public void setVariableResolver(javax.faces.el.VariableResolver variableResolver)
    {
        _variableResolver = variableResolver;
    }

    public javax.faces.application.ViewHandler getViewHandler()
    {
        return null;
    }

    public void setViewHandler(javax.faces.application.ViewHandler p1)
    {
    }

    public javax.faces.application.StateManager getStateManager()
    {
        return null;
    }

    public void setStateManager(javax.faces.application.StateManager p1)
    {
    }

    public void addComponent(java.lang.String p1, java.lang.String p2)
    {
    }

    public javax.faces.component.UIComponent createComponent(java.lang.String p1)
    {
        return null;
    }

    public javax.faces.component.UIComponent createComponent(javax.faces.el.ValueBinding p1, javax.faces.context.FacesContext p2, java.lang.String p3)
    {
        return null;
    }

    public java.util.Iterator getComponentTypes()
    {
        return null;
    }

    public void addConverter(java.lang.String p1, java.lang.String p2)
    {
    }

    public void addConverter(java.lang.Class p1, java.lang.String p2)
    {
    }

    public javax.faces.convert.Converter createConverter(java.lang.String p1)
    {
        return null;
    }

    public javax.faces.convert.Converter createConverter(java.lang.Class p1)
    {
        return null;
    }

    public java.util.Iterator getConverterIds()
    {
        return null;
    }

    public java.util.Iterator getConverterTypes()
    {
        return null;
    }

    public javax.faces.el.MethodBinding createMethodBinding(java.lang.String p1, java.lang.Class[] p2)
    {
        return null;
    }

    public java.util.Iterator getSupportedLocales()
    {
        return null;
    }

    public void setSupportedLocales(java.util.Collection p1)
    {
    }

    public void addValidator(java.lang.String p1, java.lang.String p2)
    {
    }

    public javax.faces.validator.Validator createValidator(java.lang.String p1)
    {
        return null;
    }

    public java.util.Iterator getValidatorIds()
    {
        return null;
    }

    public javax.faces.el.ValueBinding createValueBinding(java.lang.String expr)
    {
        return new ValueBindingImpl(this, (String) expr);
    }
}