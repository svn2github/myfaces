/*
 * Created on Apr 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.apache.myfaces.custom.collapsiblemenu;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.html.util.DummyFormUtils;

/**
 * @author sharathreddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollapsibleMenuUtils {

    public static String getFormName(UIComponent component, FacesContext context) {
        //Find form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = parent.getParent();
        }

        if (parent != null) {
            //link is nested inside a form
            return ((UIForm) parent).getClientId(context);
        }
        //not nested in form, we must add a dummy form at the end of the
        // document
        return DummyFormUtils.getDummyFormName();
    }
    
    /**
     * Get the parent UIForm. If no parent is a UIForm then returns null.
     * 
     * @param component
     * @return UIForm
     */
    public static UIForm getParentForm(UIComponent component) {
        // See if we are in a form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

    
    
}
