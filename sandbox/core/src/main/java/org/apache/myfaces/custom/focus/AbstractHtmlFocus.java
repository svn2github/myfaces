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
package org.apache.myfaces.custom.focus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.UIComponentPerspective;
import org.apache.myfaces.component.html.util.HtmlComponentUtils;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import java.util.Iterator;

/**
 * @author Rogerio Pereira Araujo (latest modification by $Author$)
 * @version $Revision$ $Date$
 * @JSFComponent name = "s:focus"
 * class = "org.apache.myfaces.custom.focus.HtmlFocus"
 * tagClass = "org.apache.myfaces.custom.focus.HtmlFocusTag"
 */
public abstract class AbstractHtmlFocus extends UIInput
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Focus";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Focus";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";

    private static final boolean DEFAULT_REMEMBER_CLIENT_FOCUS = false;

    private static Log log = LogFactory.getLog(AbstractHtmlFocus.class);

    /**
     * The JSF id of the component to receive focus.
     *
     * @JSFProperty
     */
    public abstract String getFor();

    /**
     * @JSFProperty defaultValue="false"
     */
    public abstract boolean isRememberClientFocus();


    /*protected UIComponent findUIComponent()
    {
        String forStr = getFor();

        if (forStr == null)
        {
            throw new IllegalArgumentException("focus@for must be specified");
        }

        UIComponent forComp = findComponent(forStr);
        if (forComp == null)
        {
            log.warn("could not find UIComponent referenced by attribute focus@for = '"
                    + forStr + "'");
        }
        return forComp;
    }*/

    /**
     * Standard method for finding other components by id, inherited by
     * most UIComponent objects.
     * <p/>
     * The lookup is performed in a manner similar to finding a file
     * in a filesystem; there is a "base" at which to start, and the
     * id can be for something in the "local directory", or can include
     * a relative path. Here, NamingContainer components fill the role
     * of directories, and ":" is the "path separator". Note, however,
     * that although components have a strict parent/child hierarchy,
     * component ids are only prefixed ("namespaced") with the id of
     * their parent when the parent is a NamingContainer.
     * <p/>
     * The base node at which the search starts is determined as
     * follows:
     * <ul>
     * <li>When expr starts with ':', the search starts with the root
     * component of the tree that this component is in (ie the ancestor
     * whose parent is null).
     * <li>Otherwise, if this component is a NamingContainer then the search
     * starts with this component.
     * <li>Otherwise, the search starts from the nearest ancestor
     * NamingContainer (or the root component if there is no NamingContainer
     * ancestor).
     * </ul>
     *
     * @param expr is of form "id1:id2:id3".
     * @return UIComponent or null if no component with the specified id is
     *         found.
     */
    //https://issues.apache.org/jira/browse/TOMAHAWK-1607
    UIComponent getRootComponent(UIComponent component)
    {
        UIComponent parent;
        for (;;)
        {
            parent = component.getParent();
            if (parent == null)
            {
                return component;
            }
            component = parent;
        }
    }

    public UIComponent findComponentWithRowSupport(String expr)
    {
        if (expr == null) throw new NullPointerException("expr");
        if (expr.length() == 0) throw new IllegalArgumentException("empty expr"); //TODO: not specified!

        UIComponent findBase;
        if (expr.charAt(0) == NamingContainer.SEPARATOR_CHAR)
        {
            findBase = getRootComponent(this);
            expr = expr.substring(1);
        } else
        {
            if (this instanceof NamingContainer)
            {
                findBase = this;
            } else
            {
                findBase = HtmlComponentUtils.findParentNamingContainer(this, true /* root if not found */);
            }
        }

        return findComponentWithRowSupportFromBase(findBase, expr);
    }

    public UIComponent findComponentWithRowSupportFromBase(UIComponent base, String expr)
    {
        if (expr == null) throw new NullPointerException("expr");
        if (expr.length() == 0) throw new IllegalArgumentException("empty expr"); //TODO: not specified!

        // Remember current row index
        int originalRowIndex = -2;
        UIData uiData = null;
        if (base instanceof UIData)
        {
            uiData = (UIData) base;
            originalRowIndex = uiData.getRowIndex();
        }

        UIComponent foundComponent;
        int separator = expr.indexOf(NamingContainer.SEPARATOR_CHAR);
        if (separator == -1)
        {
            foundComponent = findComponentWithRowSupport(base, expr);
        } else
        {
            String id = expr.substring(0, separator);
            base = findComponentWithRowSupport(base, id);
            if (base == null)
            {
                foundComponent = null;
            } else
            {
                if (!(base instanceof NamingContainer))
                    throw new IllegalArgumentException("Intermediate identifier " + id + " in search expression " +
                            expr + " identifies a UIComponent that is not a NamingContainer");
                foundComponent = findComponentWithRowSupportFromBase(base, expr.substring(separator + 1));
            }
        }

        // Reset row index
        if (null != uiData)
        {
            if (foundComponent != uiData)
            {

                UIComponentPerspective cp = new UIComponentPerspective(uiData, foundComponent, uiData.getRowIndex());
                foundComponent = cp;

                // If the foundComponent is the uiData with a particular rowIndex, I guess we're going to need to leave it alone.
                uiData.setRowIndex(originalRowIndex);
            }
        }

        return foundComponent;
    }

    UIComponent findComponentWithRowSupport(UIComponent findBase, String id)
    {
        if ((findBase instanceof UIData) && (id.matches("[0-9]*")))
        {
            UIData uiData = (UIData) findBase;
            int rowIndex = Integer.parseInt(id);
            uiData.setRowIndex(rowIndex);
            return uiData;
        }

        if (idsAreEqual(id, findBase))
        {
            return findBase;
        }

        for (Iterator it = findBase.getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent childOrFacet = (UIComponent) it.next();
            if (!(childOrFacet instanceof NamingContainer))
            {
                UIComponent find = findComponentWithRowSupportFromBase(childOrFacet, id);
                if (find != null) return find;
            } else if (idsAreEqual(id, childOrFacet))
            {
                return childOrFacet;
            }
        }

        return null;
    }

    /*
     * Return true if the specified component matches the provided id.
     * This needs some quirks to handle components whose id value gets
     * dynamically "tweaked", eg a UIData component whose id gets
     * the current row index appended to it.
     */
    private static boolean idsAreEqual(String id, UIComponent cmp)
    {
        if (id.equals(cmp.getId()))
            return true;

        if (cmp instanceof UIData)
        {
            UIData uiData = ((UIData) cmp);

            if (uiData.getRowIndex() == -1)
            {
                return dynamicIdIsEqual(id, cmp.getId());
            } else
            {
                return id.equals(cmp.getId() + NamingContainer.SEPARATOR_CHAR + uiData.getRowIndex());
            }
        }

        return false;
    }

    private static boolean dynamicIdIsEqual(String dynamicId, String id)
    {
        return dynamicId.matches(id + ":[0-9]*");
    }

    protected UIComponent findUIComponent()
    {
        String forStr = getFor();

        if (forStr == null)
        {
            throw new IllegalArgumentException("focus@for must be specified");
        }

        UIComponent forComp = findComponentWithRowSupport(forStr);

        if (forComp == null)
        {
            log.warn("could not find UIComponent referenced by attribute focus@for = '"
                    + forStr + "'");
        }
        return forComp;
    }

}
