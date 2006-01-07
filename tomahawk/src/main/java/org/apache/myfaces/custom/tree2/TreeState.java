package org.apache.myfaces.custom.tree2;

import java.io.Serializable;

public interface TreeState extends Serializable
{

    /**
     * Indicates whether or not the specified {@link TreeNode} is expanded.
     *
     * @param nodeId The id of the node in question.
     * @return If the node is expanded.
     */
    public boolean isNodeExpanded(String nodeId);

    /**
     * Toggle the expanded state of the specified {@link TreeNode}.
     * @param nodeId The id of the node whose expanded state should be toggled.
     */
    public void toggleExpanded(String nodeId);

    /**
     * Expand the complete path specified.  If any node in the path is already expanded,
     * that node should be left as it is.
     *
     * @param nodePath The path to be expanded.
     */
    public void expandPath(String[] nodePath);

    /**
     * Collapse the complete path specified.  If any node in the path is already collapsed,
     * that node should be left as it is.
     *
     * @param nodePath The path to be collapsed.
     */
    public void collapsePath(String[] nodePath);

    /**
     * Getter for transient property.
     * @return boolean
     */
    public boolean isTransient();

    /**
     * Setter for transient property
     * @param trans boolean
     */
    public void setTransient(boolean trans);

}
