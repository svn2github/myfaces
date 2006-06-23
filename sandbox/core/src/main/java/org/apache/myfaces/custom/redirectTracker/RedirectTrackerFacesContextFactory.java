/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.custom.redirectTracker;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

/**
 * decoreates the external context to allow to intercept its redirect method.
 */
public class RedirectTrackerFacesContextFactory extends FacesContextFactory
{
	private final FacesContextFactory original;

	public RedirectTrackerFacesContextFactory(FacesContextFactory original)
	{
		this.original = original;
	}

	public FacesContext getFacesContext(Object context,
										Object request,
										Object response,
										Lifecycle lifecycle) throws FacesException
	{
		final FacesContext facesContext = original.getFacesContext(context, request, response, lifecycle);

		return new RedirectTrackerFacesContextWrapper(facesContext);
	}

	private static class RedirectTrackerFacesContextWrapper extends FacesContextWrapper
	{
		private final ExternalContext externalContextWrappper;

		public RedirectTrackerFacesContextWrapper(final FacesContext facesContext)
		{
			super(facesContext);
			externalContextWrappper = new RedirectTrackerExternalContextWrapper(facesContext.getExternalContext());

			FacesContext.setCurrentInstance(this);
		}

		public ExternalContext getExternalContext()
		{
			return externalContextWrappper;
		}
	}
}
