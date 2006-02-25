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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final String INCL_TYPE_REQ_KEY = "DOJO_DEVELOPMENT_INCLUDE";
    private static final Log    log              = LogFactory.getLog(DojoUtils.class);
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
    private static final String DJCONFIG_REQ_KEY                 = "MYFACES_DJCONFIG";

    /**
     * Request singleton getter method for the djConfig object
     *
     * @param context
     * @return
     */
    public static DojoConfig getDjConfigInstance(FacesContext context)
    {
        //we wont have a need for a synchronized here, since
        //we are in a single request cycle anyway
        //but take care if you use the djconfig in multiple threads!
        DojoConfig djConfig = (DojoConfig) ((HttpServletRequest) context.getExternalContext().getRequest())
                .getAttribute(DJCONFIG_REQ_KEY);
        if (djConfig == null)
        {
            djConfig = new DojoConfig();
            ((HttpServletRequest) context.getExternalContext().getRequest()).setAttribute(DJCONFIG_REQ_KEY, djConfig);
        }
        return djConfig;
    }

    /**
     * helper to merge in an external dojo config instance
     * the merge algorithm is that an existing entry is overwritten
     * if a new config entry is set
     * make sure that this is not called too often
     * due to the fact that we do heavy reflection in here
     * @param context
     * @param config
     */
    public static void mergeExternalDjConfig(FacesContext context, DojoConfig config)
    {
        //we now do the same as beanutils, but for dependency reasons we code it
        DojoConfig configSingleton = getDjConfigInstance(context);
        Class dcConfigClass = DojoConfig.class;
        Method[] djConfigFieldArr = dcConfigClass.getMethods();
        for (int cnt = 0; cnt < djConfigFieldArr.length; cnt++)
        {

            try
            {
                Method configPropertyField = djConfigFieldArr[cnt];
                String methodCore = null;
                if (!configPropertyField.getName().startsWith("getClass")
                        && configPropertyField.getName().startsWith("get")
                        || configPropertyField.getName().startsWith("is"))
                    methodCore = (configPropertyField.getName().startsWith("get")) ? configPropertyField.getName()
                            .substring(3) : configPropertyField.getName().substring(2);

                if (methodCore != null)
                {
                    Object val = configPropertyField.invoke(config, null);
                    if (val != null)
                    {
                        Class[] setterParams = new Class[1];
                        setterParams[0] = val.getClass();
                        Method setMethod = dcConfigClass.getMethod("set" + methodCore, setterParams);
                        if (setMethod != null)
                        {
                            Object[] setterArgs = new Object[1];
                            setterArgs[0] = val;
                            setMethod.invoke(configSingleton, setterArgs);
                        }
                    }
                }
            }
            catch (IllegalArgumentException e)
            {
                log.error(e);
            }
            catch (SecurityException e)
            {
                log.error(e);
            }
            catch (IllegalAccessException e)
            {
                log.error(e);
            }
            catch (InvocationTargetException e)
            {
                log.error(e);
            }
            catch (NoSuchMethodException e)
            {
                log.error(e);
            }
        }

    }

    public static void addMainInclude(FacesContext facesContext, String javascriptLocation, DojoConfig config)
    {

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        /*
         * var djConfig = {
         isDebug: false
         };
         TODO add a saner handling of collecting all djconfig data
         and then merging it
         */
        if (!isInlineScriptSet(facesContext, DJCONFIG_INITKEY))
        {
            addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, DJCONFIG_INITKEY);
            addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, config.toString());
        }

        String dojofile = (getExpanded(facesContext) != null && getExpanded(facesContext).booleanValue())  ? DOJO_FILE_UNCOMPRESSED : DOJO_FILE;
        if (javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, javascriptLocation + dojofile);
        }
        else
        {
            addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, DojoResourceLoader.class,
                    dojofile);
        }

    }

    /**
     * adds a dojo require include to our mix
     * of stuff used
     *
     * @param facesContext
     * @param required
     */
    public static void addRequire(FacesContext facesContext, String required)
    {
        if (isInlineScriptSet(facesContext, DOJO_REQUIRE + required))
            return;

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        String requiredBuilder = createDojoRequireString(required);

        addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, requiredBuilder);
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
     * @param facesContext the local faces context
     * @param component the component which the reuire is for
     * @param required a require dojo style xx.xx.xx
     * @throws IOException
     */
    public static void addRequire(FacesContext facesContext, UIComponent component, String required) throws IOException
    {

        if (isInlineScriptSet(facesContext, DOJO_REQUIRE + required))
            return;

        String requiredBuilder = createDojoRequireString(required);

        ResponseWriter writer = facesContext.getResponseWriter();
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

    public static void addProvide(FacesContext facesContext, UIComponent component, String provided) throws IOException
    {
        if (isInlineScriptSet(facesContext, DOJO_PROVIDE + provided))
            return;

        String providedBuilder = createDojoProvideScript(provided);

        ResponseWriter writer = facesContext.getResponseWriter();
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
     * @param facesContext
     * @param component
     * @return
     */
    public static void addDebugConsole(FacesContext facesContext, UIComponent component) throws IOException
    {
        /*check whether we have a debugging flag already set*/
        if (isInlineScriptSet(facesContext, "/*DOJO DEBUGCONSOLE ON*/"))
            return;
        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, "/*DOJO DEBUGCONSOLE ON*/");

        ResponseWriter writer = facesContext.getResponseWriter();
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

    /**
     * creates a debug statement for the debug console
     * @param stmnt the debug message displayed by the debug console
     * @return javaScriptcode String
     */
    public static String createDebugStatement(String stmnt)
    {
        return "dojo.debug(\"" + stmnt + "\");\n";
    }

    /**
     * creates a debug statement and a corresponding value for the debug console
     * @param stmnt the debug message displayed and given value by the debug console
     * @return javaScriptcode String
     */
    public static String createDebugStatement(String stmnt, String value)
    {
        return "dojo.debug(\"" + stmnt + ":\");dojo.debug("+value+");\n";
    }

    /**
     * helper to write out debug statements
     * this is only a convenience method to reduce the
     * code bloat
     * 
     * @param writer
     * @param stmnt
     * @return
     * @throws IOException
     */
    public static void writeDebugStatement(ResponseWriter writer, String stmnt) throws IOException
    {
        stmnt = createDebugStatement(stmnt);
        writer.write(stmnt);
    }

    /**
     * if this flag is set to true somewhere before
     * the rendering, the expanded version is loaded
     * otherwise the nonexpanded version is loaded
     * 
     * @param facesContext context because we again have a full request singleton here
     * @param expanded if set to true the expanded version of the dojo scripts are loaded
     * otherwise the non expanded ones are loaded
     */
    public static void setExpanded(FacesContext facesContext, Boolean expanded)
    {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute(INCL_TYPE_REQ_KEY, expanded);
    }

    /**
     * getter for the expanded flat
     * @param facesContext
     * @return
     */
    public static Boolean getExpanded(FacesContext facesContext)
    {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Boolean devStatus = (Boolean) request.getAttribute(INCL_TYPE_REQ_KEY);
        return devStatus;
    }

}
