
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
dojo.require("dojo.debug");
dojo.require("dojo.html");
var  myfaces_stateChange_globalExclusionList = new Array();



/**
* central function definition for the state change notifier
*/
function org_apache_myfaces_StateChangedNotifier(paramnotifierName, paramformId, paramhiddenFieldId, parammessage, paramexcludeCommandIdList) {
    this.notifierName = paramnotifierName;
    this.formId = paramformId;
    this.hiddenFieldId = paramhiddenFieldId;
    this.message = parammessage;
    this.excludeCommandIdList = paramexcludeCommandIdList;
    this.arrCommandIds = new Array();
    this.objectsToConfirmList = new Array();
    this.objectsToConfirmBeforeExclusion = new Array();
    /*global exclusion list singleton which keeps track of all exclusions over all entire forms.
		we cannot allow that some predefined exclusions
		are rendered invalid by another tag
	*/
}



/**
* prepares the notifier entry fields
* and trigger filters
* for the entire mechanism
*/
org_apache_myfaces_StateChangedNotifier.prototype.prepareNotifier = function () {
    this.addOnChangeListener("input");
    this.addOnChangeListener("textarea");
    this.addOnChangeListener("select");
    var globalExclLen = myfaces_stateChange_globalExclusionList.length;
    for (var cnt = 0; cnt < globalExclLen; cnt += 1) {
        this.arrCommandIds.push(myfaces_stateChange_globalExclusionList[cnt]);
    }
    if (this.excludeCommandIdList !== null) {
        var newIds = this.excludeCommandIdList.split(",");
        
		//we do not filter double entries for now
		//since the number of exclusion tags will be kept small
		//anyway
        for (var cnt = globalExclLen; cnt < (globalExclLen + newIds.length); cnt += 1) {
            myfaces_stateChange_globalExclusionList.push(newIds[cnt - globalExclLen]);
            this.arrCommandIds.push(newIds[cnt - globalExclLen]);
        }
        this.addObjectsToConfirmList("a",null);
        this.addObjectsToConfirmList("input","button");
        this.addObjectsToConfirmList("input","submit");
        this.addObjectsToConfirmList("button",null);
        this.putConfirmExcludingElements();
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.changeHiddenValue = function (evt) {
    var hiddenField = dojo.byId(this.hiddenFieldId);
    hiddenField.value = "true";
};
org_apache_myfaces_StateChangedNotifier.prototype.addOnChangeListener = function (tagName) {
    var arrElements = document.getElementsByTagName(tagName);
    for (var i = 0; i < arrElements.length; i += 1) {
        dojo.event.connect(arrElements[i], "onchange", this, "changeHiddenValue");
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.addObjectsToConfirmList = function (tagName, tagType) {
    var arrElements = document.getElementsByTagName(tagName);
    for (var i = 0; i < arrElements.length; i += 1) {
        var elementId = arrElements[i].id;
        var onclick = arrElements[i].onclick;
     
        if(tagType === null || (tagType !== null && arrElements[i].type === tagType)) {
	        if (elementId !== null && onclick !== null && elementId !== "") {
	            this.objectsToConfirmBeforeExclusion.push(elementId);
	        }
	    }
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.putConfirmExcludingElements = function () {
    for (var cnt = 0; cnt < this.objectsToConfirmBeforeExclusion.length; cnt += 1) {
        var elementId = this.objectsToConfirmBeforeExclusion[cnt];
        if (!this.isElementExcluded(elementId)) {
            this.objectsToConfirmList.push(elementId);
        } else {
        	this.removeConfirmInElement(elementId); //remove old includes from the list if we get one 
        }
    }
    for (var cnt2 = 0; cnt2 < this.objectsToConfirmList.length; cnt2 += 1) {
        var objectToConfirm = this.objectsToConfirmList[cnt2];
        this.putConfirmInElement(objectToConfirm);
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.isElementExcluded = function (elementId) {
    if (this.arrCommandIds === null || (this.arrCommandIds.length == 1 && (this.arrCommandIds[0] === null || this.arrCommandIds[0] === ""))) {
        return false;
    }
    for (var i = 0; i < this.arrCommandIds.length; i += 1) {
        var excludedId = this.arrCommandIds[i];
        var idRegex = null;
        if (elementId.indexOf(":") > -1) {
            idRegex = new RegExp(".*" + excludedId + "([\\d+])?");
        } else {
            idRegex = new RegExp(excludedId + "([\\d+])?");
        }
        if (elementId.match(idRegex) !== null) {
            return true;
        }
    }
};
/**
* builds up a show message function
* dependend on the correct browser
*/
org_apache_myfaces_StateChangedNotifier.prototype.showMessage = function () {
    var hiddenField = dojo.byId(this.hiddenFieldId);
    if (hiddenField.value == "true") {
            //if (!confirm(message)) return false;
        var confirmit = confirm(this.message);
        if(confirmit ) 
        	hiddenField.value == "false";
        return confirmit;
    }
    return true;
};
org_apache_myfaces_StateChangedNotifier.prototype.removeConfirmInElement = function (commandId) {
    var command = dojo.byId(commandId);
	var oldOnClick = command.getAttribute("old_onclick");
	
	if(oldOnClick !== null) {
		command.setAttribute("onclick",oldOnClick);
	}	
}
org_apache_myfaces_StateChangedNotifier.prototype.putConfirmInElement = function (commandId) {
    var command = dojo.byId(commandId);
    if (command !== null) {
        var onclick = command.getAttribute("onclick");
        var onclickstr = onclick + "";
        command.setAttribute("old_onclick", onclickstr);
        if (dojo.render.html.ie) {
            onclickstr = onclickstr.replace(/function anonymous\(\)/, "");
            onclickstr = "if (" + this.notifierName + ".showMessage()) { " + onclickstr + " }";
            command.setAttribute("onclick", new Function("", onclickstr));
            
        } else {
            command.setAttribute("onclick", "if (" + this.notifierName + ".showMessage()) { " + onclick + " }");
        }
    }
};

