<%@ page session="false" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>

<%@ include file="inc/head.inc" %>


<body>
<h1>PPR Example </h1>
<span id="cnt">0</span> seconds since last page refresh.
<script>
    var sec=0;
    function counter(){
        setTimeout("counter();",1000);
        document.getElementById("cnt").innerHTML = sec++;
    }
    counter();
</script>

<f:view>

    <h:form id="mainform" >
       

<t:messages id="messageList" styleClass="error" showDetail="true" summaryFormat="{0} "/>
    <h:inputText value="#{pprExampleBean.textField}" />

    <h:commandButton id="pprSubmit1" value="PPR Submit" />
    <h:commandButton id="pprSubmit2" value="2 nd PPR Submit" />
    <h:commandButton id="normalSubmit" value="Submit" />

    <s:pprPanelGroup id="ppr1" partialTriggers="mainform:pprSubmit1,mainform:pprSubmit2">
        <h:outputText value="#{pprExampleBean.textField}" />
    </s:pprPanelGroup>

    <s:pprPanelGroup id="ppr2" partialTriggers="mainform:pprSubmit1,mainform:pprSubmit2">
         <h:outputText value="#{pprExampleBean.textField}" />
    </s:pprPanelGroup>

    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>

