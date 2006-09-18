package org.apache.myfaces.custom.redirectTracker.policy;

import org.apache.myfaces.custom.redirectTracker.RedirectTrackerPolicy;
import org.apache.myfaces.custom.redirectTracker.RedirectTrackerContext;

/**
 * This implementation saves the messages only
 */
public class MessagesRedirectTrackPolicy implements RedirectTrackerPolicy
{
	public String trackRedirect(RedirectTrackerContext redirectContext, String redirectPath)
	{
		redirectContext.saveMessages();

		return redirectPath;
	}
}
