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

package org.apache.myfaces.custom.scope;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * phase listener class which does
 * the needed scope cleanup operations
 *
 * @author Werner Punz werpu@gmx.at
 * @version $Revision$ $Date$
 */
public class AfterScopePhaseListener implements PhaseListener {

	/**
     *
     */
    private static final long serialVersionUID = 9137086632177423625L;

    /**
	 * we have to tackle the scope cleanup
	 * at the latest stage possible
	 * thus we intercept it at the last phase
	 * at the after phase stage
	 */
	public void afterPhase(PhaseEvent arg0) {
		ScopeHolder holder = (ScopeHolder) ScopeUtils.getManagedBean(UIScope.SCOPE_CONTAINER_KEY);
		if(holder != null)
		    holder.pageRefresh();
	}

	public void beforePhase(PhaseEvent arg0) {
	}

	/**
	 * last phase possible, the render responce phase
	 */
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
