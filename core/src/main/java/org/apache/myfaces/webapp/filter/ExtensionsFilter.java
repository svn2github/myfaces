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
package org.apache.myfaces.webapp.filter;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filters is mandatory for the use of many components.
 * It handles the Multipart requests (for file upload)
 * It's used by the components that need javascript libraries
 *
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExtensionsFilter implements Filter {

    private Log log = LogFactory.getLog(ExtensionsFilter.class);

    private int _uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB

    private int _uploadThresholdSize = 1 * 1024 * 1024; // 1 MB

    private String _uploadRepositoryPath = null; //standard temp directory

    private ServletContext _servletContext;

    public static final String DOFILTER_CALLED = "org.apache.myfaces.component.html.util.ExtensionFilter.doFilterCalled";

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {

        String param = filterConfig.getInitParameter("uploadMaxFileSize");

        _uploadMaxFileSize = resolveSize(param, _uploadMaxFileSize);

        param = filterConfig.getInitParameter("uploadThresholdSize");

        _uploadThresholdSize = resolveSize(param, _uploadThresholdSize);

        _uploadRepositoryPath = filterConfig.getInitParameter("uploadRepositoryPath");

        _servletContext = filterConfig.getServletContext();
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

        HttpServletRequest extendedRequest = httpRequest;

        // For multipart/form-data requests
        if (FileUpload.isMultipartContent(httpRequest)) {
            extendedRequest = new MultipartRequestWrapper(httpRequest, _uploadMaxFileSize, _uploadThresholdSize, _uploadRepositoryPath);
        }

        // Serve resources
        AddResource addResource;

        try
        {
            addResource=AddResourceFactory.getInstance(httpRequest);
            if( addResource.isResourceUri( httpRequest ) ){
                addResource.serveResource(_servletContext, httpRequest, httpResponse);
                return;
            }
        }
        catch(Throwable th)
        {
            log.error("Exception wile retrieving addResource",th);
            throw new ServletException(th);
        }

        try
        {
        	addResource.responseStarted();
        	
	        if (addResource.requiresBuffer())
	        {
		        ExtensionsResponseWrapper extendedResponse = new ExtensionsResponseWrapper((HttpServletResponse) response);
		
		        // Standard request
		        chain.doFilter(extendedRequest, extendedResponse);
		
		        extendedResponse.finishResponse();
		
		        // write the javascript stuff for myfaces and headerInfo, if needed
		        HttpServletResponse servletResponse = (HttpServletResponse)response;
		
		        // only parse HTML responses
		        if (extendedResponse.getContentType() != null && isValidContentType(extendedResponse.getContentType()))
		        {
		            addResource.parseResponse(extendedRequest, extendedResponse.toString(),
		                    servletResponse);
		
		            addResource.writeMyFacesJavascriptBeforeBodyEnd(extendedRequest,
		                    servletResponse);
		
		            if( ! addResource.hasHeaderBeginInfos() ){
		                // writes the response if no header info is needed
		                addResource.writeResponse(extendedRequest, servletResponse);
		                return;
		            }
		
		            // Some headerInfo has to be added
		            addResource.writeWithFullHeader(extendedRequest, servletResponse);
		
		            // writes the response
		            addResource.writeResponse(extendedRequest, servletResponse);
		        }
		        else
		        {

		        	byte[] responseArray = extendedResponse.getBytes();

                    if(responseArray.length > 0)
                    {
 			        	// When not filtering due to not valid content-type, deliver the byte-array instead of a charset-converted string.
 			        	// Otherwise a binary stream gets corrupted.
 			            servletResponse.getOutputStream().write(responseArray);
 		        	}
                }
	        }
	        else
	        {
		        chain.doFilter(extendedRequest, response);
	        }
        }
        finally
        {
        	addResource.responseFinished();        	
        }
    }

    public boolean isValidContentType(String contentType)
    {
        return contentType.startsWith("text/html") ||
                contentType.startsWith("text/xml") ||
                contentType.startsWith("application/xhtml+xml") ||
                contentType.startsWith("application/xml");
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
        // NoOp
    }


}
