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
 * Convenient wrapper for scriptaculous Effect.Move 
 * 
 * @author Leonardo Uribe
 *
 */
@JSFClientBehavior(
        name="s:effectMove",
        clazz="org.apache.myfaces.custom.effect.EffectMoveBehavior",
        bodyContent="empty")
public abstract class AbstractEffectMoveClientBehavior extends EffectClientBehaviorBase
    implements JsEffectCallbackTarget
{
    public static final String BEHAVIOR_ID = "org.apache.myfaces.custom.effect.EffectMoveBehavior";
    public static final String RENDERER_TYPE = "org.apache.myfaces.custom.effect.EffectMoveBehavior";

    public AbstractEffectMoveClientBehavior()
    {
    }
    
    /**
     * integer value, either the new absolute target of the effect 
     * elements left value or the modifier of its current left value, 
     * depending on the mode option
     * 
     * @return
     */
    @JSFProperty
    public abstract Integer getX();

    /**
     * integer value, either the new absolute target of the effect 
     * elements top value or the modifier of its current top value, 
     * depending on the mode option
     * 
     * @return
     */
    @JSFProperty
    public abstract Integer getY();

    /**
     * string, defaults to 'relative', can also be 'absolute', 
     * specifies if the element is moved absolutely or relative to 
     * its own position.
     * 
     * @return
     */
    @JSFProperty
    public abstract String getMode();
}
