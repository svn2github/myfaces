/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.fisheye;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

/**
 * Renderer for the FishEyeList component
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFishEyeListRenderer extends Renderer
{
    public static final String ATTACH_EDGE_ATTR = "dojo:attachEdge";

    public static final String DOJO_STYLE_CLASS = "dojo-FisheyeList";
    public static final String EFFECT_UNITS_ATTR = "dojo:effectUnits";
    public static final String ITEM_HEIGHT_ATTR = "dojo:itemHeight";
    public static final String ITEM_MAX_HEIGHT_ATTR = "dojo:itemMaxHeight";
    public static final String ITEM_MAX_WIDTH_ATTR = "dojo:itemMaxWidth";
    public static final String ITEM_PADDING_ATTR = "dojo:itemPadding";
    public static final String ITEM_WIDTH_ATTR = "dojo:itemWidth";
    public static final String LABEL_EDGE_ATTR = "dojo:labelEdge";
    public static final String ORIENTATION_ATTR = "dojo:orientation";
    public static final String RENDERER_TYPE = "org.apache.myfaces.FishEyeList";

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        System.out.println("rendering fisheye");
        if (component.isRendered())
        {
            HtmlFishEyeList fisheye = (HtmlFishEyeList) component;
            ResponseWriter writer = context.getResponseWriter();
            //initialize DOJO
            String javascriptLocation = (String) component.getAttributes().get(
                    JSFAttr.JAVASCRIPT_LOCATION);
            DojoUtils.addMainInclude(context, component, javascriptLocation,
                    DojoUtils.getDjConfigInstance(context));
            DojoUtils.addRequire(context, component, "dojo.widget.FisheyeList");

            writer.startElement(HTML.DIV_ELEM, fisheye);
            writer.writeAttribute(HTML.CLASS_ATTR, DOJO_STYLE_CLASS, null);
            writeAttribute(writer, fisheye, ITEM_WIDTH_ATTR, fisheye
                    .getItemWidth());
            writeAttribute(writer, fisheye, ITEM_HEIGHT_ATTR, fisheye
                    .getItemHeight());
            writeAttribute(writer, fisheye, ITEM_MAX_WIDTH_ATTR, fisheye
                    .getItemMaxWidth());
            writeAttribute(writer, fisheye, ITEM_MAX_HEIGHT_ATTR, fisheye
                    .getItemMaxHeight());
            writeAttribute(writer, fisheye, ORIENTATION_ATTR, fisheye
                    .getOrientation());
            writeAttribute(writer, fisheye, EFFECT_UNITS_ATTR, fisheye
                    .getEffectUnits());
            writeAttribute(writer, fisheye, ITEM_PADDING_ATTR, fisheye
                    .getItemPadding());
            writeAttribute(writer, fisheye, ATTACH_EDGE_ATTR, fisheye
                    .getAttachEdge());
            writeAttribute(writer, fisheye, LABEL_EDGE_ATTR, fisheye
                    .getLabelEdge());
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        if (component.isRendered())
        {
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement(HTML.DIV_ELEM);
        }
    }

    protected void writeAttribute(ResponseWriter writer,
            HtmlFishEyeList fisheye, String name, Object value)
            throws IOException
    {
        if (name != null && value != null)
        {
            writer.writeAttribute(name, value, null);
        }
    }

}
