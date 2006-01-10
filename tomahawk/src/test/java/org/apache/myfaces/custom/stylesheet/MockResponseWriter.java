package org.apache.myfaces.custom.stylesheet;

import java.io.StringWriter;
import java.io.Writer;

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
