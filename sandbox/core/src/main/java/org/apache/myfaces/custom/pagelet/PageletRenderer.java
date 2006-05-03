package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * @author Thomas Spiegl, Werner Punz
 *
 */
public class PageletRenderer extends BasePageletRenderer {

    // private static Log log =
    // LogFactory.getLog(PageletRenderer.class);
    SubrendererPagelet        pageletDelegate        = new SubrendererPagelet();
    SubrendererPageletRawText pageletRawTextDelegate = new SubrendererPageletRawText();

    //TODO other subrenderer delegates will follow
    SubrendererTextarea textareaDelegate = new SubrendererTextarea();

    public void decode(FacesContext facesContext, UIComponent uiComponent) {

        // TODO decode
        if (!(uiComponent instanceof EditableValueHolder)) {
            throw new IllegalArgumentException("Component " + uiComponent.getClientId(facesContext) + " is not an EditableValueHolder");
        }

        Map    paramMap = facesContext.getExternalContext().getRequestParameterMap();
        String uniqueId = calcUniqueId(uiComponent, facesContext);

        // ((HttpServletRequest)facesContext.getExternalContext().getRequest()).getParameterMap()
        String paramName = uniqueId + TEXT_AREA_SUFFIX;


        if (paramMap.containsKey(paramName)) {
            ((EditableValueHolder) uiComponent).setSubmittedValue(paramMap.get(paramName));
        } // else if (paramMap.containsKey(backupControlParam)){
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    	//super.encodeBegin(facesContext, uiComponent);

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Pagelet control     = (Pagelet) uiComponent;
        String              controlMode = control.getControlMode();

        if ((controlMode != null) && (controlMode.equals(Pagelet.CONTROL_MODE_TEXTAREA)))
            textareaDelegate.encodeEnd(facesContext, uiComponent);
        else if ((controlMode != null) && controlMode.equals(Pagelet.COMTROL_MODE_PAGELET_RAWTEXT))
            pageletRawTextDelegate.encodeEnd(facesContext, uiComponent);
        else
            pageletDelegate.encodeEnd(facesContext, uiComponent);
    }


}
