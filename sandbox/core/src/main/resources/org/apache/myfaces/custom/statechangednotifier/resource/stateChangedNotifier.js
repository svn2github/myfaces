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

org.apache.myfaces.StateChangedNotifier = function(formId, hiddenFieldId, message, excludeCommandIdList)
{

    this.formId = formId;
    this.hiddenFieldId = hiddenFieldId;
    this.message = message;
    this.excludeCommandIdList = excludeCommandIdList;

    var arrCommandIds = null;
    var clickedCommand = null;

    this.prepareNotifier = function ()
    {
        var form = document.getElementById(formId);

        if (excludeCommandIdList != null)
        {
            arrCommandIds = excludeCommandIdList.split(",");
        }

        var parser = new dojo.xml.Parse();
        elementsInForm = parser.parseElement(form);
        traverseTree(elementsInForm, null);

        form.onsubmit = confirmSubmit;
    }

    function traverseTree(x, type)
    {
        for (var y in x)
        {
            if (typeof x[y] == "object" && x[y] != x.nodeRef)
            {
                traverseTree(x[y], y);
            }

            else
            {
                if (type == "input" || type == "textarea" || type == "select")
                {
                    processComponent(x[y]);
                }

                if (arrCommandIds != null)
                {
                    var elementId = x[y].id;
                    var onclick = x[y].onclick;

                    if (elementId != null && onclick != null && elementId != '')
                    {
                         checkExclusion(elementId);
                    }
                }
            }
        }
    }

    function processComponent(inputComponent)
    {
        dojo.event.browser.addListener(inputComponent, "onchange", changeHiddenValue);
    }

    function changeHiddenValue()
    {
        var hiddenField = getHiddenElement();
        hiddenField.value = "true";
    }

    function confirmSubmit()
    {
        var hiddenField = getHiddenElement();
        if (hiddenField.value == "true")
        {
            if (!confirm(message)) return false;
        }
    }

    function checkExclusion(elementId)
    {
        for (i=0; i<arrCommandIds.length; i++)
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

            if (elementId.match(idRegex))
            {
                excludeCommandWithId(elementId);
            }
        }
    }


    function excludeCommandWithId(commandId)
    {
        var command = document.getElementById(commandId);

        if (command != null)
        {
            clickedCommand = command;
            command.onclick = submitWithoutConfirm;
        }
    }

    function submitWithoutConfirm()
    {
        var form = document.getElementById(formId);

        document.forms[formId].elements[formId+':_link_hidden_'].value= clickedCommand.id;

        var clearFunction = "clear_"+formId;

        form.onsubmit = clearFunction;
        form.submit();
        return false;
    }

    function getHiddenElement()
    {
        return document.getElementById(hiddenFieldId);
    }
}