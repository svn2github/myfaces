package org.apache.myfaces.custom.date;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

public class MockHtmlDateRendererTestRenderKit extends RenderKit {
  private Map renderers = new HashMap();
  
  public MockHtmlDateRendererTestRenderKit() {
    addRenderer("javax.faces.Input", "org.apache.myfaces.Date", new HtmlDateRenderer());
  }
  
  public void addRenderer(String family, String rendererType, Renderer renderer) {
    Map sub = (Map)renderers.get(family);
    if(null == sub) {
      sub = new HashMap();
      renderers.put(family, sub);
    }
    sub.put(rendererType, renderer);
  }

  public Renderer getRenderer(String family, String rendererType) {
    Renderer renderer = null;
    Map sub = (Map)renderers.get(family);
    if(null != sub) {
      renderer = (Renderer)sub.get(rendererType);
    }
    return renderer;
  }

  public ResponseStateManager getResponseStateManager() {
    return null;
  }

  public ResponseWriter createResponseWriter(Writer writer,
      String contentTypeList, String characterEncoding) {
    return null;
  }

  public ResponseStream createResponseStream(OutputStream out) {
    return null;
  }

}
