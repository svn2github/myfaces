window.org = window.org || {};
org.apache = org.apache || {};
org.apache.myfaces = org.apache.myfaces || {};

if (!org.apache.myfaces.JSListener) {

    org.apache.myfaces.JSListener = function () {
    };
    org.apache.myfaces.JSListener.setExpressionProperty = function (srcId, destId, property, expression) {
        var log = true;
        var logStr = "";

        try {

            if (log) logStr += "Source-Element id: " + srcId;

            var srcElem = document.getElementById(srcId);

            if (log) logStr += "\n Source-element: " + srcElem;
            if (log) logStr += "\n Type of source-element: " + typeof(srcElem);

            if (log) logStr += "\n\n  Destination-element id: " + destId;

            var destElem = document.getElementById(destId);

            if (log) logStr += "\n Destination-element: " + destElem;
            if (log) logStr += "\n Type of destination-element: " + typeof(destElem);

            if (log) logStr += "\n\n  Expression before parsing: " + expression;

            var replaceMacro = org.apache.myfaces.JSListener.replaceMakro;
            expression = replaceMacro(expression, "srcElem", srcElem);
            expression = replaceMacro(expression, "destElem", destElem);

            if (log) logStr += "\n Expression after parsing: " + expression;

            var value = eval(expression);

            if (property) {
                var destElemStr = "destElem.";

                var valueStr;

                if (typeof (value) == 'string') {
                    valueStr = "'" + value + "'";
                }
                else {
                    valueStr = value;
                }

                var propertySetStr = destElemStr + property + "=" + valueStr + ";";

                if (log) logStr += "\n\n  Property set string: " + propertySetStr;

                eval(propertySetStr);
            }
        }
        catch (e) {
            var errorString = 'Error encountered : ';
            errorString += e['message'];
            errorString += logStr;

            if (document.all) {
                e['description'] = errorString;
                throw e;
            }
            else {
                throw errorString;
            }
        }
    };

    //orgApacheMyfacesJsListenerReplaceMakro
    org.apache.myfaces.JSListener.replaceMakro = function(expression, macroName/*, elem*/) {

        var regEx = new RegExp("\\$" + macroName, "g");
        expression = expression.replace(regEx, macroName);

        /*    if(orgApacheMyfacesJsListenerIsArray(elem))
         {
         var arrRegEx = new RegExp("\\$"+macroName+"[\\[]([0-9]+)[\\]]", "g");
         expression = expression.replace(arrRegEx,macroName+"[$1]");
         expression = expression.replace(regEx,macroName+"[0]");
         }
         else
         {*/
//    }

        return expression;
    };

}