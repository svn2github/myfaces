package org.apache.myfaces.component.html.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.ResourceLoader;

public class StreamingResourceLoader implements ResourceLoader
{
	private final static Log log = LogFactory.getLog(StreamingResourceLoader.class);
	
	public StreamingResourceLoader()
	{
	}

	public void serveResource(ServletContext context, HttpServletRequest request, HttpServletResponse response, String resourceUri) throws IOException
	{
		int pos = resourceUri.indexOf("/");
		Long requestId = new Long(Long.parseLong(resourceUri.substring(0, pos), 10));
		
		StreamingAddResource.HeaderInfoEntry headerInfoEntry = StreamingAddResource.getHeaderInfo(requestId);
		if (headerInfoEntry == null)
		{
			log.warn("No streamable resources found for request: " + requestId + " resourceUri: " + resourceUri);
			return;
		}

		/*
		 * Even if we have a request id in our url, 
		 * ensure the browser didnt cache this response.
		 */
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache, must-revalidate");
		
		try
		{
			PrintWriter pw = response.getWriter();
			
			StreamingAddResource.StreamablePositionedInfo positionedInfo;
			try
			{
				while ((positionedInfo = headerInfoEntry.fetchInfo()) != null)
				{
					positionedInfo.writePositionedInfo(response, pw);
					pw.flush();
				}
				pw.close();
			}
			catch (InterruptedException e)
			{
				throw (IOException) new IOException().initCause(e);
			}
		}
		finally
		{
			StreamingAddResource.removeHeaderInfo(requestId);
		}
	}

}
