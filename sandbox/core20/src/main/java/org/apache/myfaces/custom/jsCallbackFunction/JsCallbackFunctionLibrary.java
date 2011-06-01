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
package org.apache.myfaces.custom.jsCallbackFunction;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;

public final class JsCallbackFunctionLibrary
{
    public JsCallbackFunctionLibrary()
    {
    }
    
    public static String jsCallbackFunctionName(UIComponent c, String expr)
    {
        UIComponent foundComponent = null;
        if (c != null)
        {
            foundComponent = c.findComponent(expr);
        }
        if (foundComponent == null)
        {
            foundComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent(expr);
        }
        if (foundComponent == null)
        {
            throw new FacesException("Cannot found target component:" + expr);
        }
        JsCallbackFunctionName jsFunctionCallback = (JsCallbackFunctionName) foundComponent;
        
        return jsFunctionCallback.getFunctionName();
    }
    
    public static String generateJsFunctionName(String name)
    {
        return JavascriptUtils.getValidJavascriptName(name, true);
    }
}
