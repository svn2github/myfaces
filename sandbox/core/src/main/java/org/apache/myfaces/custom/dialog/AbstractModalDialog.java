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

package org.apache.myfaces.custom.dialog;

import javax.faces.component.UIPanel;

import org.apache.myfaces.custom.dojo.DojoWidget;

/**
 * Embeds into the current page a javascript object with methods which can be called to
 * display (and hide) a modal popup window. 
 * <p>
 * When the popup window is displayed, the current window contents become "greyed out" and
 * the new window appears on top of the original. The original window does not respond to 
 * keys or clicks; only the new window can be accessed by the user. When the popup window
 * is closed then the original window is again accessable.
 * </p>
 * <p>
 * When this component has a child facet named "titleBar" then the contents of that facet
 * are rendered at the top of the popup window. This facet is intended to allow users to
 * define their own custom window "decoration".
 * </p>
 * <p>
 * When this component has no "titleBar" facet, but does have a "dialogTitle" property,
 * then a default window decoration is generated. It consists of a table row with two
 * cells. The left cell contains the dialogTitle text. If property renderCloseButton is
 * true, then the right cell holds a "close" icon. Styles are defined for the row and
 * cells so that the look-and-feel can be customised.
 * </p>
 * <p>
 * The new window can optionally load a page from the server when it is displayed. If
 * one of viewId or contentURL is defined, then an internal frame is inserted after the
 * titleBar, and the specified contents is immediately loaded into the popup window when
 * it is displayed.
 * </p>
 * <p>
 * The rest of the child components (ie other than titleBar) are displayed after the
 * titlebar (and after the contents of viewId or contentURL if it is defined).
 * </p>
 * <p>
 * This component internally uses the Dojo modal window widget.
 * </p>
 * 
 * @JSFComponent
 *   name = "s:modalDialog"
 *   class = "org.apache.myfaces.custom.dialog.ModalDialog"
 *   tagClass = "org.apache.myfaces.custom.dialog.ModalDialogTag"
 *   
 */
public abstract class AbstractModalDialog extends UIPanel implements DojoWidget {

    public static final String COMPONENT_TYPE = "org.apache.myfaces.ModalDialog";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.ModalDialog";
    
    /**
     * A space separated list with attribute='value' pairs, which control the behaviour of the dojo dialog.
     * 
     * @JSFProperty
     */
    public abstract String getDialogAttr();

    /**
     * An optional raw id to assign to the html div that encloses the modal dialog.
     * <p>
     * This id can be useful for controlling the dialog with javascript. However in most
     * cases it is not necessary.
     * </p>
     * <p>
     * If this is property is not defined then an id will be automatically generated.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract String getDialogId();

    /**
     * Specifies the name of the javascript variable which provides access to the
     * dialog functionality.
     * <p>
     * For example, if this is "myDialog" then a javascript object with name
     * "myDialog" will be defined. This exposes the following methods:
     * </p>
     * <ul>
     * <li>myDialog.show() will display the modal dialog window</li>
     * <li>myDialog.hide() will hide it (though this is not normally needed).</li> 
     * </ul>
     * 
     * @JSFProperty
     */
    public abstract String getDialogVar();
    
    public abstract void setDialogVar(String dialogVar);

    /**
     * A list of ids of components which, when activated, should cause the popup
     * dialog to be hidden.
     * <p>
     * If the standard title-bar is displayed (ie no custom titleBar facet exists,
     * and property dialogTitle is defined) then the standard close-button in that
     * bar is automatically included.
     * </p>
     * <p>
     * If the child components of this component (which will appear in the popup)
     * include other items that should cause the window to close (ie a button) then
     * their ids should be defined via this property. This component will then
     * automatically wire them up to the necessary functions to cause the popup
     * to be closed when they are activated.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract String getHiderIds();

    /**
     * The URL of the view to show within the content area of the dialog.
     * <p>
     * Optional; when not defined (or defined as an EL expression, but that expression returns null)
     * then the popup dialog will be opened with no content. Presumably custom javascript in the
     * calling page will arrange to populate the window content appropriately.
     * </p>
     * <p>
     * The modalDialog component treats this as a <i>url</i> relative to the webapp base. The value
     * should not start with a slash.
     * </p>
     * <p> 
     * Note that technically this value is not a viewId. A viewId is the internal path to the
     * page definition (eg "foo.jsp" or "foo.xhtml"). What the browser accesses is a URL (which
     * contains the path that triggers the facelets servlet, eg "foo.faces" or "foo.jsf" or
     * "/faces/foo".
     * </p>
     * The valueIt should not start with a slash.
     * 
     * @JSFProperty
     */
    public abstract String getViewId();

    /**
     * The URL to show within the content area of the dialog.
     * <p>
     * This may be:
     * </p>
     * <ul>
     * <li>an absolute url ("http://..")</li>
     * <li>a url relative to the current webapp</li>
     * <li>a url relative to the current page</li>
     * </ul>
     * 
     * @JSFProperty
     */
    public abstract String getContentURL();

    //@Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * HTML: CSS styling instructions.
     * 
     * @JSFProperty
     */
    public abstract String getStyle();

    /**
     * The CSS class for this element. Corresponds to the HTML 'class' attribute.
     * <p>
     * This value is also used as a base for defining style classes for parts of the
     * standard window "title bar" decoration.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract String getStyleClass();

    /**
     * Optional enforced dojo widgetId
     *  
     * @JSFProperty
     */
    public abstract String getWidgetId();

    /**
     * The title text to show in the title area of the popup window 
     * (ie the "window decoration").
     * <p>
     * Ignored if there is a "titleBar" facet as a child.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract String getDialogTitle();

    /**
     * Specifies whether there should be a "close" icon to the right of the
     * popup window title.
     * <p>
     * Ignored if there is a "titleBar" facet as a child, or dialogTitle is
     * not defined. 
     * </p>
     * <p>
     * Defaults to true.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract Boolean getCloseButton();

    /**
     * An alias for the "dialogVar" property.
     *
     * @deprecated use getDialogVar instead.
     * 
     * @JSFProperty
     */
    public String getWidgetVar() {
        return getDialogVar();
    }

    public void setWidgetVar(String widgetVar) {
       setDialogVar(widgetVar);
    }
}
