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

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>
    <h:form>
        <s:accordionPanel id="test1" layout="accordion">
            <t:panelTab label="Content 1">
                <h:outputText value="Content 1"/>
                <h:inputText value="" onchange="this.form.submit();"/>
            </t:panelTab>
            <t:panelTab label="Content 2">
                <h:outputText value="Content 1"/>
                <h:inputText value="" onchange="this.form.submit();"/>
            </t:panelTab>
        </s:accordionPanel>

        <s:accordionPanel id="test2"
                          layout="toggling"
                          collapsedTextColor="lightgray"
                          collapsedFontWeight="normal"
                          collapsedBackColor="lightgray"
                          expandedTextColor="lightgray"
                          expandedFontWeight="bold"
                          expandedBackColor="gray"
                          hoverTextColor="white"
                          hoverBackColor="black"
                          borderColor="blue">
            <t:panelTab>
                <f:facet name="header">
                    <h:outputText value="Header in facet"/>
                </f:facet>
                <f:facet name="closedContent">
                    <h:outputText value="The tab is now closed - open it by clicking on the header."/>
                </f:facet>
                <h:outputText value="Content 1"/>
                <h:inputText value=""/>
            </t:panelTab>
            <t:panelTab label="Content 2">
                <h:outputText value="Content 1"/>
                <h:inputText value=""/>
            </t:panelTab>
            <t:panelTab label="Content 2">
                <h:outputText value="Content 1"/>
                <h:inputText value=""/>
            </t:panelTab>
        </s:accordionPanel>
    </h:form>
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
