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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;

/**
 * This filters is mandatory for the use of many components.
 * It handles the Multipart requests (for file upload)
 * It's used by the components that need javascript libraries
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExtensionsFilter implements Filter {

    private int uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB

    private int uploadThresholdSize = 1 * 1024 * 1024; // 1 MB

    private String uploadRepositoryPath = null; //standard temp directory
    private static final String DOFILTER_CALLED = "org.apache.myfaces.component.html.util.ExtensionFilter.doFilterCalled";

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {

        String param = filterConfig.getInitParameter("uploadMaxFileSize");

        uploadMaxFileSize = resolveSize(param, uploadMaxFileSize);

        param = filterConfig.getInitParameter("uploadThresholdSize");

        uploadThresholdSize = resolveSize(param, uploadThresholdSize);

        uploadRepositoryPath = filterConfig.getInitParameter("uploadRepositoryPath");
    }

    private int resolveSize(String param, int defaultValue) {
        int numberParam = defaultValue;

        if (param != null) {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("g")) {
                factor = 1024 * 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("m")) {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("k")) {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            numberParam = Integer.parseInt(number) * factor;
        }
        return numberParam;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if(request.getAttribute(DOFILTER_CALLED)!=null)
        {
            chain.doFilter(request, response);
            return;
        }

        request.setAttribute(DOFILTER_CALLED,"true");

        if (!(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

		HttpServletResponse httpResponse = (HttpServletResponse) response; 
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Serve resources
        AddResource addResource = AddResource.getInstance(httpRequest);
        if( addResource.isResourceUri( httpRequest ) ){
            addResource.serveResource(httpRequest, httpResponse);
            return;
        }
        
        HttpServletRequest extendedRequest = httpRequest;
        
        // For multipart/form-data requests
        if (FileUpload.isMultipartContent(httpRequest)) {
            extendedRequest = new MultipartRequestWrapper(httpRequest, uploadMaxFileSize, uploadThresholdSize, uploadRepositoryPath);
        }
        
        ExtensionsResponseWrapper extendedResponse = new ExtensionsResponseWrapper((HttpServletResponse) response);
        
        // Standard request
        chain.doFilter(extendedRequest, extendedResponse);
        
        extendedResponse.finishResponse();
        
        if( ! addResource.hasHeaderInfos(extendedRequest) ){
            response.getOutputStream().write( extendedResponse.getBytes());
            return;
        }
        
        // Some headerInfo has to be added
        addResource.writeWithFullHeader(extendedRequest, extendedResponse, (HttpServletResponse)response);
    }
    
    /**
     * Destroy method for this filter
     */
    public void destroy() {
		// NoOp
    }
}
