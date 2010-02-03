<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
        xmlns:f="http://java.sun.com/jsf/core"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:ui="http://java.sun.com/jsf/facelets"
        xmlns:t="http://myfaces.apache.org/tomahawk">
<!--
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
//-->
<body>
 <ui:composition template="/META-INF/templates/template.xhtml">
  <ui:define name="body">
     <h:outputText value="Mouse over the country name, abbreviation and the text
                         'Country ID' to see three different popups."/>
    <t:dataTable id="data"
            styleClass="standardTable"
            headerClass="standardTable_Header"
            rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
            var="country"
            value="#{countryList.countries}"
            preserveDataModel="true">
       <h:column>
        <h:panelGrid columns="3">
            <t:popup id="a"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="oa" value="#{country.name}"/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="#{country.cities}"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup id="b"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="ob" value="#{country.isoCode}"/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Sample Popup Text"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup id="c"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="oc" value="Country ID"/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                            <h:outputText value="#{country.id}"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>
        </h:panelGrid>
      </h:column>
   </t:dataTable>

   <h:outputText value="Mouse over the text below to see the popups."/>

        <h:panelGrid columns="1">
            <t:popup id="x"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="ox" value="This is the first textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 1"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup id="y"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="oy" value="This is the second textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 2"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup styleClass="popup" id="z">
                <h:outputText id="oz" value="This is the third textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 3"/>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>
            <t:popup
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText value="This is some text without an id."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="no id popup text"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

        </h:panelGrid>
  </ui:define>
 </ui:composition>
</body>
</html>
