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

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

/**
 * This class inspects the EL expression and returns a ValueReferenceWrapper
 * when Unified EL is not available.
 *
 * Note: Copy from MyFaces Core 2.0.
 */
final class ValueReferenceResolver extends ELResolver {
    private final ELResolver resolver;

    /**
     * This is a simple solution to keep track of the resolved objects,
     * since ELResolver provides no way to know if the current ELResolver
     * is the last one in the chain. By assigning (and effectively overwriting)
     * this field, we know that the value after invoking the chain is always
     * the last one.
     * <p/>
     * This solution also deals with nested objects (like: #{myBean.prop.prop.prop}.
     */
    private ValueReferenceWrapper lastObject;

    /**
     * Constructor is only used internally.
     *
     * @param elResolver An ELResolver from the current ELContext.
     */
    ValueReferenceResolver(final ELResolver elResolver) {
        this.resolver = elResolver;
    }

    /**
     * This method can be used to extract the ValueReferenceWrapper from the given ValueExpression.
     *
     * @param valueExpression The ValueExpression to resolve.
     * @param elCtx           The ELContext, needed to parse and execute the expression.
     * @return The ValueReferenceWrapper.
     */
    public static ValueReferenceWrapper resolve(final ValueExpression valueExpression, final ELContext elCtx) {
        final ValueReferenceResolver resolver = new ValueReferenceResolver(elCtx.getELResolver());
        valueExpression.getValue(new ELContextDecorator(elCtx, resolver));
        return resolver.lastObject;
    }

    /**
     * This method is the only one that matters. It keeps track of the objects in the EL expression.
     * <p/>
     * It creates a new ValueReferenceWrapper and assigns it to lastObject.
     *
     * @param context  The ELContext.
     * @param base     The base object, may be null.
     * @param property The property, may be null.
     * @return The resolved value
     */
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        lastObject = new ValueReferenceWrapper(base, property);
        return resolver.getValue(context, base, property);
    }

    // ############################ Standard delegating implementations ############################

    public final Class<?> getType(final ELContext ctx, final Object base, final Object property) {
        return resolver.getType(ctx, base, property);
    }

    public final void setValue(final ELContext ctx, final Object base, final Object property, final Object value) {
        resolver.setValue(ctx, base, property, value);
    }

    public final boolean isReadOnly(final ELContext ctx, final Object base, final Object property) {
        return resolver.isReadOnly(ctx, base, property);
    }

    public final Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext ctx, final Object base) {
        return resolver.getFeatureDescriptors(ctx, base);
    }

    public final Class<?> getCommonPropertyType(final ELContext ctx, final Object base) {
        return resolver.getCommonPropertyType(ctx, base);
    }

}
