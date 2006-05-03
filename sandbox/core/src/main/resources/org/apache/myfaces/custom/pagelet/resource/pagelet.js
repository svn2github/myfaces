/*
	Copyright (c) 2004-2005, Irian GesmbH
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/**
* first initialize our package as dojo subpackage upon
* our resource loading criterion
*
*/

dojo.provide("org.apache.myfaces.pagelet.PageletEditor");
dojo.require("org.apache.myfaces.pagelet.Progressor");
/*
new rich edit from div

oncreate
hitch the value from the hidden field

change the onsubmits of the of the hidden
field so that the values are traversed before
the old submit

set the hidden field that onvaluechange is plastered
into the rich text

*/

org.apache.myfaces.pagelet.PageletEditor = function() {
	//dojo.widget.html.Editor = function() {
	
	dojo.widget.html.Editor.call(this);
	this.widgetType = "PageletEditor";
	
}
dojo.inherits(org.apache.myfaces.pagelet.PageletEditor, dojo.widget.html.Editor);
dojo.lang.extend(org.apache.myfaces.pagelet.PageletEditor, {
	
	insertToolbar: function(tbNode, richTextNode) {
		//interception point for the toolbar
		//here we can add a scrolling div which shifts
		//the rich text node into the inner core
		
		
		//and then add the richtext node to the mix
		dojo.html.insertBefore(tbNode, richTextNode);
		//dojo.html.insertBefore(this._toolbarContainer.domNode, this._richText.domNode);
	},
	
	setRichText: function(richText) {
		if(this._richText && this._richText == richText) {
			dojo.debug("Already set the richText to this richText!");
			return;
		}

		if(this._richText && !this._richText.isClosed) {
			dojo.debug("You are switching richTexts yet you haven't closed the current one. Losing reference!");
		}
		this._richText = richText;
		//this._richTextContainer = document.createElement("div");
		//this._richTextContainer.style.overflow = "auto";

		//this._richText.domNode.style.overflow = 'auto';
		//this._richText.domNode.style.height="500px";
		

		dojo.event.connect(this._richText, "close", this, "onClose");
		dojo.event.connect(this._richText, "onLoad", this, "onLoad");
		dojo.event.connect(this._richText, "onDisplayChanged", this, "updateToolbar");
		
		
		if(dojo.render.html.ie)
			dojo.event.connect(this._richText.editNode, "onkeydown",this, "keydown"); 
	
		
		if(this._toolbarContainer) {
			this._toolbarContainer.enable();
			this.updateToolbar(true);
		}
			
	},
	
	focus: function() {
			//TODO push this into a timeout code for moz fixing
		if(this._richText != null && this._richText.editNode != null)
			this._richText.editNode.focus();		
		else
			dojo.lang.setTimeout(this,this.focus, 100);
	
	},
	
	/**
	* some key shifting here
	* due to demands by third parties
	* we have to shift the default ie behavior a little bit
	* in the enter key areas
	*/
	keydown: function(evt) {
		if(!this.changeNotified && typeof(myfaces_stateChange_notificationFields)!="undefined" && myfaces_stateChange_notificationFields != null) {
		  //first trigger the notifier we have a change
			var notifierArray = myfaces_stateChange_notificationFields;
		  	this.changeNotified = true;
		  	if(notifierArray != null) {
		  		for(var notifierField in notifierArray) {
		  		
		  			if(notifierArray[notifierField] != null) {
			  			notifierField = dojo.byId(notifierArray[notifierField]);
			  			notifierField.value = "true";
			  			
			  		}
		  		}
		  	}
		}	
		
		//enter now is enter
		if((!evt.ctrlKey) && evt.keyCode == 13) {
			//This is a fix for the input key handler in ie
			//so that it behaves in a sane way regarding line breaks
			evt.preventDefault();
			//since this has to work on ie only
			if (document.selection) {
            	var range = document.selection.createRange();
            	if (this._richText.editNode.parentElement.contains(range.parentElement())) {
                	range.pasteHTML("<br>");
                	range.select();
            	} 	
            }
		} else if(evt.keyCode == 13) {//ctrl enter now is paragraph, I hate IE
			evt.preventDefault();
			//since this has to work on ie only
			if (document.selection) {
            	var range = document.selection.createRange();
            	if (this._richText.editNode.parentElement.contains(range.parentElement())) {
                	range.pasteHTML("<p></p>");
                	range.select();
            	} 	
            }
		}
	}
  }	
);  



dojo.widget.tags.addParseTreeHandler("dojo:PageletEditor");

/**
* Pagelets
* pagelet javascripts
* @author Werner Punz
* @version 1.0 ie only
*/ 

/*==================Utility subsystem=================*/	
/******************************************************
* shifting the element to the body
* to fix the dreaded ie positioning 
* bug regarding fixed
*******************************************************/
function myfaces_shiftToBody(id) {
	try {
    var element = dojo.byId(id);
    var parent 	= element.parentNode;

    if(parent == document.body) return;
    parent.removeChild(element);
    document.body.insertBefore(element, document.body.firstChild);
    } catch (e) {};//we ignore error messages because it is uncritical
    //and we sometimes run into situations where we do not need it
}

/******************************************************
* sets the scroll offst of a given window
*******************************************************/
function setScrollOffset(scrollOffset) {
	window.scrollTo(scrollOffset[0], scrollOffset[1]);
}
	



		
/*===================Spellchecker subsystem==========================*/
/********************************************************************
* generic dojoed spellchecker class which does some spellchecking
* and does a fixup of the markup code with replacement
*
* @param parentRef a reference to the parent variable for callback reasons(Timer)
* @param contentDiv the div or edit field holding the content
* @param isRichEdit if true a rich edit control has to be shown (currently unused)
* @param spellurl   the spellchecking url
* @param spellMethid the spellchecker method (jsf method binding string)
**********************************************************************/
function myfaces_Spellchecker(parentRef, contentDivId, isRichEdit, spellurl, spellmethod) {
	this.parentRef 			= parentRef;
    this.contentDivId 		= contentDivId;
    this.spellcheckingTag 	= "<span class='myfaces_SpellingError' id='myfaces_SpellErr:spellErrCnt'>myFaces_contentNode</span>"
	this.spellmethod 		= spellmethod;

    if(spellurl != null)
    	this.searchUrl = spellurl + "?%{param1}";
    else	
    	this.searchUrl = "./org/apache/myfaces/ajax/spellchecker?%{param1}";

    myfaces_shiftToBody("dojoDialog_spellcheckResults");    	
    this.isRichEdit = isRichEdit;
    if(this.isRichEdit == null)
    	this.isRichEdit = false;
};
		      
/************************************************************
 * helper to fetch the content holder
 ************************************************************/
myfaces_Spellchecker.prototype.getContentHolder = function() {
	var contentHolder = null;
	if(!this.isRichEdit) {
		contentHolder = dojo.byId(this.contentDivId);	
    } else {
		contentHolder = dojo.widget.manager.getWidgetById(this.contentDivId);
       	contentHolder = contentHolder._richText;
    }
    if(contentHolder == null) dojo.debug("Error: there is no content holder for this id:"+this.contentDivId);
    return contentHolder;
}; 
		     
		       	
		     
/**********************************************************
 * convenience method for fetching the content
 * @returns the content of the current 
 * edit area
 **********************************************************/
myfaces_Spellchecker.prototype.getContent = function() {
 	var contentHolder 	= this.getContentHolder();
 		
 	var content 		=  null;
    if(contentHolder.getEditorContent != null)
    	content = contentHolder.getEditorContent();
    else if(contentHolder.value != null)
      	content = contentHolder.value;
    else	
    	content = contentHolder.innerHTML;
 	return content;
};
		 
		 
/*************************************************************
 * this method converts a found error keyword into a
 * href with a given styleclass for identification
 * @param errorString the found error which has to be tagged
 *************************************************************/
myfaces_Spellchecker.prototype.markAsError = function(errorString) {
      		
	var content 			=  this.getContent();	
	var correctionString	= this.spellcheckingTag.replace(/myFaces_contentNode/,errorString);
	var pattern 			= new RegExp("[^a-zA-Z_0-9]"+errorString+"[^a-zA-Z_0-9]","g"); //replaceall
    var contentHolder 		= this.getContentHolder();

    content 				= content.replace (pattern, correctionString.replace("spellErrCnt","_0"));
    if(contentHolder.editNode != null)
    	contentHolder.editNode.innerHTML = content;
    else if(contentHolder.value != null)
      	contentHolder.value = content;
    else
      	contentHolder.innerHTML = content;
};

/*****************************************************
 * an ajaxed mark as error
 * the calling link
 * is set via the object initialization
 * parameter
 *
 ******************************************************/
myfaces_Spellchecker.prototype.markAsErrorAjax = function(dummyparam) {
	var content 		=  this.getContent();
	/**
	* note we use a parametrized array
	* the first entry is the operation we want to achieve
	* the second one the content
	* the third+ one is an additional paramlist 
	* depending on the op
	*/
	var requestUri = dojo.string.paramString(this.searchUrl,{"param1": 
		"spck[]="+encodeURIComponent("spellcheck")+			//first param the commend
		"&spck[]="+encodeURIComponent(this.spellmethod)		//second param speller method
	});
	var _this = this;
	var progressor = new org.apache.myfaces.pagelet.Progressor();
	progressor.showInProgress("Spellchecking");
  	dojo.io.bind({
    	url		: requestUri,
    	method	: "post",
    	useCache:true,
    	content	: {"content": content},
    	load	:function(type, data, evt)  {
    		progressor.hideInProgress();
			
	
			
			var contentHolder 	= _this.getContentHolder();
			//mozilla rendering
			if(dojo.render.html.moz){
				//moz cannot handle the inline rich edit styleclass stuff for now
				//so we have to do it manually, since we do not have a replaceAll we have to do it manually
				var dataNew = data.replace("class\='myfaces_SpellingError'>", "class='myfaces_SpellingError' style='background-color: yellow;border: 1px solid #999;' >");
				while(dataNew != data) {
					data = dataNew;
					dataNew = data.replace("class\='myfaces_SpellingError'>", "class='myfaces_SpellingError' style='background-color: yellow;border: 1px solid #999;' >");
				} 
			}
	
			
         	if(contentHolder.editNode != null)
          		contentHolder.editNode.innerHTML = data;
         	else if(contentHolder.value != null)
          		contentHolder.value = data;
         	else
          		contentHolder.innerHTML = data;
        
			_this.activateSpellcheckingArr();   	
			        
		},
		error: function(type, data, evt) {
			progressor.hideInProgress();
		},
		mimetype: "text/plain",
		transport: "XMLHTTPTransport"
	});	
};
			
/*********************************************************
* ajax cleanup this basically passes the string and cleans
* up the remaining spellcheckign parts
* it uses the connection params from the object initializer
*
**********************************************************/		
myfaces_Spellchecker.prototype.cleanupAjax = function() {
	//var searchUrl = "http://localhost:8080/spellchecker/org/apache/myfaces/ajax/spellchecker?%{param1}";
	var content 		=  this.getContent();
        
	/**
	* note we use a parametrized array
	* the first entry is the operation we want to achieve
	* the second one the content
	* the third+ one is an additional paramlist 
	* depending on the op
	*/	        	 
	var requestUri = dojo.string.paramString(this.searchUrl,{"param1": 
	"spck[]="+encodeURIComponent("cleanup")+			//first param the comment
	"&spck[]="+encodeURIComponent(this.spellmethod)		//second param speller method
	});
				
				
	var _this = this;
	var progressor = new org.apache.myfaces.pagelet.Progressor();
  	progressor.showInProgress("Cleanup");
  		
	try {
     	dojo.io.bind({
		sync: true,
		url: requestUri,
		useCache:true,
		method: "POST",
		content: {"content": content},
	
		load:function(type, data, evt)  {
			 progressor.hideInProgress();
			 var contentHolder 	= _this.getContentHolder();
		         
	         if(contentHolder.editNode != null)
	          	contentHolder.editNode.innerHTML = data;
	         else if(contentHolder.value != null)
	          	contentHolder.value 	= data;
	         else
	          	contentHolder.innerHTML = data;
	
			if(dojo.render.html.moz) {
				//only in design mode off we can enable javascript for now
				//I dont know how the google people did it without
				contentHolder.document.designMode = "on";
			}
		},
		error: function(type, data, evt) {
			progressor.hideInProgress();
			dojo.debug("error-transport"+data); 
		},
		mimetype: "text/plain",
		transport: "XMLHTTPTransport"
		});	
	} catch (e) {
		progressor.hideInProgress();
		alert("Error:"+e.message);
	}
};

/**
 * some resetting from the 
 */				
myfaces_Spellchecker.prototype.reset = function() {

	if(dojo.render.html.moz) {
		var contentHolder = this.getContentHolder();
		//only in design mode off we can enable javascript for now
		//I dont know how the google people did it without
		contentHolder.document.designMode = "on";
	}
};			

/*************************************************************
* activates the spell checking areas
* for further processing
* this is called after the spellchecking marking
* it sets the event handlers of the spellcheck markers
* to the suggestion list parameters
**************************************************************/
myfaces_Spellchecker.prototype.activateSpellcheckingArr = function() {
	//we have the walk the hrefs and set the onclick handlers to
	//an ajax and popup trigger
	   				
	//now we walk the dom and dynamically readjust the elements		
	//this works only on ie unfortunately :-(
	var errorHrefs = dojo.html.getElementsByClass("myfaces_SpellingError", document,"*",0);
	for(var href in errorHrefs) {
		dojo.event.connect(errorHrefs[href], "onclick", this,"onCorrectionList");
	}
	//on moz we have to do it differently again, since we have a list of spelling errors
	//anyway which roots from 0... to somethign we can iterate over the ids
	if(dojo.render.html.moz){
		var contentHolder = this.getContentHolder();
		//only in design mode off we can enable javascript for now
		//I dont know how the google people did it without
		contentHolder.document.designMode = "off";
		
		
		var spellingErrCnt = 0;
		while(true) {
			var errId = "myfaces_SpellErr:" + spellingErrCnt;
			spellingErrCnt += 1;
			var errSpan = contentHolder.document.getElementById(errId);
			if(errSpan == null) return;
			dojo.event.connect(errSpan, "onclick", this,"onCorrectionList");
		}
	}
	
};
     
/***********************************************************
* convenience method to access the tooltip singleton
*
* @param parentid the id the tooltip has to process, in our
* 		     case a simple spellchecking marked area
************************************************************/
myfaces_Spellchecker.prototype.getToolTip = function(parentId) {
	var tip = dojo.widget.manager.getWidgetById("myfaces_Spcktooltip_dojoDialog_spellcheckResults");
    if(tip == null) {
    	myfaces_shiftToBody("dojoDialog_spellcheckResults");
                	
		tip = dojo.widget.createWidget("Tooltip",{id:("myfaces_Spcktooltip_dojoDialog_spellcheckResults"), 
		executeScripts:true, 
            
    	connectId:parentId},dojo.byId("dojoDialog_spellcheckResults"));
    }
    return tip;
};
      		
     
/*************************************************************
* trigger for an actiavation of a click to the
* on correction list within an error
*
* @param evt standard javascript event param passed by the js 
*			event subsystem
**************************************************************/
myfaces_Spellchecker.prototype.onCorrectionList = function(evt) {
   			
	if(evt.target.id != null) {  			
		var toolTip 	=   this.getToolTip(evt.target.id);
		var content 	=   evt.target.innerHTML;
		var _this 		= this;
		var _target		= evt.target;
		var jsonedData 	= null;
		var requestUri 	= dojo.string.paramString(this.searchUrl,{"param1": 
	    	"spck[]="+encodeURIComponent("showsuggestions")+	//first param the comment
			"&spck[]="+encodeURIComponent(this.spellmethod)		//second param speller method
	    	});
		var progressor = new org.apache.myfaces.pagelet.Progressor();

	  	progressor.showInProgress("Getting suggestions");
		try {
	     	dojo.io.bind({
			sync: true,
			url: requestUri,
			useCache:true,
			method: "POST",
			content: {"content": content,"targetid": (evt.target.id+"")},
			
			load:function(type, data, evt)  {
				progressor.hideInProgress();
				var jsonFunc = new Function("jsoner", "return "+data);
				jsonedData = jsonFunc();
			},
			
			error: function(type, data, evt) {

				dojo.debug("error-transport"+data); 
			},
			mimetype: "text/plain",
			transport: "XMLHTTPTransport"
			});	
		} catch (e) {
			progressor.hideInProgress();
			alert("Error:"+e.message);
		}
	   				   					
		toolTip.domNode.innerHTML = jsonedData["content"]+ "<li><div class='myfaces_SuggestEntry' id='"+evt.target.id+"_closecorr'>[close]</div></li>";
	    if(toolTip.displayed) {
	   		toolTip.erase();	
	   		toolTip.displayed = false;
	   	}
		   				
		/*display the tooltip with a slight correctional position compared to the original pos*/
		var scroll_offset 	= dojo.html.getScrollOffset();
		if(!dojo.render.html.ie) {
			
			//TODO mozilla iframe fixup for this one
			var contentHolder = this.getContentHolder();
			scroll_offset = [contentHolder.document.scrollLeft, contentHolder.document.scrollTop]
			//toolTip.mouseX 		= evt.pageX + scroll_offset[0]|| evt.clientX + scroll_offset[0];
			//toolTip.mouseY 		= evt.pageY + scroll_offset[1]|| evt.clientY + scroll_offset[1];
			scroll_offset 		= dojo.html.getScrollOffset();
			//toolTip.mouseX 		= evt.pageX + scroll_offset[0]|| evt.clientX + scroll_offset[0];
			//toolTip.mouseY 		= evt.pageY + scroll_offset[1]|| evt.clientY + scroll_offset[1];
			//TODO we have to walk all parents for the offset calc, here is a problem with
			//moz
			//alternatively we could use one of the corners
			
			//We can resolve this in ie 7
			//toolTip.domNode.style.position = "fixed";
		} else {
			toolTip.mouseX 		= evt.pageX + scroll_offset[0]|| evt.clientX + scroll_offset[0];
			toolTip.mouseY 		= evt.pageY + scroll_offset[1]|| evt.clientY + scroll_offset[1];
		}
		toolTip.displayed = false;
		toolTip.display();   						
						
		if(jsonedData["noEntries"] != null) {
			var noEntries = parseInt(jsonedData["noEntries"]);
	   		for(var cnt = 1; cnt <= noEntries; cnt++) {
		   		var targetHREF = dojo.byId(evt.target.id+"_corrid_"+cnt);
		   		//we need to store the ref cascade to trackback once the correction is triggered
		   		targetHREF["callerHrefId"] = evt.target.id;			
		   		dojo.event.connect(targetHREF,"onclick",_this,"onDoCorrection");
		   	}
		}
		dojo.event.connect(dojo.byId(evt.target.id+"_closecorr"),"onclick",this, "onHideToolTip");
	}	
};
   			
/***********************************************************
* hide the tooltip event callback
*
* @param evt the event param sent by the javascript event
*			subsystem
***********************************************************/
myfaces_Spellchecker.prototype.onHideToolTip = function(evt) {
	var scrollOffset 	= dojo.html.getScrollOffset();
	var tip 			= dojo.widget.manager.getWidgetById("myfaces_Spcktooltip_dojoDialog_spellcheckResults");
	
	if(tip !== null) {
		tip.erase();	
		tip.displayed = false;
	};
	setScrollOffset(scrollOffset);
	return false;
};
   			
/***********************************************************
* correction event callback
*
* @param evt the event handler param passed by the js event
* 			subsystem
************************************************************/
myfaces_Spellchecker.prototype.onDoCorrection = function(evt) {
	var targetId 		= evt.target["callerHrefId"];
	var scrollOffset 	= dojo.html.getScrollOffset();
	var toolTip 		= this.getToolTip(targetId);
	var target 			= dojo.byId(targetId);
	
	if(dojo.render.html.moz) { //we have the target in an iframe
		target = this.getContentHolder().document.getElementById(targetId);
		target.innerHTML 	= evt.target.innerHTML;
		//we have to do some tricks here because the dom editor does not like node shifting
		target.style.backgroundColor="";
		target.onClick = "";
	} else {
		target.outerHTML 	= evt.target.innerHTML;
	}
	
	
	
	toolTip.hide();
	setScrollOffset(scrollOffset);
	return false;
}
     
       
  
/*================ Dialog edit subsystem =================*/
/*========================================================
* we define an event callback class to 
* have a functionality encapuslation in a sane manner
* we have a general wrapper here to have a distinction between
* the spellchecker itself and the connections to the outer
* system, a more clear split was planned originally
* but for now we have this messy split here
* in the long run this has to be changed to something more clear
*
* @param parentRef the ref to the var needed for internal timers
* @param dlgId 	   the div dialogId which is referenced by the pagelet
* @param parentControlId the id of the parent control which has to be filled internally (usually a hidden field)
* @param dialogEditorId  the id of the internal dialog editor
* @param spellurl 		 the url for the spellchecker ajax call
* @param spellmethod	 the jsf spellmethod binding passthrough param
* @param readonly		 readonly
* @param controlMode	 the controlMode as defined by the pagelet component 
* itself (currently supported pagelet pagelet-richtext und pagelet-textarea und pagelet-textarea)
*=========================================================*/
function myfaces_Pagelet(parentRef, dlgid, parentControlId, dialogEditorId, spellurl, spellmethod, readonly, controlMode) {
    
    /*we reference only the ids to avoid tangeling control objects on ie*/
    this.dialogId 		= dlgid;
    this.parentRef 		= parentRef;
    this.parentId 		= parentControlId;
    this.dialogEditId 	= dialogEditorId;
    this.readonly 		= readonly;   	
    this.controlMode	= controlMode;
        	
	myfaces_shiftToBody(this.dialogId);
  
	//causes layout problems if not done, after subsequent posts
	dojo.byId(this.dialogId).style.visibility="hidden";
	dojo.byId(this.dialogId).style.position="absolute"; 
        	
	var parentContent 	= dojo.byId(this.parentId);
	var dialogEditor  	= dojo.byId(this.dialogEditId);
	var val 			= null;
	
	if(parentContent.value)
		val = parentContent.value;
	else 
		val = parentContent.innerHTML;
        			
    if(dialogEditor.value)
    	dialogEditor.value = val;
    else 
    	dialogEditor.innerHTML = val;			
  
	/*register a spellchecking object in a 0:1 reference*/
	/*which we will use later for dynamic spellcheck triggering*/
	/*and content aftercleaning*/
	this.spellchecker = new myfaces_Spellchecker(this.parentRef+".spellchecker", this.dialogEditId+"_dj", true, spellurl, spellmethod);
};


	
	
/*********************************************************************************
* method which returns the editor
*
* @param fixit adds a setContent method to the editor (which is missing currently)
* @param enforceCreation enforces the creation of a new editor
* 				even if an old one already was created
*********************************************************************************/
myfaces_Pagelet.prototype.getEditor = function(fixit, enforceCreation) {
	var editor = dojo.widget.manager.getWidgetById(this.dialogEditId+"_dj");

	if(editor == null) {
		var oldItemGroups = dojo.widget.html.Editor.itemGroups;
		
	
		
		//var theitems = [ "linkGroup",  "|", "textGroup", "blockGroup","|", "justifyGroup", "|", "listGroup", "indentGroup", "|", "colorGroup"];
        //todo move the items from here to jsf
        var theitems = [  "textGroup" ,"|", "justifyGroup"];
        if(this.controlMode == "pagelet-rawtext") {
        	theitems = [];
        }
        
        
        editor = dojo.widget.createWidget("PageletEditor", {id:(this.dialogEditId+"_dj"), items: theitems }, 
                                dojo.byId(this.dialogEditId));
 		
 	}
	if(fixit != null && fixit && editor.setEditorContent == null) {
    	editor.setEditorContent = function(text) {
	        if(editor._richText != null && editor._richText.editNode != null)
	            editor._richText.editNode.innerHTML = text;
	        else
	            dojo.debug("no rich text found"+this.dialogEditId);                        
	    }
	}
	return editor;
};	             
        
        	
/************************************************************
* event handler which shows the main dialog
* so that the text can be edited
* 
* @param evt standard event param
*************************************************************/
myfaces_Pagelet.prototype.onShowdlg = function (evt) {
	var dialog = dojo.byId(this.dialogId);
	myfaces_shiftToBody(this.dialogId);
            
	var scroll_offset 	= dojo.html.getScrollOffset();
    //ie fix
    dialog.style.position = "absolute";
    //if (dojo.render.html.ie) {//ie does not do fixed yet, we have to simulate
    dialog.style.left 	= (parseInt(scroll_offset[0])+ 20)+"px" 
    dialog.style.top 	= (parseInt(scroll_offset[1])+20) +"px";
    //}
    var viewport_size 	= dojo.html.getViewportSize();
            
    dialog.style.width 	= (parseInt(viewport_size[0] ) - 40)+"px";
    dialog.style.height	= (parseInt(viewport_size[1] ) - 40)+"px";
    
    if(!this.bgIframe)
    	this.bgIframe 	= new dojo.html.BackgroundIframe();
   
    this.bgIframe.show([0,0, parseInt(viewport_size[0]), parseInt(viewport_size[1])]);
	this.bgIframe.setZIndex(1);
            
            
    dialog.style.zIndex 	= 3;
    dialog.style.visibility = "visible";
            
    dojo.fx.html.fadeShow(dialog, 200);
            
    var editorDiv 			= dojo.byId(this.dialogEditId);
    editorDiv.style.width 	= (parseInt(viewport_size[0]) - 40)+"px";
    
    if(this.readonly != null && !this.readonly)
    	editorDiv.style.height	= (parseInt(viewport_size[1]) - 96)+"px";
    else if(dojo.render.html.ie) //Fixup for a layouting bug in ie 
    	editorDiv.style.height	= (parseInt(viewport_size[1]) - 50)+"px";
    else
    	editorDiv.style.height	= (parseInt(viewport_size[1]) - 70)+"px";
    
    editorDiv.style.overflow = "auto";
    
    
    var parent  			= dojo.byId(this.parentId);
            
    var editor = null;
    if(!this.readonly)
    	editor = this.getEditor(true);
            
           
    dojo.html.show(this.dialogId);
    dojo.html.show(this.dialogEditId);//keep it suppressed so that we do not regenerate it
  			
    var parentContent 	= null;
    
    //we probe for a rich text field first for getting the values
    parentContent =  dojo.widget.manager.getWidgetById(this.parentId+"RichText");
    if(parentContent == null)
    	parentContent = dojo.byId(this.parentId);
    
    var val 			= null;
    
    if(parentContent.getEditorContent) {    	
    	val = parentContent.getEditorContent();  
    	//alert(parentContent.getEditorContent());      
    } else if(parentContent.value) {
		val = parentContent.value;
	} else {
		val = parentContent.innerHTML;
	}

	dojo.lang.setTimeout(this, this.onDlgFill, 500, val);
	
};
 
myfaces_Pagelet.prototype.onDlgFill = function(val) {
   	if(this.readonly) {
   	//in this case we are preinitalized
   		var dialogEditor  	= dojo.byId(this.dialogEditId);
   		dialogEditor.innerHTML = val;
   		return;
   	}
   	
   	var editor = this.getEditor(true);
    
	if(editor.setEditorContent) {
    	editor.setEditorContent(val);
    	editor.focus();
    } else 
    	dojo.lang.setTimeout(this, this.onDlgFill, 200, val);
    		
}; 
 
    
/************************************************************
* event handler which closes the 
* editor dialog
*
* @param evt standard javascript event
*************************************************************/
myfaces_Pagelet.prototype.onDlgClose = function(evt) {
    var dialog = dojo.byId(this.dialogId);

    var parent =  dojo.widget.manager.getWidgetById(this.parentId+"RichText");
    if(parent == null)
    	parent = dojo.byId(this.parentId);
    
    /*lets ajax the cleanup (because in the end only the server knows the data structures)*/
    this.spellchecker.cleanupAjax();
    
	var editor = this.getEditor();
	
	if(parent.setEditorContent) {
   		parent.setEditorContent(editor.getEditorContent());
   	} else if(parent.value)
   		parent.value = editor.getEditorContent();
   	else	
   		parent.innerHTML = editor.getEditorContent();
    
    /*we need an optional backup control for the value binding*/
    /*TODO remove this binding legacy in favor of a generalized control approach*/
    var parentBack = dojo.byId(this.parentId+"_ta");
    
    if(parentBack != null )
    	parentBack.value = editor.getEditorContent();
    
    //ie bleed through fix
    if(this.bgIframe != null)
    	 this.bgIframe.hide();
    dojo.html.hide(this.dialogId);
};

myfaces_Pagelet.prototype.onDlgCancel = function(evt) {
    //ie bleed through fix
    this.spellchecker.reset();
    if(this.bgIframe != null)
    	 this.bgIframe.hide();
    dojo.html.hide(this.dialogId);
}

/*******************************************************
* standard spell check callback
********************************************************/     
myfaces_Pagelet.prototype.onSpellCheck = function() {
 	this.spellchecker.markAsErrorAjax("");
};
    
/*******************************************************
* standard cleanup callback
********************************************************/     
myfaces_Pagelet.prototype.onCleanup = function() {
	this.spellchecker.cleanupAjax();
};
