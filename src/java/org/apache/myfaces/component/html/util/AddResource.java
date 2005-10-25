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
package org.apache.myfaces.component.html.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HtmlResponseWriterImpl;

/**
 * This is a utility class to render link to resources used by custom components.
 * Mostly used to avoid having to include <script src="..."></script>
 * in the head of the pages before using a component.
 *
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class AddResource
{
    private static final String PATH_SEPARATOR = "/";

    protected static final Log log = LogFactory.getLog(AddResource.class);

    private static final String RESOURCE_VIRTUAL_PATH = "/faces/myFacesExtensionResource";

    private static StringBuffer ADDITIONAL_JAVASCRIPT_TO_BODY_TAG = null;

    private static final String HEADER_INFO_REQUEST_ATTRUBITE_NAME = AddResource.class.getName()
            + ".HEADER_INFO";

    private static final String RESOURCES_CACHE_KEY = AddResource.class.getName() + ".CACHE_KEY";

    protected final String _contextPath;

    private AddResource(String contextPath)
    {
        _contextPath = contextPath;
    }

    private static final Map _addResourceMap = new HashMap();

    private static AddResource getInstance(String contextPath)
    {
        AddResource instance = (AddResource) _addResourceMap.get(contextPath);
        if (instance == null)
        {
            synchronized (_addResourceMap)
            {
                instance = (AddResource) _addResourceMap.get(contextPath);
                if (instance == null)
                {
                    instance = new AddResource(contextPath);
                    _addResourceMap.put(contextPath, instance);
                }
            }
        }
        return instance;
    }

    public static AddResource getInstance(FacesContext context)
    {
        return getInstance(context.getExternalContext().getRequestContextPath());
    }

    public static AddResource getInstance(HttpServletRequest request)
    {
        return getInstance(request.getContextPath());
    }

    // Methodes to Add resources

    public void addJavaScriptHere(FacesContext context, Class myfacesCustomComponent,
            String resourceName) throws IOException
    {
        addJavaScriptHere(context, new MyFacesResourceHandler(myfacesCustomComponent, resourceName));
    }

    public void addJavaScriptHere(FacesContext context, String uri) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        String src = context.getExternalContext().encodeResourceURL(getResourceUri(context, uri));
        writer.writeURIAttribute(HTML.SRC_ATTR, src, null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    public void addJavaScriptHere(FacesContext context, ResourceHandler resourceHandler)
            throws IOException
    {
        validateResourceHandler(resourceHandler);

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        String src = context.getExternalContext().encodeResourceURL(
                getResourceUri(context, resourceHandler));
        writer.writeURIAttribute(HTML.SRC_ATTR, src, null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    public void addResourceHere(FacesContext context, ResourceHandler resourceHandler)
            throws IOException
    {
        validateResourceHandler(resourceHandler);

        String path = getResourceUri(context, resourceHandler);
        ResponseWriter writer = context.getResponseWriter();
        writer.write(context.getExternalContext().encodeResourceURL(path));
    }

    /**
     * @param resourceHandler
     */
    protected void validateResourceHandler(ResourceHandler resourceHandler)
    {
        if (resourceHandler == null)
        {
            throw new IllegalArgumentException("ResourceHandler is null");
        }
        validateResourceLoader(resourceHandler.getResourceLoaderClass());
    }

    /**
     * @param resourceloader
     */
    protected void validateResourceLoader(Class resourceloader)
    {
        if (!ResourceLoader.class.isAssignableFrom(resourceloader))
        {
            throw new FacesException("Class " + resourceloader.getName() + " must implement "
                    + ResourceLoader.class.getName());
        }
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, ResourceHandler resourceHandler)
    {
        addJavaScriptToHeader(context, resourceHandler, false);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, Class myfacesCustomComponent,
            String resourceName)
    {
        addJavaScriptToHeader(context, new MyFacesResourceHandler(myfacesCustomComponent,
                resourceName));
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, Class myfacesCustomComponent,
            String resourceName, boolean defer)
    {
        addJavaScriptToHeader(context, new MyFacesResourceHandler(myfacesCustomComponent,
                resourceName), defer);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, String uri)
    {
        addJavaScriptToHeader(context, uri, false);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, String uri, boolean defer)
    {
        addHeaderInfo(context, getScriptInstance(context, uri, defer));
    }

    public static void addJavaScriptToBodyTag(String JavaScriptEventName, String addedJavaScript)
    {
        if (ADDITIONAL_JAVASCRIPT_TO_BODY_TAG == null)
        {
            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG = new StringBuffer();

            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG.append(" ").append(JavaScriptEventName).append("=\"").append(addedJavaScript);
        }
        else
        {
            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG.append(addedJavaScript);
        }
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptToHeader(FacesContext context, ResourceHandler resourceHandler,
            boolean defer)
    {
        validateResourceHandler(resourceHandler);
        addHeaderInfo(context, getScriptInstance(context, resourceHandler, defer));
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, Class myfacesCustomComponent,
            String resourceName)
    {
        addStyleSheet(context, new MyFacesResourceHandler(myfacesCustomComponent, resourceName));
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, String uri)
    {
        addHeaderInfo(context, getStyleInstance(context, uri));
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, ResourceHandler resourceHandler)
    {
        validateResourceHandler(resourceHandler);
        addHeaderInfo(context, getStyleInstance(context, resourceHandler));
    }

    /**
     * Adds the given Inline Style to the document Header.
     */
    public void addInlineStyleToHeader(FacesContext context, String inlineStyle)
    {
        addHeaderInfo(context, getInlineStyleInstance(inlineStyle));
    }

    /**
     * Adds the given Inline Script to the document Header.
     */
    public void addInlineScriptToHeader(FacesContext context, String inlineScript)
    {
        addHeaderInfo(context, getInlineScriptInstance(inlineScript));
    }

    public String getResourceUri(FacesContext context, Class myfacesCustomComponent, String resource, boolean withContextPath)
    {
        return getResourceUri(context, new MyFacesResourceHandler(myfacesCustomComponent, resource), withContextPath);
    }

    public String getResourceUri(FacesContext context, Class myfacesCustomComponent, String resource)
    {
        return getResourceUri(context, new MyFacesResourceHandler(myfacesCustomComponent, resource));
    }

    /**
     * Get the Path used to retrieve an resource.
     */
    public String getResourceUri(FacesContext context, ResourceHandler resourceHandler)
    {
        String uri = resourceHandler.getResourceUri(context);
        if (uri == null)
        {
            return getResourceUri(context, resourceHandler.getResourceLoaderClass(), true);
        }
        return getResourceUri(context, resourceHandler.getResourceLoaderClass(), true) + uri;
    }
    /**
     * Get the Path used to retrieve an resource.
     */
    public String getResourceUri(FacesContext context, ResourceHandler resourceHandler, boolean withContextPath)
    {
        String uri = resourceHandler.getResourceUri(context);
        if (uri == null)
        {
            return getResourceUri(context, resourceHandler.getResourceLoaderClass(), withContextPath);
        }
        return getResourceUri(context, resourceHandler.getResourceLoaderClass(), withContextPath) + uri;
    }

    /**
     * Get the Path used to retrieve an resource.
     */
    public String getResourceUri(FacesContext context, String uri)
    {
        return getResourceUri(context, uri, true);
    }

    /**
     * Get the Path used to retrieve an resource.
     */
    public String getResourceUri(FacesContext context, String uri, boolean withContextPath)
    {
        if(withContextPath)
        {
            return context.getApplication().getViewHandler().getResourceURL(context, uri);
        }
        return uri;
    }

    /**
     * Get the Path used to retrieve an resource.
     */
    protected String getResourceUri(FacesContext context, Class resourceLoader, boolean withContextPath)
    {
        StringBuffer sb = new StringBuffer(200);
        sb.append(RESOURCE_VIRTUAL_PATH);
        sb.append(PATH_SEPARATOR);
        sb.append(resourceLoader.getName());
        sb.append(PATH_SEPARATOR);
        sb.append(getCacheKey(context));
        sb.append(PATH_SEPARATOR);
        return getResourceUri(context, sb.toString(), withContextPath);
    }

    protected long getCacheKey(FacesContext context)
    {
        // cache key is hold in application scope so it is recreated on redeploying the webapp.
        Map applicationMap = context.getExternalContext().getApplicationMap();
        Long cacheKey = (Long) applicationMap.get(RESOURCES_CACHE_KEY);
        if (cacheKey == null)
        {
            cacheKey = new Long(System.currentTimeMillis() / 100000);
            applicationMap.put(RESOURCES_CACHE_KEY, cacheKey);
        }
        return cacheKey.longValue();
    }

    public boolean isResourceUri(HttpServletRequest request)
    {
        String path;
        if (_contextPath != null)
        {
            path = _contextPath + RESOURCE_VIRTUAL_PATH;
        }
        else
        {
            path = RESOURCE_VIRTUAL_PATH;
        }
        return request.getRequestURI().startsWith(path);
    }

    private Class getClass(String className) throws ClassNotFoundException
    {
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        validateResourceLoader(clazz);
        return clazz;
    }

    public void serveResource(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        String uri = request.getRequestURI();
        String classNameStartsAfter = RESOURCE_VIRTUAL_PATH + '/';

        int posStartClassName = uri.indexOf(classNameStartsAfter) + classNameStartsAfter.length();
        int posEndClassName = uri.indexOf(PATH_SEPARATOR, posStartClassName);
        String className;
        className = uri.substring(posStartClassName, posEndClassName);
        int posEndCacheKey = uri.indexOf(PATH_SEPARATOR, posEndClassName + 1);
        String resourceUri = null;
        if (posEndCacheKey + 1 < uri.length())
        {
            resourceUri = uri.substring(posEndCacheKey + 1);
        }
        try
        {
            Class resourceLoader = getClass(className);
            validateResourceLoader(resourceLoader);
            ((ResourceLoader) resourceLoader.newInstance()).serveResource(request, response,
                    resourceUri);
            response.flushBuffer();
        }
        catch (ClassNotFoundException e)
        {
            log.error("Could not find class for name: " + className, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Could not find resourceloader class for name: " + className);
        }
        catch (InstantiationException e)
        {
            log.error("Could not instantiate class for name: " + className, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not instantiate resourceloader class for name: " + className);
        }
        catch (IllegalAccessException e)
        {
            log.error("Could not access class for name: " + className, e);
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Could not access resourceloader class for name: " + className);
        }
        catch (Throwable e)
        {
            log.error("Error while serving resource: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Header stuffs

    private Set getHeaderInfos(HttpServletRequest request)
    {
        Set set = (Set) request.getAttribute(HEADER_INFO_REQUEST_ATTRUBITE_NAME);
        if (set == null)
        {
            set = new LinkedHashSet();
            request.setAttribute(HEADER_INFO_REQUEST_ATTRUBITE_NAME, set);
        }
        return set;
    }

    private void addHeaderInfo(FacesContext context, HeaderInfo info)
    {
        //todo: fix this to work in PortletRequest as well
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set set = getHeaderInfos(request);
        set.add(info);
    }

    public boolean hasHeaderInfos(HttpServletRequest request)
    {
        return request.getAttribute(HEADER_INFO_REQUEST_ATTRUBITE_NAME) != null;
    }

    /**
     * Add the resources to the &lt;head&gt; of the page.
     * If the head tag is missing, but the &lt;body&gt; tag is present, the head tag is added.
     * If both are missing, no resources is added.
     * 
     * TODO : Change the ordering so that the user header CSS & JS override MyFaces' ones.
     */
    public void writeWithFullHeader(HttpServletRequest request,
            ExtensionsResponseWrapper responseWrapper, HttpServletResponse response)
            throws IOException
    {

        StringBuffer originalResponse = new StringBuffer(responseWrapper.toString());

        ParseCallbackListener l = new ParseCallbackListener();
        ReducedHTMLParser.parse(originalResponse, l);

        int headerInsertPosition = l.getHeaderInsertPosition();
        int bodyInsertPosition = l.getBodyInsertPosition();
        int beforeBodyPosition = l.getBeforeBodyPosition();
        boolean addHeaderTags = false;

        if (headerInsertPosition == -1)
        {
            if (beforeBodyPosition != -1)
            {
                addHeaderTags = true;
                headerInsertPosition = beforeBodyPosition;
            }
            else
            {
                log.warn("Response has no <head> or <body> tag:\n" + originalResponse);
            }
        }

        ResponseWriter writer = new HtmlResponseWriterImpl(response.getWriter(), HtmlRendererUtils
                .selectContentType(request.getHeader("accept")), null);

        if (headerInsertPosition > 0)
            writer.write(originalResponse.substring(0, headerInsertPosition));
        if (headerInsertPosition >= 0 && addHeaderTags)
            writer.write("<head>");

        if (headerInsertPosition >= 0)
        {
            for (Iterator i = getHeaderInfos(request).iterator(); i.hasNext();)
            {
                writer.write("\n");
                HeaderInfo headerInfo = (HeaderInfo) i.next();
                headerInfo.writeHeaderInfo(response, writer);
            }
        }

        if (headerInsertPosition >= 0 && addHeaderTags)
            writer.write("</head>");

        writer
                .write(headerInsertPosition > 0 ? originalResponse.substring(headerInsertPosition) : originalResponse
                        .toString());
        if (bodyInsertPosition > 0)
        {
            if (ADDITIONAL_JAVASCRIPT_TO_BODY_TAG != null)
            {
                originalResponse.insert( bodyInsertPosition + 5, ADDITIONAL_JAVASCRIPT_TO_BODY_TAG + "\"" );
            }
        }

        writer.write( headerInsertPosition > 0 ?
                originalResponse.substring(headerInsertPosition) : originalResponse.toString());
    }

    private HeaderInfo getStyleInstance(FacesContext context, ResourceHandler resourceHandler)
    {
        return new StyleHeaderInfo(getResourceUri(context, resourceHandler));
    }

    private HeaderInfo getScriptInstance(FacesContext context, ResourceHandler resourceHandler,
            boolean defer)
    {
        return new ScriptHeaderInfo(getResourceUri(context, resourceHandler), defer);
    }

    private HeaderInfo getStyleInstance(FacesContext context, String uri)
    {
        return new StyleHeaderInfo(getResourceUri(context, uri));
    }

    protected HeaderInfo getScriptInstance(FacesContext context, String uri, boolean defer)
    {
        return new ScriptHeaderInfo(getResourceUri(context, uri), defer);
    }

    private HeaderInfo getInlineScriptInstance(String inlineScript)
    {
        return new InlineScriptHeaderInfo(inlineScript);
    }

    private HeaderInfo getInlineStyleInstance(String inlineStyle)
    {
        return new InlineStyleHeaderInfo(inlineStyle);
    }

    private interface HeaderInfo
    {
        public abstract void writeHeaderInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException;
    }

    private abstract class AbstractResourceUri
    {
        protected final String _resourceUri;

        protected AbstractResourceUri(String resourceUri)
        {
            _resourceUri = resourceUri;
        }

        public int hashCode()
        {
            return _resourceUri.hashCode();
        }

        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (obj == this)
            {
                return true;
            }
            if (obj instanceof AbstractResourceUri)
            {
                AbstractResourceUri other = (AbstractResourceUri) obj;
                return _resourceUri.equals(other._resourceUri);
            }
            return false;
        }

        protected String getResourceUri()
        {
            return _resourceUri;
        }
    }

    private class StyleHeaderInfo extends AbstractResourceUri implements HeaderInfo
    {
        protected StyleHeaderInfo(String resourceUri)
        {
            super(resourceUri);
        }

        public void writeHeaderInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.LINK_ELEM, null);
            writer.writeAttribute(HTML.REL_ATTR, HTML.STYLESHEET_VALUE, null);
            writer.writeAttribute(HTML.HREF_ATTR, response.encodeURL(this.getResourceUri()), null);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.STYLE_TYPE_TEXT_CSS, null);
            writer.endElement(HTML.LINK_ELEM);
        }
    }

    private class ScriptHeaderInfo extends AbstractResourceUri implements HeaderInfo
    {
        protected final boolean _defer;

        public ScriptHeaderInfo(String resourceUri, boolean defer)
        {
            super(resourceUri);
            _defer = defer;
        }

        public int hashCode()
        {
            return new HashCodeBuilder().append(this.getResourceUri()).append(_defer).toHashCode();
        }

        public boolean equals(Object obj)
        {
            if (super.equals(obj))
            {
                if (obj instanceof ScriptHeaderInfo)
                {
                    ScriptHeaderInfo other = (ScriptHeaderInfo) obj;
                    return new EqualsBuilder().append(_defer, other._defer).isEquals();
                }
            }
            return false;
        }

        public void writeHeaderInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.SCRIPT_ELEM, null);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.writeAttribute(HTML.SRC_ATTR, response.encodeURL(this.getResourceUri()), null);

            if (_defer)
            {
                writer.writeAttribute(HTML.SCRIPT_ELEM_DEFER_ATTR, "true", null);
            }
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    private abstract class InlineHeaderInfo implements HeaderInfo
    {
        private final String _inlineValue;

        protected InlineHeaderInfo(String inlineValue)
        {
            _inlineValue = inlineValue;
        }

        public String getInlineValue()
        {
            return _inlineValue;
        }

        public int hashCode()
        {
            return new HashCodeBuilder().append(_inlineValue).toHashCode();
        }

        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (obj == this)
            {
                return true;
            }
            if (obj instanceof InlineHeaderInfo)
            {
                InlineHeaderInfo other = (InlineHeaderInfo) obj;
                return new EqualsBuilder().append(_inlineValue, other._inlineValue).isEquals();
            }
            return false;
        }
    }

    private class InlineScriptHeaderInfo extends InlineHeaderInfo
    {
        protected InlineScriptHeaderInfo(String inlineScript)
        {
            super(inlineScript);
        }

        public void writeHeaderInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.SCRIPT_ELEM, null);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.writeText(getInlineValue(), null);
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    private class InlineStyleHeaderInfo extends InlineHeaderInfo
    {
        protected InlineStyleHeaderInfo(String inlineStyle)
        {
            super(inlineStyle);
        }

        public void writeHeaderInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.STYLE_ELEM, null);
            writer.writeAttribute(HTML.REL_ATTR, HTML.STYLESHEET_VALUE, null);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.STYLE_TYPE_TEXT_CSS, null);
            writer.writeText(getInlineValue(), null);
            writer.endElement(HTML.STYLE_ELEM);
        }
    }

    private static class ParseCallbackListener implements CallbackListener
    {
        private int headerInsertPosition = -1;
        private int bodyInsertPosition = -1;
        private int beforeBodyPosition = -1;

        public void openedStartTag(int charIndex, int tagIdentifier)
        {
            if (tagIdentifier == ReducedHTMLParser.BODY_TAG)
            {
                beforeBodyPosition = charIndex;
            }
        }

        public void closedStartTag(int charIndex, int tagIdentifier)
        {
            if (tagIdentifier == ReducedHTMLParser.HEAD_TAG)
            {
                headerInsertPosition = charIndex + 1;
            }
            else if (tagIdentifier == ReducedHTMLParser.BODY_TAG)
            {
                bodyInsertPosition = charIndex + 1;
            }
        }

        public void openedEndTag(int charIndex, int tagIdentifier)
        {
        }

        public void closedEndTag(int charIndex, int tagIdentifier)
        {
        }

        public void attribute(int charIndex, int tagIdentifier, String key, String value)
        {
        }

        public int getHeaderInsertPosition()
        {
            return headerInsertPosition;
        }

        public int getBodyInsertPosition()
        {
            return bodyInsertPosition;
        }

        public int getBeforeBodyPosition()
        {
            return beforeBodyPosition;
        }
    }
}
