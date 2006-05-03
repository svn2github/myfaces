dojo.provide("org.apache.myfaces.pagelet.MyFacesHiddenRichText");

dojo.require("dojo.widget.*");
dojo.require("dojo.dom");
dojo.require("dojo.html");
dojo.require("dojo.event.*");
dojo.require("dojo.style");
dojo.require("dojo.widget.HtmlRichText");

/**
* a helper rich text inliner based on a original hidden field
* this is handy for controls which do not want to be based on
* text areas for form value traversal
* TODO move this into its own package
* once the dojo allows packaged components
*/
org.apache.myfaces.pagelet.MyFacesHiddenRichText = function() {
	dojo.widget.HtmlRichText.call(this);	
	dojo.debug("hiddenrichie");
	this.widgetType = "MyFacesHiddenRichText";
	this.changeNotified = false;
};

/**
* lets kick in the inheritance
*/
dojo.inherits(org.apache.myfaces.pagelet.MyFacesHiddenRichText, dojo.widget.HtmlRichText);
dojo.widget.tags.addParseTreeHandler("dojo:myfaceshiddenrichtext");


dojo.lang.extend(org.apache.myfaces.pagelet.MyFacesHiddenRichText, {
	
	/**
	 * Transforms the node referenced in this.domNode into a rich text editing
	 * node. This can result in the creation and replacement with an <iframe> if
	 * designMode is used, an <object> and active-x component if inside of IE or
	 * a reguler element if contentEditable is available.
	 */
	open: function (element) {
		var html = "";
		dojo.event.topic.publish("dojo.widget.RichText::open", this);

		if (!this.isClosed) { this.close(); }
		this._content = "";
		if (arguments.length == 1) { this.domNode = element; } // else unchanged
		/*todo reduce the code duplication by figuring out on how 
		to call the super methods*/
		if (this.domNode.nodeName == "INPUT" || this.domNode.nodeName == "TEXTAREA") {
			this.textarea = this.domNode;
			this.domNode = document.createElement("div");
			with(this.textarea.style){
				display = "block";
				position = "absolute";
				width = "1px";
				height = "1px";
				border = margin = padding = "0px";
				visiblity = "hidden";
			}
			dojo.dom.insertBefore(this.domNode, this.textarea);
			//this.domNode.innerHTML = html;
			
			if(this.textarea.form){
				dojo.event.connect(this.textarea.form, "onsubmit", 
					dojo.lang.hitch(this, function(){
						this.textarea.value = this.getEditorContent();
					})
				);
			}
			/*we add a helper to the textarea so that we can keep em in sync*/
			this.textarea.setEditorContent = dojo.lang.hitch(this.textarea, function(val) {
				this.setEditorContent(val);
			});			
			/*
			we also add a geteditorcontent function so that we are in sync
			also at the get stage
			*/
			this.textarea.getEditorContent = dojo.lang.hitch(this.textarea, function() {
				var val = this.getEditorContent();
				this.textarea.value = val;
				return val;
			});			
			
			
			
			// dojo plucks our original domNode from the document so we need
			// to go back and put ourselves back in
			var editor = this;
			dojo.event.connect(this, "postCreate", function (){
				dojo.dom.insertAfter(editor.textarea, editor.domNode);
			});
			html = this.textarea.value;
		} else {
			html = this.domNode.innerHTML;
		}
				
		this._oldHeight = dojo.style.getContentHeight(this.domNode);
		this._oldWidth = dojo.style.getContentWidth(this.domNode);
		
		this.savedContent = document.createElement("div");
		while (this.domNode.hasChildNodes()) {
			this.savedContent.appendChild(this.domNode.firstChild);
		}
		
		// If we're a list item we have to put in a blank line to force the
		// bullet to nicely align at the top of text
		if (this.domNode.nodeName == "LI") { this.domNode.innerHTML = " <br>"; }
				
		if (this.saveName != "") {
			var saveTextarea = document.getElementById("dojo.widget.RichText.savedContent");
			if (saveTextarea.value != "") {
				var datas = saveTextarea.value.split(this._SEPARATOR);
				for (var i = 0; i < datas.length; i++) {
					var data = datas[i].split(":");
					if (data[0] == this.saveName) {
						html = data[1];
						datas.splice(i, 1);
						break;
					}
				}				
			}
			this.connect(window, "onunload", "_saveContent");
		}

		// Safari's selections go all out of whack if we do it inline,
		// so for now IE is our only hero
		//if (typeof document.body.contentEditable != "undefined") {
		if (this.useActiveX && dojo.render.html.ie) { // active-x
			this._drawObject(html);
		} else if (dojo.render.html.ie) { // contentEditable, easy
			this.editNode = document.createElement("div");
			with (this.editNode) {
				contentEditable = true;
				innerHTML = html;
				style.height = this.minHeight;
			}
			this.domNode.appendChild(this.editNode);
			
			var events = ["onBlur", "onFocus", "onKeyPress",
				"onKeyDown", "onKeyUp", "onClick"];
			for (var i = 0; i < events.length; i++) {
				this.connect(this.editNode, events[i].toLowerCase(), events[i]);
			}
		
			this.window = window;
			this.document = document;
			
			this.onLoad();
		} else { // designMode in iframe
			this._drawIframe(html);
		}

		// TODO: this is a guess at the default line-height, kinda works
		if (this.domNode.nodeName == "LI") { this.domNode.lastChild.style.marginTop = "-1.2em"; }
		dojo.html.addClass(this.domNode, "RichTextEditable");
		
		this.isClosed = false;
		if(dojo.render.html.ie)
			dojo.event.connect(this.editNode, "onkeydown",this, "keydown"); 
	
	
	}, 
	
	
	setEditorContent: function(val){
		var ec = val;
		dojo.lang.forEach(this.contentFilters, function(ef){
			ec = ef(ec);
		});
		
		try{
			if(this._content.length > 0)
				this._content = ec;
			else
				this.editNode.innerHTML = ec;
			this.textarea.value = this.getEditorContent();
		}catch(e){ /* squelch */ }
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
            	if (this.editNode.parentElement.contains(range.parentElement())) {
                	range.pasteHTML("<br>");
                	range.select();
            	} 	
            }
		} else if(evt.keyCode == 13) {//ctrl enter now is paragraph, I hate IE
			evt.preventDefault();
			//since this has to work on ie only
			if (document.selection) {
            	var range = document.selection.createRange();
            	if (this.editNode.parentElement.contains(range.parentElement())) {
                	range.pasteHTML("<p></p>");
                	range.select();
            	} 	
            }
		}
	}
});