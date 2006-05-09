/*
 * Copyright 2006 The Apache Software Foundation.
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


import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.event.ActionEvent;
import javax.faces.component.html.HtmlCommandLink;

/**
 * Test case for {@link UITreeData}.
 */
public class UITreeDataTest extends AbstractTreeTestCase
{
    private NodeSimulator nodeSim;

    /**
     * Constructor
     * @param name String
     */
    public UITreeDataTest(String name)
    {
        super(name);
    }

    /**
     * See abstract class
     */
    public void setUp()
    {
        super.setUp();
    }

    /**
     * Tests the selection of a specific node using both server side and client side
     * toggle options.
     *
     * @throws Exception
     */
    public void testNodeSelected() throws Exception
    {
    	tree.setClientSideToggle(false);
        // tree.getAttributes().put(JSFAttr.CLIENT_SIDE_TOGGLE, Boolean.FALSE);

        ActionEvent event = new ActionEvent(new HtmlCommandLink());

        // set the node to be selected
        tree.setNodeId("0:1:0");
        tree.setNodeSelected(event);

        assertTrue("Node 0:1:0 should be selected", tree.isNodeSelected());

    	tree.setClientSideToggle(true);
        // tree.getAttributes().put(JSFAttr.CLIENT_SIDE_TOGGLE, Boolean.TRUE);

        // set the node to be selected
        tree.setNodeId("0:1:0");
        tree.setNodeSelected(event);

        assertTrue("Node 0:1:0 should be selected", tree.isNodeSelected());
    }

    /**
     * Tests programatic selection of a node.  (See MYFACES-717)
     * @throws Exception
     */
    public void testProgramaticSelection() throws Exception
    {
        TreeModelBase treeModel = new TreeModelBase(rootNode);

        TreeStateBase treeState = new TreeStateBase();
        treeState.setSelected("0:3");

        treeModel.setTreeState(treeState);

        tree.setValue(treeModel);
        tree.setNodeId("0:3");
        assertTrue("Node 0:3 should be selected", tree.isNodeSelected());
    }

    public static Test suite()
    {
        return new TestSuite(UITreeDataTest.class);
    }
}
