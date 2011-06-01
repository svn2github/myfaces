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
 * Convenient wrapper for scriptaculous Effect.Scale 
 * 
 * @author Leonardo Uribe
 *
 */
@JSFClientBehavior(
        name="s:effectScale",
        clazz="org.apache.myfaces.custom.effect.EffectScaleBehavior",
        bodyContent="empty")
public abstract class AbstractEffectScaleClientBehavior extends EffectClientBehaviorBase
    implements JsEffectCallbackTarget
{
    public static final String BEHAVIOR_ID = "org.apache.myfaces.custom.effect.EffectScaleBehavior";
    public static final String RENDERER_TYPE = "org.apache.myfaces.custom.effect.EffectScaleBehavior";

    public AbstractEffectScaleClientBehavior()
    {
    }
    
    /**
     * Percent value used to indicate the final scale. 
     * 
     * @return
     */
    @JSFProperty
    public abstract String getPercent();
    
    /**
     * Sets whether the element should be scaled horizontally, defaults to true.
     * 
     * @return
     */
    @JSFProperty
    public abstract Boolean getScaleX();
    
    /**
     * Sets whether the element should be scaled vertically, defaults to true.
     * 
     * @return
     */
    @JSFProperty
    public abstract Boolean getScaleY();
    
    /**
     * Sets whether content scaling should be enabled, defaults to true.
     * 
     * @return
     */
    @JSFProperty
    public abstract Boolean getScaleContent();
    
    /**
     * If true, scale the element in a way that the center of 
     * the element stays on the same position on the screen, 
     * defaults to false.
     * 
     * @return
     */
    @JSFProperty
    public abstract Boolean getScaleFromCenter();
    
    /**
     * Either 'box' (default, scales the visible area of the element) 
     * or 'contents' (scales the complete element, that is parts 
     * normally only visible by scrolling are taken into account). 
     * You can also precisely control the size the element will 
     * become by assigning the originalHeight and originalWidth 
     * variables to scaleMode. 
     * 
     * Example: scaleMode: { originalHeight: 900, originalWidth: 900 }
     * 
     * @return
     */
    @JSFProperty
    public abstract String getScaleMode();
    
    /**
     * integer value, percentage (0%-100%), defaults to 100
     * 
     * @return
     */
    @JSFProperty
    public abstract Integer getScaleFrom();
    
}
