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
package org.apache.myfaces.custom.div;

import javax.faces.component.behavior.ClientBehaviorHolder;

import org.apache.myfaces.component.EventAware;
import org.apache.myfaces.component.UniversalProperties;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

/**
 * Places a div around its children. Unless otherwise specified, 
 * all attributes accept static values or EL expressions.
 * 
 * @JSFComponent
 *   name = "t:div"
 *   class = "org.apache.myfaces.custom.div.Div"
 *   tagClass = "org.apache.myfaces.custom.div.DivTag"
 * @since 1.1.7
 * @author bdudney (latest modification by $Author: lu4242 $)
 * @version $Revision: 691856 $ $Date: 2008-09-03 21:40:30 -0500 (mié, 03 sep 2008) $
 */
public abstract class AbstractDiv extends HtmlTag implements EventAware, UniversalProperties, ClientBehaviorHolder
{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.Div";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.DivRenderer";

    /**
     * @JSFProperty
     *   tagExcluded = "true"
     */
    public Object getValue()
    {
        return "div";
    }
}