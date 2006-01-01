package org.apache.myfaces.custom.date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

public class MockHtmlDateRendererTestRenderKitFactory extends RenderKitFactory {
  private Map renderKits = new HashMap();

  public MockHtmlDateRendererTestRenderKitFactory() {
    addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, new MockHtmlDateRendererTestRenderKit());
  }
  
  public void addRenderKit(String renderKitId, RenderKit renderKit) {
    renderKits.put(renderKitId, renderKit);
  }

  public RenderKit getRenderKit(FacesContext context, String renderKitId) {
    return (RenderKit)renderKits.get(renderKitId);
  }

  public Iterator getRenderKitIds() {
    List ids = new ArrayList();
    ids.add(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    return ids.iterator();
  }

}
