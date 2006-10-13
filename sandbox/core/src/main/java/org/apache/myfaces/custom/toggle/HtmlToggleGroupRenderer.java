/*
 * Copyright 2004-2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.toggle;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlGroupRendererBase;

public class HtmlToggleGroupRenderer extends HtmlGroupRendererBase {

    private static final String DISPLAY_NONE_STYLE = ";display:none;";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, HtmlToggleGroup.class);
        boolean editMode = checkEditMode(component.getChildren());
        toggleVisibility(component.getChildren(), editMode);
        super.encodeEnd(context, component);
    }

    private void toggleVisibility(List children, boolean editMode) {
        Iterator it = children.iterator();
        while (it.hasNext()) {
            UIComponent component = (UIComponent) it.next();

            if (editMode)
            // The toggle component is hidden in edit mode
            {
                if (component instanceof ToggleLink) {
                    hideComponent(component);
                }
                // all other components: show if in edit mode
                else {
                    showComponent(component);
                }
                continue;
            }

            if (!editMode)
            // The toggle component is displayed in 'view' mode
            {
                if (component instanceof ToggleLink) {
                    showComponent(component);
                }
                // all other components are hidden in 'view' mode
                else {
                    hideComponent(component);
                }
            }

        }
    }

    // checks if this component has getStyle/setStyle methods
    private boolean hasStyleAttribute(UIComponent component) {
        Method[] methods = component.getClass().getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals("getStyle")) {
                return true;
            }
        }
        return false;
    }

    // hides component by appending 'display:none' to the 'style' attribute
    private void hideComponent(UIComponent component) {

        System.out.println("hiding component is " + component.getClass());

        FacesContext context = FacesContext.getCurrentInstance();

        if (!hasStyleAttribute(component)) {
            Log log = getLog();
            log.info("style attribute expected, not found for component " + component.getClientId(context));
            return;
        }

        try {
            Class c = component.getClass();
            Method getStyle = c.getMethod("getStyle", new Class[] {});
            Method setStyle = c.getMethod("setStyle", new Class[] { String.class });

            String style = (String) getStyle.invoke(component, new Object[] {});
            if (style == null) {
                style = ";display:none;";
            } else if (style.indexOf(";display:none;") == -1) {
                style = style.concat(";display:none;");
            }
            setStyle.invoke(component, new Object[] { style });
        } catch (Throwable e) {
            Log log = getLog();
            log.error("unable to set style attribute on component " + component.getClientId(context));
        }

    }

    private Log getLog() {
        Log log = LogFactory.getLog(HtmlToggleGroupRenderer.class);
        return log;
    }

    // displays component by removing, if present, any 'display:none' style
    // attribute
    private void showComponent(UIComponent component) {

        FacesContext context = FacesContext.getCurrentInstance();

        if (!hasStyleAttribute(component)) {
            Log log = getLog();
            log.info("style attribute expected, not found for component " + component.getClientId(context));
            return;
        }

        try {
            Class c = component.getClass();
            Method getStyle = c.getMethod("getStyle", new Class[] {});
            Method setStyle = c.getMethod("setStyle", new Class[] { String.class });

            String style = (String) getStyle.invoke(component, new Object[] {});
            if (style == null || style.length() == 0) {
                return;
            } else {
                int index = style.indexOf(";display:none;");
                if (index == -1)
                    return;

                if (index == 0) {
                    style = null;
                } else {
                    style = style.substring(0, index);
                }
            }
            setStyle.invoke(component, new Object[] { style });
        } catch (Throwable e) {
            Log log = getLog();
            log.error("unable to set style attribute on component " + component.getClientId(context));
        }

    }

    // gets the edit mode from the child 'toggleLink' component
    private boolean checkEditMode(List children) {
        Iterator it = children.iterator();
        while (it.hasNext()) {
            UIComponent component = (UIComponent) it.next();
            if (component instanceof ToggleLink) {
                System.out.println("edit mode is " + ((ToggleLink) component).getEditMode());
                return ((ToggleLink) component).getEditMode();
            }
        }
        Log log = getLog();
        log.error("Could not find child ToggleLink component for HtmlToggleGroup");
        return false;

    }

}
