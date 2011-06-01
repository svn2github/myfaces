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
 * Convenient wrapper for scriptaculous. The properties here are common to all effects 
 * 
 * @author Leonardo Uribe
 *
 */
@JSFClientBehavior
public abstract class EffectClientBehaviorBase extends ClientBehaviorBase
{

    public EffectClientBehaviorBase()
    {
    }

    /**
     * 
     * @return
     */
    @JSFProperty
    public String getForId()
    {
        return (String) getStateHelper().eval(PropertyKeys.forId);
    }
    
    public void setForId(String forId)
    {
        getStateHelper().put(PropertyKeys.forId, forId ); 
    }

    /**
     * 
     * @return
     */
    @JSFProperty
    public String getAppendJs()
    {
        return (String) getStateHelper().eval(PropertyKeys.appendJs);
    }
    
    public void setAppendJs(String appendJs)
    {
        getStateHelper().put(PropertyKeys.appendJs, appendJs ); 
    }

    enum PropertyKeys
    {
         forId
         , appendJs
    }
}
