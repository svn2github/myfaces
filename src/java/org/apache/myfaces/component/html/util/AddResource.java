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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HtmlResponseWriterImpl;
import org.apache.myfaces.custom.buffer.HtmlBufferResponseWriterWrapper;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * This is a utility class to render link to resources used by custom components.
 * Mostly used to avoid having to include [script src="..."][/script]
 * in the head of the pages before using a component.
 * <p>
 * When used together with the ExtensionsFilter, this class can allow components
 * in the body of a page to emit script and stylesheet references into the page
 * head section. The relevant methods on this object simply queue the changes,
 * and when the page is complete the ExtensionsFilter calls back into this 
 * class to allow it to insert the commands into the buffered response.
 * <p>
 * This class also works with the ExtensionsFilter to allow components to
 * emit references to javascript/css/etc which are bundled in the component's
 * jar file. Special URLs are generated which the ExtensionsFilter will later
 * handle by retrieving the specified resource from the classpath.
 * <p>
 * The special URL format is:
 * <pre>
 * {contextPath}/faces/myFacesExtensionResource/
 *    {resourceLoaderName}/{cacheKey}/{resourceURI}
 * </pre>
 * Where:
 * <ul>
 * <li> {contextPath} is the context path of the current webapp
 * <li> {resourceLoaderName} is the fully-qualified name of a class which 
 *  implements the ResourceLoader interface. When a browser app sends a request
 *  for the specified resource, an instance of the specified ResourceLoader class
 *  will be created and passed the resourceURI part of the URL for resolving to the
 *  actual resource to be served back. The standard MyFaces ResourceLoader
 *  implementation only serves resources for files stored beneath path
 *  org/apache/myfaces/custom in the classpath but non-myfaces code can provide their
 *  own ResourceLoader implementations.
 * </ul>
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class AddResource
{
    private static final String PATH_SEPARATOR = "/";

    protected static final Log log = LogFactory.getLog(AddResource.class);

    private static final String RESOURCE_VIRTUAL_PATH = "/faces/myFacesExtensionResource";

    private static final String HEADER_BEGIN_INFO_REQUEST_ATTRIBUTE_NAME = AddResource.class.getName()
            + ".HEADER_BEGIN_INFO";

    private static final String BODY_END_INFO_REQUEST_ATTRIBUTE_NAME = AddResource.class.getName()
            + ".BODY_END_INFO";

    private static final String BODY_ONLOAD_INFO_REQUEST_ATTRIBUTE_NAME = AddResource.class.getName()
            + ".BODY_ONLOAD_INFO";

    private static final String RESOURCES_CACHE_KEY = AddResource.class.getName() + ".CACHE_KEY";

    protected final String _contextPath;

    public static final Position HEADER_BEGIN = new Position(0);
    public static final Position BODY_END = new Position(1);
    public static final Position BODY_ONLOAD = new Position(2);

    private AddResource(String contextPath)
    {
        _contextPath = contextPath;
    }

    /**
     * Map of AddResource instances keyed by context path. This map will have
     * more than one entry only if the myfaces library is in the "shared"
     * classpath of a container where more than one webapp can see it
     * simultaneously.
     */
    private static final Map _addResourceMap = new HashMap();

    /**
     * Internal factory method.
     * <p>
     * Return an instance of AddResource keyed by context path, or create one
     * if no such instance already exists. Note that the same AddResource object
     * is shared among all threads servicing the same webapp, so all methods
     * on this class are required to be threadsafe.
     * <p>
     * Note that this method is package-scope for the purposes of unit-testing only.
     * This method should be treated as private by non-test code.
     */
    static AddResource getInstance(String contextPath)
    {
        // Yes, this method does use a variant of the "double locking" idiom
        // which is well documented to be invalid for general use. However
        // it is believed safe for use here because AddResource objects in
        // _addResourceMap are never removed or replaced.
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

    // Methods to add resources

    /**
     * Insert a [script src="url"] entry at the current location in the response.
     * The resource is expected to be in the classpath, at the same location as the
     * specified component + "/resource".
     * <p>
     * Example: when customComponent is class example.Widget, and 
     * resourceName is script.js, the resource will be retrieved from 
     * "example/Widget/resource/script.js" in the classpath.
     */
    public void addJavaScriptHere(FacesContext context, Class myfacesCustomComponent,
            String resourceName) throws IOException
    {
        addJavaScriptHere(context, new MyFacesResourceHandler(myfacesCustomComponent, resourceName));
    }

    /**
     * Insert a [script src="url"] entry at the current location in the response.
     * 
     * @param uri is the location of the desired resource, relative to the base
     * directory of the webapp (ie its contextPath). 
     */
    public void addJavaScriptHere(FacesContext context, String uri) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        String src = context.getExternalContext().encodeResourceURL(getResourceUri(context, uri));
        writer.writeURIAttribute(HTML.SRC_ATTR, src, null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Insert a [script src="url"] entry at the current location in the response.
     * 
     * @param context
     * 
     * @param resourceHandler is an object which specifies exactly how to build the url
     * that is emitted into the script tag. Code which needs to generate URLs in ways
     * that this class does not support by default can implement a custom ResourceHandler.
     * 
     * @throws IOException
     */
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
     * Verify that the resource handler is acceptable. Null is not
     * valid, and the getResourceLoaderClass method must return a
     * Class object whose instances implements the ResourceLoader
     * interface.
     * 
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
     * Given a Class object, verify that the instances of that class 
     * implement the ResourceLoader interface.
     * 
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
     * Adds the given Javascript resource to the document header at the specified
     * document positioy by supplying a resourcehandler instance.
     * <p>
     * Use this method to have full control about building the reference url
     * to identify the resource and to customize how the resource is 
     * written to the response. In most cases, however, one of the convenience
     * methods on this class can be used without requiring a custom ResourceHandler
     * to be provided.
     * <p>
     * If the script has already been referenced, it's added only once.
     * <p>
     * Note that this method <i>queues</i> the javascript for insertion, and that
     * the script is inserted into the buffered response by the ExtensionsFilter
     * after the page is complete.
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, ResourceHandler resourceHandler)
    {
        addJavaScriptAtPosition(context, position, resourceHandler, false);
    }

    /**
     * Insert a [script src="url"] entry into the document header at the
     * specified document position. If the script has already been
     * referenced, it's added only once.
     * <p> 
     * The resource is expected to be in the classpath, at the same location as the
     * specified component + "/resource".
     * <p>
     * Example: when customComponent is class example.Widget, and 
     * resourceName is script.js, the resource will be retrieved from 
     * "example/Widget/resource/script.js" in the classpath.
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, Class myfacesCustomComponent,
                                        String resourceName)
    {
        addJavaScriptAtPosition(context, position, new MyFacesResourceHandler(myfacesCustomComponent,
                resourceName));
    }

    /**
     * Insert a [script src="url"] entry into the document header at the
     * specified document position. If the script has already been
     * referenced, it's added only once.
     * 
     * @param defer specifies whether the html attribute "defer" is set on the
     * generated script tag. If this is true then the browser will continue
     * processing the html page without waiting for the specified script to
     * load and be run.
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, Class myfacesCustomComponent,
                                        String resourceName, boolean defer)
    {
        addJavaScriptAtPosition(context, position, new MyFacesResourceHandler(myfacesCustomComponent,
                resourceName), defer);
    }

    /**
     * Insert a [script src="url"] entry into the document header at the
     * specified document position. If the script has already been
     * referenced, it's added only once.
     * 
     * @param uri is the location of the desired resource, relative to the base
     * directory of the webapp (ie its contextPath). 
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, String uri)
    {
        addJavaScriptAtPosition(context, position, uri, false);
    }

    /**
     * Adds the given Javascript resource at the specified document position.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, String uri, boolean defer)
    {
        addPositionedInfo(context, position, getScriptInstance(context, uri, defer));
    }

    public void addJavaScriptToBodyTag(FacesContext context,
                                       String javascriptEventName, String addedJavaScript)
    {
        AttributeInfo info = new AttributeInfo();
        info.setAttributeName(javascriptEventName);
        info.setAttributeValue(addedJavaScript);

        addPositionedInfo(context, BODY_ONLOAD, info);
    }

    /**
     * Adds the given Javascript resource at the specified document position.
     * If the script has already been referenced, it's added only once.
     */
    public void addJavaScriptAtPosition(FacesContext context, Position position, ResourceHandler resourceHandler,
                                        boolean defer)
    {
        validateResourceHandler(resourceHandler);
        addPositionedInfo(context, position, getScriptInstance(context, resourceHandler, defer));
    }

    /**
     * Adds the given Style Sheet at the specified document position.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, Position position, Class myfacesCustomComponent,
            String resourceName)
    {
        addStyleSheet(context, position, new MyFacesResourceHandler(myfacesCustomComponent, resourceName));
    }

    /**
     * Adds the given Style Sheet at the specified document position.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, Position position, String uri)
    {
        addPositionedInfo(context, position, getStyleInstance(context, uri));
    }

    /**
     * Adds the given Style Sheet at the specified document position.
     * If the style sheet has already been referenced, it's added only once.
     */
    public void addStyleSheet(FacesContext context, Position position, ResourceHandler resourceHandler)
    {
        validateResourceHandler(resourceHandler);
        addPositionedInfo(context, position, getStyleInstance(context, resourceHandler));
    }

    /**
     * Adds the given Inline Style at the specified document position.
     */
    public void addInlineStyleAtPosition(FacesContext context, Position position, String inlineStyle)
    {
        addPositionedInfo(context, position, getInlineStyleInstance(inlineStyle));
    }

    /**
     * Adds the given Inline Script at the specified document position.
     */
    public void addInlineScriptAtPosition(FacesContext context, Position position, String inlineScript)
    {
        addPositionedInfo(context, position, getInlineScriptInstance(inlineScript));
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

    /**
     * Return a value used in the {cacheKey} part of a generated URL for a
     * resource reference.
     * <p>
     * Caching in browsers normally works by having files served to them
     * include last-modified and expiry-time http headers. Until the expiry
     * time is reached, a browser will silently use its cached version. After
     * the expiry time, it will send a "get if modified since {time}" message,
     * where {time} is the last-modified header from the version it has cached.
     * <p>
     * Unfortunately this scheme only works well for resources represented as
     * plain files on disk, where the webserver can easily and efficiently see
     * the last-modified time of the resource file. When that query has to be
     * processed by a servlet that doesn't scale well, even when it is possible
     * to determine the resource's last-modified date from servlet code.
     * <p>
     * Fortunately, for the AddResource class a static resource is only ever
     * accessed because a URL was embedded by this class in a dynamic page.
     * This makes it possible to implement caching by instead marking every
     * resource served with a very long expiry time, but forcing the URL that
     * points to the resource to change whenever the old cached version becomes
     * invalid; the browser effectively thinks it is fetching a different
     * resource that it hasn't seen before. This is implemented by embedding
     * a "cache key" in the generated URL.
     * <p>
     * Rather than using the actual modification date of a resource as the
     * cache key, we simply use the webapp deployment time. This means that all
     * data cached by browsers will become invalid after a webapp deploy (all
     * the urls to the resources change). It also means that changes that occur
     * to a resource <i>without</i> a webapp redeploy will not be seen by browsers.
     */
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
        String uri = request.getContextPath() + request.getServletPath() + request.getPathInfo();
        String classNameStartsAfter = RESOURCE_VIRTUAL_PATH + '/';

        int posStartClassName = uri.indexOf(classNameStartsAfter) + classNameStartsAfter.length();
        int posEndClassName = uri.indexOf(PATH_SEPARATOR, posStartClassName);
        String className = uri.substring(posStartClassName, posEndClassName);
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

    // Positioned stuffs

    private Set getHeaderBeginInfos(HttpServletRequest request)
    {
        Set set = (Set) request.getAttribute(HEADER_BEGIN_INFO_REQUEST_ATTRIBUTE_NAME);
        if (set == null)
        {
            set = new LinkedHashSet();
            request.setAttribute(HEADER_BEGIN_INFO_REQUEST_ATTRIBUTE_NAME, set);
        }
        return set;
    }

    private Set getBodyEndInfos(HttpServletRequest request)
    {
        Set set = (Set) request.getAttribute(BODY_END_INFO_REQUEST_ATTRIBUTE_NAME);
        if (set == null)
        {
            set = new LinkedHashSet();
            request.setAttribute(BODY_END_INFO_REQUEST_ATTRIBUTE_NAME, set);
        }
        return set;
    }

    private Set getBodyOnloadInfos(HttpServletRequest request)
    {
        Set set = (Set) request.getAttribute(BODY_ONLOAD_INFO_REQUEST_ATTRIBUTE_NAME);
        if (set == null)
        {
            set = new LinkedHashSet();
            request.setAttribute(BODY_ONLOAD_INFO_REQUEST_ATTRIBUTE_NAME, set);
        }
        return set;
    }

    private void addPositionedInfo(FacesContext context, Position position, PositionedInfo info)
    {
        if(HEADER_BEGIN.equals(position))
        {
            //todo: fix this to work in PortletRequest as well
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            Set set = getHeaderBeginInfos(request);
            set.add(info);
        }
        else if(BODY_END.equals(position))
        {
            //todo: fix this to work in PortletRequest as well
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            Set set = getBodyEndInfos(request);
            set.add(info);

        }
        else if(BODY_ONLOAD.equals(position))
        {
            //todo: fix this to work in PortletRequest as well
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            Set set = getBodyOnloadInfos(request);
            set.add(info);
        }
    }

    public boolean hasHeaderBeginInfos(HttpServletRequest request)
    {
        return request.getAttribute(HEADER_BEGIN_INFO_REQUEST_ATTRIBUTE_NAME) != null;
    }

    /**
     * Add the resources to the &lt;head&gt; of the page.
     * If the head tag is missing, but the &lt;body&gt; tag is present, the head tag is added.
     * If both are missing, no resource is added.
     *
     * The ordering is such that the user header CSS & JS override the MyFaces' ones.
     */
    public void writeWithFullHeader(HttpServletRequest request,
            String bufferedResponse, HttpServletResponse response)
            throws IOException
    {

        StringBuffer originalResponse = new StringBuffer(bufferedResponse);

        ParseCallbackListener l = new ParseCallbackListener();
        ReducedHTMLParser.parse(originalResponse, l);

        int headerInsertPosition = l.getHeaderInsertPosition();
        int bodyInsertPosition = l.getBodyInsertPosition();
        int beforeBodyPosition = l.getBeforeBodyPosition();
        int afterBodyContentInsertPosition = l.getAfterBodyContentInsertPosition();

        boolean addHeaderTags = false;

        if (headerInsertPosition == -1)
        {
            if (beforeBodyPosition != -1)
            {
                // The input html has a body start tag, but no head tags. We therefore
                // need to insert head start/end tags for our content to live in.
                addHeaderTags = true;
                headerInsertPosition = beforeBodyPosition;
            }
            else
            {
                // neither head nor body tags in the input
                log.warn("Response has no <head> or <body> tag:\n" + originalResponse);
            }
        }

        ResponseWriter writer = new HtmlResponseWriterImpl(response.getWriter(), HtmlRendererUtils
                .selectContentType(request.getHeader("accept")), null);

        if(afterBodyContentInsertPosition >=0)
        {
            // insert all the items that want to go immediately after the <body> tag.
            HtmlBufferResponseWriterWrapper writerWrapper = HtmlBufferResponseWriterWrapper.
                getInstance(writer);

            for (Iterator i = getBodyEndInfos(request).iterator(); i.hasNext();)
            {
                writerWrapper.write("\n");

                PositionedInfo positionedInfo = (PositionedInfo) i.next();

                if(!(positionedInfo instanceof WritablePositionedInfo))
                    throw new IllegalStateException("positionedInfo of type : "+
                            positionedInfo.getClass().getName());
                ((WritablePositionedInfo) positionedInfo).
                        writePositionedInfo(response, writerWrapper);
            }

            originalResponse.insert(headerInsertPosition,writerWrapper.toString());
        }

        if(bodyInsertPosition>0)
        {
            StringBuffer buf = new StringBuffer();
            Set bodyInfos = getBodyOnloadInfos(request);
            if (bodyInfos.size() > 0) 
            {
                int i=0;
                for (Iterator it = getBodyOnloadInfos(request).iterator(); it.hasNext();)
                {
                    AttributeInfo positionedInfo = (AttributeInfo) it.next();
                    if(i==0)
                    {
                        buf.append(positionedInfo.getAttributeName());
                        buf.append("=\"");
                    }
                    buf.append(positionedInfo.getAttributeValue());
    
                    i++;
                }

                buf.append("\"");
                originalResponse.insert( bodyInsertPosition-1, " "+
                        buf.toString());
            }
        }

        if (headerInsertPosition >= 0)
        {
            HtmlBufferResponseWriterWrapper writerWrapper = HtmlBufferResponseWriterWrapper.
                getInstance(writer);

            if(addHeaderTags)
                writerWrapper.write("<head>");

            for (Iterator i = getHeaderBeginInfos(request).iterator(); i.hasNext();)
            {
                writerWrapper.write("\n");

                PositionedInfo positionedInfo = (PositionedInfo) i.next();

                if(!(positionedInfo instanceof WritablePositionedInfo))
                    throw new IllegalStateException("positionedInfo of type : "+
                            positionedInfo.getClass().getName());
                ((WritablePositionedInfo) positionedInfo).
                        writePositionedInfo(response, writerWrapper);
            }

            if(addHeaderTags)
                writerWrapper.write("</head>");

            originalResponse.insert(headerInsertPosition,writerWrapper.toString());

        }

        writer.write(originalResponse.toString());
    }

    private PositionedInfo getStyleInstance(FacesContext context, ResourceHandler resourceHandler)
    {
        return new StylePositionedInfo(getResourceUri(context, resourceHandler));
    }

    private PositionedInfo getScriptInstance(FacesContext context, ResourceHandler resourceHandler,
            boolean defer)
    {
        return new ScriptPositionedInfo(getResourceUri(context, resourceHandler), defer);
    }

    private PositionedInfo getStyleInstance(FacesContext context, String uri)
    {
        return new StylePositionedInfo(getResourceUri(context, uri));
    }

    protected PositionedInfo getScriptInstance(FacesContext context, String uri, boolean defer)
    {
        return new ScriptPositionedInfo(getResourceUri(context, uri), defer);
    }

    private PositionedInfo getInlineScriptInstance(String inlineScript)
    {
        return new InlineScriptPositionedInfo(inlineScript);
    }

    private PositionedInfo getInlineStyleInstance(String inlineStyle)
    {
        return new InlineStylePositionedInfo(inlineStyle);
    }

    public static class Position
    {
        private final int _pos;

        private Position(int pos)
        {
            _pos = pos;            
        }
        
        public boolean equals(Object obj)
        {
            if(obj == null)
            {
                return false;
            }
            if(obj == this)
            {
                return true;
            }
            if(obj instanceof Position)
            {
                return ((Position)obj)._pos == _pos;
            }
            return false;
        }
        
        public int hashCode()
        {
            return _pos;
        }
    }

    private interface PositionedInfo
    {
    }

    private static class AttributeInfo implements PositionedInfo
    {
        private String _attributeName;
        private String _attributeValue;

        public String getAttributeName()
        {
            return _attributeName;
        }

        public void setAttributeName(String attributeName)
        {
            _attributeName = attributeName;
        }

        public String getAttributeValue()
        {
            return _attributeValue;
        }

        public void setAttributeValue(String attributeValue)
        {
            _attributeValue = attributeValue;
        }
    }

    private interface WritablePositionedInfo extends PositionedInfo
    {
        public abstract void writePositionedInfo(HttpServletResponse response, ResponseWriter writer)
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

    private class StylePositionedInfo extends AbstractResourceUri implements WritablePositionedInfo
    {
        protected StylePositionedInfo(String resourceUri)
        {
            super(resourceUri);
        }

        public void writePositionedInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.LINK_ELEM, null);
            writer.writeAttribute(HTML.REL_ATTR, HTML.STYLESHEET_VALUE, null);
            writer.writeAttribute(HTML.HREF_ATTR, response.encodeURL(this.getResourceUri()), null);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.STYLE_TYPE_TEXT_CSS, null);
            writer.endElement(HTML.LINK_ELEM);
        }
    }

    private class ScriptPositionedInfo extends AbstractResourceUri implements WritablePositionedInfo
    {
        protected final boolean _defer;

        public ScriptPositionedInfo(String resourceUri, boolean defer)
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
                if (obj instanceof ScriptPositionedInfo)
                {
                    ScriptPositionedInfo other = (ScriptPositionedInfo) obj;
                    return new EqualsBuilder().append(_defer, other._defer).isEquals();
                }
            }
            return false;
        }

        public void writePositionedInfo(HttpServletResponse response, ResponseWriter writer)
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

    private abstract class InlinePositionedInfo implements WritablePositionedInfo
    {
        private final String _inlineValue;

        protected InlinePositionedInfo(String inlineValue)
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
            if (obj instanceof InlinePositionedInfo)
            {
                InlinePositionedInfo other = (InlinePositionedInfo) obj;
                return new EqualsBuilder().append(_inlineValue, other._inlineValue).isEquals();
            }
            return false;
        }
    }

    private class InlineScriptPositionedInfo extends InlinePositionedInfo
    {
        protected InlineScriptPositionedInfo(String inlineScript)
        {
            super(inlineScript);
        }

        public void writePositionedInfo(HttpServletResponse response, ResponseWriter writer)
                throws IOException
        {
            writer.startElement(HTML.SCRIPT_ELEM, null);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.writeText(getInlineValue(), null);
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    private class InlineStylePositionedInfo extends InlinePositionedInfo
    {
        protected InlineStylePositionedInfo(String inlineStyle)
        {
            super(inlineStyle);
        }

        public void writePositionedInfo(HttpServletResponse response, ResponseWriter writer)
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
        private int afterBodyContentInsertPosition=-1;

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
                headerInsertPosition = charIndex;
            }
            else if (tagIdentifier == ReducedHTMLParser.BODY_TAG)
            {
                bodyInsertPosition = charIndex;
            }
        }

        public void openedEndTag(int charIndex, int tagIdentifier)
        {
            if (tagIdentifier == ReducedHTMLParser.BODY_TAG)
            {
                afterBodyContentInsertPosition = charIndex;
            }
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

        public int getAfterBodyContentInsertPosition()
        {
            return afterBodyContentInsertPosition;
        }
    }
}
