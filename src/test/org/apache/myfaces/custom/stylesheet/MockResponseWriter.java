package org.apache.myfaces.custom.stylesheet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.HtmlResponseWriterImpl;

public class MockResponseWriter extends HtmlResponseWriterImpl {

  private static Writer writer = new StringWriter();
  
  public MockResponseWriter() {
    super(writer, null, null);
  }
  
  public String getContent()
  {
    return MockResponseWriter.writer.toString();
  }

}
