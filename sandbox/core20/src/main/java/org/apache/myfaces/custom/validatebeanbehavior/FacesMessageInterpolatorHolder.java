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

import javax.faces.context.FacesContext;
import javax.validation.MessageInterpolator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

/**
 * Holder class to prevent NoClassDefFoundError in environments without Bean Validation.
 * <p/>
 * This is needed, because holder classes are loaded lazily. This means that when it's not
 * used, it will not be loaded, parsed and initialized. The BeanValidator class is used always,
 * so the MessageInterpolator references need to be in this separate class.
 */
final class FacesMessageInterpolatorHolder {

    // Needs to be volatile.
    private static volatile FacesMessageInterpolator instance;

    /**
     * Helper method for initializing the FacesMessageInterpolator.
     * <p/>
     * It uses the "Single Check Idiom" as described in Joshua Bloch's Effective Java 2nd Edition.
     *
     * @param validatorFactory Used to obtain the MessageInterpolator.
     * @return The instantiated MessageInterpolator for BeanValidator.
     */
    static MessageInterpolator get(final ValidatorFactory validatorFactory) {
        FacesMessageInterpolatorHolder.FacesMessageInterpolator ret = instance;
        if (ret == null) {
            final MessageInterpolator interpolator = validatorFactory.getMessageInterpolator();
            instance = ret = new FacesMessageInterpolator(interpolator);
        }
        return ret;
    }

    /**
     * Standard MessageInterpolator, as described in the JSR-314 spec.
     */
    private static class FacesMessageInterpolator implements MessageInterpolator {
        private final MessageInterpolator interpolator;

        FacesMessageInterpolator(final MessageInterpolator interpolator) {
            this.interpolator = interpolator;
        }

        public String interpolate(final String s, final Context context) {
            final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            return interpolator.interpolate(s, context, locale);
        }

        public String interpolate(final String s, final Context context, final Locale locale) {
            return interpolator.interpolate(s, context, locale);
        }
    }
}
