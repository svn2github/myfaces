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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Locale;

/**
 * The redirect tracker maintains a list/map of data needed to restore the myfaces
 * system after a navigation redirect
 */
public class RedirectTrackerManager
{
	private final static Log log = LogFactory.getLog(RedirectTrackerManager.class);

	public static final String SESSION_KEY = RedirectTrackerManager.class.getName();
	public static final String REDIRECT_ARG = "_rtid";
	public static final int TRACK_REDIRECTS = 20; // TODO: make configureable

	private final int redirects;
	private final Map redirectEntryMap;
	private final List redirectEntryList;
	private final Map requestBeanMap = new TreeMap();

	private long requests = 0;

	private static class Entry
	{
		private final String mapKey;
		private List messages;
		private Map beanMap;
		private Locale locale;

		private Entry(String mapKey)
		{
			this.mapKey = mapKey;
		}

		void addMessage(Object clientId, Object message)
		{
			if (messages == null)
			{
				messages = new ArrayList();
			}
			messages.add(new MessageEntry(clientId, message));
		}

		public Iterator getMessages()
		{
			if (messages == null)
			{
				return null;
			}

			return messages.iterator();
		}

		public boolean hasCapturedData()
		{
			return (messages != null && messages.size() > 0)
				|| (beanMap != null && beanMap.size() > 0)
				|| locale != null;
		}
	}

	private static class MessageEntry
	{
		private final Object clientId;
		private final Object message;

		public MessageEntry(Object clientId, Object message)
		{
			this.clientId = clientId;
			this.message = message;
		}
	}

	/**
	 * Instantiate the tracker
	 *
	 * @param redirects nuof redirects to track
	 */
	public RedirectTrackerManager(int redirects)
	{
		this.redirects = redirects;
		redirectEntryMap = new TreeMap();
		redirectEntryList = new ArrayList(redirects);
	}

	/**
	 * check if this was a redirect, and if, process it
	 */
	public void processTrackedRequest(FacesContext facesContext)
	{
		Object rtid = facesContext.getExternalContext().getRequestParameterMap().get(REDIRECT_ARG);
		if (!isRedirectedRequest(rtid))
		{
			return;
		}

		setupFaces(facesContext, rtid);
	}

	/**
	 * check to see if the request parameter contains the identifier to the saved states
	 */
	protected boolean isRedirectedRequest(Object rtid)
	{
		if (rtid == null)
		{
			return false;
		}

		synchronized (redirectEntryMap)
		{
			return redirectEntryMap.containsKey(rtid);
		}
	}

	/**
	 * access the redirect tracker
	 */
	public static RedirectTrackerManager getInstance(FacesContext facesContext)
	{
		Map sessionMap = facesContext.getExternalContext().getSessionMap();
		if (sessionMap == null)
		{
			return null;
		}
		RedirectTrackerManager redirectManager = (RedirectTrackerManager) sessionMap.get(SESSION_KEY);
		if (redirectManager == null)
		{
			redirectManager = createRedirectTrackerManager();
			sessionMap.put(SESSION_KEY, redirectManager);
		}

		return redirectManager;
	}

	/**
	 * create a new redirect tracker
	 */
	protected static RedirectTrackerManager createRedirectTrackerManager()
	{
		return new RedirectTrackerManager(TRACK_REDIRECTS);
	}

	/**
	 * add the current state to the redirect map/list
	 * @return the new path used for redirect
	 */
	public String trackRedirect(FacesContext facesContext, String redirectPath)
	{
		long rtid = getNextRequestNo();
		String mapKey = Long.toString(rtid, Character.MAX_RADIX);

		Entry entry = new Entry(mapKey);

		saveBeans(entry);
		saveMessages(facesContext, entry);

		// prepare for next redirect
		clearSaveStateBean();

		if (!entry.hasCapturedData())
		{
			// nothing to restore
			return redirectPath;
		}

		synchronized (redirectEntryMap)
		{
			redirectEntryMap.put(mapKey, entry);
			redirectEntryList.add(entry);

			while (redirectEntryList.size() > redirects)
			{
				Entry prevEntry = (Entry) redirectEntryList.remove(0);
				redirectEntryMap.remove(prevEntry.mapKey);
			}
		}

		if (redirectPath.indexOf('?') == -1)
		{
			return redirectPath + "?" + REDIRECT_ARG + "=" + rtid;
		}
		else
		{
			return redirectPath + "&" + REDIRECT_ARG + "=" + rtid;
		}
	}

	protected void saveBeans(Entry entry)
	{
		entry.beanMap = new TreeMap(requestBeanMap);
	}

	/**
	 * Add the object to the current request holder
	 */
	public void addSaveStateBean(String expressionString, Object value)
	{
		if (log.isDebugEnabled())
		{
			log.debug("addSaveStateBean: " + expressionString + " value=" + value);
		}
		requestBeanMap.put(expressionString, value);
	}

	/**
	 * request done, clear saveState beanmap
	 */
	public void clearSaveStateBean()
	{
		requestBeanMap.clear();
	}

	protected void saveMessages(FacesContext facesContext, Entry entry)
	{
		Iterator iterClientIds = facesContext.getClientIdsWithMessages();
		while (iterClientIds.hasNext())
		{
			String clientId = (String) iterClientIds.next();
			Iterator iterMessages = facesContext.getMessages(clientId);
			while (iterMessages.hasNext())
			{
				Object message = iterMessages.next();

				if (log.isDebugEnabled())
				{
					log.debug("saveMessage: " + message);
				}

				entry.addMessage(clientId, message);
			}
		}
	}

	protected void restoreMessages(FacesContext facesContext, Entry entry)
	{
		Iterator iterMessages = entry.getMessages();
		if (iterMessages == null)
		{
			return;
		}

		while (iterMessages.hasNext())
		{
			MessageEntry message = (MessageEntry) iterMessages.next();
			facesContext.addMessage((String) message.clientId, (FacesMessage) message.message);
		}
	}

	protected void saveLocale(FacesContext facesContext, Entry entry)
	{
		if (facesContext.getViewRoot() != null && facesContext.getViewRoot().getLocale() != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("saveLocale: " + facesContext.getViewRoot().getLocale());
			}

			entry.locale = facesContext.getViewRoot().getLocale();
		}
	}

	protected void restoreLocale(FacesContext facesContext, Entry entry)
	{
		if (entry.locale != null && facesContext.getViewRoot() != null)
		{
			facesContext.getViewRoot().setLocale(entry.locale);
		}
	}

	/**
	 * resetup faces after redirect request
	 */
	protected void setupFaces(FacesContext facesContext, Object rtid)
	{
		Entry entry;
		synchronized (redirectEntryMap)
		{
			entry = (Entry) redirectEntryMap.get(rtid);
		}

		if (entry == null)
		{
			// entry lost?
			return;
		}

		restoreLocale(facesContext, entry);
		restoreMessages(facesContext, entry);
		restoreBeans(facesContext, entry);
	}

	protected void restoreBeans(FacesContext facesContext, Entry entry)
	{
		Iterator iterBeanMap = entry.beanMap.entrySet().iterator();
		while (iterBeanMap.hasNext())
		{
			Map.Entry bean = (Map.Entry) iterBeanMap.next();

			String beanName = bean.getKey().toString();

			if (log.isDebugEnabled())
			{
				log.debug("restore bean: " + beanName + " value=" + bean.getValue());
			}

			if (beanName.startsWith("#{") && beanName.endsWith("}"))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(bean.getKey().toString());
				vb.setValue(facesContext, bean.getValue());
			}
			else
			{
				facesContext.getExternalContext().getRequestMap().put(beanName, bean.getValue());

				// if we add the bean directly to the request map, also put it again into our own list
				addSaveStateBean(beanName, bean.getValue());
			}
		}
	}

	/**
	 * get the next request number
	 */
	protected synchronized long getNextRequestNo()
	{
		requests++;
		return requests;
	}
}