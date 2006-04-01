package org.apache.myfaces.custom.suggestajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.suggestajax.inputsuggestajax.InputSuggestAjax;
import org.apache.myfaces.taglib.html.ext.HtmlInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

/**
 * @author Gerald Müllan
 *         Date: 25.03.2006
 *         Time: 17:05:58
 */
public class SuggestAjaxTag extends HtmlInputTextTag
{
    private final static Class[] DEFAULT_SIGNATURE = new Class[]{String.class};
    private final static Class[] SUGGEST_ITEM_SIGNATURE = new Class[]{String.class, Integer.class};

    private static Log log = LogFactory.getLog(SuggestAjaxTag.class);

    private String _suggestedItemsMethod;
    private String _maxSuggestedItems;

    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;

    private String _layout;

    public String getComponentType() {
        return InputSuggestAjax.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return InputSuggestAjax.DEFAULT_RENDERER_TYPE;
    }

    public void release() {

        super.release();

       _suggestedItemsMethod = null;
       _maxSuggestedItems = null;
       _popupId = null;
       _popupStyleClass = null;
       _popupStyle = null;
       _layout = null;
    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        setIntegerProperty(component,"maxSuggestedItems", _maxSuggestedItems);

        SuggestAjaxTag.setSuggestedItemsMethodProperty(getFacesContext(),component,_suggestedItemsMethod);
        setStringProperty(component,"popupId",_popupId);
        setStringProperty(component,"popupStyleClass",_popupStyleClass);
        setStringProperty(component,"popupStyle",_popupStyle);
        setStringProperty(component,"layout",_layout);
    }

    public static void setSuggestedItemsMethodProperty(FacesContext context,
                                                       UIComponent component,
                                                       String suggestedItemsMethod)
    {
        if (suggestedItemsMethod != null)
        {
            if (!(component instanceof SuggestAjax))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no InputSuggestAjax");
            }
            if (isValueReference(suggestedItemsMethod))
            {
                if (((SuggestAjax)component).getMaxSuggestedItems()!=null) {
                    MethodBinding mb = context.getApplication().createMethodBinding(suggestedItemsMethod, SuggestAjaxTag.SUGGEST_ITEM_SIGNATURE);
                    ((SuggestAjax)component).setSuggestedItemsMethod(mb);
                } else {
                    MethodBinding mb = context.getApplication().createMethodBinding(suggestedItemsMethod, SuggestAjaxTag.DEFAULT_SIGNATURE);
                    ((SuggestAjax)component).setSuggestedItemsMethod(mb);
                }
            }
            else
            {
                SuggestAjaxTag.log.error("Invalid expression " + suggestedItemsMethod);
            }
        }
    }

    // setter methodes to populate the components properites
    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public void setSuggestedItemsMethod(String suggestedItemsMethod)
    {
        _suggestedItemsMethod = suggestedItemsMethod;
    }

    public void setPopupId(String popupId)
    {
        _popupId = popupId;
    }

    public void setPopupStyleClass(String popupStyleClass)
    {
        _popupStyleClass = popupStyleClass;
    }

    public void setPopupStyle(String popupStyle)
    {
        _popupStyle = popupStyle;
    }

    public void setMaxSuggestedItems(String maxSuggestedItems) {
        _maxSuggestedItems = (maxSuggestedItems);
    }

}
