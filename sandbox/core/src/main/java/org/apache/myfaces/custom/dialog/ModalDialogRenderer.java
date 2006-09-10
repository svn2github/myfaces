package org.apache.myfaces.custom.dialog;

import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.StringTokenizer;

public class ModalDialogRenderer extends Renderer {
    public static final String RENDERER_TYPE = "org.apache.myfaces.ModalDialog";

    public static final String DIV_ID_PREFIX = "_div";

    //@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        DojoUtils.addMainInclude(context, component, javascriptLocation,
                                 DojoUtils.getDjConfigInstance(context));
        DojoUtils.addRequire(context, component, "dojo.widget.Dialog");

        writeModalDialogBegin((ModalDialog) component, context.getResponseWriter());
    }

    //@Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        StringBuffer buf = new StringBuffer();

        buf.append("</div>");

        context.getResponseWriter().write(buf.toString());
    }

    private void appendHiderIds(StringBuffer buf, ModalDialog dlg) {
        String[] hiders = null;
        if (dlg.getHiderIds() != null) {
            hiders = dlg.getHiderIds().split(",");
        }

        for (int i = 0; i < hiders.length; i++) {
            String varName = "btn" + i;
            buf.append("var ").append(varName).append(" = document.getElementById(\"")
            .append(hiders[i].trim()).append("\");")
            .append(dlg.getDialogVar()).append(".setCloseControl(").append(varName).append(");");
        }
    }

    private void appendDialogAttributes(StringBuffer buf, ModalDialog dlg) {
        if(dlg.getDialogAttr() == null)
            return;

        StringTokenizer it = new StringTokenizer(dlg.getDialogAttr(), " ");
        while(it.hasMoreElements())
        {
            String[] pair = it.nextToken().split("=");
            String attribute = pair[0];
            String value = pair[1].replaceAll("'", "");
            try {
                Double number = new Double(value);
            }
            catch(NumberFormatException e) {
                value = new StringBuffer("\"").append(value).append("\"").toString();
            }
            buf.append(attribute).append(":").append(value).append(", ");
        }
        buf.setLength(buf.length() - 2);
    }

    private void writeModalDialogBegin(ModalDialog dlg, ResponseWriter writer) throws IOException {
        StringBuffer buf = new StringBuffer();
        buf.append("<script type=\"text/javascript\">")
        .append("var ").append(dlg.getDialogVar()).append(";")
        .append("function loader(e) {").append(dlg.getDialogVar())
        .append(" = dojo.widget.createWidget(\"dialog\", {id:")
        .append("\"").append(dlg.getDialogId()).append("\", ");

        appendDialogAttributes(buf, dlg);

        String dlgId = dlg.getId() != null ?
                       dlg.getId() :
                       new StringBuffer(dlg.getDialogId()).append(DIV_ID_PREFIX).toString();
        buf.append("}, dojo.byId(\"")
        .append(dlgId).append("\"));");

        appendHiderIds(buf, dlg);

        buf.append("}")
        .append("dojo.addOnLoad(loader);")
        .append("</script>");

        buf.append("<div id=\"").append(dlgId).append("\"");
        if(dlg.getStyle() != null)
            buf.append(" style=\"").append(dlg.getStyle()).append("\"");
        if(dlg.getStyleClass() != null)
            buf.append(" class=\"").append(dlg.getStyleClass()).append("\"");
        buf.append(">");

        writer.write(buf.toString());
    }
}
