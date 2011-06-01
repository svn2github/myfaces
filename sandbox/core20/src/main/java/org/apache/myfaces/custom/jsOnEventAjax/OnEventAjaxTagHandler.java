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
package org.apache.myfaces.custom.jsOnEventAjax;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFFaceletTag;

@JSFFaceletTag
public class OnEventAjaxTagHandler extends ComponentHandler
{

    public OnEventAjaxTagHandler(ComponentConfig config)
    {
        super(config);
        //just check if attribute has been defined
        getRequiredAttribute("eventName");
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c,
            UIComponent parent)
    {
        /*
        c.subscribeToEvent(PostAddToViewEvent.class, new SuscribeSetOnEventAjaxListener());
        */
    }
    
    /*
    public static final class SetOnEventAjaxListener 
        implements ComponentSystemEventListener, StateHolder
    {
        private String eventName;
        
        public SetOnEventAjaxListener()
        {
        }
        
        public SetOnEventAjaxListener(String eventName)
        {
            this.eventName = eventName;
        }

        public void processEvent(ComponentSystemEvent event)
        {
            ClientBehaviorHolder target = (ClientBehaviorHolder) event.getComponent();
            
            List<ClientBehavior> list = target.getClientBehaviors().get(this.eventName);
            
            if (list != null)
            {
                for (ClientBehavior cb : list)
                {
                    if (cb instanceof AjaxBehavior)
                    {
                        String functionName = 
                                "tomahawkOnEventAjax_"+eventName+"_"+JavascriptUtils.getValidJavascriptName(event.getComponent().getClientId(),false);
                        
                        ((AjaxBehavior) cb).setOnevent(functionName);
                    }
                }
            }
        }

        public boolean isTransient()
        {
            return false;
        }

        public void setTransient(boolean arg0)
        {
        }
        
        public void restoreState(FacesContext context, Object state)
        {
            this.eventName = (String) state;
        }

        public Object saveState(FacesContext context)
        {
            return eventName;
        }
    }

    public static final class SuscribeSetOnEventAjaxListener 
        implements ComponentSystemEventListener
    {
        //Required because Mojarra call PostAddToViewEvent
        private transient boolean processed = false;
        
        public void processEvent(ComponentSystemEvent event)
        {
            AbstractOnEventAjaxComponent c = (AbstractOnEventAjaxComponent) event.getComponent();
            
            UIComponent target = ( c.getForId() != null ) ? 
                    c.findComponent(c.getForId()) : 
                        null;

            if (target != null && !processed)
            {
                target.subscribeToEvent(PreRenderComponentEvent.class, new SetOnEventAjaxListener(c.getEventName()));
                processed = true;
            }
        }
    }
    */
}
