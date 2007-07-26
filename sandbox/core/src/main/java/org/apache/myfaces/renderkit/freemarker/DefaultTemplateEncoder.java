package org.apache.myfaces.renderkit.freemarker;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.security.auth.login.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Martin Marinschek
 */
public class DefaultTemplateEncoder implements TemplateEncoder {

    private static final Log log = LogFactory.getLog(DefaultTemplateEncoder.class);

    public void encodeTemplate(FacesContext context, UIComponent component, String template) throws IOException {    
            Configuration cfg = new Configuration();
        log.info("Encoding template : " + getClass().getResource(template));
        TemplateLoader templateLoader = new ClassTemplateLoader(getClass(), template);
        cfg.setTemplateLoader(templateLoader);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        Template temp = cfg.getTemplate(template);
        try
        {
            temp.process(getDatamodel(context, component), context.getResponseWriter());
        }
        catch (TemplateException e)
        {
            throw new IOException(e.getMessage());
        }
    }
}
