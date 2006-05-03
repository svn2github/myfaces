/*************************************************************
 * AJAX Spell Checker - Version 2.4
 * (C) 2005 - Garrison Locke
 *
 * This spell checker is built in the style of the Gmail spell
 * checker.  It uses AJAX to communicate with the backend without
 * requiring the page be reloaded.  If you use this code, please
 * give me credit and a link to my site would be nice.
 * http://www.broken-notebook.com.
 *
 * Copyright (c) 2005, Garrison Locke
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the http://www.broken-notebook.com nor the names of its
 *     contributors may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 *************************************************************/
var cp = new cpaint();
cp.set_transfer_mode('post');
cp.set_response_type('text');


// If there are already any onclick handlers loaded in the page, we'll add
// our onclick handler first and then call the old one, rather than completely
// overriding it.  The checkClickLocation is used to hide the suggestions div
// when the user clicks outside it.
if (document.onclick) {
    var old_onclick = document.onclick;
    document.onclick = function(e) {
        checkClickLocation(e);
        old_onclick(e);
    }
} else {
    document.onclick = checkClickLocation;
}

var currObj; //the current spell checker being used
var spellingSuggestionsDiv = null;  // Auto-generated suggestions div


/*************************************************************
 * ajaxSpell(varName, width, height, spellUrl, divId, name, id)
 *
 * This is the constructor that creates a new ajaxSpell object.
 * All of it is dynamically generated so the user doesn't have
 * to add a bunch of crap to their site.
 *
 * @param varName - the name of the variable that the object is
 *                  assigned to (must be unique and the same as the variable)
 * @param width - the width of the spell checker
 * @param height - the height of the spell checker
 * @param spellUrl - the url of the spell_checker.php code
 * @param divId - the id of the div that the spell checker is
 *                contained in (must be unique)
 * @param name - the name of the textarea form element
 * @param id - the id of the spell checker textarea (must be unique)
 *
 *************************************************************/
function ajaxSpell(varName, width, height, spellUrl, divId, name, actionSpanId, statusSpanId, id,
                   spellcheckermb, text_spchk, text_resume, text_working, text_nomisspelling, text_checking){ //@MODIFIED

    currObj = this;

    currObj.config               = new Array();         //the array of configuration options
    currObj.config['varName']    = varName;             //the name of the variable that this instance is stored in
    currObj.config['width']      = width;               //the width of the textarea
    currObj.config['height']     = height;              //the height of the textarea
    currObj.config['spellUrl']   = spellUrl;            //url to spell checker php code (spell_checker.php by default);
    currObj.config['divId']      = divId;               //the id of the div that the spell checker element is in
    currObj.config['name']       = name;                //what you want the form element's name to be
    currObj.config['id']         = id;                  //the unique id of the spell_checker textarea
    currObj.config['spellcheckermb']  	= spellcheckermb; // Methodbinding @MODIFIED
    currObj.config['text_spchk']  		= text_spchk; // Text for spellcheck link @MODIFIED
    currObj.config['text_resume']  		= text_resume; // Text for resume link @MODIFIED
    currObj.config['text_working']  	= text_working; // working text @MODIFIED
    currObj.config['text_nomisspelling']  = text_nomisspelling; // no misspelling text @MODIFIED
    currObj.config['text_checking'] 	= text_checking; // checking text @MODIFIED
    document.getElementById(currObj.config['divId']).style.width = currObj.config['width'];


    //generate the div to hold the spell checker controls
    /*
    currObj.controlPanelDiv = document.createElement('DIV');
    currObj.controlPanelDiv.className = 'control_panel';
    document.getElementById(currObj.config['divId']).appendChild(currObj.controlPanelDiv);
    */
    currObj.controlPanelDiv = document.getElementById(divId);

    currObj.actionSpan = document.getElementById(actionSpanId);
    //currObj.actionSpan = document.createElement('SPAN');
    //currObj.actionSpan.className = "action";
    //currObj.actionSpan.id = "action";
    //currObj.controlPanelDiv.appendChild(currObj.actionSpan);

    //the span that toggles between spell checking and editing
    if (currObj.config['text_spchk'] != null  && currObj.actionSpan != null) {
        currObj.actionSpan.innerHTML = "<a class=\"check_spelling\" onclick=\"setCurrentObject(" + currObj.config['varName'] + "); " + currObj.config['varName'] + ".spellCheck();\">" + currObj.config['text_spchk'] + "</a>";
    }

    //the span that lets the user know of the status of the spell checker
    currObj.statusSpan = document.getElementById(statusSpanId);
    //currObj.statusSpan = document.createElement('SPAN');
    //currObj.statusSpan.className = "status";
    //currObj.statusSpan.id = "status";
    //currObj.controlPanelDiv.appendChild(currObj.statusSpan);

    //the textarea to be spell checked
    currObj.textBox = document.getElementById(id);
    //currObj.textBox = document.createElement('TEXTAREA');
    //currObj.textBox.className = "text_box";
    //currObj.textBox.id = currObj.config['id'];
    //currObj.textBox.style.width = currObj.config['width'] + "px";
    //currObj.textBox.style.height = currObj.config['height'] + "px";
    //currObj.textBox.name = currObj.config['name'];
    //document.getElementById(currObj.config['divId']).appendChild(currObj.textBox);

    currObj.objToCheck              = document.getElementById(currObj.config['id']);      //the actual object we're spell checking
    /*currObj.spellingResultsDiv      = null;                                               // Auto-generated results div
*/

    //prototypes for the ajaxSpell objects
    ajaxSpell.prototype.spellCheck           = spellCheck;
    ajaxSpell.prototype.spellCheck_cb        = spellCheck_cb;
    ajaxSpell.prototype.showSuggestions      = showSuggestions;
    ajaxSpell.prototype.showSuggestions_cb   = showSuggestions_cb;
    ajaxSpell.prototype.replaceWord          = replaceWord;
    ajaxSpell.prototype.switchText           = switchText;
    ajaxSpell.prototype.switchText_cb        = switchText_cb;
    ajaxSpell.prototype.resumeEditing        = resumeEditing;
    ajaxSpell.prototype.resetSpellChecker    = resetSpellChecker;
    ajaxSpell.prototype.resetAction          = resetAction;
    ajaxSpell.prototype.setText              = setText;
}


/*************************************************************
 * setCurrentObject(obj)
 *
 * This sets the current object to be the spell checker that
 * the user is currently using.
 *
 * @param obj - the spell checker currently being used
 *
 *************************************************************/
function setCurrentObject(obj){
    currObj = obj;
}; //end setCurrentObject function


/*************************************************************
 * setText(text)
 *
 * This can be used to set preloaded text in the spell checker
 * textarea.
 *
 * @param text - the text to put in the textarea
 *
 *************************************************************/
function setText(text){
    with(currObj);
    text = text.replace(/<br *\/?>/gi, "\n");
    currObj.objToCheck.value = decodeURIComponent(text);
}; //end setText function



/*************************************************************
 * spellCheck_cb(new_data)
 *
 * This is the callback function that the spellCheck php function
 * returns the spell checked data to.  It sets the results div
 * to contain the markedup misspelled data and changes the status
 * message.  It also sets the width and height of the results
 * div to match the element that's being checked.
 * If there are no misspellings then new_data is the empty
 * string and the status is set to "No Misspellings Found".
 *
 * @param new_data - The marked up misspelled data returned from php.
 *
 *************************************************************/
function spellCheck_cb(new_data) {
    with(currObj);
    new_data = new_data.toString();
    var isThereAMisspelling = new_data.charAt(0);
    new_data = new_data.substring(1);


    currObj.spellingResultsDiv = document.getElementById(currObj.config['varName']+'_edit_div');
    currObj.spellingResultsDiv.innerHTML = new_data;

    currObj.spellingResultsDiv.className = 'edit_box';
    currObj.spellingResultsDiv.style.width = currObj.objToCheck.style.width;
    currObj.spellingResultsDiv.style.height = currObj.objToCheck.style.height;
    currObj.spellingResultsDiv.style.display = '';

    currObj.objToCheck.style.display = "none";
    /*currObj.objToCheck.parentNode.insertBefore(currObj.spellingResultsDiv,currObj.objToCheck);
*/
    currObj.statusSpan.innerHTML = "";
    if (currObj.config['text_resume'] != null) {
        currObj.actionSpan.innerHTML = "<a class=\"resume_editing\" onclick=\"setCurrentObject(" + currObj.config['varName'] + "); " + currObj.config['varName'] + ".resumeEditing();\">" + currObj.config['text_resume'] + "</a>";
    }

    if(isThereAMisspelling != "1"){
        currObj.statusSpan.innerHTML = currObj.config['text_nomisspelling'];
        currObj.objToCheck.disabled = false;
    }
}; //end spellCheck_cb function


/*************************************************************
 * spellCheck()
 *
 * The spellCheck javascript function sends the text entered by
 * the user in the text box to php to be spell checked.  It also
 * sets the status message to "Checking..." because it's currently
 * checking the spelling.
 *
 *************************************************************/
function spellCheck() {
    with(currObj);
    var query;

    if (currObj.spellingResultsDiv) {
        /*currObj.spellingResultsDiv.parentNode.removeChild(currObj.spellingResultsDiv);
        */
        currObj.spellingResultsDiv.style.display = 'none';
        /*
        currObj.spellingResultsDiv = null;
        */
    }
    if (currObj.config['text_spchk'] != null) {
        currObj.actionSpan.innerHTML = "<a class=\"check_spelling\">" + currObj.config['text_spchk'] + "</a>";
    }
    currObj.statusSpan.innerHTML = currObj.config['text_checking'];
    query = currObj.objToCheck.value;
    cp.call(currObj.config['spellUrl'], 'spellCheck', spellCheck_cb, query, currObj.config['varName'],
            currObj.config['spellcheckermb'], // @MODIFIED
            "CHECK"); // @MODIFIED
}; //end spellcheck function*/


/*************************************************************
 * checkClickLocation(e)
 *
 * This function is called by the event listener when the user
 * clicks on anything.  It is used to close the suggestion div
 * if the user clicks anywhere that's not inside the suggestion
 * div.  It just checks to see if the name of what the user clicks
 * on is not "suggestions" then hides the div if it's not.
 *
 * @param e - the event, in this case the user clicking somewhere on
 *     the page.
 *
 *************************************************************/
function checkClickLocation(e){
    if (spellingSuggestionsDiv) {

        // Bah.  There's got to be a better way to deal with this, but the click
        // on a word to get suggestions starts up a race condition between
        // showing and hiding the suggestion box, so we'll ignore the first
        // click.
        if (spellingSuggestionsDiv.ignoreNextClick) {
            spellingSuggestionsDiv.ignoreNextClick = false;
        }
        else {
            var theTarget = getTarget(e);

            if (theTarget != spellingSuggestionsDiv){
                spellingSuggestionsDiv.parentNode.removeChild(spellingSuggestionsDiv);
                spellingSuggestionsDiv = null;
            }
        }
    }

    return true; // Allow other handlers to continue.
}; //end checkClickLocation function


/*************************************************************
 * getTarget(e)
 *
 * The get target function gets the correct target of the event.
 * This function is required because IE handles the events in
 * a different (wrong) manner than the rest of the browsers.
 *
 * e - the target, in this case the user clicking somewhere on
 *     the page.
 *
 *************************************************************/
function getTarget(e){
    var value;
    if (checkBrowser() == "ie"){
        value = window.event.srcElement;
    }
    else{
        value = e.target;
    }
    return value;
}; //end getTarget function


/*************************************************************
 * checkBrowser()
 *
 * The checkBrowser function simply checks to see what browser
 * the user is using and returns a string containing the browser
 * type.
 *
 *************************************************************/
function checkBrowser(){
    var theAgent = navigator.userAgent.toLowerCase();
    if(theAgent.indexOf("msie") != -1){
        if(theAgent.indexOf("opera") != -1){
            return "opera";
        }
        else{
            return "ie";
        }
    }
    else if(theAgent.indexOf("netscape") != -1){
        return "netscape";
    }
    else if(theAgent.indexOf("firefox") != -1){
        return "firefox";
    }
    else if(theAgent.indexOf("mozilla/5.0") != -1){
        return "mozilla";
    }
    else if(theAgent.indexOf("\/") != -1){
        if (theAgent.substr(0,theAgent.indexOf('\/')) != 'mozilla'){
            return navigator.userAgent.substr(0,theAgent.indexOf('\/'));
        }
        else{
            return "netscape";
        }
    }
    else if(theAgent.indexOf(' ') != -1){
        return navigator.userAgent.substr(0,theAgent.indexOf(' '));
    }
    else{
        return navigator.userAgent;
    }
}; //end checkBrowser function


/*************************************************************
 * showSuggestions_cb(new_data)
 *
 * The showSuggestions_cb function is a callback function that
 * php's showSuggestions function returns to.  It sets the
 * suggestions table to contain the new data and then displays
 * the suggestions div.  It also clears the status message.
 *
 * new_data - The suggestions table returned from php.
 *
 *************************************************************/
function showSuggestions_cb(new_data){
    with(currObj);
    currObj.statusSpan.innerHTML = "";

    spellingSuggestionsDiv.innerHTML = new_data;
    spellingSuggestionsDiv.style.display = 'block';
}; //end showSuggestions_cb function


/*************************************************************
 * showSuggestions(word, id)
 *
 * The showSuggestions function calls the showSuggestions php
 * function to get suggestions for the misspelled word that the
 * user has clicked on.  It sets the status to "Searching...",
 * hides the suggestions div, finds the x and y position of the
 * span containing the misspelled word that user clicked on so
 * the div can be displayed in the correct location, and then
 * calls the showSuggestions php function with the misspelled word
 * and the id of the span containing it.
 *
 * word - the misspelled word that the user clicked on
 * id - the id of the span that contains the misspelled word
 *
 *************************************************************/
function showSuggestions(word, id) {
    with(currObj);
    /*
    currObj.statusSpan.innerHTML = "Searching...";
    */
    currObj.statusSpan.innerHTML = "";
    /*TODO show the text at teh current mouse cursor pos instead of
    the window location
    */
    var x = findPosX(id);
    var y = findPosY(id);

    var scrollPos = 0;
    if (checkBrowser() != "ie") {
        scrollPos = currObj.spellingResultsDiv.scrollTop;
    }

    if (spellingSuggestionsDiv) {
        spellingSuggestionsDiv.parentNode.removeChild(spellingSuggestionsDiv);
    }
    spellingSuggestionsDiv = document.createElement('DIV');
    spellingSuggestionsDiv.style.display = "none";
    spellingSuggestionsDiv.className = 'suggestion_box';
    spellingSuggestionsDiv.style.position = 'absolute';
    spellingSuggestionsDiv.style.left = x + 'px';
    spellingSuggestionsDiv.style.top = (y+16-scrollPos) + 'px';
	spellingSuggestionsDiv.style.zIndex = 999;
    // Bah.  There's got to be a better way to deal with this, but the click
    // on a word to get suggestions starts up a race condition between
    // showing and hiding the suggestion box, so we'll ignore the first
    // click.
    spellingSuggestionsDiv.ignoreNextClick = true;

    document.body.appendChild(spellingSuggestionsDiv);

    cp.call(currObj.config['spellUrl'], 'showSuggestions', showSuggestions_cb, word, id,
            currObj.config['spellcheckermb'], // @MODIFIED
            "SUGGEST"); // @MODIFIED
}; //end showSuggestions function


/*************************************************************
 * replaceWord(id, newWord)
 *
 * The replaceWord function takes the id of the misspelled word
 * that the user clicked on and replaces the innerHTML of that
 * span with the new word that the user selects from the suggestion
 * div.  It hides the suggestions div and changes the color of
 * the previously misspelled word to green to let the user know
 * it has been changed.  It then calls the switchText php function
 * with the innerHTML of the div to update the text of the text box.
 *
 * id - the id of the span that contains the word to be replaced
 * newWord - the word the user selected from the suggestions div
 *           to replace the misspelled word.
 *
 *************************************************************/
function replaceWord(id, newWord){
	if(id == null) return;
	if(document.getElementById(id) == null) return;
    document.getElementById(id).innerHTML = trim(newWord);
    if (spellingSuggestionsDiv) {
        spellingSuggestionsDiv.parentNode.removeChild(spellingSuggestionsDiv);
        spellingSuggestionsDiv = null;
    }
    document.getElementById(id).style.color = "#005500";
    setTextInBackground();
}; //end replaceWord function


/*************************************************************
 * switchText()
 *
 * The switchText function is a funtion is called when the user
 * clicks on resume editing (or submits the form).  It calls the
 * php function to switchText and uncomments the html and replaces
 * breaks and everything.  Here all the breaks that the user has
 * typed are replaced with %u2026.  Firefox does this goofy thing
 * where it cleans up the display of your html, which adds in \n's
 * where you don't want them.  So I replace the user-entered returns
 * with something unique so that I can rip out all the breaks that
 * the browser might add and we don't want.
 *
 *************************************************************/
function switchText() {
    with(currObj);
    var text = currObj.spellingResultsDiv.innerHTML;
    text = text.replace(/<br *\/?>/gi, "~~~");
    cp.call(currObj.config['spellUrl'], 'switchText', switchText_cb, text);

}; //end switchText function


/*************************************************************
* we simple set the values in the background so that
* we have a clean jsf traversal even in normal
* spellcheck mode
**************************************************************/
function setTextInBackground() {
    with(currObj);
    var text = currObj.spellingResultsDiv.innerHTML;
    text = text.replace(/<br *\/?>/gi, "~~~");
    cp.call(currObj.config['spellUrl'], 'setTextInBackground', setTextInBackground_cb, text);
    /*setTextInBackground_cb(text);*/
}

/*************************************************************
* setTextInBackground_cb(new_string)
**************************************************************/
function setTextInBackground_cb(new_string) {
    with(currObj);
    new_string = new_string.replace(/~~~/gi, "\n");
    currObj.objToCheck.value = entityConversion(new_string);
}; //end switchText_cb function




/*************************************************************
 * switchText_cb(new_string)
 *
 * The switchText_cb function is a call back funtion that the
 * switchText php function returns to.  I replace all the %u2026's
 * with returns.  It then replaces the text in the text box with
 * the corrected text fromt he div.
 *
 * new_string - The corrected text from the div.
 *
 *************************************************************/
function switchText_cb(new_string) {
    with(currObj);
    setTextInBackground_cb(new_string);
    currObj.objToCheck.disabled = false;
    currObj.spellingResultsDiv = document.getElementById(currObj.config['varName']+'_edit_div');
    currObj.spellingResultsDiv.style.display = 'none';
    currObj.objToCheck.style.display = "inline";
    currObj.resetAction();
}; //end switchText_cb function




/*************************************************************
 * resumeEditing()
 *
 * The resumeEditing function is called when the user is in the
 * correction mode and wants to return to the editing mode.  It
 * hides the results div and the suggestions div, then enables
 * the text box and unhides the text box.  It also calls
 * resetAction() to reset the status message.
 *
 *************************************************************/
function resumeEditing() {
    with(currObj);
    if (currObj.config['text_resume'] != null) {
        currObj.actionSpan.innerHTML = "<a class=\"resume_editing\">" + currObj.config['text_resume'] + "</a>";
    }
    currObj.statusSpan.innerHTML = currObj.config['text_working'];

    if (spellingSuggestionsDiv) {
        spellingSuggestionsDiv.parentNode.removeChild(spellingSuggestionsDiv);
        spellingSuggestionsDiv = null;
    }

    currObj.switchText();
}; //end resumeEditing function


/*************************************************************
 * resetAction()
 *
 * The resetAction function just resets the status message to
 * the default action of "Check Spelling".
 *
 *************************************************************/
function resetAction() {
    with(currObj);
    if (currObj.config['text_spchk']) {
        currObj.actionSpan.innerHTML = "<a class=\"check_spelling\" onclick=\"setCurrentObject(" + currObj.config['varName'] + "); " + currObj.config['varName'] + ".spellCheck();\">" + currObj.config['text_spchk'] + "</a>";
    }
    currObj.statusSpan.innerHTML = "";
}; //end resetAction function


/*************************************************************
 * resetSpellChecker()
 *
 * The resetSpellChecker function resets the entire spell checker
 * to the defaults.
 *
 *************************************************************/
function resetSpellChecker() {
    with(currObj);
    currObj.resetAction();

    currObj.objToCheck.value = "";
    currObj.objToCheck.style.display = "inline";
    currObj.objToCheck.disabled = false;

    if (currObj.spellingResultsDiv)
        currObj.spellingResultsDiv.style.display = 'none';

    if (spellingSuggestionsDiv) {
        spellingSuggestionsDiv.parentNode.removeChild(spellingSuggestionsDiv);
        spellingSuggestionsDiv = null;
    }
    currObj.statusSpan.style.display = "none";

}; //end resetSpellChecker function


/*************************************************************
 * findPosX(object)
 *
 * The findPosX function just finds the X offset of the top left
 * corner of the object it's given.
 *
 * object - the object that you want to find the upper left X
 *          coordinate of.
 *
 *************************************************************/
function findPosX(object){
    var curleft = 0;
    var obj = document.getElementById(object);
    return getTotalOffset(obj, 'left', true) ;
}; //end findPosX function


/*************************************************************
 * findPosY(object)
 *
 * The findPosY function just finds the Y offset of the top left
 * corner of the object it's given.
 *
 * object - the object that you want to find the upper left Y
 *          coordinate of.
 *
 *************************************************************/
function findPosY(object){
    var curtop = 0;
    var obj = document.getElementById(object);
    curTop =  getTotalOffset(obj, 'top', true) ;
    /*fix between quirks of various browsers regarding the page offset for
    some kind of reason the page offset calculation has to be fixed from outside
    otherwise the div moves into a false position*/
    if(window.pageYOffset)
    	curTop += window.pageYOffset;
    else
    	curTop += window.document.body.scrollTop;
    
    return curTop;
}; //end findPosY function

/**
* backported from dojo
* generic offset calculation function
* instead of the original function
* the dojo one works as expected with scrollers
* as well
*/
function getTotalOffset(node, type, includeScroll){
    var typeStr = (type=="top") ? "offsetTop" : "offsetLeft";
    var typeScroll = (type=="top") ? "scrollTop" : "scrollLeft";

    var alt = (type=="top") ? "y" : "x";
    var ret = 0;
    if(node["offsetParent"]){

        if(includeScroll && node.parentNode != document.body) {
          ret -= sumAncestorProperties(node, typeScroll);
        }
        // FIXME: this is known not to work sometimes on IE 5.x since nodes
        // soemtimes need to be "tickled" before they will display their
        // offset correctly
        do {
            ret += node[typeStr];
            node = node.offsetParent;
        } while (node != document.getElementsByTagName("body")[0].parentNode && node != null);

    }else if(node[alt]){
        ret += node[alt];
    }
    return ret;
}
/*************************************************************
 * trim(s)
 *
 * Trims white space from a string.
 *
 * s - the string you want to trim.
 *
 *************************************************************/
function trim(s) {
  while (s.substring(0,1) == ' ') {
    s = s.substring(1,s.length);
  }
  while (s.substring(s.length-1,s.length) == ' ') {
    s = s.substring(0,s.length-1);
  }
  return s;
};


/************************************************
* backported from dojo the code is more generic
* maybe it will solve a problem we have
* with positioning
* node is the dom node to sum the ancestors for
* prop is the property which has to be summed
*************************************************/
function sumAncestorProperties(node, prop) {
    if (!node) { return 0; } // FIXME: throw an error?

    var retVal = 0;
    while (node) {
        var val = node[prop];
        if (val) {
            retVal += val - 0;
        }
        node = node.parentNode;
    }
    return retVal;
}

/**
* TODO please ask the author for relicensing perms
* before putting it into the apache codebase
* source
* http://groups.google.at/group/comp.lang.javascript/browse_thread/thread/d6de007a580f5f83/b357fd5b39f841e1?lnk=st&q=unescape+%26uuml%3B&rnum=1#b357fd5b39f841e1
*/

function replace(string,text,by) {

    var strLength = string.length, txtLength = text.length;
    if ((strLength == 0) || (txtLength == 0)) return string;

    var i = string.indexOf(text);
    if ((!i) && (text != string.substring(0,txtLength))) return string;
    if (i == -1) return string;

    var newstr = string.substring(0,i) + by;

    if (i+txtLength < strLength)
        newstr +=
replace(string.substring(i+txtLength,strLength),text,by);

    return newstr;

}

function entityConversion(text) {

    text = replace(text,'&quot;',unescape('%22'));
    text = replace(text,'&amp;',unescape('%26'));
    text = replace(text,'&lt;',unescape('%3C'));
    text = replace(text,'&gt;',unescape('%3E'));
    text = replace(text,'&nbsp;',unescape('%A0'));
    text = replace(text,'&iexcl;',unescape('%A1'));
    text = replace(text,'&cent;',unescape('%A2'));
    text = replace(text,'&pound;',unescape('%A3'));
    text = replace(text,'&yen;',unescape('%A5'));
    text = replace(text,'&brvbar;',unescape('%A6'));
    text = replace(text,'&sect;',unescape('%A7'));
    text = replace(text,'&uml;',unescape('%A8'));
    text = replace(text,'&copy;',unescape('%A9'));
    text = replace(text,'&ordf;',unescape('%AA'));
    text = replace(text,'&laquo;',unescape('%AB'));
    text = replace(text,'&not;',unescape('%AC'));
    text = replace(text,'&shy;',unescape('%AD'));
    text = replace(text,'&reg;',unescape('%AE'));
    text = replace(text,'&macr;',unescape('%AF'));
    text = replace(text,'&deg;',unescape('%B0'));
    text = replace(text,'&plusmn;',unescape('%B1'));
    text = replace(text,'&sup2;',unescape('%B2'));
    text = replace(text,'&sup3;',unescape('%B3'));
    text = replace(text,'&acute;',unescape('%B4'));
    text = replace(text,'&micro;',unescape('%B5'));
    text = replace(text,'&para;',unescape('%B6'));
    text = replace(text,'&middot;',unescape('%B7'));
    text = replace(text,'&cedil;',unescape('%B8'));
    text = replace(text,'&sup1;',unescape('%B9'));
    text = replace(text,'&ordm;',unescape('%BA'));
    text = replace(text,'&raquo;',unescape('%BB'));
    text = replace(text,'&frac14;',unescape('%BC'));
    text = replace(text,'&frac12;',unescape('%BD'));
    text = replace(text,'&frac34;',unescape('%BE'));
    text = replace(text,'&iquest;',unescape('%BF'));
    text = replace(text,'&Agrave;',unescape('%C0'));
    text = replace(text,'&Aacute;',unescape('%C1'));
    text = replace(text,'&Acirc;',unescape('%C2'));
    text = replace(text,'&Atilde;',unescape('%C3'));
    text = replace(text,'&Auml;',unescape('%C4'));
    text = replace(text,'&Aring;',unescape('%C5'));
    text = replace(text,'&AElig;',unescape('%C6'));
    text = replace(text,'&Ccedil;',unescape('%C7'));
    text = replace(text,'&Egrave;',unescape('%C8'));
    text = replace(text,'&Eacute;',unescape('%C9'));
    text = replace(text,'&Ecirc;',unescape('%CA'));
    text = replace(text,'&Euml;',unescape('%CB'));
    text = replace(text,'&Igrave;',unescape('%CC'));
    text = replace(text,'&Iacute;',unescape('%CD'));
    text = replace(text,'&Icirc;',unescape('%CE'));
    text = replace(text,'&Iuml;',unescape('%CF'));
    text = replace(text,'&ETH;',unescape('%D0'));
    text = replace(text,'&Ntilde;',unescape('%D1'));
    text = replace(text,'&Ograve;',unescape('%D2'));
    text = replace(text,'&Oacute;',unescape('%D3'));
    text = replace(text,'&Ocirc;',unescape('%D4'));
    text = replace(text,'&Otilde;',unescape('%D5'));
    text = replace(text,'&Ouml;',unescape('%D6'));
    text = replace(text,'&times;',unescape('%D7'));
    text = replace(text,'&Oslash;',unescape('%D8'));
    text = replace(text,'&Ugrave;',unescape('%D9'));
    text = replace(text,'&Uacute;',unescape('%DA'));
    text = replace(text,'&Ucirc;',unescape('%DB'));
    text = replace(text,'&Uuml;',unescape('%DC'));
    text = replace(text,'&Yacute;',unescape('%DD'));
    text = replace(text,'&THORN;',unescape('%DE'));
    text = replace(text,'&szlig;',unescape('%DF'));
    text = replace(text,'&agrave;',unescape('%E0'));
    text = replace(text,'&aacute;',unescape('%E1'));
    text = replace(text,'&acirc;',unescape('%E2'));
    text = replace(text,'&atilde;',unescape('%E3'));
    text = replace(text,'&auml;',unescape('%E4'));
    text = replace(text,'&aring;',unescape('%E5'));
    text = replace(text,'&aelig;',unescape('%E6'));
    text = replace(text,'&ccedil;',unescape('%E7'));
    text = replace(text,'&egrave;',unescape('%E8'));
    text = replace(text,'&eacute;',unescape('%E9'));
    text = replace(text,'&ecirc;',unescape('%EA'));
    text = replace(text,'&euml;',unescape('%EB'));
    text = replace(text,'&igrave;',unescape('%EC'));
    text = replace(text,'&iacute;',unescape('%ED'));
    text = replace(text,'&icirc;',unescape('%EE'));
    text = replace(text,'&iuml;',unescape('%EF'));
    text = replace(text,'&eth;',unescape('%F0'));
    text = replace(text,'&ntilde;',unescape('%F1'));
    text = replace(text,'&ograve;',unescape('%F2'));
    text = replace(text,'&oacute;',unescape('%F3'));
    text = replace(text,'&ocirc;',unescape('%F4'));
    text = replace(text,'&otilde;',unescape('%F5'));
    text = replace(text,'&ouml;',unescape('%F6'));
    text = replace(text,'&divide;',unescape('%F7'));
    text = replace(text,'&oslash;',unescape('%F8'));
    text = replace(text,'&ugrave;',unescape('%F9'));
    text = replace(text,'&uacute;',unescape('%FA'));
    text = replace(text,'&ucirc;',unescape('%FB'));
    text = replace(text,'&uuml;',unescape('%FC'));
    text = replace(text,'&yacute;',unescape('%FD'));
    text = replace(text,'&thorn;',unescape('%FE'));
    text = replace(text,'&yuml;',unescape('%FF'));


    return text;
}