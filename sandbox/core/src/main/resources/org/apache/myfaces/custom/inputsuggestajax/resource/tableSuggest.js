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
org_apache_myfaces_TableSuggest = function()
{
    this.tablePagesCollection = new dojo.collections.ArrayList();
    this.firstHighlightedElem = null;
    this.actualHighlightedElem = null;

    org_apache_myfaces_TableSuggest.prototype.putValueToField = function(trElem)
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
    };

    org_apache_myfaces_TableSuggest.prototype.handleRequestResponse = function(url, popUp, tableSuggest, keyCode)
    {
        if(keyCode == 40)  //down key
        {
            if(!this.firstHighlightedElem)
            {
                var firstOptionElem = this.getFirstRowElem(popUp);
                dojo.html.removeClass(firstOptionElem,"tableSuggestOut");
                dojo.html.addClass(firstOptionElem,"tableSuggestHover");
                this.firstHighlightedElem = firstOptionElem;
                this.actualHighlightedElem = firstOptionElem;
                this.actualHighlightedElem.focus();

                this.putValueToField(firstOptionElem);
            }
            else
            {
                var nextElem = dojo.dom.nextElement(this.actualHighlightedElem);

                if(nextElem)
                {
                    if(dojo.dom.getTagName(nextElem) == "tr")
                    {
                        dojo.html.removeClass(this.actualHighlightedElem,"tableSuggestHover");
                        dojo.html.addClass(this.actualHighlightedElem,"tableSuggestOut");
                        dojo.html.removeClass(nextElem,"tableSuggestOut");
                        dojo.html.addClass(nextElem,"tableSuggestHover");
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
                            var firstOptionElem = this.getFirstRowElem(popUp);
                            this.actualHighlightedElem = firstOptionElem;
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
                    dojo.html.removeClass(this.actualHighlightedElem,"tableSuggestHover");
                    dojo.html.addClass(this.actualHighlightedElem,"tableSuggestOut");
                    dojo.html.removeClass(prevElem,"tableSuggestOut");
                    dojo.html.addClass(prevElem,"tableSuggestHover");
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
                    var lastOptionElem = this.getLastRowElem(popUp);
                    this.actualHighlightedElem = lastOptionElem;
                }
                else
                    dojo.debug("could not move to next item in table, wrong item is");dojo.debug(nextElem);
            }
        }
        else
        {
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

                                      var table = dojo.dom.firstElement(popUp);
                                      var tbody = table.childNodes[1];
                                      var rowElem = tbody.firstChild;

                                      tableSuggest.putValueToField(rowElem);
                                  }
                                  else
                                  {
                                      collection.add(tablePagesArray[k]);
                                  }
                              }

                              collection.add(firstPage);
                              collection.add(firstPageField);

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

        this.tablePagesCollection.remove(0);
        this.tablePagesCollection.remove(1);

        var thisPage = dojo.dom.prevElement(thisPageField);

        var popUp = dojo.dom.getFirstAncestorByTag(thisPage, "div");

        dojo.dom.removeChildren(popUp);

        dojo.dom.insertAtPosition(nextPage, popUp, "first");
        dojo.dom.insertAtPosition(nextPageField, popUp, "last");

        this.tablePagesCollection.add(thisPage);
        this.tablePagesCollection.add(thisPageField);
    };

    org_apache_myfaces_TableSuggest.prototype.previousPage = function(thisTable)
    {
        var prevPage = this.tablePagesCollection.item(this.tablePagesCollection.count+1);
        var prevPageField = this.tablePagesCollection.item(this.tablePagesCollection.count);

        this.tablePagesCollection.remove(this.tablePagesCollection.count+1);
        this.tablePagesCollection.remove(this.tablePagesCollection.count);

        var popUp = dojo.dom.getFirstAncestorByTag(thisTable, "div");

        dojo.dom.removeChildren(popUp);

        dojo.dom.insertAtPosition(prevPage, popUp, "first");
        dojo.dom.insertAtPosition(prevPageField, popUp, "last");

        this.tablePagesCollection.insert(this.tablePagesCollection.count,thisTable);
        this.tablePagesCollection.insert(this.tablePagesCollection.count,dojo.dom.nextElement(thisTable));
    };

    org_apache_myfaces_TableSuggest.prototype.getFirstRowElem = function(popUp)
    {
        var table = dojo.dom.firstElement(popUp);
        var tbody = table.childNodes[1];
        var firstRowElem = dojo.dom.firstElement(tbody);

        return firstRowElem;
    };

    org_apache_myfaces_TableSuggest.prototype.getLastRowElem = function(popUp)
    {
        var table = dojo.dom.firstElement(popUp);
        var tbody = table.childNodes[1];
        var lastRowElem = dojo.dom.lastElement(tbody);

        return lastRowElem;
    };
}