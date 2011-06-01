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
package org.apache.myfaces.custom.validatebeanbehavior;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ValueReference;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessages;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.servlet.ServletContext;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFClientBehavior;

/**
 * Behavior for Bean Validation validations in JavaScript.
 * <p/>
 * This class can be attached to UIComponent instances and validate the entire form.
 * Any UIMessages instances in the form are looked up and used for positioning error messages.
 *
 * @author Jan-Kees van Andel
 */
@JSFClientBehavior(
        name="s:validateBean",
        id="org.apache.myfaces.custom.ValidateBeanBehavior")
@ResourceDependency(library="oam.custom.validateBean", name = "validateBeanBehavior.js")
public class ValidateBeanBehavior extends ClientBehaviorBase {

    /** {@inheritDoc} */
    @Override
    public String getScript(final ClientBehaviorContext ctx) {
        final UIComponent component = ctx.getComponent();
        if (!(component instanceof UICommand)) {
            throw new FacesException("Unsupported component: " + component + " Only UICommand components are supported");
        }
        final UIViewRoot viewRoot = ctx.getFacesContext().getViewRoot();

        UIForm form = ComponentUtils.findParentForm(component);
        List<UIInput> inputsInForm = ComponentUtils.findInputsInForm(form);
        UIMessages messages = ComponentUtils.findMessagesInTree(viewRoot);

        return getSubmitHandler(form, inputsInForm, messages);
    }

    /**
     * Fetch all necessary metadata and write a JavaScript submit handler to the client.
     *
     * @param form The form which must be validated.
     * @param inputsInForm The input fields in the form.
     * @param messages The messages to use for displaying error messages.
     * @return The JavaScript submit handler call.
     */
    private String getSubmitHandler(final UIForm form, final List<UIInput> inputsInForm, final UIMessages messages) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        final String clientId = form.getClientId(fc);
        final String messagesId = (messages != null) ? "'" + messages.getClientId(fc) + "'": "null";
        final List<ValidationPropertyModel> validations = new ArrayList<ValidationPropertyModel>();

        for (final UIInput input : inputsInForm) {
            final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(fc, input);
            final Set<ConstraintDescriptor<?>> constraints = propertyDescriptor.getConstraintDescriptors();
            final ValidationPropertyModel model = createValidationModel(input, propertyDescriptor, constraints);
            validations.add(model);
        }

        return writeJavaScript(fc, clientId, messagesId, validations);
    }

    /**
     * Write the JavaScript submit handler to the client.
     *
     * @param fc The FacesContext.
     * @param clientId The client ID of the form.
     * @param messagesId The client ID of the messages component.
     * @param validations The list with field validations.
     * @return The JavaScript event handler string.
     */
    private String writeJavaScript(final FacesContext fc, final String clientId,
                                   final String messagesId, final List<ValidationPropertyModel> validations) {
        final StringBuilder sb = new StringBuilder();
        sb.append("return org.jkva.validateBean.validateForm(");
        sb.append("'").append(clientId).append("', ");
        sb.append("'").append(messagesId).append("', ");
        sb.append("[");
        String sep = "";

        for (final ValidationPropertyModel validationModel : validations) {
            sb.append(sep);
            final String validation = writeJavaScriptForField(validationModel, fc);
            sb.append(validation);
            sep = ",";
        }

        sb.append("])");
        return sb.toString();
    }

    /**
     * Write a JSON object with all validation metadata for the given field.
     *
     * @param model The validation metadata for a field.
     * @param fc The FacesContext.
     * @return The JavaScript event handler string, as a JSON object.
     */
    private String writeJavaScriptForField(final ValidationPropertyModel model, final FacesContext fc) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("fieldId: '").append(model.getComponent().getClientId(fc)).append("'");
        if (model.isRequired()) {
            sb.append(",required: true");
        }
        sb.append(",type: '").append(model.getType()).append("'");
        if (model.getMin() != null) {
            sb.append(",min: true");
            sb.append(",minValue: ").append(model.getMin()).append("");
        }
        if (model.getMax() != null) {
            sb.append(",max: true");
            sb.append(",maxValue: ").append(model.getMax()).append("");
        }
        if (model.isFutureDate()) {
            String nowStr = new SimpleDateFormat(model.getDateFormat()).format(new Date());
            sb.append(",future: true");
            sb.append(",nowStr: '").append(nowStr).append("'");
            sb.append(",dateFormat: '").append(model.getDateFormat()).append("'");
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Create the Validation metadata, by inspecting the component, managed bean and {ConstraintDescriptor}s.
     *
     * @param component The input component.
     * @param propertyDescriptor The property descriptor
     * @param constraints The constraint descriptors.
     * @return The validation metadata.
     */
    private ValidationPropertyModel createValidationModel(final UIComponent component,
                                                          final PropertyDescriptor propertyDescriptor,
                                                          final Set<ConstraintDescriptor<?>> constraints) {
        final ValidationPropertyModel model = new ValidationPropertyModel();
        model.setComponent(component);

        if (component instanceof UIInput) {
            model.setRequired(((UIInput) component).isRequired());
        }

        final Class<?> type = propertyDescriptor.getElementClass();
        if (type.equals(String.class)) {
            model.setType("text");
        } else if (Number.class.isAssignableFrom(type)) {
            model.setType("numeric");
        } else if (Date.class.isAssignableFrom(type)
                || Calendar.class.isAssignableFrom(type)) {
            model.setType("date");
        }

        for (final ConstraintDescriptor<?> constraint : constraints) {
            final Annotation annotation = constraint.getAnnotation();
            if (annotation instanceof NotNull) {
                model.setRequired(true);
            } else if (annotation instanceof Min) {
                model.setMin(((Min) annotation).value());
            } else if (annotation instanceof Max) {
                model.setMax(((Max) annotation).value());
            } else if (annotation instanceof Future) {
                model.setFutureDate(true);
            }
        }

        final Converter converter = ((UIInput) component).getConverter();
        if (converter instanceof DateTimeConverter) {
            model.setDateFormat(((DateTimeConverter) converter).getPattern());
        }

        return model;
    }

    // Copied from MyFaces Core 2.0.
    private PropertyDescriptor getPropertyDescriptor(final FacesContext fc, final UIComponent component) {
        final ValueReferenceWrapper reference = getValueReference(component, fc);

        if (reference != null) {
            final Object base = reference.getBase();
            if (base != null) {
                final Class<?> valueBaseClass = base.getClass();
                final String valueProperty = (String) reference.getProperty();
                if (valueBaseClass != null && valueProperty != null) {
                    // Initialize Bean Validation.
                    final ValidatorFactory validatorFactory = createValidatorFactory(fc);
                    final javax.validation.Validator validator = createValidator(validatorFactory);
                    final BeanDescriptor beanDescriptor = validator.getConstraintsForClass(valueBaseClass);
                    if (beanDescriptor.isBeanConstrained()) {
                        return beanDescriptor.getConstraintsForProperty(valueProperty);
                    }
                }
            }
        }

        return null;
    }

    // Copied from MyFaces Core 2.0.
    private javax.validation.Validator createValidator(final ValidatorFactory validatorFactory) {
        return validatorFactory //
                .usingContext() //
                .messageInterpolator(FacesMessageInterpolatorHolder.get(validatorFactory)) //
                .getValidator();

    }

    // Copied from MyFaces Core 2.0.
    private ValueReferenceWrapper getValueReference(final UIComponent component, final FacesContext context) {
        final ValueExpression valueExpression = component.getValueExpression("value");
        final ELContext elCtx = context.getELContext();
        if (ExternalSpecifications.isUnifiedELAvailable()) {
            final ValueReference valueReference = getUELValueReference(valueExpression, elCtx);
            if (valueReference == null) {
                return null;
            }
            return new ValueReferenceWrapper(valueReference.getBase(), valueReference.getProperty());
        } else {
            return ValueReferenceResolver.resolve(valueExpression, elCtx);
        }
    }

    // Copied from MyFaces Core 2.0.
    private ValueReference getUELValueReference(final ValueExpression valueExpression, final ELContext elCtx) {
        final String methodName = "getValueReference";
        final String methodSignature = valueExpression.getClass().getName() +
                "." + methodName +
                "(" + ELContext.class + ")";
        try {
            final Method method = valueExpression.getClass().getMethod(methodName, ELContext.class);
            if (!ValueReference.class.equals(method.getReturnType())
                    && !ValueReference.class.isAssignableFrom(method.getReturnType())) {
                throw new NoSuchMethodException(
                        methodSignature +
                                "doesn't return " + ValueReference.class +
                                ", but " + method.getReturnType());
            }
            return (ValueReference) method.invoke(valueExpression, elCtx);
        } catch (NoSuchMethodException e) {
            throw new FacesException(
                    "MyFaces indicates Unified EL is available, but method: " +
                            methodSignature +
                            " is not available", e);
        } catch (InvocationTargetException e) {
            throw new FacesException("Exception invoking " + methodSignature, e);
        } catch (IllegalAccessException e) {
            throw new FacesException("Exception invoking " + methodSignature, e);
        }
    }

    // Copied from MyFaces Core 2.0.
    private synchronized ValidatorFactory createValidatorFactory(final FacesContext context) {
        final Object ctx = context.getExternalContext().getContext();
        if (ctx instanceof ServletContext) {
            final ServletContext servletCtx = (ServletContext) ctx;
            final Object attr = servletCtx.getAttribute(VALIDATOR_FACTORY_KEY);
            if (attr != null) {
                return (ValidatorFactory) attr;
            } else {
                if (ExternalSpecifications.isBeanValidationAvailable()) {
                    final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    servletCtx.setAttribute(VALIDATOR_FACTORY_KEY, attr);
                    return factory;
                } else {
                    throw new FacesException(
                            "Bean Validation (API or implementation) is not present, but required for " +
                                    this.getClass().getSimpleName());
                }
            }
        } else {
            throw new FacesException("Only Servlet environments are supported for " +
                    this.getClass().getSimpleName());
        }
    }

    // Copied from MyFaces Core 2.0.
    public static final String VALIDATOR_FACTORY_KEY = "javax.faces.validator.beanValidator.ValidatorFactory";
}

