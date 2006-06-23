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

import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * captures beans put into the request scope
 */
class RedirectTrackerRequestMapWrapper implements Map
{
	private final Map original;

	public RedirectTrackerRequestMapWrapper(Map original)
	{
		this.original = original;
	}

	public int size()
	{
		return original.size();
	}

	public boolean isEmpty()
	{
		return original.isEmpty();
	}

	public boolean containsKey(Object key)
	{
		return original.containsKey(key);
	}

	public boolean containsValue(Object value)
	{
		return original.containsValue(value);
	}

	public Object get(Object key)
	{
		return original.get(key);
	}

	public Object put(Object key, Object value)
	{
		RedirectTrackerManager manager = RedirectTrackerManager.getInstance(FacesContext.getCurrentInstance());
		if (manager != null)
		{
			manager.addSaveStateBean(key.toString(), value);
		}

		return original.put(key, value);
	}

	public Object remove(Object key)
	{
		return original.remove(key);
	}

	public void putAll(Map t)
	{
		if (t != null)
		{
			RedirectTrackerManager manager = RedirectTrackerManager.getInstance(FacesContext.getCurrentInstance());
			if (manager != null)
			{
				Iterator iterEntrySet = t.entrySet().iterator();
				while (iterEntrySet.hasNext())
				{
					Entry entry = (Entry) iterEntrySet.next();
					manager.addSaveStateBean(entry.getKey().toString(), entry.getValue());
				}
			}
		}
		original.putAll(t);
	}

	public void clear()
	{
		original.clear();
	}

	public Set keySet()
	{
		return original.keySet();
	}

	public Collection values()
	{
		return original.values();
	}

	public Set entrySet()
	{
		return original.entrySet();
	}

	public boolean equals(Object o)
	{
		return original.equals(o);
	}

	public int hashCode()
	{
		return original.hashCode();
	}
}
