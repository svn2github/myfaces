package org.apache.myfaces.custom.ajax.api;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Collection;

/**
 * @author Gerald Müllan
 *         Date: 01.04.2006
 *         Time: 16:05:52
 */
public interface AjaxSuggestRenderer
{
    public Collection getSuggestedItems(FacesContext context, UIComponent uiComponent);
}
