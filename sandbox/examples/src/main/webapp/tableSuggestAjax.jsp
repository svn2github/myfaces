<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<html>

<%@include file="inc/head.inc" %>

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
    
   <h:form id="ajaxform">

     <f:verbatim><br/><br/><br/><br/></f:verbatim>

     <h:panelGrid columns="2">
        <h:panelGrid columns="4">
         <f:verbatim> City Field TableSuggest <br/>  (Paginator) <br/> Suggest starts with 2. char </f:verbatim>
         <s:tableSuggestAjax var="address" id="suggest" startRequest="2"
                             nextPageFieldClass="ajaxNextTablePageField"
                             value="#{inputSuggestAjax.suggestValue}" betweenKeyUp="300"
                             columnOutClass="tableSuggestOut" columnHoverClass="tableSuggestHover"
                             suggestedItemsMethod="#{inputSuggestAjax.getAddressList}"
                             maxSuggestedItems="10" tableStyleClass="ajaxTable">
            <t:column>
                 <f:facet name="header">
                     <s:outputText value="city"/>
                 </f:facet>
                 <s:outputText for="cityField" label="#{address.city}"/>
             </t:column>
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="street"/>
                 </f:facet>
                 <s:outputText for="streetNameField" label="#{address.streetName}"/>
             </t:column>
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="state"/>
                 </f:facet>
                 <s:outputText forValue="stateField" label="#{address.state}" value="#{address.zip}"/>
             </t:column>
         </s:tableSuggestAjax>
         <f:verbatim> City Field TableSuggest <br/>  (Scrolling) <br/> If first chars are "as" scrolling list appears </f:verbatim>
         <s:tableSuggestAjax var="address" id="cityField2" tableStyleClass="ajaxTableWidth"
                             nextPageFieldClass="ajaxNextTablePageField" betweenKeyUp="300"
                             columnOutClass="tableSuggestOut" columnHoverClass="tableSuggestHover"
                             maxSuggestedItems="50" popupStyle="position:absolute; z-index:100000; overflow:auto; height:200px;"
                             suggestedItemsMethod="#{inputSuggestAjax.getAddressList}">
            <t:column>
                 <s:outputText for="cityField2" label="#{address.city}"/>
             </t:column>
             <t:column>
                 <s:outputText for="streetNameField" label="#{address.streetName}"/>
             </t:column>
             <t:column>
                 <s:outputText forValue="stateField" label="#{address.state}" value="#{address.zip}"/>
             </t:column>
         </s:tableSuggestAjax>
     </h:panelGrid>
         <h:panelGrid>
             <h:commandButton/>
             <h:outputText value="Street"/>
             <t:inputText id="streetNameField" />
             <h:outputText value="State"/>
             <t:selectOneMenu id="stateField">
                  <f:selectItem value="" itemLabel="NY" itemValue="11"/>
                  <f:selectItem value="" itemLabel="IL" itemValue="12"/>
                  <f:selectItem value="" itemLabel="NW" itemValue="13"/>
                  <f:selectItem value="" itemLabel="SJ" itemValue="14"/>
                  <f:selectItem value="" itemLabel="KL" itemValue="15"/>
                  <f:selectItem value="" itemLabel="MH" itemValue="16"/>
             </t:selectOneMenu>
         </h:panelGrid>
 </h:panelGrid>
    </h:form>
    
</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>

