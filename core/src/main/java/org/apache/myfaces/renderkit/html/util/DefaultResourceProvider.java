package org.apache.myfaces.renderkit.html.util;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * A class which provide the resource using the standard <code>class.getResource</code> lookup
 * stuff.
 *
 * @author imario (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DefaultResourceProvider implements ResourceProvider
{
	private final Class clazz;

	public DefaultResourceProvider(Class clazz)
	{
		this.clazz = clazz;
	}

	protected URL getResource(String resource)
	{
		resource = "resource/" + resource;
		return clazz.getResource(resource);
	}

	public boolean exists(ServletContext context, String resource)
	{
		return getResource(resource) != null;
	}

	public long getLastModified(ServletContext context, String resource) throws IOException
	{
		return getResource(resource).openConnection().getLastModified();
	}

	public int getContentLength(ServletContext context, String resource) throws IOException
	{
		return getResource(resource).openConnection().getContentLength();
	}

	public InputStream getInputStream(ServletContext context, String resource) throws IOException
	{
		return getResource(resource).openConnection().getInputStream();
	}

	public String getEncoding(ServletContext context, String resource) throws IOException
	{
		// cant be determined ... use default
		try
		{
			return System.getProperty("file.encoding");
		}
		catch (SecurityException e)
		{
			// not allowed
			return null;
		}
	}
}
