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
package org.apache.myfaces.custom.fileupload;

import org.apache.commons.fileupload.FileItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UploadedFileDefaultFileImpl extends UploadedFileDefaultImplBase
{
	private transient FileItem fileItem = null;

    /*
    TODO/manolito: Do we need an empty constructor?!
    public UploadedFileDefaultFileImpl()
    {
    }
    */


    public UploadedFileDefaultFileImpl(FileItem fileItem) throws IOException
    {
        super(fileItem.getName(), fileItem.getContentType());
    	this.fileItem = fileItem;
    }


    /**
     * Answer the uploaded file contents.
     *
     * @return file contents
     */
    public byte[] getBytes() throws IOException
    {
    	byte[] bytes = new byte[(int)getSize()];
        if (fileItem != null) fileItem.getInputStream().read(bytes);
        return bytes;
    }


    /**
     * Answer the uploaded file contents input stream
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException
    {
    	return fileItem != null
               ? fileItem.getInputStream()
               : new ByteArrayInputStream(new byte[0]);
    }


    /**
     * Answer the size of this file.
     * @return
     */
    public long getSize()
    {
    	return fileItem != null ? fileItem.getSize() : 0;
    }
}
