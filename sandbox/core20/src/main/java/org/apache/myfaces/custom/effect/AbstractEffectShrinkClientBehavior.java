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
 * Convenient wrapper for scriptaculous Effect.Shrink 
 * 
 * @author Leonardo Uribe
 *
 */
@JSFClientBehavior(
        name="s:effectShrink",
        clazz="org.apache.myfaces.custom.effect.EffectShrinkBehavior",
        bodyContent="empty")
public abstract class AbstractEffectShrinkClientBehavior extends EffectClientBehaviorBase
    implements JsEffectCallbackTarget
{
    public static final String BEHAVIOR_ID = "org.apache.myfaces.custom.effect.EffectShrinkBehavior";
    public static final String RENDERER_TYPE = "org.apache.myfaces.custom.effect.EffectShrinkBehavior";

    public AbstractEffectShrinkClientBehavior()
    {
    }
    
    /**
     * float value, in seconds, defaults to 1.0
     * 
     * @return
     */
    @JSFProperty
    public abstract Float getDuration();
    
    /**
     * string, defaults to 'center', can also be: 'top-left', 'top-right', 
     * 'bottom-left', 'bottom-right', the direction to "shrink" the element to
     * 
     * @return
     */
    @JSFProperty
    public abstract String getDirection();
    
}
