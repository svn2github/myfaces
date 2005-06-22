/*
 * Copyright 2004 The Apache Software Foundation.
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
/*
 * Utilities used by MyFaces x:inputHtml tag
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
var myFacesKupuTextToLoad;
var myFacesKupuClientId;
var myFacesKupuFormId;
var myFacesKupuOriginalDocOnSubmit;
var myFacesKupuResourceBaseURL;

function myFacesKupuSet(text, clientId, formId, resourceBaseURL){
	myFacesKupuTextToLoad = text;
	myFacesKupuClientId = clientId;
	myFacesKupuFormId = formId;
	myFacesKupuResourceBaseURL = resourceBaseURL;
	
	var onLoadSrc;
    if( document.all ) // IE
		onLoadSrc = document.body;
	else // Mozilla
		onLoadSrc = window;

	myFacesKupuOriginalDocOnLoad = onLoadSrc.onload;
	onLoadSrc.onload = myFacesKupuInit;
	
	var form = document.forms[myFacesKupuFormId];
	myFacesKupuOriginalDocOnSubmit = form.onsubmit;
	form.onsubmit = myFacesKupuSubmit;
}

function myFacesKupuInit(){
	if( myFacesKupuOriginalDocOnLoad )
		myFacesKupuOriginalDocOnLoad();

	kupu = startKupu();

	kupu.getInnerDocument().documentElement.getElementsByTagName('body')[0].innerHTML = myFacesKupuTextToLoad;
}

function myFacesKupuSubmit(){
	kupu.prepareForm(document.forms[myFacesKupuFormId], myFacesKupuClientId);
	if( myFacesKupuOriginalDocOnSubmit )
		return myFacesKupuOriginalDocOnSubmit();
}

// Redefine or extend buggy kupu functions

function openPopup(url, width, height){
    /* open and center a popup window */
    var sw = screen.width;
    var sh = screen.height;
    var left = sw / 2 - width / 2;
    var top = sh / 2 - height / 2;
    var win = window.open(myFacesKupuResourceBaseURL+url, 'someWindow', 
                'width=' + width + ',height=' + height + ',left=' + left + ',top=' + top);
    return win;
}