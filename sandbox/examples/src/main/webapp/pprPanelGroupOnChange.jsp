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
<h1>PPR Example - using PPR with drop-down and checkbox onChange handlers</h1>
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
        <t:commandLink />
		<h:panelGrid columns="2">

			<h:outputText value="update group by checking:" />
			<h:selectBooleanCheckbox id="checkbox"
                    value="#{pprExampleBean.checkBoxValue}"
			        onclick="submit(this);"/>
			
			<h:outputText value="update group by changing the value:" />
			<h:selectOneMenu id="dropDown" onchange="submit(this);"
				value="#{pprExampleBean.dropDownValue}"	>
				<f:selectItem itemLabel="test1" itemValue="value1"/>
				<f:selectItem itemLabel="test2" itemValue="value2"/>
				<f:selectItem itemLabel="test3" itemValue="value3"/>
			</h:selectOneMenu>
			
			<h:outputText value="Checkbox is:" />
			<s:pprPanelGroup id="pprCheckBoxValue"
				partialTriggers="checkbox">
				<h:outputText value="#{pprExampleBean.checkBoxValue ? 'checked' : 'not checked'}" />
			</s:pprPanelGroup>

            <h:outputText value="Drop Down value is:" />
			<s:pprPanelGroup id="dropDownPPR"
				partialTriggers="dropDown">
				<h:outputText value="#{pprExampleBean.dropDownValue}" />
			</s:pprPanelGroup>
        </h:panelGrid>

        <s:fieldset legend="about this example">
         <f:verbatim>
             <br />
             <br />
            This example shows how onClick-Handlers of checkboxes or dropdowns <br />
            can be used to update page regions via AJAX.<br />
            It is vital, that the this element is included as parameter in the call <br />
            of the submit function so the PPRCtrl knows which element tries to submit <br />
            and therefore is able to determine wheather or not matching partialTriggers <br />
            and/or partialTriggerPatterns are present.
        </f:verbatim>
    </s:fieldset>

    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp"%>

</body>

</html>

