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

package org.apache.myfaces.custom.globalId;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * A simple container-component that causes its child components to render a clientId value without
 * any prefix.
 * <p>
 * Important: this component works only when run in a JSF-1.2 (or later) environment. When run in
 * a JSF-1.1 environment it will not cause an error, but will instead act like a NamingContainer
 * itself, ie will <i>add</i> its own id to the child component's clientId.
 * </p>
 * <p>
 * Every JSF component has a "clientId" property; when the component is rendered, many components
 * output this as part of the rendered representation. In particular, when rendering HTML, many
 * components write an "id" attribute on their html element which contains the clientId. The clientId
 * is defined as being the clientId value of the nearest NamingContainer ancestor plus ":" plus the
 * component's id.
 * </p>
 * <p>
 * The prefixing of the parent container's clientId is important for safely building views from
 * multiple files (eg using Facelets templating or JSP includes). However in some cases it is
 * necessary or useful to render a clientId which is just the raw id of the component without any
 * naming-container prefix; this component can be used to do that simply by adding an instance of
 * this type as an ancestor of the problem components. This works for <i>all</i> JSF components, 
 * not just Tomahawk ones.
 * </p>
 * <p>
 * Use of this component should be a "last resort"; having clientIds which contain the id of the ancestor
 * NamingContainer is important and useful behaviour. It allows a view to be built from multiple different
 * files (using facelets templating or jsp includes); without this feature, component ids would need to be
 * very carefully managed to ensure the same id was not used in two places. In addition, it would not be
 * possible to include the same page fragment twice.
 * </p>
 * <p>
 * Ids are sometimes used by Cascading Style Sheets to address individual components, and JSF compound
 * ids are not usable by CSS. However wherever possible use a style <i>class</i> to select the component
 * rather than using this component to assign a "global" id.
 * </p>
 * <p>
 * Ids are sometimes used by javascript "onclick" handlers to locate HTML elements associated with the
 * clicked item (document.getById). Here, the onclick handler method can be passed the id of the clicked
 * object, and some simple string manipulation can then compute the correct clientId for the target
 * component, rather than using this component to assign a "global" id to the component to be accessed.
 * </p>
 * <p>
 * This component is similar to the "forceId" attribute available on many Tomahawk components. Unlike
 * the forceId attribute this (a) can be used with all components, not just Tomahawk ones, and (b)
 * applies to all its child components.
 * </p>
 * <p>
 * Note that since JSF1.2 forms have the property prefixId which can be set to false to make a UIForm
 * act as if it is not a NamingContainer. This is a good idea; the form component should probably
 * never have been a NamingContainer, and disabling this has no significant negative effects.
 * </p>
 * 
 * @JSFComponent
 *   name = "s:globalId"
 *   tagClass = "org.apache.myfaces.custom.globalId.GlobalIdTag"
*/
public class GlobalId extends UIComponentBase implements NamingContainer
{
    public final static String COMPONENT_FAMILY = "org.apache.myfaces.custom.globalId";
    public final static String COMPONENT_TYPE = "org.apache.myfaces.custom.globalId";

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    // Note: this method was added to UIComponentBase in JSF 1.2; JSF-1.1 environments will
    // simply never call it.
    public String getContainerClientId(FacesContext facesContext)
    {
        return null;
    }
}