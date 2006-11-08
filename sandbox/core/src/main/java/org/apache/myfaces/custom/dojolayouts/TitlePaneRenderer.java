package org.apache.myfaces.custom.dojolayouts;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

public class TitlePaneRenderer extends DojoContentPaneRenderer {

    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

    }

    protected void encodeJavascriptBegin(FacesContext context, UIComponent component) throws IOException {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        try {
            if (javascriptLocation != null) {
                DojoUtils.addMainInclude(context, component, javascriptLocation, new DojoConfig());
            } else {
                DojoUtils.addMainInclude(context, component, null, new DojoConfig());
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        String [] requires = {"dojo.widget.myfaces.SavestatingTitlePane",
                              "dojo.widget.html.myfaces.SavestatingTitlePane"};
        DojoUtils.addRequire(context, component, requires);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    protected void encodeJavascriptEnd(FacesContext context, UIComponent component) throws IOException {

        String [] attributeNames = {"containerNodeClass", "label", "labelNodeClass", "widgetVar", "widgetId"};
        DojoUtils.renderWidgetInitializationCode(context, component, "SavestatingTitlePane", attributeNames);
    }

    public boolean getRendersChildren() {
        return true;
    }

}