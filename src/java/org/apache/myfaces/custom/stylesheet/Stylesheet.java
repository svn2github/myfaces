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
package org.apache.myfaces.custom.stylesheet;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
*/

public class Stylesheet extends UIOutput {

	public static final String COMPONENT_TYPE = "org.apache.myfaces.Stylesheet";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";
	private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Stylesheet";
	private static final Log log = LogFactory.getLog(Stylesheet.class);

    private String _path = null;


    // ------------------------------------------------------------ Constructors
    public Stylesheet() {

        setRendererType(DEFAULT_RENDERER_TYPE);

    }


    public String getFamily() {

        return COMPONENT_FAMILY;

    }


    public String getPath() {

		if (_path != null) return _path;
		ValueBinding vb = getValueBinding("path");
		return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPath(String path) {
        this._path = path;
    }

    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _path = (String) values[1];

    }

    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _path;
        return values;

    }
}