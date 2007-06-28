package org.apache.myfaces.renderkit.freemarker;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class FreemarkerRenderer extends Renderer
{
    private static final Log log = LogFactory.getLog(FreemarkerRenderer.class);

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException
    {
        if (!component.isRendered()) return;
        encodeTemplate(context, component, getTemplateName(context, component) + "_begin.ftl");
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        if (!component.isRendered()) return;
        if (!getRendersChildren()) return;
        encodeTemplate(context, component, getTemplateName(context, component) + "_children.ftl");
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (!component.isRendered()) return;
        encodeTemplate(context, component, getTemplateName(context, component) + "_end.ftl");
    }

    protected void encodeTemplate(FacesContext context, UIComponent component, String template) throws IOException {
        Configuration cfg = new Configuration();
        log.info(getClass().getResource("templates/outputText_begin.ftl"));
        TemplateLoader templateLoader = new ClassTemplateLoader(getClass(), "template");
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
    
    protected abstract Object getDatamodel(FacesContext context, UIComponent component);
    protected abstract String getTemplateName(FacesContext context, UIComponent component);
    
    
}
