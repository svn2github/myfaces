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
public class DojoUtils
{

    private static final String BODY_SCRIPT_INFOS_ATTRIBUTE_NAME = "bodyScriptInfos";
    private static final String DOJO_FILE_UNCOMPRESSED           = "dojo.js.uncompressed.js";
    private static final String DOJO_FILE                        = "dojo.js";

    /**
     * dojo utils flag which can be altered for various states of the dojo lib
     */
    public static final boolean DOJO_COMPRESSED                  = false;
    public static final boolean DOJO_DEBUG                       = false;

    public static final void addMainInclude(FacesContext context, String javascriptLocation, DojoConfig config)
    {
        AddResource addResource = AddResource.getInstance(context);
        /*
         * var djConfig = {
         isDebug: false
         };

         */
        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, config.toString());
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
    public static final void addRequire(FacesContext context, String required) throws IOException
    {
        if (isInlineScriptSet(context, "dojo.require:" + required))
            return;

        AddResource addResource = AddResource.getInstance(context);
        String requiredBuilder = createDojoRequireString(required);

        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, requiredBuilder);
    }

    /**
     * helper method for the proper dojo require script creation
     * @param required the creation package for the require functionality
     * @return
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
    public static final void addRequire(FacesContext context, UIComponent component, String required)
            throws IOException
    {

        if (isInlineScriptSet(context, "dojo.require:" + required))
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
     * @see http://dojotoolkit.org/docs/fast_widget_authoring.html for an example on this
     */
    public static final void addProvide(FacesContext context, String provided) throws IOException
    {
        if (isInlineScriptSet(context, "dojo.provide:" + provided))
            return;

        AddResource addResource = AddResource.getInstance(context);
        String providedBuilder = createDojoProvideScript(provided);

        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, providedBuilder);
    }

    public static final void addProvide(FacesContext context, UIComponent component, String provided)
            throws IOException
    {
        if (isInlineScriptSet(context, "dojo.provide:" + provided))
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
     * @return
     */
    public static String createDojoProvideScript(String provided)
    {

        StringBuffer providedBuilder = new StringBuffer(32);
        providedBuilder.append("dojo.provide('");
        providedBuilder.append(provided);
        providedBuilder.append("');");
        return providedBuilder.toString();
    }

}
