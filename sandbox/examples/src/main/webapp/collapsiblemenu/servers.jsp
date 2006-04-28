<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>
<html>

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

<%@include file="../inc/head.inc" %>

<body>

<f:view>

    <h:form>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <s:collapsibleMenu left="0" top="85px" width="140px" height="85%">    
      
      <s:collapsiblePanel title="#{example_messages.panel_frequent_tasks}" >    
          <s:collapsibleIcon url="../images/users.gif" title="#{example_messages.icon_users}" 
                  action="#{collapsibleMenuBean.viewUsers}" actionListener="#{collapsibleMenuBean.actionListener}">
              <f:param name="bogus1" value="bogus2"/>					              
           </s:collapsibleIcon> 
           <s:collapsibleIcon url="../images/mail.gif" title="#{example_messages.icon_mail}" action="mail"/>        
      </s:collapsiblePanel>    

      <s:collapsiblePanel title="#{example_messages.panel_administration}" displayed="true">    
        <s:collapsibleIcon url="../images/databases.gif" title="#{example_messages.icon_databases}" 
                       action="#{collapsibleMenuBean.viewDatabases}">        
          <f:param name="bogus1" value="bogus2"/>					              
        </s:collapsibleIcon> 
        <s:collapsibleIcon url="../images/servers.gif" title="#{example_messages.icon_servers}" action="servers"/>        
      </s:collapsiblePanel>    

      <s:collapsiblePanel title="#{example_messages.panel_reports}">    
        <s:collapsibleIcon url="../images/statistics.gif" title="#{example_messages.icon_statistics}"
                       action="#{collapsibleMenuBean.viewStatistics}">        
          <f:param name="bogus1" value="bogus2"/>					              
        </s:collapsibleIcon> 
        <s:collapsibleIcon url="../images/charts.gif" title="#{example_messages.icon_charts}" action="charts"/>        
      </s:collapsiblePanel>    

    </s:collapsibleMenu>  
    
    <div class="mainPane">
  	<h:outputText>SERVERS</h:outputText>
        <f:verbatim><br/></f:verbatim>
	<h:outputText>Test: When the page submits to itself, the same panel must be expanded</h:outputText>	
        <f:verbatim><br/></f:verbatim>
	<h:commandButton value="Submit"/>
     </div>
   </h:form>
</f:view>


<%@include file="../inc/page_footer.jsp" %>

</body>

</html>



