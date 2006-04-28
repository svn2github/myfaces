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
package org.apache.myfaces.custom.collapsiblemenu;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlLinkRendererBase;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.HTMLEncoder;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;

/**
 * Renderer for the Collapsible Icon component
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class HtmlCollapsibleIconRenderer extends HtmlLinkRendererBase {

    public void decode(FacesContext facesContext, UIComponent component)
    {
        String clientId = component.getClientId(facesContext);
        
        String formName = CollapsibleMenuUtils.getFormName(component, facesContext);
        String fieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(formName);
                
        String reqValue = (String)
            facesContext.getExternalContext().getRequestParameterMap().get(fieldName);
        
        if (reqValue != null && reqValue.equals(clientId))
        {
            component.queueEvent(new ActionEvent(component));
    
            CollapsiblePanel panel = (CollapsiblePanel) component.getParent();
            //since only 1 panel can be displayed, hide all the other panels
            Iterator it = panel.getParent().getChildren().iterator();
            while (it.hasNext()) {
                UIComponent child = (UIComponent) it.next();
                if (child instanceof CollapsiblePanel) {
                    ((CollapsiblePanel) child).setDisplayed(false);
                }
            }
            //set the parent panel of this icon to be the currently expanded panel
            panel.setDisplayed(true);
        } 
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        
        CollapsibleIcon icon = (CollapsibleIcon) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.writeComment("Begin Render Icon");
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.startElement("p", icon);
        writer.writeAttribute(HTML.STYLE_ATTR, "cursor:pointer", null);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.SPAN_ELEM, icon);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.startElement(HTML.IMG_ELEM, icon);
        String url = icon.getUrl();
        String src = facesContext.getApplication().
            getViewHandler().getResourceURL(facesContext, url);
        writer.writeAttribute(HTML.SRC_ATTR, src, null);
        
        writer.writeAttribute(HTML.CLASS_ATTR, "icon", null);
        writer.writeAttribute(HTML.WIDTH_ATTR, "32", null);
        writer.writeAttribute(HTML.HEIGHT_ATTR, "32", null);
        writer.writeAttribute(HTML.BORDER_ATTR, "0", null);
                
        String onMouseOver = "this.className='iconOver'";
        writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, onMouseOver, null);
        
        String onMouseOut = "this.className='icon'";
        writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, onMouseOut, null);
        
        String onMouseDown = "this.className='iconClick'";
        writer.writeAttribute(HTML.ONMOUSEDOWN_ATTR, onMouseDown, null);
        
        String onMouseUp = "this.className='iconOver'";
        writer.writeAttribute(HTML.ONMOUSEUP_ATTR, onMouseUp, null);
                
        String onClick = generateOnClickJavaScript(facesContext, component);
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClick, null);
        
        writer.endElement(HTML.IMG_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.endElement(HTML.SPAN_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.write("<br>");
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.startElement(HTML.SPAN_ELEM, icon);
        writer.writeAttribute(HTML.CLASS_ATTR, "iconLabel", null);
        
        writer.write(icon.getTitle());
        writer.endElement(HTML.SPAN_ELEM);
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement("p");
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.writeComment("End Render Icon");
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
    }
    
    private String generateOnClickJavaScript(FacesContext facesContext,
            UIComponent component) throws IOException
    {
        
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);        
        
        //Get form name
        String formName = CollapsibleMenuUtils.getFormName(component, facesContext);
        
        StringBuffer onClick = new StringBuffer();

        //call the clear_<formName> method
        onClick.append(HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName
                (formName)).append("();");

        String jsForm = "document.forms['" + formName + "']";

        if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isAutoScroll())
        {
            JavascriptUtils.appendAutoScrollAssignment(onClick, formName);
        }

        //add id parameter for decode
        String hiddenFieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(formName);
        onClick.append(jsForm);
        onClick.append(".elements['").append(hiddenFieldName).append("']");
        onClick.append(".value='").append(clientId).append("';");
        
        addHiddenCommandParameter(facesContext, component, hiddenFieldName);
        
        //add child parameters
        for (Iterator it = getChildren(component).iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                Object value = ((UIParameter)child).getValue();

                renderLinkParameter(name, value, onClick, jsForm, component);
            }
        }

        // onSubmit
        onClick.append("if(").append(jsForm).append(".onsubmit){var result=").
            append(jsForm).append(".onsubmit();  if( (typeof result == 'undefined') || result ) {"+jsForm+".submit();}}else{");

        //submit
        onClick.append(jsForm);
        onClick.append(".submit();}return false;");  //return false, so that browser does not handle the click

        return onClick.toString();
        //writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
    }
    
    private void renderLinkParameter(String name,
            Object value,
            StringBuffer onClick,
            String jsForm,
            UIComponent component)
    {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }
        onClick.append(jsForm);
        onClick.append(".elements['").append(name).append("']");
        //UIParameter is no ValueHolder, so no conversion possible
        String strParamValue = value != null ? HTMLEncoder.encode(value.toString(), false, false) : "";
        onClick.append(".value='").append(strParamValue).append("';");

        addHiddenCommandParameter(facesContext, component, name);
    }

    //Either a form exists above this component in the component heirarchy, 
    //or we use the dummy form generated by MyFaces
    private void addHiddenCommandParameter(FacesContext facesContext, 
            UIComponent component, String hiddenFieldName) {
        
        UIForm nestingForm = CollapsibleMenuUtils.getParentForm(component);
        
        if (nestingForm != null) {
            super.addHiddenCommandParameter(facesContext, nestingForm, hiddenFieldName);
        }
        else {
            DummyFormUtils.addDummyFormParameter(facesContext, hiddenFieldName);
        }
    }

}
   