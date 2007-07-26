package org.apache.myfaces.renderkit.freemarker;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * @author Martin Marinschek
 */
public interface TemplateEncoder {
    public void encodeTemplate(FacesContext context, UIComponent component, String template) throws IOException;
}
