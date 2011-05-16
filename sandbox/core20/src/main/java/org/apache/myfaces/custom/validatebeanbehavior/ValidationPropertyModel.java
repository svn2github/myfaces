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

import javax.faces.component.UIComponent;

/**
 * This class contains all validation related properties for a single UIInput component.
 *
 * @author Jan-Kees van Andel
 */
final class ValidationPropertyModel {

    /**
     * Is this field required?
     */
    private boolean required;

    /**
     * The type of the field, for syntactic validation.
     */
    private String type;

    /**
     * The minimum and maximum value of a number.
     */
    private Long min, max;

    /**
     * A validator that checks if a date is in the future. Today is not considered a future date.
     */
    private boolean futureDate;

    /**
     * The format (SimpleDateFormat pattern) of a date field.
     */
    private String dateFormat;

    /**
     * The corresponding component.
     */
    private UIComponent component;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public boolean isFutureDate() {
        return futureDate;
    }

    public void setFutureDate(boolean futureDate) {
        this.futureDate = futureDate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }
}
