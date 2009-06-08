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
package org.apache.myfaces.webapp.filter.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.apache.commons.fileupload.util.Streams;

/**
 * Custom implementation of ServletFileUpload intended to parse request but it
 * catch and swallow FileSizeLimitExceededExceptions in order to return as
 * many usable items as possible.
 * 
 * <p>
 * NOTE: This class should be used(instantiated) only by 
 * ServletMultipartRequestWrapper. By that reason, it could be changed
 * or removed in the future.
 * </p>
 * 
 * @since 1.1.9
 * @author Phillip Webb
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 703744 $ $Date: 2008-10-11 17:28:20 -0500 (Sat, 11 Oct 2008) $
 *
 */
public class ServletChacheFileSizeErrorsFileUpload extends ServletFileUpload
{
   
    public ServletChacheFileSizeErrorsFileUpload()
    {
        super();
    }
    
    public ServletChacheFileSizeErrorsFileUpload(FileItemFactory fileItemFactory)
    {
        super(fileItemFactory);
    }
        
    /**
     * Determine the length of an uploaded file as indicated by the header.
     * 
     * @param pHeaders
     * @return length or -1
     */
    private long getContentLength(FileItemHeaders pHeaders) {
        try {
            return Long.parseLong(pHeaders.getHeader(FileUploadBase.CONTENT_LENGTH));
        } catch (Exception e) {
            return -1;
        }
    }    

    /**
     * Similar to {@link ServletFileUpload#parseRequest(RequestContext)} but will
     * catch and swallow FileSizeLimitExceededExceptions in order to return as
     * many usable items as possible.
     * 
     * @param fileUpload
     * @return List of {@link FileItem} excluding any that exceed max size.  
     * @throws FileUploadException
     */
    public List parseRequestCatchingFileSizeErrors(HttpServletRequest request, FileUpload fileUpload)
            throws FileUploadException
    {
        try
        {
            List items = new ArrayList();
            
            // The line below throws a SizeLimitExceededException (wrapped by a
            // FileUploadIOException) if the request is longer than the max size
            // allowed by fileupload requests (FileUpload.getSizeMax)
            // But note that if the request does not send proper headers this check
            // just will not do anything and we still have to check it again.
            FileItemIterator iter = fileUpload
                    .getItemIterator(new ServletRequestContext(request));

            FileItemFactory fac = fileUpload.getFileItemFactory();
            if (fac == null)
            {
                throw new NullPointerException(
                        "No FileItemFactory has been set.");
            }
            
            long maxFileSize = this.getFileSizeMax();
            long maxSize = this.getSizeMax();
            boolean checkMaxSize = false;
            
            if (maxFileSize == -1L)
            {
                //The max allowed file size should be approximate to the maxSize
                maxFileSize = maxSize;
            }
            if (maxSize != -1L)
            {
                checkMaxSize = true;
            }
            
            while (iter.hasNext())
            {
                final FileItemStream item = iter.next();
                FileItem fileItem = fac.createItem(item.getFieldName(), item
                        .getContentType(), item.isFormField(), item.getName());

                long allowedLimit = 0L;
                try
                {
                    if (maxFileSize != -1L || checkMaxSize)
                    {
                        if (checkMaxSize)
                        {
                            allowedLimit = maxSize > maxFileSize ? maxFileSize : maxSize;
                        }
                        else
                        {
                            //Just put the limit
                            allowedLimit = maxFileSize;
                        }
                        
                        long contentLength = getContentLength(item.getHeaders());

                        //If we have a content length in the header we can use it
                        if (contentLength != -1L && contentLength > allowedLimit)
                        {
                            throw new FileUploadIOException(
                                    new FileSizeLimitExceededException(
                                            "The field "
                                                    + item.getFieldName()
                                                    + " exceeds its maximum permitted "
                                                    + " size of " + allowedLimit
                                                    + " characters.",
                                            contentLength, allowedLimit));
                        }

                        //Otherwise we must limit the input as it arrives (NOTE: we cannot rely
                        //on commons upload to throw this exception as it will close the 
                        //underlying stream
                        final InputStream itemInputStream = item.openStream();
                        
                        InputStream limitedInputStream = new LimitedInputStream(
                                itemInputStream, allowedLimit)
                        {
                            protected void raiseError(long pSizeMax, long pCount)
                                    throws IOException
                            {
                                throw new FileUploadIOException(
                                        new FileSizeLimitExceededException(
                                                "The field "
                                                        + item.getFieldName()
                                                        + " exceeds its maximum permitted "
                                                        + " size of "
                                                        + pSizeMax
                                                        + " characters.",
                                                pCount, pSizeMax));
                            }
                        };

                        //Copy from the limited stream
                        long bytesCopied = Streams.copy(limitedInputStream, fileItem
                                .getOutputStream(), true);
                        
                        // Decrement the bytesCopied values from maxSize, so the next file copied 
                        // takes into account this value when allowedLimit var is calculated
                        // Note the invariant before the line is maxSize >= bytesCopied, since if this
                        // is not true a FileUploadIOException is thrown first.
                        maxSize -= bytesCopied;
                    }
                    else
                    {
                        //We can just copy the data
                        Streams.copy(item.openStream(), fileItem
                                .getOutputStream(), true);
                    }
                }
                catch (FileUploadIOException e)
                {
                    try
                    {
                        throw (FileUploadException) e.getCause();
                    }
                    catch (FileUploadBase.FileSizeLimitExceededException se)
                    {
                        request
                                .setAttribute(
                                        "org.apache.myfaces.custom.fileupload.exception",
                                        "fileSizeLimitExceeded");
                        String fieldName = fileItem.getFieldName();
                        request.setAttribute(
                                "org.apache.myfaces.custom.fileupload."+fieldName+".maxSize",
                                new Integer((int)allowedLimit));
                    }
                }
                catch (IOException e)
                {
                    throw new IOFileUploadException("Processing of "
                            + FileUploadBase.MULTIPART_FORM_DATA
                            + " request failed. " + e.getMessage(), e);
                }
                if (fileItem instanceof FileItemHeadersSupport)
                {
                    final FileItemHeaders fih = item.getHeaders();
                    ((FileItemHeadersSupport) fileItem).setHeaders(fih);
                }
                if (fileItem != null)
                {
                    items.add(fileItem);
                }
            }
            return items;
        }
        catch (FileUploadIOException e)
        {
            throw (FileUploadException) e.getCause();
        }
        catch (IOException e)
        {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
