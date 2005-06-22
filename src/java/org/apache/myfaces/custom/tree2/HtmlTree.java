/*
 * Copyright 2004 The Apache Software Foundation.
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

import javax.faces.context.FacesContext;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.event.ActionEvent;
import javax.faces.el.MethodBinding;

import java.util.HashSet;
import java.util.Map;
import java.io.IOException;


/**
 * Represents "tree data" in an HTML format.  Also provides a mechanism for maintaining expand/collapse
 * state of the nodes in the tree.
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class HtmlTree extends UITreeData
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Tree2";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Tree2";
    private static final String NODE_STATE_KEY = "org.apache.myfaces.tree.NODE_STATE_KEY";
    private UICommand _expandControl;
    private String _varNodeToggler;
    private HashSet _expandedNodes = new HashSet();
    private String _selectedNodeId;

    /**
     * Constructor
     */
    public HtmlTree()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
        _expandControl = new HtmlCommandLink();
    }

    // see superclass for documentation
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _expandedNodes;
        values[2] = _varNodeToggler;
        values[3] = _selectedNodeId;

        return ((Object) (values));
    }

    // see superclass for documentation
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _expandedNodes = (HashSet)values[1];
        setVarNodeToggler((String)values[2]);
        _selectedNodeId = (String)values[3];
    }

    // see superclass for documentation
    public void setNodeId(String nodeId)
    {
        super.setNodeId(nodeId);

        if (_varNodeToggler != null)
        {
            Map requestMap = getFacesContext().getExternalContext().getRequestMap();
            requestMap.put(_varNodeToggler, this);
        }
    }

    public void processDecodes(FacesContext context)
    {
        super.processDecodes(context);

        // store the expand/collapse state information in the session (long story)
        Map sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put(NODE_STATE_KEY + ":" + getId(), _expandedNodes);
    }

    public void encodeBegin(FacesContext context)
            throws IOException
    {
        /**
         * The expand/collapse state of the nodes is stored in the session in order to ensure that this information
         * is preserved across requests where the same tree is reused in a tile (server-side include.)  When using
         * the server-side toggle method without this step, the tree would not remember the expand/collapse state.
         * Since we didn't think it was appropriate to burden the end user with this information as part of a backing
         * bean, it just being stored in the session during encode and retrieved during decode.
         */
        // restore the expand/collapse state information from the session
        Map sessionMap = context.getExternalContext().getSessionMap();
        HashSet nodeState = (HashSet)sessionMap.get(NODE_STATE_KEY + ":" + getId());

        if (nodeState != null)
        {
            _expandedNodes = nodeState;
        }

        super.encodeBegin(context);
    }

    /**
     * Gets the expand/collapse control that can be used to handle expand/collapse nodes.  This is only used in server-side
     * mode.  It allows the nagivation controls (if any) to be clickable as well as any commandLinks the user has set up in
     * their JSP.
     *
     * @return UICommand
     */
    public UICommand getExpandControl()
    {
        return _expandControl;
    }

    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;

        // create a method binding for the expand control
        String bindingString = "#{" + varNodeToggler + ".toggleExpanded}";
        MethodBinding actionBinding = FacesContext.getCurrentInstance().getApplication().createMethodBinding(bindingString, null);
        _expandControl.setAction(actionBinding);
    }

    public String toggleExpanded()
    {
        String nodeId = getNodeId();

        if (_expandedNodes.contains(nodeId))
        {
            _expandedNodes.remove(nodeId);
        }
        else
        {
            _expandedNodes.add(nodeId);
        }

        return null;
    }

    /**
     * Indicates whether or not the current {@link TreeNode} is expanded.
     * @return boolean
     */
    public boolean isNodeExpanded()
    {
        return (_expandedNodes.contains(getNodeId()) && getNode().getChildCount() > 0);
    }

    protected void processChildNodes(FacesContext context, TreeNode parentNode, int processAction)
    {
        super.processChildNodes(context, parentNode, processAction);
    }

    /**
     * Implements the {@link ActionListener} interface.  Basically, this method is used to listen for
     * node selection events (when a user has clicked on a leaf node.)
     *
     * @param event ActionEvent
     */
    public void setNodeSelected(ActionEvent event)
    {
        _selectedNodeId = getNodeId();
    }

    /**
     * Indicates whether or not the current {@link TreeNode} is selected.
     * @return boolean
     */
    public boolean isNodeSelected()
    {
        return (getNodeId() != null) ? getNodeId().equals(_selectedNodeId) : false;
    }
}
