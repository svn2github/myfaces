/*
 /*
 * Copyright 2005 The Apache Software Foundation.
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
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class MyFacesResourceLoader implements ResourceLoader
{
    protected static final Log log = LogFactory.getLog(MyFacesResourceLoader.class);

    static final String ORG_APACHE_MYFACES_CUSTOM = "org.apache.myfaces.custom";

    private static long lastModified = 0;

    private static long getLastModified()
    {
        if (lastModified == 0)
        {
            final String format = "yyyy-MM-dd HH:mm:ss Z"; // Must match the one used in the build file
            final String bundleName = AddResource.class.getName();
            ResourceBundle resources = ResourceBundle.getBundle(bundleName);
            String sLastModified = resources.getString("lastModified");
            try
            {
                lastModified = new SimpleDateFormat(format).parse(sLastModified).getTime();
            }
            catch (ParseException e)
            {
                lastModified = new Date().getTime();
                log.error("Unparsable lastModified : " + sLastModified);
            }
        }

        return lastModified;
    }

    /**
     * @see org.apache.myfaces.component.html.util.ResourceLoader#serveResource(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public void serveResource(HttpServletRequest request, HttpServletResponse response,
            String resourceUri) throws IOException
    {
        String[] uriParts = resourceUri.split("/", 2);

        String component = uriParts[0];
        if (component == null || component.trim().length() == 0)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
            log.error("Could not find parameter for component to load a resource.");
            return;
        }
        Class componentClass;
        String className = ORG_APACHE_MYFACES_CUSTOM + "." + component;
        try
        {
            componentClass = loadComponentClass(className);
        }
        catch (ClassNotFoundException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            log.error("Could not find the class for component " + className
                    + " to load a resource.");
            return;
        }
        String resource = uriParts[1];
        if (resource == null || resource.trim().length() == 0)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No resource defined");
            log.error("No resource defined component class " + className);
            return;
        }
        resource = "resource/" + resource;

        InputStream is = componentClass.getResourceAsStream(resource);
        if (is == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to find resource "
                    + resource + " for component " + component
                    + ". Check that this file is available " + "in the classpath in sub-directory "
                    + "/resource of the package-directory.");
            log.error("Unable to find resource " + resource + " for component " + component
                    + ". Check that this file is available " + "in the classpath in sub-directory "
                    + "/resource of the package-directory.");
        }
        else
        {
            defineContentHeaders(request, response, resource);
            defineCaching(request, response, resource);
            writeResource(request, response, is);
        }
    }

    /**
     * @param request
     * @param response
     * @param in
     */
    protected void writeResource(HttpServletRequest request, HttpServletResponse response,
            InputStream in) throws IOException
    {
        ServletOutputStream out = response.getOutputStream();
        try
        {
            byte[] buffer = new byte[1024];
            for (int size = in.read(buffer); size != -1; size = in.read(buffer))
            {
                out.write(buffer, 0, size);
            }
        }
        finally
        {
            out.close();
        }
    }

    protected void defineCaching(HttpServletRequest request, HttpServletResponse response,
            String resource)
    {
        response.setDateHeader("Last-Modified", getLastModified());

        // Set browser cache to a week.
        // There is no risk, as the cache key is part of the URL.
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.DAY_OF_YEAR, 7);
        response.setDateHeader("Expires", expires.getTimeInMillis());
    }

    protected void defineContentHeaders(HttpServletRequest request, HttpServletResponse response,
            String resource)
    {
        if (resource.endsWith(".js"))
            response.setContentType(HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT);
        else if (resource.endsWith(".css"))
            response.setContentType(HTML.STYLE_TYPE_TEXT_CSS);
        else if (resource.endsWith(".gif"))
            response.setContentType("image/gif");
        else if (resource.endsWith(".png"))
            response.setContentType("image/png");
        else if (resource.endsWith(".jpg") || resource.endsWith(".jpeg"))
            response.setContentType("image/jpeg");
        else if (resource.endsWith(".xml") || resource.endsWith(".xsl"))
            response.setContentType("text/xml"); // XSL has to be served as XML.
    }

    protected Class loadComponentClass(String componentClass) throws ClassNotFoundException
    {
        return Thread.currentThread().getContextClassLoader().loadClass(componentClass);
    }

    protected void validateCustomComponent(Class myfacesCustomComponent)
    {
        if (!myfacesCustomComponent.getName().startsWith(ORG_APACHE_MYFACES_CUSTOM + "."))
        {
            throw new IllegalArgumentException(
                    "expected a myfaces custom component class in package "
                            + ORG_APACHE_MYFACES_CUSTOM);
        }
    }
}