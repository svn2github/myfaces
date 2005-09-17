var orgApacheMyfacesPopupCurrentlyOpenedPopup;

function orgApacheMyfacesPopup(popupId,displayAtDistanceX,displayAtDistanceY)
{
    this.popupId = popupId;
    this.displayAtDistanceX=displayAtDistanceX;
    this.displayAtDistanceY=displayAtDistanceY;    
    this.display = orgApacheMyfacesPopupDisplay;
    this.hide = orgApacheMyfacesPopupHide;
    this.redisplay=orgApacheMyfacesPopupRedisplay;
}

function orgApacheMyfacesPopupDisplay(ev)
{

    if(orgApacheMyfacesPopupCurrentlyOpenedPopup!=null)
        orgApacheMyfacesPopupCurrentlyOpenedPopup.style.display="none";

    var elem;
    var x;
    var y;

    if(document.all)
    {
        elem = window.event.srcElement;
        x=window.event.clientX;
        x+=orgApacheMyfacesPopupGetScrollingX();
        y=window.event.clientY;
        y+=orgApacheMyfacesPopupGetScrollingY();
    }
    else
    {
        elem = ev.target;
        x=ev.pageX;
        y=ev.pageY;
    }

    x+=this.displayAtDistanceX;
    y+=this.displayAtDistanceY;

    var popupElem = document.getElementById(this.popupId);

    if(popupElem.style.display!="block")
    {
        popupElem.style.display="block";
        popupElem.style.left=""+x+"px";
        popupElem.style.top=""+y+"px";
        orgApacheMyfacesPopupCurrentlyOpenedPopup = popupElem;
    }
}

function orgApacheMyfacesPopupHide()
{
    var popupElem = document.getElementById(this.popupId);
    popupElem.style.display="none";
}

function orgApacheMyfacesPopupRedisplay()
{
    var popupElem = document.getElementById(this.popupId);
    popupElem.style.display="block";
    orgApacheMyfacesPopupCurrentlyOpenedPopup = popupElem;
}

function orgApacheMyfacesPopupGetScrollingX() {
    if (self.pageXOffset) {
        return self.pageXOffset;
    } else if (document.documentElement && document.documentElement.scrollLeft) {
        return document.documentElement.scrollLeft;
    } else if (document.body) {
        return document.body.scrollLeft;
    } else {
        return 0;
        }
}

function orgApacheMyfacesPopupGetScrollingY() {
    if (self.pageYOffset) {
        return self.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
        return document.documentElement.scrollTop;
    } else if (document.body) {
        return document.body.scrollTop;
    } else {
        return 0;
    }
}