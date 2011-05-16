/**
 * Generic progress bar provider which provides the progress bar for the dojo toolkit
 */
if ('undefined' == typeof(org)) {
    org = {};
}
if ('undefined' == typeof(org.apache)) {
    org.apache = {};
}
if ('undefined' == typeof(org.apache.myfaces)) {
    org.apache.myfaces = {};
}
if ('undefined' == typeof(org.apache.myfaces.dojoProgressbarProvider)) {
    /**
     * Encapsulation class
     * for the refactured code
     * from the main class
     *
     */
    org.apache.myfaces.dojoProgressbarProvider = function() {
    };
    /**
     * increases the progress bar value
     * @param passwordStrength
     * @param progressBarID
     * @param degree
     */
    org.apache.myfaces.dojoProgressbarProvider.prototype.increaseProgressBar = function (progressBarID, degree) {
        var indicatorOuter = document.getElementById(progressBarID + "_CONTAINER");
        var indicatorInner = document.getElementById(progressBarID);
        /*fetch the width as numeric value*/
        var maxWidth = parseInt(indicatorOuter.offsetWidth);
        var currentValue = Math.round(maxWidth * degree);//FIXME degree
        if (degree == 1) {
            indicatorInner.className = "progressStrong";
            indicatorInner.style.width = maxWidth + "px";
        } else if (0.75 > degree && degree > 0.25) {
            indicatorInner.className = "progressVeryWeak";
            indicatorInner.style.width = currentValue + "px";
        } else if (1 > degree && degree > 0.75) {
            indicatorInner.className = "progressWeak";
            indicatorInner.style.width = currentValue + "px";
        } else {
            indicatorInner.className = "progressPoor";
            indicatorInner.style.width = currentValue + "px";
        }
    };
}
