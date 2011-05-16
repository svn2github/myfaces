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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessages;
import java.util.ArrayList;
import java.util.List;

/**
 * Common component tree utilities.
 *
 * @author Jan-Kees van Andel
 */
final class ComponentUtils {

    /**
     * Find and return the parent form for the given component.
     *
     * @param component The component.
     * @return The parent form, never <code>null</code>.
     * @throws FacesException if no parent form is found.
     */
    static UIForm findParentForm(UIComponent component) {
        while (component.getParent() != null) {
            component = component.getParent();
            if (component instanceof UIForm) {
                return (UIForm) component;
            }
        }
        throw new FacesException("No parent form found");
    }

    /**
     * Find and return all UIInput instances in the given form.
     * <p/>
     * This method doesn't care about nesting. It looks through the entire form and returns all UIInput components.
     *
     * @param form The form to search in.
     * @return A list with all UIInput components in the form, never <code>null</code>. If nothing is found,
     *         an empty {java.util.List} is returned.
     */
    static List<UIInput> findInputsInForm(UIForm form) {
        return findInputsInTree(form);
    }

    /**
     * Find all UIInput instances that are children of the given component tree.
     *
     * @param component The component tree to search in.
     * @return A list with all UIInput components in the component, never <code>null</code>. If nothing is found,
     *         an empty {java.util.List} is returned.
     */
    private static List<UIInput> findInputsInTree(final UIComponent component) {
        final List<UIInput> ret = new ArrayList<UIInput>();

        if (component != null) {
            final List<UIComponent> children = component.getChildren();
            for (final UIComponent child : children) {
                if (child instanceof UIInput) {
                    ret.add((UIInput) child);
                } else {
                    ret.addAll(findInputsInTree(child));
                }
            }
        }

        return ret;
    }

    /**
     * Find the first UIMessages instance in the given component tree.
     *
     * @param component The component tree to search in.
     * @return The first UIMessages instance, or <code>null</code> if nothing is found.
     */
    static UIMessages findMessagesInTree(final UIComponent component) {
        final List<UIComponent> children = component.getChildren();
        for (final UIComponent child : children) {
            if (child instanceof UIMessages) {
                return (UIMessages) child;
            } else {
                final UIMessages messages = findMessagesInTree(child);
                if (messages != null) {
                    return messages;
                }
            }
        }
        return null;
    }
}
