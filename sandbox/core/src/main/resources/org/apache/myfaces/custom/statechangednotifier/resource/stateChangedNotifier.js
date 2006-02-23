
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
/**
* central function definition for the state change notifier
*/
function org_apache_myfaces_StateChangedNotifier(paramnotifierName, paramformId, paramhiddenFieldId, parammessage, paramexcludeCommandIdList) {
    dojo.debug("org.apache.myfaces.StateChangedNotifier:begin");
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
    dojo.debug("this.prepareNotifier:begin");
    dojo.debug(this.excludeCommandIdList);
    var form = dojo.byId(this.formId);
    dojo.debug("this.prepareNotifier:formfound" + form);
    this.addOnChangeListener("input");
    this.addOnChangeListener("textarea");
    this.addOnChangeListener("select");
    if (this.excludeCommandIdList !== null) {
        dojo.debug(this.excludeCommandIdList);
        this.arrCommandIds = this.excludeCommandIdList.split(",");
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
    dojo.debug("addOnChangeListener:" + tagName);
    var arrElements = document.getElementsByTagName(tagName);
    dojo.debug("addOnChangeListener arrElements.length" + arrElements.length);
    for (var i = 0; i < arrElements.length; i += 1) {
        dojo.debug("adding change listener to:" + arrElements[i].id);
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
            dojo.debug("adding confirm listener:" + elementId);
            this.objectsToConfirmList.push(elementId);
        }
    }
    for (var cnt2 = 0; cnt2 < this.objectsToConfirmList.length; cnt2 += 1) {
        var objectToConfirm = this.objectsToConfirmList[cnt2];
        this.putConfirmInElement(objectToConfirm);
    }
};
org_apache_myfaces_StateChangedNotifier.prototype.isElementExcluded = function (elementId) {
    dojo.debug(this.arrCommandIds);
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
        dojo.debug("adding confirm to" + commandId);
        if (dojo.render.html.ie) {
            onclickstr = onclickstr.replace(/function anonymous\(\)/, "");
            onclickstr = "if (" + this.notifierName + ".showMessage()) { " + onclickstr + " }";
            dojo.debug("onclickst" + onclickstr);
            command.setAttribute("onclick", new Function("", onclickstr));
        } else {
            command.setAttribute("onclick", "if (" + this.notifierName + ".showMessage()) { " + onclick + " }");
        }
    }
};

