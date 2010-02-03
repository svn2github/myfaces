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
    <h:form id="testForm">
        <h:panelGrid>
             <t:messages showDetail="true" showSummary="false"/>
            <h:outputText value="Example for disabled input value submit"/>

                <script type="text/javascript"><!--
                    function changeDisabledInputValue() {
                        var fName = document.getElementById("testForm:firstName");
                        var lName = document.getElementById("testForm:lastName");
                        var fullName = document.getElementById("testForm:fullName");
                        fullName.value = fName.value+" "+lName.value;
                        fullName.onchange();
                    }
                //--></script>

            <h:outputText value="Person" style="font-weight:bold;"/>
            <h:panelGrid columns="2">
                <h:outputLabel for="firstName" value="First Name:"/>
                <h:inputText id="firstName" value="#{inputTextDisabled.firstName}"/>
                <h:outputLabel for="lastName" value="Last Name:"/>
                <h:inputText id="lastName" value="#{inputTextDisabled.lastName}"/>
                <h:outputLabel for="fullName" value="Full Name:"/>
                <t:inputText id="fullName" value="#{inputTextDisabled.fullName}" disabledOnClientSide="true">
                    <f:validateLength maximum="20" minimum="2"/>
                </t:inputText>
            </h:panelGrid>
            <h:outputLink value="#" onclick="changeDisabledInputValue();"><f:verbatim>Change disabled input field's value</f:verbatim></h:outputLink>
            <h:commandButton value="Show current values of Person"/>
        </h:panelGrid>


        <h:panelGrid columns="2">
            <h:outputText value="First name of Person:"/>
            <h:outputText value="#{inputTextDisabled.firstName}"/>
            <h:outputText value="Last name of Person:"/>
            <h:outputText value="#{inputTextDisabled.lastName}"/>
            <h:outputText value="Full name of Person:"/>
            <h:outputText value="#{inputTextDisabled.fullName}"/>
        </h:panelGrid>

    </h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
