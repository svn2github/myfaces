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

/**
 * This class provides access to the object pointed to by the EL expression.
 * <p/>
 * It makes the BeanValidator work when Unified EL is not available.
 *
 * Note: Copy from MyFaces Core 2.0.
 */
final class ValueReferenceWrapper {
    private final Object base, property;

    /**
     * Full constructor.
     *
     * @param base     The object the reference points to.
     * @param property The property the reference points to.
     */
    public ValueReferenceWrapper(final Object base, final Object property) {
        this.base = base;
        this.property = property;
    }

    /**
     * The object the reference points to.
     *
     * @return base.
     */
    public final Object getBase() {
        return base;
    }

    /**
     * The property the reference points to.
     *
     * @return property.
     */
    public final Object getProperty() {
        return property;
    }
}
