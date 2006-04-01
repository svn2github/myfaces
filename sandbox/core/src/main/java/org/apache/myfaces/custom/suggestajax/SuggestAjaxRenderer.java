package org.apache.myfaces.custom.suggestajax;

import org.apache.myfaces.custom.ajax.api.AjaxDecodePhaseListener;
import org.apache.myfaces.custom.ajax.api.AjaxSuggestRenderer;
import org.apache.myfaces.renderkit.html.ext.HtmlTextRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * @author Gerald Müllan
 * @author Martin Marinschek
 * @version $Revision: 177984 $ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class SuggestAjaxRenderer extends HtmlTextRenderer implements AjaxSuggestRenderer
{
    public static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

    public Collection getSuggestedItems(FacesContext context, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(context, uiComponent, SuggestAjax.class);

        SuggestAjax suggestAjax = (SuggestAjax) uiComponent;

        //getting the suggested items
        MethodBinding mb = suggestAjax.getSuggestedItemsMethod();
        Integer maxSuggestedCount = suggestAjax.getMaxSuggestedItems();

        Collection suggesteds = null;

        if (maxSuggestedCount != null
                && maxSuggestedCount.intValue() > 0)
        {
            try
            {
                suggesteds = (Collection) mb.invoke(context,new Object[]{
                        AjaxDecodePhaseListener.getValueForComponent(context, uiComponent),
                        maxSuggestedCount});
            }
            catch(MethodNotFoundException dummy)
            {
                suggesteds = (List) mb.invoke(context,new Object[]{
                        AjaxDecodePhaseListener.getValueForComponent(context, uiComponent)});
            }
        }
        else
        {
            try
            {
                suggesteds = (List) mb.invoke(context,new Object[]{
                        AjaxDecodePhaseListener.getValueForComponent(context, uiComponent)});
            }
            catch(MethodNotFoundException dummy)
            {
                suggesteds = (Collection) mb.invoke(context,new Object[]{
                        AjaxDecodePhaseListener.getValueForComponent(context, uiComponent),
                        new Integer( DEFAULT_MAX_SUGGESTED_ITEMS )});
            }
        }

        return suggesteds;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

}
