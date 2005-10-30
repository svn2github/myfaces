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
package org.apache.myfaces.custom.collapsiblepanel;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.validator.Validator;
import javax.faces.event.ValueChangeListener;
import javax.faces.convert.Converter;
import java.util.Iterator;
import java.io.IOException;

/**
 * @author Kalle Korhonen (latest modification by $Author$)
 * 
 * @version $Revision$ $Date$
 *
 */
public class HtmlCollapsiblePanel extends HtmlPanelGroup implements EditableValueHolder
{
    private UIInput _inputDelegate = new UIInput();

    //private static final Log log = LogFactory.getLog(HtmlCollapsiblePanel.class);

    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");

        if (!isRendered()) return;

        if(isCollapsed())
        {
            UIComponent component = getFacet("closedContent");

            if(component != null)
            {
                component.processDecodes(context);
            }
        }
        else
        {
            for (Iterator it = getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                child.processDecodes(context);
            }
        }

        try
        {
            decode(context);
        }
        catch (RuntimeException e)
        {
            context.renderResponse();
            throw e;
        }
    }

    public String getClientId(FacesContext context)
    {
        return super.getClientId(context);
    }

    public void processUpdates(FacesContext context)
    {
        initialiseVars(context);

        super.processUpdates(context);

        removeVars(context);
    }

    private void initialiseVars(FacesContext context)
    {
        if(getVar()!=null)
        {
            context.getExternalContext().getRequestMap().put(getVar(),
                            Boolean.valueOf(isCollapsed()));
        }

        if(getTitleVar()!=null)
        {
            context.getExternalContext().getRequestMap().put(getTitleVar(),
                            getTitle());
        }
    }

    private void removeVars(FacesContext context)
    {
        if(getVar()!=null)
        {
            context.getExternalContext().getRequestMap().remove(getVar());
        }

        if(getTitleVar()!=null)
        {
            context.getExternalContext().getRequestMap().remove(getTitleVar());
        }
    }

    public void processValidators(FacesContext context)
    {
        initialiseVars(context);

        super.processValidators(context);

        removeVars(context);
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        initialiseVars(context);

        super.encodeChildren(context);

        removeVars(context);
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------
    private static final boolean DEFAULT_COLLAPSED = true;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlCollapsiblePanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.CollapsiblePanel";

    private String _var = null;
    private String _title = null;
    private String _titleVar = null;

    public HtmlCollapsiblePanel()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setCollapsed(boolean collapsed)
    {
        setValue(Boolean.valueOf(collapsed));
    }

    public boolean isCollapsed()
    {
        Object value = getValue();

        if(value instanceof Boolean)
        {
            return ((Boolean) value).booleanValue();
        }

        return true;
    }

    public void setValue(Object value)
    {
        _inputDelegate.setValue(value);
    }

    public Object getLocalValue()
    {
        return _inputDelegate.getLocalValue();
    }

    public Object getValue()
    {
        return _inputDelegate.getValue();
    }

    public Converter getConverter()
    {
        return _inputDelegate.getConverter();
    }

    public void setConverter(Converter converter)
    {
        _inputDelegate.setConverter(converter);
    }

    public String getTitle()
    {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public String getVar()
    {
        if (_var != null) return _var;
        ValueBinding vb = getValueBinding("var");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTitleVar(String titleVar)
    {
        _titleVar = titleVar;
    }

    public String getTitleVar()
    {
        if (_titleVar != null) return _titleVar;
        ValueBinding vb = getValueBinding("titleVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    //------------------ GENERATED CODE END ---------------------------------------

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _inputDelegate.saveState(context);
        values[2] = _title;
        values[3] = _var;
        values[4] = _titleVar;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
          Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _inputDelegate.restoreState(context, values[1]);
        _title = (String)values[2];
        _var = (String)values[3];
        _titleVar = (String) values[4];
    }

    public Object getSubmittedValue()
    {
        return _inputDelegate.getSubmittedValue();
    }

    public void setSubmittedValue(Object submittedValue)
    {
        _inputDelegate.setSubmittedValue(submittedValue);
    }

    public boolean isLocalValueSet()
    {
        return _inputDelegate.isLocalValueSet();
    }

    public void setLocalValueSet(boolean localValueSet)
    {
        _inputDelegate.setLocalValueSet(localValueSet);
    }

    public boolean isValid()
    {
        return _inputDelegate.isValid();
    }

    public void setValid(boolean valid)
    {
        _inputDelegate.setValid(valid);
    }

    public boolean isRequired()
    {
        return _inputDelegate.isRequired();
    }

    public void setRequired(boolean required)
    {
        _inputDelegate.setRequired(required);
    }

    public boolean isImmediate()
    {
        return _inputDelegate.isImmediate();
    }

    public void setImmediate(boolean immediate)
    {
        _inputDelegate.setImmediate(immediate);
    }

    public MethodBinding getValidator()
    {
        return _inputDelegate.getValidator();
    }

    public void setValidator(MethodBinding validatorBinding)
    {
        _inputDelegate.setValidator(validatorBinding);
    }

    public MethodBinding getValueChangeListener()
    {
        return _inputDelegate.getValueChangeListener();
    }

    public void setValueChangeListener(MethodBinding valueChangeMethod)
    {
        _inputDelegate.setValueChangeListener(valueChangeMethod);
    }

    public void addValidator(Validator validator)
    {
        _inputDelegate.addValidator(validator);
    }

    public Validator[] getValidators()
    {
        return _inputDelegate.getValidators();
    }

    public void removeValidator(Validator validator)
    {
        _inputDelegate.removeValidator(validator);
    }

    public void addValueChangeListener(ValueChangeListener listener)
    {
        _inputDelegate.addValueChangeListener(listener);
    }

    public ValueChangeListener[] getValueChangeListeners()
    {
        return _inputDelegate.getValueChangeListeners();
    }

    public void removeValueChangeListener(ValueChangeListener listener)
    {
        _inputDelegate.removeValueChangeListener(listener);
    }
}
