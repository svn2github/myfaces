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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;

/**
 * This is a utility class to render link to resources used by custom components.
 * Mostly used to avoid having to include <script src="..."></script>
 * in the head of the pages before using a component.
 *
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AddResource {
    protected static final Log log = LogFactory.getLog(AddResource.class);

    private static final String COMPONENTS_PACKAGE = "org.apache.myfaces.custom.";

    private static final String RESOURCE_VIRTUAL_PATH = "/faces/myFacesExtensionResource";

    private static final String ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME = "myFacesHeaderResource2Render";

    // Methodes to Add resources

    /**
     * Adds the given Javascript resource to the document body.
     */
    public static void addJavaScriptHere(Class componentClass, String resourceFileName, FacesContext context) throws IOException{
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
        writer.writeURIAttribute(HTML.SRC_ATTR,
                getResourceMappedPath(componentClass, resourceFileName, context),
                null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script is already has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(Class componentClass, String resourceFileName, FacesContext context){
        addJavaScriptToHeader(componentClass, resourceFileName, false, context);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script is already has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(Class componentClass, String resourceFileName, boolean defer, FacesContext context){
        AdditionalHeaderInfoToRender jsInfo =
            new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_JS, componentClass, resourceFileName, defer);
        addAdditionalHeaderInfoToRender(context, jsInfo );
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet is already has already been referenced, it's added only once.
     */
    public static void addStyleSheet(Class componentClass, String resourceFileName, FacesContext context){
        AdditionalHeaderInfoToRender cssInfo =
            new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_CSS, componentClass, resourceFileName);
        addAdditionalHeaderInfoToRender(context, cssInfo );
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet is already has already been referenced, it's added only once.
     */
    public static void addInlineStyleToHeader(String inlineStyle, FacesContext context){
        AdditionalHeaderInfoToRender cssInfo =
            new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_CSS_INLINE, inlineStyle);
        addAdditionalHeaderInfoToRender(context, cssInfo );
    }

    /**
     * Get the Path used to retrieve an internal resource for a custom component.
     * Example : You can use this to initialize javascript scripts so that they know the path of some other resources
     * (image, css, ...).
     * <code>
     * 	AddResource.addJavaScriptOncePerPage(HtmlCalendarRenderer.class, "popcalendar.js", facesContext,
     * 		"jscalendarSetImageDirectory("+AddResource.getResourceMappedPath(HtmlCalendarRenderer.class, "DB", facesContext)+")");
     * </code>
     *
     * Note : set context to null if you want the path after the application context path.
     */
    public static String getResourceMappedPath(Class componentClass, String resourceFileName, FacesContext context){
        HttpServletRequest request = null;
        if( context != null )
            request = (HttpServletRequest)context.getExternalContext().getRequest();

        return getResourceMappedPath(
                getComponentName(componentClass),
                resourceFileName,
                request);
    }

    protected static String getResourceMappedPath(String componentName, String resourceFileName, HttpServletRequest request){
        String contextPath = "";
        if( request != null )
            contextPath = request.getContextPath();
        return contextPath+RESOURCE_VIRTUAL_PATH+"/"+componentName+'/'+getCacheKey()+'/'+resourceFileName;
    }

	private static long getCacheKey(){
		// A 100 sec. delay between 2 deployement should be enough
		// and helps reduce the URL length.
		return getLastModified() / 100000; 
	}

	private static long lastModified = 0;
	private static long getLastModified(){
		if( lastModified == 0 ){
			final String format = "yyyy-MM-dd HH:mm:ss Z"; // Must match the one used in the build file
	        final String bundleName = AddResource.class.getName();
	        ResourceBundle resources = ResourceBundle.getBundle( bundleName );
			String sLastModified = resources.getString("lastModified");
			try {
				lastModified = new SimpleDateFormat(format).parse( sLastModified ).getTime();
			} catch (ParseException e) {
				lastModified = new Date().getTime();
				log.error("Unparsable lastModified : "+sLastModified);
			}
		}
		
		return lastModified; 
	}

    public static boolean isResourceMappedPath(HttpServletRequest request){
        return request.getRequestURI().indexOf( RESOURCE_VIRTUAL_PATH ) != -1;
    }

    /**
     * Decodes the path to return the requested componentName & resourceFileName
     * String[0] == componentName
     * String[1] == resourceFileName
     */
    private static String[] getResourceInfoFromPath(HttpServletRequest request){
        String uri = request.getRequestURI();
        String componentNameStartsAfter = RESOURCE_VIRTUAL_PATH+'/';

        int posStartComponentName = uri.indexOf( componentNameStartsAfter )+componentNameStartsAfter.length();
        int posEndComponentName = uri.indexOf("/", posStartComponentName);
        String componentName = uri.substring(posStartComponentName, posEndComponentName);

		// Skip cache key
		int posStartResourceFileName = uri.indexOf("/", posEndComponentName+1)+1;

        String resourceFileName = uri.substring(posStartResourceFileName);

        return new String[]{componentName, resourceFileName};
    }

    protected static String getComponentName(Class componentClass){
        String name = componentClass.getName();
        if( ! name.startsWith(COMPONENTS_PACKAGE) ){
            log.error("getComponentName called for non extension component : "+name+"\n"+
                    "For security reasons, only components member of the "+COMPONENTS_PACKAGE+" are allowed to add ressources.");
            return null;
        }

        name = name.substring( COMPONENTS_PACKAGE.length() );

        return name;
    }

    static Class getComponent(String componentName) throws ClassNotFoundException{
        return Class.forName( COMPONENTS_PACKAGE+componentName );
    }

    static private InputStream getResource(String componentName, String resourceFileName) {
        Class component;
        try {
            component = getComponent(componentName);
        } catch (ClassNotFoundException e) {
            log.error("Class not found for component "+componentName);
			return null;
        }
        while( resourceFileName.startsWith(".") || resourceFileName.startsWith("/") || resourceFileName.startsWith("\\") )
                resourceFileName = resourceFileName.substring(1);

        return component.getResourceAsStream( "resource/"+resourceFileName );
    }

    static public void serveResource(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String[] resourceInfo = getResourceInfoFromPath(request);
        String componentName = resourceInfo[0];
        String resourceFileName = resourceInfo[1];

        log.debug("Serving resource "+resourceFileName+" for component "+componentName);

        String lcResourceFileName = resourceFileName.toLowerCase();

        if( lcResourceFileName.endsWith(".js") )
            response.setContentType("text/javascript");
        else if( lcResourceFileName.endsWith(".css") )
            response.setContentType("text/css");
        else if( lcResourceFileName.endsWith(".gif") )
            response.setContentType("image/gif");
        else if( lcResourceFileName.endsWith(".png") )
            response.setContentType("image/png");
        else if( lcResourceFileName.endsWith(".jpg") || lcResourceFileName.endsWith(".jpeg") )
            response.setContentType("image/jpeg");
        else if( lcResourceFileName.endsWith(".xml")  || lcResourceFileName.endsWith(".xsl") )
            response.setContentType("text/xml"); // XSL has to be served as XML.

        InputStream is = getResource(componentName, resourceFileName);
        if( is == null ){
            throw new IOException("Unable to find resource "+resourceFileName+" for component "+componentName+
                    ". Check that this file is available in the classpath in sub-directory /resource of the component-directory.");
        }

		response.setDateHeader("Last-Modified", getLastModified());

		// Set browser cache to a week.
		// There is no risk, as the cache key is part of the URL.
		Calendar expires = Calendar.getInstance();
		expires.add(Calendar.DAY_OF_YEAR, 7);
		response.setDateHeader("Expires", expires.getTimeInMillis());

        OutputStream os = response.getOutputStream();
        int c;
        while ((c = is.read()) != -1)
            os.write(c);

        os.close();
    }

    // Header stuffs

    private static Set getAdditionalHeaderInfoToRender(HttpServletRequest request){
        Set set = (Set) request.getAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME);
        if( set == null ){
            set = new LinkedHashSet();
            request.setAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME, set);
        }

        return set;
    }

    private static void addAdditionalHeaderInfoToRender(FacesContext context, AdditionalHeaderInfoToRender info){
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set set = getAdditionalHeaderInfoToRender( request );
        set.add( info );
    }

    static public boolean hasAdditionalHeaderInfoToRender(HttpServletRequest request){
        return request.getAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME) != null;
    }

    static public void writeWithFullHeader(HttpServletRequest request,
            ExtensionsResponseWrapper responseWrapper,
            HttpServletResponse response) throws IOException{

        String originalResponse = responseWrapper.toString();

        // try the most common cases first
        boolean addHeaderTags = false;
        int insertPosition = originalResponse.indexOf( "</head>" );

        if( insertPosition < 0 ){
			insertPosition = originalResponse.indexOf( "</HEAD>" );

	        if( insertPosition < 0 ){
	            insertPosition = originalResponse.indexOf( "<body" );
	            addHeaderTags = true;

		        if( insertPosition < 0 ){
		        	insertPosition = originalResponse.indexOf( "<BODY" );
		            addHeaderTags = true;

			        if( insertPosition < 0 ){
			 	       // the two most common cases head/HEAD and body/BODY did not work, so we try it with lowercase
			           String lowerCase = originalResponse.toLowerCase(response.getLocale());
			           insertPosition = lowerCase.indexOf( "</head>" );

			           if( insertPosition < 0 ){
			    	       insertPosition = lowerCase.indexOf( "<body" );
			               addHeaderTags = true;
			           }
			        }
		        }
	        }

	        if( insertPosition < 0 ){
	            log.warn("Response has no <head> or <body> tag:\n"+originalResponse);
	            insertPosition = 0;
	        }
        }

        PrintWriter writer = response.getWriter();

        if( insertPosition > 0 )
            writer.write( originalResponse.substring(0, insertPosition) );
        if( addHeaderTags )
            writer.write("<head>");

        for(Iterator i = getAdditionalHeaderInfoToRender(request).iterator(); i.hasNext() ;){
            AdditionalHeaderInfoToRender headerInfo = (AdditionalHeaderInfoToRender) i.next();
            writer.write( headerInfo.getString(request) );
        }

        if( addHeaderTags )
            writer.write("</head>");

        writer.write( originalResponse.substring(insertPosition) );
    }

    private static class AdditionalHeaderInfoToRender{
        static final int TYPE_JS = 0;
        static final int TYPE_CSS = 1;
        static final int TYPE_CSS_INLINE = 2;

        public int type;
        public boolean deferJS = false;
        public String componentName;
        public String resourceFileName;
        public String inlineText;

        public AdditionalHeaderInfoToRender(int infoType, Class componentClass, String resourceFileName) {
            this.type = infoType;
            this.componentName = getComponentName(componentClass);
            this.resourceFileName = resourceFileName;
        }

        public AdditionalHeaderInfoToRender(int infoType, Class componentClass, String resourceFileName, boolean defer) {
            if( defer && infoType != TYPE_JS )
                log.error("Defer can only be used for scripts.");
            this.type = infoType;
            this.componentName = getComponentName(componentClass);
            this.resourceFileName = resourceFileName;
            this.deferJS = defer;
        }

        public AdditionalHeaderInfoToRender(int infoType, String inlineText) {
            if( infoType != TYPE_CSS_INLINE )
                log.error("This constructor only supports TYPE_CSS_INLINE");
            this.type = infoType;
            this.inlineText = inlineText;
        }

        public int hashCode() {
            return (componentName+((char)7)
                    +resourceFileName+((char)7)
                    +(type+""+((char)7))
                    +(inlineText+""+((char)7))
                    +(deferJS+"")).hashCode();
        }

        public boolean equals(Object obj) {
            if( !(obj instanceof AdditionalHeaderInfoToRender) )
                return false;
            AdditionalHeaderInfoToRender toCompare = (AdditionalHeaderInfoToRender) obj;

            if( type != toCompare.type || deferJS != toCompare.deferJS )
                return false;

            if( componentName == null ){
                if( toCompare.componentName != null )
                    return false;
            }else if( ! componentName.equals(toCompare.componentName) )
                return false;

            if( resourceFileName == null ){
                if( toCompare.resourceFileName != null )
                    return false;
            }else if( ! resourceFileName.equals(toCompare.resourceFileName) )
                return false;

            if( inlineText == null )
                return toCompare.inlineText == null;

            return inlineText.equals(toCompare.inlineText);
        }

        public String getString(HttpServletRequest request){
            switch (type) {
           case TYPE_JS:
                    return "<script "
                        +"src=\""+getResourceMappedPath(componentName, resourceFileName, request)+"\" "
                        +(deferJS ? "defer=\"true\" " : "")
                        +"type=\"text/javascript\""
                        +">"
                        +"</script>\n";
           case TYPE_CSS:
               return "<link rel=\"stylesheet\" "
               	+"href=\""+getResourceMappedPath(componentName, resourceFileName, request)+"\" "
               	+"type=\"text/css\"/>\n";
           case TYPE_CSS_INLINE:
               return "<style type=\"text/css\">"+inlineText+"</style>\n";
            default:
                log.warn("Unknown type:"+type);
                return "<link href=\""+"\"/>\n";
            }
        }
    }
}