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

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;

/**
 * Provide common properties for handle javascript effects callbacks
 * 
 * @author Leonardo Uribe
 *
 */
public interface JsEffectCallbackTarget
{
    /**
     * Javascript function callback called before the effects rendering loop is started, or as soon as it is added to a queue.
     */
    @JSFProperty
    public abstract String getBeforeStart();
    
    /**
     * Javascript function callback called before the effect is setup.
     */
    @JSFProperty
    public abstract String getBeforeSetup();
    
    /**
     * Javascript function callback called after the effect is setup and immediately before the main effects rendering loop is started.
     */
    @JSFProperty
    public abstract String getAfterSetup();
    
    /**
     * Javascript function callback called on each iteration of the effects rendering loop, before the redraw takes place.
     */
    @JSFProperty
    public abstract String getBeforeUpdate();
    
    /**
     * Javascript function callback called on each iteration of the effects rendering loop, after the redraw takes place.
     */
    @JSFProperty
    public abstract String getAfterUpdate(); 

    /**
     * Javascript function callback called on each iteration of the effects rendering loop, after the redraw takes place. 
     * 
     */
    @JSFProperty
    public abstract String getAfterFinish();

}
