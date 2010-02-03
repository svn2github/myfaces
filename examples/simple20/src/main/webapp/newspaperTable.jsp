<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
        xmlns:f="http://java.sun.com/jsf/core"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:ui="http://java.sun.com/jsf/facelets"
        xmlns:t="http://myfaces.apache.org/tomahawk">
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
<body>
 <ui:composition template="/META-INF/templates/template.xhtml">
  <ui:define name="body">
    <h:panelGrid columns="1">

		<h:outputText value="Implementation using obsolete t:newspaperTable" />

	    <t:newspaperTable id="dataOld"
	            newspaperColumns="2"
	            styleClass="standardTable"
	            headerClass="standardTable_Header"
	            footerClass="standardTable_Header"
	            rowClasses="standardTable_Row1,standardTable_Row2"
	            columnClasses="standardTable_Column,standardTable_ColumnCentered"
	            var="country"
	            value="#{countryList.countries}">
	           <f:facet name="spacer">
	             <f:verbatim> &nbsp; </f:verbatim>
	           </f:facet>
	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_name']}" />
	           </f:facet>
	            <h:outputText value="#{country.name}" />
	       </h:column>

	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_iso']}" />
	           </f:facet>
	           <h:outputText value="#{country.isoCode}" />
	       </h:column>

	    </t:newspaperTable>

		<h:outputText value="Implementation using t:dataTable" />

	    <t:dataTable id="dataNew"
	            newspaperColumns="2"
	            styleClass="standardTable"
	            headerClass="standardTable_Header"
	            footerClass="standardTable_Header"
	            rowClasses="standardTable_Row1,standardTable_Row2"
	            columnClasses="standardTable_Column,standardTable_ColumnCentered"
	            var="country"
	            value="#{countryList.countries}">
	           <f:facet name="spacer">
	             <f:verbatim> &nbsp; </f:verbatim>
	           </f:facet>
	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_name']}" />
	           </f:facet>
	            <h:outputText value="#{country.name}" />
	       </h:column>

	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_iso']}" />
	           </f:facet>
	           <h:outputText value="#{country.isoCode}" />
	       </h:column>

	    </t:dataTable>

		<h:outputText value="Implementation using t:dataTable horizontally" />

	    <t:dataTable id="dataNew2"
	            newspaperColumns="2"
	            newspaperOrientation="horizontal"
	            styleClass="standardTable"
	            headerClass="standardTable_Header"
	            footerClass="standardTable_Header"
	            rowClasses="standardTable_Row1,standardTable_Row2"
	            columnClasses="standardTable_Column,standardTable_ColumnCentered"
	            var="country"
	            value="#{countryList.countries}">
	           <f:facet name="spacer">
	             <f:verbatim> &nbsp; </f:verbatim>
	           </f:facet>
	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_name']}" />
	           </f:facet>
	            <h:outputText value="#{country.name}" />
	       </h:column>

	       <h:column>
	           <f:facet name="header">
	              <h:outputText value="#{example_messages['label_country_iso']}" />
	           </f:facet>
	           <h:outputText value="#{country.isoCode}" />
	       </h:column>

	    </t:dataTable>

	</h:panelGrid>
  </ui:define>
 </ui:composition>
</body>
</html>
