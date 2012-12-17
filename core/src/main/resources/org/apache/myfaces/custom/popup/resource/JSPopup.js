window.org = window.org || {};
org.apache = org.apache || {};
org.apache.myfaces = org.apache.myfaces || {};

//org.apache.myfaces.popupCurrentlyOpenedPopup;
//org.apache.myfaces.popupFrameUnder;
if (!org.apache.myfaces.Popup) {

    org.apache.myfaces.popupFrameUnder = null;
    org.apache.myfaces.popupCurrentlyOpenedPopup = null;

    org.apache.myfaces.Popup = function (popupId, displayAtDistanceX, displayAtDistanceY) {
        this.popupId = popupId;
        this.displayAtDistanceX = displayAtDistanceX;
        this.displayAtDistanceY = displayAtDistanceY;
        this.isIE6 = document.all && !window.opera && !window.XMLHttpRequest;
    };                              

    org.apache.myfaces.Popup.prototype.display = function (ev) {

        if (org.apache.myfaces.popupCurrentlyOpenedPopup != null)
            org.apache.myfaces.popupCurrentlyOpenedPopup.style.display = "none";

        //var elem;
        var x;
        var y;

        if (window.event) /*ie eventing system active*/
        {
            if (org.apache.myfaces.popupFrameUnder != null)
                org.apache.myfaces.popupFrameUnder.style.display = "none";

            //elem = window.event.srcElement;
            x = window.event.clientX;
            x += this.popupGetScrollingX();
            y = window.event.clientY;
            y += this.popupGetScrollingY();
        }
        else {
            //elem = ev.target;
            x = ev.pageX;
            y = ev.pageY;
        }

        x += this.displayAtDistanceX;
        y += this.displayAtDistanceY;

        var popupElem = document.getElementById(this.popupId);

        if (popupElem.style.display != "block") {
            popupElem.style.display = "block";
            popupElem.style.left = "" + x + "px";
            popupElem.style.top = "" + y + "px";
            org.apache.myfaces.popupCurrentlyOpenedPopup = popupElem;
        }
        this.ieIFrameFix();
    };

    /**
     * hide function which
     * hides the popup from the browser
     */
    org.apache.myfaces.Popup.prototype.hide = function () {
        var popupElem = document.getElementById(this.popupId);
        popupElem.style.display = "none";

        if (document.all && (org.apache.myfaces.popupFrameUnder != null)) { /*ie specific popup under fix*/
            org.apache.myfaces.popupFrameUnder.style.display = "none";
        }
        this.ieIFrameFix();
    };

    /**
     * simple redisplay
     * displays an already existing hidden popup
     */
    org.apache.myfaces.Popup.prototype.redisplay = function () {
        var popupElem = document.getElementById(this.popupId);
        popupElem.style.display = "block";
        org.apache.myfaces.popupCurrentlyOpenedPopup = popupElem;
        this.ieIFrameFix();
    };

    org.apache.myfaces.Popup.prototype.popupGetScrollingX = function () {
        if (self.pageXOffset) {
            return self.pageXOffset;
        } else if (document.documentElement && document.documentElement.scrollLeft) {
            return document.documentElement.scrollLeft;
        } else if (document.body) {
            return document.body.scrollLeft;
        } else {
            return 0;
        }
    };

    org.apache.myfaces.Popup.prototype.popupGetScrollingY = function () {
        if (self.pageYOffset) {
            return self.pageYOffset;
        } else if (document.documentElement && document.documentElement.scrollTop) {
            return document.documentElement.scrollTop;
        } else if (document.body) {
            return document.body.scrollTop;
        } else {
            return 0;
        }
    };

    /**
     * fix for the div over control bug in ie6
     */
    org.apache.myfaces.Popup.prototype.ieIFrameFix = function () {
        if (this.isIE6) { //IE6 only
            if (org.apache.myfaces.popupCurrentlyOpenedPopup == null) return false;
            var iframe = document.getElementById(org.apache.myfaces.popupCurrentlyOpenedPopup.id + "_IFRAME");

            if (iframe == null) {
                org.apache.myfaces.popupFrameUnder = document.createElement("<iframe src='javascript:false;' id='" + org.apache.myfaces.popupCurrentlyOpenedPopup.id + "_IFRAME' style='visibility:hidden; position: absolute; top:0px;left:0px; filter:alpha(Opacity=0);' frameborder='0' scroll='none' />");
                document.body.insertBefore(org.apache.myfaces.popupFrameUnder);
            } else {
                org.apache.myfaces.popupFrameUnder = iframe;
            }

            var popup = org.apache.myfaces.popupCurrentlyOpenedPopup;
            iframe = org.apache.myfaces.popupFrameUnder;

            if (popup != null &&
                    (popup.style.display == "block")) {

                popup.style.zIndex = 99;
                iframe.style.zIndex = popup.style.zIndex - 1;
                iframe.style.width = popup.offsetWidth;
                iframe.style.height = popup.offsetHeight;
                iframe.style.top = popup.style.top;
                iframe.style.left = popup.style.left;

                iframe.style.marginTop = popup.style.marginTop;
                iframe.style.marginLeft = popup.style.marginLeft;
                iframe.style.marginRight = popup.style.marginRight;
                iframe.style.marginBottem = popup.style.marginBottom;

                iframe.style.display = "block";
                iframe.style.visibility = "visible";
                /*we have to set an explicit visible otherwise it wont work*/

            } else {
                iframe.style.display = "none";
            }
        }
        return false;
    };

}