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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HtmlResponseWriterImpl;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This is a utility class to render link to resources used by custom components.
 * Mostly used to avoid having to include <script src="..."></script>
 * in the head of the pages before using a component.
 *
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class AddResource {
    protected static final Log log = LogFactory.getLog(AddResource.class);

    private static final String COMPONENTS_PACKAGE = "org.apache.myfaces.custom.";

    private static final String RESOURCE_VIRTUAL_PATH = "/faces/myFacesExtensionResource";

    private static final String ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME = "myFacesHeaderResource2Render";

    private static StringBuilder ADDITIONAL_JAVASCRIPT_TO_BODY_TAG = null;

    private static Set registeredClasses = new HashSet();

    private AddResource() {
        //no object creation allowed (util clazz)
    }

    // Methodes to Add resources

    /**
     * Adds the given Javascript resource to the document body.
     */
    public static void addJavaScriptHere(Class componentClass, String resourceFileName, FacesContext context, UIComponent component) throws IOException{

        addJavaScriptHere(componentClass, null, resourceFileName, context, component);

    }

    public static void addJavaScriptHere(
            Class componentClass, String baseDirectory, String resourceFileName, FacesContext context, UIComponent component)
            throws IOException
    {

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);

        if(baseDirectory != null)
        {
            writer.writeURIAttribute(HTML.SRC_ATTR,
                    getResourceBasePath(baseDirectory)+resourceFileName, null);
        }
        else
        {
            writer.writeURIAttribute(HTML.SRC_ATTR,
                    getResourceMappedPath(componentClass, resourceFileName, context),
                    null);
        }

        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Adds the given Javascript resource to the document body, without passing UIComponent.
     */
    public static void addJavaScriptHere(Class componentClass, String resourceFileName, FacesContext context) throws IOException{
        addJavaScriptHere(componentClass, resourceFileName,context,null);
    }

    /**
     * Adds the given Javascript resource to the document body, without passing UIComponent.
     */
    public static void addJavaScriptHere(String baseDirectory, String resourceFileName, FacesContext context) throws IOException{
        addJavaScriptHere(null, baseDirectory, resourceFileName,context,null);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(Class componentClass, String resourceFileName, FacesContext context){
        addJavaScriptToHeader(componentClass, resourceFileName, false, context);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(Class componentClass, String resourceFileName, boolean defer, FacesContext context){
        addJavaScriptToHeader(componentClass, null, resourceFileName, defer, context);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(String baseDirectory, String resourceFileName, FacesContext context){
        addJavaScriptToHeader(baseDirectory, resourceFileName, false, context);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(String baseDirectory, String resourceFileName, boolean defer, FacesContext context){
        addJavaScriptToHeader(null, baseDirectory, resourceFileName, defer, context);
    }

    /**
     * Adds the given Javascript resource to the document Header.
     * If the script has already been referenced, it's added only once.
     */
    public static void addJavaScriptToHeader(Class componentClass,
                                             String baseDirectory, String resourceFileName, boolean defer, FacesContext context){
        AdditionalHeaderInfoToRender jsInfo = null;
        
        if(baseDirectory!=null)
        {
            jsInfo = new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_JS, baseDirectory, resourceFileName, defer);
        }
        else
        {
            jsInfo = new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_JS, componentClass, resourceFileName, defer);
        }

        addAdditionalHeaderInfoToRender(context, jsInfo );
    }

    public static void addJavaScriptToBodyTag(String JavaScriptEventName, String addedJavaScript)
    {
        if (ADDITIONAL_JAVASCRIPT_TO_BODY_TAG == null)
        {
            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG = new StringBuilder();

            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG.append(" ").append(JavaScriptEventName).append("=\"").append(addedJavaScript);
        }
        else
        {
            ADDITIONAL_JAVASCRIPT_TO_BODY_TAG.append(addedJavaScript);
        }
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public static void addStyleSheet(Class componentClass, String resourceFileName, FacesContext context){
        addStyleSheet(componentClass, null, resourceFileName, context);
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public static void addStyleSheet(String baseDirectory, String resourceFileName, FacesContext context){
        addStyleSheet(null, baseDirectory, resourceFileName, context);
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public static void addStyleSheet(Class componentClass,
                                     String baseDirectory, String resourceFileName, FacesContext context){

        AdditionalHeaderInfoToRender cssInfo = null;
        if(baseDirectory!=null && baseDirectory.trim().length()>0)
        {
            cssInfo = new AdditionalHeaderInfoToRender(
                    AdditionalHeaderInfoToRender.TYPE_CSS, baseDirectory, resourceFileName);
        }
        else
        {
            cssInfo = new AdditionalHeaderInfoToRender(
                    AdditionalHeaderInfoToRender.TYPE_CSS, componentClass, resourceFileName);
        }
        addAdditionalHeaderInfoToRender(context, cssInfo );
    }

    /**
     * Adds the given Style Sheet to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public static void addInlineStyleToHeader(String inlineStyle, FacesContext context){
        AdditionalHeaderInfoToRender cssInfo =
            new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_CSS_INLINE, inlineStyle);
        addAdditionalHeaderInfoToRender(context, cssInfo );
    }

    /**
     * Adds the given Script to the document Header.
     * If the style sheet has already been referenced, it's added only once.
     */
    public static void addInlineScriptToHeader(String inlineScript, FacesContext context){
        AdditionalHeaderInfoToRender scriptInfo =
            new AdditionalHeaderInfoToRender(AdditionalHeaderInfoToRender.TYPE_JS_INLINE, inlineScript);
        addAdditionalHeaderInfoToRender(context, scriptInfo );
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
        String contextPath = null;
        if( context != null ){
            contextPath = context.getExternalContext().getRequestContextPath();
        }

        return getResourceMappedPath(
                getValidatedClassName(componentClass),
                resourceFileName,
                contextPath);
    }

    public static String getResourceBasePath(String baseDirectory)
    {
        return baseDirectory+(baseDirectory.endsWith("/")?"":"/");
    }

    public static String getResourceBasePath(Class componentClass, FacesContext context){
        String contextPath = null;
        if( context != null ){
            contextPath = context.getExternalContext().getRequestContextPath();
        }

        return getResourceBasePath(
                getValidatedClassName(componentClass),
                contextPath);
    }

    public static String getResourceBasePath(String componentName, String contextPath)
    {
        String returnString = RESOURCE_VIRTUAL_PATH+"/"+componentName+'/'+getCacheKey();
        return (contextPath == null) ? returnString : contextPath + returnString;
    }

    protected static String getResourceMappedPath(String componentName, String resourceFileName, String contextPath){
       return getResourceBasePath(componentName,contextPath)+'/'
               +resourceFileName;
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
        String classNameStartsAfter = RESOURCE_VIRTUAL_PATH+'/';

        int posStartClassName = uri.indexOf( classNameStartsAfter )+classNameStartsAfter.length();
        int posEndClassName = uri.indexOf("/", posStartClassName);
        String className = uri.substring(posStartClassName, posEndClassName);

        // Skip cache key
        int posStartResourceFileName = uri.indexOf("/", posEndClassName+1)+1;

        String resourceFileName = uri.substring(posStartResourceFileName);

        return new String[]{className, resourceFileName};
    }

    /**
     * Only classes of package org.apache.myfaces.custom 
     * or registered classes are allowed to load resources.
     *  
     * @param clazz the class to register
     */
    public static void registerAccess(Class clazz)
    {
        registeredClasses.add(clazz);
    }

    private static String getValidatedClassName(Class componentClass)
    {
        validateClass(componentClass);
        return componentClass.getName();
    }

    private static void validateClass(Class componentClass)
    {
        String name = componentClass.getName();
        if (!(registeredClasses.contains(componentClass) || name.startsWith(COMPONENTS_PACKAGE)))
        {
            throw new FacesException(
                    "For security reasons, only components member of the "
                            + COMPONENTS_PACKAGE
                            + " or registered classes (use AddResource.registerAccess) are allowed to add resources.");
        }
    }

    static private InputStream getResource(String className, String resourceFileName) {
        Class clazz;
        try {
            clazz = getClass(className);
        } catch (ClassNotFoundException e) {
            log.error("Class not found for component "+className);
            return null;
        }
        while( resourceFileName.startsWith(".") || resourceFileName.startsWith("/") || resourceFileName.startsWith("\\") )
                resourceFileName = resourceFileName.substring(1);

        return clazz.getResourceAsStream( "resource/"+resourceFileName );
    }

    private static Class getClass(String className) throws ClassNotFoundException
    {
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        validateClass(clazz);
        return clazz;
    }

    static public void serveResource(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String[] resourceInfo = getResourceInfoFromPath(request);
        String className = resourceInfo[0];
        String resourceFileName = resourceInfo[1];

        log.debug("Serving resource "+resourceFileName+" for package "+className);

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

        InputStream is = getResource(className, resourceFileName);
        if( is == null ){
            throw new IOException("Unable to find resource "+resourceFileName+" for resource "+className+
                    ". Check that this file is available in the classpath in sub-directory /resource of the package-directory.");
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

        //todo: fix this to work in PortletRequest as well
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set set = getAdditionalHeaderInfoToRender( request );
        set.add( info );
    }

    static public boolean hasAdditionalHeaderInfoToRender(HttpServletRequest request){
        return request.getAttribute(ADDITIONAL_HEADER_INFO_REQUEST_ATTRUBITE_NAME) != null;
    }

    /**
     * Add the resources to the &lt;head&gt; of the page.
     * If the head tag is missing, but the &lt;body&gt; tag is present, the head tag is added.
     * If both are missing, no resources is added.
     * 
     * TODO : Change the ordering so that the user header CSS & JS override MyFaces' ones.
     */
    static public void writeWithFullHeader(HttpServletRequest request,
                                           ExtensionsResponseWrapper responseWrapper,
                                           HttpServletResponse response) throws IOException{

        StringBuffer originalResponse = new StringBuffer(responseWrapper.toString());

        ParseCallbackListener l = new ParseCallbackListener();
        ReducedHTMLParser.parse(originalResponse, l);

        int headerInsertPosition = l.getHeaderInsertPosition();
        int bodyInsertPosition = l.getBodyInsertPosition();
        int beforeBodyPosition = l.getBeforeBodyPosition();
        boolean addHeaderTags = false;

        if(headerInsertPosition == -1)
        {
            if(beforeBodyPosition!=-1)
            {
                addHeaderTags = true;
                headerInsertPosition=beforeBodyPosition;
            }
            else
            {
                log.warn("Response has no <head> or <body> tag:\n"+originalResponse);
            }
        }

        PrintWriter writer = response.getWriter();

        if( headerInsertPosition > 0 )
            writer.write( originalResponse.substring(0, headerInsertPosition) );
        if(headerInsertPosition >=0 && addHeaderTags )
            writer.write("<head>");

        if(headerInsertPosition >= 0)
        {
            for(Iterator i = getAdditionalHeaderInfoToRender(request).iterator(); i.hasNext() ;){
                AdditionalHeaderInfoToRender headerInfo = (AdditionalHeaderInfoToRender) i.next();
                headerInfo.writeString(request.getContextPath(),writer,
                        HtmlRendererUtils.selectContentType(request.getHeader("accept")),null);
            }
        }

        if(headerInsertPosition >=0 && addHeaderTags){}
            writer.write("</head>");

        if (bodyInsertPosition > 0)
        {
            if (ADDITIONAL_JAVASCRIPT_TO_BODY_TAG != null)
            {
                originalResponse.insert( bodyInsertPosition + 5, ADDITIONAL_JAVASCRIPT_TO_BODY_TAG + "\"" );
            }
        }

        writer.write( headerInsertPosition > 0 ?
                originalResponse.substring(headerInsertPosition) : originalResponse.toString());
    }

    private static class AdditionalHeaderInfoToRender{
        static final int TYPE_JS = 0;
        static final int TYPE_CSS = 1;
        static final int TYPE_CSS_INLINE = 2;
        static final int TYPE_JS_INLINE = 3;

        public int type;
        public boolean deferJS = false;
        public String componentName;
        public String baseDirectory;
        public String resourceFileName;
        public String inlineText;

        public AdditionalHeaderInfoToRender(int infoType, String baseDirectory, String resourceFileName) {
            this.type = infoType;
            this.baseDirectory = baseDirectory;
            this.resourceFileName = resourceFileName;
        }

        public AdditionalHeaderInfoToRender(int infoType, String baseDirectory, String resourceFileName, boolean defer) {
            if( defer && infoType != TYPE_JS )
                log.error("Defer can only be used for scripts.");
            this.type = infoType;
            this.baseDirectory = baseDirectory;
            this.resourceFileName = resourceFileName;
            this.deferJS = defer;
        }

        public AdditionalHeaderInfoToRender(int infoType, Class componentClass, String resourceFileName) {
            this.type = infoType;
            this.componentName = getValidatedClassName(componentClass);
            this.resourceFileName = resourceFileName;
        }

        public AdditionalHeaderInfoToRender(int infoType, Class componentClass, String resourceFileName, boolean defer) {
            if( defer && infoType != TYPE_JS )
                log.error("Defer can only be used for scripts.");
            this.type = infoType;
            this.componentName = getValidatedClassName(componentClass);
            this.resourceFileName = resourceFileName;
            this.deferJS = defer;
        }

        public AdditionalHeaderInfoToRender(int infoType, String inlineText) {
            if( infoType != TYPE_CSS_INLINE  && infoType != TYPE_JS_INLINE)
                log.error("This constructor only supports TYPE_CSS_INLINE or TYPE_JS_INLINE");
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

        public void writeString(String contextPath, Writer writer, String contentType,
                                String characterEncoding) throws IOException
        {

            HtmlResponseWriterImpl responseWriter = new HtmlResponseWriterImpl(
                    writer,contentType,characterEncoding);

            responseWriter.writeText("\n",null);

            switch (type) {
                case TYPE_JS:
                    responseWriter.startElement(HTML.SCRIPT_ELEM,null);

                    if(baseDirectory != null)
                    {
                        responseWriter.writeAttribute(HTML.SRC_ATTR,
                            getResourceBasePath(baseDirectory)+resourceFileName,
                            null);
                    }
                    else
                    {
                        responseWriter.writeAttribute(HTML.SRC_ATTR,
                            getResourceMappedPath(componentName, resourceFileName, contextPath),null);
                    }

                    if(deferJS)
                        responseWriter.writeAttribute("defer","true",null);
                    responseWriter.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
                    responseWriter.endElement(HTML.SCRIPT_ELEM);
                    break;
                case TYPE_CSS:
                    responseWriter.startElement("link",null);
                    responseWriter.writeAttribute("rel","stylesheet",null);

                    if(baseDirectory != null )
                    {
                        responseWriter.writeAttribute(
                                HTML.HREF_ATTR,
                                baseDirectory+(baseDirectory.endsWith("/")?"":"/")+resourceFileName,
                                null);
                    }
                    else
                    {
                        responseWriter.writeAttribute(HTML.HREF_ATTR,
                            getResourceMappedPath(componentName, resourceFileName, contextPath),null);
                    }

                    responseWriter.writeAttribute(HTML.TYPE_ATTR,"text/css",null);
                    responseWriter.endElement("link");
                    break;

                case TYPE_CSS_INLINE:
                    responseWriter.startElement(HTML.STYLE_ELEM,null);
                    responseWriter.writeAttribute("rel","stylesheet",null);
                    responseWriter.writeAttribute(HTML.TYPE_ATTR,"text/css",null);
                    responseWriter.writeText(inlineText,null);
                    responseWriter.endElement(HTML.STYLE_ELEM);
                    break;
                case TYPE_JS_INLINE:
                    responseWriter.startElement(HTML.SCRIPT_ELEM,null);
                    responseWriter.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
                    responseWriter.writeText(inlineText,null);
                    responseWriter.endElement(HTML.SCRIPT_ELEM);
                    break;
                default:
                    log.warn("Unknown type:"+type);
            }
        }
    }

    private static class ParseCallbackListener implements CallbackListener
    {
        private int headerInsertPosition=-1;
        private int bodyInsertPosition=-1;
        private int beforeBodyPosition=-1;

        public void openedStartTag(int charIndex, int tagIdentifier)
        {
            if(tagIdentifier==ReducedHTMLParser.BODY_TAG)
            {
                beforeBodyPosition = charIndex;
            }
        }

        public void closedStartTag(int charIndex, int tagIdentifier)
        {
            if(tagIdentifier== ReducedHTMLParser.HEAD_TAG)
            {
                headerInsertPosition = charIndex+1;
            }
            else if(tagIdentifier==ReducedHTMLParser.BODY_TAG)
            {
                bodyInsertPosition = charIndex+1;
            }
        }

        public void openedEndTag(int charIndex, int tagIdentifier)
        {
        }

        public void closedEndTag(int charIndex, int tagIdentifier)
        {
        }

        public void attribute(int charIndex, int tagIdentifier, String key, String value)
        {
        }

        public int getHeaderInsertPosition()
        {
            return headerInsertPosition;
        }

        public int getBodyInsertPosition()
        {
            return bodyInsertPosition;
        }

        public int getBeforeBodyPosition()
        {
            return beforeBodyPosition;
        }
    }
}
