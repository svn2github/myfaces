/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.collapsiblemenu;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

/**
 * Allows the Collapsible menu to be constructed from a backing bean, instead of 
 * static JSP
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class DynamicCollapsibleMenuTag extends UIComponentTagBase
{
    public String getComponentType()
    {
        return "javax.faces.SelectItems";
    }

    public String getRendererType()
    {
        return null;
    }
}