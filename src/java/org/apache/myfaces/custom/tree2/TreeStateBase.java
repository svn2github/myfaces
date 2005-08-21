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

    // see interface
    public void expandPath(String[] nodePath)
    {
        for (int i=0; i < nodePath.length; i++)
        {
            String nodeId = nodePath[i];
            _expandedNodes.add(nodeId);
        }
    }

    // see interface
    public void collapsePath(String[] nodePath)
    {
        for (int i=0; i < nodePath.length; i++)
        {
            String nodeId = nodePath[i];
            _expandedNodes.remove(nodeId);
        }
    }
}
