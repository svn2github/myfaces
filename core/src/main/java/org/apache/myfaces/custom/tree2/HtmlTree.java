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

import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import java.util.Map;

/**
 * Represents "tree data" in an HTML format.  Also provides a mechanism for maintaining expand/collapse
 * state of the nodes in the tree.
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class HtmlTree extends UITreeData
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlTree2";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.HtmlTree2";
    private UICommand _expandControl;
    private String _varNodeToggler;
    //private String _selectedNodeId;

    /**
     * Constructor
     */
    public HtmlTree()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
        _expandControl = new HtmlCommandLink();
        _expandControl.setParent(this);
    }

    // see superclass for documentation
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _varNodeToggler;
        //values[2] = _selectedNodeId;

        return ((Object) (values));
    }

    // see superclass for documentation
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        setVarNodeToggler((String)values[1]);
        //_selectedNodeId = (String)values[2];
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

//    /**
//     * Implements the {@link javax.faces.event.ActionListener} interface.  Basically, this
//     * method is used to listen for node selection events (when a user has clicked on a
//     * leaf node.)
//     *
//     * @param event ActionEvent
//     */
//    public void setNodeSelected(ActionEvent event)
//    {
//        _selectedNodeId = getNodeId();
//    }
//
//    /**
//     * Indicates whether or not the current {@link TreeNode} is selected.
//     * @return boolean
//     */
//    public boolean isNodeSelected()
//    {
//        return (getNodeId() != null) ? getNodeId().equals(_selectedNodeId) : false;
//    }
}
