/*
 * Copyright 2004-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.renderkit.html.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Used in AddResourceFactory to check if the extensions filter is correctly configured
 *  
 * @author Mario Ivankovits (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class WebXmlFilterHandler extends DefaultHandler
{
	private final static Set VALID_EXTFLT_PATH = Collections.unmodifiableSet(new TreeSet(Arrays.asList(new String[]
    {
			"/faces/*", "/faces/myFacesExtensionResource/*" 
    })));
    
	// searching for new state
	private final static int SEARCH_STATE = 0;
	// within <filter>
	private final static int FILTER_STATE = 1;
	// within <filter-mapping>
	private final static int FILTER_MAPPING_STATE = 2;
	// within <filter-name>
	private final static int FILTER_DEF_NAME_STATE = 11;
	// within <filter-class>
	private final static int FILTER_CLASS_STATE = 12;
	// within <filter-name>
	private final static int FILTER_MAP_NAME_STATE = 21;
	// within <url-pattern>
	private final static int URL_PATTERN = 22;
	
	private int state = SEARCH_STATE;
	
	private StringBuffer currentFilterName = new StringBuffer();
	private StringBuffer currentFilterClass = new StringBuffer();
	private StringBuffer currentUrlPattern = new StringBuffer();
	
	private final Map foundFilters = new TreeMap();

	protected static class FilterEntry
	{
		private final String filterName;
		private final String filterClass;
		private final Set urlPatterns = new TreeSet();
		
		protected FilterEntry(String filterName, String filterClass)
		{
			this.filterName = filterName;
			this.filterClass = filterClass;
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if ("filter".equals(localName))
		{
			state = FILTER_STATE;
		}
		else if ("filter-mapping".equals(localName))
		{
			state = FILTER_MAPPING_STATE;
		}
		else if (state == FILTER_STATE && "filter-name".equals(localName))
		{
			state = FILTER_DEF_NAME_STATE;
			currentFilterName.setLength(0);
		}
		else if (state == FILTER_STATE && "filter-class".equals(localName))
		{
			state = FILTER_CLASS_STATE;
			currentFilterClass.setLength(0);
		}
		else if (state == FILTER_MAPPING_STATE && "filter-name".equals(localName))
		{
			state = FILTER_MAP_NAME_STATE;
			currentFilterName.setLength(0);
		}
		else if (state == FILTER_MAPPING_STATE && "url-pattern".equals(localName))
		{
			state = URL_PATTERN;
			currentUrlPattern.setLength(0);
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		switch (state)
		{
			case FILTER_DEF_NAME_STATE:
			case FILTER_MAP_NAME_STATE:
				currentFilterName.append(ch, start, length);
				break;
			case FILTER_CLASS_STATE:
				currentFilterClass.append(ch, start, length);
				break;
			case URL_PATTERN:
				currentUrlPattern.append(ch, start, length);
				break;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if ((state == FILTER_STATE && "filter".equals(localName)) ||
			(state == FILTER_MAPPING_STATE && "filter-mapping".equals(localName)))
		{
			state = SEARCH_STATE;
		}
		else if (state == FILTER_DEF_NAME_STATE && "filter-name".equals(localName))
		{
			state = FILTER_STATE;
		}
		else if (state == FILTER_CLASS_STATE && "filter-class".equals(localName))
		{
			state = FILTER_STATE;
			registerFilter();
		}
		else if (state == FILTER_MAP_NAME_STATE && "filter-name".equals(localName))
		{
			state = FILTER_MAPPING_STATE;
		}
		else if (state == URL_PATTERN && "url-pattern".equals(localName))
		{
			state = FILTER_MAPPING_STATE;
			addUrlPattern();
		}
	}

	protected void addUrlPattern()
	{
		String filterName = currentFilterName.toString();
		
		FilterEntry filter = (FilterEntry) foundFilters.get(filterName);
		if (filter == null)
		{
			// should not happen ....
			return;
		}
		
		filter.urlPatterns.add(currentUrlPattern.toString());
	}

	protected void registerFilter()
	{
		String filterClass = currentFilterClass.toString();
		if (!org.apache.myfaces.component.html.util.ExtensionsFilter.class.getName().equals(filterClass) &&
			!org.apache.myfaces.webapp.filter.ExtensionsFilter.class.getName().equals(filterClass))
		{
			// not the extensions filter
			return;
		}
		
		String filterName = currentFilterName.toString();
		
		foundFilters.put(filterName, new FilterEntry(filterName, filterClass));
	}
	
	public boolean isValidEnvironment()
	{
		Iterator iterFilters = foundFilters.values().iterator();
		while (iterFilters.hasNext())
		{
			FilterEntry filterEntry = (FilterEntry) iterFilters.next();
			if (checkFilterPatterns(filterEntry))
			{
				// stop on the first extensions filter which is able to serve our resources
				return true;
			}
		}
		
		return false;
	}

	protected boolean checkFilterPatterns(FilterEntry filterEntry)
	{
		Iterator iterPatterns = filterEntry.urlPatterns.iterator();
		while (iterPatterns.hasNext())
		{
			String urlPattern = (String) iterPatterns.next();
			if (VALID_EXTFLT_PATH.contains(urlPattern))
			{
				return true;
			}
		}
		
		return false;
	}
}
