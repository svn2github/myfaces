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

    //puting the values from the choosen row into the fields
    org_apache_myfaces_TableSuggest.prototype.putValueToField = function(trElem)
    {
        var j = 0;

        for(;j<trElem.childNodes.length;j++)
        {
            var tdElem = trElem.childNodes[j];

            for(a=0;a<tdElem.childNodes.length;a++)
            {
                var spanElem = tdElem.childNodes[a];

                if(spanElem.id)
                {
                    var idToPutValue = spanElem.id.substr(11);
                    var elemToPutValue = dojo.byId(idToPutValue);

                    if (elemToPutValue)
                    {
                        if(dojo.dom.getTagName(elemToPutValue) == "input") {
                            elemToPutValue.value = spanElem.innerHTML;
                            if(elemToPutValue.onchange)
                            {
                                elemToPutValue.onchange();
                            }
                        }
                        else if (dojo.dom.getTagName(elemToPutValue) == "select")
                        {
                            for (i = 0; i < elemToPutValue.options.length; i++)
                            {
                                if (elemToPutValue.options[i].value == spanElem.innerHTML)
                                    elemToPutValue.options[i].selected = true;
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
                              var iframe = null;

                              if(dojo.render.html.ie)
                              {
                                  if(!tableSuggest.iframe) {
                                    tableSuggest.iframe = document.createElement("<iframe>");
                                    tableSuggest.iframe.frameborder='0';
                                    tableSuggest.iframe.src='about:blank';
                                    var s = tableSuggest.iframe.style;
                                    s.position = "absolute";
                                    s.display = "inline";
                                    s.zIndex = 2;
                                    var popUpParent = popUp.parentNode;
                                    dojo.dom.insertAtPosition(tableSuggest.iframe, popUpParent, "first");
                                  }
                               }

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

                                      if(firstPage.rows) {
                                        if(firstPage.rows.length == 2) {
                                            var row = firstPage.rows[1];
                                            tableSuggest.putValueToField(row); }
                                      }

                                  }
                                  else
                                  {
                                      collection.add(tablePagesArray[k]);
                                  }
                              }

                               if(dojo.render.html.ie) {
                                  tableSuggest.iframe.style.width = dojo.style.getBorderBoxWidth(popUp);
                                  tableSuggest.iframe.style.height = dojo.style.getBorderBoxHeight(popUp);
                               }

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
    };

    org_apache_myfaces_TableSuggest.prototype.previousPage = function(thisPage)
    {
        var collLength = this.tablePagesCollection.count;
        var prevPageField = this.tablePagesCollection.item(collLength-1);
        var prevPage = this.tablePagesCollection.item(collLength-2);

        this.tablePagesCollection.removeAt(collLength-1);
        this.tablePagesCollection.removeAt(collLength-2);

        var popUp = dojo.dom.getFirstAncestorByTag(thisPage, "div");

        dojo.dom.removeChildren(popUp);

        dojo.dom.insertAtPosition(prevPage, popUp, "first");
        dojo.dom.insertAtPosition(prevPageField, popUp, "last");

        var table = dojo.dom.firstElement(thisPage);
        var thisPageField = dojo.dom.nextElement(table);
        this.tablePagesCollection.insert(0,thisPageField);
        this.tablePagesCollection.insert(0,thisPage);

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
        }else return null;
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

    org_apache_myfaces_TableSuggest.prototype.resetSettings = function(popUpId)
    {
        var popUp = dojo.byId(popUpId);
        dojo.dom.removeChildren(popUp);

        if(this.iframe)
            this.iframe.style.display = "none";

        this.firstHighlightedElem = null;
        this.actualHighlightedElem = null;
        this.iframe = null;
    };

}