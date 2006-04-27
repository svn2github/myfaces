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

    this.inputField = null;

    this.popUp = null;
    this.popUpStyle = null;

    this.firstHighlightedElem = null;
    this.actualHighlightedElem = null;

    this.iframe = null;
    this.requestLocker = false;

    this.lastKeyPressTime = new Date();
    this.scrollingRow = 0;

    //puting the values from the choosen row into the fields
    org_apache_myfaces_TableSuggest.prototype.putValueToField = function(trElem)
    {
        if (trElem)
        {
            this.scrollOverflowDiv(trElem);

            for (j = 0; j < trElem.childNodes.length; j++)
            {
                var tdElem = trElem.childNodes[j];

                for (a = 0; a < tdElem.childNodes.length; a++)
                {
                    var spanElem = tdElem.childNodes[a];

                    if (spanElem.id)
                    {
                        var idToPutValue = spanElem.id.substr(10);
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

    org_apache_myfaces_TableSuggest.prototype.scrollOverflowDiv = function(trElem)
    {
        if(this.popUp.style.overflow == "auto")
        {
            var popUpStyleHeight = this.popUp.style.height;
            var popUpHeight = dojo.style.getOuterHeight(this.popUp);
            var popUpContentHeight = dojo.style.getOuterHeight(this.popUp.childNodes[0]);

            if(popUpStyleHeight != "" && popUpContentHeight > popUpHeight)
            {
                var prevElem = dojo.dom.prevElement(trElem);

                if (prevElem)
                {
                    this.scrollingRow++;

                    if (this.scrollingRow >= 4)
                    {
                        var prevYPos = dojo.html.getScrollTop();
                        window.location.href = "#" + prevElem.id;
                        var currentYPos = dojo.html.getScrollTop();
                        //smoothScroll(prevYPos, currentYPos);
                        this.inputField.focus();
                    }
                }
            }
        }
    };

    org_apache_myfaces_TableSuggest.prototype.smoothYScroll = function(prevYPos,currentYPos)
    {
        var yDiff = prevYPos-currentYPos;

        while(yDiff < 0){
            window.scrollBy(0,-50);
            dojo.lang.setTimeout('org_apache_myfaces_TableSuggest.prototype.smoothYScroll('+prevYPos+','+currentYPos+')',1000);
            yDiff = yDiff+50;
        }
    };

    org_apache_myfaces_TableSuggest.prototype.handleRequestResponse = function(url, tableSuggest, keyCode)
    {
        if(!this.popUpStyle)
            this.popUpStyle = this.popUp.style.cssText;
        else
            this.popUp.style.cssText = this.popUpStyle;

        if(keyCode == 40)  //down key
        {
            if(!this.firstHighlightedElem)
            {
                var firstOptionElem = this.getFirstRowElem(this.popUp);
                this.putValueToField(firstOptionElem);
                this.addHoverClass(firstOptionElem);
                this.firstHighlightedElem = firstOptionElem;
                this.actualHighlightedElem = firstOptionElem;
            }
            else
            {
                var nextElem = dojo.dom.nextElement(this.actualHighlightedElem);

                if(nextElem)
                {
                    if(dojo.dom.getTagName(nextElem) == "tr")
                    {
                        this.putValueToField(nextElem);
                        this.addOutClass(this.actualHighlightedElem);
                        this.addHoverClass(nextElem);
                        this.actualHighlightedElem = nextElem
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
                            this.firstHighlightedElem = this.getFirstRowElem(this.popUp);
                            this.putValueToField(this.firstHighlightedElem);
                            this.addOutClass(this.actualHighlightedElem);
                            this.addHoverClass(this.firstHighlightedElem);
                            this.actualHighlightedElem = this.firstHighlightedElem;
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

            if(prevElem)
            {
                if(dojo.dom.getTagName(prevElem) == "tr")
                {
                    this.putValueToField(prevElem);
                    this.addOutClass(this.actualHighlightedElem);
                    this.addHoverClass(prevElem);
                    this.actualHighlightedElem = prevElem;
                }
            }
            else
            {
                var table = dojo.html.getFirstAncestorByTag(this.actualHighlightedElem,"table");

                if(table)
                {
                    this.previousPage(table);
                    this.addOutClass(this.actualHighlightedElem);
                    this.actualHighlightedElem = this.getLastRowElem(this.popUp);
                    this.putValueToField(this.actualHighlightedElem);
                    this.addHoverClass(this.actualHighlightedElem);
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

                              dojo.dom.removeChildren(tableSuggest.popUp);

                              collection.clear();

                              for(k=0;k<tablePagesArray.length;k++)
                              {
                                  if(k==0)
                                  {
                                      firstPage = tablePagesArray[k];
                                      firstPageField = tablePagesArray[k+1];
                                      dojo.dom.insertAtPosition(firstPage, tableSuggest.popUp, "first");
                                      dojo.dom.insertAtPosition(firstPageField, tableSuggest.popUp, "last");
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

                              tableSuggest.handleIFrame();
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

            dojo.dom.removeChildren(this.popUp);

            dojo.dom.insertAtPosition(nextPage, this.popUp, "first");
            dojo.dom.insertAtPosition(nextPageField, this.popUp, "last");

            this.tablePagesCollection.add(thisPage);
            this.tablePagesCollection.add(thisPageField);

            this.handleIFrame();
        }
    };

    org_apache_myfaces_TableSuggest.prototype.previousPage = function(thisPage)
    {
        var collLength = this.tablePagesCollection.count;

        if (collLength && collLength > 0)
        {
            var prevPageField = this.tablePagesCollection.item(collLength - 1);
            var prevPage = this.tablePagesCollection.item(collLength - 2);

            this.tablePagesCollection.removeAt(collLength - 1);
            this.tablePagesCollection.removeAt(collLength - 2);

            var thisPageField = dojo.dom.nextElement(thisPage);
            this.tablePagesCollection.insert(0, thisPageField);
            this.tablePagesCollection.insert(0, thisPage);

            dojo.dom.removeChildren(this.popUp);
            dojo.dom.insertAtPosition(prevPage, this.popUp, "first");
            dojo.dom.insertAtPosition(prevPageField, this.popUp, "last");

            this.handleIFrame();
        }
    };

    org_apache_myfaces_TableSuggest.prototype.getFirstRowElem = function()
    {
        var table = dojo.dom.firstElement(this.popUp);
        if (table) {
            var tbody = table.childNodes[1];
            var firstRowElem = dojo.dom.firstElement(tbody);
            return firstRowElem;
        } else return null;
    };

    org_apache_myfaces_TableSuggest.prototype.getLastRowElem = function()
    {
        var table = dojo.dom.firstElement(this.popUp);
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

    org_apache_myfaces_TableSuggest.prototype.handleIFrame = function()
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
                var popUpParent = this.popUp.parentNode;
                dojo.dom.insertAtPosition(this.iframe, popUpParent, "first");
            }

            var popUpStyleHeight = this.popUp.style.height;
            var popUpHeight = dojo.style.getOuterHeight(this.popUp);
            var popUpContentHeight = dojo.style.getOuterHeight(this.popUp.childNodes[0]);
            var popUpContentWidth = dojo.style.getOuterWidth(this.popUp.childNodes[0]);

            if(popUpStyleHeight == "" || popUpHeight < popUpContentHeight)
                this.iframe.style.height = popUpHeight;
            else
                this.iframe.style.height = popUpContentHeight;

            if(popUpStyleHeight != "" && popUpContentHeight > popUpHeight)
                this.iframe.style.width = popUpContentWidth+13;
            else
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

    org_apache_myfaces_TableSuggest.prototype.resetSettings = function()
    {
        if(this.popUp)
        {
            dojo.dom.removeChildren(this.popUp);
            this.popUp.style.cssText = "";
        }

        if(this.iframe && this.popUp)
            this.popUp.parentNode.removeChild(this.popUp.parentNode.childNodes[0])

        this.firstHighlightedElem = null;
        this.actualHighlightedElem = null;
        this.iframe = null;
        this.requestLocker = false;
        this.scrollingRow = 0;
    };

    org_apache_myfaces_TableSuggest.prototype.decideRequest = function(clientId, ajaxUrl,
                                                                      millisBetweenKeyUps, startChars, event)
    {
        if( !this.requestLocker && (this.requestBetweenKeyUpEvents(millisBetweenKeyUps) || this.lastKeyUpEvent()) )
        {
            this.requestLocker = true;

            this.inputField = dojo.byId(clientId);
            this.popUp = dojo.byId(clientId+"_auto_complete");
            var inputValue = this.inputField.value;
            var url = ajaxUrl + "&" + clientId + "=" + inputValue;

            if(startChars)
            {
                if(inputValue.length >= startChars)
                    this.handleRequestResponse(url, this, event.keyCode);
            }
            else if(inputValue != "" )
                this.handleRequestResponse(url, this, event.keyCode);

            if(inputValue == "" )
                this.resetSettings();

            this.requestLocker = false;
        }
    };

}