/*
 * Copyright 2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.myfaces.custom.effect;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.custom.dojo.DojoResourceLoader;
import org.apache.myfaces.custom.prototype.PrototypeResourceLoader;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

/**
 * Fade field only currently the renderer is implemented and the tag, because we
 * do not store any extra functionality over a simple div in here

 * For the fade, we only need a simple renderer and a backend javascript
 * inclusion so that we can serve it out of the jar
 *
 * @author Werner Punz werpu@gmx.at
 * @version $Revision$ $Date$
 */
public class EffectRenderer extends HtmlRenderer
{

    /**
     * Encodes any stand-alone javascript functions that are needed. Uses either
     * the extension filter, or a user-supplied location for the javascript
     * files.
     *
     * @param context
     *            FacesContext
     * @param component
     *            UIComponent
     */
    private void encodeJavascript(FacesContext context, UIComponent component)
    {
        // AddResource takes care to add only one reference to the same script

        // render javascript function for client-side toggle (it won't be used
        // if user has opted for server-side toggle)
        String javascriptLocation = (String) component.getAttributes().get(
                JSFAttr.JAVASCRIPT_LOCATION);
        AddResource addResource = AddResource.getInstance(context);
        if(javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/dojodebug_off.js");

            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/prototype.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/effects.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/fat.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/dojo.js.uncompressed.js");
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DojoResourceLoader.class, "dojodebug_off.js");

            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class,
                    "prototype.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "effects.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, FATResourceLoader.class, "fat.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DojoResourceLoader.class, "dojo.js.uncompressed.js");

        }
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public static final String RENDERER_TYPE = "script.aculo.us.renderer";

    /**
     * We only need an encodeBeing method because the fade control, does not
     * have any childs
     *
     * The fading is done via a styleClass tag according to the FAT specs, for
     * now this is more flexible than embedding yet another tag in another tag
     * but less flexible for styleClass usage reasons (but you always can use
     * x:div for handling this issue in a proper manner)
     *
     * we have to do a full overload unfortunately because it is altered
     * severely
     *
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        if ((context == null) || (component == null))
        {
            throw new NullPointerException();
        }

        Boolean rendered = (Boolean) component.getAttributes().get("rendered");

        if ((rendered != null) && (!rendered.booleanValue()))
            return;
        encodeJavascript(context, component);
        super.encodeBegin(context, component);
        prepareFade(component);
        renderEffectsBegin(context, component);
    }

    /**
     * prepares the styleClasses for the fading renderer
     *
     * @param component
     */
    private void prepareFade(UIComponent component)
    {
        Div theComponent = (Div) component;
        Boolean fade = (Boolean) component.getAttributes().get(
                EffectTag.TAG_PARAM_FADE);

        String theStyleClass = theComponent.getStyleClass();
        if (theStyleClass == null)
            theStyleClass = "";
        if (fade != null && fade.booleanValue()
                && !(theStyleClass.indexOf("fade") != -1))
        {

            if (theStyleClass != null
                    && !theStyleClass.trim().equalsIgnoreCase(""))
                theStyleClass += ";fade";
            else
                theStyleClass = "fade";

            String fadeColor = (String) component.getAttributes().get(
                    EffectTag.TAG_PARAM_FADECOLOR);

            fadeColor = (fadeColor != null) ? fadeColor : "";
            fadeColor = fadeColor.replaceAll("#", "");
            if (fadeColor.length() != 0)
                fadeColor = "-" + fadeColor;

            theStyleClass += fadeColor;
            theComponent.setStyleClass(theStyleClass);
        }
    }

    public void renderEffectsBegin(FacesContext context, UIComponent component)
            throws IOException
    {

        // dump all the parameters which affect us into we dont set a central
        // unchecked here to keep the warning level high
        Boolean squish = (Boolean) component.getAttributes().get(
                EffectTag.TAG_PARAM_SQUISH);
        Boolean puff = (Boolean) component.getAttributes().get(
                EffectTag.TAG_PARAM_PUFF);
        Boolean scale = (Boolean) component.getAttributes().get(
                EffectTag.TAG_PARAM_SCALE);
        Boolean pulsate = (Boolean) component.getAttributes().get(
                EffectTag.TAG_PARAM_PULSATE);
        Integer scaleSize = (Integer) component.getAttributes().get(
                EffectTag.TAG_PARAM_SCALE_SIZE);

        Div div = (Div) component;
        ResponseWriter writer = context.getResponseWriter();

        // if(fade != null && fade.booleanValue())
        // ScriptController.renderScriptWithDeps(context, component, writer,
        // ScriptController.FAT_VIEW_ID);

        writer.startElement(HTML.DIV_ELEM, component);
        HtmlRendererUtils.writeIdIfNecessary(writer, component, context);

        String styleClass = div.getStyleClass();
        String style = div.getStyle();
        if (null != styleClass && null != style)
        {
            throw new IllegalStateException(
                    "Only one of style or styleClass can be specified");
        }
        if (null != styleClass)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
        if (null != style)
        {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }
        // todo check the existing code for already existent javascript code
        // not needed now, but probably in the long run

        if (puff != null && puff.booleanValue())
        {
            writer.writeAttribute(HTML.ONCLICK_ATTR,
                    "javascript:new Effect.Puff(this);", null);
        }
        if (squish != null && squish.booleanValue())
        {
            writer.writeAttribute(HTML.ONCLICK_ATTR,
                    "javascript:new Effect.Squish(this);", null);
        }
        if (pulsate != null && pulsate.booleanValue())
        {
            writer.writeAttribute(HTML.ONCLICK_ATTR,
                    "javascript:new Effect.Pulsate(this);", null);
        }
        if (scale != null && scale.booleanValue())
        {
            writer.writeAttribute(HTML.ONMOUSEOVER_ATTR,
                    "javascript:new Effect.Scale(this," + scaleSize.toString()
                            + ");", null);
            writer.writeAttribute(HTML.ONMOUSEOUT_ATTR,
                    "javascript:new Effect.Scale(this,100);", null);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext facesContext,
            UIComponent uiComponent) throws IOException
    {

        RendererUtils.renderChildren(facesContext, uiComponent);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
    }

    /**
     * Standard encode end
     *
     */
    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        super.encodeEnd(facesContext, component);
    }

}
