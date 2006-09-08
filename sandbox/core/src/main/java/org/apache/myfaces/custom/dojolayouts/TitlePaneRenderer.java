package org.apache.myfaces.custom.dojolayouts;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.map.HashedMap;
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
        DojoUtils.addRequire(context, component, "dojo.widget.TitlePane");
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    protected void encodeJavascriptEnd(FacesContext context, UIComponent component) throws IOException {
        TitlePane pane = (TitlePane) component;
        Map attributes = new HashedMap();
        String panelComponentVar = DojoUtils.calculateWidgetVarName(component.getClientId(context));
        attributes.put("id", panelComponentVar);

        if (pane.getContainerNodeClass() != null)
            attributes.put("containerNodeClass", pane.getContainerNodeClass());
        if (pane.getLabel() != null)
            attributes.put("label", pane.getLabel());
        if (pane.getLabelNodeClass() != null)
            attributes.put("labelNodeClass", pane.getLabelNodeClass());

        DojoUtils.renderWidgetInitializationCode(context, component, "TitlePane", attributes);
    }

    public boolean getRendersChildren() {
        return true;
    }

}