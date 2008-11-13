<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
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

        <h:outputText value="Output:" />
        <h:panelGrid columns="2">
            <h:outputText value="'first' will display the first rendered child component only (will render only B):" />
            <t:renderOne>
                <h:outputText value="<b>A - FirstItem</b>" rendered="#{renderOne.ARendered}" escape="false"/>
                <h:outputText value="<b>B - SecondItem</b>" rendered="#{renderOne.BRendered}" escape="false"/>
                <h:outputText value="<b>C - ThirdItem</b>" rendered="#{renderOne.CRendered}" escape="false"/>
                <h:outputText value="<b>D - FourthItem</b>" rendered="#{renderOne.DRendered}" escape="false"/>
            </t:renderOne>

            <h:outputText value="'index' will display the third component only (will render only C):" />
            <t:renderOne type="index" value="#{renderOne.index}">
                <h:outputText value="<b>A - FirstItem</b>" rendered="#{renderOne.ARendered}" escape="false"/>
                <h:outputText value="<b>B - SecondItem</b>" rendered="#{renderOne.BRendered}" escape="false"/>
                <h:outputText value="<b>C - ThirdItem</b>" rendered="#{renderOne.CRendered}"  escape="false"/>
                <h:outputText value="<b>D - FourthItem</b>" rendered="#{renderOne.DRendered}" escape="false"/>
            </t:renderOne>
        
        </h:panelGrid>
        
    </h:form>
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>
</html>
