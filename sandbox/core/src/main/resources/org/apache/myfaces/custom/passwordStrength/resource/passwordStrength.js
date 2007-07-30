function hide(id) { 
   if (document.getElementById){ 
      obj = document.getElementById(id); 
      obj.style.display = "none"; 
   } 
}
function show(id) { 
   if (document.getElementById){ 
      obj = document.getElementById(id); 
      obj.style.display = ""; 
   }
}

function startUpPasswordStrength(objID) {
	hide(objID);
}

function increaseProgressBar(progressBarID, maxValue, currentValue){
	dojo.widget.byId(progressBarID).setMaxProgressValue(maxValue, true);
	dojo.widget.byId(progressBarID).setProgressValue(currentValue, true);
	dojo.widget.byId(progressBarID).render();
}

//This method is used for online updating the status of the textbox ...
function updateStatusValue(textID, preferredLength, 
						   prefixText, passwordDesc, 
						   indicatorMessageID, leftCharsMessageID, 
						   strengthIndicatorType, progressBarId, 
						   showDetails) {				   
    //Get the current message content ...
    var content = document.getElementById(textID).value;
    var currentStatus = prefixText;
   	var diff = 0;	     
   	   	
    /**
    * Check whether to display the strength indicator as a text or as bar ...
    */
    if(strengthIndicatorType == "text") {
	    //Check the contents ...   
	    var tokens = passwordDesc.split(";");
	    var length = tokens.length;
	    //Calc the delta of char changes : diff = preferredLength / tokens;
	    var delta = preferredLength / length;
	    var change = delta;
	    //Segmentize it ...
	    for(i = 0; i < length; ++i) {
	       //Get the correct tokenizer ...
	       if(content.length <= delta) {
	          currentStatus +=  tokens[i];
	          break;
	       } 
	       //If we reached the last character then always display the last value ...
	       if(i == length - 1) {
	          currentStatus +=  tokens[length-1];
	          break;	  
	       }
	       delta = change * (i + 2);
	    }    
	    if(document.all){ 
	           document.getElementById(indicatorMessageID).innerText = currentStatus;
	    } else {
	           document.getElementById(indicatorMessageID).textContent = currentStatus;
	    }
		
    }
    else { /*Here we are dealing with bar*/
		increaseProgressBar(progressBarId, preferredLength, content.length);   	    
    }
    /**
    * Here display the left characters message part ...
    */    
    if(content.length == 0) {
        hide(leftCharsMessageID);           
    } else if(showDetails == "true" && content.length < preferredLength) {
        show(leftCharsMessageID);
	diff = (preferredLength - content.length);
	var charLeft = diff + " characters are left";
      if(document.all) { 
             document.getElementById(leftCharsMessageID).innerText = charLeft;
      } else {
             document.getElementById(leftCharsMessageID).textContent = charLeft;
      }
    } else {
        hide(leftCharsMessageID);
    }						   						   
}


