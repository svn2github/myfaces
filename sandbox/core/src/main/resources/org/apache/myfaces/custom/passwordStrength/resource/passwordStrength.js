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

window.onload=function() {
	hide('indicatorMessage');
}

//This method is used for online updating the status of the textbox ...
function updateStatusValue(txtName, preferredLength, prefixText, passwordDesc, showDetails) {
    //Define the variables ...
    var content = document.getElementById(txtName).value;
    var currentStatus = prefixText;
    var diff = 0;
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
    //Show the result messages ...
    if(document.all){ 
           document.getElementById("indicatorMessage").innerText = currentStatus;
    } else {
           document.getElementById("indicatorMessage").textContent = currentStatus;
    }
    if(content.length == 0) {
        hide("leftCharsMessage");           
    } else if(showDetails == "true" && content.length < preferredLength) {
        show("leftCharsMessage");
	diff = (preferredLength - content.length);
	var charLeft = diff + " characters are left";
      if(document.all) { 
             document.getElementById("leftCharsMessage").innerText = charLeft;
      } else {
             document.getElementById("leftCharsMessage").textContent = charLeft;
      }
    } else {
        hide("leftCharsMessage");
    }
}

