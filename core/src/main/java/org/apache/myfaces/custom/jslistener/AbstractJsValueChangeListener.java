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
package org.apache.myfaces.custom.jslistener;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;

/**
 * Value change listener on client side. 
 * 
 * Unless otherwise specified, all attributes accept static values or EL expressions.
 * 
 * @JSFComponent
 *   name = "t:jsValueChangeListener"
 *   class = "org.apache.myfaces.custom.jslistener.JsValueChangeListener"
 *   tagClass = "org.apache.myfaces.custom.jslistener.JsValueChangeListenerTag"
 * 
 * @JSFJspProperty name = "rendered" returnType = "boolean" tagExcluded = "true"
 * @JSFJspProperty name = "binding" returnType = "java.lang.String" tagExcluded = "true"
 * @JSFJspProperty name = "id" returnType = "java.lang.String" tagExcluded = "true"
 * 
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractJsValueChangeListener extends UIComponentBase
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.JsValueChangeListener";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.JsValueChangeListener";

    /**
     * @JSFProperty
     */
    public abstract String getFor();

    /**
     * @JSFProperty
     *   required="true"
     */
    public abstract String getExpressionValue();

    /**
     * @JSFProperty
     */
    public abstract String getProperty();

    /**
     *  If specified this JavaScript event will be inserted in the 
     *  body tag. JavaScript code will be the same like it is 
     *  rendered in the parent component.
     * 
     * @JSFProperty
     */
    public abstract String getBodyTagEvent();

}
