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
<h1>PPR Example - triggering a partial-page update in a data-table</h1>
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

        <t:dataTable value="#{pprExampleBean.simpleCarList}" var="carDetail" binding="#{pprExampleBean.carTable}">
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Id"/>
                </f:facet>
                <h:inputText value="#{carDetail.id}" immediate="true" valueChangeListener="#{pprExampleBean.idChanged}"/>
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Type"/>
                </f:facet>
                <h:inputText value="#{carDetail.type}" immediate="true" valueChangeListener="#{pprExampleBean.typeChanged}"/>
            </h:column>
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Color"/>
                </f:facet>
                <h:inputText value="#{carDetail.color}" immediate="true" valueChangeListener="#{pprExampleBean.colorChanged}"/>
            </h:column>
            <h:column>
                <h:commandButton id="update"/>
                <s:pprPanelGroup id="carEntryUpdate" partialTriggers="update">
                    <h:panelGrid columns="2">
                      <h:outputText value="Id"/>
                      <h:outputText value="#{carDetail.id}"/>
                      <h:outputText value="Type"/>
                      <h:outputText value="#{carDetail.type}"/>
                      <h:outputText value="Color"/>
                      <h:outputText value="#{carDetail.color}"/>
                    </h:panelGrid>
                </s:pprPanelGroup>
            </h:column>
        </t:dataTable>

        <s:fieldset legend="about this example">
            <f:verbatim>
                 <br />
                 <br />
                This example demonstrates if and how tables can be used together with the pprPanelGroup.
            </f:verbatim>
        </s:fieldset>
    </h:form>


</f:view>

<%@include file="inc/page_footer.jsp"%>

</body>

</html>


