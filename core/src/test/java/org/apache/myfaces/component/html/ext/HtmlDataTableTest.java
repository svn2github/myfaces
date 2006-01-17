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
package org.apache.myfaces.component.html.ext;

import java.util.Collection;
import java.util.HashSet;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

import junit.framework.TestCase;

import org.apache.myfaces.mock.MockExternalContext;
import org.apache.myfaces.mock.MockFacesContext;
import org.apache.myfaces.renderkit.RenderKitFactoryImpl;
import org.apache.myfaces.renderkit.html.HtmlRenderKitImpl;

/**
 * @author Mathias Brökelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTableTest extends TestCase
{
    private FacesContext _ctx;
    private HtmlDataTable _dataTable;

    protected void setUp() throws Exception
    {
        MockFacesContext mock = new MockFacesContext();
        mock.setViewRoot(new UIViewRoot());
        mock.setExternalContext(new MockExternalContext());
        _dataTable = new HtmlDataTable();
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                RenderKitFactoryImpl.class.getName());
        RenderKitFactory rkf = (RenderKitFactory) FactoryFinder
                .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        rkf.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                new HtmlRenderKitImpl());
        _ctx = mock;
    }

    protected void tearDown() throws Exception
    {
        _ctx = null;
        _dataTable = null;
    }

    /*
     * Test method for
     * 'org.apache.myfaces.component.html.ext.HtmlDataTable.getClientId(FacesContext)'
     */
    public void testGetClientIdFacesContext()
    {
        assertEquals(-1, _dataTable.getRowIndex());
        String baseClientId = _dataTable.getClientId(_ctx);
        assertNotNull(baseClientId);
        Collection rowClientIds = new HashSet();
        for (int i = 0; i < 10; i++)
        {
            _dataTable.setRowIndex(i);
            assertTrue("Duplicate client id while iterating rows",
                    rowClientIds.add(_dataTable.getClientId(_ctx)));
        }
        _dataTable.setRowIndex(-1);
        assertEquals(baseClientId, _dataTable.getClientId(_ctx));
    }

}
