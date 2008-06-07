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
package org.apache.myfaces.custom.exporter.util;

import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.util.ComponentUtils;

/**
 * This class is a utility class for serving data exporting.
 */
public class ExporterUtil {
    
    /**
     * This utility method is used for getting the tomahawk dataTable
     * from the passed the dataScroller id.
     * @param facesContext
     * @param scrollerID
     * @return the Tomahawk HtmlDataTable object.
     */
    public static HtmlDataTable getDataTableFromDataScroller(
            FacesContext facesContext, String scrollerID) {

        HtmlDataScroller dataScroller = (HtmlDataScroller) ComponentUtils
                .findComponentById(facesContext, facesContext.getViewRoot(),
                        scrollerID);
        return getDataTableFromDataScroller(facesContext, dataScroller);
    }
    
    /**
     * This utility method is used for getting the tomahawk dataTable from the
     * passed the dataScroller object.
     * 
     * @param facesContext
     * @param scrollerID
     * @return the Tomahawk HtmlDataTable object.
     */
    public static HtmlDataTable getDataTableFromDataScroller(
            FacesContext facesContext, HtmlDataScroller dataScroller) {

        HtmlDataTable tomahawkDataTable = (HtmlDataTable) ComponentUtils
                .findComponentById(facesContext, facesContext.getViewRoot(),
                        dataScroller.getFor());
        return tomahawkDataTable;
    }    
}
