<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<!--
/*
 * Copyright 2005 The Apache Software Foundation.
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
<html>

    <%@include file="inc/head.inc" %>

    <body>
        <f:view>
        	<t:htmlTag value="h1">Schedule examples</t:htmlTag>
        	<h:panelGrid columns="2">
        		<h:commandLink value="Example 1" action="schedule1" />
        		<h:outputText value="A very simple example, containing a
        		schedule component in workweek mode, and a calendar
        		component that is used to navigate through the schedule. The
        		schedule is backed by a SimpleScheduleModel, storing
        		appointments in session scope. " />
        		<h:commandLink value="Example 2" action="schedule2" />
        		<h:outputText value="The same example, but now with the
        		possibility to add and delete appointments." />
        		<h:commandLink value="Example 3" action="schedule3" />
        		<h:outputText value="This example shows how you can customize
        		the look and feel of the schedule component" />
        		<h:commandLink value="Example 4" action="schedule4" />
        		<h:outputText value="A rather ugly example showing how you
        		can override the theme for some parts of the schedule. Also
        		a custom EntryRenderer, which assigns a random color to each
        		entry, is used."/>
        	</h:panelGrid>
        </f:view>
    </body>

    <%@include file="inc/page_footer.jsp" %>

</html>
