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
package org.apache.myfaces.custom.calendar;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;

import org.apache.myfaces.component.behavior.AbstractClientBehaviorTestCase;
import org.apache.myfaces.component.behavior.HtmlClientEventAttributesUtil;
import org.apache.myfaces.component.behavior.HtmlRenderedClientEventAttr;
import org.apache.myfaces.custom.calendar.HtmlInputCalendar;
import org.apache.myfaces.shared_tomahawk.renderkit.ClientBehaviorEvents;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Leonardo Uribe (latest modification by $Author: jankeesvanandel $)
 * @version $Revision: 799929 $ $Date: 2009-08-01 16:29:33 -0500 (sáb, 01 ago 2009) $
 */
public class HtmlCalendarInlineClientBehaviorRendererTest extends AbstractClientBehaviorTestCase
{
    private HtmlRenderedClientEventAttr[] attrs = null;
    
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        attrs = (HtmlRenderedClientEventAttr[]) 
            org.apache.myfaces.shared_tomahawk.util.ArrayUtils.concat( 
                    HtmlClientEventAttributesUtil.generateClientBehaviorEventAttrs(),
                new HtmlRenderedClientEventAttr[]{
                    new HtmlRenderedClientEventAttr(HTML.ONBLUR_ATTR, ClientBehaviorEvents.BLUR),
                    new HtmlRenderedClientEventAttr(HTML.ONFOCUS_ATTR, ClientBehaviorEvents.FOCUS)
                });
    }

    @Override
    protected void setUpServletObjects() throws Exception
    {
        // TODO Auto-generated method stub
        super.setUpServletObjects();
        request.setAttribute("org.apache.MyFaces.FIRST_SUBMIT_SCRIPT_ON_PAGE", Boolean.FALSE);
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
        attrs = null;
    }


    @Override
    protected UIComponent createComponentToTest()
    {
        HtmlInputCalendar calendar = new HtmlInputCalendar();
        UIComponent form = new HtmlForm();
        form.getChildren().add(calendar);
        facesContext.getViewRoot().getChildren().add(form);
        return calendar;
    }

    @Override
    protected HtmlRenderedClientEventAttr[] getClientBehaviorHtmlRenderedAttributes()
    {
        return attrs;
    }

    @Test
    @Ignore
    @Override
    public void testClientBehaviorHolderRendersName()
    {
        super.testClientBehaviorHolderRendersName();
    }
}
