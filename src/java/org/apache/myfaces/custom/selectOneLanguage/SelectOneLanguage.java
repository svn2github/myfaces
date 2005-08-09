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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date: 2005-05-11 12:14:23 -0400 (Wed, 11 May 2005) $
 */
public class SelectOneLanguage extends HtmlSelectOneMenu {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.SelectOneLanguage";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SelectOneLanguageRenderer";

    private Integer _maxLength = null;

    public SelectOneLanguage() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

	public Integer getMaxLength() {
		if (_maxLength != null) return _maxLength;
		ValueBinding vb = getValueBinding("length");
		return vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
	}
	public void setMaxLength(Integer maxLength) {
		_maxLength = maxLength;
	}

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _maxLength;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _maxLength = (Integer)values[1];
    }
}