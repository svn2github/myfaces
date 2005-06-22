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
package org.apache.myfaces.custom.buffer;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class Buffer extends UIComponentBase{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.Buffer";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    private static final String DEFAULT_RENDERER_TYPE = BufferRenderer.RENDERER_TYPE;

    private String _intoExpression = null;

    public Buffer(){
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily(){
        return COMPONENT_FAMILY;
    }

    public void setInto(String intoExpression){
        _intoExpression = intoExpression;
    }

    public Object saveState(FacesContext context){
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _intoExpression;
        return values;
    }

    public void restoreState(FacesContext context, Object state){
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _intoExpression = (String) values[1];
    }

    void fill(String content, FacesContext facesContext){
        ValueBinding intoVB;

        if (_intoExpression == null) {
            intoVB = getValueBinding("into");
            _intoExpression = intoVB.getExpressionString();
        } else {
            intoVB = facesContext.getApplication().createValueBinding( _intoExpression );
        }

        intoVB.setValue(facesContext, content);
    }
}