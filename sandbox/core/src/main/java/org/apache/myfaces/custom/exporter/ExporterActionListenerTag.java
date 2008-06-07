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

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Export datatable contents to an excel file or a pdf file.
 * 
 * @JSFJspTag
 *   name="s:exporterActionListener"
 *   bodyContent="JSP" 
 * 
 * This class is acting as the tag handler for the Exporter ActionListener.
 */
public class ExporterActionListenerTag extends TagSupport {

    private static final long serialVersionUID = -1455677614701939262L;
    private String _fileType;
    private String _fileName;
    private String _for;
    private String _showDisplayedPageOnly;    
    
    public int doStartTag() throws JspException {

        // check whether the attributes are not null
        if (_for == null) 
        {
            throw new JspException("for attribute not set");
        }

        if (_fileType == null) 
        {
            throw new JspException("fileType attribute not set");
        }

        // find the parent UIComponentTag which should be an ActionSource.
        UIComponentTag componentTag = UIComponentTag
                .getParentUIComponentTag(pageContext);

        if (componentTag == null) 
        {
            throw new JspException("ExporterActionListenerTag has no UIComponentTag ancestor");
        }

        if (componentTag.getCreated()) 
        {

            // if the component was just created, so we add the Listener.
            UIComponent component = componentTag.getComponentInstance();

            if (component instanceof ActionSource) 
            {
                ExporterActionListener exporterActionListener = new ExporterActionListener();

                exporterActionListener.setFor(_for);
                exporterActionListener.setFileType(_fileType);
                exporterActionListener.setFilename(_fileName);
                exporterActionListener.setShowDisplayedPageOnly(_showDisplayedPageOnly);

                ((ActionSource) component)
                        .addActionListener(exporterActionListener);
            }
            else 
            {
                throw new JspException("Component " + component.getId()
                        + " is no ActionSource");
            }
        }

        return Tag.SKIP_BODY;
    }

    public void release() {
        super.release();
        _fileType = null;
        _fileName = null;
        _for = null;
        _showDisplayedPageOnly = null;
    }
    
    public String getFilename() {
        return _fileName;
    }

    /**
     * Default name of the generated excel or pdf file, if not specified value of the "for" attribute will be used
     * 
     * @JSFJspAttribute
     */
    public void setFilename(String _filename) {
        this._fileName = _filename;
    }

    public String getFileType() {
        return _fileType;
    }

    /**
     * "PDF" or "XLS"
     * 
     * @JSFJspAttribute
     */
    public void setFileType(String type) {
        _fileType = type;
    }

    public String getFor() {
        return _for;
    }

    /**
     * Id of the datatable component whose values to be exported
     * 
     * @JSFJspAttribute
     */
    public void setFor(String _for) {
        this._for = _for;
    }

    public String getShowDisplayedPageOnly() {
        return _showDisplayedPageOnly;
    }

    public void setShowDisplayedPageOnly(String showDisplayedPageOnly) {
        _showDisplayedPageOnly = showDisplayedPageOnly;
    }   
    
}
