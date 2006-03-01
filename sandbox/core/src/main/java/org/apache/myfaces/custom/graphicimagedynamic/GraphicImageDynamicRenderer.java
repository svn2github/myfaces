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

package org.apache.myfaces.custom.graphicimagedynamic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.util.ParameterResourceHandler;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlImageRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.AddResource;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.ResourceLoader;
import org.apache.myfaces.shared_tomahawk.util.ClassUtils;

/**
 * @author Sylvain Vieujot
 * @version $Revision$ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class GraphicImageDynamicRenderer extends HtmlImageRenderer implements ResourceLoader
{
    protected static class SimpleImageContext implements ImageContext
    {
        private final Map _params;
        private final Integer _width;
        private final Integer _height;

        public SimpleImageContext(Map params, Integer width, Integer height)
        {
            _params = params;
            _width = width;
            _height = height;
        }

        public Map getParamters()
        {
            return _params;
        }

        public Integer getWidth()
        {
            return _width;
        }

        public Integer getHeight()
        {
            return _height;
        }
    }

    private static final Log log = LogFactory.getLog(GraphicImageDynamicRenderer.class);

    private static final class ImageResponseStream extends ResponseStream
    {
        private final OutputStream _out;

        private ImageResponseStream(OutputStream out)
        {
            _out = out;
        }

        public void close() throws IOException
        {
            _out.flush();
            _out.close();
        }

        public void flush() throws IOException
        {
            _out.flush();
        }

        public void write(byte[] b, int off, int len) throws IOException
        {
            _out.write(b, off, len);
        }

        public void write(byte[] b) throws IOException
        {
            _out.write(b);
        }

        public void write(int b) throws IOException
        {
            _out.write(b);
        }
    }

    private static final String RENDERER_PARAM = "_renderer";
    private static final String WIDTH_PARAM = "_width";
    private static final String HEIGHT_PARAM = "_height";

    public static final String RENDERER_TYPE = "org.apache.myfaces.GraphicImageDynamicRenderer";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, GraphicImageDynamic.class);

        GraphicImageDynamic graphicImageDynamic = (GraphicImageDynamic) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.IMG_ELEM, graphicImageDynamic);
        HtmlRendererUtils.writeIdIfNecessary(writer, graphicImageDynamic, context);
        HtmlRendererUtils.renderHTMLAttributes(writer, graphicImageDynamic,
                                               HTML.IMG_PASSTHROUGH_ATTRIBUTES);

        Map params = getParameterMap(context, component);
        String width = graphicImageDynamic.getWidth();
        if (width != null)
        {
            params.put(WIDTH_PARAM, width);
        }
        String height = graphicImageDynamic.getHeight();
        if (height != null)
        {
            params.put(HEIGHT_PARAM, height);
        }

        Class imageRendererClass = graphicImageDynamic.getImageRendererClass();
        if (imageRendererClass == null)
        {
            throw new FacesException("No imageRendererClass defined for component "
                                     + component.getId());
        }
        params.put(RENDERER_PARAM, imageRendererClass.getName());

        AddResource addResource = AddResourceFactory.getInstance(context);
        String url = addResource.getResourceUri(context, new ParameterResourceHandler(this
                .getClass(), params));
        writer.writeAttribute(HTML.SRC_ATTR, url, null);

        writer.endElement(HTML.IMG_ELEM);
    }

    protected Map getParameterMap(FacesContext context, UIComponent component)
    {
        Map result = new HashMap();
        for (Iterator iter = component.getChildren().iterator(); iter.hasNext();)
        {
            UIComponent child = (UIComponent) iter.next();
            if (child instanceof UIParameter)
            {
                UIParameter uiparam = (UIParameter) child;
                Object value = uiparam.getValue();
                if (value != null)
                {
                    result.put(uiparam.getName(), value);
                }
            }
        }
        return result;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    /**
     * @throws IOException
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.util.ResourceLoader#serveResource(javax.servlet.ServletContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public void serveResource(ServletContext context, HttpServletRequest request,
                              HttpServletResponse response, String resourceUri) throws IOException
    {
        FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
                .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId(context));
        FacesContext facesContext = facesContextFactory.getFacesContext(context, request, response,
                                                                        lifecycle);
        facesContext.setResponseStream(new ImageResponseStream(response.getOutputStream()));
        try
        {
            Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
            Object rendererValue = requestMap.get(RENDERER_PARAM);
            if (rendererValue == null)
            {
                throw new FacesException("no image renderer defined.");
            }
            try
            {
                Class rendererClass = ClassUtils.classForName(rendererValue.toString());
                if (!ImageRenderer.class.isAssignableFrom(rendererClass))
                {
                    throw new FacesException("Image renderer class [" + rendererValue
                                             + "] does not implement " + ImageRenderer.class.getName());
                }
                try
                {
                    ImageRenderer imageRenderer = (ImageRenderer) rendererClass.newInstance();
                    renderImage(imageRenderer, facesContext);
                }
                catch (InstantiationException e)
                {
                    throw new FacesException("could not instantiate image renderer class "
                                             + rendererValue + " : " + e.getMessage(), e);
                }
                catch (IllegalAccessException e)
                {
                    throw new FacesException("could not instantiate image renderer class "
                                             + rendererValue + " : " + e.getMessage(), e);
                }
                catch (Exception e)
                {
                    throw new FacesException("could not renderer image "
                                             + rendererValue + " : " + e.getMessage(), e);
                }
            }
            catch (ClassNotFoundException e)
            {
                throw new FacesException("image renderer class not found: " + e.getMessage(), e);
            }
            facesContext.getResponseStream().close();
        }
        finally
        {
            facesContext.release();
        }
    }

    protected void renderImage(ImageRenderer imageRenderer, FacesContext facesContext)
            throws Exception
    {
            ImageContext imageContext = createImageContext(facesContext);

        imageRenderer.setContext(facesContext, imageContext);

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
                .getResponse();


        int contentLength = imageRenderer.getContentLength();
        if( contentLength >0 )
        {
            response.setContentLength(contentLength);
        }

        String contentType = imageRenderer.getContentType();
        if (contentType != null && contentType.length() > 0 )
        {
            response.setContentType(contentType);
        }

        ResponseStream out = facesContext.getResponseStream();
        try
        {
                imageRenderer.renderResource( out );
        }
        finally
        {
            out.close();
            facesContext.responseComplete();
        }
    }

    protected ImageContext createImageContext(FacesContext facesContext)
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        final Map requestMap = externalContext.getRequestParameterMap();
        Object value = requestMap.get(WIDTH_PARAM);
        Integer width = null;
        if (value != null)
        {
            try
            {
                width = Integer.valueOf(value.toString());
            }
            catch (NumberFormatException e)
            {
                log.error("Invalid value for image width : " + value + ", " + e.getMessage(), e);
            }
        }
        Integer height = null;
        value = requestMap.get(HEIGHT_PARAM);
        if (value != null)
        {
            try
            {
                height = Integer.valueOf(value.toString());
            }
            catch (NumberFormatException e)
            {
                log.error("Invalid value for image height : " + value + ", " + e.getMessage(), e);
            }
        }
        return new SimpleImageContext(requestMap, width, height);
    }

    private String getLifecycleId(ServletContext context)
    {
        String lifecycleId = context.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }
}
