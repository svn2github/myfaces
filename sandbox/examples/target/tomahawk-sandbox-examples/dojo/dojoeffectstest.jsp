<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<html>
	<head>
		<meta HTTP-EQUIV="Content-Type" CONTENT="text/html;charset=UTF-8" />
		<title>MyFaces - the free JSF Implementation</title>

		<link rel="stylesheet" type="text/css" href="css/basic.css" />
		<style>
		body {
			font-family: Arial, Helvetica, sans-serif;
			padding: 0;
			margin: 0;
		}


		#fade {
			position : absolute;
			left : 200px;
			top : 100px;
			background : #ddd;
			width : 400px;
			height : 300px;
			text-align : center;
						
		}
  </STYLE>
	</head>

	<!--
/*
 * Copyright 2004 The Apache Software Foundation.
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



	<body>
		<f:view>
			<h:panelGroup>
				<f:verbatim>
				</f:verbatim>




				<s:dojoInitializer require="dojo.lfx.html.*"/>	
								
				<h:outputText value="prototype for the aminations" />
				

				<f:verbatim>
				<div id="fade" style="" >
				   		fade field
				   </div>
				   <script type="text/javascript">
				   		myfaces_fader = function(theid, thetime) {
				   		   fadeid = theid;
				   		   time = thetime;
				   		};
				   		myfaces_fader.prototype.hide  =  function() {
				   			dojo.style.setOpacity( dojo.byId(fadeid),0);
						};
						myfaces_fader.prototype.fadeIn = function() {
				   			dojo.lfx.html.fadeIn( dojo.byId(fadeid), time).play();
				   	    };  
				   	    myfaces_fader.prototype.show  =  function() {
				   			dojo.style.setOpacity( dojo.byId(fadeid),100);
						}; 
						myfaces_fader.prototype.fadeOut = function() {
				   			dojo.lfx.html.fadeOut( dojo.byId(fadeid), time).play();
				   	    };  
				   		
				   		var fader = new myfaces_fader('fade',300);
				   		fader.hide();	
				   </script>
				   
				   <a href="javascript:;" onclick="fader.fadeIn()">fade</a>
				   <a href="javascript:;" onclick="fader.fadeOut()">fadeOut</a>
					
				</f:verbatim>
			</h:panelGroup>
		</f:view>

		<%@include file="../inc/page_footer.jsp"%>

	</body>

</html>

