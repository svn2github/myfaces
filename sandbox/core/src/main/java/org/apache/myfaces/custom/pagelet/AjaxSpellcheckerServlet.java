package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Thomas Spiegl
 */
public class AjaxSpellcheckerServlet extends HttpServlet {

    private static final String LIFECYCLE_ID_ATTR    = "javax.faces.LIFECYCLE_ID";
    public static final String  AJAX_PATH            = "/org/apache/myfaces/ajax";
    private static final String SERVLET_INFO         = "FacesServlet of the MyFaces API implementation";
    private FacesContextFactory _facesContextFactory;
    private Lifecycle           _lifecycle;
    private ServletConfig       _servletConfig;

    public static String getServletPath(ServletRequest request, String path) {
        String context = ((HttpServletRequest) request).getContextPath();

        return (context != null) ? (context + AjaxSpellcheckerServlet.AJAX_PATH + "/" + path) : (AjaxSpellcheckerServlet.AJAX_PATH + "/" + path);
    }

    public void destroy() {
        _servletConfig       = null;
        _facesContextFactory = null;
        _lifecycle           = null;
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public String getServletInfo() {
        return SERVLET_INFO;
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        _servletConfig       = servletConfig;
        _facesContextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        _lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        FacesContext          facesContext = _facesContextFactory.getFacesContext(_servletConfig.getServletContext(), servletRequest, servletResponse, _lifecycle);
        AjaxSpellCheckHandler handler      = new AjaxSpellCheckHandler();
        handler.handleAjaxRequest(facesContext);
    }

    private String getLifecycleId() {
        String lifecycleId = _servletConfig.getServletContext().getInitParameter(LIFECYCLE_ID_ATTR);

        return (lifecycleId != null) ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }
}
