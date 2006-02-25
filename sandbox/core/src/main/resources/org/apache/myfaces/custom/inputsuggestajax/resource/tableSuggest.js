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

//puting the values from the choosen row into the fields
function putValueToField(trElem)
{
    var j = 0;

    for(;j<trElem.childNodes.length;j++)
    {
        var idToPutValue = trElem.childNodes[j].id.substr(11);
        var elemToPutValue = document.getElementById(idToPutValue);

        if(trElem.childNodes[j].childNodes[1] == null)
            elemToPutValue.value = trElem.childNodes[j].childNodes[0].innerHTML;
        else
        {   //quick fix to put the value in a selectOneMenu; todo: more generic and embedding in dojo 
            for(i=0;i<elemToPutValue.options.length;i++)
            {
                if(elemToPutValue.options[i].value == trElem.childNodes[j].childNodes[1].innerHTML)
                    elemToPutValue.options[i].selected = true;
            }
       }
    }
}

function handleRequestResponse(url, handlerNode, popUp)
{
    dojo.io.bind
    ({
       url:  url+handlerNode.value,
       handle: function(type, data, evt)
               {
                  dojo.debug("after response");
                  //if(data) dojo.debug(data.substr(0,7));

                  if(type == "load")  //&& data?
                  {
                      dojo.debug("response successful");
                      if(dojo.string.startsWithAny(data, "<table>")) popUp.innerHTML = data;
                  }
                  else if(type == "error")
                  {
                      dojo.debug("error during response");
                      //dojo.debug(data);
                      // here, data is our error object
                  }
              },
         mimetype: "text/plain"
    });
}
