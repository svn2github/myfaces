<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>
<html>

<!--
/*
 * Copyright 2006 The Apache Software Foundation.
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

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <p>
        There are two forms in this page. Each one has a stateChangedNotifier, so if one of the elements in the form changes
        a confirmation window will be shown when clicking on a link within each form.
    </p>

    <p>
        <b>FORM 1:</b>
    </p>

    <h:form id="form1">
        <s:stateChangedNotifier id="stateChangedId"
                                confirmationMessage="Are you sure?"
                                disabled="false" />

        <h:panelGrid columns="1">
            <h:inputText value=""/>
            <h:selectManyCheckbox>
                <f:selectItem itemValue="item1" itemLabel="Item 1"/>
                <f:selectItem itemValue="item2" itemLabel="Item 2"/>
                <f:selectItem itemValue="item3" itemLabel="Item 3"/>
            </h:selectManyCheckbox>
        </h:panelGrid>

        <t:commandLink value="Submit Form 1 (goes to the home page)" action="home"/>
    </h:form>

    <f:verbatim>
        <p>
            <hr/>
            <b>FORM 2:</b>
        </p>
    </f:verbatim>

    <h:form id="form2">
        <s:stateChangedNotifier confirmationMessage="Values have changed. Did you know it?"
                                disabled="false"
                                excludeCommandsWithClientIds="excludedLink1,form2:excludedLink2"/>

        <h:panelGrid columns="1">
            <h:inputText value=""/>
            <h:inputTextarea value=""/>
            <h:selectOneMenu value="item2">
                <f:selectItem itemValue="item1" itemLabel="Item 1"/>
                <f:selectItem itemValue="item2" itemLabel="Item 2"/>
                <f:selectItem itemValue="item3" itemLabel="Item 3"/>
            </h:selectOneMenu>

            <t:commandLink immediate="true" value="Submit Form 2 (goes to the home page)" action="home"/>
            <t:commandLink id="excludedLink1" forceId="true"
                       value="Excluded link (goes to the home page withouth warning)" action="home"/>
            <t:commandLink id="excludedLink2" immediate="true"
                       value="Another excluded link (goes to the home page withouth warning)" action="home"/>
        </h:panelGrid>


    </h:form>
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
