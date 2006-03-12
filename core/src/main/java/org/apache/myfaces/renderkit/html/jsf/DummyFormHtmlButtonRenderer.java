/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.renderkit.html.jsf;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlButtonRendererBase;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;


/**
 * Add dummyForm functionality 
 * 
 * @author Mario Ivankovits (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DummyFormHtmlButtonRenderer
    extends HtmlButtonRendererBase
{
    protected void addHiddenCommandParameter(FacesContext facesContext, UIForm nestingForm, String hiddenFieldName)
	{
		if (nestingForm != null)
        {
			super.addHiddenCommandParameter(facesContext, nestingForm, hiddenFieldName);
        }
		else
		{
            DummyFormUtils.addDummyFormParameter(facesContext, hiddenFieldName);
		}
	}
    
    protected FormInfo findNestingForm(UIComponent uiComponent, FacesContext facesContext)
    {
    	return DummyFormUtils.findNestingForm(uiComponent, facesContext);
    }
}
