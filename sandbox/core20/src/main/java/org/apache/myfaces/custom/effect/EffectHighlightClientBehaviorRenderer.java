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

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFClientBehaviorRenderer;

@JSFClientBehaviorRenderer(
        renderKitId = "HTML_BASIC",
        type = "org.apache.myfaces.custom.effect.EffectHighlightBehavior")
@ResourceDependencies({
    @ResourceDependency(library="oam.custom.prototype", name="prototype.js"),
    @ResourceDependency(library="oam.custom.prototype", name="effects.js")
})
public class EffectHighlightClientBehaviorRenderer extends ClientBehaviorRenderer
{
    /**
     * Effect.Fade('id_of_element', { duration: 3.0 });
     */
    @Override
    public String getScript(ClientBehaviorContext behaviorContext,
            ClientBehavior behavior)
    {
        AbstractEffectHighlightClientBehavior effectBehavior = (AbstractEffectHighlightClientBehavior) behavior;
        
        UIComponent target = ( effectBehavior.getForId() != null ) ? 
                behaviorContext.getComponent().findComponent(effectBehavior.getForId()) : 
                    behaviorContext.getComponent();
        if (target == null)
        {
            throw new FacesException("Cannot find component set with forId: "+effectBehavior.getForId());
        }
        String clientId = target.getClientId();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("new Effect.Highlight('");
        sb.append(clientId);
        sb.append("'");
        if (EffectUtils.isAnyPropertySet(
                effectBehavior.getKeepBackgroundImage(),
                effectBehavior.getRestorecolor(),
                effectBehavior.getStartcolor(),
                effectBehavior.getEndcolor(),
                EffectUtils.isAnyJsEffectCallbackTargetPropertySet(effectBehavior)
                ))
        {
            sb.append(",{");
            boolean addComma = false;
            addComma = EffectUtils.addProperty(sb, "keepBackgroundImage", effectBehavior.getKeepBackgroundImage(), addComma);
            addComma = EffectUtils.addStringProperty(sb, "restorecolor", effectBehavior.getRestorecolor(), addComma);
            addComma = EffectUtils.addStringProperty(sb, "startcolor", effectBehavior.getStartcolor(), addComma);
            addComma = EffectUtils.addStringProperty(sb, "endcolor", effectBehavior.getEndcolor(), addComma);
            //Javascript callbacks
            addComma = EffectUtils.addJSCallbacks(sb, effectBehavior, addComma);
            
            sb.append('}');
        }
        sb.append(')');
        
        if (effectBehavior.getAppendJs() != null && effectBehavior.getAppendJs().length() > 0)
        {
            sb.append(';');
            sb.append(effectBehavior.getAppendJs());
        }

        return sb.toString();
    }
    
}
