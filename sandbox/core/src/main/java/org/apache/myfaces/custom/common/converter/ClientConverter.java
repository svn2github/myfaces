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
package org.apache.myfaces.custom.common.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * interface to allow a converter to send code which converts a string to object and vice versa
 * on the client
 *
 * @author imario@apache.org
 */
public interface ClientConverter
{
    /**
     * This should add the converter scriptlet to the resulting view only once.
     * Using {@link org.apache.myfaces.renderkit.html.util.AddResource} will ensure this.
     *
     * @param facesContext current faces context
     */
    public void addScriptResource(FacesContext facesContext);

    /**
     * This should return the javascript portion which can be used to convert the value
     * of the given component. <br />
     * Example:
     * <code>
     * new org.myfaces.converter.NumberConverter().getAsObject('[component.getClientId()]');
     * </code>
     * For sure the clientId stuff is evaluated on the server.
     * As you can see there will be no <code>var result =</code> stuff rendered. You have to
     * ensure that you do something with the result yourself.
     *
     * @param facesContext current faces context
     * @param component the component to convert its value
     * @return the scriptlet
     */
    public String getConvertScript(FacesContext facesContext, UIComponent component);

    /**
     * Get the allowed input mask. This mask can be used to check each keypress during input
     *
     * @return null if not suitable
     */
    public InputMask getInputMask();
}
