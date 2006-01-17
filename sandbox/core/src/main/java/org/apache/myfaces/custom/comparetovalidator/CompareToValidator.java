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
package org.apache.myfaces.custom.comparetovalidator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.util.MessageUtils;
import org.apache.myfaces.util._ComponentUtils;

/**
 * @author Mike Kienenberger (latest modification by $Author$)
 * @version $Revision$ $Date$
 * 
 * Validates this component against another component.
 * 
 * Specify the foreign component with the for={foreign-component-id} attribute.
 *
 * Valid operator attribute values:
 * 
 *   equals:                  eq, ==, =,
 * 	 not equals:              ne, !=,
 *   greater than:            gt, >,
 *   less than:               lt, <,
 *   greater than or equals:  ge, >=,
 *   less than or equals:     le, <=
 *
 * If the comparator attribute is specified, the component values are compared
 * using the specified java.util.Comparator object.
 * If no comparator is specified, the component values must implement Comparable
 * and are compared using compareTo().
 * If either value or foreign value does not implement Comparable and no Comparator
 * is specified, validation always succeeds.
 *
 * Put this validator on the bottom-most component to insure that
 * the foreign component's value has been converted and validated first.
 * 
 * However, this validator will attempt to convert and validate the foreign
 * component's value if this has not already occurred.  This process may not
 * be identical to the standard JSF conversion and validation process.
 * 
 * The validation error message key is currently hardcoded as
 * 
 *     "{0} value <{1}> must be {2} {3} value <{4}>"
 * 
 * where {0} is the parent component id,
 *       {1} is the parent component value,
 *       {2} is the operator name,
 *       {3} is the foreign component id, and
 *       {4} is the foreign component value.
 * 
 * The alternateOperatorName attribute can specify a custom operator name.
 * For example, use "after" instead of "greater than" when comparing dates.
 * 
 * The message attribute can specify an alternate validation error message key.
 * For example, use "{0} must be {2} {3}" to remove values from the message.
 *
 * 
 * faces-config.xml configuration:
 * 
 * 	<validator>
 * 		<description>CompareTo validator</description>
 * 		<validator-id>org.apache.myfaces.validator.CompareTo</validator-id>
 * 		<validator-class>org.apache.myfaces.custom.comparetovalidator.CompareToValidator</validator-class>
 * 	</validator>
 * 
 * 
 * Facelets configuration (inside a taglib.xml file):
 * 
 * <tag>
 *      <tag-name>compareToValidator</tag-name>
 *      <validator>
 *          <validator-id>org.apache.myfaces.validator.CompareTo</validator-id>
 *      </validator>
 *  </tag>
 * 
 * 
 * Example usage:
 * 
 *   <t:inputCalendar id="startDate"/>
 *   <t:inputCalendar id="endDate">
 *       <sandbox:compareToValidator operator="gt" for="startDate" />
 *       <sandbox:compareToValidator operator="gt" for="startDate" message="Start date must be before end date." />
 *       <sandbox:compareToValidator operator="gt" for="startDate" message="{0} must be {2} {3}" />
 *       <sandbox:compareToValidator operator="gt" for="startDate" alternateOperatorName="after" />
 *       <sandbox:compareToValidator operator="gt" for="startDate" message="{0} must be {2} {3}" alternateOperatorName="after" />
 *       <sandbox:compareToValidator operator="gt" for="startDate" comparator="#{dateComparator}" />
 *   <t:inputCalendar>
 * 
 * 
 * Known issues:
 *   - Operator names should be localized.
 *   - The default message key should be localized.
 *   - Perhaps an exception should be thrown if the two values are not Comparable and no Comparator is specified.
 * 
 */
public class CompareToValidator implements Validator, StateHolder {
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "org.apache.myfaces.validator.CompareTo";

	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the comparison check fails.</p>
	 */
	// public static final String COMPARE_TO_MESSAGE_ID = "org.apache.myfaces.CompareTo.INVALID";
	public static final String COMPARE_TO_MESSAGE_ID = "{0} value <{1}> must be {2} {3} value <{4}>";

	public CompareToValidator(){
	    super();
	}

	//the foreign component_id on which the validation is based.
	protected String _foreignComponentName = null;
	protected String _operator = null;
	protected Object _comparator = null;
	protected String _message = null;
	protected String _alternateOperatorName = null;

	//JSF-Field for StateHolder-IF
	protected boolean _transient = false;

    public static final String OPERATOR_EQUALS = "eq";
    public static final String OPERATOR_NOT_EQUALS = "ne";
    public static final String OPERATOR_GREATER_THAN = "gt";
    public static final String OPERATOR_LESS_THAN = "lt";
    public static final String OPERATOR_GREATER_THAN_OR_EQUALS = "ge";
    public static final String OPERATOR_LESS_THAN_OR_EQUALS = "le";

    public static final String OPERATOR_EQUALS_ALT = "==";
    public static final String OPERATOR_NOT_EQUALS_ALT = "!=";
    public static final String OPERATOR_GREATER_THAN_ALT = ">";
    public static final String OPERATOR_LESS_THAN_ALT = "<";
    public static final String OPERATOR_GREATER_THAN_OR_EQUALS_ALT = ">=";
    public static final String OPERATOR_LESS_THAN_OR_EQUALS_ALT = "<=";

    public static final String OPERATOR_EQUALS_ALT2 = "=";

    protected String getOperatorForString(String operatorSpecified)
    {
        String operatorFound = null;
        
        if (OPERATOR_EQUALS.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_EQUALS;
        else if (OPERATOR_NOT_EQUALS.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_NOT_EQUALS;
        else if (OPERATOR_GREATER_THAN.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_GREATER_THAN;
        else if (OPERATOR_LESS_THAN.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_LESS_THAN;
        else if (OPERATOR_GREATER_THAN_OR_EQUALS.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_GREATER_THAN_OR_EQUALS;
        else if (OPERATOR_LESS_THAN_OR_EQUALS.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_LESS_THAN_OR_EQUALS;
        
        else if (OPERATOR_EQUALS_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_EQUALS;
        else if (OPERATOR_NOT_EQUALS_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_NOT_EQUALS;
        else if (OPERATOR_GREATER_THAN_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_GREATER_THAN;
        else if (OPERATOR_LESS_THAN_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_LESS_THAN;
        else if (OPERATOR_GREATER_THAN_OR_EQUALS_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_GREATER_THAN_OR_EQUALS;
        else if (OPERATOR_LESS_THAN_OR_EQUALS_ALT.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_LESS_THAN_OR_EQUALS;
        
        else if (OPERATOR_EQUALS_ALT2.equalsIgnoreCase(operatorSpecified))
            return OPERATOR_EQUALS;
        
        throw new IllegalStateException("Operator has unknown value of '" + operatorSpecified + "'");
    }

    protected String nameForOperator(String operator)
    {
        if (OPERATOR_EQUALS == operator)
            return "equal";
        else if (OPERATOR_NOT_EQUALS == operator)
            return "not equal";
        else if (OPERATOR_GREATER_THAN == operator)
            return "greater than";
        else if (OPERATOR_LESS_THAN == operator)
            return "less than";
        else if (OPERATOR_GREATER_THAN_OR_EQUALS == operator)
            return "greater than or equal";
        else if (OPERATOR_LESS_THAN_OR_EQUALS == operator)
            return "less than or equal";
        
        throw new IllegalStateException("Operator has unknown value of '" + operator + "'");
    }
    
    protected boolean validateOperatorOnComparisonResult(String operator, int result)
    {
        if (OPERATOR_EQUALS == operator)
            return result == 0;
        else if (OPERATOR_NOT_EQUALS == operator)
            return result != 0;
        else if (OPERATOR_GREATER_THAN == operator)
            return result > 0;
        else if (OPERATOR_LESS_THAN == operator)
            return result < 0;
        else if (OPERATOR_GREATER_THAN_OR_EQUALS == operator)
            return result >= 0;
        else if (OPERATOR_LESS_THAN_OR_EQUALS == operator)
            return result <= 0;
        
        throw new IllegalStateException("Operator has unknown value of '" + operator + "'");
    }
    
	public void validate(
		FacesContext facesContext,
		UIComponent uiComponent,
		Object value)
		throws ValidatorException {

	    if (facesContext == null) throw new NullPointerException("facesContext");
        if (uiComponent == null) throw new NullPointerException("uiComponent");

        // Don't perform validation if the value is null
		if (value == null)
		{
		    return;
		}

		String foreignComponentName = getFor();
		
		UIInput foreignComponent = (UIInput) uiComponent.getParent().findComponent(foreignComponentName);
        if(foreignComponent == null)
            throw new FacesException("Unable to find component '" + foreignComponentName + "' (calling findComponent on component '" + uiComponent.getId() + "')");

        if (foreignComponent.isRequired() && foreignComponent.getValue()== null ) {
            return;
        }

        Object foreignValue;
        if (foreignComponent.isValid())
        {
            foreignValue = foreignComponent.getValue();
        }
        else
        {
            foreignValue = getConvertedValueNonValid(facesContext, foreignComponent);
        }

        // Don't perform validation if the foreign value is null
        if (null == foreignValue)
        {
        	return;
        }

        String operator = getOperatorForString(getOperator());
        
        String alternateOperatorName = getAlternateOperatorName();
        Object[] args = {
                uiComponent.getId(),
                value.toString(),
                (alternateOperatorName == null) ? nameForOperator(operator) : alternateOperatorName,
                foreignComponent.getId(),
                (foreignValue == null) ? foreignComponent.getId() : foreignValue.toString()
        };

        String message = getMessage();
        if (null == message)  message = COMPARE_TO_MESSAGE_ID;
        
        Comparator comparator = createComparator();

        if (null != comparator)
        {
            if (false == validateOperatorOnComparisonResult(operator, comparator.compare(value, foreignValue)))
            {
            	throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, message, args));
            }
        }
        else if ( (value instanceof Comparable) && (foreignValue instanceof Comparable) )
        {
            try
            {
                if (false == validateOperatorOnComparisonResult(operator, ((Comparable)value).compareTo(foreignValue)))
                {
                	throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, message, args));
                }
            }
            catch (RuntimeException exception)
            {
                if (exception instanceof ValidatorException)
                {
                    throw exception;
                }
                else
                {
                	throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, message + ": " + exception.getLocalizedMessage(), args));
                }
            }
        }
        else if (value instanceof Comparable)
        {
            throw new ClassCastException(getClassCastExceptionMessage("value", Comparable.class, value));
        }
        else if (foreignValue instanceof Comparable)
        {
            throw new ClassCastException(getClassCastExceptionMessage("foreignValue", Comparable.class, foreignValue));
        }
	}

	protected String getClassCastExceptionMessage(String name, Class clazz, Object object)
	{
	    if (null == object)
	        return name + " must be type " + clazz + " but is null" + object.getClass();
	    else return name + " must be type " + clazz + " but is type " + object.getClass();
	}
	
	protected Comparator createComparator()
    {
        Object comparator = getComparator();
        
        if (null == comparator)  return null;
        
        if (false == comparator instanceof Comparator)
        {
            throw new ClassCastException(getClassCastExceptionMessage("comparator", Comparator.class, comparator));
        }
        
        return (Comparator)comparator;
    }

	// -------------------------------------------------------- GETTER & SETTER

    /**
	 * @return the foreign component_id, on which a value should be validated
	 */
	public String getFor() {
        if (_foreignComponentName != null) return _foreignComponentName;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
	}

	/**
	 * @param string the foreign component_id, on which a value should be validated
	 */
	public void setFor(String string) {
		_foreignComponentName = string;
	}

    public String getOperator()
    {
        if (_operator != null) return _operator;
        ValueBinding vb = getValueBinding("operator");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }
    
    public void setOperator(String operator)
    {
        this._operator = operator;
    }

    public Object getComparator()
    {
        if (_comparator != null) return _comparator;
        ValueBinding vb = getValueBinding("comparator");
        return vb != null ? (Comparator)vb.getValue(getFacesContext()) : null;
    }
    
    public void setComparator(Object comparator)
    {
        this._comparator = comparator;
    }

    public String getMessage()
    {
        if (_message != null) return _message;
        ValueBinding vb = getValueBinding("message");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }
    
    public void setMessage(String message)
    {
        this._message = message;
    }

    public String getAlternateOperatorName()
    {
        if (_alternateOperatorName != null) return _alternateOperatorName;
        ValueBinding vb = getValueBinding("alternateOperatorName");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }
    
    public void setAlternateOperatorName(String alternateOperatorName)
    {
        this._alternateOperatorName = alternateOperatorName;
    }

	// -------------------------------------------------------- StateholderIF

	public Object saveState(FacesContext context) {
	    if (_transient)  return null;
	    
	    Object value[] = new Object[5];
		value[0] = _foreignComponentName;
		value[1] = _operator;
		value[2] = _comparator;
		value[3] = _message;
		value[4] = _alternateOperatorName;
		return value;
	}

	public void restoreState(FacesContext context, Object state) {
	    if (_transient)  return;
	    
	    Object value[] = (Object[])state;
		_foreignComponentName = (String) value[0];
		_operator = (String) value[1];
		_comparator = value[2];
		_message = (String) value[3];
		_alternateOperatorName = (String) value[4];
	}

	public boolean isTransient() {
		return _transient;
	}

	public void setTransient(boolean newTransientValue) {
		_transient = newTransientValue;
	}

	
	// ---------------- Borrowed to convert foreign submitted values
	
    protected Renderer getRenderer(FacesContext context, UIComponent foreignComponent)
    {
        if (context == null) throw new NullPointerException("context");
        String rendererType = foreignComponent.getRendererType();
        if (rendererType == null) return null;
        String renderKitId = context.getViewRoot().getRenderKitId();
        RenderKitFactory rkf = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkf.getRenderKit(context, renderKitId);
        Renderer renderer = renderKit.getRenderer(foreignComponent.getFamily(), rendererType);
        if (renderer == null)
        {
            getFacesContext().getExternalContext().log("No Renderer found for component " + foreignComponent + " (component-family=" + foreignComponent.getFamily() + ", renderer-type=" + rendererType + ")");
        }
        return renderer;
    }

    protected Converter findUIOutputConverter(FacesContext facesContext, UIOutput component)
    {
        Converter converter = component.getConverter();
        if (converter != null) return converter;

        //Try to find out by value binding
        ValueBinding vb = component.getValueBinding("value");
        if (vb == null) return null;

        Class valueType = vb.getType(facesContext);
        if (valueType == null) return null;

        if (String.class.equals(valueType)) return null;    //No converter needed for String type
        if (Object.class.equals(valueType)) return null;    //There is no converter for Object class

        try
        {
            return facesContext.getApplication().createConverter(valueType);
        }
        catch (FacesException e)
        {
            getFacesContext().getExternalContext().log("No Converter for type " + valueType.getName() + " found", e);
            return null;
        }
    }

    
	// --------------------- borrowed from UIComponentBase ------------
	
    private Map _valueBindingMap = null;

    public ValueBinding getValueBinding(String name)
    {
        if (name == null) throw new NullPointerException("name");
        if (_valueBindingMap == null)
        {
            return null;
        }
        else
        {
            return (ValueBinding)_valueBindingMap.get(name);
        }
    }

    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (name == null) throw new NullPointerException("name");
        if (_valueBindingMap == null)
        {
            _valueBindingMap = new HashMap();
        }
        _valueBindingMap.put(name, binding);
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

    
	// --------------------- borrowed and modified from UIInput ------------

	protected Object getConvertedValueNonValid(FacesContext facesContext, UIInput component)
    {
        Object componentValueObject;
        Object submittedValue = component.getSubmittedValue();
        if (submittedValue == null)
        {
            componentValueObject = null;
        }
        else
        {
            try
            {
                Renderer renderer = getRenderer(facesContext, component);
                if (renderer != null)
                {
                    componentValueObject = renderer.getConvertedValue(facesContext, component, submittedValue);
                }
                else if (submittedValue instanceof String)
                {
                    Converter converter = findUIOutputConverter(facesContext, component);
                    if (converter != null)
                    {
                        componentValueObject = converter.getAsObject(facesContext, component, (String)submittedValue);
                    }
                }
            }
            catch (ConverterException e)
            {
            }
            componentValueObject = submittedValue;
        }
        return componentValueObject;
    }
}