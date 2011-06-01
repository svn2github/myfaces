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
package org.apache.myfaces.custom.effect;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFClientBehavior;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;
import org.apache.myfaces.custom.behavior.ClientBehaviorBase;

/**
 * Convenient wrapper for scriptaculous Effect.Highlight 
 * 
 * @author Leonardo Uribe
 *
 */
@JSFClientBehavior(
        name="s:effectHighlight",
        clazz="org.apache.myfaces.custom.effect.EffectHighlightBehavior",
        bodyContent="empty")
public abstract class AbstractEffectHighlightClientBehavior extends EffectClientBehaviorBase
    implements JsEffectCallbackTarget
{
    public static final String BEHAVIOR_ID = "org.apache.myfaces.custom.effect.EffectHighlightBehavior";
    public static final String RENDERER_TYPE = "org.apache.myfaces.custom.effect.EffectHighlightBehavior";

    public AbstractEffectHighlightClientBehavior()
    {
    }
    
    /**
     * Sets the color of first frame of the highlight. Defaults to "#ffff99" (a light yellow).
     * 
     * @return
     */
    @JSFProperty
    public abstract String getStartcolor();
    
    /**
     * Sets the color of the last frame of the highlight. This is best set to 
     * the background color of the highlighted element. Defaults to "#ffffff" 
     * (white).
     * 
     * @return
     */
    @JSFProperty
    public abstract String getEndcolor();
    
    /**
     * Sets the background-color of the element after the highlight has finished. 
     * Defaults to the current background-color of the highlighted element 
     * (see Note).
     * 
     * @return
     */
    @JSFProperty
    public abstract String getRestorecolor();

    /**
     * Unless this is set to true, any background image on the element will 
     * not be preserved.
     * 
     * @return
     */
    @JSFProperty
    public abstract Boolean getKeepBackgroundImage();

}
