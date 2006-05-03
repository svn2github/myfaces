/*
 * Copyright 2004 The Apache Software Foundation.
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
