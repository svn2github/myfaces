/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.form;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlFormRendererBase;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 */
public class HtmlFormRenderer extends HtmlFormRendererBase
{

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlFormRendererBase#getActionUrl(javax.faces.context.FacesContext, javax.faces.component.UIForm )
     */
    protected String getActionUrl(FacesContext facesContext, UIForm form)
    {
        String actionUrl = getActionUrl(facesContext);
        if (form instanceof HtmlForm)
        {
            HtmlForm htmlForm = (HtmlForm) form;

            if(htmlForm.getAction()!=null)
            {
                return htmlForm.getAction();
            }
            else
            {
                String scheme = htmlForm.getScheme();
                String serverName = htmlForm.getServerName();
                Integer portObj = htmlForm.getPort();
                if (!(scheme == null && serverName == null && portObj == null))
                {
                    Object request = facesContext.getExternalContext().getRequest();

                    //todo: fix this to work in PortletRequest as well
                    if (request instanceof HttpServletRequest)
                    {
                        HttpServletRequest httpRequest = (HttpServletRequest) request;
                        // build absolute url
                        int serverPort = 0;
                        if (portObj == null)
                        {
                            serverPort = httpRequest.getServerPort();
                        }
                        else
                        {
                            serverPort = portObj.intValue();
                        }
                        if (scheme == null)
                        {
                            scheme = httpRequest.getScheme();
                        }
                        else if (portObj == null)
                        {
                            serverPort = 0;
                        }

                        if (serverName == null)
                        {
                            serverName = httpRequest.getServerName();
                        }
                        StringBuffer sb = new StringBuffer();
                        sb.append(scheme);
                        sb.append("://");
                        sb.append(serverName);

                        if (serverPort != 0)
                        {
                            if (("http".equals(scheme) && serverPort != 80)
                                            || ("https".equals(scheme) && serverPort != 443))
                            {
                                sb.append(":");
                                sb.append(serverPort);
                            }
                        }

                        sb.append(actionUrl);
                        actionUrl = sb.toString();
                    }
                }
            }
        }
        return actionUrl;
    }
    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlFormRendererBase#getMethod(javax.faces.context.FacesContext, javax.faces.component.UIForm )
     */
    protected String getMethod(FacesContext facesContext, UIForm form)
    {
        if(form instanceof HtmlForm)
        {
            String method = ((HtmlForm) form).getMethod();

            if(method != null)
                return method;
        }

        return "post";
    }

}
