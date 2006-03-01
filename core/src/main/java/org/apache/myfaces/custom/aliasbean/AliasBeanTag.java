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
package org.apache.myfaces.custom.aliasbean;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AliasBeanTag extends UIComponentTagBase {

    private Log log = LogFactory.getLog(AliasBeanTag.class);

    private String _alias;
    private String _valueExpression;

    public void release() {
        super.release();

        _alias=null;
        _valueExpression=null;

    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, "alias", _alias);
        setStringProperty(component, "value", _valueExpression);
    }

    public String getComponentType() {
        return AliasBean.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    public void setAlias(String alias){
        _alias = alias;
    }

    public void setValue(String valueExpression){
        _valueExpression = valueExpression;
    }

    public int doStartTag() throws JspException
    {
        int retVal= super.doStartTag();

        UIComponent comp = getComponentInstance();

        if(comp instanceof AliasBean)
        {
            ((AliasBean) comp).makeAlias(getFacesContext());
        }
        else
        {
            log.warn("associated component is no aliasBean");
        }

        return retVal;
    }

    public int doEndTag() throws JspException
    {
        UIComponent comp = getComponentInstance();

        if(comp instanceof AliasBean)
        {
            ((AliasBean) comp).removeAlias(getFacesContext());
        }
        else
        {
            log.warn("associated component is no aliasBean");
        }

        return super.doEndTag();
    }
}