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
package org.apache.myfaces.custom.ppr;

import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Thomas Spiegl
 */
public class PPRSubmitRenderer extends Renderer {

    public static final String TRANSIENT_MARKER_ATTRIBUTE = "org.apache.myfaces.PPRPanelGroup.transientComponent";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        //if(PPRSupport.isPartialRequest(context)) {
        //    return;
        //}
        UIComponent parent = component.getParent();
        if (parent instanceof UICommand) {
            FormInfo fi = RendererUtils.findNestingForm(component, context);
            if (fi == null) {
                throw new FacesException("PPRPanelGroup must be embedded in a form.");
            }
            PPRSupport.initPPR(context, component);
            List panelGroups = new ArrayList(5);
            String id = parent.getId();
            addPPRPanelGroupComponents(context.getViewRoot(), panelGroups);
            for (int i = 0; i < panelGroups.size(); i++) {
                PPRPanelGroup pprGroup = (PPRPanelGroup) panelGroups.get(i);
                List triggers = pprGroup.parsePartialTriggers();
                for (int j = 0; j < triggers.size(); j++) {
                    PartialTriggerParser.PartialTrigger trigger = (PartialTriggerParser.PartialTrigger) triggers.get(j);
                    if (trigger.getPartialTriggerId().equals(id)) {
                        PPRSupport.encodeJavaScript(context, parent, pprGroup, trigger);
                    }
                }
            }
        }
        else {
            // TODO warning
        }
    }

    public void addPPRPanelGroupComponents(UIComponent component, List list) {
        for (Iterator it = component.getChildren().iterator(); it.hasNext();) {
            UIComponent c = (UIComponent) it.next();
            if (c instanceof PPRPanelGroup) {
                list.add(c);
            }
            if (c.getChildCount() > 0) {
                addPPRPanelGroupComponents(c, list);
            }
        }
    }
}