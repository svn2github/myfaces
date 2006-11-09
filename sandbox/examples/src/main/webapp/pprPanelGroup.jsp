<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

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

<html>

<%@ include file="inc/head.inc"%>


<body>
<h1>PPR Example</h1>
<span id="cnt">0</span>
seconds since last page refresh.
<script>
    var sec=0;
    function counter(){
        setTimeout("counter();",1000);
        document.getElementById("cnt").innerHTML = sec++;
    }
    counter();
</script>

<f:view>

	<h:form id="mainform">


		<t:messages id="messageList" styleClass="error" showDetail="true"
			summaryFormat="{0} " />
			
		<t:saveState value="#{pprExampleBean.partialChangeDropDown}" />
		<t:saveState value="#{pprExampleBean.partialChangeCheckBox}" />
			
		<h:panelGrid columns="2">
			<h:outputText value="Enter the value for update:" />
			<h:inputText value="#{pprExampleBean.textField}" />
			
			<h:outputText value="partial update button:" />
			<h:commandButton id="pprSubmit1" value="PPR Submit" />
			
			<h:outputText value="Click this for update:" />
			<h:commandButton id="configuredSubmit" value="Configured Submit" />
			
			<h:outputText value="update first group partialy:" />
			<h:selectBooleanCheckbox value="#{pprExampleBean.partialUpdateConfiguredButton}" 
			onclick="oamSubmitForm('mainform',this.id);"/>
			
			<h:outputText value="Click this for normal update with action:" />
			<h:commandButton id="normalSubmit" value="testAction" action="#{pprExampleBean.testAction}"/>

			<h:outputText value="Click this for partial update with action:" />
			<h:commandButton id="partialAction" value="partial testAction" action="#{pprExampleBean.testAction}"/>
			
			<h:outputText value="partialy submit by change:" />
			<h:selectOneMenu id="partialDropDown" onchange="submit(this);"
				value="#{pprExampleBean.partialChangeDropDown}"	
				valueChangeListener="#{pprExampleBean.testValueChangeListener}">
				<f:selectItem itemLabel="test1" itemValue="test1"/>
				<f:selectItem itemLabel="test2" itemValue="test2"/>
				<f:selectItem itemLabel="test3" itemValue="test3"/>
			</h:selectOneMenu>
			
			<h:outputText value="partialy submit by change:" />
			<h:selectBooleanCheckbox id="partialCheckBox" 
			value="#{pprExampleBean.partialChangeCheckBox}"
			valueChangeListener="#{pprExampleBean.testValueChangeListener}"
			onclick="oamSubmitForm('mainform',this.id);"/>

			<h:outputText value="Command Link partial update:" />
			<t:commandLink id="link" >
				<h:outputText value="Click here" />
			</t:commandLink>
			

			<h:outputText value="First PPRPanelGroup:" />
			<s:pprPanelGroup id="ppr1"
				partialTriggers="secondform:pprSubmit3,partialCheckBox,partialDropDown,partialAction,link,pprSubmit1#{pprExampleBean.partialUpdateConfiguredButton ? ',configuredSubmit' : ''}">
				<h:outputText value="#{pprExampleBean.textField}" />
			</s:pprPanelGroup>
		</h:panelGrid>
	</h:form>
	<h:form id="secondform">
		<h:panelGrid columns="2">
			<h:outputText value="Second PPRPanelGroup:" />
			<s:pprPanelGroup id="ppr2"
				partialTriggers="mainform:partialCheckBox,mainform:partialDropDown,mainform:partialAction,mainform:link,mainform:pprSubmit1">
				<h:commandButton id="pprSubmit3" value="#{pprExampleBean.textField}" />
			</s:pprPanelGroup>

			<h:outputText value="Message:" />
			<s:pprPanelGroup id="ppr3" partialTriggerPattern="mainform:.*">
				<h:outputText value="#{pprExampleBean.message}" />
			</s:pprPanelGroup>
			
			<h:outputText value="Pattern Matching PPRPanelGroup:" />
			<s:pprPanelGroup id="ppr4" partialTriggerPattern="mainform:.*" 
				inlineLoadingMessage="Loading...">
				<h:outputText value="#{pprExampleBean.textField}" />
			</s:pprPanelGroup>
		</h:panelGrid>

	</h:form>


</f:view>

<%@include file="inc/page_footer.jsp"%>

</body>

</html>

