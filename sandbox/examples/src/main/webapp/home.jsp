<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>

    <%@include file="inc/head.inc" %>

    <f:view>

        <h:panelGrid>
            <h:panelGrid id="header_group1" columns="2" styleClass="pageHeader"  >
                <t:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="" />
                <f:verbatim>
                    <h:outputText style="font-size:20px;color:#FFFFFF;" escape="false" value="MyFaces - The free JavaServer&#8482; Faces Implementation"/>
                    <h:outputText style="font-size:10px;color:#FFFFFF;"value="(Version 1.1.1)"/>
                </f:verbatim>
            </h:panelGrid>

            <h:outputLink value="inputSuggestAjax.jsf" ><f:verbatim>InputSuggestAjax</f:verbatim></h:outputLink>
            <h:outputLink value="inputSuggest.jsf" ><f:verbatim>Input Suggest</f:verbatim></h:outputLink>
            <h:outputLink value="schedule.jsf" ><f:verbatim>Schedule</f:verbatim></h:outputLink>
            <h:outputLink value="autoUpdateDataTable.jsf" ><f:verbatim>Automatically updated dataTable per AJAX</f:verbatim></h:outputLink>
            <h:outputLink value="inputAjax.jsf" ><f:verbatim>AJAX Form Components</f:verbatim></h:outputLink>            
            <h:outputLink value="accordionPanel.jsf" ><f:verbatim>AccordionPanel</f:verbatim></h:outputLink>
            <h:outputLink value="validateUrl.jsf" ><f:verbatim>Validation example 2 - including URL validator</f:verbatim></h:outputLink>
            <h:outputLink value="graphicImageDynamic.jsf" ><f:verbatim>GraphicImageDynamic - graphic image without a dedicated servlet</f:verbatim></h:outputLink>
            <h:outputLink value="graphicImageDynamicText.jsf" ><f:verbatim>GraphicImageDynamic - text rendered as graphic image</f:verbatim></h:outputLink>
            <h:outputLink value="effect.jsf" ><f:verbatim>Effect - FAT and script.aculo.us effects</f:verbatim></h:outputLink>
			<h:commandLink action="go_scope_shop"><f:verbatim>Scope - saveState without serialisation</f:verbatim></h:commandLink>
			<h:outputLink value="picklist.jsf"><f:verbatim>selectManyPicklist - a picklist</f:verbatim></h:outputLink>
            <h:outputLink value="dateTimeConverter.jsf"><f:verbatim>DateTimeConverter - a datetime converter that uses system timezone as default</f:verbatim></h:outputLink>
            <h:outputLink value="focus.jsf"><f:verbatim>Focus - a component to set a target component as the focus on page load.</f:verbatim></h:outputLink>
        </h:panelGrid>
    </f:view>

</html>
