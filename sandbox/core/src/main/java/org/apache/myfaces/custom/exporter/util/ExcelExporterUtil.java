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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class is a utility class for serving excel exporting.
 */
public class ExcelExporterUtil {
    
    private static void addColumnHeaders(HSSFSheet sheet, List columns) {
        HSSFRow rowHeader = sheet.createRow(0);

        for (int i = 0; i < columns.size(); i++) {
            UIColumn column = (UIColumn) columns.get(i);
            addColumnValue(rowHeader, column.getHeader(), i);
        }
    }

    private static List getColumns(HtmlDataTable table) {
        List columns = new ArrayList();
        for (int i = 0; i < table.getChildCount(); i++) {
            UIComponent child = (UIComponent) table.getChildren().get(i);
            if (child instanceof UIColumn) {
                columns.add(child);
            }
        }
        return columns;
    }

    private static void addColumnValue(HSSFRow rowHeader,
            UIComponent component, int index) {
        HSSFCell cell = rowHeader.createCell((short) index);
        cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        if (component instanceof ValueHolder) {
            String stringValue = RendererUtils.getStringValue(FacesContext
                    .getCurrentInstance(), component);
            cell.setCellValue(stringValue);
        }
    }
    
    /*
     * This method is used for adding the columns values to the HSSFSheet.
     */
    private static void generateTableContent(FacesContext facesContext,
            HSSFSheet sheet, List columns, HtmlDataTable dataTable) {
        
        int numberOfColumns = columns.size();
        int numberOfRows = dataTable.getRowCount();        
        int startFrom = 0;
        int currentIndex = 1;
        int endAt = numberOfRows;         
    
        /* fill the table with the data. */
        for (int i = startFrom; i < endAt; ++i) {
            dataTable.setRowIndex(i);
            HSSFRow row = sheet.createRow(currentIndex++);
            for (int j = 0; j < numberOfColumns; ++j) {
                UIColumn column = (UIColumn) columns.get(j);
                addColumnValue(row, (UIComponent) column.getChildren().get(0),
                        j);
            }
        }
    }    
    
    /*
     * This utility method is used for writing the excelModel (generatedExcel)
     * to the response (response) and uses the (fileName) as the generated file 
     * name.
     */
    private static void writeExcelToResponse(HttpServletResponse response,
            HSSFWorkbook generatedExcel, String fileName) throws IOException {

        /* write the model to the stream */
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="
                + fileName + ".xls");

        generatedExcel.write(response.getOutputStream());
    }
    
    /*
     * This utility method is used for generating the (HSSFWorkbook) 
     * excel table model from the passed (HtmlDataTable).
     * @param facesContext
     * @param table the passed (HtmlDataTable).
     * @return the (HSSFWorkbook) object. 
     */
    private static HSSFWorkbook generateExcelTableModel(
            FacesContext facesContext, HtmlDataTable dataTable) {
  
        HSSFWorkbook workbook = new HSSFWorkbook();       
        HSSFSheet sheet = workbook.createSheet(dataTable.getId());
        List columns = getColumns(dataTable);
        int currentRowIndex = dataTable.getRowIndex();

        addColumnHeaders(sheet, columns);
        generateTableContent(facesContext, sheet, columns, dataTable);

        dataTable.setRowIndex(currentRowIndex);
        return workbook;
    }    

    /**
     * This utility method is used for generating the excel 
     * table to the HttpServletResponse object. 
     * @param workBook
     * @param response
     * @param fileName
     * @throws IOException
     */    
    public static void generateEXCEL(FacesContext facesContext,
            HttpServletResponse response, String fileName, HtmlDataTable dataTable)
            throws IOException {

        /*
         * By default if the fileName is not specified, then use the
         * table id.
         */
        if (fileName == null) 
        {
            fileName = dataTable.getId();
        }

        /* generate the excel model */
        HSSFWorkbook generatedExcel = ExcelExporterUtil
                .generateExcelTableModel(facesContext, dataTable);

        writeExcelToResponse(response, generatedExcel, fileName);
    }
}
