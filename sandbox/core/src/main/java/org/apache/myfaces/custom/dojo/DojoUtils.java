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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.util.AddResource;

/**
 * Utils class for the dojo infrastructure
 * to ease the component building mechanisms
 * 
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoUtils
{
    
    private static final String DOJO_FILE_UNCOMPRESSED = "/dojo.js.uncompressed.js";
    //TODO chew this through the flag code
    private static final String DOJO_FILE              = "/dojo.js";

    /**
     * dojo utils flag which can be altered for various states of the dojo lib
     */
    //TODO make em actually usable, they are currently only debugging flags
    public static final boolean DOJO_COMPRESSED        = false;
    public static final boolean DOJO_DEBUG             = false;

    /**
     * Write a dojo require include 
     * 
     * @param writer        the response write
     * @param component     the component
     * @param dojoPackage   the dojo package string (wildcards allowed
     * @throws IOException  in case of an io error
     */
    public static final void require(ResponseWriter writer, UIComponent component, String dojoPackage)
            throws IOException
    {
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        StringBuffer buf = new StringBuffer(40);

        buf.append("dojo.require(\"");
        buf.append(dojoPackage);
        buf.append(");");
        writer.write(buf.toString());

        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * handles the central include of the dojo
     * core, subsequent packages can be loaded 
     * via require if needed
     * 
     * @param context
     * @param javascriptLocation
     */
    public static final void addMainInclude(FacesContext context, String javascriptLocation)
    {
        AddResource addResource = AddResource.getInstance(context);

        if (javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation
                    + DOJO_FILE_UNCOMPRESSED);
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DojoResourceLoader.class,
                    DOJO_FILE_UNCOMPRESSED);
        }
    }

}
