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
package org.apache.myfaces.renderkit.html.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.HtmlBufferResponseWriterWrapper;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.io.IOException;

/**
 * This phase listener puts in the request the javascript code needed to render the dummyForm
 * and the autoscroll feature.
 *
 * The ExtensionsFilter will put this code before the closing &tt;/body&gt; tag.
 *
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExtensionsPhaseListener implements PhaseListener {

    private static final Log log = LogFactory.getLog(ExtensionsPhaseListener.class);


    private static final String ORG_APACHE_MYFACES_MY_FACES_JAVASCRIPT = "org.apache.myfaces.myFacesJavascript";

    public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }

    public void beforePhase(PhaseEvent event)
    {
    }

    public void afterPhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();

        try
        {
            renderCodeBeforeBodyEnd(facesContext);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Renders stuff such as the dummy form and the autoscroll javascript, which goes before the closing &lt;/body&gt;
     * @throws IOException
     */
    public static void renderCodeBeforeBodyEnd(FacesContext facesContext) throws IOException
    {
        Object myFacesJavascript = facesContext.getExternalContext().getRequestMap().get(ORG_APACHE_MYFACES_MY_FACES_JAVASCRIPT);

        if (myFacesJavascript != null)
        {
            return;
        }

        ResponseWriter responseWriter = facesContext.getResponseWriter();
        HtmlBufferResponseWriterWrapper writerWrapper = HtmlBufferResponseWriterWrapper
                    .getInstance(responseWriter);
        facesContext.setResponseWriter(writerWrapper);

        if (DummyFormUtils.isWriteDummyForm(facesContext))
        {
            DummyFormUtils.writeDummyForm(writerWrapper, DummyFormUtils.getDummyFormParameters(facesContext));
        }

        MyfacesConfig myfacesConfig = MyfacesConfig.getCurrentInstance(facesContext.getExternalContext());
        if (myfacesConfig.isDetectJavascript())
        {
            if (! JavascriptUtils.isJavascriptDetected(facesContext.getExternalContext()))
            {

                writerWrapper.startElement("script",null);
                writerWrapper.writeAttribute("attr", HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
                StringBuffer script = new StringBuffer();
                script.append("document.location.replace('").
                        append(facesContext.getApplication().getViewHandler().getResourceURL(facesContext, "/_javascriptDetector_")).append("?goto=").append(facesContext.getApplication().getViewHandler().getActionURL(facesContext, facesContext.getViewRoot().getViewId())).append("');");
                writerWrapper.writeText(script.toString(),null);
                writerWrapper.endElement(HTML.SCRIPT_ELEM);
            }
        }

        if (myfacesConfig.isAutoScroll())
        {
            JavascriptUtils.renderAutoScrollFunction(facesContext, writerWrapper);
        }

        //todo: this is just a quick-fix - Sun RI screams if the old responsewriter is null
        //but how to reset the old response-writer then?
        if(responseWriter!=null)
            facesContext.setResponseWriter(responseWriter);

        facesContext.getExternalContext().getRequestMap().put(ORG_APACHE_MYFACES_MY_FACES_JAVASCRIPT, "<!-- MYFACES JAVASCRIPT -->\n"+writerWrapper.toString()+"\n");
    }


}
