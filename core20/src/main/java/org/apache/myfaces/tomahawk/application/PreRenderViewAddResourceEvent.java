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
package org.apache.myfaces.tomahawk.application;

import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.SystemEventListener;

/**
 * This event is triggered after PreRenderViewEvent but before 
 * ViewHandler.render, to give the chance to component to add 
 * resources to the current view before render occur.
 * 
 * <p>The component resources to be added are supposed to be transient, so
 * each time before the view will be rendered, this event will be published
 * and the resources will be on the page.</p>
 * 
 * <p>This event should be propagated only for the "real" component tree, that
 * means for a datatable component it should to traverse all rows.</p>
 * 
 * @since 1.1.10
 */
public class PreRenderViewAddResourceEvent extends ComponentSystemEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 3534476922995054263L;

    public PreRenderViewAddResourceEvent(UIComponent component)
    {
        super(component);
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener)
    {
        return listener instanceof SystemEventListener;
    }
}
