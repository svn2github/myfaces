package org.apache.myfaces.custom.inputTextHelp;

import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlTextRenderer;
import org.apache.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Thomas Obereder
 * @version Date: 09.06.2005, 22:50:48
 */
public class HtmlTextHelpRenderer extends HtmlTextRenderer
{
    protected void renderNormal(FacesContext facesContext, UIComponent component) throws IOException
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            addJavaScriptResources(facesContext);
            renderInputTextHelp(facesContext, (UIInput)helpTextComp);
        }
        else
        {
            super.renderNormal(facesContext, component);
        }
    }

    public static boolean isSelectText(UIComponent component)
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            return helpTextComp.isSelectText();
        }
        return false;
    }

    public static String getHelpText(UIComponent component)
    {
        if(component instanceof HtmlInputTextHelp)
        {
            HtmlInputTextHelp helpTextComp = (HtmlInputTextHelp) component;
            if(helpTextComp.getHelpText() != null)
                return helpTextComp.getHelpText();
        }
        return null;
    }

    public void renderInputTextHelp(FacesContext facesContext, UIInput input)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, input);

        writer.writeAttribute(HTML.ID_ATTR, input.getClientId(facesContext), null);
        writer.writeAttribute(HTML.NAME_ATTR, input.getClientId(facesContext), null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);

        renderHelpTextAttributes(input, writer, facesContext);

        String value = RendererUtils.getStringValue(facesContext, input);
        value = (value.equals("") || value == null) ? getHelpText(input) : value;

        writer.writeAttribute(HTML.VALUE_ATTR, HTMLEncoder.encode(value,true,true), null);

        writer.endElement(HTML.INPUT_ELEM);
    }

    private void renderHelpTextAttributes(UIComponent component,
                                                ResponseWriter writer,
                                                FacesContext facesContext)
            throws IOException
    {
        if(!(component instanceof HtmlInputTextHelp))
        {
            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        }
        else
        {
            String id = component.getClientId(facesContext);
            if(isSelectText(component))
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED_AND_ONFOCUS_AND_ONCLICK);
                writer.writeAttribute(HTML.ONFOCUS_ATTR,
                        HtmlInputTextHelp.JS_FUNCTION_SELECT_TEXT + "('" +
                            getHelpText(component) + "', '" + id +"')", null);
                writer.writeAttribute(HTML.ONCLICK_ATTR,
                        HtmlInputTextHelp.JS_FUNCTION_SELECT_TEXT + "('" +
                            getHelpText(component) + "', '" + id +"')", null);

            }
            else
            {
                if(getHelpText(component) != null)
                {
                    HtmlRendererUtils.renderHTMLAttributes(writer, component,
                            HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED_AND_ONFOCUS_AND_ONCLICK);
                    writer.writeAttribute(HTML.ONFOCUS_ATTR,
                            HtmlInputTextHelp.JS_FUNCTION_RESET_HELP + "('" +
                            getHelpText(component) + "', '" + id +"')", null);
                writer.writeAttribute(HTML.ONCLICK_ATTR,
                        HtmlInputTextHelp.JS_FUNCTION_RESET_HELP + "('" +
                            getHelpText(component) + "', '" + id +"')", null);
                }
                else
                {
                    HtmlRendererUtils.renderHTMLAttributes(writer,
                            component, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
                }
            }
        }

         if (isDisabled(facesContext, component))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue) throws ConverterException
    {
        if(submittedValue!=null && component instanceof HtmlInputTextHelp &&
                ((HtmlInputTextHelp) component).getHelpText()!=null &&
            submittedValue.equals(((HtmlInputTextHelp) component).getHelpText()))
        {
            submittedValue = "";
        }

        return super.getConvertedValue(facesContext, component, submittedValue);
    }

    private static void addJavaScriptResources(FacesContext facesContext)
    {
        AddResource.addJavaScriptToHeader(HtmlTextHelpRenderer.class,
                                            "inputTextHelp.js",
                                            facesContext);
    }
}
