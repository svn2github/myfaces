<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc"%>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<body>

<f:view>
    <h:form id="test">
        <h:panelGrid columns="1">
            <t:dataTable id="data1" value="#{testColspanBean.lines}" var="line" border="2" width="100%">
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

            <t:dataTable id="data2" value="#{testColspanBean.lines}" var="line" border="2" width="100%">
                <t:column >
                    <f:facet name="header">
                        <f:verbatim escape="false">head 1</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 1</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col1}" />
                </t:column>
                <t:column colspan="2" >
                    <f:facet name="header">
                        <f:verbatim escape="false">head 2</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 2</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col2}" />
                </t:column>
                <t:column headercolspan="3" footercolspan="3">
                    <f:facet name="header">
                        <f:verbatim escape="false">head 3</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 3</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col3}" />
                </t:column>
                <t:column>
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

            <t:dataTable id="data3" value="#{testColspanBean.lines}" var="line" border="2" width="100%">
                <t:column headercolspan="2" >
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
                <t:column colspan="3" >
                    <f:facet name="header">
                        <f:verbatim escape="false">head 3</f:verbatim>
                    </f:facet>
                    <f:facet name="footer">
                        <f:verbatim escape="false">foot 3</f:verbatim>
                    </f:facet>
                    <h:outputText value="#{line.col3}" />
                </t:column>
                <t:column headercolspan="2" >
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
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
