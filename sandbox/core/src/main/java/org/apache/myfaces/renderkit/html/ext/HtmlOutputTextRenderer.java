package org.apache.myfaces.renderkit.html.ext;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.freemarker.FreemarkerRenderer;

public class HtmlOutputTextRenderer extends FreemarkerRenderer
{

    protected Object getDatamodel(FacesContext context, UIComponent component)
    {
        UIOutput outputText = (UIOutput)component;
        Map datamodel = new HashMap();
        datamodel.put("text", outputText.getValue());
        return datamodel;
    }

    protected String getTemplateName(FacesContext context,
            UIComponent component)
    {
        return "outputText";
    }

    /**
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        return true;
    }

}
