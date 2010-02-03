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
            <h1>MyFaces Components and Features
            </h1><h3>What is it, that makes MyFaces different?
            </h3>

            <br/><hr/><b>Tiles support in MyFaces</b>
            <br/>You can easily use <a class='wiki' target="_blank" href='http://struts.apache.org'>struts/tiles</a> in JSF projects by using myfaces-tiles extension.

            <br/><a title="no description" href='tiki-index.php?page=TilesSupport' class='wiki'>learn more...</a>
            <br/><hr/><b>JSCook Menu component</b> '' - Javascript navigation menu&quot;
            <br/>This component renders a Javascript menu based on the excellent JSCookMenu by Heng Yuan
            <br/><a title="no description" href='tiki-index.php?page=JSCookMenu' class='wiki'>learn more...</a>
            <br/><hr/><b>Data Scroller component</b> <i> - scroll through UIData</i>
            <br/>This component renders a scroller to run over the pages of an UIData (eg. &lt;h:dataTable&gt;).

            <br/><a title="no description" href='tiki-index.php?page=DataScroller' class='wiki'>learn more...</a>
            <br/><hr/><b>Navigation component</b> <i> - convenient navigation menu</i>
            <br/>This component renders a hierarchical (vertical) navigation menu and remembers it's open/close/active state over different pages.
            <br/><a title="no description" href='tiki-index.php?page=PanelNavigation' class='wiki'>learn more...</a>
            <br/><hr/><b>Calendar component</b> <i>with localisation support</i>
            <br/>A (not yet ;) sophisticated calendar component with localisation support.
            <br/><a title="no description" href='tiki-index.php?page=InputCalendar' class='wiki'>learn more...</a>

            <br/><hr/><b>File Upload component</b>
            <br/>Convenient component for uploading files.
            <br/><a title="no description" href='tiki-index.php?page=InputFileUpload' class='wiki'>learn more...</a>
            <br/><hr/><b>UISaveState component</b> <i> - saving model state in the client response</i>
            <br/>Traditional JSP/Servlet applications save their state information within HttpSession objects. This is an easy to use but not always satisfying approach. <a title="no description" href='tiki-index.php?page=UISaveState' class='wiki'>learn more...</a>
            <br/><hr/><b>SortHeader component</b> <i>- clickable list column header with sort direction arrow</i>

            <br/>Convenient support for writing lists, that can be (re)sorted by a click on a column header.
            <br/><a title="no description" href='tiki-index.php?page=SortHeader' class='wiki'>learn more...</a>
            <br/><hr/><b>Extended DataTable component</b> <i>- list sort support and preserve DataModel option</i>
            <br/>Extends the standard &lt;h:dataTable&gt; by support for sorting (see SortHeader) and an option to save the DataModel state.
            <br/>To avoid unwanted sideeffects when having a DataModel backed by a database result, state of such a DataModel sometimes must be saved.
            <br/><a title="no description" href='tiki-index.php?page=ExtDataTable' class='wiki'>learn more...</a>
            <br/><hr/><b>Layout component</b> <i>- dynamic page layout control</i>

            <br/>Support for switchable page layout.
            <br/>Try the example and change the layout under &quot;Options&quot; to see the magic!
            <br/><hr/><b>TabbedPane component</b>
            <br/>A swing like tabbed pane renderer.
            <br/>(see web\example\tabbedPane.jsp)
            <br/><hr/><b>DataList component</b>
            <br/>A UIData based list that can be used do simple iteration over a group of components or to render a HTML &lt;ul&gt; or &lt;ol&gt; list.

            <br/><hr/><b>RssTicker component</b>
            <br/>Displays the value of rss-files.
            <br/><a title="no description" href='tiki-index.php?page=RssTicker' class='wiki'>learn more...</a>
            <br/><hr/><b>EmailValidator</b>
            <br/>Component for validating syntax of emails.
            <br/><a title="no description" href='tiki-index.php?page=EmailValidator' class='wiki'>learn more...</a>
            <br/><hr/><b>CreditCardValidator</b>
            <br/>Component for validating credit card numbers.
            <br/><a title="no description" href='tiki-index.php?page=CreditCardValidator' class='wiki'>learn more...</a>
            <br/><hr/><b>RegExprValidator</b>

            <br/>Component for validating regular expressions.
            <br/><a title="no description" href='tiki-index.php?page=RegExprValidator' class='wiki'>learn more...</a>
            <br/><hr/><b>EqualValidator</b>
            <br/>Component for validating value from component a against value from component b.
            <br/><a title="no description" href='tiki-index.php?page=EqualValidator' class='wiki'>learn more...</a>

  </ui:define>
 </ui:composition>
</body>
</html>