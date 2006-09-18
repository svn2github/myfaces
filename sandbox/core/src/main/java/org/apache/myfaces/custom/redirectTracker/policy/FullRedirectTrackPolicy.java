package org.apache.myfaces.custom.redirectTracker.policy;

import org.apache.myfaces.custom.redirectTracker.RedirectTrackerPolicy;
import org.apache.myfaces.custom.redirectTracker.RedirectTrackerContext;

/**
 * This implementation tries to save the full request data
 */
public class FullRedirectTrackPolicy implements RedirectTrackerPolicy
{
	public String trackRedirect(RedirectTrackerContext redirectContext, String redirectPath)
	{
		redirectContext.saveLocale();
		redirectContext.saveBeans();
		redirectContext.saveMessages();

		return redirectPath;
	}
}
