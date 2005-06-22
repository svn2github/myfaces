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
package org.apache.myfaces.custom.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlResponseWriterImpl;
import org.apache.myfaces.renderkit.html.util.DummyFormResponseWriter;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BufferRenderer extends Renderer {
    private static final Log log = LogFactory.getLog(BufferRenderer.class);

    public static final String RENDERER_TYPE = "org.apache.myfaces.Buffer";

    private ResponseWriter initialWriter;
    private HtmlBufferResponseWriterWrapper bufferWriter;

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, Buffer.class);

        initialWriter = facesContext.getResponseWriter();
        bufferWriter = HtmlBufferResponseWriterWrapper.getInstance( initialWriter );
        facesContext.setResponseWriter( bufferWriter );
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException{
        RendererUtils.checkParamValidity(facesContext, component, Buffer.class);
        RendererUtils.renderChildren(facesContext, component);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) {
        Buffer buffer = (Buffer)uiComponent;
        buffer.fill(bufferWriter.toString(), facesContext);

        facesContext.setResponseWriter( initialWriter );

        if( bufferWriter.getDummyFormParams() != null ){
            try{ // Attempt to add the dummy form params (will not work with Sun RI)
				DummyFormResponseWriter dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter( facesContext );
				for(Iterator i = bufferWriter.getDummyFormParams().iterator() ; i.hasNext() ;)
					dummyFormResponseWriter.addDummyFormParameter( i.next().toString() );
				if( bufferWriter.isWriteDummyForm() )
					dummyFormResponseWriter.setWriteDummyForm( true );
            } catch (Exception e) {
                log.warn("Dummy form parameters are not supported by this JSF implementation.");
            }
        }
    }

    private static class HtmlBufferResponseWriterWrapper extends HtmlResponseWriterImpl {

        private ByteArrayOutputStream stream;
        private PrintWriter writer;

        static public HtmlBufferResponseWriterWrapper getInstance(ResponseWriter initialWriter){
            ByteArrayOutputStream instanceStream = new ByteArrayOutputStream();
            PrintWriter instanceWriter = new PrintWriter(instanceStream, true);

            return new HtmlBufferResponseWriterWrapper(initialWriter, instanceStream, instanceWriter);
        }

        private HtmlBufferResponseWriterWrapper(ResponseWriter initialWriter, ByteArrayOutputStream stream, PrintWriter writer){
            super(writer, initialWriter.getContentType(), initialWriter.getCharacterEncoding());
            this.stream = stream;
            this.writer = writer;
        }

        public String toString(){
            try{
                stream.flush();
                writer.close();

                return stream.toString( getCharacterEncoding() );
            }catch(UnsupportedEncodingException e){
                // an attempt to set an invalid character encoding would have caused this exception before
                throw new RuntimeException("ResponseWriter accepted invalid character encoding " + getCharacterEncoding());
            } catch (IOException e) {
                throw new RuntimeException( e );
            }
        }
    }
}