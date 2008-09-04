/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.tomahawk.application.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.BasicAttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;
import org.apache.myfaces.shared_tomahawk.webapp.webxml.ServletMapping;
import org.apache.myfaces.shared_tomahawk.webapp.webxml.WebXml;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * This ViewHandler should be used with myfaces core 1.2.4 or superior
 * and it does not work with jsf ri 1.2 
 * 
 * @since 1.1.7
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 */
public class JspTilesTwoViewHandlerImpl
        extends ViewHandler {
    private ViewHandler _viewHandler;

    public static final String FORM_STATE_MARKER = "<!--@@JSF_FORM_STATE_MARKER@@-->";
    public static final int FORM_STATE_MARKER_LEN = FORM_STATE_MARKER.length();
    
    private static final Log log = LogFactory.getLog(JspTilesTwoViewHandlerImpl.class);
    private static final String TILES_DEF_EXT = ".tiles";
    private String tilesExtension = TILES_DEF_EXT;
    private static final String AFTER_VIEW_TAG_CONTENT_PARAM = JspTilesTwoViewHandlerImpl.class + ".AFTER_VIEW_TAG_CONTENT";

    public JspTilesTwoViewHandlerImpl(ViewHandler viewHandler)
    {
        _viewHandler = viewHandler;
    }

    private void initContainer(ExternalContext context) {

        if(TilesAccess.getContainer(context.getContext())==null) {
            try
            {
                TilesContainerFactory factory = TilesContainerFactory.getFactory(context.getContext());
                TilesContainer container = factory.createTilesContainer(context.getContext());
                TilesAccess.setContainer(context.getContext(),container);
            }
            catch (Exception e)
            {
                throw new FacesException("Error reading tiles definitions : " + e.getMessage(), e);
            }
        }
    }

    public void renderView(FacesContext facesContext, UIViewRoot viewToRender) throws IOException, FacesException
    {
        if (viewToRender == null)
        {
            log.fatal("viewToRender must not be null");
            throw new NullPointerException("viewToRender must not be null");
        }

        ExternalContext externalContext = facesContext.getExternalContext();

        String viewId = deriveViewId(externalContext, viewToRender.getViewId());

        if(viewId==null) {
            //deriving view-id made clear we are not responsible for this view-id - call the delegate
            _viewHandler.renderView(facesContext, viewToRender);
            return;
        }


        initContainer(externalContext);

        String tilesId = deriveTileFromViewId(viewId);

        TilesContainer container = TilesAccess.getContainer(externalContext.getContext());

        Object[] requestObjects = {externalContext.getRequest(), externalContext.getResponse()};

        if(container.isValidDefinition(tilesId, requestObjects)) {

            //propagate our new view-id to wherever it makes sense
            setViewId(viewToRender, viewId, facesContext);
            renderTilesView(facesContext, requestObjects, container, viewToRender, viewId, tilesId);
        } else {
            //we're not using tiles as no valid definition has been found
            //just call the delegate view-handler to let it do its thing
            _viewHandler.renderView(facesContext, viewToRender);
        }
    }

    private void renderTilesView(FacesContext facesContext,
            Object[] requestObjects, TilesContainer container,
            UIViewRoot viewToRender, String viewId, String tilesId)
            throws IOException
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        handleCharacterEncoding(viewId, externalContext, viewToRender);
        container.startContext(requestObjects);
        try
        {
            //container.render(tilesId,requestObjects);
            buildTilesViewLikeContainer(externalContext, container, tilesId,
                    requestObjects);
        }
        catch (TilesException e)
        {
            throw new FacesException(e);
        }
        finally
        {
            container.endContext(requestObjects);
        }

        handleCharacterEncodingPostDispatch(externalContext);

        // render the view in this method (since JSF 1.2)
        RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder
                .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderFactory.getRenderKit(facesContext,
                viewToRender.getRenderKitId());
        ServletResponse response = (ServletResponse) requestObjects[1];
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        if (responseWriter == null)
        {
            responseWriter = renderKit.createResponseWriter(response
                    .getWriter(), null, ((HttpServletRequest) externalContext
                    .getRequest()).getCharacterEncoding());
            facesContext.setResponseWriter(responseWriter);
        }

        ResponseWriter oldResponseWriter = responseWriter;
        StateMarkerAwareWriter stateAwareWriter = null;

        StateManager stateManager = facesContext.getApplication()
                .getStateManager();
        if (stateManager.isSavingStateInClient(facesContext))
        {
            stateAwareWriter = new StateMarkerAwareWriter();

            // Create a new response-writer using as an underlying writer the stateAwareWriter
            // Effectively, all output will be buffered in the stateAwareWriter so that later
            // this writer can replace the state-markers with the actual state.
            responseWriter = oldResponseWriter
                    .cloneWithWriter(stateAwareWriter);
            facesContext.setResponseWriter(responseWriter);
        }

        actuallyRenderView(facesContext, viewToRender);

        //We're done with the document - now we can write all content
        //to the response, properly replacing the state-markers on the way out
        //by using the stateAwareWriter
        if (stateManager.isSavingStateInClient(facesContext))
        {
            stateAwareWriter.flushToWriter(response.getWriter());
        }
        else
        {
            stateManager.saveView(facesContext);
        }

        // Final step - we output any content in the wrappedResponse response from above to the response,
        // removing the wrappedResponse response from the request, we don't need it anymore
        ViewResponseWrapper afterViewTagResponse = (ViewResponseWrapper) externalContext
                .getRequestMap().get(AFTER_VIEW_TAG_CONTENT_PARAM);
        externalContext.getRequestMap().remove(AFTER_VIEW_TAG_CONTENT_PARAM);
        if (afterViewTagResponse != null)
        {
            afterViewTagResponse.flushToWriter(response.getWriter(),
                    facesContext.getExternalContext()
                            .getResponseCharacterEncoding());
        }
        response.flushBuffer();
    }
    
    
    private String deriveTileFromViewId(String viewId) {
        String tilesId = viewId;
        int idx = tilesId.lastIndexOf('.');
        if (idx > 0)
        {
            tilesId = tilesId.substring(0, idx) + tilesExtension;
        }
        else
        {
            tilesId = tilesId  + tilesExtension;
        }
        return tilesId;
    }

    private String deriveViewId(ExternalContext externalContext, String viewId) {
        ServletMapping servletMapping = getServletMapping(externalContext);

        String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
        String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
        if (servletMapping.isExtensionMapping())
        {
            if (!viewId.endsWith(suffix))
            {
                int dot = viewId.lastIndexOf('.');
                if (dot == -1)
                {
                    if (log.isTraceEnabled()) log.trace("Current viewId has no extension, appending default suffix " + suffix);
                    return viewId + suffix;
                }
                else
                {
                    if (log.isTraceEnabled()) log.trace("Replacing extension of current viewId by suffix " + suffix);
                    return viewId.substring(0, dot) + suffix;
                }
            }

            //extension-mapped page ends with proper suffix - all ok
            return viewId;
        }
        else if (!viewId.endsWith(suffix))
        {
            // path-mapping used, but suffix is no default-suffix
            return null;
        }
        else {
            //path-mapping used, suffix is default-suffix - all ok
            return viewId;
        }
    }

    private void handleCharacterEncodingPostDispatch(ExternalContext externalContext) {
        // handle character encoding as of section 2.5.2.2 of JSF 1.1
        if (externalContext.getRequest() instanceof HttpServletRequest) {
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.setAttribute(ViewHandler.CHARACTER_ENCODING_KEY, response.getCharacterEncoding());
            }
        }
    }

    private void handleCharacterEncoding(String viewId, ExternalContext externalContext, UIViewRoot viewToRender) {
        if (log.isTraceEnabled()) log.trace("Dispatching to " + viewId);

        // handle character encoding as of section 2.5.2.2 of JSF 1.1
        if (externalContext.getResponse() instanceof ServletResponse) {
            ServletResponse response = (ServletResponse) externalContext.getResponse();
            response.setLocale(viewToRender.getLocale());
        }
    }
    
    private void buildTilesViewLikeContainer(ExternalContext externalContext,
            TilesContainer container, String definitionName,
            Object... requestItems) throws TilesException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Render request recieved for definition '"
                    + definitionName + "'");
        }
        TilesRequestContext tilesRequest = ((BasicTilesContainer) container)
                .getContextFactory().createRequestContext(
                        container.getApplicationContext(), requestItems);
        Definition definition = ((BasicTilesContainer) container)
                .getDefinitionsFactory().getDefinition(definitionName,
                        tilesRequest);
        if (definition == null)
        {
            if (log.isWarnEnabled())
            {
                String message = "Unable to find the definition '"
                        + definitionName + "'";
                log.warn(message);
            }
            throw new NoSuchDefinitionException(definitionName);
        }
        if (!isPermitted(tilesRequest, definition.getRole()))
        {
            log.info("Access to definition '" + definitionName
                    + "' denied. User not in role '" + definition.getRole());
            return;
        }

        AttributeContext originalContext = container
                .getAttributeContext(requestItems);
        BasicAttributeContext subContext = new BasicAttributeContext(
                originalContext);
        subContext.addMissing(definition.getAttributes());
        BasicAttributeContext.pushContext(subContext, tilesRequest);

        try
        {
            if (definition.getPreparer() != null)
            {
                prepare(container, tilesRequest, definition.getPreparer(), true);
            }
            String dispatchPath = definition.getTemplate();
            if (log.isDebugEnabled())
            {
                log.debug("Dispatching to definition path '"
                        + definition.getTemplate() + " '");
            }

            if (!buildView(container, tilesRequest, externalContext,
                    dispatchPath))
            {
                //building the view was unsuccessful - an exception occurred during rendering
                //we need to jump out
                return;
            }

            // tiles exception so that it doesn't need to be rethrown.
        }
        catch (TilesException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("Error rendering tile", e);
            throw new TilesException(e.getMessage(), e);
        }
        finally
        {
            BasicAttributeContext.popContext(tilesRequest);
        }
    }
    
    /**
     * Checks if the current user is in one of the comma-separated roles
     * specified in the <code>role</code> parameter.
     *
     * @param request The request context.
     * @param role The comma-separated list of roles.
     * @return <code>true</code> if the current user is in one of those roles.
     */
    private boolean isPermitted(TilesRequestContext request, String role) {
        if (role == null) {
            return true;
        }
        StringTokenizer st = new StringTokenizer(role, ",");
        while (st.hasMoreTokens()) {
            if (request.isUserInRole(st.nextToken())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Execute a preparer.
     *
     * @param context The request context.
     * @param preparerName The name of the preparer.
     * @param ignoreMissing If <code>true</code> if the preparer is not found,
     * it ignores the problem.
     * @throws TilesException If the preparer is not found (and
     * <code>ignoreMissing</code> is not set) or if the preparer itself threw an
     * exception.
     */
    private void prepare(TilesContainer container, TilesRequestContext context, String preparerName, boolean ignoreMissing) throws TilesException {
        if (log.isDebugEnabled()) {
            log.debug("Prepare request received for '" + preparerName);
        }
        ViewPreparer preparer = ((BasicTilesContainer)container).getPreparerFactory().getPreparer(preparerName, context);
        if (preparer == null && ignoreMissing) {
            return;
        }
        if (preparer == null) {
            throw new NoSuchPreparerException("Preparer '" + preparerName + " not found");
        }
        AttributeContext attributeContext = BasicAttributeContext.getContext(context);
        preparer.execute(context, attributeContext);
    }
    
    /**Build the view-tree before rendering.
     * This is done by dispatching to the underlying JSP-page, effectively processing it, creating
     * components out of any text in between JSF components (not rendering the text to the output of course, this
     * will happen later while rendering), attaching these components
     * to the component tree, and buffering any content after the view-root.
     *
     * @param container The current TilesContainer
     * @param tilesRequest The current TilesRequestContext - its response will be replaced while the view-building happens (we want the text in the component tree, not on the actual servlet output stream)
     * @param externalContext The external context where the response will be replaced while building
     * @param viewId The view-id to dispatch to
     * @return true if successfull, false if an error occurred during rendering
     * @throws IOException
     */
    private boolean buildView(TilesContainer container,
            TilesRequestContext tilesRequest, ExternalContext externalContext,
            String viewId) throws IOException
    {
        HttpServletResponse response = (HttpServletResponse) tilesRequest
                .getResponse();
        ViewResponseWrapper wrappedResponse = new ViewResponseWrapper(response);
        tilesRequest = new ServletTilesRequestContext(
                ((ServletTilesApplicationContext) container
                        .getApplicationContext()).getServletContext(),
                (HttpServletRequest) tilesRequest.getRequest(), wrappedResponse);
        tilesRequest.dispatch(viewId);
        tilesRequest = new ServletTilesRequestContext(
                ((ServletTilesApplicationContext) container
                        .getApplicationContext()).getServletContext(),
                (HttpServletRequest) tilesRequest.getRequest(), response);
        boolean errorResponse = wrappedResponse.getStatus() < 200
                || wrappedResponse.getStatus() > 299;
        if (errorResponse)
        {
            wrappedResponse.flushToWrappedResponse();
            return false;
        }
        // store the wrapped response in the request, so it is thread-safe
        externalContext.getRequestMap().put(AFTER_VIEW_TAG_CONTENT_PARAM,
                wrappedResponse);
        return true;
    }
    
    /**
     * Render the view now - properly setting and resetting the response writer
     */
    private void actuallyRenderView(FacesContext facesContext, UIViewRoot viewToRender) throws IOException {
        // Set the new ResponseWriter into the FacesContext, saving the old one aside.
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        //Now we actually render the document
        // Call startDocument() on the ResponseWriter.
        responseWriter.startDocument();
        // Call encodeAll() on the UIViewRoot
        viewToRender.encodeAll(facesContext);
        // Call endDocument() on the ResponseWriter
        responseWriter.endDocument();
        responseWriter.flush();
    }

    private void setViewId(UIViewRoot viewToRender, String viewId, FacesContext facesContext) {
        viewToRender.setViewId(viewId);
        if(facesContext.getViewRoot()!=null) {
            facesContext.getViewRoot().setViewId(viewId);
        }
    }

    private static ServletMapping getServletMapping(ExternalContext externalContext)
    {
        String servletPath = externalContext.getRequestServletPath();
        String requestPathInfo = externalContext.getRequestPathInfo();

        WebXml webxml = WebXml.getWebXml(externalContext);
        List<?> mappings = webxml.getFacesServletMappings();

        boolean isExtensionMapping = requestPathInfo == null;

        for (int i = 0, size = mappings.size(); i < size; i++)
        {
            ServletMapping servletMapping = (ServletMapping) mappings.get(i);
            if (servletMapping.isExtensionMapping() == isExtensionMapping)
            {
                String urlpattern = servletMapping.getUrlPattern();
                if (isExtensionMapping)
                {
                    String extension = urlpattern.substring(1, urlpattern.length());
                    if (servletPath.endsWith(extension))
                    {
                        return servletMapping;
                    }
                }
                else
                {
                    urlpattern = urlpattern.substring(0, urlpattern.length() - 2);
                    // servletPath starts with "/" except in the case where the
                    // request is matched with the "/*" pattern, in which case
                    // it is the empty string (see Servlet Sepc 2.3 SRV4.4)
                    if (servletPath.equals(urlpattern))
                    {
                        return servletMapping;
                    }
                }
            }
        }
        log.error("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
        throw new IllegalArgumentException("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
    }


    public Locale calculateLocale(FacesContext context)
    {
        return _viewHandler.calculateLocale(context);
    }

    public String calculateRenderKitId(FacesContext context)
    {
        return _viewHandler.calculateRenderKitId(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId)
    {
        return _viewHandler.createView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId)
    {
        return _viewHandler.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path)
    {
        return _viewHandler.getResourceURL(context, path);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId)
    {
        return _viewHandler.restoreView(context, viewId);
    }

    public void writeState(FacesContext context) throws IOException
    {
        _viewHandler.writeState(context);
    }
    
    /**
     * Writes the response and replaces the state marker tags with the state information for the current context
     */
    private static class StateMarkerAwareWriter extends Writer
    {
        private StringBuilder buf;

        public StateMarkerAwareWriter()
        {
            this.buf = new StringBuilder();
        }

        @Override
        public void close() throws IOException
        {
        }

        @Override
        public void flush() throws IOException
        {
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException
        {
            if ((off < 0) || (off > cbuf.length) || (len < 0)
                    || ((off + len) > cbuf.length) || ((off + len) < 0))
            {
                throw new IndexOutOfBoundsException();
            }
            else if (len == 0)
            {
                return;
            }
            buf.append(cbuf, off, len);
        }

        public StringBuilder getStringBuilder()
        {
            return buf;
        }

        public void flushToWriter(Writer writer) throws IOException
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            StateManager stateManager = facesContext.getApplication().getStateManager();

            StringWriter stateWriter = new StringWriter();
            ResponseWriter realWriter = facesContext.getResponseWriter();
            facesContext.setResponseWriter(realWriter.cloneWithWriter(stateWriter));

            Object serializedView = stateManager.saveView(facesContext);

            stateManager.writeState(facesContext, serializedView);
            facesContext.setResponseWriter(realWriter);

            StringBuilder contentBuffer = getStringBuilder();
            String state = stateWriter.getBuffer().toString();

            ExternalContext extContext = facesContext.getExternalContext();
            if (JavascriptUtils.isJavascriptAllowed(extContext) && MyfacesConfig.getCurrentInstance(extContext).isViewStateJavascript()) {
             // If javascript viewstate is enabled no state markers were written
             write(contentBuffer, 0, contentBuffer.length(), writer);
             writer.write(state);
            } else {
             // If javascript viewstate is disabled state markers must be replaced
             int lastFormMarkerPos = 0;
             int formMarkerPos = 0;
             // Find all state markers and write out actual state instead
             while ((formMarkerPos = contentBuffer.indexOf(FORM_STATE_MARKER, formMarkerPos)) > -1)
             {
             // Write content before state marker
             write(contentBuffer, lastFormMarkerPos, formMarkerPos, writer);
             // Write state and move position in buffer after marker
             writer.write(state);
             formMarkerPos += FORM_STATE_MARKER_LEN;
             lastFormMarkerPos = formMarkerPos;
             }
                // Write content after last state marker
                if (lastFormMarkerPos < contentBuffer.length()) {
                 write(contentBuffer, lastFormMarkerPos, contentBuffer.length(), writer);
                }
            }

        }

        /**
         * Writes the content of the specified StringBuffer from index
         * <code>beginIndex</code> to index <code>endIndex - 1</code>.
         *
         * @param contentBuffer the <code>StringBuffer</code> to copy content from
         * @param begin the beginning index, inclusive.
         * @param end the ending index, exclusive
         * @param writer the <code>Writer</code> to write to
         * @throws IOException if an error occurs writing to specified <code>Writer</code>
         */
        private void write(StringBuilder contentBuffer, int beginIndex, int endIndex, Writer writer) throws IOException {
            int index = beginIndex;
            int bufferSize = 2048;
            char[] bufToWrite = new char[bufferSize];

            while (index < endIndex)
            {
                int maxSize = Math.min(bufferSize, endIndex - index);

                contentBuffer.getChars(index, index + maxSize, bufToWrite, 0);
                writer.write(bufToWrite, 0, maxSize);

                index += bufferSize;
            }
        }
    }
}
