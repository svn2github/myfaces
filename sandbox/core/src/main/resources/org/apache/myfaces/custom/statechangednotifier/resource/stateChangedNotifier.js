/**
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
dojo.provide("org.apache.myfaces.StateChangedNotifier");

org.apache.myfaces.StateChangedNotifier = function(notifierName, formId, hiddenFieldId, message, excludeCommandIdList)
{
    this.notifierName = notifierName;
    this.formId = formId;
    this.hiddenFieldId = hiddenFieldId;
    this.message = message;
    this.excludeCommandIdList = excludeCommandIdList;

    var arrCommandIds = null;

    var objectsToConfirmList = new Array();
    var objectsToConfirmBeforeExclusion = new Array();

    this.prepareNotifier = function ()
    {
        var form = document.getElementById(formId);

        addOnChangeListener("input");
        addOnChangeListener("textarea");
        addOnChangeListener("select");

        if (excludeCommandIdList != null)
        {
            arrCommandIds = excludeCommandIdList.split(",");
            addObjectsToConfirmList("a");
            addObjectsToConfirmList("input");
            addObjectsToConfirmList("button");

            putConfirmExcludingElements();
        }
    }

    this.showMessage = function()
    {
        var hiddenField = getHiddenElement();

        if (hiddenField.value == "true")
        {
            //if (!confirm(message)) return false;
            return confirm(message);
        }

        return true;

    }

    function addOnChangeListener(tagName)
    {
    	
        var arrElements = document.getElementsByTagName(tagName);
		
        for (var i=0; i<arrElements.length; i++)
        {
            dojo.event.browser.addListener(arrElements[i], "onchange", changeHiddenValue);
        }
        
    }

    function addObjectsToConfirmList(tagName)
    {
       var arrElements = document.getElementsByTagName(tagName);

        for (var i=0; i<arrElements.length; i++)
        {
            var elementId = arrElements[i].id;
            var onclick = arrElements[i].onclick;

            if (elementId != null && onclick != null && elementId != '')
            {
                 objectsToConfirmBeforeExclusion.push(elementId);
            }
        }
    }

    function putConfirmExcludingElements()
    {
        for (var i=0; i<objectsToConfirmBeforeExclusion.length; i++)
        {
            var elementId = objectsToConfirmBeforeExclusion[i];

            if (!isElementExcluded(elementId))
            {
                objectsToConfirmList.push(elementId);
            }
        }

        for (var i=0; i<objectsToConfirmList.length; i++)
        {
            var objectToConfirm = objectsToConfirmList[i];
            putConfirmInElement(objectToConfirm);
        }
    }

    function changeHiddenValue()
    {
        var hiddenField = getHiddenElement();
        hiddenField.value = "true";
    }

    function isElementExcluded(elementId)
    {
        for (var i=0; i<arrCommandIds.length; i++)
        {
            var excludedId = arrCommandIds[i];
            var idRegex = null;

            if (elementId.indexOf(":") > -1)
            {
                idRegex = new RegExp(".*"+excludedId+"([\\d+])?")
            }
            else
            {
                idRegex = new RegExp(excludedId+"([\\d+])?")
            }

            if (elementId.match(idRegex) != null)
            {
                return true;
            }
        }
    }


    function putConfirmInElement(commandId)
    {
        var command = document.getElementById(commandId);
        if (command != null)
        {
            var onclick = command.getAttribute("onclick");
            var onclickstr = onclick+"";
          	
			
			if(dojo.render.html.ie) { 
				onclickstr = onclickstr.replace(/function anonymous\(\)/,"");  
				onclickstr = "if ("+notifierName+".showMessage()) { "+onclickstr+" }"; 
				alert(onclickstr);     
		        command.setAttribute("onclick", new Function("",onclickstr));
    		} else {
    	        command.setAttribute("onclick", "if ("+notifierName+".showMessage()) { "+onclick+" }");
    		}
        }
    }

    function getHiddenElement()
    {
        return document.getElementById(hiddenFieldId);
    }
}