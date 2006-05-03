/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
