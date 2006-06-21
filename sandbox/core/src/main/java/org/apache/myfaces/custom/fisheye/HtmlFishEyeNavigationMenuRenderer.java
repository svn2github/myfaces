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
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;

/**
 * Renderer for the FishEyeList component
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFishEyeNavigationMenuRenderer extends HtmlLinkRenderer
{
    public static final String ATTACH_EDGE_ATTR = "dojo:attachEdge";

    public static final String CAPTION_ATTR = "caption";
    public static final String DOJO_ITEM_STYLE_CLASS = "dojo-FisheyeListItem";
    public static final String DOJO_STYLE_CLASS = "dojo-FisheyeList";
    public static final String EFFECT_UNITS_ATTR = "dojo:effectUnits";
    public static final String ICON_SRC_ATTR = "dojo:iconsrc";
    public static final String ITEM_HEIGHT_ATTR = "dojo:itemHeight";
    public static final String ITEM_MAX_HEIGHT_ATTR = "dojo:itemMaxHeight";
    public static final String ITEM_MAX_WIDTH_ATTR = "dojo:itemMaxWidth";
    public static final String ITEM_PADDING_ATTR = "dojo:itemPadding";
    public static final String ITEM_WIDTH_ATTR = "dojo:itemWidth";
    public static final String LABEL_EDGE_ATTR = "dojo:labelEdge";
    public static final String ORIENTATION_ATTR = "dojo:orientation";
    public static final String CONSERVATIVE_TRIGGER_ATTR = "dojo:conservativeTrigger";
    public static final String RENDERER_TYPE = "org.apache.myfaces.FishEyeList";

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void decode(FacesContext context, UIComponent component)
    {
        FormInfo nestingForm = findNestingForm(component, context);
        if (nestingForm != null)
        {
            String fieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(nestingForm.getFormName());
            String reqValue = (String) context.getExternalContext()
            .getRequestParameterMap().get(fieldName);
            UIComponent source = context.getViewRoot().findComponent(reqValue);
            if (source instanceof UINavigationMenuItem)
            {
                UINavigationMenuItem item = (UINavigationMenuItem) source;
                item.queueEvent(new ActionEvent(item));
            }
        }
        
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        if (component.isRendered())
        {
            HtmlFishEyeNavigationMenu fisheye = (HtmlFishEyeNavigationMenu) component;
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
            writeAttribute(writer, fisheye, CONSERVATIVE_TRIGGER_ATTR, fisheye
                    .getConservativeTrigger());
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        List children = component.getChildren();
        for (Iterator cit = children.iterator(); cit.hasNext();)
        {
            UIComponent child = (UIComponent) cit.next();
            if (!child.isRendered())
                continue;
            if (child instanceof UINavigationMenuItem)
            {
                renderMenuItem(context, writer, (UINavigationMenuItem) child);
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        if (component.isRendered())
        {
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement(HTML.DIV_ELEM);
        }
    }

    /**
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        //always render the menu items
        return true;
    }

    protected void renderMenuItem(FacesContext context, ResponseWriter writer,
            UINavigationMenuItem item) throws IOException
    {
        //find the enclosing form
        FormInfo formInfo = findNestingForm(item, context);
        String clientId = item.getClientId(context);
        if (formInfo == null)
        {
            throw new IllegalArgumentException("Component " + clientId
                    + " must be embedded in an form");
        }
        UIComponent nestingForm = formInfo.getForm();
        String formName = formInfo.getFormName();

        StringBuffer onClick = new StringBuffer();

        //call the clear_<formName> method
        onClick.append(
                HtmlRendererUtils
                        .getClearHiddenCommandFormParamsFunctionName(formName))
                .append("();");
        String jsForm = "document.forms['" + formName + "']";

        if (MyfacesConfig.getCurrentInstance(context.getExternalContext())
                .isAutoScroll())
        {
            org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils
                    .appendAutoScrollAssignment(onClick, formName);
        }

        //add id parameter for decode
        String hiddenFieldName = HtmlRendererUtils
                .getHiddenCommandLinkFieldName(formName);
        onClick.append(jsForm);
        onClick.append(".elements['").append(hiddenFieldName).append("']");
        onClick.append(".value='").append(clientId).append("';");
        addHiddenCommandParameter(context, nestingForm, hiddenFieldName);

        //add the target window
        String target = item.getTarget();
        if (target != null && target.trim().length() > 0)
        {
            onClick.append(jsForm);
            onClick.append(".target='");
            onClick.append(target);
            onClick.append("';");
        }

        // onSubmit
        onClick.append("if(").append(jsForm).append(".onsubmit){var result=")
                .append(jsForm).append(
                        ".onsubmit();  if( (typeof result == 'undefined') || result ) {"
                                + jsForm + ".submit();}}else{");

        //submit
        onClick.append(jsForm);
        onClick.append(".submit();}return false;"); //return false, so that browser does not handle the click

        writer.startElement(HTML.DIV_ELEM, item);
        writer.writeAttribute(HTML.CLASS_ATTR, DOJO_ITEM_STYLE_CLASS, null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
        writer.writeAttribute(CAPTION_ATTR, item.getItemLabel(), null);
        writer.writeAttribute(ICON_SRC_ATTR, item.getIcon(), null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.endElement(HTML.DIV_ELEM);
    }

    protected void writeAttribute(ResponseWriter writer,
            HtmlFishEyeNavigationMenu fisheye, String name, Object value)
            throws IOException
    {
        if (name != null && value != null)
        {
            writer.writeAttribute(name, value, null);
        }
    }

}
