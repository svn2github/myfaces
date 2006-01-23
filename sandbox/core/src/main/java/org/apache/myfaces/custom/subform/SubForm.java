package org.apache.myfaces.custom.subform;

import javax.faces.component.UIComponentBase;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Iterator;

/**
 * @author Gerald Muellan
 *         Date: 19.01.2006
 *         Time: 13:58:18
 */
public class SubForm extends UIComponentBase
                     implements NamingContainer
{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.SubForm";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SubForm";
    public static final String COMPONENT_FAMILY = "javax.faces.Form";

    private static final String PARTIAL_ENABLED = "org.apache.myfaces.IsPartialPhaseExecutionEnabled";
    private static final String ACTION_FOR_LIST = "org.apache.myfaces.ActionForList";


    public SubForm()
    {
        super.setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void processValidators(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;

        boolean tearDownRequired = setupPartialInfo(context);

        for (Iterator it = getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent childOrFacet = (UIComponent)it.next();
            childOrFacet.processValidators(context);
        }

        if(tearDownRequired)
        {
            tearDownPartialInfo(context);
        }
    }

    /**Sets up information if this component is included in
     * the group of components which are associated with the current action.
     *
     * @param context
     * @return true if there has been a change by this setup which has to be undone after the phase finishes.
     */
    private boolean setupPartialInfo(FacesContext context)
    {
        //the following section is not in the spec
        //there is no place to put it into the actual implementation, though
        //we want to execute validation (and model update) only
        //if certain conditions are met
        //especially, we want to switch validation/update on/off depending on
        //the attribute "actionFor" of a MyFaces extended button or link
        //if you use commandButtons which don't set these
        //request parameters, this won't cause any adverse effects
        //except the additional performance hit of getting the request-parameter

        //is it necessary to remove the request-parameter again?
        boolean tearDownRequired = false;

        //get the list of (parent) client-ids for which a validation should be performed
        List li = (List) context.getExternalContext().getRequestMap().get(
                ACTION_FOR_LIST);

        //check if the list is existing at all - if not, we want to validate in any case.
        if(li==null || li.size()==0)
        {
            if(!context.getExternalContext().getRequestMap().containsKey(PARTIAL_ENABLED))
            {
                context.getExternalContext().getRequestMap().put(PARTIAL_ENABLED,Boolean.TRUE);
                tearDownRequired = true;
            }
        }
        //if there is a list, check if the current client id
        //matches an entry of the list
        else if(li.contains(getClientId(context)))
        {
            if(!context.getExternalContext().getRequestMap().containsKey(PARTIAL_ENABLED))
            {
                context.getExternalContext().getRequestMap().put(PARTIAL_ENABLED,Boolean.TRUE);
                tearDownRequired = true;
            }
        }

        return tearDownRequired;
    }

    /**
     * Remove the information about this component being included in the partial
     * phase execution.
     *
     * @param context
     */
    private void tearDownPartialInfo(FacesContext context)
    {
        context.getExternalContext().getRequestMap().remove(PARTIAL_ENABLED);
    }


}
