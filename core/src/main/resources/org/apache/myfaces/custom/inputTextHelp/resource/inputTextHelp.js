window.org = window.org || {};
org.apache = org.apache || {};
org.apache.myfaces = org.apache.myfaces || {};
if (!org.apache.myfaces.InputTextHelp) {
    org.apache.myfaces.InputTextHelp = function() {};

    org.apache.myfaces.InputTextHelp.resetHelpValue = function(helpText, id) {
        var element = document.getElementById(id);
        if (element.value == helpText) {
            element.value = "";
        }
    };

    org.apache.myfaces.InputTextHelp.selectText = function(helpText, id) {
        var element = document.getElementById(id);
        if (element.value == helpText) {
            element.select();
        }
    };
}