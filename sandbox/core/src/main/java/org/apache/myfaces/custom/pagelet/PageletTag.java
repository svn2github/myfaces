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
package org.apache.myfaces.custom.pagelet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.shared_tomahawk.component.DisplayValueOnlyCapable;
import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlInputTextareaTagBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;


/**
 * @author Thomas Spiegl
 *
 */
public class PageletTag extends HtmlInputTextareaTagBase {

    private static final String WIDTH_ATTR               = "width".intern();
    private static final String HEIGHT_ATTR              = "height".intern();
    private static final String TXT_SPELLCHK_ATTR        = "textSpellcheck".intern();
    private static final String TXT_RESUME_ATTR          = "textResume".intern();
    private static final String TXT_WORKING_ATTR         = "textWorking".intern();
    private static final String TXT_CHECKING_ATTR        = "textChecking".intern();
    private static final String TXT_NO_MISSPELLINGS_ATTR = "textNoMisspellings".intern();
    private static final String TXT_ZOOM_WIDTH_ATTR      = "zoomWidth".intern();
    private static final String TXT_ZOOM_HEIGHT_ATTR     = "zoomHeight".intern();
    private static final String TXT_CONTROL_MODE         = "controlMode".intern();

    private String _controlMode;
    private String _displayValueOnly;
    private String _displayValueOnlyStyle;
    private String _displayValueOnlyStyleClass;
    private String _height, _zoomHeight;

    private String _spellchecker;
    private String _textSpellcheck, _textResume, _textWorking, _textNoMisspellings, _textChecking;
    private String _width, _zoomWidth;

    public static void setMethodProperty(FacesContext context, UIComponent component, String method) {

        if (method != null) {

            if (!(component instanceof Pagelet)) {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no AjaxInputSpellcheck");
            }

            if (UIComponentTag.isValueReference(method)) {
                MethodBinding mb = context.getApplication().createMethodBinding(method, new Class[] { Text.class });
                ((Pagelet) component).setSpellchecker(mb);
            } else {
                Log log = LogFactory.getLog(PageletTag.class);
            	log.error("Invalid expression " + method);
            }
        }
    }

    public String getComponentType() {
        return Pagelet.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return Pagelet.DEFAULT_RENDERER_TYPE;
    }


    public void release() {
        super.release();
        _spellchecker               = null;
        _displayValueOnly           = null;
        _displayValueOnlyStyle      = null;
        _displayValueOnlyStyleClass = null;
        _controlMode				= null;
    }

    public void setControlMode(String controlMode) {
        this._controlMode = controlMode;
    }

    public void setDisplayValueOnly(String displayValueOnly) {
        _displayValueOnly = displayValueOnly;
    }

    public void setDisplayValueOnlyStyle(String displayValueOnlyStyle) {
        _displayValueOnlyStyle = displayValueOnlyStyle;
    }

    public void setDisplayValueOnlyStyleClass(String displayValueOnlyStyleClass) {
        _displayValueOnlyStyleClass = displayValueOnlyStyleClass;
    }

    public void setHeight(String height) {
        _height = height;
    }

    public void setSpellchecker(String spellchecker) {
        _spellchecker = spellchecker;
    }

    public void setTextChecking(String textChecking) {
        _textChecking = textChecking;
    }

    public void setTextNoMisspellings(String textNoMisspellings) {
        _textNoMisspellings = textNoMisspellings;
    }

    public void setTextResume(String textResume) {
        _textResume = textResume;
    }

    public void setTextSpellcheck(String textSpellcheck) {
        _textSpellcheck = textSpellcheck;
    }

    public void setTextWorking(String textWorking) {
        _textWorking = textWorking;
    }

    public void setWidth(String width) {
        _width = width;
    }

    public void setZoomHeight(String zoomHeight) {
        this._zoomHeight = zoomHeight;
    }

    public void setZoomWidth(String zoomWidth) {
        this._zoomWidth = zoomWidth;
    }

    protected void setProperties(UIComponent uiComponent) {
        super.setProperties(uiComponent);

        setIntegerProperty(uiComponent, WIDTH_ATTR, _width);
        setIntegerProperty(uiComponent, HEIGHT_ATTR, _height);
        setStringProperty(uiComponent, TXT_SPELLCHK_ATTR, _textSpellcheck);
        setStringProperty(uiComponent, TXT_RESUME_ATTR, _textResume);
        setStringProperty(uiComponent, TXT_WORKING_ATTR, _textWorking);
        setStringProperty(uiComponent, TXT_CHECKING_ATTR, _textChecking);
        setStringProperty(uiComponent, TXT_NO_MISSPELLINGS_ATTR, _textNoMisspellings);
        setIntegerProperty(uiComponent, TXT_ZOOM_WIDTH_ATTR, _zoomWidth);
        setIntegerProperty(uiComponent, TXT_ZOOM_HEIGHT_ATTR, _zoomHeight);
        setBooleanProperty(uiComponent, DisplayValueOnlyCapable.DISPLAY_VALUE_ONLY_ATTR, _displayValueOnly);
        setStringProperty(uiComponent, DisplayValueOnlyCapable.DISPLAY_VALUE_ONLY_STYLE_ATTR, _displayValueOnlyStyle);
        setStringProperty(uiComponent, DisplayValueOnlyCapable.DISPLAY_VALUE_ONLY_STYLE_CLASS_ATTR, _displayValueOnlyStyleClass);
        setStringProperty(uiComponent, TXT_CONTROL_MODE, _controlMode);
        setMethodProperty(getFacesContext(), uiComponent, _spellchecker);
    }
}
