
/**
		we have double code I know, todo clean this up with the pagelet.js
		*/
		function myfaces_shiftToTop(id) {
			try {
		    var element = dojo.byId(id);
		    var parent 	= element.parentNode;
		
		    if(parent == document.body) return;
		    parent.removeChild(element);
		    document.body.insertBefore(element, document.body.firstChild);
		    } catch (e) {};//we ignore error messages because it is uncritical
		    //and we sometimes run into situations where we do not need it
		}
		
		/**
		hide function for our own dialog system
		(dojos seem to freak out the ie over huge stress)
		*/
		function hidRawTextDialog(bindingVar, dialogDivId) {
			if(bindingVar.bgIframe != null)
 				bindingVar.bgIframe.hide();
			dojo.html.hide(dialogDivId);
		}
	
		/**
		show function for our own dialog system
		(dojos seem to freak out the ie over huge stress)
		*/
		function showRawTextDialog(bindingVar, dialogDivId, textAreaId) {
			myfaces_shiftToTop(dialogDivId);
			//myfaces_shiftToBody(dialogDivId);
	
			var textArea = dojo.byId(textAreaId);
		    var viewport_size 	= dojo.html.getViewportSize();
    		
		    textArea.style.width 	= (parseInt(viewport_size[0] ) - 80)+"px";
		    textArea.style.height	= (parseInt(viewport_size[1] ) - 80)+"px";
		
			var dialog = dojo.byId(dialogDivId);
							            
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
			    
			if(!bindingVar.bgIframe)
			   bindingVar.bgIframe 	= new dojo.html.BackgroundIframe();
			   
			bindingVar.bgIframe.show([0,0, parseInt(viewport_size[0]), parseInt(viewport_size[1])]);
			bindingVar.bgIframe.setZIndex(1);
			            
			dialog.style.zIndex 	= 3;
			dialog.style.visibility = "visible";
			dojo.fx.html.fadeShow(dialog, 200);
		}

