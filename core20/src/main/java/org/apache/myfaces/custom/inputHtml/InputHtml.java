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
package org.apache.myfaces.custom.inputHtml;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

/**
 * HTML Editor using the kupu library.
 * http://kupu.oscom.org/
 *
 * An inline HTML based word processor based on the Kupu library. 
 * 
 * See http://kupu.oscom.org 
 * 
 * Right now, the support is limited to one editor per page 
 * (but you can use tabs to have multiple editors, but only 
 * one rendered at a time). 
 * 
 * Unless otherwise specified, all attributes accept static values or EL expressions.
 *
 * @JSFComponent
 *   name = "t:inputHtml"
 *   tagClass = "org.apache.myfaces.custom.inputHtml.InputHtmlTag"
 *
 * @author Sylvain Vieujot (latest modification by $Author: skitching $)
 * @version $Revision: 673833 $ $Date: 2008-07-03 16:58:05 -0500 (jue, 03 jul 2008) $
 */
public class InputHtml extends HtmlInputText {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.InputHtml";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.InputHtml";

    private static final Log log = LogFactory.getLog(HtmlInputText.class);

    public InputHtml() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * Use a text area instead of the javascript HTML editor. 
     * 
     * Default is false. Use with caution.
     * 
     * @JSFProperty
     */
    public String getFallback(){
        return (String) getStateHelper().eval(PropertyKeys.fallback, "false");
    }
    public void setFallback(String _fallback){
        getStateHelper().put(PropertyKeys.fallback, _fallback);
    }

    /**
     * The type of the value. It can be either fragment for an HTML 
     * fragment (default) or document for a full HTML document, with 
     * head, title, body, ... tags.
     * 
     * @JSFProperty
     */
    public String getType(){
        return (String) getStateHelper().eval(PropertyKeys.type, "fragment");
    }
    public void setType(String _type){
        getStateHelper().put(PropertyKeys.type, _type);
    }
    public boolean isTypeDocument(){
        return getType().equals("document");
    }

    /**
     * Allows the user to edit the HTML source code. Default is true.
     * 
     * @JSFProperty
     */
    public boolean isAllowEditSource(){
        return (Boolean) getStateHelper().eval(PropertyKeys.allowEditSource, Boolean.TRUE);
    }
    public void setAllowEditSource(boolean allowEditSource){
        getStateHelper().put(PropertyKeys.allowEditSource, allowEditSource);
    }

    /**
     * Allows the user to insert external links. Default is true.
     * 
     * @JSFProperty
     */
    public boolean isAllowExternalLinks(){
        return (Boolean) getStateHelper().eval(PropertyKeys.allowExternalLinks, Boolean.TRUE);
    }
    public void setAllowExternalLinks(boolean allowExternalLinks){
        getStateHelper().put(PropertyKeys.allowExternalLinks, allowExternalLinks);
    }

    /**
     * Show the Kupu Logo in the buttons bar. Default is true.
     * 
     * @JSFProperty
     */
    public boolean isAddKupuLogo(){
        return (Boolean) getStateHelper().eval(PropertyKeys.addKupuLogo, Boolean.TRUE);
    }
    public void setAddKupuLogo(boolean addKupuLogo){
        getStateHelper().put(PropertyKeys.addKupuLogo, addKupuLogo);
    }

    /**
     * Shortcut to avoid setting all the showXXToolBox to true. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowAllToolBoxes(){
        return (Boolean) getStateHelper().eval(PropertyKeys.showAllToolBoxes, Boolean.FALSE);
    }
    public void setShowAllToolBoxes(boolean showAllToolBoxes){
        getStateHelper().put(PropertyKeys.showAllToolBoxes, showAllToolBoxes);
    }

    /**
     * Show the Properties tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowPropertiesToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showPropertiesToolBox, Boolean.FALSE);
    }

    public void setShowPropertiesToolBox(boolean showPropertiesToolBox){
        getStateHelper().put(PropertyKeys.showPropertiesToolBox, showPropertiesToolBox);
    }

    /**
     * Show the Links tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowLinksToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showLinksToolBox, Boolean.FALSE);
    }
    
    public void setShowLinksToolBox(boolean showLinksToolBox){
        getStateHelper().put(PropertyKeys.showLinksToolBox, showLinksToolBox);
    }

    /**
     * Show the Images tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowImagesToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showImagesToolBox, Boolean.FALSE);
    }
    public void setShowImagesToolBox(boolean showImagesToolBox){
        getStateHelper().put(PropertyKeys.showImagesToolBox, showImagesToolBox);
    }

    /**
     * Show the Tables tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowTablesToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showTablesToolBox, Boolean.FALSE);
    }
    public void setShowTablesToolBox(boolean showTablesToolBox){
        getStateHelper().put(PropertyKeys.showTablesToolBox, showTablesToolBox);
    }

    /**
     * Show the Cleanup Expressions tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowCleanupExpressionsToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showCleanupExpressionsToolBox, Boolean.FALSE);
    }
    public void setShowCleanupExpressionsToolBox(boolean showCleanupExpressionsToolBox){
        getStateHelper().put(PropertyKeys.showCleanupExpressionsToolBox, showCleanupExpressionsToolBox);
    }

    /**
     * Show the Debug tool box next to the text. Default is false.
     * 
     * @JSFProperty
     */
    public boolean isShowDebugToolBox(){
        if( isShowAllToolBoxes() )
            return true;

        return (Boolean) getStateHelper().eval(PropertyKeys.showDebugToolBox, Boolean.FALSE);
    }
    public void setShowDebugToolBox(boolean showTablesToolBox){        
        getStateHelper().put(PropertyKeys.showDebugToolBox, showTablesToolBox);
    }

    public boolean isShowAnyToolBox(){
           return isShowAllToolBoxes()
               || isShowPropertiesToolBox()
               || isShowLinksToolBox()
               || isShowImagesToolBox()
               || isShowTablesToolBox()
               || isShowCleanupExpressionsToolBox()
               || isShowDebugToolBox();
    }

    public String getValueAsHtmlDocument(FacesContext context){
        String val = RendererUtils.getStringValue(context, this);
        if( isHtmlDocument( val ) )
            return val;

        return "<html><body>"+(val==null ? "" : val)+"</body></html>";
    }

    private static boolean isHtmlDocument(String text){
        if( text == null )
            return false;

        if( text.indexOf("<body>")!=-1 || text.indexOf("<body ")!=-1
            || text.indexOf("<BODY>")!=-1 || text.indexOf("<BODY ")!=-1 )
            return true;

        return false;
    }

    public String getValueFromDocument(String text){
        if( text == null )
            return "";

        if( isTypeDocument() )
            return text.trim();

        if( !isHtmlDocument(text) )
            return text.trim();

        // Extract the fragment from the document.
        String fragment = getHtmlBody( text );
        if( fragment.endsWith("<br />") )
            return fragment.substring(0, fragment.length()-6);
        return fragment;
    }
    
    String getHtmlBody(String html){
        html = html.trim();
        if( html.length() == 0 )
            return "";

        String lcText = html.toLowerCase();
        int textLength = lcText.length();
        int bodyStartIndex = -1;
        while(bodyStartIndex < textLength){
            bodyStartIndex++;
            bodyStartIndex = lcText.indexOf("<body", bodyStartIndex);
            if( bodyStartIndex == -1 )
                break; // not found.

            bodyStartIndex += 5;
            char c = lcText.charAt(bodyStartIndex);
            if( c=='>' )
                break;

            if( c!=' ' && c!='\t' )
                continue;

            bodyStartIndex = lcText.indexOf('>', bodyStartIndex);
            break;
        }
        bodyStartIndex++;

        int bodyEndIndex = lcText.lastIndexOf("</body>")-1;
        
        if( bodyStartIndex<0 || bodyEndIndex<0
           || bodyStartIndex > bodyEndIndex
           || bodyStartIndex>=textLength || bodyEndIndex>=textLength ){

            if( lcText.indexOf("<body/>")!=-1 || lcText.indexOf("<body />")!=-1 )
                return "";
            
            int htmlStartIndex = lcText.indexOf("<html>");
            int htmlEndIndex = lcText.indexOf("</html>");
            if( htmlStartIndex != -1 && htmlEndIndex > htmlStartIndex )
                return html.substring(htmlStartIndex+6, htmlEndIndex);
            
            if( isTypeDocument() )
                log.warn("Couldn't extract HTML body from :\n"+html);
            return html.trim();
        }

        return html.substring(bodyStartIndex, bodyEndIndex+1).trim();
    }
    
    protected enum PropertyKeys
    {
        fallback,
        type,
        allowEditSource,
        allowExternalLinks,
        addKupuLogo,
        showAllToolBoxes,
        showPropertiesToolBox,
        showLinksToolBox,
        showImagesToolBox,
        showTablesToolBox,
        showCleanupExpressionsToolBox,
        showDebugToolBox,
        
    }
}
