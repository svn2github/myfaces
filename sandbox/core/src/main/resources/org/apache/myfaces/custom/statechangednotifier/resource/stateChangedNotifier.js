
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
/*global exclusion list singleton which keeps track of all exclusions over all entire forms.
we cannot allow that some predefined exclusions
are rendered invalid by another tag
*/
var org_apache_myfaces_GlobalExclusionList = new Array();
/**
* central function definition for the state change notifier
*/
function org_apache_myfaces_StateChangedNotifier(paramnotifierName, paramformId, paramhiddenFieldId, parammessage, paramexcludeCommandIdList) {
    this.notifierName = paramnotifierName;
    this.formId = paramformId;
    this.hiddenFieldId = paramhiddenFieldId;
    this.message = parammessage;
    this.excludeCommandIdList = paramexcludeCommandIdList;
    this.arrCommandIds = null;
    this.objectsToConfirmList = new Array();
    this.objectsToConfirmBeforeExclusion = new Array();
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
    for (var cnt = 0; cnt < org_apache_myfaces_GlobalExclusionList.lengh; cnt += 1) {
        this.arrCommandIds[cnt] = org_apache_myfaces_GlobalExclusionList[cnt];
    }
    if (this.excludeCommandIdList !== null) {
        var newIds = this.excludeCommandIdList.split(",");
        var globalExclLen = org_apache_myfaces_GlobalExclusionList.length;
		//we do not filter double entries for now
		//since the number of exclusion tags will be kept small
		//anyway
        for (var cnt = globalExclLen; cnt < (globalExclLen + newIds.length); cnt += 1) {
            this.arrCommandIds[cnt] = org_apache_myfaces_GlobalExclusionList.length[cnt] = newIds[cnt - globalExclLen];
        }
        this.addObjectsToConfirmList("a");
        this.addObjectsToConfirmList("input");
        this.addObjectsToConfirmList("button");
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
org_apache_myfaces_StateChangedNotifier.prototype.addObjectsToConfirmList = function (tagName) {
    var arrElements = document.getElementsByTagName(tagName);
    for (var i = 0; i < arrElements.length; i += 1) {
        var elementId = arrElements[i].id;
        var onclick = arrElements[i].onclick;
        if (elementId !== null && onclick !== null && elementId !== "") {
            this.objectsToConfirmBeforeExclusion.push(elementId);
        }
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.putConfirmExcludingElements = function () {
    for (var cnt = 0; cnt < this.objectsToConfirmBeforeExclusion.length; cnt += 1) {
        var elementId = this.objectsToConfirmBeforeExclusion[cnt];
        if (!this.isElementExcluded(elementId)) {
            this.objectsToConfirmList.push(elementId);
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
        return confirm(this.message);
    }
    return false;
};
org_apache_myfaces_StateChangedNotifier.prototype.putConfirmInElement = function (commandId) {
    var command = dojo.byId(commandId);
    if (command !== null) {
        var onclick = command.getAttribute("onclick");
        var onclickstr = onclick + "";
        if (dojo.render.html.ie) {
            onclickstr = onclickstr.replace(/function anonymous\(\)/, "");
            onclickstr = "if (" + this.notifierName + ".showMessage()) { " + onclickstr + " }";
            command.setAttribute("onclick", new Function("", onclickstr));
        } else {
            command.setAttribute("onclick", "if (" + this.notifierName + ".showMessage()) { " + onclick + " }");
        }
    }
};

