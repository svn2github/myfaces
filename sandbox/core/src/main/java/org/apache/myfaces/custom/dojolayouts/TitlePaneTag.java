package org.apache.myfaces.custom.dojolayouts;

import javax.faces.component.UIComponent;

public class TitlePaneTag extends DojoContentPaneTag {

    public static final String TAG_PARAM_ContainerNodeClass = "containerNodeClass";

    public static final String TAG_PARAM_Label              = "label";

    public static final String TAG_PARAM_LabelNodeClass     = "labelNodeClass";

    private String             _containerNodeClass          = null;

    private String             _label                       = null;

    private String             _labelNodeClass              = null;

    public String getComponentType() {
        return TitlePane.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return TitlePane.RENDERER_TYPE;
    }


    public void release() {
        super.release();
        // //release containerNodeClass begin
        _containerNodeClass = null;
        // //release containerNodeClass end
        // //release labelNodeClass begin
        _labelNodeClass = null;
        // //release labelNodeClass end

        // //release label begin
        _label = null;
        // //release label end

    }

    public void setContainerNodeClass(String containerNodeClass) {
        _containerNodeClass = containerNodeClass;
    }

 
    public void setLabel(String label) {
        _label = label;
    }

    public void setLabelNodeClass(String labelNodeClass) {
        _labelNodeClass = labelNodeClass;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        // // setProperties containerNodeClass begin
        super.setStringProperty(component, TAG_PARAM_ContainerNodeClass, _containerNodeClass);
        // //setProperties containerNodeClass end
        // //setProperties labelNodeClass begin
        super.setStringProperty(component, TAG_PARAM_LabelNodeClass, _labelNodeClass);
        // //setProperties labelNodeClass end
        // // setProperties label begin
        super.setStringProperty(component, TAG_PARAM_Label, _label);
        // //setProperties label end

    }
}
