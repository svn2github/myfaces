/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.behavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFClientBehavior;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;

/**
 * Base behavior implementation for Apache MyFaces Tomahawk.
 *
 */
@JSFClientBehavior(
        configExcluded = true,
        evaluateELOnExecution = true,
        tagHandler = "org.apache.myfaces.custom.behavior.ClientBehaviorBaseTagHandler")
public abstract class ClientBehaviorBase extends
        javax.faces.component.behavior.ClientBehaviorBase
{

    private transient FacesContext _facesContext;
    private StateHelper _stateHelper = null;

    /**
     * @param context
     */
    @Override
    public Object saveState(FacesContext facesContext)
    {
        if (initialStateMarked())
        {
            Object parentSaved = super.saveState(facesContext);
            Object stateHelperSaved = null;
            StateHelper stateHelper = getStateHelper(false);
            if (stateHelper != null)
            {
                stateHelperSaved = stateHelper.saveState(facesContext);
            }

            if (parentSaved == null && stateHelperSaved == null)
            {
                //No values
                return null;
            }
            return new Object[] { parentSaved, stateHelperSaved };
        }
        else
        {
            Object[] values = new Object[2];
            values[0] = super.saveState(facesContext);
            StateHelper stateHelper = getStateHelper(false);
            if (stateHelper != null)
            {
                values[1] = stateHelper.saveState(facesContext);
            }
            return values;
        }
    }

    @Override
    public void restoreState(FacesContext facesContext, Object o)
    {
        if (o == null)
        {
            return;
        }
        Object[] values = (Object[]) o;
        if (values[0] != null)
        {
            super.restoreState(facesContext, values[0]);
        }
        getStateHelper().restoreState(facesContext, values[1]);
    }

    // --------------------- borrowed from UIComponentBase ------------

    @SuppressWarnings("unchecked")
    public ValueExpression getValueExpression(String name)
    {
        if (name == null)
            throw new NullPointerException("name");
        StateHelper helper = getStateHelper(false);
        if (helper == null)
        {
            return null;
        }
        Map<String, Object> bindings = (Map<String, Object>) helper
                .get(PropertyKeys.bindings);
        if (bindings == null)
        {
            return null;
        }
        else
        {
            return (ValueExpression) bindings.get(name);
        }
    }

    public void setValueExpression(String name, ValueExpression expression)
    {
        if (name == null)
            throw new NullPointerException("name");
        if (expression == null)
        {
            getStateHelper().remove(PropertyKeys.bindings, name);
        }
        else
        {
            getStateHelper().put(PropertyKeys.bindings, name, expression);
        }
    }

    /**
     * Serializes objects which are "attached" to this component but which are
     * not UIComponent children of it. Examples are validator and listener
     * objects. To be precise, it returns an object which implements
     * java.io.Serializable, and which when serialized will persist the
     * state of the provided object.
     * <p>
     * If the attachedObject is a List then every object in the list is saved
     * via a call to this method, and the returned wrapper object contains
     * a List object.
     * <p>
     * If the object implements StateHolder then the object's saveState is
     * called immediately, and a wrapper is returned which contains both
     * this saved state and the original class name. However in the case
     * where the StateHolder.isTransient method returns true, null is
     * returned instead.
     * <p>
     * If the object implements java.io.Serializable then the object is simply
     * returned immediately; standard java serialization will later be used
     * to store this object.
     * <p>
     * In all other cases, a wrapper is returned which simply stores the type
     * of the provided object. When deserialized, a default instance of that
     * type will be recreated.
     */
    public static Object saveAttachedState(FacesContext context,
            Object attachedObject)
    {
        if (context == null)
        {
            throw new NullPointerException("context");
        }

        if (attachedObject == null)
            return null;
        // StateHolder interface should take precedence over
        // List children
        if (attachedObject instanceof StateHolder)
        {
            StateHolder holder = (StateHolder) attachedObject;
            if (holder.isTransient())
            {
                return null;
            }

            return new _AttachedStateWrapper(attachedObject.getClass(),
                    holder.saveState(context));
        }
        else if (attachedObject instanceof List)
        {
            List<Object> lst = new ArrayList<Object>(
                    ((List<?>) attachedObject).size());
            for (Object item : (List<?>) attachedObject)
            {
                if (item != null)
                {
                    lst.add(saveAttachedState(context, item));
                }
            }

            return new _AttachedListStateWrapper(lst);
        }
        else if (attachedObject instanceof Serializable)
        {
            return attachedObject;
        }
        else
        {
            return new _AttachedStateWrapper(attachedObject.getClass(), null);
        }
    }

    @SuppressWarnings("unchecked")
    public static Object restoreAttachedState(FacesContext context,
            Object stateObj) throws IllegalStateException
    {
        if (context == null)
            throw new NullPointerException("context");
        if (stateObj == null)
            return null;
        if (stateObj instanceof _AttachedListStateWrapper)
        {
            List<Object> lst = ((_AttachedListStateWrapper) stateObj)
                    .getWrappedStateList();
            List<Object> restoredList = new ArrayList<Object>(lst.size());
            for (Object item : lst)
            {
                restoredList.add(restoreAttachedState(context, item));
            }
            return restoredList;
        }
        else if (stateObj instanceof _AttachedStateWrapper)
        {
            Class<?> clazz = ((_AttachedStateWrapper) stateObj).getClazz();
            Object restoredObject;
            try
            {
                restoredObject = clazz.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(
                        "Could not restore StateHolder of type "
                                + clazz.getName()
                                + " (missing no-args constructor?)", e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            if (restoredObject instanceof StateHolder)
            {
                _AttachedStateWrapper wrapper = (_AttachedStateWrapper) stateObj;
                Object wrappedState = wrapper.getWrappedStateObject();

                StateHolder holder = (StateHolder) restoredObject;
                holder.restoreState(context, wrappedState);
            }
            return restoredObject;
        }
        else
        {
            return stateObj;
        }
    }

    protected FacesContext getFacesContext()
    {
        if (_facesContext == null)
        {
            return FacesContext.getCurrentInstance();
        }
        else
        {
            return _facesContext;
        }
    }

    boolean isCachedFacesContext()
    {
        return _facesContext != null;
    }

    void setCachedFacesContext(FacesContext facesContext)
    {
        _facesContext = facesContext;
    }

    protected StateHelper getStateHelper()
    {
        return getStateHelper(true);
    }

    /**
     * returns a delta state saving enabled state helper
     * for the current component
     * @param create if true a state helper is created if not already existing
     * @return an implementation of the StateHelper interface or null if none exists and create is set to false
     */
    protected StateHelper getStateHelper(boolean create)
    {
        if (_stateHelper != null)
        {
            return _stateHelper;
        }
        if (create)
        {
            _stateHelper = new _DeltaStateHelper(this);
        }
        return _stateHelper;
    }
    
    /**
     * The event that this client behavior should be attached.
     * 
     * @return
     */
    @JSFProperty
    private String getEvent()
    {
        return null;
    }

    enum PropertyKeys
    {
        bindings
    }
}
