package org.apache.myfaces.custom.tree2;

import java.util.HashSet;

public class TreeStateBase implements TreeState 
{
	
    private static final long serialVersionUID = -6767283932185878071L;
    private HashSet _expandedNodes = new HashSet();
    private boolean _transient = false;
	
    // see interface
    public boolean isNodeExpanded(String nodeId)
    {
        return (_expandedNodes.contains(nodeId) /*&& !getNode().isLeaf()*/);
    }
    
    // see interface
    public void toggleExpanded(String nodeId)
    {
        if (_expandedNodes.contains(nodeId))
        {
            _expandedNodes.remove(nodeId);
        }
        else
        {
            _expandedNodes.add(nodeId);
        }
    }

    // see interface
    public boolean isTransient() 
    {
        return _transient;
    }
    
    // see interface
    public void setTransient(boolean trans) 
    {
        _transient = trans;
    }
    
    
    /**
     * If set to true, all nodes will be expanded by default.  NOTE: A value of false is ignored.
     * @param expandAll boolean
     */
    /*
    public void setExpandAll(boolean expandAll)
    {
        if (expandAll)
        {
            TreeNode originalNode = currentNode;

            //List rootChildren = root.getChildren();
            int kidId = 0;

            //for (int i = 0; i < rootChildren.size(); i++)
            //{
                expandEverything(null, kidId++);
            //}
    
            currentNode = originalNode;
        }
    }
    */
    
    /**
     * Private helper method that recursviely expands all of the nodes.
     * @param parentId The id of the parent node (if applicable)
     * @param childCount The child number of the node to expand (will be incremented as you recurse.)
     */
    /*
    private void expandEverything(String parentId, int childCount)
    {
        String nodeId = (parentId != null) ? parentId + SEPARATOR + childCount : "0";    
        setNodeId(nodeId);
        
        _expandedNodes.add(nodeId);
        
        List children = getNode().getChildren();
        for (int kount=0; kount < children.size(); kount++)
        {
            expandEverything(nodeId, kount);
        }
    }
    */

}
