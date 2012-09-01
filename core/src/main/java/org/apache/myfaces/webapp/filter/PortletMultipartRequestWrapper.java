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
package org.apache.myfaces.webapp.filter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.webapp.filter.portlet.ActionRequestWrapper;
import org.apache.myfaces.webapp.filter.portlet.PortletChacheFileSizeErrorsFileUpload;

/**
 * @since 1.1.8
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PortletMultipartRequestWrapper
        extends ActionRequestWrapper
{
    private static Log log = LogFactory.getLog(PortletMultipartRequestWrapper.class);
    public static final String UPLOADED_FILES_ATTRIBUTE = "org.apache.myfaces.uploadedFiles";
    public static final String WWW_FORM_URLENCODED_TYPE = "application/x-www-form-urlencoded";

    ActionRequest request = null;
    HashMap parametersMap = null;
    PortletFileUpload fileUpload = null;
    HashMap fileItems = null;
    long maxFileSize;
    long maxSize;
    int thresholdSize;
    String repositoryPath;
    boolean cacheFileSizeErrors;

    public PortletMultipartRequestWrapper(Object request,
                                   int maxFileSize, int thresholdSize,
                                   String repositoryPath){
        super((ActionRequest) request );
        this.request = (ActionRequest) request;
        this.maxFileSize = maxFileSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
        //Default values
        this.maxSize = maxFileSize;
        this.cacheFileSizeErrors = false;
    }
    
    public PortletMultipartRequestWrapper(Object request,
            int maxFileSize, int thresholdSize,
            String repositoryPath, int maxRequestSize, boolean cacheFileSizeErrors){
        super((ActionRequest) request );
        this.request = (ActionRequest) request;
        this.maxFileSize = maxFileSize;
        this.maxSize = maxRequestSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
        this.cacheFileSizeErrors = cacheFileSizeErrors;
    }

        public PortletMultipartRequestWrapper(Object request,
                                   long maxFileSize, int thresholdSize,
                                   String repositoryPath){
        super((ActionRequest) request );
        this.request = (ActionRequest) request;
        this.maxFileSize = maxFileSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
        //Default values
        this.maxSize = maxFileSize;
        this.cacheFileSizeErrors = false;
    }
    
    public PortletMultipartRequestWrapper(Object request,
            long maxFileSize, int thresholdSize,
            String repositoryPath, long maxRequestSize, boolean cacheFileSizeErrors){
        super((ActionRequest) request );
        this.request = (ActionRequest) request;
        this.maxFileSize = maxFileSize;
        this.maxSize = maxRequestSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
        this.cacheFileSizeErrors = cacheFileSizeErrors;
    }

    private void parseRequest() {
        if (cacheFileSizeErrors)
        {
            fileUpload = new PortletChacheFileSizeErrorsFileUpload();
        }
        else
        {
            fileUpload = new PortletFileUpload();
        }

        fileUpload.setSizeMax(maxSize);
        fileUpload.setFileSizeMax(maxFileSize);

        //fileUpload.setSizeThreshold(thresholdSize);

        if(repositoryPath != null && repositoryPath.trim().length()>0)
        {
            //fileUpload.setRepositoryPath(repositoryPath);
            fileUpload.setFileItemFactory(
                    new DiskFileItemFactory(thresholdSize,
                            new File(repositoryPath)));
        }
        else
        {
            fileUpload.setFileItemFactory(
                    new DiskFileItemFactory(thresholdSize,
                            new File(System.getProperty("java.io.tmpdir"))));
        }

        String charset = request.getCharacterEncoding();
        fileUpload.setHeaderEncoding(charset);


        List requestParameters = null;
        
        try
        {
            if (cacheFileSizeErrors)
            {
                requestParameters = ((PortletChacheFileSizeErrorsFileUpload) fileUpload).
                    parseRequestCatchingFileSizeErrors(request, fileUpload);
            }
            else
            {
                requestParameters = fileUpload.parseRequest(request);
            }
        }
        catch (FileUploadBase.FileSizeLimitExceededException e)
        {
            // Since commons-fileupload does not allow to continue processing files
            // if this exception is thrown, we can't do anything else.
            // So, the current request is rejected and we can't restore state, so 
            // this request is dealt like a new request, but note that caching the params
            // below it is possible to detect if the current request has been aborted
            // or not.
            // Note that if cacheFileSizeErrors is true, this is not thrown, so the request
            // is not aborted unless other different error occur.
            request.setAttribute(
                    "org.apache.myfaces.custom.fileupload.exception","fileSizeLimitExceeded");
            request.setAttribute("org.apache.myfaces.custom.fileupload.maxSize",
                    new Integer((int)maxFileSize));
            
            if (log.isWarnEnabled())
                log.warn("FileSizeLimitExceededException while uploading file.", e);
            
            requestParameters = Collections.EMPTY_LIST;
        }
        catch (FileUploadBase.SizeLimitExceededException e)
        {
            // This exception is thrown when the max request size has been reached.
            // In this case, the current request is rejected. The current 
            // request is dealt like a new request, but note that caching the params below
            // params it is possible to detect if the current request has been aborted
            // or not.
            request.setAttribute(
                "org.apache.myfaces.custom.fileupload.exception","sizeLimitExceeded");
            request.setAttribute("org.apache.myfaces.custom.fileupload.maxSize",
                new Integer((int)maxSize));
            
            if (log.isWarnEnabled())
                log.warn("SizeLimitExceededException while uploading file.", e);
            
            requestParameters = Collections.EMPTY_LIST;
        }
        catch(FileUploadException fue)
        {
            if (log.isErrorEnabled())
                log.error("Exception while uploading file.", fue);
            
            requestParameters = Collections.EMPTY_LIST;
        }        

        parametersMap = new HashMap( requestParameters.size() );
        fileItems = new HashMap();

        for (Iterator iter = requestParameters.iterator(); iter.hasNext(); ){
            FileItem fileItem = (FileItem) iter.next();

            if (fileItem.isFormField()) {
                String name = fileItem.getFieldName();

                // The following code avoids commons-fileupload charset problem.
                // After fixing commons-fileupload, this code should be
                //
                //     String value = fileItem.getString();
                //
                String value = null;
                if ( charset == null) {
                    value = fileItem.getString();
                } else {
                    try {
                        value = new String(fileItem.get(), charset);
                    } catch (UnsupportedEncodingException e){
                        value = fileItem.getString();
                    }
                }

                addTextParameter(name, value);
            } else { // fileItem is a File
                if (fileItem.getName() != null) {
                    fileItems.put(fileItem.getFieldName(), fileItem);
                }
            }
        }

        //Add the query string paramters
        for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();

            Object value = entry.getValue();

            if(value instanceof String[])
            {
                String[] valuesArray = (String[])entry.getValue();
                for (int i = 0; i < valuesArray.length; i++)
                {
                    addTextParameter((String)entry.getKey(), valuesArray[i]);
                }
            }
            else if(value instanceof String)
            {
                String strValue = (String)entry.getValue();
                addTextParameter((String)entry.getKey(), strValue);
            }
            else if(value != null)
                throw new IllegalStateException("value of type : "+value.getClass()+" for key : "+
                        entry.getKey()+" cannot be handled.");

        }
    }

    private void addTextParameter(String name, String value){
        if( ! parametersMap.containsKey( name ) ){
            String[] valuesArray = {value};
            parametersMap.put(name, valuesArray);
        }else{
            String[] storedValues = (String[])parametersMap.get( name );
            int lengthSrc = storedValues.length;
            String[] valuesArray = new String[lengthSrc+1];
            System.arraycopy(storedValues, 0, valuesArray, 0, lengthSrc);
            valuesArray[lengthSrc] = value;
            parametersMap.put(name, valuesArray);
        }
    }

    public Enumeration getParameterNames() {
        if( parametersMap == null ) parseRequest();

        return Collections.enumeration( parametersMap.keySet() );
    }

    public String getParameter(String name) {
        if( parametersMap == null ) parseRequest();

        String[] values = (String[])parametersMap.get( name );
        if( values == null )
            return null;
        return values[0];
    }

    public String[] getParameterValues(String name) {
        if( parametersMap == null ) parseRequest();

        return (String[])parametersMap.get( name );
    }

    public Map getParameterMap() {
        if( parametersMap == null ) parseRequest();

        return parametersMap;
    }

    // Hook for the x:inputFileUpload tag.
    public FileItem getFileItem(String fieldName) {
        if( fileItems == null ) parseRequest();

        return (FileItem) fileItems.get( fieldName );
    }

    /**
     * Not used internally by MyFaces, but provides a way to handle the uploaded files
     * out of MyFaces.
     */
    public Map getFileItems(){
        if( fileItems == null ) parseRequest();
        return fileItems;
    }


    public Object getAttribute(String string) {
        if (string.equals(UPLOADED_FILES_ATTRIBUTE)) {
            return getFileItems();
        }
        return super.getAttribute(string);
    }
    
    public String getContentType()
    {
      return WWW_FORM_URLENCODED_TYPE;
    }     
}
