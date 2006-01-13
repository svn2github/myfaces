/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.custom.stylesheet;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import junit.framework.TestCase;

import org.apache.myfaces.renderkit.html.HtmlRenderKitImpl;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;


public class StylesheetRendererTest extends TestCase {

  FacesContext context;
  MockResponseWriter writer = new MockResponseWriter();
  
  /*
   * Test method for 'org.apache.myfaces.custom.stylesheet.StylesheetRenderer.encodeEnd(FacesContext, UIComponent)'
   */
  public void testInline() throws IOException {

    Stylesheet stylesheet = new Stylesheet();
    stylesheet.setPath("test.css");
    stylesheet.setInline(true);
    stylesheet.setMedia("printer");
    stylesheet.encodeEnd(this.context);
    this.context.renderResponse();
    
    System.out.println(this.writer.getContent());
  }

  public void testLink() throws IOException {

    Stylesheet stylesheet = new Stylesheet();
    stylesheet.setPath("test.css");
    stylesheet.setMedia("printer");
    stylesheet.encodeEnd(this.context);
    this.context.renderResponse();
    
    System.out.println(this.writer.getContent());
  }
  private FacesContext getMockFacesContext(ResponseWriter writer) {

    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.apache.myfaces.renderkit.RenderKitFactoryImpl");
    
    MockControl ctxControl = MockClassControl.createControl(FacesContext.class);
    FacesContext mockFacesCtx = (FacesContext) ctxControl.getMock();
    
    MockControl extCtxControl = MockClassControl.createControl(ExternalContext.class);
    ExternalContext mockExtCtx = (ExternalContext)extCtxControl.getMock();
    mockFacesCtx.getExternalContext();
    ctxControl.setReturnValue(mockExtCtx);
    
    mockFacesCtx.renderResponse();
    ctxControl.setVoidCallable();
    
    mockExtCtx.getRequestContextPath();
    extCtxControl.setReturnValue("");
    
    MockControl rendererKitControl = MockClassControl.createControl(HtmlRenderKitImpl.class);
    RenderKit rKit = (RenderKit) rendererKitControl.getMock();
    rKit.getRenderer(Stylesheet.COMPONENT_FAMILY, Stylesheet.COMPONENT_TYPE);
    rendererKitControl.setReturnValue(new StylesheetRenderer());
   
    
    
    mockFacesCtx.getResponseWriter();
    ctxControl.setReturnValue(writer);
    
    mockFacesCtx.getRenderKit();
    ctxControl.setReturnValue(rKit);


    MockControl viewRootControl = MockClassControl.createControl(UIViewRoot.class);
    UIViewRoot uiViewRoot = (UIViewRoot) viewRootControl.getMock();
    uiViewRoot.getRenderKitId();
    viewRootControl.setReturnValue(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    
    mockFacesCtx.getViewRoot();//(uiViewRoot);
    ctxControl.setReturnValue(uiViewRoot);
    

    ((RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY)).addRenderKit(
        RenderKitFactory.HTML_BASIC_RENDER_KIT, rKit);    
    
    ctxControl.replay();
    extCtxControl.replay();
    viewRootControl.replay();
    rendererKitControl.replay();
    
    return mockFacesCtx;
  }

  protected void setUp() throws Exception {

    this.context = getMockFacesContext(this.writer); 
  }

  protected void tearDown() throws Exception {
  }

}
