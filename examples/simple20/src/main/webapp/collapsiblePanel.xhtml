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
    <t:saveState value="#{firstCollapsiblePanelBean}"/>
    <t:saveState value="#{secondCollapsiblePanelBean}"/>
    <t:saveState value="#{thirdCollapsiblePanelBean}"/>

    <h:form id="form">
        <t:messages/>
        <t:collapsiblePanel id="test1" value="#{firstCollapsiblePanelBean.collapsed}" title="testTitle">
            <h:panelGrid>
                <h:outputText value="#{firstCollapsiblePanelBean.firstName}"/>
                <h:inputText value="#{firstCollapsiblePanelBean.surName}"/>
                <t:inputCalendar value="#{firstCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
            </h:panelGrid>
        </t:collapsiblePanel>

        <t:subform id="subform">
	        <t:collapsiblePanel id="test2" value="#{secondCollapsiblePanelBean.collapsed}" title="testTitle"
	                            var="test2collapsed">
	            <f:facet name="header">
	                <t:div style="width:500px;background-color:#CCCCCC;">
	                    <h:outputText value="Person"/>
	                    <t:headerLink immediate="true" actionFor="subform">
	                        <h:outputText value="> Details" rendered="#{test2collapsed}"/>
	                        <h:outputText value="v Overview" rendered="#{!test2collapsed}"/>
	                    </t:headerLink>
	                </t:div>
	            </f:facet>
	            <f:facet name="closedContent">
	                <h:panelGroup>
	                    <h:outputText value="#{secondCollapsiblePanelBean.firstName}"/>
	                    <h:outputText value=" "/>
	                    <h:outputText value="#{secondCollapsiblePanelBean.surName}"/>
	                    <h:outputText value=", born on: "/>
	                    <h:outputText value="#{secondCollapsiblePanelBean.birthDate}"/>
	                </h:panelGroup>
	            </f:facet>
	            <h:panelGrid>
	                <h:outputText value="#{secondCollapsiblePanelBean.firstName}"/>
	                <h:inputText value="#{secondCollapsiblePanelBean.surName}"/>
	                <t:inputCalendar value="#{secondCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
	            </h:panelGrid>
	        </t:collapsiblePanel>
        </t:subform>

        <t:collapsiblePanel id="test3" value="#{thirdCollapsiblePanelBean.collapsed}" title="testTitle"
                            var="test2collapsed">
            <f:valueChangeListener type="org.apache.myfaces.examples.collapsiblepanel.CollapsiblePanelValueChangeListener" />
            <f:facet name="header">
                <t:div style="width:500px;background-color:#CCCCCC;">
                    <h:outputText value="Person"/>
                    <t:headerLink>
                        <h:outputText value="> Details" rendered="#{test2collapsed}"/>
                        <h:outputText value="v Overview" rendered="#{!test2collapsed}"/>
                    </t:headerLink>
                </t:div>
            </f:facet>
            <f:facet name="closedContent">
                <h:panelGroup>
                    <h:outputText value="#{thirdCollapsiblePanelBean.firstName}"/>
                    <h:outputText value=" "/>
                    <h:outputText value="#{thirdCollapsiblePanelBean.surName}"/>
                    <h:outputText value=", born on: "/>
                    <h:outputText value="#{thirdCollapsiblePanelBean.birthDate}"/>
                </h:panelGroup>
            </f:facet>
            <h:panelGrid>
                <h:outputText value="#{thirdCollapsiblePanelBean.firstName}"/>
                <h:inputText value="#{thirdCollapsiblePanelBean.surName}"/>
                <t:inputCalendar value="#{thirdCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
            </h:panelGrid>
        </t:collapsiblePanel>

        <t:dataTable id="test_dt" var="person" value="#{thirdCollapsiblePanelBean.persons}" preserveDataModel="false">
            <h:column>
                <t:collapsiblePanel id="test4" var="test4collapsed" value="#{person.collapsed}">
                    <f:facet name="header">
                        <t:div style="width:500px;background-color:#CCCCCC;">
                            <h:outputText value="Person"/>
                            <t:headerLink immediate="true">
                                <h:outputText value="> Details" rendered="#{test4collapsed}"/>
                                <h:outputText value="v Overview" rendered="#{!test4collapsed}"/>
                            </t:headerLink>
                            <h:commandLink value=" test" action="#{person.test}"/>
                        </t:div>
                    </f:facet>
                    <h:inputText id="firstname_input" value="#{person.firstName}"/>
                    <h:commandLink value="test" action="#{person.test}"/>
                </t:collapsiblePanel>
            </h:column>
        </t:dataTable>
    </h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
