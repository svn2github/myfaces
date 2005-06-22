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

import javax.faces.component.NamingContainer;

import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Model class for the tree component.  It provides random access to nodes in a tree
 * made up of instances of the {@link TreeNode} class.
 *
 * @author Sean Schofield
 * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
 * @version $Revision$ $Date$
 */
public class TreeModel
{
    private final static String SEPARATOR = String.valueOf(NamingContainer.SEPARATOR_CHAR);

    private TreeNode root;
    private TreeNode currentNode;

    /**
     * Constructor
     * @param root The root TreeNode
     */
    public TreeModel(TreeNode root)
    {
        this.root = root;
    }

    /**
     * Gets the current {@link TreeNode} or <code>null</code> if no node ID is selected.
     * @return The current node
     */
    public TreeNode getNode()
    {
        return currentNode;
    }

    /**
     * Sets the current {@link TreeNode} to the specified node ID, which is a colon-separated list
     * of node indexes.  For instance, "0:0:1" means "the second child node of the first child node
     * under the root node."
     *
     * @param nodeId The id of the node to set
     */
    public void setNodeId(String nodeId)
    {
        if (nodeId == null)
        {
            currentNode = null;
            return;
        }

        currentNode = getNodeById(nodeId);
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
        if (nodeId == null)
        {
            throw new IllegalArgumentException("Cannot determine parents for a null node.");
        }

        ArrayList pathList = new ArrayList();
        pathList.add(nodeId);

        while (nodeId.lastIndexOf(SEPARATOR) != -1)
        {
            nodeId = nodeId.substring(0, nodeId.lastIndexOf(SEPARATOR));
            pathList.add(nodeId);
        }

        String[] pathInfo = new String[pathList.size()];

        for (int i=0; i < pathInfo.length; i++)
        {
            pathInfo[i] = (String)pathList.get(pathInfo.length - i - 1);
        }

        return pathInfo;
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
        if (nodeId.lastIndexOf(SEPARATOR) == -1)
        {
            // root node considered to be the last child
            return true;
        }

        //first get the id of the parent
        String parentId = nodeId.substring(0, nodeId.lastIndexOf(SEPARATOR));
        String childString = nodeId.substring(nodeId.lastIndexOf(SEPARATOR) + 1);
        int childId = Integer.parseInt(childString);
        TreeNode parentNode = getNodeById(parentId);

        return  childId + 1== parentNode.getChildCount();
    }

    private TreeNode getNodeById(String nodeId)
    {
        TreeNode node = root;

        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(nodeId, SEPARATOR);
        sb.append(st.nextToken()).append(SEPARATOR);

        while (st.hasMoreTokens())
        {
            int nodeIndex = Integer.parseInt(st.nextToken());
            sb.append(nodeIndex);

            // don't worry about invalid index, that exception will be caught later and dealt with
            node = (TreeNode)node.getChildren().get(nodeIndex);
            sb.append(SEPARATOR);
        }

        return node;
    }
}
