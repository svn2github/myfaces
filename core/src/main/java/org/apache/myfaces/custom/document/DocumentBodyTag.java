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
package org.apache.myfaces.custom.document;
import javax.faces.component.UIComponent;

/**
 * Document to enclose the whole document. If not otherwise possible you can use
 * state="start|end" to demarkate the document boundaries
 * 
 * @author Mario Ivankovits (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DocumentBodyTag extends AbstractDocumentTag
{
    private String _onload;
    private String _onunload;
    private String _onresize;
    private String _onkeypress;

    public String getComponentType()
	{
		return DocumentBody.COMPONENT_TYPE;
	}

	public String getRendererType()
	{
		return DocumentBodyRenderer.RENDERER_TYPE;
	}

    public void release() {
        super.release();
        _onload = null;
        _onunload = null;
        _onresize = null;
        _onkeypress = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setStringProperty(component, "onload", _onload);
        setStringProperty(component, "onunload", _onunload);
        setStringProperty(component, "onresize", _onresize);
        setStringProperty(component, "onkeypress", _onkeypress);
    }

    public void setOnload(String onload) {
        _onload = onload;
    }

    public void setOnunload(String onunload) {
        _onunload = onunload;
    }

    public void setOnresize(String onresize) {
        _onresize = onresize;
    }

    public void setOnkeypress(String onkeypress) {
        _onkeypress = onkeypress;
    }
}