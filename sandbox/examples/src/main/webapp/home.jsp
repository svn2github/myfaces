﻿﻿<%@ page session="false" contentType="text/html;charset=utf-8"%>
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

            <h:outputText value="Resource Serving"/>
            <h:panelGrid style="padding-left:25px">
	            <h:outputLink value="graphicImageDynamic.jsf" ><f:verbatim>GraphicImageDynamic - graphic image without a dedicated servlet</f:verbatim></h:outputLink>
	            <h:outputLink value="graphicImageDynamicText.jsf" ><f:verbatim>GraphicImageDynamic - text rendered as graphic image</f:verbatim></h:outputLink>
	            <h:outputLink value="outputLinkDynamic.jsf" ><f:verbatim>OutputLinkDynamic - resource serving from a link without a dedicated servlet</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Input Handling"/>
            <h:panelGrid style="padding-left:25px">
            	<h:outputLink value="focus.jsf"><f:verbatim>Focus - a component to set a target component as the focus on page load.</f:verbatim></h:outputLink>
            	<h:outputLink value="stateChangedNotifier.jsf"><f:verbatim>stateChangedNotifier - Shows a confirmation message if some of the fields in the form have changed</f:verbatim></h:outputLink>
	            <h:outputLink value="dateTimeConverter.jsf"><f:verbatim>DateTimeConverter - a datetime converter that uses system timezone as default</f:verbatim></h:outputLink>
	            <h:outputLink value="valueChangeNotifier.jsf"><f:verbatim>valueChangeNotifier - Calls a custom valueChangeEvent method during MODEL_UPDATE Phase of the Faces Lifecycle</f:verbatim></h:outputLink>
	            
	            <h:outputText value="Validation"/>
	            <h:panelGrid style="padding-left:25px">
	            	<h:outputLink value="validateUrl.jsf" ><f:verbatim>Validation example 2 - including URL validator</f:verbatim></h:outputLink>
	            	<h:outputLink value="validateCompareTo.jsf" ><f:verbatim>validateCompareTo - Compare values on two different components</f:verbatim></h:outputLink>
	            	<h:outputLink value="subForm.jsf"><f:verbatim>SubForm - Partial validation and model update with SubForms</f:verbatim></h:outputLink>
	            </h:panelGrid>

            </h:panelGrid>

            <h:outputText value="Layout"/>
            <h:panelGrid style="padding-left:25px">
            	<h:outputLink value="accordionPanel.jsf" ><f:verbatim>AccordionPanel</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Input Suggest"/>
            <h:panelGrid style="padding-left:25px">
	            <h:outputLink value="inputSuggestAjax.jsf" ><f:verbatim>InputSuggestAjax - Suggested items list through Ajax</f:verbatim></h:outputLink>
                <h:outputLink value="tableSuggestAjax.jsf" ><f:verbatim>TableSuggestAjax - Suggested table through Ajax (choosing a row puts column values to specific dom nodes) </f:verbatim></h:outputLink>
                <h:outputLink value="inputAjax.jsf" ><f:verbatim>AJAX Form Components - server side validation through ajax </f:verbatim></h:outputLink>
	            <h:outputLink value="inputSuggest.jsf" ><f:verbatim>Input Suggest - Suggest without Ajax</f:verbatim></h:outputLink>
            </h:panelGrid>
            
            <h:outputText value="Schedule"/>
            <h:panelGrid style="padding-left:25px">
	            <h:outputLink value="schedule/example1.jsf" ><f:verbatim>Schedule with sample entries</f:verbatim></h:outputLink>
	            <h:outputLink value="schedule/example2.jsf" ><f:verbatim>Schedule with possibility for adding/removing entries</f:verbatim></h:outputLink>
	            <h:outputLink value="schedule/example3.jsf" ><f:verbatim>Customizable schedule</f:verbatim></h:outputLink>
	            <h:outputLink value="schedule/example4.jsf" ><f:verbatim>Schedule with custom styleClasses and custom EntryRenderer</f:verbatim></h:outputLink>
	            <h:outputLink value="schedule/example5.jsf" ><f:verbatim>Example demonstrating the submitOnClick and mouseListener properties</f:verbatim></h:outputLink>
            </h:panelGrid>
                        
            <h:outputText value="Data Tables"/>
            <h:panelGrid style="padding-left:25px">
            	<h:outputLink value="autoUpdateDataTable.jsf" ><f:verbatim>Automatically updated dataTable per AJAX</f:verbatim></h:outputLink>
            	<h:outputLink value="selectOneRow.jsf"><f:verbatim>selectOneRow - a DataTable Enhancement</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Selection Lists"/>
            <h:panelGrid style="padding-left:25px">
               <h:outputLink value="picklist.jsf"><f:verbatim>selectManyPicklist - A picklist, where you select components from a list and the selected items are displayed in another list</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Messages"/>
            <h:panelGrid style="padding-left:25px">
               <h:outputLink value="ifMessage.jsf"><f:verbatim>ifMessage - renders its children only if there is a message in the FacesContext for the specified component(s)</f:verbatim></h:outputLink>
            </h:panelGrid>
            
            <h:outputText value="FishEye Navigation"/>
            <h:panelGrid style="padding-left:25px">
               <h:outputLink value="fisheye.jsf"><f:verbatim>fishEyeNavigationMenu - the Dojo Toolkit FishEye widget</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Miscellaneous"/>
            <h:panelGrid style="padding-left:25px">
	 			<h:commandLink action="go_scope_shop"><f:verbatim>Scope - saveState without serialisation</f:verbatim></h:commandLink>
           		<h:outputLink value="effect.jsf" ><f:verbatim>Effect - FAT and script.aculo.us effects</f:verbatim></h:outputLink>
	            <h:outputLink value="dojo/textareatestjsfonly.jsf"><f:verbatim>Integration of Dojo Toolkit</f:verbatim></h:outputLink>
            </h:panelGrid>

        </h:panelGrid>
    </f:view>
   

</html>
