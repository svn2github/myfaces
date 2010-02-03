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
    <h:form id="test">
        <h:panelGrid columns="1">

            <h:outputText value="Implementation using t:panelGrid" />

            <t:panelGrid id="grid1" columns="4" border="1" styleClass="standardTable standardTable_Row1 standardTable_Column" >
                <t:panelGroup>
                    <f:verbatim escape="false">1-1</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">1-2</f:verbatim>
                </t:panelGroup>
                <t:panelGroup colspan="2">
                    <f:verbatim escape="false">1-3</f:verbatim>
                </t:panelGroup>
                <t:panelGroup colspan="3">
                    <f:verbatim escape="false">2-1</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">2-4</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">3-1</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">3-2</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">3-3</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">3-4</f:verbatim>
                </t:panelGroup>
            </t:panelGrid>

            <f:verbatim>&nbsp;</f:verbatim>

            <t:panelGrid id="grid2" columns="4" border="1" styleClass="standardTable standardTable_Row1 standardTable_Column" >
                <t:panelGroup>
                    <f:verbatim escape="false">1-1</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">1-2</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">1-3</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">1-4</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">2-1</f:verbatim>
                </t:panelGroup>
                <t:panelGroup colspan="2">
                    <f:verbatim escape="false">2-2</f:verbatim>
                </t:panelGroup>
                <t:panelGroup>
                    <f:verbatim escape="false">2-4</f:verbatim>
                </t:panelGroup>
                <t:panelGroup colspan="4">
                    <f:verbatim escape="false">3-1</f:verbatim>
                </t:panelGroup>
            </t:panelGrid>

            <f:verbatim>&nbsp;</f:verbatim>
            <h:outputText value="Implementation using t:dataTable" />

            <t:dataTable id="table1" value="#{testColspanBean.lines}" var="line"
                rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column,standardTable_ColumnCentered"
            styleClass="standardTable" headerClass="standardTable_Header" footerClass="standardTable_Header">
                <t:column colspan="2">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 1</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 1</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col1}" />
                </t:column>
                <t:column headercolspan="3">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 2</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 2</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col2}" />
                </t:column>
                <t:column>
                    <f:facet name="header">
                        <f:verbatim escape="false">head 3</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 3</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col3}" />
                </t:column>
                <t:column footercolspan="2">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 4</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 4</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col4}" />
                </t:column>
                <t:column>
                    <f:facet name="header">
                        <f:verbatim escape="false">head 5</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 5</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col5}" />
                </t:column>
            </t:dataTable>

            <f:verbatim>&nbsp;</f:verbatim>

            <t:dataTable id="table2" value="#{testColspanBean.lines}" var="line"
                rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column,standardTable_ColumnCentered"
            styleClass="standardTable" headerClass="standardTable_Header" footerClass="standardTable_Header">
                <t:column headercolspan="2">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 1</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 1</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col1}" />
                </t:column>
                <t:column>
                    <f:facet name="header">
                        <f:verbatim escape="false">head 2</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 2</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col2}" />
                </t:column>
                <t:column colspan="3">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 3</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 3</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col3}" />
                </t:column>
                <t:column headercolspan="2">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 4</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 4</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col4}" />
                </t:column>
                <t:column>
                    <f:facet name="header">
                        <f:verbatim escape="false">head 5</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 5</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col5}" />
                </t:column>
            </t:dataTable>

        </h:panelGrid>
    </h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
