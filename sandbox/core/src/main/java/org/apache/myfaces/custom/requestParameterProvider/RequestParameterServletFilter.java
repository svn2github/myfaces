package org.apache.myfaces.custom.requestParameterProvider;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Thomas Obereder
 * @version 30.04.2006 14:38:23
 * 
 * Once moved to tomahawk, we can get rid of this filter and add its functionality to the default ExtensionsFilter.
 */
public class RequestParameterServletFilter implements Filter
{
	public final static String REQUEST_PARAM_FILTER_CALLED = RequestParameterServletFilter.class.getName() + ".CALLED";
	
    public RequestParameterServletFilter()
    {
    }

    public void init(FilterConfig filterConfig)
    {
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException
    {
        if(servletResponse instanceof HttpServletResponse)
        {
        	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    		if (!Boolean.TRUE.equals(httpServletRequest.getAttribute(REQUEST_PARAM_FILTER_CALLED)))
    		{
    			httpServletRequest.setAttribute(REQUEST_PARAM_FILTER_CALLED, Boolean.TRUE);
    			servletResponse = new RequestParameterResponseWrapper((HttpServletResponse) servletResponse);
    		}
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy()
    {
    }
}
