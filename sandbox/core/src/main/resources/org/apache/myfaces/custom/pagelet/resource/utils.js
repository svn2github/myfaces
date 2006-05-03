
/**
* resizing function
*/
function resizeTa(id, value, max) {
    ta = document.getElementById(id);
    if (ta == null) {
        return;
    }
    
    //Set the value to the maximum value if max is not set
    if(max == null || max <= 0) {
    	max = ta.clientHeight;
    }
    
    if (ta.style.height == "") {
        ta.style.height = value + "px";
    }
    if ((parseInt(ta.style.height) + value) <= max) {
        ta.style.height = (parseInt(ta.style.height) + value) + "px";
    }
    return false;
}
/**
* textarea downsize
*/
function downsizeTa(id, value, min) {
    ta = document.getElementById(id);
    if (ta == null) {
        return;
    }
    
    if (ta.style.height == "") {
        ta.style.height = min + "px";
    }
    if ((parseInt(ta.style.height) - value) > min) {
        ta.style.height = (parseInt(ta.style.height) - value) + "px";
    } else
    	ta.style.height = min + "px";
    return false;
}
/**
* functional zoom
* id component id
* maxSizeX maxSize horizontally
* maxSizeY maxSize vertically
* stepSize the animation step size
*/
function myfaces_zoomTa(id, maxSizeX, maxSizeY, stepSize) {
    ta = document.getElementById(id);
    
    
    if (ta == null) {
        return false;
    }
	if(maxSizeX == null)
		maxSizeX = ta.clientWidth;
	if(maxSizeY == null)
		maxSizeY = ta.clientHeight;

    ta.style.width = maxSizeX + "px";
    ta.style.height = maxSizeY + "px";
    return false;
}
/**
* functional shrink
* id component id
* maxSizeX maxSize horizontally
* maxSizeY maxSize vertically
* stepSize the animation step size
*/
function myfaces_shrinkTa(id, minSizeX, minSizeY, stepSize) {
    ta = document.getElementById(id);
    if (ta == null) {
        return false;
    }
    ta.style.width = minSizeX + "px";
    ta.style.height = minSizeY + "px";
    return false;
}
/**
* simple toogle zoom which does the zoom
* and shrink within one click
* @param id the id of the component to avoid browser leakage
* @param maxSizeX the horizontal toggle max size
* @param maxSizeY the vertical toggle max size
* @steps placeholder for an animation steps param 
*/
function myfaces_toggleZoomTa(id, maxSizeX, maxSizeY, steps) {
    ta = document.getElementById(id);
    if (ta == null) {
        return false;
    }
    if (ta["zoomed"] == true) {
        myfaces_shrinkTa(id, ta["minSizeX"], ta["minSizeY"], steps);
        ta["zoomed"] = false;
    } else {
    	ta["minSizeX"] = parseInt(ta.style.width);
    	ta["minSizeY"] = parseInt(ta.style.height);
    	
    	/*we cannot shrink a component with a mathMax value smaller than its size*/
        myfaces_zoomTa(id, myfaces_mathMax(ta["minSizeX"], maxSizeX),myfaces_mathMax(ta["minSizeY"], maxSizeY), steps);
        ta["zoomed"] = true;
    }
    return false;
}
/**
* helper math functions
*/
function myfaces_mathMin(val1, val2) {
    return (val1 < val2) ? val1 : val2;
}
function myfaces_mathMax(val1, val2) {
    return (val1 > val2) ? val1 : val2;
}
/**
* helper display functions
*/
function displayNone(id) {
    e = document.getElementById(id);
    if (e == null) {
        return;
    }
    e.style.display = "none";
}
function displayInline(id) {
    e = document.getElementById(id);
    if (e == null) {
        return;
    }
    e.style.display = "inline";
}
function displayBlock(id) {
    e = document.getElementById(id);
    if (e == null) {
        return;
    }
    e.style.display = "block";
}
function displayFloat(id) {
    e = document.getElementById(id);
    if (e == null) {
        return;
    }
    e.style.display = "float";
}

