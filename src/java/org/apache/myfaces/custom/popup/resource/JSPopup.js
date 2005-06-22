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
    var x = 0;

    if (document.body && document.body.scrollLeft && !isNaN(document.body.scrollLeft)) {
        x = document.body.scrollLeft;
    }

    return x;
}

function orgApacheMyfacesPopupGetScrollingY() {

    var y = 0;

    if (document.body && document.body.scrollTop && !isNaN(document.body.scrollTop)) {
        y = document.body.scrollTop;
    }

    return y;
}