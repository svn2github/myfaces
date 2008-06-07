/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.exporter;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.exporter.util.ExcelExporterUtil;
import org.apache.myfaces.custom.exporter.util.ExporterConstants;
import org.apache.myfaces.custom.exporter.util.PDFExporterUtil;
import org.apache.myfaces.custom.util.ComponentUtils;

/**
 * 
 * This class is acting as the Exporter ActionListener.
 */
public class ExporterActionListener implements ActionListener, StateHolder {

    private static final Log   log                       = LogFactory
                                                                 .getLog(ExporterActionListener.class);

    public static final String FILENAME_KEY              = "filename";

    public static final String FILE_TYPE_KEY             = "fileType";

    public static final String FOR_KEY                   = "for";

    public static final String SHOWDISPLAYEDPAGEONLY_KEY = "showDisplayedPageOnly";

    private String             _fileType;

    private String             _fileName;

    private String             _for;

    private String             _showDisplayedPageOnly;

    public void processAction(ActionEvent event) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object response = facesContext.getExternalContext().getResponse();

        if (!(response instanceof HttpServletResponse)) {
            log.error("ExporteActionListener requires servlet");
        }
        else {
            try {

                /* get the source dataScroller component */
                HtmlDataScroller dataScroller = (HtmlDataScroller) ComponentUtils
                        .findComponentById(facesContext, facesContext
                                .getViewRoot(), _for);

                if (!(dataScroller instanceof HtmlDataScroller)) 
                {
                    throw new RuntimeException(
                            "exporterActionListener for attribute should contain a "
                                    + "dataScroller component id");
                }
                
                /*
                 * By default if the showDisplayedPageOnly is not specified, then the
                 * default is false.
                 */
                if (_showDisplayedPageOnly == null) 
                {
                    _showDisplayedPageOnly = "false";
                }
                
                if (ExporterConstants.EXCEL_FILE_TYPE
                        .equalsIgnoreCase(_fileType)) 
                {

                    /*
                     * Excel case. Generate the XLS to the response stream.
                     */
                    
                    Object contextResponse = facesContext.getExternalContext()
                            .getResponse();

                    if (contextResponse instanceof HttpServletResponse) 
                    {
                        ExcelExporterUtil.generateEXCEL(facesContext,
                                (HttpServletResponse) contextResponse,
                                _fileName, dataScroller, Boolean
                                        .parseBoolean(_showDisplayedPageOnly)); 
                    }

                }
                else 
                {

                    /*
                     * PDF case. Generate the PDF to the response stream.
                     */
                    HttpServletResponse httpResponse = (HttpServletResponse) facesContext
                            .getExternalContext().getResponse();
                    

                    PDFExporterUtil.generatePDF(facesContext, httpResponse,
                            _fileName, dataScroller, Boolean
                                    .parseBoolean(_showDisplayedPageOnly));
                }

                /* save the seralized view and complete the response. */
                facesContext.getApplication().getStateManager()
                        .saveSerializedView(facesContext);
                facesContext.responseComplete();
            }
            catch (Exception exception) 
            {
                throw new RuntimeException(exception);
            }

        }

        facesContext.responseComplete();
    }

    public String getFilename() {
        return _fileName;
    }

    public void setFilename(String _filename) {
        this._fileName = _filename;
    }

    public String getFileType() {
        return _fileType;
    }

    public void setFileType(String type) {
        _fileType = type;
    }

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }
    
    public String getShowDisplayedPageOnly() {
        return _showDisplayedPageOnly;
    }

    public void setShowDisplayedPageOnly(String showDisplayedPageOnly) {
        _showDisplayedPageOnly = showDisplayedPageOnly;
    }       

    public void restoreState(FacesContext context, Object state) {
        String values[] = (String[]) state;

        _for = values[0];
        _fileName = values[1];
        _fileType = values[2];
        _showDisplayedPageOnly = values[3];
    }

    public Object saveState(FacesContext context) {
        String values[] = new String[4];

        values[0] = _for;
        values[1] = _fileName;
        values[2] = _fileType;
        values[3] = _showDisplayedPageOnly;
        return ((String[]) values);
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        if (newTransientValue == true) {
            throw new IllegalArgumentException();
        }
    }
}
