/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.tree2;

import org.apache.myfaces.util.MessageUtils;

import javax.faces.component.UIComponentBase;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.FacesListener;
import javax.faces.el.ValueBinding;
import javax.faces.application.FacesMessage;
import javax.faces.application.*;

import java.text.MessageFormat;
import java.io.Serializable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.*;

/**
 * TreeData is a {@link UIComponent} that supports binding data stored in a tree represented
 * by a {@link TreeNode} instance.  During iterative processing over the tree nodes in the
 * data model, the object for the current node is exposed as a request attribute under the key
 * specified by the <code>var</code> property.  {@link javax.faces.render.Renderer}s of this component should use
 * the appropriate {@link Facet} to assist in rendering.
 *
 * @author Sean Schofield
 * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
 * @version $Revision$ $Date$
 */
public class UITreeData extends UIComponentBase implements NamingContainer
{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.Tree2";
    public static final String COMPONENT_FAMILY = "org.apache.myfaces.HtmlTree2";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Tree2";
    private static final String MISSING_NODE = "org.apache.myfaces.tree2.MISSING_NODE";
    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private TreeModel _model;
    private TreeNode _value;
    private String _var;
    private String _nodeId;
    private Map _saved = new HashMap();

    /**
     * Constructor
     */
    public UITreeData()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    // see superclass for documentation
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    // see superclass for documentation
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _value;
        values[2] = _var;
        return ((Object) (values));
    }


    // see superclass for documentation
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);

        _value = (TreeNode)values[1];
        _var = (String)values[2];
    }

    public void queueEvent(FacesEvent event)
    {
        super.queueEvent(new FacesEventWrapper(event, getNodeId(), this));
    }


    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof FacesEventWrapper)
        {
            FacesEventWrapper childEvent = (FacesEventWrapper) event;
            String currNodeId = getNodeId();
            setNodeId(childEvent.getNodeId());
            FacesEvent nodeEvent = childEvent.getFacesEvent();
            nodeEvent.getComponent().broadcast(nodeEvent);
            setNodeId(currNodeId);
            return;
        } 
        else
        {
            super.broadcast(event);
            return;
        }
    }


    // see superclass for documentation
    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;

        _model = null;
        _saved = new HashMap();

        processNodes(context, PROCESS_DECODES, null, 0);

        setNodeId(null);
        decode(context);
    }

    // see superclass for documentation
    public void processValidators(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;

        processNodes(context, PROCESS_VALIDATORS, null, 0);

        setNodeId(null);
    }


    // see superclass for documentation
    public void processUpdates(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;

        processNodes(context, PROCESS_UPDATES, null, 0);

        setNodeId(null);
    }

    // see superclass for documentation
    public String getClientId(FacesContext context)
    {
        String ownClientId = super.getClientId(context);
        if (_nodeId != null)
        {
            return ownClientId + NamingContainer.SEPARATOR_CHAR + _nodeId;
        } else
        {
            return ownClientId;
        }
    }

    // see superclass for documentation
    public void setValueBinding(String name, ValueBinding binding)
    {
        if ("value".equals(name))
        {
            _model = null;
        } else if ("nodeVar".equals(name) || "nodeId".equals(name) || "treeVar".equals(name))
        {
            throw new IllegalArgumentException("name " + name);
        }
        super.setValueBinding(name, binding);
    }

    // see superclass for documentation
    public void encodeBegin(FacesContext context) throws IOException
    {
        /**
         * The renderer will handle most of the encoding, but if there are any
         * error messages queued for the components (validation errors), we
         * do want to keep the saved state so that we can render the node with
         * the invalid value.
         */
        if (!keepSaved(context))
        {
            _saved = new HashMap();
        }

        super.encodeBegin(context);
    }

    /**
     * Sets the value of the TreeData.
     *
     * @param value The new value
     */
    public void setValue(TreeNode value)
    {
        _model = null;
        _value = value;
    }


    /**
     * Gets the value of the TreeData.
     *
     * @return The value
     */
    public Object getValue()
    {
        if (_value != null) return _value;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? vb.getValue(getFacesContext()) : null;
    }

    /**
     * Set the request-scope attribute under which the data object for the current node wil be exposed
     * when iterating.
     *
     * @param var The new request-scope attribute name
     */
    public void setVar(String var)
    {
        _var = var;
    }


    /**
     * Return the request-scope attribute under which the data object for the current node will be exposed
     * when iterating. This property is not enabled for value binding expressions.
     *
     * @return The iterrator attribute
     */
    public String getVar()
    {
        return _var;
    }

    /**
     * Calls through to the {@link TreeModel} and returns the current {@link TreeNode} or <code>null</code>.
     *
     * @return The current node
     */
    public TreeNode getNode()
    {
        TreeModel model = getDataModel();

        if (model == null)
        {
            return null;
        }

        return model.getNode();
    }


    public String getNodeId()
    {
        return _nodeId;
    }


    public void setNodeId(String nodeId)
    {
        saveDescendantState();

        _nodeId = nodeId;

        TreeModel model = getDataModel();
        if (model == null)
        {
            return;
        }
        
        try
        {
            model.setNodeId(nodeId);
        }
        catch (IndexOutOfBoundsException aob)
        {
            /**
             * This might happen if we are trying to process a commandLink for a node that node that no longer 
             * exists.  Instead of allowing a RuntimeException to crash the application, we will add a warning 
             * message so the user can optionally display the warning.  Also, we will allow the user to provide 
             * their own value binding method to be called so they can handle it how they see fit.
             */
            FacesMessage message = MessageUtils.getMessage(MISSING_NODE, new String[] {nodeId});
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(getId(), message);
            
            /** @todo call hook */
            /** @todo figure out whether or not to abort this method gracefully */
        }

        restoreDescendantState();

        Map requestMap = getFacesContext().getExternalContext().getRequestMap();

        if (_var != null)
        {
            if (nodeId == null)
            {
                requestMap.remove(_var);
            } else
            {
                requestMap.put(_var, getNode());
            }
        }
    }

    /**
     * Gets an array of String containing the ID's of all of the {@link TreeNode}s in the path to
     * the specified node.  The path information will be an array of <code>String</code> objects
     * representing node ID's. The array will starting with the ID of the root node and end with
     * the ID of the specified node.
     *
     * @param nodeId The id of the node for whom the path information is needed.
     * @return String[]
     */
    public String[] getPathInformation(String nodeId)
    {
        return getDataModel().getPathInformation(nodeId);
    }

    /**
     * Indicates whether or not the specified {@link TreeNode} is the last child in the <code>List</code>
     * of children.  If the node id provided corresponds to the root node, this returns <code>true</code>.
     *
     * @param nodeId The ID of the node to check
     * @return boolean
     */
    public boolean isLastChild(String nodeId)
    {
        return getDataModel().isLastChild(nodeId);
    }

    /**
     * Returns a previously cached {@link TreeModel}, if any, or sets the cache variable to either the
     * current value (if its a {@link TreeModel}) or to a new instance of {@link TreeModel} (if it's a
     * {@link TreeNode}) with the provided value object as the root node.
     *
     * @return TreeModel
     */
    private TreeModel getDataModel()
    {
        if (_model != null)
        {
            return _model;
        }

        Object value = getValue();
        if (value != null)
        {
            if (value instanceof TreeModel)
            {
                _model = (TreeModel) value;
            } else if (value instanceof TreeNode)
            {
                _model = new TreeModel((TreeNode) value);
            }
        }

        return _model;
    }

    private void processNodes(FacesContext context, int processAction, String parentId, int childLevel)
    {
        UIComponent facet = null;
        setNodeId(parentId != null ? parentId + NamingContainer.SEPARATOR_CHAR + childLevel : "0");
        TreeNode node = getNode();

        facet = getFacet(node.getType());

        if (facet == null)
        {
            throw new IllegalArgumentException("Unable to locate facet with the name: " + node.getType());
        }

        switch (processAction)
        {
            case PROCESS_DECODES:

                facet.processDecodes(context);
                break;

            case PROCESS_VALIDATORS:

                facet.processValidators(context);
                break;

            case PROCESS_UPDATES:

                facet.processUpdates(context);
                break;
        }

        processChildNodes(context, node, processAction);
    }

    /**
     * Process the child nodes of the supplied parent @{link TreeNode}.  This method is protected so that
     * it can be overriden by a subclass that may want to control how child nodes are processed.
     *
     * @param context       FacesContext
     * @param parentNode    The parent node whose children are to be processed
     * @param processAction An <code>int</code> representing the type of action to process
     */
    protected void processChildNodes(FacesContext context, TreeNode parentNode, int processAction)
    {
        int kidId = 0;
        String currId = getNodeId();

        List children = parentNode.getChildren();

        for (int i = 0; i < children.size(); i++)
        {
            processNodes(context, processAction, currId, kidId++);
        }
    }

    /**
     * To support using input components for the nodes (e.g., input fields, checkboxes, and selection
     * lists) while still only using one set of components for all nodes, the state held by the components
     * for the current node must be saved for a new node is selected.
     */
    private void saveDescendantState()
    {
        FacesContext context = getFacesContext();
        Iterator i = getFacets().values().iterator();
        while (i.hasNext())
        {
            UIComponent facet = (UIComponent) i.next();
            saveDescendantState(facet, context);
        }
    }

    /**
     * Overloaded helper method for the no argument version of this method.
     *
     * @param component The component whose state needs to be saved
     * @param context   FacesContext
     */
    private void saveDescendantState(UIComponent component, FacesContext context)
    {
        if (component instanceof EditableValueHolder)
        {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(context);
            SavedState state = (SavedState) _saved.get(clientId);
            if (state == null)
            {
                state = new SavedState();
                _saved.put(clientId, state);
            }
            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
        }

        List kids = component.getChildren();
        for (int i = 0; i < kids.size(); i++)
        {
            saveDescendantState((UIComponent) kids.get(i), context);
        }
    }


    /**
     * Used to configure a new node with the state stored previously.
     */
    private void restoreDescendantState()
    {
        FacesContext context = getFacesContext();
        Iterator i = getFacets().values().iterator();
        while (i.hasNext())
        {
            UIComponent facet = (UIComponent) i.next();
            restoreDescendantState(facet, context);
        }
    }

    /**
     * Overloaded helper method for the no argument version of this method.
     *
     * @param component The component whose state needs to be restored
     * @param context   FacesContext
     */
    private void restoreDescendantState(UIComponent component, FacesContext context)
    {
        String id = component.getId();
        component.setId(id); // forces the cilent id to be reset

        if (component instanceof EditableValueHolder)
        {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(context);
            SavedState state = (SavedState) _saved.get(clientId);
            if (state == null)
            {
                state = new SavedState();
            }
            input.setValue(state.getValue());
            input.setValid(state.isValid());
            input.setSubmittedValue(state.getSubmittedValue());
            input.setLocalValueSet(state.isLocalValueSet());
        }

        List kids = component.getChildren();
        for (int i = 0; i < kids.size(); i++)
        {
            restoreDescendantState((UIComponent)kids.get(i), context);
        }
    }

    /**
     * A regular bean with accessor methods for all state variables.
     *
     * @author Sean Schofield
     * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
     * @version $Revision$ $Date$
     */
    private static class SavedState implements Serializable
    {

        private Object submittedValue;
        private boolean valid = true;
        private Object value;
        private boolean localValueSet;

        Object getSubmittedValue()
        {
            return submittedValue;
        }

        void setSubmittedValue(Object submittedValue)
        {
            this.submittedValue = submittedValue;
        }

        boolean isValid()
        {
            return valid;
        }

        void setValid(boolean valid)
        {
            this.valid = valid;
        }

        Object getValue()
        {
            return value;
        }

        void setValue(Object value)
        {
            this.value = value;
        }

        boolean isLocalValueSet()
        {
            return localValueSet;
        }

        void setLocalValueSet(boolean localValueSet)
        {
            this.localValueSet = localValueSet;
        }
    }

    /**
     * Inner class used to wrap the original events produced by child components in the tree.
     * This will allow the tree to find the appropriate component later when its time to
     * broadcast the events to registered listeners.  Code is based on a similar private
     * class for UIData.
     */
    private static class FacesEventWrapper extends FacesEvent
    {

        private FacesEvent _wrappedFacesEvent;
        private String _nodeId;


        public FacesEventWrapper(FacesEvent facesEvent, String nodeId, UIComponent component)
        {
            super(component);
            _wrappedFacesEvent = facesEvent;
            _nodeId = nodeId;
        }


        public PhaseId getPhaseId()
        {
            return _wrappedFacesEvent.getPhaseId();
        }


        public void setPhaseId(PhaseId phaseId)
        {
            _wrappedFacesEvent.setPhaseId(phaseId);
        }


        public void queue()
        {
            _wrappedFacesEvent.queue();
        }


        public String toString()
        {
            return _wrappedFacesEvent.toString();
        }


        public boolean isAppropriateListener(FacesListener faceslistener)
        {
            // this event type is only intended for wrapping a real event
            return false;
        }


        public void processListener(FacesListener faceslistener)
        {
            throw new UnsupportedOperationException("This event type is only intended for wrapping a real event");
        }


        public FacesEvent getFacesEvent()
        {
            return _wrappedFacesEvent;
        }


        public String getNodeId()
        {
            return _nodeId;
        }
    }

    /**
     * Returns true if there is an error message queued for at least one of the nodes.
     *
     * @param context FacesContext
     * @return whether an error message is present
     */
    private boolean keepSaved(FacesContext context)
    {
        Iterator clientIds = _saved.keySet().iterator();
        while (clientIds.hasNext())
        {
            String clientId = (String) clientIds.next();
            Iterator messages = context.getMessages(clientId);
            while (messages.hasNext())
            {
                FacesMessage message = (FacesMessage) messages.next();
                if (message.getSeverity().compareTo(FacesMessage.SEVERITY_ERROR) >= 0)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
