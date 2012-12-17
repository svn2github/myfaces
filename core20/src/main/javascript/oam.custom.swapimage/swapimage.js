window.org = window.org || {};
org.apache = org.apache || {};
org.apache.myfaces = org.apache.myfaces || {};

if (!org.apache.myfaces.SwapImage) {
    org.apache.myfaces.SwapImage = function () {
    };

    org.apache.myfaces.SwapImage.restore = function () { //v3.0
        var i, x, a = org.apache.myfaces.SwapImage.MM_sr;
        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
    };

    org.apache.myfaces.SwapImage.swapImage = function () { //v3.0
        var i, j = 0, x, a = arguments;
        org.apache.myfaces.SwapImage.MM_sr = new Array;
        for (i = 0; i < (a.length - 2); i += 3) {
            if ((x = org.apache.myfaces.SwapImage.findObj(a[i])) != null) {
                org.apache.myfaces.SwapImage.MM_sr[j++] = x;
                if (!x.oSrc) x.oSrc = x.src;
                x.src = a[i + 2];
            }
        }
    };

    org.apache.myfaces.SwapImage.findObj = function (n, d) { //v4.01
        var p, i, x;
        if (!d) d = document;
        if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
            d = parent.frames[n.substring(p + 1)].document;
            n = n.substring(0, p);
        }
        if (!(x = d[n]) && d.all) x = d.all[n];
        for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
        for (i = 0; !x && d.layers && i < d.layers.length; i++) x = org.apache.myfaces.SwapImage.findObj(n, d.layers[i].document);
        if (!x && d.getElementById) x = d.getElementById(n);
        return x;
    };
}