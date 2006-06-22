﻿<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>

    <%@include file="/inc/head.inc" %>
<body>
    <f:view>
		<h:form>

			Press the button to issue a redirect navigation request and see how the messages will survive.

			<h:commandButton value="Press Me" action="#{requestTrackerRedirectBean.redirectAction}" />
			
		</h:form>
	</f:view>
</body>
</html>
