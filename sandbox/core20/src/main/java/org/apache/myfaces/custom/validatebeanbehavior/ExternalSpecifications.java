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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * <p>
 * Utility class for determining which specifications are available
 * in the current process. See JIRA issue: http://issues.apache.org/jira/browse/MYFACES-2386
 * </p>
 *
 * Note: Copy from MyFaces Core 2.0.
 *
 * @author Jan-Kees van Andel
 * @author Jakob Korherr (latest modification by $Author$)
 * @version $Revision$ $Date$
 * @since 2.0
 */
final class ExternalSpecifications {

    //private static final Log log = LogFactory.getLog(BeanValidator.class);
    private static final Logger log = Logger.getLogger(ExternalSpecifications.class.getName());

    private static Boolean beanValidationAvailable;
    private static Boolean unifiedELAvailable;

    /**
     * Static inner class to prevent NoClassDefFoundErrors when Bean Validation is not available.
     */
    private static final class ValidationHolder {
        static boolean trialErrorValidationInitialization() {
            try {
                // Trial-error approach to check for Bean Validation impl existence.
                // If any Exception occurs here, we assume that Bean Validation is not available.
                // The cause may be anything, i.e. NoClassDef, config error...
                final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

                return validator != null;
            } catch (Throwable t) {
                log.log(Level.FINE, "Error initializing Bean Validation (could be normal)", t);
                return false;
            }

        }
    }

    /**
     * This method determines if Bean Validation is present.
     * <p/>
     * Eager initialization is used for performance. This means Bean Validation binaries
     * should not be added at runtime after this variable has been set.
     *
     * @return true if Bean Validation is available, false otherwise.
     */
    static synchronized boolean isBeanValidationAvailable() {
        if (beanValidationAvailable == null) {
            try {
                beanValidationAvailable = (Class.forName("javax.validation.Validation") != null);

                if (beanValidationAvailable) {
                    beanValidationAvailable = ValidationHolder.trialErrorValidationInitialization();
                }
            } catch (Throwable t) {
                log.log(Level.FINE, "Error loading class (could be normal)", t);
                beanValidationAvailable = false;
            }

            log.info("MyFaces Bean Validation support " + (beanValidationAvailable ? "enabled" : "disabled"));
        }
        return beanValidationAvailable;
    }

    /**
     * This method determines if Unified EL is present.
     * <p/>
     * Eager initialization is used for performance. This means Unified EL binaries
     * should not be added at runtime after this variable has been set.
     *
     * @return true if UEL is available, false otherwise.
     */
    static synchronized boolean isUnifiedELAvailable() {
        if (unifiedELAvailable == null) {
            try {
                // Check if the UEL classes are available.
                // If the JSP EL classes are loaded first, UEL will not work
                // properly, hence it will be disabled.
                unifiedELAvailable = (
                        Class.forName("javax.el.ValueReference") != null
                                && Class.forName("javax.el.ValueExpression")
                                .getMethod("getValueReference", ELContext.class) != null
                );
            } catch (Throwable t) {
                log.log(Level.FINE, "Error loading class (could be normal)", t);
                unifiedELAvailable = false;
            }

            log.info("MyFaces Unified EL support " + (unifiedELAvailable ? "enabled" : "disabled"));
        }
        return unifiedELAvailable;
    }

    /**
     * this class should not be instantiated.
     */
    private ExternalSpecifications() {
    }

}
