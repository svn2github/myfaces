<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>
<html>

<!--
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
//-->

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <h:form>
    	<p>This component allows to export the datatable contents to an excel or pdf file.</p>
		
		<br>
		
		<t:dataTable id="tbl_cars"
                styleClass="scrollerTable"
                headerClass="standardTable_Header"
                footerClass="standardTable_Header"
                rowClasses="standardTable_Row1,standardTable_Row2"
                columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                var="car"
                value="#{exporterBean.carsList}"
                preserveDataModel="false"
                rows="6">
           <t:column>
               <f:facet name="header">
                   <h:outputText value="Id" />
               </f:facet>
               <h:outputText value="#{car.id}" />
           </t:column>

           <t:column>
               <f:facet name="header">
                  <h:outputText value="Type" />
               </f:facet>
               <h:outputText value="#{car.type}" />
           </t:column>

           <t:column>
               <f:facet name="header">
                  <h:outputText value="Color" />
               </f:facet>
               <h:outputText value="#{car.color}" />
           </t:column>

        </t:dataTable>
        
        <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
            <t:dataScroller id="scroll_1"
                    for="tbl_cars"
                    fastStep="10"
                    pageCountVar="pageCount"
                    pageIndexVar="pageIndex"
                    styleClass="scroller"
                    paginator="true"
                    paginatorTableClass="paginator"
                    paginatorActiveColumnStyle="font-weight:bold;"
                    paginatorMaxPages="10">
                <f:facet name="first" >
                    <t:graphicImage url="images/arrow-first.gif" border="1" />
                </f:facet>
                <f:facet name="last">
                    <t:graphicImage url="images/arrow-last.gif" border="1" />
                </f:facet>
                <f:facet name="previous">
                    <t:graphicImage url="images/arrow-previous.gif" border="1" />
                </f:facet>
                <f:facet name="next">
                    <t:graphicImage url="images/arrow-next.gif" border="1" />
                </f:facet>
                <f:facet name="fastforward">
                    <t:graphicImage url="images/arrow-ff.gif" border="1" />
                </f:facet>
                <f:facet name="fastrewind">
                    <t:graphicImage url="images/arrow-fr.gif" border="1" />
                </f:facet>
            </t:dataScroller>
            <t:dataScroller id="scroll_2"
                    for="tbl_cars"
                    rowsCountVar="rowsCount"
                    displayedRowsCountVar="displayedRowsCountVar"
                    firstRowIndexVar="firstRowIndex"
                    lastRowIndexVar="lastRowIndex"
                    pageCountVar="pageCount"
                    immediate="true"
                    pageIndexVar="pageIndex">
                <h:outputFormat value="#{example_messages['dataScroller_pages']}" styleClass="standard" >
                    <f:param value="#{rowsCount}" />
                    <f:param value="#{displayedRowsCountVar}" />
                    <f:param value="#{firstRowIndex}" />
                    <f:param value="#{lastRowIndex}" />
                    <f:param value="#{pageIndex}" />
                    <f:param value="#{pageCount}" />
                </h:outputFormat>
            </t:dataScroller>
        </h:panelGrid>        

		<br>
		
		<h:commandButton action="" value="Export as excel">
			<s:exporterActionListener for="scroll_1" fileType="XLS"/>
		</h:commandButton>
		
		<br>
		
		<h:commandButton action="" value="Export the current page as an excel file">
			<s:exporterActionListener for="scroll_1" fileType="XLS" showDisplayedPageOnly="true"/>
		</h:commandButton>				
		
		<br>
		
		<h:commandButton action="" value="Export as pdf">
			<s:exporterActionListener for="scroll_1" fileType="PDF"/>
		</h:commandButton>
		
		<br>
		
		<h:commandButton action="" value="Export the current page as a pdf file">
			<s:exporterActionListener for="scroll_1" fileType="PDF" showDisplayedPageOnly="true"/>
		</h:commandButton>		
		
    </h:form>
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
