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

      <s:dojoInitializer require="dojo.widget.Editor" debug="true"/>
       <s:dojoInitializer require="dojo.widget.DebugConsole" />
       <s:dojoInitializer require="dojo.widget.ResizeHandle" />
    
   <h:form>

     <h:outputText value="Street"/>
     <t:inputText id="streetNameField" />
     <h:outputText value="Number"/>
     <t:selectOneMenu id="streetNumberField">
          <f:selectItem value="" itemLabel="11" itemValue="11"/>
          <f:selectItem value="" itemLabel="12" itemValue="12"/>
          <f:selectItem value="" itemLabel="13" itemValue="13"/>
          <f:selectItem value="" itemLabel="14" itemValue="14"/>
          <f:selectItem value="" itemLabel="15" itemValue="15"/>
     </t:selectOneMenu>
     <h:outputText value="Zip"/>
     <t:inputText id="zipField"/>

     <f:verbatim><br/><br/></f:verbatim>

     <h:panelGrid columns="6">
         <h:outputText value="City Field TableSuggest"/>
         <s:inputSuggestAjax var="address" id="cityField"  suggestedItemsMethod="#{inputSuggestAjax.getAddressList}">
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="city"/>
                 </f:facet>
                 <s:outputText for="cityField" value="#{address.city}"/>
             </t:column>
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="street"/>
                 </f:facet>
                 <s:outputText for="streetNameField" value="#{address.streetName}"/>
             </t:column>
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="number"/>
                 </f:facet>
                 <s:outputText for="streetNumberField" value="#{address.streetNumber}"/>
             </t:column>
             <t:column>
                 <f:facet name="header">
                     <s:outputText value="zip"/>
                 </f:facet>
                 <s:outputText for="zipField" value="#{address.zip}"/>
             </t:column>
         </s:inputSuggestAjax>

         <h:outputText value="default suggest"/>
         <s:inputSuggestAjax suggestedItemsMethod="#{inputSuggestAjax.getItems}"/>

         <h:outputText value="suggest with limited suggested items"/>
         <s:inputSuggestAjax suggestedItemsMethod="#{inputSuggestAjax.getItems}" maxSuggestedItems="2" />
     </h:panelGrid>

     <f:verbatim><br/><br/><br/><br/><br/><br/><br/><br/></f:verbatim>
     <s:dojoInitializer debugConsole="true"/>

    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>

