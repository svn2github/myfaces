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
package org.apache.myfaces.custom.selectOneLanguage;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.html.ext.HtmlSelectOneMenuTag;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date: 2005-05-11 12:14:23 -0400 (Wed, 11 May 2005) $
 */
public class SelectOneLanguageTag extends HtmlSelectOneMenuTag {
    public String getComponentType() {
        return SelectOneLanguage.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.apache.myfaces.SelectOneLanguageRenderer";
    }

    private String maxLength;

    public void release() {
        super.release();
        maxLength=null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setIntegerProperty(component, "maxLength", maxLength);
    }

    public void setMaxLength(String maxLength){
        this.maxLength = maxLength;
    }
}