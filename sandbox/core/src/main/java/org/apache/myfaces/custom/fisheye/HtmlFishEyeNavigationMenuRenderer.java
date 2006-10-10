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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;

/**
 * Renderer for the FishEyeList component
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFishEyeNavigationMenuRenderer extends HtmlLinkRenderer {
    private static final String ON_CLICK_ATTR             = "onClick";

    private static final String DOJO_COMPONENT_TYPE       = "ScrollableFisheyeList";

    private static final String DOJO_ITEM_TYPE            = "ScrollableFisheyeListItem";

    public static final String  ATTACH_EDGE_ATTR          = "attachEdge";

    public static final String  CAPTION_ATTR              = "caption";

    public static final String  EFFECT_UNITS_ATTR         = "effectUnits";

    public static final String  ICON_SRC_ATTR             = "iconSrc";

    public static final String  ITEM_HEIGHT_ATTR          = "itemHeight";

    public static final String  ITEM_MAX_HEIGHT_ATTR      = "itemMaxHeight";

    public static final String  ITEM_MAX_WIDTH_ATTR       = "itemMaxWidth";

    public static final String  ITEM_PADDING_ATTR         = "itemPadding";

    public static final String  ITEM_WIDTH_ATTR           = "itemWidth";

    public static final String  LABEL_EDGE_ATTR           = "labelEdge";

    public static final String  ORIENTATION_ATTR          = "orientation";

    public static final String  CONSERVATIVE_TRIGGER_ATTR = "conservativeTrigger";

    public static final String  RENDERER_TYPE             = "org.apache.myfaces.FishEyeList";

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void decode(FacesContext context, UIComponent component) {
        FormInfo nestingForm = findNestingForm(component, context);
        if (nestingForm != null) {
            String fieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(nestingForm);
            String reqValue = (String) context.getExternalContext().getRequestParameterMap().get(fieldName);
            if (reqValue != null && reqValue.length() > 0) {
                UIComponent source = context.getViewRoot().findComponent(reqValue);
                if (source instanceof UINavigationMenuItem) {
                    UINavigationMenuItem item = (UINavigationMenuItem) source;
                    item.queueEvent(new ActionEvent(item));
                }
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (component.isRendered()) {
            HtmlFishEyeNavigationMenu fisheye = (HtmlFishEyeNavigationMenu) component;
            ResponseWriter writer = context.getResponseWriter();
            // initialize DOJO
            String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
            DojoUtils.addMainInclude(context, component, javascriptLocation, DojoUtils.getDjConfigInstance(context));
            DojoUtils.addRequire(context, component, "dojo.widget.myfaces.ScrollableFisheyeList");
            DojoUtils.addRequire(context, component, "dojo.widget.html.myfaces.ScrollableFisheyeListItem");
            DojoUtils.addRequire(context, component, "dojo.widget.Button");
            String leftName = "";
            if (fisheye.getVisibleWindow() != null) {
                writer.startElement(HTML.TABLE_ELEM, component);
                writer.startElement(HTML.TR_ELEM, component);
                writer.startElement(HTML.TD_ELEM, component);
                writer.writeAttribute(HTML.WIDTH_ATTR, "30", null);
                writer.startElement(HTML.DIV_ELEM, component);
                writer.writeAttribute(HTML.ID_ATTR, component.getClientId(context) + "_left", null);
                writer.write("&lt;&lt;");
                writer.endElement(HTML.DIV_ELEM);

                Map paramMap = new TreeMap();
                paramMap.put("id", DojoUtils.calculateWidgetVarName(component.getClientId(context) + "_left"));
                leftName = DojoUtils.renderWidgetInitializationCode(writer, component, "Button", paramMap, component.getClientId(context) + "_left", true);
                // render the onclick
                writer.endElement(HTML.TD_ELEM);
                writer.startElement(HTML.TD_ELEM, component);
            }

            writer.startElement(HTML.DIV_ELEM, fisheye);
            writer.writeAttribute(HTML.ID_ATTR, component.getClientId(context), null);

            writer.endElement(HTML.DIV_ELEM);
            String rightName = "";
            if (fisheye.getVisibleWindow() != null) {
                writer.endElement(HTML.TD_ELEM);
                writer.startElement(HTML.TD_ELEM, component);
                writer.writeAttribute(HTML.WIDTH_ATTR, "30", null);
                writer.startElement(HTML.DIV_ELEM, component);
                writer.writeAttribute(HTML.ID_ATTR, component.getClientId(context) + "_right", null);
                writer.write("&gt;&gt;");
                writer.endElement(HTML.DIV_ELEM);
                Map paramMap = new TreeMap();
                paramMap.put("id", DojoUtils.calculateWidgetVarName(component.getClientId(context) + "_right"));
                rightName = DojoUtils.renderWidgetInitializationCode(writer, component, "Button", paramMap, component.getClientId(context) + "_right", true);

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
                writer.endElement(HTML.TABLE_ELEM);
            }

            Map paramMap = new HashedMap();

            if (fisheye.getVisibleWindow() != null) {
                paramMap.put("visibleWindow", fisheye.getVisibleWindow());
            }

            paramMap.put(ITEM_WIDTH_ATTR, fisheye.getItemWidth());
            paramMap.put(ITEM_HEIGHT_ATTR, fisheye.getItemHeight());
            paramMap.put(ITEM_MAX_WIDTH_ATTR, fisheye.getItemMaxWidth());
            paramMap.put(ITEM_MAX_HEIGHT_ATTR, fisheye.getItemMaxHeight());
            paramMap.put(ORIENTATION_ATTR, fisheye.getOrientation());
            paramMap.put(EFFECT_UNITS_ATTR, fisheye.getEffectUnits());
            paramMap.put(ITEM_PADDING_ATTR, fisheye.getItemPadding());
            paramMap.put(ATTACH_EDGE_ATTR, fisheye.getAttachEdge());
            paramMap.put(LABEL_EDGE_ATTR, fisheye.getLabelEdge());
            paramMap.put(CONSERVATIVE_TRIGGER_ATTR, fisheye.getConservativeTrigger());

            DojoUtils.renderWidgetInitializationCode(context, component, DOJO_COMPONENT_TYPE, paramMap);

            if (fisheye.getVisibleWindow() != null) {
                writer.startElement(HTML.SCRIPT_ELEM, component);
                writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
                writer
                        .write(leftName + ".onClick = function(e) {" + DojoUtils.calculateWidgetVarName(component.getClientId(context))
                                + ".onLeftScroll();};");
                writer.write(rightName + ".onClick = function(e) {" + DojoUtils.calculateWidgetVarName(component.getClientId(context))
                        + ".onRightScroll();};");
                writer.endElement(HTML.SCRIPT_ELEM);
            }

        }

    }

    private Stack getChildsMenuStack(FacesContext context, UIComponent component) {
        Stack menuStack = (Stack) ((HttpServletRequest) context.getExternalContext().getRequest()).getAttribute(component.getClientId(context)
                + "_FishEyeMenuAttr");
        if (menuStack != null)
            return menuStack;

        menuStack = new Stack();
        ((HttpServletRequest) context.getExternalContext().getRequest()).setAttribute(component.getClientId(context) + "_FishEyeMenuAttr", menuStack);
        return menuStack;
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List children = component.getChildren();
        Stack menuStack = (Stack) getChildsMenuStack(context, component);
        for (Iterator cit = children.iterator(); cit.hasNext();) {
            UIComponent child = (UIComponent) cit.next();
            if (!child.isRendered())
                continue;
            if (child instanceof UINavigationMenuItem) {
                renderMenuItem(context, writer, component, (UINavigationMenuItem) child, menuStack);
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (component.isRendered()) {
            ResponseWriter writer = context.getResponseWriter();
            Stack menuStack = getChildsMenuStack(context, component);
            String jsMenuVar = DojoUtils.calculateWidgetVarName(component.getClientId(context));

            writer.startElement(HTML.SCRIPT_ELEM, component);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            while (!menuStack.isEmpty()) {
                String item = (String) menuStack.pop();
                writer.write(jsMenuVar);
                writer.write(".addChild(");
                writer.write(item);
                writer.write(");\n");
            }
            writer.write(jsMenuVar + ".postCreate(['programmaticdone'],null);\n");
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    /**
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren() {
        // always render the menu items
        return true;
    }

    protected void renderMenuItem(FacesContext context, ResponseWriter writer, UIComponent menu, UINavigationMenuItem item, Stack childsMenuStack)
            throws IOException {
        // find the enclosing form
        FormInfo formInfo = findNestingForm(item, context);
        String clientId = item.getClientId(context);
        if (formInfo == null) {
            throw new IllegalArgumentException("Component " + clientId + " must be embedded in an form");
        }
        UIComponent nestingForm = formInfo.getForm();
        String formName = formInfo.getFormName();

        StringBuffer onClick = new StringBuffer();

        String jsForm = "document.forms['" + formName + "']";
        if (RendererUtils.isAdfOrTrinidadForm(formInfo.getForm())) {
            onClick.append("submitForm('");
            onClick.append(formInfo.getForm().getClientId(context));
            onClick.append("',1,{source:'");
            onClick.append(clientId);
            onClick.append("'});return false;");
        } else {
            // call the clear_<formName> method
            onClick.append(HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName(formName)).append("();");

            if (MyfacesConfig.getCurrentInstance(context.getExternalContext()).isAutoScroll()) {
                HtmlRendererUtils.appendAutoScrollAssignment(onClick, formName);
            }

            // add id parameter for decode
            String hiddenFieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(formInfo);
            onClick.append(jsForm);
            onClick.append(".elements['").append(hiddenFieldName).append("']");
            onClick.append(".value='").append(clientId).append("';");
            addHiddenCommandParameter(context, nestingForm, hiddenFieldName);
        }

        // add the target window
        String target = item.getTarget();
        if (target != null && target.trim().length() > 0) {
            onClick.append(jsForm);
            onClick.append(".target='");
            onClick.append(target);
            onClick.append("';");
        }

        // onSubmit
        onClick.append("if(").append(jsForm).append(".onsubmit){var result=").append(jsForm).append(
                ".onsubmit();  if( (typeof result == 'undefined') || result ) {" + jsForm + ".submit();}}else{");

        // submit
        onClick.append(jsForm);
        onClick.append(".submit();}return false;"); // return false, so that
                                                    // browser does not handle
                                                    // the click

        Map paramMap = new HashMap();
        paramMap.put(CAPTION_ATTR, item.getItemLabel());
        paramMap.put(ICON_SRC_ATTR, item.getIcon());
        paramMap.put(ON_CLICK_ATTR, new StringBuffer("function () {").append(onClick).append("}"));
        // push the onclick as lambda and use a stringbuffer so that we do not
        // get enclosing quotes
        String menuItemId = DojoUtils.renderWidgetInitializationCode(writer, item, DOJO_ITEM_TYPE, paramMap, item.getClientId(context), false);
        childsMenuStack.push(menuItemId);
        // we have to revert the elements,
        // hence a stack
    }

    protected void writeAttribute(ResponseWriter writer, HtmlFishEyeNavigationMenu fisheye, String name, Object value) throws IOException {
        if (name != null && value != null) {
            writer.writeAttribute(name, value, null);
        }
    }

}
