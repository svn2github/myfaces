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
package org.apache.myfaces.validator;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.LegacyValueBinding;
import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.tag.TagAttribute;

final class _ValidatorRule extends MetaRule {

    final static class ValueBindingMetadata extends Metadata {

        private final String name;

        private final TagAttribute attr;

        private final Class type;

        public ValueBindingMetadata(String name, Class type, TagAttribute attr) {
            this.name = name;
            this.attr = attr;
            this.type = type;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ValidatorBase) instance).setValueBinding(this.name,
                    new LegacyValueBinding(this.attr.getValueExpression(ctx,
                            this.type)));
        }

    }

    public final static _ValidatorRule Instance = new _ValidatorRule();

    public _ValidatorRule() {
        super();
    }

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {
        if (meta.isTargetInstanceOf(ValidatorBase.class)) {

            // if component and dynamic, then must set expression
            if (!attribute.isLiteral()) {
                Class type = meta.getPropertyType(name);
                if (type == null) {
                    type = Object.class;
                }
                return new ValueBindingMetadata(name, type, attribute);
            }
        }
        return null;
    }
}
