package org.apache.myfaces.component.html.ext;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTablePhaseListener implements PhaseListener
{
	private static final Log log = LogFactory.getLog(HtmlDataTablePhaseListener.class);

	public HtmlDataTablePhaseListener()
	{
		if (log.isDebugEnabled())
			log.debug("New HtmlDataTablePhaseListener");
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

	public void afterPhase(PhaseEvent event)
	{
	}

	public void beforePhase(PhaseEvent event)
	{
		FacesContext facesContext = event.getFacesContext();
		UIViewRoot viewRoot = facesContext.getViewRoot();
		recurseFacetsAndChildren(facesContext, viewRoot.getFacetsAndChildren());
	}

	protected void recurseFacetsAndChildren(FacesContext facesContext, Iterator iterator)
	{
		while (iterator.hasNext())
		{
			UIComponent comp = (UIComponent) iterator.next();
			if (comp instanceof HtmlDataTable)
			{
				//((HtmlDataTable)comp).refresh(facesContext);
			}
			recurseFacetsAndChildren(facesContext, comp.getFacetsAndChildren());
		}
	}

}
