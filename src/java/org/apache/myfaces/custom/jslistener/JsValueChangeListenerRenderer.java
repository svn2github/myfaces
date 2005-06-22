/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.jslistener;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.custom.jsvalueset.HtmlJsValueSet;
import org.apache.myfaces.component.html.util.AddResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JsValueChangeListenerRenderer
        extends HtmlRenderer
{
    private static Log log = LogFactory.getLog(JsValueChangeListenerRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, JsValueChangeListener.class);

        UIComponent parent = component.getParent();

        JsValueChangeListener jsValueChangeListener = (JsValueChangeListener) component;


        String aFor = jsValueChangeListener.getFor();
        String expressionValue = jsValueChangeListener.getExpressionValue();
        String property = jsValueChangeListener.getProperty();

        AddResource.addJavaScriptToHeader(
                JsValueChangeListenerTag.class, "JSListener.js", false, getFacesContext());

        if(aFor!=null)
        {
            UIComponent forComponent = component.findComponent(aFor);

            String forComponentId = null;

            if (forComponent == null)
            {
                if (log.isInfoEnabled())
                {
                    log.info("Unable to find component '" + aFor + "' (calling findComponent on component '" + component.getClientId(getFacesContext()) + "') - will try to render component id based on the parent-id (on same level)");
                }
                if (aFor.length() > 0 && aFor.charAt(0) == UINamingContainer.SEPARATOR_CHAR)
                {
                    //absolute id path
                    forComponentId = aFor.substring(1);
                }
                else
                {
                    //relative id path, we assume a component on the same level as the label component
                    String labelClientId = component.getClientId(getFacesContext());
                    int colon = labelClientId.lastIndexOf(UINamingContainer.SEPARATOR_CHAR);
                    if (colon == -1)
                    {
                        forComponentId = aFor;
                    }
                    else
                    {
                        forComponentId = labelClientId.substring(0, colon + 1) + aFor;
                    }
                }
            }
            else
            {
                forComponentId = forComponent.getClientId(getFacesContext());
            }

            expressionValue = expressionValue.replaceAll("\\'","\\\\'");
            expressionValue = expressionValue.replaceAll("\"","\\\"");


            String methodCall = "orgApacheMyfacesJsListenerSetExpressionProperty('"+
                    parent.getClientId(getFacesContext())+"','"+
                    forComponentId+"',"+
                    (property==null?"null":"'"+property+"'")+
                    ",'"+expressionValue+"');";


            callMethod(parent, "onchange",methodCall);

        }
    }

    private void callMethod(UIComponent uiComponent, String propName, String value)
    {
        Object oldValue = uiComponent.getAttributes().get(propName);

        if(oldValue != null)
        {
            String oldValueStr = oldValue.toString().trim();

            //check if method call has already been added...
            if(oldValueStr.indexOf(value)!=-1)
                return;

            if(oldValueStr.length()>0 && !oldValueStr.endsWith(";"))
                oldValueStr +=";";

            value = oldValueStr + value;

        }

        uiComponent.getAttributes().put(propName, value);
    }


    protected Application getApplication()
    {
        return getFacesContext().getApplication();
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

}
