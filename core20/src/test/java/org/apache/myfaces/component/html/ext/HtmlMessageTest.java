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
package org.apache.myfaces.component.html.ext;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;

import junit.framework.Test;

import org.apache.myfaces.test.AbstractTomahawkViewControllerTestCase;
import org.apache.myfaces.test.utils.TestUtils;

public class HtmlMessageTest extends AbstractTomahawkViewControllerTestCase
{

    public HtmlMessageTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Verify component renders with the default renderer.
     */
    public void testDefaultRenderer()
    {
        // Define required panel group
        HtmlPanelGroup panelGroup = new HtmlPanelGroup();

        // Define the referenced component
        UIComponent referencedComponent = new HtmlInputText();
        referencedComponent.setId("referencedComponent");
        //referencedComponent.setParent(panelGroup);
        panelGroup.getChildren().add(referencedComponent);
        facesContext.addMessage(referencedComponent.getId(), new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "summary", "detail"));

        // Define the component
        HtmlMessage component = new HtmlMessage();
        component.setId("TestComponent");

        referencedComponent.setParent(panelGroup);
        panelGroup.getChildren().add(component);
        component.setFor(referencedComponent.getId());

        // Render the component
        try
        {
            TestUtils.renderComponent(facesContext, panelGroup);
        }
        catch (IOException e)
        {
            fail(e.getMessage());
        }

        // Verify component was rendered
        assertIdExists(component.getId());
    }
}
