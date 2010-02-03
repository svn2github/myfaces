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
<h:form>

    <t:saveState id="ss1" value="#{tabbedPaneBean}" />

    <t:messages id="messageList" showSummary="true" showDetail="true" summaryFormat="{0}:" />

	<f:subview id="panelTabbedPane1">
	    <h:outputText value="client-side tab switching"/>

	    <t:panelTabbedPane bgcolor="#FFFFCC" serverSideTabSwitch="false">

	        <p>
	            <h:outputText value="#{example_messages['tabbed_common']}"/>
	        </p>


	        <t:panelTab id="tab1" label="#{example_messages['tabbed_tab1']}" rendered="#{tabbedPaneBean.tab1Visible}">
	                <h:selectBooleanCheckbox id="testCheckBox" value="#{testCheckBox.checked}"/><h:outputLabel for="testCheckBox" value="A checkbox"/>
	                <br/><br/>
	            <h:inputText id="inp1"/><br/>
	            <h:inputText id="inp2" required="true" /><h:message for="inp2" showSummary="false" showDetail="true" />
	        </t:panelTab>

	        <f:subview id="tab2" >
	            <t:panelTab label="#{example_messages['tabbed_tab2']}" rendered="#{tabbedPaneBean.tab2Visible}">
                    <h:inputTextarea ></h:inputTextarea>
                </t:panelTab>
	        </f:subview>

	        <t:panelTab id="tab3" label="#{example_messages['tabbed_tab3']}" rendered="#{tabbedPaneBean.tab3Visible}">

	            <t:selectOneRadio value="#{testRadioButton.choice}">
	                    <f:selectItem itemValue="0" itemLabel="First Choice" />
	                    <f:selectItem itemValue="1" itemLabel="Second Choice" />
	            </t:selectOneRadio>

	            <br/><br/>

	            <t:dataTable id="xxx" value="#{testCheckList.testCheckBoxes}"
	                        var="checkBox"
	                        preserveDataModel="true"
	                        rowIndexVar="rowNumber">
	                <h:column>
	                    <f:facet name="header">
	                        <h:outputText value="Check boxes list" />
	                    </f:facet>
	                    <h:selectBooleanCheckbox value="#{checkBox.checked}"/>
	                    <h:outputText value="Check box #{rowNumber}"/>
	                </h:column>
	                <h:column>
	                    <f:facet name="header">
	                        <h:outputText value="Text" />
	                    </f:facet>
	                    <h:inputText value="#{checkBox.text}"/>
	                </h:column>
	            </t:dataTable>

	            <br/><br/>

	            <h:inputText id="inp3"/>
                <br/>
	        </t:panelTab>

	        <br/><hr/><br/>

	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab1Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible1']}"/>
	        <br/>
	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab2Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible2']}" />
	        <br/>
	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab3Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible3']}" />
	        <br/>

	        <h:commandButton value="#{example_messages['tabbed_submit']}" />

	    </t:panelTabbedPane>
	</f:subview>
	<f:subview id="panelTabbedPane2">
	    <h:outputText value="server-side tab switching"/>

	    <t:panelTabbedPane bgcolor="#CCFFFF" serverSideTabSwitch="true">

	        <p>
	            <h:outputText value="#{example_messages['tabbed_common']}"/>
	        </p>


	        <t:panelTab id="tab1" label="#{example_messages['tabbed_tab1']}" rendered="#{tabbedPaneBean.tab1Visible}">
	                <h:selectBooleanCheckbox id="testCheckBox" value="#{testCheckBox.checked}"/><h:outputLabel for="testCheckBox" value="A checkbox"/>
	                <br/><br/>
	            <h:inputText id="inp1"/><br/>
	            <h:inputText id="inp2" required="true" /><h:message for="inp2" showSummary="false" showDetail="true" />
	        </t:panelTab>

	        <f:subview id="tab2" >
                <t:panelTab label="#{example_messages['tabbed_tab2']}" rendered="#{tabbedPaneBean.tab2Visible}">
                    <h:inputTextarea ></h:inputTextarea>
                </t:panelTab>
	        </f:subview>

	        <t:panelTab id="tab3" label="#{example_messages['tabbed_tab3']}" rendered="#{tabbedPaneBean.tab3Visible}">

	            <t:selectOneRadio value="#{testRadioButton.choice}">
	                    <f:selectItem itemValue="0" itemLabel="First Choice" />
	                    <f:selectItem itemValue="1" itemLabel="Second Choice" />
	            </t:selectOneRadio>

	            <br/><br/>

	            <t:dataTable id="xxx" value="#{testCheckList.testCheckBoxes}"
	                        var="checkBox"
	                        preserveDataModel="true"
	                        rowIndexVar="rowNumber">
	                <h:column>
	                    <f:facet name="header">
	                        <h:outputText value="Check boxes list" />
	                    </f:facet>
	                    <h:selectBooleanCheckbox value="#{checkBox.checked}"/>
	                    <h:outputText value="Check box #{rowNumber}"/>
	                </h:column>
	                <h:column>
	                    <f:facet name="header">
	                        <h:outputText value="Text" />
	                    </f:facet>
	                    <h:inputText value="#{checkBox.text}"/>
	                </h:column>
	            </t:dataTable>

	           <br/><br/>

	            <h:inputText id="inp3"/><f:verbatim><br/></f:verbatim>
	        </t:panelTab>

	        <br/><hr/><br/>

	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab1Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible1']}"/>
	        <br/>
	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab2Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible2']}" />
	        <br/>
	        <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab3Visible}"/>
	        <h:outputText value="#{example_messages['tabbed_visible3']}" />
	        <br/>

	        <h:commandButton value="#{example_messages['tabbed_submit']}" />

	    </t:panelTabbedPane>
	</f:subview>

</h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
