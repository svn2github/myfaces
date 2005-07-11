package org.apache.myfaces.custom.datascroller;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 * @author MBroekelmann
 *
 */
public class ScrollerActionEvent extends ActionEvent
{
	private static final long serialVersionUID = -5692343289423906802L;

	private final String mScrollerfacet;

	private final int mPageIndex;

	/**
	 * @param component
	 * @param scrollerfacet
	 */
	public ScrollerActionEvent(UIComponent component, String scrollerfacet)
	{
		super(component);
		mScrollerfacet = scrollerfacet;
		mPageIndex = -1;
	}

	/**
	 *
	 */
	public ScrollerActionEvent(UIComponent component, int pageIndex)
	{
		super(component);
		if (pageIndex < 0)
		{
			throw new IllegalArgumentException("wrong pageindex");
		}
		mPageIndex = pageIndex;
		mScrollerfacet = null;
	}

	/**
	 * @return Returns the scrollerfacet.
	 */
	public String getScrollerfacet()
	{
		return mScrollerfacet;
	}

	/**
	 * @return int
	 */
	public int getPageIndex()
	{
		return mPageIndex;
	}
}
