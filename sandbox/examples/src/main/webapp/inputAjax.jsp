<%@ page session="false" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s" %>

<html>

<%@ include file="inc/head.inc" %>

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

<script type="text/javascript">
    function successful(elname, elvalue)
    {
        var statusDiv = document.getElementById("statusDiv");
        statusDiv.innerHTML = "YAY!";
    }
    function failed(elname, elvalue)
    {
        var statusDiv = document.getElementById("statusDiv");
        statusDiv.innerHTML = "DOH!";
    }
</script>
<style type="text/css">
    .bold {
        font-weight: bold;
    }

    .error {
        font-weight: bold;
        color: red;
    }
</style>

<h1>Ajax Form Components</h1>

<p>The backend data model will update without having to click the submit button. To see error handling, put in some
    random text into the Date field.</p>
<f:view>

<h:form>
    <h:panelGrid columns="2" columnClasses="bold,normal">
        <h:outputText value="Input Some Text"/>
        <h:panelGrid columns="1">
            <s:inputTextAjax value="#{inputAjaxBean.text1}"
                             id="text1"
                             forceId="true"></s:inputTextAjax>
            <f:verbatim>This component demonstrates ajax updating ability when you change the
                text.</f:verbatim>
        </h:panelGrid>

        <h:outputText value="Input Some Text2"/>
        <h:panelGrid columns="1">
            <s:inputTextAjax value="#{inputAjaxBean.text2}"
                             id="text2"
                             forceId="true"
                             showOkButton="true"
                             showCancelButton="true"
                             validator="#{inputAjaxBean.validateText2}">
                <f:valueChangeListener type="org.apache.myfaces.custom.inputAjax.SampleValueChangeListener"/>
            </s:inputTextAjax>
            <s:message for="text2" styleClass="error"/>
            <f:verbatim>This component demonstrates ajax updating ability, but you must click Ok for it to send.
                Cancel will clear the text and not send an update. Will show a validation error if the string is
                less then 3 characters.</f:verbatim>
        </h:panelGrid>

        <h:outputText value="Input a Date"/>
        <h:panelGrid columns="1">
            <s:inputTextAjax value="#{inputAjaxBean.date1}"
                             id="date1"
                             forceId="true">
                <s:convertDateTime pattern="yyyy-MM-dd"/>
            </s:inputTextAjax>
            <s:message for="date1" styleClass="error"/>
            <f:verbatim>This component demonstrates error handling capabilities, enter an invalid string to see the
                error returned from the server through ajax.</f:verbatim>
        </h:panelGrid>


        <h:outputText value="Select Some Boxes"/>
        <h:panelGrid columns="1">
            <s:selectManyCheckboxAjax
                    id="smcb"
                    forceId="true"
                    value="#{inputAjaxBean.chosenValues}">
                <f:selectItems
                        value="#{inputAjaxBean.checkboxItems}"/>
            </s:selectManyCheckboxAjax>
            <f:verbatim>This component demonstrates ajax updating ability on a checkboxes.</f:verbatim>
        </h:panelGrid>

        <h:outputText value="Select A Radio Button"/>
        <h:panelGrid columns="1">
            <s:selectOneRadioAjax
                    id="radio1"
                    forceId="true"
                    value="#{inputAjaxBean.radioValue}">
                <f:valueChangeListener type="org.apache.myfaces.custom.inputAjax.SampleValueChangeListener"/>
                <f:selectItems
                        value="#{inputAjaxBean.radioItems}"/>
            </s:selectOneRadioAjax>
            <f:verbatim>This component demonstrates ajax updating ability on a radio buttons.</f:verbatim>
        </h:panelGrid>

        <h:outputText value="Toggle Switch"/>
        <h:panelGrid columns="1">
            <h:panelGroup>
                <s:selectBooleanCheckboxAjax
                        id="toggle1"
                        forceId="true"
                        value="#{inputAjaxBean.toggle1}"/>
                <h:outputText value="Got Milk?"/>
            </h:panelGroup>
            <s:message for="toggle1" styleClass="error"/>
            <f:verbatim>This component demonstrates ajax updating ability based on a toggle switch.</f:verbatim>
        </h:panelGrid>

        <h:outputText value="Toggle Switch With Images"/>
        <h:panelGrid columns="1">
            <h:panelGroup>
                <s:selectBooleanCheckboxAjax
                        id="toggle2"
                        forceId="true"
                        value="#{inputAjaxBean.toggle2}"
                        onImage="images/nav-plus.gif"
                        offImage="images/nav-minus.gif">
                    <f:valueChangeListener type="org.apache.myfaces.custom.inputAjax.SampleValueChangeListener"/>
                        </s:selectBooleanCheckboxAjax>
                <h:outputText value="Got Juice?"/>
            </h:panelGroup>
            <s:message for="toggle2" styleClass="error"/>
            <f:verbatim>This component demonstrates ajax updating ability based on a toggle switch.</f:verbatim>
        </h:panelGrid>


    </h:panelGrid>
    <t:div id="statusDiv" forceId="true"></t:div>
    <h:commandButton action="#{inputAjaxBean.submit}" value="Submit"/>
</h:form>

<h:outputLink value="inputAjax.jsf"><h:outputText value="Refresh"></h:outputText></h:outputLink>

<t:htmlTag value="br"/>
<h:outputText value="Current chosen checkbox values in server model"/>
<t:htmlTag value="br"/>
<h:dataTable
        value="#{inputAjaxBean.chosenValues}"
        var="cvalue">
    <h:column>
        <f:facet name="header">
            <h:outputText value="value"/>
        </f:facet>
        <h:outputText value="#{cvalue}"/>
    </h:column>

</h:dataTable>

</f:view>

<%@ include file="inc/page_footer.jsp" %>

</body>

</html>

