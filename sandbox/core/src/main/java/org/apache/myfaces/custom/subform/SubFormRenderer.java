package org.apache.myfaces.custom.subform;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * @author Gerald Muellan
 *         Date: 19.01.2006
 *         Time: 14:01:35
 */
public class SubFormRenderer extends HtmlRenderer
{
    private static final String SUBMIT_FUNCTION_SUFFIX = "_submit";    
    private static final String HIDDEN_PARAM_NAME      = "org.apache.myfaces.custom.subform.submittedId";
     
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
                
        ResponseWriter writer = context.getResponseWriter();
        
        HtmlRendererUtils.writePrettyLineSeparator(context);
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.SCRIPT_TYPE_ATTR,org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
                
        UIForm parentForm = findParentForm(component);
        writer.writeText(createPartialSubmitJS(component.getId(), parentForm.getClientId(context)), null);
        
        writer.endElement(org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.SCRIPT_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(context);   
    }

    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        
        Map paramValuesMap = context.getExternalContext().getRequestParameterMap();
        String reqValue = (String)paramValuesMap.get(HIDDEN_PARAM_NAME);
        if (reqValue != null && component.getId().equals(reqValue)) {                 
            ((SubForm)component).setSubmitted(true);
        }
    }       
    /**
     *
     */    
    protected String createPartialSubmitJS(String subFormId, String parentFormClientId) {
        StringBuffer script = new StringBuffer();
        script.append("function ");
        script.append(subFormId+SUBMIT_FUNCTION_SUFFIX+"()");        
        script.append(" {\n");        
        script.append("var form = document.forms['"+parentFormClientId+"'];\n");   
        script.append("var el = document.createElement(\"input\");\n");
        script.append("el.type = \"hidden\";\n");
        script.append("el.name = \""+HIDDEN_PARAM_NAME+"\";\n");
        script.append("el.value = \""+subFormId+"\";\n");
        script.append("form.appendChild(el);\n");        
        script.append("form.submit();\n");        
        script.append("}\n");        
        
        return script.toString();
    }
    /**
     * 
     */
    protected UIForm findParentForm(UIComponent component) {
        UIComponent parentComponent = component.getParent();
        while (!(parentComponent instanceof UIForm)) {
            parentComponent = parentComponent.getParent();
        }
        return (UIForm)parentComponent;
    }
}
