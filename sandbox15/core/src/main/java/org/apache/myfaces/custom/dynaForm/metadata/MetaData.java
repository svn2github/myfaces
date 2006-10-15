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
package org.apache.myfaces.custom.dynaForm.metadata;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.persistence.TemporalType;

import org.apache.myfaces.custom.dynaForm.guiBuilder.ComponentEnum;
import org.apache.myfaces.custom.dynaForm.lib.SelectionSourceEnum;

/**
 * Holds all the metadata
 */
public class MetaData implements MetaDataInterface
{
	private final Set<String> requestedFields = new TreeSet<String>();
	private final Set<String> requestedFieldParents = new TreeSet<String>();
	private final Map<String, FieldImpl> fields = new LinkedHashMap<String, FieldImpl>();

	private boolean lockFields = false;

	/**
	 * Metadata for a field
	 */
	public static class FieldImpl implements FieldInterface, Serializable
	{
		private final String name;
		private final String baseName;
		private String preferredExternalName;
		private Class type = null;
		private boolean entityType = false;
		private Boolean canRead = null;
		private Boolean canWrite = null;
		private Boolean disabled = null;
		private Boolean displayOnly = null;
		private boolean required = false;
		private RelationType relationType = RelationType.NONE;
		private boolean embedded = true;
		private Integer displaySize = null;
		private Integer minSize = null;
		private Integer maxSize = null;
		private Double minValue = null;
		private Double maxValue = null;
		private TemporalType temporalType = null;

		private Selection[] allowedSelection = null;
		private boolean allowMultipleSelections = false;
		private SelectionSourceEnum selectionSource = null;

		private UIComponent wantedComponent = null;
		private ComponentEnum wantedComponentType = ComponentEnum.Automatic;

		protected FieldImpl(String name)
		{
			this.name = name;
			int pos = name.lastIndexOf('.');
			if (pos > -1)
			{
				this.baseName = name.substring(pos+1);
			}
			else
			{
				this.baseName = name;
			}
		}

		public String getName()
		{
			return name;
		}

		public String getBaseName()
		{
			return baseName;
		}

		public String getExternalName()
		{
			if (getPreferredExternalName() != null)
			{
				return getPreferredExternalName();
			}

			return getName();
		}

		public String getPreferredExternalName()
		{
			return preferredExternalName;
		}

		public void setPreferredExternalName(String preferredExternalName)
		{
			this.preferredExternalName = preferredExternalName;
		}

		public Class getType()
		{
			return type;
		}

		public void setType(Class type)
		{
			if (this.type != null && this.type != type)
			{
				throw new IllegalArgumentException("" + name
						+ ": reset with different type denied. curr:"
						+ this.type.getName() + " new:" + type.getName());
			}
			this.type = type;
		}

		public Boolean getDisplayOnly()
		{
			return displayOnly;
		}

		public void setDisplayOnly(Boolean displayOnly)
		{
			this.displayOnly = displayOnly;
		}

		public boolean isCanRead()
		{
			return getCanRead() != null && getCanRead().booleanValue();
		}

		public void setCanRead(boolean canRead)
		{
			this.canRead = canRead;
		}

		public Boolean getCanRead()
		{
			return this.canRead;
		}

		public void setCanWrite(boolean canWrite)
		{
			this.canWrite = canWrite;
		}

		public Boolean getCanWrite()
		{
			return canWrite;
		}

		public void setDisabled(boolean disabled)
		{
			this.disabled = disabled;
		}

		public Boolean getDisabled()
		{
			return disabled;
		}

		public boolean getRequired()
		{
			return required;
		}

		public void setRequired(boolean nullable)
		{
			this.required = nullable;
		}

		public Selection[] getAllowedSelections()
		{
			return allowedSelection;
		}

		public void setAllowedSelections(Selection[] allowedSelections)
		{
			this.allowedSelection = allowedSelections;
		}

		public RelationType getRelationType()
		{
			return relationType;
		}

		public void setRelationType(RelationType relationType)
		{
			this.relationType = relationType;
		}

		public Integer getMaxSize()
		{
			return maxSize;
		}

		public void setMaxSize(Integer maxSize)
		{
			this.maxSize = maxSize;
		}

		public Double getMaxValue()
		{
			return maxValue;
		}

		public void setMaxValue(Double maxValue)
		{
			this.maxValue = maxValue;
		}

		public Integer getMinSize()
		{
			return minSize;
		}

		public void setMinSize(Integer minSize)
		{
			this.minSize = minSize;
		}

		public Double getMinValue()
		{
			return minValue;
		}

		public void setMinValue(Double minValue)
		{
			this.minValue = minValue;
		}

		public void setWantedComponent(UIComponent component)
		{
			this.wantedComponent = component;
		}

		public UIComponent getWantedComponent()
		{
			return wantedComponent;
		}

		public void setWantedComponentType(ComponentEnum componentType)
		{
			this.wantedComponentType = componentType;
		}

		public ComponentEnum getWantedComponentType()
		{
			return this.wantedComponentType;
		}

		public void setDisplaySize(int displaySize)
		{
			this.displaySize = displaySize;
		}

		public Integer getDisplaySize()
		{
			return displaySize;
		}

		public void setTemporalType(TemporalType temporalType)
		{
			this.temporalType = temporalType;
		}

		public TemporalType getTemporalType()
		{
			return temporalType;
		}

		public boolean getAllowMultipleSelections()
		{
			return allowMultipleSelections;
		}

		public void setAllowMultipleSelections(boolean allowMultipleSelections)
		{
			this.allowMultipleSelections = allowMultipleSelections;
		}

		public SelectionSourceEnum getSelectionSource()
		{
			return selectionSource;
		}

		public void setSelectionSource(SelectionSourceEnum selectionSource)
		{
			this.selectionSource = selectionSource;
		}

		public boolean isEntityType()
		{
			return entityType;
		}

		public void setEntityType(boolean entityType)
		{
			this.entityType = entityType;
		}

		public boolean isEmbedded()
		{
			return embedded;
		}

		public void setEmbedded(boolean embedded)
		{
			this.embedded = embedded;
		}
	}

	public MetaData()
	{
	}

	/**
	 * should this field be processed
	 *
	 * @see #setLockFields(boolean)
	 */
	public boolean processField(String name)
	{
		return !lockFields || processFieldParent(name) || requestedFields.contains(name) || fields.containsKey(name);
	}

	/**
	 * should this field be processed.
	 *
	 * @return true if the given name is the parent of one of the requestedFields
	 * @see #processField(String)
	 * @see #setLockFields(boolean)
	 */
	public boolean processFieldParent(String name)
	{
		return requestedFieldParents.contains(name);
	}

	/**
	 * request to add this field if we reach it. eg. used to trigger traversing the object graph for
	 * linked entities
	 */
	public void requestField(String name)
	{
 		int currIndex = name.indexOf('.');
		while (currIndex > -1)
		{
			String key = name.substring(0, currIndex);
			if (!requestedFieldParents.contains(key))
			{
				requestedFieldParents.add(key);
			}
			currIndex = name.indexOf('.', currIndex+1);
		}

		requestedFields.add(name);
	}

	public Set<String> getRequestedFields()
	{
		return Collections.unmodifiableSet(requestedFields);
	}

	/**
	 * add a new field to the metadata or return one if one already exists for
	 * the given name
	 */
	public FieldImpl getOrCreateField(String name)
	{
		if (!processField(name))
		{
			throw new SecurityException("Current state do not allow to add the field: " + name);
		}

		FieldImpl field = fields.get(name);
		if (field == null)
		{
			field = new FieldImpl(name);
			fields.put(name, field);
		}
		return field;
	}

	public int getFieldCount()
	{
		return fields.size();
	}

	public Iterator<String> iterFieldNames()
	{
		return fields.keySet().iterator();
	}

	public FieldInterface getField(String name)
	{
		return fields.get(name);
	}

	public String[] getFieldNames()
	{
		return fields.keySet().toArray(new String[fields.size()]);
	}

	/**
	 * if set to true this avoids any field to be newly created, only already existent fields are to be processed
	 */
	public boolean setLockFields(boolean lockFields)
	{
		boolean prev = this.lockFields;
		this.lockFields = lockFields;
		return prev;
	}
}
