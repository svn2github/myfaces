package org.apache.myfaces.examples.debug;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * shows logger messages for each phase of the FacesLifecycle.
 * @author matzew
 *
 */
public class DebugPhaseListener implements PhaseListener
{

	private static Log log = LogFactory.getLog(DebugPhaseListener.class);
	
	public void afterPhase(PhaseEvent event)
	{
		if(log.isInfoEnabled())
		{
			log.info("AFTER PHASE " + event.getPhaseId());
		}
	}

	public void beforePhase(PhaseEvent event)
	{
		if(log.isInfoEnabled())
		{
			log.info("BEFORE PHASE " + event.getPhaseId());
		}
	
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}