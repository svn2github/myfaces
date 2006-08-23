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
//Declare the myfaces package in the JS Context

dojo.provide("org.apache.myfaces");

//Define the Partial Page Rendering Controller Class

org.apache.myfaces.PPRCtrl = function(formId)
{
    this.partialTriggers = new Array;
    this.replaceFormSubmitFunction(formId);
    this.addButtonOnClickHandlers();
}

//Method for JSF Components to register their Partial Triggers

org.apache.myfaces.PPRCtrl.prototype.addPartialTrigger= function(inputElementId, refreshZoneId)
{
    if (this.partialTriggers[inputElementId] === undefined)
    {
        this.partialTriggers[inputElementId] = refreshZoneId;
    }
    else
    {
        this.partialTriggers[inputElementId] =
        this.partialTriggers[inputElementId] +
        "," +
        refreshZoneId;
    }
};

//Callback Method which handles the AJAX Response

org.apache.myfaces.PPRCtrl.prototype.handleCallback = function(type, data, evt)
{
    if(type == "load")
    {
    var componentUpdates = data.getElementsByTagName("component");
    var componentUpdate = null;
    var domElement = null;
        for (var i = 0; i < componentUpdates.length; i++)
        {
            componentUpdate = componentUpdates[i];
            domElement = document.getElementById(componentUpdate.getAttribute("id"));
            domElement.innerHTML = componentUpdate.firstChild.data;
        }
    }
    else
    {
        alert("an Error occured during the ajax-request " + data.message);
    }
}

//This Method checks if an AJAX Call is to be done instead of submitting the form
//as usual. If so it uses dojo.bind to submit the mainform via AJAX

org.apache.myfaces.PPRCtrl.prototype.ajaxSubmitFunction = function(triggerElement)
{
    if(typeof triggerElement != "undefined")
    {
        var triggeredComponents = this.getTriggeredComponents(triggerElement);
        if(triggeredComponents !=null)
        {
            var requestUri = "";
            var formAction = this.form.attributes["action"];
            if(formAction == null)
            {
                requestUri = location.href;
            }
            else
            {
                requestUri = formAction.nodeValue;
            }

            var content=new Array;
            content[triggerElement.name]=triggerElement.value;
            content["org.apache.myfaces.PPRCtrl.triggeredComponents"]=triggeredComponents;
            content["org.apache.myfaces.PPRCtrl.ajaxRequest"]="true";
            dojo.io.bind({
                url		: requestUri,
                method	: "post",
                useCache: false,
                content	: content,
                handle	: this.handleCallback,
                mimetype: "text/xml",
                transport: "XMLHTTPTransport",
                formNode: this.form
            });
            return false;
        }
        else
        {
            this.form.submit_orig();
        }
    }
    else
    {
        this.form.submit_orig();
    }
}

//This Method replaces the mainform Submitfunciton to call AJAX submit

org.apache.myfaces.PPRCtrl.prototype.formSubmitReplacement = function(triggeredElement)
{
    this.myFacesPPRCtrl.ajaxSubmitFunction(triggeredElement);
}

//The submit Function of the mainform is replaced with the AJAX submit method
//This Method is called during the initailisation of a PPR Controller

org.apache.myfaces.PPRCtrl.prototype.replaceFormSubmitFunction = function(formId)
{
    this.form = document.getElementById(formId);
    if(typeof this.form == "undefined" || this.form.tagName.toLowerCase() != "form")
    {
        alert("MyFaces PPR Engine: Form with id:" + formId + " not found!");
        return;
    }
    this.form.submit_orig = this.form.submit;
    this.form.myFacesPPRCtrl = this;
    this.form.submit = this.formSubmitReplacement;
}

//TODO: event connect
//This Method defines joinpoints for all inputs of either type submit or image

org.apache.myfaces.PPRCtrl.prototype.addButtonOnClickHandlers = function()
{
    if (typeof this.form == "undefined" || this.form.tagName.toLowerCase() != "form")
    {
        return;
    }

        formButtons = new Array();
        for (var i = 0; i < this.form.elements.length; i++)
        {
            if (this.form.elements[i].tagName.toLowerCase() == "input" &&
                (this.form.elements[i].type.toLowerCase() == "submit" ||
                 this.form.elements[i].type.toLowerCase() == "image")
                )
            {
            formButtons.push(this.form.elements[i]);
            }
        }

        for (var i = 0; i < formButtons.length; i++)
        {
            var button = formButtons[i];
                button.onclick_orig = button.onclick;
                button.onclick = this.buttonOnClickHandler;
                button.myFacesPPRCtrl=this;
                //dojo.event.connect(button,"onClick",this,"buttonOnClickHandler");
        }

}

//PointCutAdvisor which invokes the AJAX Submit Method of the PPR Controller after custom
//onclick-handlers for submit-buttons and submit-images

org.apache.myfaces.PPRCtrl.prototype.buttonOnClickHandler = function (_event)
{
    if(this.onclick_orig.type != "undefined")
    {
        this.onclick_orig();
    }
    return this.myFacesPPRCtrl.ajaxSubmitFunction(this);
}

//Based on the Component which triggerd the submit this Method returns a comma-seperated
//list of component-ids which are to be updated via an AJAX call

org.apache.myfaces.PPRCtrl.prototype.getTriggeredComponents = function(triggerElement)
{
    if (typeof triggerElement != "undefined")
    {
        if (typeof this.partialTriggers[triggerElement.id] != "undefined")
        {
            return this.partialTriggers[triggerElement.id];
        }
    }
    return null;
};