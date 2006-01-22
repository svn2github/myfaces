package org.apache.myfaces.custom.subform;

import javax.faces.component.UIComponentBase;
import javax.faces.component.NamingContainer;

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
    public static final String COMPONENT_FAMILY = "org.apache.myfaces.SubForm";

    public SubForm()
    {
        super.setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }


}
