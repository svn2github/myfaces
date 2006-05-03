dojo.hostenv.setModulePrefix("org.apache.myfaces.pagelet", "../spellchecker.AjaxInputSpellcheckRenderer");
dojo.widget.manager.registerWidgetPackage("org.apache.myfaces.pagelet"); 

dojo.provide("org.apache.myfaces.pagelet.Progressor");	
/*=====================================================
 * class for a a generic progress
 * handler
 * this class currently shows a floating
 * div and blends it out
 *====================================================*/
org.apache.myfaces.pagelet.Progressor = function() {
	dojo.widget.HtmlWidget.call(this);
	/**************************************************
	* shows the progress text
	*
	* @param text the progress text to be shown
	**************************************************/
    this.showInProgress = function (text) {
	    
	    //ie bleed through fix
	    if(!this.bgIframe)
	    	this.bgIframe 	= new dojo.html.BackgroundIframe();
	 
        var inprogress = dojo.byId("dojoDialog_inprogress");
       	myfaces_shiftToBody("dojoDialog_inprogress");
        inprogress.style.position = "absolute";
        if (inprogress === null) {
            return;
        }
        if (text !== null) {
            inprogress.innerHTML = text;
        }
	    
	    //visible neede otherwise fade show does not work	
        var scroll_offset 	  = dojo.html.getScrollOffset();
        inprogress.style.left = (parseInt(scroll_offset[0])+ 20)+"px" 
        inprogress.style.top  = (parseInt(scroll_offset[1])+20) +"px";
        
        var w = dojo.style.getOuterWidth(inprogress);
		var h = dojo.style.getOuterHeight(inprogress);
        
        inprogress.style.visibility = "visible";
        dojo.fx.html.fadeShow(inprogress, 300);
        this.bgIframe.show([0,0,w,h]);
		this.bgIframe.setZIndex(98);
		inprogress.style.zIndex = 99;
		
    };
		    
    /**
    * hides the progress text
    */
    this.hideInProgress = function () {
        var inprogress = dojo.byId("dojoDialog_inprogress");
        if (inprogress === null) {
            return;
        }
        dojo.fx.html.fadeHide(inprogress, 300);
        inprogress.style.visibility = "hidden";
        this.bgIframe.hide();
    };
}
dojo.inherits(org.apache.myfaces.pagelet.Progressor, dojo.widget.HtmlWidget);