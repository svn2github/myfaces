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
package org.apache.myfaces.custom.validatebeanbehavior;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFComponent;

/**
 * Wrapper component for UIMessages, to make sure there always is a messages placeholder in the DOM.
 *
 * @author Jan-Kees van Andel
 */
@JSFComponent
public class UIMessagesWrapper extends UIComponentBase {

    /**
     * The component type for this component.
     */
    public static final String COMPONENT_TYPE = "org.apache.myfaces.custom.validatebeanbehavior.UIMessagesWrapper";

    /**
     * The component family for this component.
     */
    public static final String COMPONENT_FAMILY = "org.apache.myfaces.custom.MessagesWrapper";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);

        final ResponseWriter writer = context.getResponseWriter();
        writer.write("<div id=\"");
        writer.write(getClientId());
        writer.write("\">");
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.write("</div>");

        super.encodeEnd(context);
    }
}
