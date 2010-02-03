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
 <ui:composition template="/META-INF/templates/pageLayout.xhtml">
  <ui:define name="body">
      <t:saveState id="ss1" value="#{optionsForm.language}"/>
                <h:messages id="messageList"/>

                    <h4>Options</h4>
                    <table border="1"><tr><td>
                
                <h:form id="form1">
                    <h:outputText value="#{example_messages['option_lang']}"/>
                    <f:verbatim>:&nbsp;</f:verbatim>
                    <h:selectOneMenu id="locale" value="#{optionsForm.language}">
                        <f:selectItems id="available" value="#{optionsForm.availableLanguages}"/>
                    </h:selectOneMenu>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="#{example_messages['option_layout']}"/>
                    <f:verbatim>:&nbsp;</f:verbatim>
                    <h:selectOneMenu id="layout" value="#{globalOptions.pageLayout}">
                        <f:selectItem id="item101" itemLabel="Classic" itemValue="classic"/>
                        <f:selectItem id="item102" itemLabel="Navigation right" itemValue="navigationRight"/>
                        <f:selectItem id="item103" itemLabel="Upside down" itemValue="upsideDown"/>
                    </h:selectOneMenu>
                    <br/>
                    <h:commandButton id="apply" value="#{example_messages['button_apply']}"
                                     action="#{optionsCtrl.changeLocale}"/>
                </h:form>


                    </td></tr></table>
  </ui:define>
 </ui:composition>
</body>
</html>