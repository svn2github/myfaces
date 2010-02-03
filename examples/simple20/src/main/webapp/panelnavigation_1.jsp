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
    <t:div id="subnavigation_outer">
        <t:div id="subnavigation">
            <h:form>
        <t:panelNavigation2 id="nav1" layout="list" itemClass="mypage" activeItemClass="selected" openItemClass="selected" >
            <t:commandNavigation2  value="#{example_messages['panelnav_products']}" action="go_panelnavigation_1" >
                <t:commandNavigation2 action="#{navigationMenu.getAction1}" actionListener="#{navigationMenu.actionListener}">
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="#{example_messages['panelnav_serach1']}" />
                </t:commandNavigation2>
                <t:commandNavigation2 actionListener="#{navigationMenu.actionListener}" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="#{example_messages['panelnav_serach_acc1']}" />
                </t:commandNavigation2>
                <t:commandNavigation2 action="go_panelnavigation_1" actionListener="#{navigationMenu.actionListener}" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="#{example_messages['panelnav_search_adv1']}" />
                </t:commandNavigation2>
            </t:commandNavigation2>
            <t:commandNavigation2 value="#{example_messages['panelnav_shop']}" action="go_panelnavigation_1" actionListener="#{navigationMenu.actionListener}" />
            <t:commandNavigation2 value="#{example_messages['panelnav_corporate']}" action="go_panelnavigation_1" actionListener="#{navigationMenu.actionListener}" >
                <t:commandNavigation2 action="go_panelnavigation_1" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="#{example_messages['panelnav_news1']}" />
                </t:commandNavigation2>
                <t:commandNavigation2 action="go_panelnavigation_1" actionListener="#{navigationMenu.actionListener}" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="#{example_messages['panelnav_investor1']}" />
                </t:commandNavigation2>
            </t:commandNavigation2>
            <t:commandNavigation2 value="#{example_messages['panelnav_contact']}" action="go_panelnavigation_1" actionListener="#{navigationMenu.actionListener}" />
        </t:panelNavigation2>
            <f:verbatim><br/></f:verbatim>
        <t:panelNavigation2 id="nav2" layout="list" styleClass="mypage" >
            <t:commandNavigation2 value="MyAccount" action="go_panelnavigation_1" >
                <t:commandNavigation2 action="go_panelnavigation_1" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="Login" />
                </t:commandNavigation2>
                <t:commandNavigation2 action="go_panelnavigation_1" >
                    <f:verbatim>&#8250; </f:verbatim>
                    <t:outputText value="Register" />
                </t:commandNavigation2>
            </t:commandNavigation2>
        </t:panelNavigation2>
            </h:form>
        </t:div>
    </t:div>
  </ui:define>
 </ui:composition>
</body>
</html>
