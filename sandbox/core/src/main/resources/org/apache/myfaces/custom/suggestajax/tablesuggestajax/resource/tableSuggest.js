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


org_apache_myfaces_TableSuggest = function(ajaxUrl,
                                           millisBetweenKeyUps,
                                           startChars,
                                           charset,
                                           acceptValueToField,
                                           fieldNames, 
                                           styleClassOptions)
{
    this.tablePagesCollection = new dojo.collections.ArrayList();

    this.inputField = null;

    this.inputFieldId = null;
    this.popUp = null;
    this.url = ajaxUrl;
    this.charset = charset;
    this.firstHighlightedElem = null;
    this.actualHighlightedElem = null;

    this.blurTimer = null;
    this.validateInput = true;
    this.saveOldValues = true;
    this.oldValues = null;
    
    this.iframe = null;
    this.timeOut = null;
    
    this.data = null;
    this.columnHeaders = null;

    this.startChars = startChars;
    this.acceptValueToField = acceptValueToField;
    this.lastKeyPressTime = new Date();
    this.millisBetweenKeyUps = millisBetweenKeyUps;
    this.scrollingRow = 0;
    this.fieldNames = fieldNames;

    
    this.tableStyleClass = styleClassOptions.tableStyleClass;
    this.columnHoverClass = styleClassOptions.columnHoverClass;  
    this.columnOutClass = styleClassOptions.columnOutClass;  
    

    //puting the values from the choosen row into the fields
    org_apache_myfaces_TableSuggest.prototype.putValueToField = function(trElem)
    {
        if (trElem)
        {
            this.scrollOverflowDiv(trElem);

            for (var j = 0; j < trElem.childNodes.length; j+=1)
            {
                var tdElem = trElem.childNodes[j];

                for (var a = 0; a < tdElem.childNodes.length; a+=1)
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
                                for (i = 0; i < elemToPutValue.options.length; i+=1)
                                {
                                    var optionValue = spanElem.innerHTML;

                                    if (elemToPutValue.options[i].value == optionValue) {
                                        elemToPutValue.options[i].selected = true;
                                    }    
                                }
                            }
                        }
                    }
                }
            }
        }
        this.inputField.focus();
    };


    org_apache_myfaces_TableSuggest.prototype.typeAhead = function(suggestion)
    {
        var len = this.inputField.value.length; 
        this.inputField.value = suggestion;
        
        if (this.inputField.createTextRange)
        {
            var range = this.inputField.createTextRange();
            range.moveStart("character",  len);
            range.moveEnd("character", suggestion.length - len);
            range.select();
        }
        else if (this.inputField.setSelectionRange) {
            this.inputField.setSelectionRange(len, suggestion.length);
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
                    this.scrollingRow+=1;

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

    org_apache_myfaces_TableSuggest.prototype.handleRequestResponse = function(tableSuggest)
    {
        
        if (this.timeOut != null) 
        {
            clearTimeout(this.timeOut);
            this.timeOut = null;
        }
        
        var idValuePair = "&" + this.inputField.id + "=" + this.inputField.value;

        this.timeOut = setTimeout(function() {
        
            this.firstHighlightedElem = null;
            this.actualHighlightedElem = null;

            dojo.io.bind(
            {
                url:  tableSuggest.url + idValuePair,
                handle: function(type, data, evt) {
                
                    dojo.debug("after response");
                    //if(data) dojo.debug(data);
                    
                    if(type == "load" && data)
                    {
                        tableSuggest.parseAjaxResponse(data);
                        tableSuggest.renderDropdown();
                        return;
			
                        dojo.debug("response successful");
                        var tablePagesArray = dojo.html.createNodesFromText(data);
                        var collection = tableSuggest.tablePagesCollection;

                        var firstPage = null;
                        var firstPageField = null;

                        dojo.dom.removeChildren(tableSuggest.popUp);

                        collection.clear();

                        for(k=0;k<tablePagesArray.length;k+=1)
                        {
                            if(k==0)
                            {
                                firstPage = tablePagesArray[k];
                                firstPageField = tablePagesArray[k+1];
                                dojo.dom.insertAtPosition(firstPage, tableSuggest.popUp, "first");
                                dojo.dom.insertAtPosition(firstPageField, tableSuggest.popUp, "last");
                                k+=1;
					
                                if(firstPage.rows && firstPage.rows.length == 2) 
                                {
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
                },   //end function
                mimetype: "text/xml",
                content: { charset: tableSuggest.charset }
            }); //end bind
            
        }, this.millisBetweenKeyUps);
    };
    
    //handles the user pressing the 'Up-Arrow' key
    org_apache_myfaces_TableSuggest.prototype.handleUpKey = function()
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
        
        return;
    }
    
    //parses the XML ajax response data
    org_apache_myfaces_TableSuggest.prototype.parseAjaxResponse = function(data)
    {
        var root = data.documentElement;
        
	//parse the column names of the table to be rendered 
        var columnHeaders = root.getElementsByTagName("columnHeader");
	var columnHeadersArray = new Array();
	for (var i = 0; i < columnHeaders.length; i+=1)
        {
 	    columnHeadersArray[i] = columnHeaders[i].firstChild.nodeValue;
	}
	this.columnHeaders = columnHeadersArray;
	        
        //parse the individual data items
        var items = root.getElementsByTagName("item");
        var itemsArray = new Array();
        
        for (var i = 0; i < items.length; i+=1)
        {
            var currentItem = items[i];
	    var columnsArray = new Array();
            
            for (var j = 0; j < currentItem.childNodes.length; j+=1)
            {   
                var currentColumn = currentItem.childNodes[j];
                var column = new Object();

                var currentChild = currentColumn.firstChild;                

                do {
                    column[currentChild.tagName] = currentChild.firstChild.nodeValue;
                } while (currentChild = currentChild.nextSibling);
                
                columnsArray[j] = column;
	    }
            
            itemsArray[i] = columnsArray;
	}
        this.data = itemsArray;
    }
    	
    //Render the ajax drop-down window
    org_apache_myfaces_TableSuggest.prototype.renderDropdown = function()
    {
        if (this.actualHighlightedElem != null)
        {
            this.actualHighlightedElem = null;        
        }

        dojo.dom.removeChildren(this.popUp);
        
        //if no data exists
        if (this.data == null || this.data.length == 0) 
        {
            this.resetSettings();        
            return;       
        }
        
        //create a table for the suggesting items
	var suggestTable = document.createElement("table");
        //suggestTable.setAttribute("class", this.tableStyleClass);
        suggestTable.className = "ajaxTable";
         
        var tbody = document.createElement("tbody");
	suggestTable.appendChild(tbody);
                
        var thead = suggestTable.createTHead();
	var tr, td, tn, span;
	        
	//add the column Headers
        tr = thead.insertRow(0);
        
        for (var i = 0; i < this.columnHeaders.length; i+=1)
        {
	    td = tr.insertCell(tr.cells.length);
            tn = document.createTextNode(this.columnHeaders[i]);
            td.appendChild(tn);
        }
        	
        var tableSuggestAjax = this;

        //adding the data items	
	for (var i = 0; i < this.data.length; i+=1)
        {
            var columnsArray = this.data[i];
            tr = tbody.insertRow(tbody.rows.length);
                        
            tr.onmouseover = function() { this.className = tableSuggestAjax.columnHoverClass; }
            tr.onmouseout = function() { this.className= tableSuggestAjax.columnOutClass; }
            tr.id = "row" + (i + 1) + this.inputFieldId;
            var tableSuggest = this;
            tr.onclick = function(event) { 
                var evt = (event) ? event : window.event;

                //find the enclosing tr element
                var target = evt.target || evt.srcElement;
                var obj = target.parentNode;
                while (obj.tagName != 'TR')
                {
                    obj = obj.parentNode;
                }
                
                tableSuggest.putValueToField(obj);
            }
	                            	
            for (var j = 0; j < columnsArray.length; j+=1) 
            {
                td = tr.insertCell(tr.cells.length);
                
                //DEBUG
                if (typeof columnsArray[j].forText != "undefined")
                { 
                    span = document.createElement("span");
                    //span.setAttribute("id", "putValueTo" + columnsArray[j].forText); 
                    span.id = "putValueTo" + columnsArray[j].forText; 
                    tn = document.createTextNode(columnsArray[j].label);
                    span.appendChild(tn);
                    td.appendChild(span);
                }
                else {
                    span = document.createElement("span");
                    span.id = "putValueTo" + columnsArray[j].forValue;
                    tn = document.createTextNode(columnsArray[j].value); 
                    span.appendChild(tn);
                    span.style.display = "none";
                    td.appendChild(span);
                    
                    span = document.createElement("span");
                    tn = document.createTextNode(columnsArray[j].label); 
                    span.appendChild(tn);
                    td.appendChild(span);
                }
            }
	}   
                
        dojo.dom.insertAtPosition(suggestTable, this.popUp, "first");
        this.handleIFrame();
        
        var suggestion = (this.data[0])[0].label;
        this.typeAhead(suggestion);
    }
    
    //handles the user pressing the 'Down Arrow' key
    org_apache_myfaces_TableSuggest.prototype.handleDownKey = function()
    {
        if(this.actualHighlightedElem == null)
        {
	    this.actualHighlightedElem = this.getFirstRowElem(this.popUp);
            if (this.actualHighlightedElem == null) return;
        }
        else {
            var nextElem = dojo.dom.nextElement(this.actualHighlightedElem);
            if (nextElem == null) return;
        
            this.addOutClass(this.actualHighlightedElem);
            this.actualHighlightedElem = nextElem;
        }
        
        this.putValueToField(this.actualHighlightedElem);
        this.addHoverClass(this.actualHighlightedElem);
        
        return;    
    } 
    

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
        dojo.html.removeClass(elem, this.columnOutClass);
        dojo.html.addClass(elem, this.columnHoverClass);
    };

    org_apache_myfaces_TableSuggest.prototype.addOutClass = function(elem)
    {
        dojo.html.removeClass(elem, this.columnHoverClass);
        dojo.html.addClass(elem, this.columnOutClass);
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

    org_apache_myfaces_TableSuggest.prototype.onFocus = function() {
        this.hasFocus = true;
        if (this.saveOldValues)
        {
            this.oldValues = new Object();
            for (var i = 0; i < this.fieldNames.length; i+=1)
            {
                var fieldName = this.fieldNames[i];
                var field = dojo.byId(fieldName);
                this.oldValues[fieldName] = field.value;
            }
            this.saveOldValues = false;
        }
    }
    
    org_apache_myfaces_TableSuggest.prototype.onBlur = function() {
        
        var tableSuggest = this;
        this.hasFocus = false;
        
        var selectedRecord = null;
        
        //the timeout function will validate the user's input
        this.blurTimer = setTimeout(function() { 
            
            tableSuggest.resetSettings();        
            if (tableSuggest.hasFocus) return;

            //next time you tab into this field, you must save the old values
            tableSuggest.saveOldValues = true;
            
            var inputValue = tableSuggest.inputField.value;
                        
            if (tableSuggest.data == null) return;
            //search if this value is from the list of acceptable values
            for (var i = 0; i < tableSuggest.data.length; i+=1)
            {
	        var item = tableSuggest.data[i];
                var primaryColumn = item[0];
                if (inputValue == primaryColumn.label) 
                {
                    tableSuggest.updateForeignKeyFields(item);
                    return;
                }
            }
            //validation failed : restore the original values of all the fields (including foreign-key fields)
            for (var i = 0; i < tableSuggest.fieldNames.length; i+=1)
            {   
                var fieldName = tableSuggest.fieldNames[i];
                var field = dojo.byId(fieldName);
                field.value = tableSuggest.oldValues[fieldName]; 
                //tableSuggest.inputField.value = "";
                //tableSuggest.inputField.focus();
            }
            
      }, 500);

    }
    
    org_apache_myfaces_TableSuggest.prototype.updateForeignKeyFields = function(row) 
    {
        for (var i = 0; i < row.length; i+=1)
        {
            var column = row[i];
            var fieldId, field;
            
            if (typeof column.forText != "undefined")
            {
                fieldId = column.forText;
                field = dojo.byId(fieldId); 
                field.value = column.label;
            }
            else {
                fieldId = column.forValue;
                field = dojo.byId(fieldId);     
                field.value = column.value;
            }
        }
    }
    
    org_apache_myfaces_TableSuggest.prototype.handleKeyDown = function(evt)
    {
        var keyCode = evt.keyCode;
        
        switch (keyCode) 
        {
            case 40:    //down key
                this.handleDownKey();
                break;
            case 38:    //up key    
                this.handleUpKey();
                break;
            case 27: //escape
                this.resetSettings();
                break;
            case 13:  //enter
                this.resetSettings();
                break;
            default:
                return true;
        }
        evt.preventDefault();
    }
        
    org_apache_myfaces_TableSuggest.prototype.decideRequest = function(event)
    {
        this.inputFieldId = event.target.id;
        this.inputField = dojo.byId(this.inputFieldId);
        this.popUp = dojo.byId(event.target.id + "_auto_complete");
        
        var inputValue = this.inputField.value;
        	
        if( (event.keyCode == 13) || (event.keyCode == 9) ) //enter, tab keys
        {             
            this.resetSettings();
            return;
        }
			
	if(inputValue == "" )
	{
            this.resetSettings();
            return;
        }
		
        if (this.startChars) //minimum # of characters required before look-up is activated
        {
            if (inputValue.length < this.startChars)
            {
                this.resetSettings();
                return;
            }
        }
        
        //I tried to block these events in the keydown event-handler itself, but 
        //haven't quite figured out how to do it. 
        switch (event.keyCode) 
        {
            case 40:    //down key
            case 38:    //up key    
            case 27: //escape
            case 13:  //enter
              return;
        }
    
        this.handleRequestResponse(this);
        
    };
}

