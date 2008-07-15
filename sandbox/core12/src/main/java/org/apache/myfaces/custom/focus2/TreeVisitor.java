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
package org.apache.myfaces.custom.focus2;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class TreeVisitor
{

    public static interface TreeTraversalListener
    {

        /**
         * @param context
         * @param level
         * @param component
         * @return false to stop traversal
         */
        boolean traverse(FacesContext context, int level, UIComponent component);
    }

    public static void traverseTree(FacesContext context,
            TreeTraversalListener listener, UIComponent startingFrom)
    {

        int level = 0;

        if (!listener.traverse(context, level, startingFrom))
            return;

        if (!recurseDown(context, listener, startingFrom, level + 1))
            return;

        recurseUp(context, listener, startingFrom, level - 1);
    }

    private static boolean recurseDown(FacesContext context,
            TreeTraversalListener listener, UIComponent comp, int level)
    {
        Iterator it = comp.getFacetsAndChildren();

        while (it.hasNext())
        {
            UIComponent child = (UIComponent) it.next();
            if (!listener.traverse(context, level, child))
                return false;
            if (!recurseDown(context, listener, child, level + 1))
                return false;
        }

        return true;
    }

    private static boolean recurseUp(FacesContext context,
            TreeTraversalListener listener, UIComponent comp, int level)
    {
        UIComponent parent = comp.getParent();
        if (parent != null)
        {
            Iterator siblingIt = parent.getFacetsAndChildren();

            boolean traverseNow = false;

            while (siblingIt.hasNext())
            {
                if (traverseNow)
                {
                    UIComponent nextSibling = (UIComponent) siblingIt.next();

                    if (!listener.traverse(context, level, nextSibling))
                        return false;

                    if (!recurseDown(context, listener, nextSibling, level + 1))
                        return false;
                }
                else
                {
                    UIComponent potSibling = (UIComponent) siblingIt.next();

                    if (potSibling == comp)
                    {
                        traverseNow = true;
                    }
                }
            }

            return recurseUp(context, listener, parent, level - 1);
        }
        return false;
    }
}
