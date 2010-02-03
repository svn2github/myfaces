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
    <h:form>

        <h:outputText value ="List" />

        <h:panelGrid columns="1">
          <h:selectOneMenu id="menu1" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carList}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneMenu>

          <h:selectOneRadio id="radio1" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carList}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneRadio>

          <h:selectOneListbox id="list1" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carList}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneListbox>
        </h:panelGrid>

        <h:outputText value ="Map" />

        <h:panelGrid columns="1">
          <h:selectOneMenu id="menu2" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carMap}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneMenu>

          <h:selectOneRadio id="radio2" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carMap}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneRadio>

          <h:selectOneListbox id="list2" value="#{selectItemsBean.selectedCarColor}">
              <t:selectItems value="#{selectItemsBean.carMap}" var="Car" itemLabel="#{Car.type}" itemValue="#{Car.color}" />
          </h:selectOneListbox>
        </h:panelGrid>

        <h:commandButton value="GO!" action="doSomething"/>
    </h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
