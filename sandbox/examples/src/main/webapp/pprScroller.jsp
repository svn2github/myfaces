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

    <t:saveState value="#{pprScrollerExampleBean.cars}" />

    <t:messages id="messageList" styleClass="error" showDetail="true"
        summaryFormat="{0} " />

    <h:form id="tableForm">
	<s:pprPanelGroup id="updatedData" partialTriggerPattern="tableForm:scroll_1.*">
		<t:dataTable id="data"
                  styleClass="scrollerTable"
                  headerClass="standardTable_Header"
                  footerClass="standardTable_Header"
                  rowClasses="standardTable_Row1,standardTable_Row2"
                  columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                  var="car"
                  value="#{pprScrollerExampleBean.cars}"
                  preserveDataModel="true"
                  rows="10"
                  rowId="#{car.type}"
                  rowOnClick="alert('rowId: ' + this.id)"
                  sortColumn="#{pprScrollerExampleBean.sort}"
                  sortAscending="#{pprScrollerExampleBean.ascending}"
                  preserveSort="true">
        <h:column>
            <h:outputText value="#{car.id}" />
        </h:column>
        <h:column>
            <f:facet name="header">
                <t:commandSortHeader columnName="type" arrow="true" immediate="false">
                    <h:outputText value="BliBli" />
                </t:commandSortHeader>
            </f:facet>
            <h:outputText value="#{car.type}" />
        </h:column>
        <h:column>
            <f:facet name="header">
                <t:commandSortHeader columnName="color" arrow="true" immediate="false">
                    <h:outputText value="BlaBla" />
                </t:commandSortHeader>
            </f:facet>
            <h:inputText value="#{car.color}" >
                <f:validateLength maximum="10"/>
            </h:inputText>
        </h:column>
    </t:dataTable>
    <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
        <t:dataScroller id="scroll_1"
                        for="data"
                        fastStep="10"
                        pageCountVar="pageCount"
                        pageIndexVar="pageIndex"
                        styleClass="scroller"
                        paginator="true"
                        paginatorMaxPages="9"
                        paginatorTableClass="paginator"
                        paginatorActiveColumnStyle="font-weight:bold;">
            <f:facet name="first" >
                <t:graphicImage url="images/arrow-first.gif" border="1" />
            </f:facet>
            <f:facet name="last">
                <t:graphicImage url="images/arrow-last.gif" border="1" />
            </f:facet>
            <f:facet name="previous">
                <t:graphicImage url="images/arrow-previous.gif" border="1" />
            </f:facet>
            <f:facet name="next">
                <t:graphicImage url="images/arrow-next.gif" border="1" />
            </f:facet>
            <f:facet name="fastforward">
                <t:graphicImage url="images/arrow-ff.gif" border="1" />
            </f:facet>
            <f:facet name="fastrewind">
                <t:graphicImage url="images/arrow-fr.gif" border="1" />
            </f:facet>
        </t:dataScroller>
    </h:panelGrid>
    </s:pprPanelGroup>
    </h:form>
</f:view>



<%@include file="inc/page_footer.jsp"%>

</body>

</html>

