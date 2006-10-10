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

function orgApacheMyfacesSubmitOnEventRegister(eventType, callbackFunction, inputComponentId, clickComponentId)
{
    // alert("eventType:" + eventType + " callback:" + callbackFunction + " input:" + inputComponentId + " click:" + clickComponentId);
    
    var inputComponent;
    if (inputComponentId != null && inputComponentId != '')
    {
        inputComponent = document.getElementById(inputComponentId);
    }
    else
    {
        inputComponent = document;
    }

    var clickComponent = document.getElementById(clickComponentId);
    if (!clickComponent)
    {
        alert("SubmitOnEvent: can't find button or link '" + clickComponentId + "'");
        return;
    }

    var handler;

    if (callbackFunction != null && callbackFunction != '')
    {
        handler=function(event)
        {
            if (!event)
            {
                event = window.event;
            }

            var callbackRef = eval(callbackFunction);
            if (!callbackRef)
            {
                alert("submitOnEnter: can't find callback Function '" + callbackFunction + "'");
                return true;
            }

            var ret = callbackRef(event, inputComponentId, clickComponentId);
            orgApacheMyfacesSubmitOnEventSetEvent(event, !ret);
            if (ret)
            {
                orgApacheMyfacesSubmitOnEventGeneral(clickComponentId);            
            }
            return !ret;
        };
    }
    else if (eventType == "keypress" || eventType == "keydown" || eventType == "keyup")
    {
        handler=function(event)
        {
            return orgApacheMyfacesSubmitOnEventSetEvent(event, orgApacheMyfacesSubmitOnEventKeypress(event, clickComponentId));
        };
    }
    else
    {
        handler=function(event)
        {
            return orgApacheMyfacesSubmitOnEventSetEvent(event, orgApacheMyfacesSubmitOnEventGeneral(clickComponentId));
        };
    }

    if (inputComponent.addEventListener)
    {
        inputComponent.addEventListener(eventType, handler, false);
    }
    else if (inputComponent.attachEvent)
    {
        inputComponent.attachEvent("on" + eventType, handler);
    }
    else
    {
        alert("SubmitOnEvent: your browser do support event attaching");
    }
}

function orgApacheMyfacesSubmitOnEventSetEvent(event, ret)
{
    if (!ret)
    {
        if (!event)
        {
            event = window.event;
        }

        event.cancelBubble = true;
        if (event.stopPropagation)
        {
            event.stopPropagation();
        }
        if (event.preventDefault)
        {
            event.preventDefault();
        }
    }

    return ret;
}

function orgApacheMyfacesSubmitOnEventGeneral(componentId)
{
    var clickComponent = document.getElementById(componentId);
    if ((clickComponent.nodeName && clickComponent.nodeName.toLowerCase() == "a")
        || (clickComponent.tagName && clickComponent.tagName.toLowerCase() == "a"))
    {
        orgApacheMyfacesSubmitOnEventClickLink(clickComponent);
        return false;
    }
    else if (clickComponent.type && clickComponent.type.toLowerCase() == "submit")
    {
        orgApacheMyfacesSubmitOnEventClickButton(clickComponent);
        return false;
    }
    else
    {
        alert("SubmitOnEvent: don't know how to fire component '" + componentId + "'");
    }

    return true;
}

function orgApacheMyfacesSubmitOnEventKeypress(event, componentId)
{
    var keycode;
    if (window.event)
    {
        keycode = window.event.keyCode;
    }
    else if (event)
    {
        keycode = event.which;
    }
    else
    {
        return true;
    }

    if (keycode == 13)
    {
        orgApacheMyfacesSubmitOnEventGeneral(componentId);
        return false;
    }
    
    return true;
}

function orgApacheMyfacesSubmitOnEventClickLink(fireOnThis)
{
    if (document.createEvent)
    {
        var evObj = document.createEvent('MouseEvents')
        evObj.initEvent('click', true, false)
        fireOnThis.dispatchEvent(evObj)
    }
    else if (fireOnThis.fireEvent)
    {
        fireOnThis.fireEvent('onclick')
    }
    else
    {
        alert("SubmitOnEvent: your browser do not support fireing an event on a link");
    }
}

function orgApacheMyfacesSubmitOnEventClickButton(fireOnThis)
{
    fireOnThis.click();
}
