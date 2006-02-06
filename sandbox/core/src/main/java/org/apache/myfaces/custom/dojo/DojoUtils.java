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

    private static final String DOJO_FILE_UNCOMPRESSED = "dojo.js.uncompressed.js";
    private static final String DOJO_FILE              = "dojo.js";

    /**
     * dojo utils flag which can be altered for various states of the dojo lib
     */
    public static final boolean DOJO_COMPRESSED        = false;
    public static final boolean DOJO_DEBUG             = false;


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
    public static final void addRequired(FacesContext context, String required)
    {
        AddResource addResource = AddResource.getInstance(context);
        StringBuffer requiredBuilder = new StringBuffer(32);
        requiredBuilder.append("dojo.require(\"");
        requiredBuilder.append(required);
        requiredBuilder.append("\");");

        addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, requiredBuilder.toString());
    }
}
