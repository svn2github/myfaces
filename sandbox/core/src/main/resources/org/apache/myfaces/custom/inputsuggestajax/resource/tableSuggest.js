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


org_apache_myfaces_TableSuggest = function()
{
    this.tablePagesCollection = new dojo.collections.ArrayList();

    this.firstHighlightedElem = null;
    this.actualHighlightedElem = null;

    this.iframe = null;
    this.requestLocker = false;

    this.lastKeyPressTime = new Date();

    //puting the values from the choosen row into the fields
    org_apache_myfaces_TableSuggest.prototype.putValueToField = function(trElem)
    {
        if (trElem)
        {
           /* var currentYPos = dojo.html.getScrollTop();
            var currentXPos = dojo.html.getScrollLeft();
            //window.location.href = dojo.dom.nextElement(trElem).name;
            var nextElem = dojo.dom.nextElement(trElem);
            nextElem.childNodes[0].onclick();
            window.scrollTo(currentXPos,currentYPos);
            var inputField = dojo.byId("_idJsp3:cityField2");
            inputField.focus();*/

            for (j = 0; j < trElem.childNodes.length; j++)
            {
                var tdElem = trElem.childNodes[j];

                for (a = 0; a < tdElem.childNodes.length; a++)
                {
                    var spanElem = tdElem.childNodes[a];

                    if (spanElem.id)
                    {
                        var idToPutValue = spanElem.id.substr(11);
                        var elemToPutValue = dojo.byId(idToPutValue);

                        if (elemToPutValue)
                        {
                            if (dojo.dom.getTagName(elemToPutValue) == "input")
                            {
                                elemToPutValue.value = spanElem.innerHTML;
                                if (elemToPutValue.onchange)
                                {
                                    elemToPutValue.onchange();
                                }
                            }
                            else if (dojo.dom.getTagName(elemToPutValue) == "select")
                            {
                                for (i = 0; i < elemToPutValue.options.length; i++)
                                {
                                    var optionValue = spanElem.innerHTML;

                                    if (elemToPutValue.options[i].value == optionValue)
                                        elemToPutValue.options[i].selected = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    org_apache_myfaces_TableSuggest.prototype.handleRequestResponse = function(url, popUp, tableSuggest, keyCode)
    {
        if(keyCode == 40)  //down key
        {
            tableSuggest.requestLocker = false;

            if(!this.firstHighlightedElem)
            {
                var firstOptionElem = this.getFirstRowElem(popUp);
                this.addHoverClass(firstOptionElem);
                this.firstHighlightedElem = firstOptionElem;
                this.actualHighlightedElem = firstOptionElem;

                this.putValueToField(firstOptionElem);
            }
            else
            {
                var nextElem = dojo.dom.nextElement(this.actualHighlightedElem);

                if(nextElem)
                {
                    if(dojo.dom.getTagName(nextElem) == "tr")
                    {
                        this.addOutClass(this.actualHighlightedElem);
                        this.addHoverClass(nextElem);
                        this.actualHighlightedElem = nextElem

                        this.putValueToField(nextElem);
                    }
                }
                else
                {
                    var table = dojo.html.getFirstAncestorByTag(this.actualHighlightedElem,"table");
                    var pageField = dojo.dom.nextElement(table);

                    if(pageField)
                    {
                        if(dojo.dom.getTagName(pageField) == "div")
                        {
                            this.nextPage(pageField);
                            this.firstHighlightedElem = this.getFirstRowElem(popUp);
                            this.addOutClass(this.actualHighlightedElem);
                            this.addHoverClass(this.firstHighlightedElem);
                            this.actualHighlightedElem = this.firstHighlightedElem;

                            this.putValueToField(this.firstHighlightedElem);
                        }
                    }
                    else
                        dojo.debug("could not move to next item in table, wrong item is");dojo.debug(nextElem);
                }
            }
        }
        else if(keyCode == 38)  //up key
        {
            var prevElem = dojo.dom.prevElement(this.actualHighlightedElem);
            tableSuggest.requestLocker = false;

            if(prevElem)
            {
                if(dojo.dom.getTagName(prevElem) == "tr")
                {
                    this.addOutClass(this.actualHighlightedElem);
                    this.addHoverClass(prevElem);
                    this.actualHighlightedElem = prevElem;

                    this.putValueToField(prevElem);
                }
            }
            else
            {
                var table = dojo.html.getFirstAncestorByTag(this.actualHighlightedElem,"table");

                if(table)
                {
                    this.previousPage(table);
                    this.addOutClass(this.actualHighlightedElem);
                    this.actualHighlightedElem = this.getLastRowElem(popUp);
                    this.addHoverClass(this.actualHighlightedElem);

                    this.putValueToField(this.actualHighlightedElem);
                }
                else
                    dojo.debug("could not move to next item in table, wrong item is");dojo.debug(nextElem);
            }
        }
        else
        {
            this.firstHighlightedElem = null;
            this.actualHighlightedElem = null;

            dojo.io.bind
            ({
               url:  url,
               handle: function(type, data, evt)
                       {
                            dojo.debug("after response");
                            //if(data) dojo.debug(data);

                            if(type == "load" && data)
                            {
                              dojo.debug("response successful");
                              var tablePagesArray = dojo.html.createNodesFromText(data);
                              var collection = tableSuggest.tablePagesCollection;

                              var firstPage = null;
                              var firstPageField = null;

                              dojo.dom.removeChildren(popUp);

                              collection.clear();

                              for(k=0;k<tablePagesArray.length;k++)
                              {
                                  if(k==0)
                                  {
                                      firstPage = tablePagesArray[k];
                                      firstPageField = tablePagesArray[k+1];
                                      dojo.dom.insertAtPosition(firstPage, popUp, "first");
                                      dojo.dom.insertAtPosition(firstPageField, popUp, "last");
                                      k++;

                                      if(firstPage.rows && firstPage.rows.length == 2) {
                                         var row = firstPage.rows[1];
                                         tableSuggest.putValueToField(row);
                                      }
                                  }
                                  else
                                  {
                                      collection.add(tablePagesArray[k]);
                                  }
                              }

                              tableSuggest.handleIFrame(popUp);

                              tableSuggest.requestLocker = false;
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
    };

    org_apache_myfaces_TableSuggest.prototype.nextPage = function(thisPageField)
    {
        var collLength = this.tablePagesCollection.count;

        if (collLength && collLength > 0)
        {
            var nextPage = this.tablePagesCollection.item(0);
            var nextPageField = this.tablePagesCollection.item(1);

            this.tablePagesCollection.removeAt(0);
            this.tablePagesCollection.removeAt(0);

            var thisPage = dojo.dom.prevElement(thisPageField);

            var popUp = dojo.dom.getFirstAncestorByTag(thisPage, "div");

            dojo.dom.removeChildren(popUp);

            dojo.dom.insertAtPosition(nextPage, popUp, "first");
            dojo.dom.insertAtPosition(nextPageField, popUp, "last");

            this.tablePagesCollection.add(thisPage);
            this.tablePagesCollection.add(thisPageField);
        }
    };

    org_apache_myfaces_TableSuggest.prototype.previousPage = function(thisPage)
    {
        var collLength = this.tablePagesCollection.count;

        if (collLength && collLength > 0)
        {
            var prevPageField = this.tablePagesCollection.item(collLength - 1);
            var prevPage = this.tablePagesCollection.item(collLength - 2);
            var popUp = dojo.dom.getFirstAncestorByTag(thisPage, "div");

            this.tablePagesCollection.removeAt(collLength - 1);
            this.tablePagesCollection.removeAt(collLength - 2);

            var thisPageField = dojo.dom.nextElement(thisPage);
            this.tablePagesCollection.insert(0, thisPageField);
            this.tablePagesCollection.insert(0, thisPage);

            dojo.dom.removeChildren(popUp);
            dojo.dom.insertAtPosition(prevPage, popUp, "first");
            dojo.dom.insertAtPosition(prevPageField, popUp, "last");
        }
    };

    org_apache_myfaces_TableSuggest.prototype.getFirstRowElem = function(popUp)
    {
        var table = dojo.dom.firstElement(popUp);
        if (table) {
            var tbody = table.childNodes[1];
            var firstRowElem = dojo.dom.firstElement(tbody);
            return firstRowElem;
        } else return null;
    };

    org_apache_myfaces_TableSuggest.prototype.getLastRowElem = function(popUp)
    {
        var table = dojo.dom.firstElement(popUp);
        if(table) {
            var tbody = table.childNodes[1];
            var lastRowElem = dojo.dom.lastElement(tbody);
            return lastRowElem;
        } else return null;
    };

    org_apache_myfaces_TableSuggest.prototype.addHoverClass = function(elem)
    {
        dojo.html.removeClass(elem,"tableSuggestOut");
        dojo.html.addClass(elem,"tableSuggestHover");
    };

    org_apache_myfaces_TableSuggest.prototype.addOutClass = function(elem)
    {
        dojo.html.removeClass(elem,"tableSuggestHover");
        dojo.html.addClass(elem,"tableSuggestOut");
    };

    org_apache_myfaces_TableSuggest.prototype.requestBetweenKeyUpEvents = function(millisBetweenKeyPress)
    {
        var currentTime = new Date();

        if( (currentTime - this.lastKeyPressTime) > millisBetweenKeyPress)
        {
            dojo.debug(currentTime - this.lastKeyPressTime);
            this.lastKeyPressTime = currentTime;
            return true;
        }
        else
        {
            dojo.debug(currentTime - this.lastKeyPressTime);
            this.lastKeyPressTime = currentTime;
            return false;
        }
    };

    org_apache_myfaces_TableSuggest.prototype.handleIFrame = function(popUp)
    {
        if (dojo.render.html.ie)
        {
            if (!this.iframe)
            {
                this.iframe = document.createElement("<iframe>");
                this.iframe.frameborder = '0';
                this.iframe.src = 'about:blank';
                var s = this.iframe.style;
                s.position = "absolute";
                s.zIndex = 2;
                var popUpParent = popUp.parentNode;
                dojo.dom.insertAtPosition(this.iframe, popUpParent, "first");
            }

            var popUpStyleHeight = popUp.style.height;
            var popUpHeight = dojo.style.getOuterHeight(popUp);
            var popUpContentHeight = dojo.style.getOuterHeight(popUp.childNodes[0]);
            var popUpContentWidth = dojo.style.getOuterWidth(popUp.childNodes[0]);

            if(popUpStyleHeight == "" || popUpHeight < popUpContentHeight)
                this.iframe.style.height = popUpHeight;
            else
                this.iframe.style.height = popUpContentHeight;

            this.iframe.style.width = popUpContentWidth;
        }
    };

    org_apache_myfaces_TableSuggest.prototype.lastKeyUpEvent = function()
    {
        //dojo.lang.setTimeout('',4000);
        var currentTime = new Date();
        dojo.debug("last keyUpEvent?");
        dojo.debug(currentTime - this.lastKeyPressTime);
        if( (currentTime - this.lastKeyPressTime) > 250)
        {
            dojo.debug("was last keyUpEvent");
            return true;
        }
        else return false;
    };

    org_apache_myfaces_TableSuggest.prototype.resetSettings = function(popUpId)
    {
        var popUp = dojo.byId(popUpId);
        dojo.dom.removeChildren(popUp);

        if(this.iframe && popUp)
            popUp.parentNode.removeChild(popUp.parentNode.childNodes[0])

        this.firstHighlightedElem = null;
        this.actualHighlightedElem = null;
        this.iframe = null;
        this.requestLocker = false;
    };

}