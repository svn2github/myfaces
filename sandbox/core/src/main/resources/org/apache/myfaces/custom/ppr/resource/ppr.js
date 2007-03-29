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

//Declare the myfaces package in the JS Context

dojo.provide("org.apache.myfaces");

dojo.require("dojo.dom.*");

//Define the Partial Page Rendering Controller Class

org.apache.myfaces.PPRCtrl = function(formId, showDebugMessages, stateUpdate)
{
    this.blockPeriodicalUpdateDuringPost = false;
    this.showDebugMessages = showDebugMessages;
    this.stateUpdate = stateUpdate;
                                
    if(typeof window.myFacesPartialTriggers == "undefined")
	{
    	window.myFacesPartialTriggers = new Array;
    }
    if(typeof window.myFacesPartialTriggerPatterns == "undefined")
	{
    	window.myFacesPartialTriggerPatterns = new Array;
    }
    if(typeof window.myFacesInlineLoadingMessage == "undefined")
	{
    	window.myFacesInlineLoadingMessage = new Array;
    }

    this.replaceFormSubmitFunction(formId);

    this.addButtonOnClickHandlers();
}

//Method for JSF Components to register Regular Expressions for partial update triggering

org.apache.myfaces.PPRCtrl.prototype.addInlineLoadingMessage= function(message, refreshZoneId)
{
        window.myFacesInlineLoadingMessage[refreshZoneId] = message;
};

org.apache.myfaces.PPRCtrl.prototype.addPartialTriggerPattern= function(pattern, refreshZoneId)
{
        window.myFacesPartialTriggerPatterns[refreshZoneId] = pattern;
};

//Method for JSF Components to register their Partial Triggers

org.apache.myfaces.PPRCtrl.prototype.addPartialTrigger= function(inputElementId, refreshZoneId)
{             
    if (window.myFacesPartialTriggers[inputElementId] === undefined)
    {
        window.myFacesPartialTriggers[inputElementId] = refreshZoneId;
    }
    else
    {
        window.myFacesPartialTriggers[inputElementId] =
        window.myFacesPartialTriggers[inputElementId] +
        "," +
        refreshZoneId;
    }
};

// registering a function (called before submit) on each form to block periodical refresh during request-response cycle

org.apache.myfaces.PPRCtrl.prototype.registerOnSubmitInterceptor = function()
{
    var ppr = this;

   for(var i = 0; i < document.forms.length; i++)
    {
        var form = document.forms[i];
        dojo.event.connect(form, "onsubmit", function(evt) {
            ppr.doBlockPeriodicalUpdateDuringPost();
        });
    }
};


// init function of automatically partial page refresh

org.apache.myfaces.PPRCtrl.prototype.startPeriodicalUpdate = function(refreshTimeout, refreshZoneId)
{
    var content = new Array;
    content["org.apache.myfaces.PPRCtrl.triggeredComponents"] = refreshZoneId;
    this.doAjaxSubmit(content, refreshTimeout, refreshZoneId);
};

// blocking periodical update and refreshing viewState

org.apache.myfaces.PPRCtrl.prototype.doBlockPeriodicalUpdateDuringPost = function()
{
    this.blockPeriodicalUpdateDuringPost = true;
};


//Callback Method which handles the AJAX Response

org.apache.myfaces.PPRCtrl.prototype.handleCallback = function(type, data, evt)
{      
    if(type == "load" && !this.blockPeriodicalUpdateDuringPost)
    {
	    var componentUpdates = data.getElementsByTagName("component");
	    var componentUpdate = null;
	    var domElement = null;
		for (var i = 0; i < componentUpdates.length; i++)
		{
			componentUpdate = componentUpdates[i];           
			domElement = dojo.byId(componentUpdate.getAttribute("id"));
			//todo - doesn't work with tables in IE
			domElement.innerHTML = componentUpdate.firstChild.data;
		}
	    //ensure that new buttons in the ParitalUpdate also have onclick-handlers
	    this.formNode.myFacesPPRCtrl.addButtonOnClickHandlers();

        if (this.stateUpdate)
        {
            var stateElem = data.getElementsByTagName("state")[0];
            var stateUpdate = dojo.dom.firstElement(stateElem, 'INPUT');

            if (stateUpdate)
            {
                var stateUpdateId = stateUpdate.getAttribute('id');

                if (stateUpdateId == 'javax.faces.ViewState')
                {
                    var formArray = document.forms;

                    for (var i = 0; i < formArray.length; i++)
                    {
                        var form = formArray[i];
                        var domElement = form.elements['javax.faces.ViewState'];
                        if (domElement)
                            domElement.value = stateUpdate.getAttribute('value');
                    }
                }
                else if (stateUpdateId != 'jsf_tree' && this.showDebugMessages)
                    alert("server didn't return appropriate element for state-update. returned element-id: " +
                          stateUpdate.getAttribute('id') + ", value : " + stateUpdate.getAttribute('value'));
            }
        }
    }
    else if(this.showDebugMessages)
    {
        alert("an Error occured during the ajax-request " + data.message);
    }
}

//This Method checks if an AJAX Call is to be done instead of submitting the form
//as usual. If so it uses dojo.bind to submit the mainform via AJAX

org.apache.myfaces.PPRCtrl.prototype.ajaxSubmitFunction = function(triggerElement)
{
    var formName = this.form.id;

    if(typeof formName == "undefined")
    {
        formName = this.form.name;
    }

    if(typeof triggerElement != "undefined" ||
    	typeof this.form.elements[formName +':'+'_idcl'] != "undefined")
    {
		var triggerId;    
    	var content=new Array;
    	if(typeof triggerElement != "undefined")
    	{
    		triggerId=triggerElement.id;

            if (triggerElement.tagName.toLowerCase() == "input" &&
                (triggerElement.type.toLowerCase() == "submit" ||
                 triggerElement.type.toLowerCase() == "image")
                )
            {
            	content[triggerElement.name]=triggerElement.value;
            }
            else
            {
        		oamSetHiddenInput(formName,formName +':'+'_idcl',triggerElement.id);
        	}
    	}
    	else 
    	{
    		triggerId=this.form.elements[formName +':'+'_idcl'].value;
    	}
    	
        var triggeredComponents = this.getTriggeredComponents(triggerId);
        if(triggeredComponents !=null)
        {
        	this.displayInlineLoadingMessages(triggeredComponents);
            content["org.apache.myfaces.PPRCtrl.triggeredComponents"]=triggeredComponents;
            return this.doAjaxSubmit(content, null, null)
        }
        else
        {
            this.form.submit_orig(triggerElement);
        }
    }
    else
    {
        this.form.submit_orig(triggerElement);
    }
}

org.apache.myfaces.PPRCtrl.prototype.doAjaxSubmit = function(content, refreshTimeout, refreshZoneId)
{   
	var ppr = this;  
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

    if(refreshTimeout && !this.blockPeriodicalUpdateDuringPost)
    {
        window.setTimeout(function() {
            ppr.startPeriodicalUpdate(refreshTimeout, refreshZoneId);
        }, refreshTimeout)
    }

    return false;
};

//This Method replaces the content of the PPRPanelGroups which have
//an inline-loading-message set with the loading message

org.apache.myfaces.PPRCtrl.prototype.displayInlineLoadingMessages = function(components)
{
	if(typeof components != "string")
	{
		return;
	}
	var componentIds = components.split(',');
	var domElement = null;
	for (index in componentIds)
	{
		if(typeof window.myFacesInlineLoadingMessage[componentIds[index]] != "undefined")
		{
			domElement = dojo.byId(componentIds[index]);
			if(domElement != null)
			{
				domElement.innerHTML = window.myFacesInlineLoadingMessage[componentIds[index]];
			}
		}	
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
    this.form = dojo.byId(formId);
    if( (typeof this.form == "undefined" || this.form.tagName.toLowerCase() != "form")
            && this.showDebugMessages)
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
            if(typeof button.onclick_orig == "undefined")
            {
                button.onclick_orig = button.onclick;
                button.onclick = this.buttonOnClickHandler;
                button.myFacesPPRCtrl=this;
            }
            
        }

}

//PointCutAdvisor which invokes the AJAX Submit Method of the PPR Controller after custom
//onclick-handlers for submit-buttons and submit-images

org.apache.myfaces.PPRCtrl.prototype.buttonOnClickHandler = function (_event)
{
    if(this.onclick_orig.type != "undefined")
    {
        if(this.onclick_orig() == false)
		return false;
    }
    return this.myFacesPPRCtrl.ajaxSubmitFunction(this);
}

//Based on the Component which triggerd the submit this Method returns a comma-seperated
//list of component-ids which are to be updated via an AJAX call


org.apache.myfaces.PPRCtrl.prototype.getTriggeredComponents = function(triggerId)
{	
    if (typeof triggerId != "undefined")
    {
    var retval = null;
        if (typeof window.myFacesPartialTriggers[triggerId] != "undefined")
        {
            retval = window.myFacesPartialTriggers[triggerId];
        }
        
		for (refreshZoneId in window.myFacesPartialTriggerPatterns)
		{
			if(this.isMatchingPattern(window.myFacesPartialTriggerPatterns[refreshZoneId],triggerId) &&
				typeof refreshZoneId == "string" )
				if(retval == null || retval == "")
				{
					retval = refreshZoneId;
				}
				else
				{
					retval += "," + refreshZoneId;
				}
		}	
	return retval;        
    }
    return null;
};

org.apache.myfaces.PPRCtrl.prototype.isMatchingPattern = function(pattern,stringToMatch)
{	
	if(typeof pattern != "string")
	{
		return false;
	}
	if(typeof stringToMatch != "string")
	{
		return false;
	}
	var expr =  new RegExp(pattern);
	return expr.test(stringToMatch);
}
