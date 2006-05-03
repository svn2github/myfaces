<%@ page session="false"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="x" uri="http://myfaces.apache.org/tomahawk"%>
<%@ taglib prefix="s" uri="http://myfaces.apache.org/sandbox"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">


	<head>
		<!--
		/*
		 * Copyright 2006 The Apache Software Foundation.
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

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Pagelet</title>
	</head>

	<body>
		<f:view>
			<h:form acceptCharset="iso-8859-1" enctype="iso-8859-1">
				<s:stateChangedNotifier confirmationMessage="Values have changed. Did you know it?"
                                disabled="false"
                                excludedIds="excludedLink1,excludedLink2"/>
				<h:commandLink action="doneit" value="pressme"></h:commandLink>
				
				
				<x:div styleClass="myfaces_SpellingError">testtest</x:div>
				<h:inputText id="theinut"></h:inputText>
				<s:pagelet required="true" id="ajax_spellchecker0" value="#{pageletBean.text}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100" controlMode="textarea">
					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:graphicImage url="spellcheck.png" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:graphicImage url="resume.png" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>
				</s:pagelet>
				<h:message for="ajax_spellchecker0"></h:message>
				<f:verbatim><br/>xxxx</f:verbatim>
				<s:pagelet id="ajax_spellchecker1" value="#{pageletBean.text2}" spellchecker="#{dummySpellchecker.checkSpelling}" width="200" height="30">
					<f:facet name="linkZoom">
						<h:panelGroup>
							<h:outputText value="Z" styleClass="check_spelling" rendered="true" />
							<h:graphicImage url="spellcheck.png" rendered="false" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>

					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:outputText value="Check Spelling" styleClass="check_spelling" rendered="false" />
							<h:graphicImage url="spellcheck.png" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:outputText value="Resume Editing" styleClass="resume_editing" rendered="false" />
							<h:graphicImage url="resume.png" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResize">
						<h:panelGroup>
							<h:outputText value="+ /" styleClass="resume_editing" rendered="false" />
							<h:graphicImage url="resize.png" rendered="true" alt="Bigger" />
						</h:panelGroup>
					</f:facet>

					<f:facet name="linkDownsize">
						<h:panelGroup>
							<h:outputText value="-" styleClass="resume_editing" rendered="false" />

							<h:graphicImage url="downsize.png" rendered="true" alt="Smaller" />
						</h:panelGroup>
					</f:facet>
				</s:pagelet>
				<f:verbatim><br/>yyy</f:verbatim>
				
				<h:inputTextarea style="width: 300px; height: 30px;"></h:inputTextarea>
				<f:verbatim><br/></f:verbatim>
				<s:pagelet required="false" id="ajax_spellchecker5" value="#{pageletBean.text5}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100">
					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:graphicImage url="spellcheck.png" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:graphicImage url="resume.png" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>
				</s:pagelet>
				<h:message for="ajax_spellchecker2"></h:message>

				<s:pagelet required="false" disabled="true" id="ajax_spellchecker3" value="#{pageletBean.text3}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100">
					<f:facet name="linkZoom">
						<h:panelGroup>
							<h:outputText value="Z" styleClass="check_spelling" rendered="true" />
							<h:graphicImage url="spellcheck.png" rendered="false" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>

					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:graphicImage url="spellcheck.png" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:graphicImage url="resume.png" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>
				</s:pagelet>
				<s:pagelet required="false" readonly="true" zoomWidth="600" zoomHeight="200" id="ajax_spellchecker4" value="#{pageletBean.text4}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100">
					<f:facet name="linkZoom">
						<h:panelGroup>
							<h:outputText value="Z" styleClass="check_spelling" rendered="true" />
							<h:graphicImage url="spellcheck.png" rendered="false" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>

					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:graphicImage url="spellcheck.png" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:graphicImage url="resume.png" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>
				</s:pagelet>
				
				<s:pagelet required="false" readonly="false" zoomWidth="600" zoomHeight="200" id="ajax_spellchecker7" value="#{pageletBean.text5}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100" controlMode="pagelet" binding="#{componentBean.spellChecker}">
				</s:pagelet>

			
				<s:pagelet  required="false" readonly="false" zoomWidth="600" zoomHeight="200" id="ajax_spellchecker8" value="#{pageletBean.text7}" spellchecker="#{dummySpellchecker.checkSpelling}" width="400" height="100"  controlMode="pagelet-rawtext" >
					<f:facet name="linkZoom">
						<h:panelGroup>
							<h:outputText value="Z" styleClass="check_spelling" rendered="true" />
							<h:graphicImage url="spellcheck.png" rendered="false" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkSpellchecker">
						<h:panelGroup>
							<h:graphicImage url="SPELLCHECK_0.gif" rendered="true" alt="Check Spelling" />
						</h:panelGroup>
					</f:facet>
					<f:facet name="linkResume">
						<h:panelGroup>
							<h:graphicImage url="CLEAR_0.gif" rendered="true" alt="Resume Editing" />
						</h:panelGroup>
					</f:facet>

					<f:facet name="popupLabel">
						<h:outputText value="Does this field have any value?"  rendered="true" />
					</f:facet>
				</s:pagelet>


				<h:commandLink value="submit" action="#{pageletBean.doaction}" />
			</h:form>
		</f:view>
	</body>
</html>
