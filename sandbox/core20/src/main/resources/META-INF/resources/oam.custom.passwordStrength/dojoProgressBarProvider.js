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
    org.apache.myfaces.dojoProgressbarProvider = function(passwordStrength) {
    };
    /**
     * increases the progress bar value
     * @param passwordStrength
     * @param progressBarID
     * @param degree
     */
    org.apache.myfaces.dojoProgressbarProvider.prototype.increaseProgressBar = function (progressBarID, degree) {
        var currentValue = degree * 10;
        dojo.widget.byId(progressBarID).setMaxProgressValue(10, true);
        dojo.widget.byId(progressBarID).setProgressValue(currentValue, true);
        dojo.widget.byId(progressBarID).render();
    };
}
;