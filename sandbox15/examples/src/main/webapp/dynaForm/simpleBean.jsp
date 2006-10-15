<%--
  Copyright 2006 The Apache Software Foundation.

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy of
  the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations under
  the License.
  --%>

<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox15" prefix="sn"%>

<f:view>
<t:document>
<t:documentHead>
    <%@include file="/inc/head.inc" %>
</t:documentHead>
<t:documentBody>

    <h:form>
        <%@include file="/inc/page_header.jsp" %>

        <f:loadBundle basename="org.apache.myfaces.examples.resource.simpleBean_messages" var="simpleBeanBundle"/>

        <h:panelGrid>

            <t:htmlTag value="h2">
                <h:outputText value="A simple bean input form" />
            </t:htmlTag>

            <sn:dynaForm
                    var="simpleBean"
                    uri="org.apache.myfaces.examples.dynaForm.SimpleBean"
                    valueBindingPrefix="simpleBeanBacking.simpleBean"
                    bundle="simpleBeanBundle">
                
                <h:panelGrid
                        id="simpleBean-layout"
                        columns="2" />
            </sn:dynaForm>


            <t:htmlTag value="h2">
                <h:outputText value="A simple bean list form" />
            </t:htmlTag>

            <sn:dynaForm
                    var="simpleBean2"
                    uri="org.apache.myfaces.examples.dynaForm.SimpleBean"
                    bundle="simpleBeanBundle">
                <h:dataTable
                        var="entry"
                        id="simpleBean2-layout"
                        value="#{simpleBeanBacking.simpleBeans}" />
            </sn:dynaForm>


            <t:htmlTag value="h2">
                <h:outputText
                        value="A simple bean list form - display only" />
            </t:htmlTag>

            <sn:dynaForm
                    var="simpleBean3"
                    uri="org.apache.myfaces.examples.dynaForm.SimpleBean"
                    displayOnly="true"
                    bundle="simpleBeanBundle">
                <h:dataTable
                        var="entry"
                        id="simpleBean3-layout"
                        value="#{simpleBeanBacking.simpleBeans}" />
            </sn:dynaForm>


            <h:commandButton/>
        </h:panelGrid>

        <%@include file="/inc/page_footer.jsp" %>
    </h:form>

</t:documentBody>
</t:document>
</f:view>

