/**
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

package org.apache.myfaces.custom.dojo;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

/**
 * Utils class for the dojo infrastructure
 * to ease the component building mechanisms
 * note this class uses its own double entries
 * filter due to the fact
 * that we can mix and match header and body scripts
 * as needed (we do not want to lose portal functionality do we?)
 *
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class DojoUtils
{

    private static final String DOJO_PROVIDE     = "dojo.provide:";
    private static final String DOJO_REQUIRE     = "dojo.require:";
    private static final String DJCONFIG_INITKEY = "/*djconfig init*/";

    private DojoUtils()
    {
        //nope
    }

    private static final String BODY_SCRIPT_INFOS_ATTRIBUTE_NAME = "bodyScriptInfos";
    private static final String DOJO_FILE_UNCOMPRESSED           = "dojo.js.uncompressed.js";
    private static final String DOJO_FILE                        = "dojo.js";

    /**
     * dojo utils flag which can be altered for various states of the dojo lib
     */
    public static final boolean DOJO_COMPRESSED                  = false;
    public static final boolean DOJO_DEBUG                       = false;

    public static void addMainInclude(FacesContext context, String javascriptLocation, DojoConfig config)
    {

        AddResource addResource = AddResourceFactory.getInstance(context);
        /*
         * var djConfig = {
         isDebug: false
         };
         TODO add a saner handling of collecting all djconfig data
         and then merging it
         */
        if (!isInlineScriptSet(context, DJCONFIG_INITKEY))
        {
            addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, DJCONFIG_INITKEY);
            addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, config.toString());
        }
        String dojofile = DOJO_COMPRESSED ? DOJO_FILE : DOJO_FILE_UNCOMPRESSED;
        if (javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + dojofile);
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DojoResourceLoader.class, dojofile);
        }

    }

    /**
     * adds a dojo require include to our mix
     * of stuff used
     *
     * @param context
     * @param required
     */
    public static void addRequire(FacesContext context, String required)
    {
        if (isInlineScriptSet(context, DOJO_REQUIRE + required))
            return;

        AddResource addResource = AddResourceFactory.getInstance(context);
        String requiredBuilder = createDojoRequireString(required);

        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, requiredBuilder);
    }

    /**
     * helper method for the proper dojo require script creation
     * @param required the creation package for the require functionality
     * @return dojoRequire String
     */
    public static String createDojoRequireString(String required)
    {
        StringBuffer requiredBuilder = new StringBuffer(32);
        requiredBuilder.append("dojo.require('");
        requiredBuilder.append(required);
        requiredBuilder.append("');");
        return requiredBuilder.toString();
    }

    private static Set getBodyScriptInfos(HttpServletRequest request)
    {
        Set set = (Set) request.getAttribute(BODY_SCRIPT_INFOS_ATTRIBUTE_NAME);
        if (set == null)
        {
            set = new TreeSet();
            request.setAttribute(BODY_SCRIPT_INFOS_ATTRIBUTE_NAME, set);
        }
        return set;
    }

    static boolean isInlineScriptSet(FacesContext context, String inlineScript)
    {

        //TODO move this non neutral code into the resource handler
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set set = getBodyScriptInfos(request);
        if (!set.contains(inlineScript))
        {
            set.add(inlineScript);
            return false;
        }
        return true;
    }

    /**
     * writes a local require
     * @param context
     * @param component
     * @param required
     * @throws IOException
     */
    public static void addRequire(FacesContext context, UIComponent component, String required) throws IOException
    {

        if (isInlineScriptSet(context, DOJO_REQUIRE + required))
            return;

        String requiredBuilder = createDojoRequireString(required);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.write(requiredBuilder);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * adds a dojo provide to the current list
     * of definitions within the header
     * @param context the faces context for accessing the resources internally
     * @param provided the package with the class provided by this implementation
     * @see <a href="http://dojotoolkit.org/docs/fast_widget_authoring.html">Dojo-Widget-Authoring</a> for an example on this
     */
    public static void addProvide(FacesContext context, String provided)
    {
        if (isInlineScriptSet(context, DOJO_PROVIDE + provided))
            return;

        AddResource addResource = AddResourceFactory.getInstance(context);
        String providedBuilder = createDojoProvideScript(provided);

        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, providedBuilder);
    }

    public static void addProvide(FacesContext context, UIComponent component, String provided) throws IOException
    {
        if (isInlineScriptSet(context, DOJO_PROVIDE + provided))
            return;

        String providedBuilder = createDojoProvideScript(provided);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.write(providedBuilder);
        writer.endElement(HTML.SCRIPT_ELEM);

    }

    /**
     * helper method which does the proper dojo provide script creation
     * @param provided the provided class name
     * @return dojoProvide String
     */
    public static String createDojoProvideScript(String provided)
    {

        StringBuffer providedBuilder = new StringBuffer(32);
        providedBuilder.append("dojo.provide('");
        providedBuilder.append(provided);
        providedBuilder.append("');");
        return providedBuilder.toString();
    }

    /**
     * adds a debug console to the output
     * this is for helping to debug the dojo system
     * a debug:true is required for this to work properly
     * it will not be set by this method (due to the avoidance
     * of unwanted automatisms causing sideefects)
     * 
     * @param context
     * @param component
     * @return
     */
    public static void addDebugConsole(FacesContext context, UIComponent component) throws IOException
    {
        /*check whether we have a debugging flag already set*/
        if (isInlineScriptSet(context, "/*DOJO DEBUGCONSOLE ON*/"))
            return;
        AddResource addResource = AddResourceFactory.getInstance(context);
        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, "/*DOJO DEBUGCONSOLE ON*/");

        ResponseWriter writer = context.getResponseWriter();
        //we for now have to break html until the dynamic creation
        //isses are resolved, so hold on for this messy code now
        //Since this is for debugging purposes only, we can live with it

        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, "myfaces_Dojo_Debugger", null);
        writer.writeAttribute("dojoType", "DebugConsole", null);
        writer.writeAttribute("title", "MyFaces Dojo Debug console", null);
        writer.writeAttribute("iconSrc", "images/flatScreen.gif", null);
        writer.writeAttribute("constrainToContainer", "1", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width: 400px; height: 500px; left: 200px;", null);
        writer.writeAttribute("hasShadow", "true", null);
        writer.writeAttribute("resizable", "true", null);
        writer.writeAttribute("displayCloseAction", "true", null);
        writer.writeAttribute("layoutAlign", "client", null);

        writer.endElement(HTML.DIV_ELEM);

    }
}
