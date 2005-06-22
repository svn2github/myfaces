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
package org.apache.myfaces.custom.rssticker;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.Item;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.util.MessageUtils;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$ $Log:
 *          HtmlRssTickerRenderer.java,v $ Revision 1.4 2004/07/01 21:53:10
 *          mwessendorf ASF switch
 * 
 * Revision 1.3 2004/06/27 22:06:25 mwessendorf Log
 * 
 *  
 */
public class HtmlRssTickerRenderer extends HtmlRenderer {

    public static final String RENDERER_TYPE = "org.apache.myfaces.Ticker";
	private static final String 	NO_CONNECTION 	   = "org.apache.myfaces.ticker.NOCONNECTION";

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException {
        RendererUtils.checkParamValidity(facesContext, component,
                HtmlRssTicker.class);

        HtmlRssTicker tickerComponent = (HtmlRssTicker) component;

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        tableBegin(facesContext, tickerComponent, writer);
        rowBody(facesContext, tickerComponent, writer);
        tableEnd(facesContext, tickerComponent, writer);

    }

    //-------------------------------------------------private Methods
    /**
     * @param facesContext
     * @param tickerComponent
     * @param writer
     */
    private void tableEnd(FacesContext facesContext,
            HtmlRssTicker tickerComponent, ResponseWriter writer)
            throws IOException {
        writer.endElement(HTML.TBODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    /**
     * @param facesContext
     * @param tickerComponent
     * @param writer
     */
    private void rowBody(FacesContext facesContext,
            HtmlRssTicker tickerComponent, ResponseWriter writer)
            throws IOException {

        Channel channel = tickerComponent.getChannel();

        //check if a connection to the internet is possible
        if (channel != null) {
            writer.startElement(HTML.THEAD_ELEM, tickerComponent);
            writer.startElement(HTML.TR_ELEM, tickerComponent);
            writer.startElement(HTML.TH_ELEM, tickerComponent);

            writer.write((channel.getTitle() != null) ? channel.getTitle() : "");

            writer.endElement(HTML.TH_ELEM);
            writer.endElement(HTML.TR_ELEM);
            writer.endElement(HTML.THEAD_ELEM);

            Item[] columns = tickerComponent.items();
            for (int i = 0; i < columns.length; i++) {

                rowBegin(facesContext, tickerComponent, writer);

                writer.startElement(HTML.TD_ELEM, tickerComponent);
                writer.write("<a href=\"" + columns[i].getLink()
                        + "\" target=\"_new\">");
                writer.write(columns[i].getTitle());
                writer.write("</a>");
                writer.endElement(HTML.TD_ELEM);
                rowEnd(facesContext, tickerComponent, writer);
            }
        } else {
            //perhaps we are behind a firewall,
            //no connection - so we going to send a i18n info to the user-
            FacesMessage facesMessage = MessageUtils.getMessage(FacesMessage.SEVERITY_INFO,NO_CONNECTION,null);
            writer.write(facesMessage.getSummary());
            writer.write("<br/>");
            writer.write(facesMessage.getDetail());
        }
    }

    /**
     * @param facesContext
     * @param tickerComponent
     * @param writer
     */
    private void tableBegin(FacesContext facesContext,
            HtmlRssTicker tickerComponent, ResponseWriter writer)
            throws IOException {
        writer.startElement(HTML.TABLE_ELEM, tickerComponent);
        writer.startElement(HTML.TBODY_ELEM, tickerComponent);
    }

    /**
     * @param facesContext
     * @param tickerComponent
     * @param writer
     */
    private void rowEnd(FacesContext facesContext,
            HtmlRssTicker tickerComponent, ResponseWriter writer)
            throws IOException {
        writer.endElement(HTML.TR_ELEM);
    }

    /**
     * @param facesContext
     * @param tickerComponent
     * @param writer
     */
    private void rowBegin(FacesContext facesContext,
            HtmlRssTicker tickerComponent, ResponseWriter writer)
            throws IOException {
        writer.startElement(HTML.TR_ELEM, tickerComponent);
    }

}