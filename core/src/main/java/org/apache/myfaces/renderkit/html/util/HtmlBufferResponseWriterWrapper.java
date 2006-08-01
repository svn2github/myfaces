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
package org.apache.myfaces.renderkit.html.util;

import javax.faces.context.ResponseWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

/**
 * @author Sylvain Vieujot (latest modification by $Author: grantsmith $)
 * @version $Revision: 169649 $ $Date: 2005-05-11 17:47:12 +0200 (Wed, 11 May 2005) $
 */
public class HtmlBufferResponseWriterWrapper extends org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl {

    private ByteArrayOutputStream stream;
    private PrintWriter writer;
    private ResponseWriter initialWriter;

    public ResponseWriter getInitialWriter() {
        return initialWriter;
    }

    static public HtmlBufferResponseWriterWrapper getInstance(ResponseWriter initialWriter){
        ByteArrayOutputStream instanceStream = new ByteArrayOutputStream();
        PrintWriter instanceWriter = new PrintWriter(instanceStream, true);

        return new HtmlBufferResponseWriterWrapper(initialWriter, instanceStream, instanceWriter);
    }

    private HtmlBufferResponseWriterWrapper(ResponseWriter initialWriter, ByteArrayOutputStream stream, PrintWriter writer){
        super(writer, initialWriter==null?null:initialWriter.getContentType(), initialWriter==null?null:initialWriter.getCharacterEncoding());
        this.stream = stream;
        this.writer = writer;
        this.initialWriter = initialWriter;
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
