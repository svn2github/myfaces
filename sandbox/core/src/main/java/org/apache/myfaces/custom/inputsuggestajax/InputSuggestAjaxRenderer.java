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

package org.apache.myfaces.custom.inputsuggestajax;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.custom.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.prototype.PrototypeResourceLoader;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.util.UnicodeEncoder;
import org.apache.myfaces.renderkit.html.ext.HtmlTextRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gerald Muellan
 * @author Martin Marinschek
 * @version $Revision: 177984 $ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class InputSuggestAjaxRenderer extends HtmlTextRenderer implements AjaxRenderer
{
    private static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

   /**
     * Encodes any stand-alone javascript functions that are needed.
     * Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException
     */
    private void encodeJavascript(FacesContext context, UIComponent component) throws IOException
    {
        // AddResource takes care to add only one reference to the same script

        String javascriptLocation = (String)component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        String styleLocation = (String)component.getAttributes().get(JSFAttr.STYLE_LOCATION);

        AddResource addResource = AddResourceFactory.getInstance(context);
        if(javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/prototype.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/effects.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/dragdrop.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/controls.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/myFaces.js");
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "prototype.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "effects.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "dragdrop.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "controls.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputSuggestAjaxRenderer.class, "myFaces.js");
        }

        if( StringUtils.isNotBlank( styleLocation ) )
        {
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, styleLocation + "/theme.css");
        }
        else
        {
            String theme = ((InputSuggestAjax)component).getLayout();
            if(theme == null)
            {
                theme = "default";
            }
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputSuggestAjaxRenderer.class, theme + "/theme.css");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, InputSuggestAjax.class);

        if( HtmlRendererUtils.isDisplayValueOnly(component) || isDisabled(context, component)){
        	super.encodeEnd(context, component);
        	return;
        }
        
        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        this.encodeJavascript(context,component);

        inputSuggestAjax.getAttributes().put("autocomplete","off");
        
        String oldStyleClass = inputSuggestAjax.getStyleClass();
        inputSuggestAjax.setStyleClass(
        		(oldStyleClass!=null && oldStyleClass.length()>=0 ? oldStyleClass : "")+" myFacesInputSuggestAjax");
        
        super.encodeEnd(context, component);

        inputSuggestAjax.setStyleClass(oldStyleClass);
        
        ResponseWriter out = context.getResponseWriter();

        out.startElement(HTML.DIV_ELEM, null);
        if(inputSuggestAjax.getLayout().equals("default"))
        {
            out.writeAttribute(HTML.CLASS_ATTR, "ajaxPopup", null);
        }
        if(inputSuggestAjax.getPopupStyleClass()!= null)
        {
            out.writeAttribute(HTML.CLASS_ATTR, inputSuggestAjax.getPopupStyleClass(), null);
        }
        if (inputSuggestAjax.getPopupId() != null)
        {
            out.writeAttribute(HTML.ID_ATTR,inputSuggestAjax.getPopupId(), null);
        }
        else
        {
            out.writeAttribute(HTML.ID_ATTR,
                    component.getClientId(context)+"_auto_complete", null);
        }
        if (inputSuggestAjax.getPopupStyle() != null)
        {
            out.writeAttribute(HTML.STYLE_ATTR, inputSuggestAjax.getPopupStyle(),null);
        }
        out.endElement(HTML.DIV_ELEM);

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        out.writeText("new Ajax.MyFacesAutocompleter('",null);
        out.writeText(clientId,null);
        out.writeText("','",null);
        if (inputSuggestAjax.getPopupId() != null)
        {
            out.writeText(inputSuggestAjax.getPopupId(), null);
        }
        else
        {
            out.writeText(clientId+"_auto_complete",null);
        }
        out.writeText("','",null);
        out.writeText(context.getExternalContext().encodeActionURL(actionURL+"?affectedAjaxComponent="+clientId),null);
        if (context.getApplication().getStateManager().isSavingStateInClient(context))            
        {
            out.writeText("', {\n" +
                          "      method:       'post',\n" +
                          "      asynchronous: true,\n" +
                          "      parameters: '',\n"+
                          "      callback: function(element,entry) {return entry+'&jsf_tree_64='+encodeURIComponent(document.getElementById('jsf_tree_64').value)+'&jsf_state_64='+encodeURIComponent(document.getElementById('jsf_state_64').value)+'&jsf_viewid='+encodeURIComponent(document.getElementById('jsf_viewid').value)}" +
                          "    })",null);            
        }
        else
        {                        
            out.writeText("', {\n" +
                          "      method:       'post',\n" +
                          "      asynchronous: true,\n" +
                          "      parameters: '',\n" +
                          "      callback: function(element,entry) {return entry}" +
                          "    })", null);
        }
        out.endElement(HTML.SCRIPT_ELEM);
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    public void encodeAjax(FacesContext context, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(context, uiComponent, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) uiComponent;

        MethodBinding mb = inputSuggestAjax.getSuggestedItemsMethod();
        Collection suggesteds = null;
        int maxSuggestedCount = inputSuggestAjax.getMaxSuggestedItems()!=null
        							? inputSuggestAjax.getMaxSuggestedItems().intValue()
        							: DEFAULT_MAX_SUGGESTED_ITEMS;
        if (inputSuggestAjax.getMaxSuggestedItems()!=null) {
        	try{
	        	suggesteds = (Collection) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent),
	                    new Integer(maxSuggestedCount)});
        	}catch(MethodNotFoundException dummy){
        		suggesteds = (List) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent)});
        	}
        } else {
        	try{
	        	suggesteds = (List) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent)});
        	}catch(MethodNotFoundException dummy){
        		suggesteds = (Collection) mb.invoke(context,new Object[]{
                        AjaxPhaseListener.getValueForComponent(context, uiComponent),
                        new Integer( DEFAULT_MAX_SUGGESTED_ITEMS )});
        	}
        }
        
        StringBuffer buf = new StringBuffer();
        buf.append("<ul");
        if (inputSuggestAjax.getListStyleClass() != null)
        {
            buf.append(" class='"+inputSuggestAjax.getListStyleClass()+"'");
        }
        if (inputSuggestAjax.getListStyle() != null)
        {
            buf.append(" style='"+inputSuggestAjax.getListStyle()+"'");
        }
        if (inputSuggestAjax.getListId() != null)
        {
            buf.append(" id='"+inputSuggestAjax.getListId()+"'");
        }
        buf.append(">");
        
        int suggestedCount=0;
        for (Iterator i = suggesteds.iterator() ; i.hasNext() ; suggestedCount++)
        {
        	if( suggestedCount > maxSuggestedCount )
        		break;
        	
            buf.append("<li");
            if (inputSuggestAjax.getListItemStyleClass() != null)
            {
                buf.append(" class='"+inputSuggestAjax.getListItemStyleClass()+"'");
            }
            if (inputSuggestAjax.getListItemStyle() != null)
            {
                buf.append(" style='"+inputSuggestAjax.getListItemStyle()+"'");
            }
            buf.append(">");
            buf.append(UnicodeEncoder.encode(i.next().toString()));
            buf.append("</li>");
        }
        buf.append("</ul>");

        context.getResponseWriter().write(buf.toString());
    }
}
