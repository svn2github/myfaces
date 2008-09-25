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
package org.apache.myfaces.custom.tabbedpane;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.myfaces.shared_tomahawk.util.ClassUtils;


/**
 * Adds a tab-change-listener to the enclosing t:panelTabbedPane component.
 * <p>
 * When the panelTabbedPane changes the displayed tab, the listener is invoked.
 * </p>
 *
 * @JSFJspTag
 *   name="t:tabChangeListener"
 *   bodyContent="empty"
 *   tagHandler = "org.apache.myfaces.custom.tabbedpane.TabChangeListenerTagHandler"
 *   
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 */
public class TabChangeListenerTag extends TagSupport
{
    private static final long serialVersionUID = -6903749011638483023L;
    private String type = null;


    public TabChangeListenerTag()
    {
    }

    /**
     * Define a listener to be attached to the parent HtmlPanelTabbedPane instance.
     * <p>
     * This attribute may be a literal string containing a fully-qualified class name. The
     * specified class must implement the TabChangeListener interface and have a no-arguments
     * constructor. A new instance will be created when the view is created.
     * </p>
     * <p>
     * This attribute may also be an EL expression that returns type String. The EL expression will be
     * evaluated when the view is built, and the returned value must be a fully-qualified class name. The
     * specified class must implement the TabChangeListener interface and have a no-arguments constructor.
     * A new instance will be created when the view is created.
     * </p>
     * <p>
     * This attribute may also be an EL expression that returns a TabChangeListener instance.
     * </p>
     * <p>
     * It is an error if an EL expression returns an object of any type other than String or TabChangeListener.
     * </p>
     * 
     * @JSFJspAttribute required = "true"
     */
    public void setType(String type)
    {
        this.type = type;
    }


    public int doStartTag() throws JspException
    {
        if (type == null)
        {
            throw new JspException("type attribute not set");
        }

        //Find parent UIComponentTag
        UIComponentTag parentComponentTag = UIComponentTag.getParentUIComponentTag(pageContext);
        if (parentComponentTag == null)
        {
            throw new JspException("TabChangeListenerTag has no UIComponentTag ancestor");
        }

        if (parentComponentTag.getCreated())
        {
            //Component was just created, so we add the Listener
            UIComponent parent = parentComponentTag.getComponentInstance();
            if (parent instanceof HtmlPanelTabbedPane)
            {
                Object listenerRef = type;
                if (UIComponentTag.isValueReference(type))
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ValueBinding valueBinding = facesContext.getApplication().createValueBinding(type);
                    listenerRef = valueBinding.getValue(facesContext);
                }

                if(listenerRef instanceof String)
                {
                    String className = (String) listenerRef;
                    TabChangeListener listener = (TabChangeListener) ClassUtils.newInstance(className);
                    ((HtmlPanelTabbedPane) parent).addTabChangeListener(listener);
                }
                else if(listenerRef instanceof TabChangeListener)
                {
                    TabChangeListener listener = (TabChangeListener) listenerRef;
                    ((HtmlPanelTabbedPane) parent).addTabChangeListener(listener);
                }
                else if (listenerRef == null)
                {
                    throw new JspException("Property 'type' must not be null.");
                }
                else
                {
                    throw new JspException(
                       "Property 'type' must be either a string (containing a class name) " +
                       "or a TabChangeListener instance.");
                }
            }
            else
            {
                throw new JspException(
                    "Component " + parent.getId() + " is not of type HtmlPanelTabbedPane");
            }
        }

        return Tag.SKIP_BODY;
    }
}
