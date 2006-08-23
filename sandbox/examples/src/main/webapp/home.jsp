<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>


<html>

    <%@include file="inc/head.inc" %>
<body>
    <f:view>

        <f:loadBundle basename="org.apache.myfaces.examples.resource.build" var="buildInfo"/>
    <h:form>

        <h:panelGrid>
            <h:panelGrid id="header_group1" columns="2" styleClass="pageHeader"  >
                <h:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="" />
                <f:verbatim>
                    <h:outputText style="font-size:20px;color:#FFFFFF;" escape="false" value="MyFaces - The free JavaServer&#8482; Faces Implementation"/>
                    <h:outputText style="font-size:10px;color:#FFFFFF" value=" (Sandbox Version #{buildInfo['tomahawk_version']}, using #{buildInfo ['jsf_implementation']})"/>
                </f:verbatim>
            </h:panelGrid>
            <h:outputText value="Partial Page Rendering"/>
            <h:panelGrid style="padding-left:25px">
	            <h:outputLink value="pprPanelGroup.jsf" ><f:verbatim>PPRPanelGroup - Panelgroup which gets refreshed by AJAX-Calls</f:verbatim></h:outputLink>
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
            	<h:outputLink value="timedNotifier.jsf"><f:verbatim>timedNotifier - Shows a time triggered notification dialog</f:verbatim></h:outputLink>

	            <h:outputLink value="dateTimeConverter.jsf"><f:verbatim>DateTimeConverter - a datetime converter that uses system timezone as default</f:verbatim></h:outputLink>
	            <h:outputLink value="valueChangeNotifier.jsf"><f:verbatim>valueChangeNotifier - Calls a custom valueChangeEvent method during MODEL_UPDATE Phase of the Faces Lifecycle</f:verbatim></h:outputLink>
	            <h:outputLink value="form.jsf"><f:verbatim>Form component. Standard form vs. Tomahawk/Sandbox form</f:verbatim></h:outputLink>

	            <h:outputText value="Validation"/>
	            <h:panelGrid style="padding-left:25px">
	            	<h:outputLink value="validateUrl.jsf" ><f:verbatim>Validation example 2 - including URL validator</f:verbatim></h:outputLink>
	            	<h:outputLink value="validateCompareTo.jsf" ><f:verbatim>validateCompareTo - Compare values on two different components</f:verbatim></h:outputLink>
	            	<h:outputLink value="subForm.jsf"><f:verbatim>SubForm - Partial validation and model update with SubForms</f:verbatim></h:outputLink>
	            	<h:outputLink value="validateCSV.jsf"><f:verbatim>CSVValidator - validate comma separated values with a given (sub)validator</f:verbatim></h:outputLink>
	            </h:panelGrid>

            </h:panelGrid>

            <h:outputText value="Layout"/>
            <h:panelGrid style="padding-left:25px">
            	<h:outputLink value="accordionPanel.jsf" ><f:verbatim>AccordionPanel</f:verbatim></h:outputLink>
                <h:outputLink value="hmenu.jsf" ><f:verbatim>Horizontal Menu</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Input Suggest"/>
            <h:panelGrid style="padding-left:25px">
	            <h:outputLink value="inputSuggestAjax.jsf" ><f:verbatim>InputSuggestAjax - Suggested items list through Ajax</f:verbatim></h:outputLink>
                <h:outputLink value="tableSuggestAjax.jsf" ><f:verbatim>TableSuggestAjax - Suggested table through Ajax (choosing a row puts column values to specific dom nodes) </f:verbatim></h:outputLink>
                <h:outputLink value="inputAjax.jsf" ><f:verbatim>AJAX Form Components - server side validation through ajax </f:verbatim></h:outputLink>
	            <h:outputLink value="inputSuggest.jsf" ><f:verbatim>Input Suggest - Suggest without Ajax</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Data Tables"/>
            <h:panelGrid style="padding-left:25px">
            	<h:outputLink value="autoUpdateDataTable.jsf" ><f:verbatim>Automatically updated dataTable per AJAX</f:verbatim></h:outputLink>
            	<h:outputLink value="selectOneRow.jsf"><f:verbatim>selectOneRow - a DataTable Enhancement</f:verbatim></h:outputLink>
            </h:panelGrid>

            <h:outputText value="Selection Lists"/>
            <h:panelGrid style="padding-left:25px">
               <h:outputLink value="picklist.jsf"><f:verbatim>selectManyPicklist - A picklist, where you select components from a list and the selected items are displayed in another list</f:verbatim></h:outputLink>
               <h:outputLink value="selectItems.jsf"><f:verbatim>selectitems - An extended version of the UISelectItems, implicitly populates the SelectItem collection from the given value</f:verbatim></h:outputLink>
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
	 			<h:commandLink action="go_scope_shop"><f:verbatim>Scopeshop1, an extended saveState Example showing a wizard</f:verbatim></h:commandLink>
           		<h:outputLink value="effect.jsf" ><f:verbatim>Effect - DOJO and script.aculo.us effects</f:verbatim></h:outputLink>
	            <h:outputLink value="dojo/textareatestjsfonly.jsf"><f:verbatim>Integration of Dojo Toolkit</f:verbatim></h:outputLink>
                <h:outputLink value="killSession.jsf"><f:verbatim>Kill Session - refreshes state</f:verbatim></h:outputLink>
                <h:outputLink value="typedNumberConvert.jsf"><f:verbatim>automatically convert the number to the correct type</f:verbatim></h:outputLink>

            </h:panelGrid>
            <h:panelGrid style="padding-left:25px">
                <h:outputLink value="ajaxChildComboBox.jsf" >
                    <f:verbatim>Ajax-enabled combo box - reloads its contents when the value of another combo box is changed</f:verbatim>
                </h:outputLink>
            </h:panelGrid>

            <h:outputText value="Conversation"/>
            <h:panelGrid style="padding-left:25px">
           		<h:outputLink value="conversation/index.jsf" ><f:verbatim>Conversation Tag examples</f:verbatim></h:outputLink>
            </h:panelGrid>

			<h:outputText value="Redirect Tracker"/>
			<h:panelGrid style="padding-left:25px">
				   <h:outputLink value="redirectTracker/index.jsf" ><f:verbatim>Redirect Tracker - tries to capture the current request state and reset it after a redirect</f:verbatim></h:outputLink>
			</h:panelGrid>

			<h:outputText value="XML Template"/>
            <h:panelGrid style="padding-left:25px">
           		<h:outputLink value="template/index.jsf" ><f:verbatim>XML Template examples</f:verbatim></h:outputLink>
            </h:panelGrid>

        </h:panelGrid>
    </h:form>
    </f:view>

</body>
</html>
