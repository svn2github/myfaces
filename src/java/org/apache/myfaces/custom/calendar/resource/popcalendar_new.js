//	written	by Tan Ling	Wee	on 2 Dec 2001
//	last updated 20 June 2003
//	email :	fuushikaden@yahoo.com
//
// Modified to use the MyFaces lib resources

org_apache_myfaces_CalendarInitData = function()
{
    this.fixedX = -1;			// x position (-1 if to appear below control)
    this.fixedY = -1;			// y position (-1 if to appear below control)
    this.startAt = 1;			// 0 - sunday ; 1 - monday
    this.showWeekNumber = 1;	// 0 - don't show; 1 - show
    this.showToday = 1;		// 0 - don't show; 1 - show
    this.imgDir = "images/";			// directory for images ... e.g. this.imgDir="/img/"
    this.themePrefix = "jscalendar-DB";

    this.gotoString = "Go To Current Month";
    this.todayString = "Today is";
    this.weekString = "Wk";
    this.scrollLeftMessage = "Click to scroll to previous month. Hold mouse button to scroll automatically.";
    this.scrollRightMessage = "Click to scroll to next month. Hold mouse button to scroll automatically."
    this.selectMonthMessage = "Click to select a month."
    this.selectYearMessage = "Click to select a year."
    this.selectDateMessage = "Select [date] as date." // do not replace [date], it will be replaced by date.

    this.monthName = new Array("January","February","March","April","May","June","July","August","September","October","November","December");
    this.dayName = this.startAt==0 ? new Array("Sun","Mon","Tue","Wed","Thu","Fri","Sat") : new Array("Mon","Tue","Wed","Thu","Fri","Sat","Sun");

}

org_apache_myfaces_DateParts = function(sec,min,hour,date,month,year)
{
        this.sec = sec;
        this.min = min;
        this.hour = hour;
        this.date = date;
        this.month = month;
        this.year = year;
}

org_apache_myfaces_HolidayRec =function(d, m, y, desc)
{
	this.d = d;
	this.m = m;
	this.y = y;
	this.desc = desc;
}

org_apache_myfaces_PopupCalendar = function()
{
    this.idPrefix="org_apache_myfaces_PopupCalendar";

    this.selectedDate = new org_apache_myfaces_DateParts(0,0,0,0,0,0);
    this.saveSelectedDate = new org_apache_myfaces_DateParts(0,0,0,0,0,0);

    this.monthConstructed=false;
    this.yearConstructed=false;
    this.intervalID1;
    this.intervalID2;
    this.timeoutID1;
    this.timeoutID2;
    this.ctlToPlaceValue;
    this.ctlNow;
    this.dateFormat;
    this.nStartingYear;
    this.bPageLoaded=false;
    this.ie=document.all;
    this.dom=document.getElementById;
    this.ns4=document.layers;
    this.dateFormatSymbols = null;
    this.initData = new org_apache_myfaces_CalendarInitData();
    this.today = new Date();
    this.todayDateFormat = "dd.MM.yyyy";
    this.dateNow = this.today.getDate();
    this.monthNow = this.today.getMonth();
    this.yearNow = this.today.getYear();
    this.imgSrc = new Array("drop1.gif","drop2.gif","left1.gif","left2.gif","right1.gif","right2.gif");
    this.img = new Array();

    //elements which need to change their dynamical
    //representation over time
    this.calendarDiv;
    this.selectMonthDiv;
    this.selectYearDiv;
    this.todaySpan=null;
    this.captionSpan=null;
    this.monthSpan=null;
    this.yearSpan=null
    this.changeMonthImg=null;
    this.changeYearImg=null;

    this.holidaysCounter = 0;
    this.holidays = new Array();

    this.bShow = false;

    this.myFacesCtlType = "x:inputCalendar";
    this.inputDateClientId;
}

/* hides <select> and <applet> objects (for IE only) */
org_apache_myfaces_PopupCalendar.prototype._hideElement = function( elmID, overDiv ){
  if( this.ie ){
    for( i = 0; i < document.all.tags( elmID ).length; i++ ){
      obj = document.all.tags( elmID )[i];
      if( !obj || !obj.offsetParent )
        continue;

      // Find the element's offsetTop and offsetLeft relative to the BODY tag.
      objLeft   = obj.offsetLeft;
      objTop    = obj.offsetTop;
      objParent = obj.offsetParent;

	// this loop has been commented (MYFACES-870)
	/*
      while( objParent.tagName.toUpperCase() != "BODY" ){
        objLeft  += objParent.offsetLeft;
        objTop   += objParent.offsetTop;
        objParent = objParent.offsetParent;
      }

      objParent = obj.offsetParent;
      */

      // added a try-catch to the next loop (MYFACES-870)
      try {
	      while( objParent.tagName.toUpperCase() != "BODY" ){
	        objLeft  -= objParent.scrollLeft;
	        objTop   -= objParent.scrollTop;
	        objParent = objParent.parentNode;
     	 }
      } catch (ex) {
          // ignore
      }

      objHeight = obj.offsetHeight;
      objWidth = obj.offsetWidth;

      if(( overDiv.offsetLeft + overDiv.offsetWidth ) <= objLeft );
      else if(( overDiv.offsetTop + overDiv.offsetHeight ) <= objTop );
      else if( overDiv.offsetTop >= ( objTop + objHeight ));
      else if( overDiv.offsetLeft >= ( objLeft + objWidth ));
      else
        obj.style.visibility = "hidden";
    }
  }
}

/*
* unhides <select> and <applet> objects (for IE only)
*/
org_apache_myfaces_PopupCalendar.prototype._showElement=function( elmID ){
  if( this.ie ){
    for( i = 0; i < document.all.tags( elmID ).length; i++ ){
      obj = document.all.tags( elmID )[i];

      if( !obj || !obj.offsetParent )
        continue;

      obj.style.visibility = "";
    }
  }
}

org_apache_myfaces_PopupCalendar.prototype.addHoliday=function(d, m, y, desc){
	this.holidays[this.holidaysCounter++] = new org_apache_myfaces_HolidayRec ( d, m, y, desc );
}

org_apache_myfaces_PopupCalendar.prototype._swapImage=function(srcImg, destImg){
	srcImg.setAttribute("src",this.initData.imgDir + destImg);
}

org_apache_myfaces_PopupCalendar.prototype._getCrossObj=function(elem_id){
    return document.getElementById(elem_id).style;
    //old way of doing this: (this.dom) ? document.getElementById("calendar").style : this.ie ? document.all.calendar : document.calendar;
}

org_apache_myfaces_PopupCalendar.prototype._keypresshandler=function(){
    try
    {
        if (event && event.keyCode==27)
            this._hideCalendar();
    }
    catch(ex)
    {
    }
}

org_apache_myfaces_PopupCalendar.prototype._clickhandler=function(){
    if (!this.bShow)
        this._hideCalendar();
    this.bShow = false;
}

org_apache_myfaces_PopupCalendar.prototype.init=function(){
    if (this.dom){

        if(!this.calendarDiv)
        {
            for	(i=0;i<this.imgSrc.length;i++)
                this.img[i] = new Image;

            var bodyTag = document.getElementsByTagName("body")[0];

            this.calendarDiv = document.createElement("div");
            this.calendarDiv.setAttribute("class",this.initData.themePrefix+"-div-style");

            Event.observe(this.calendarDiv,"click",function(){this.bShow=true;}.bind(this),false);

            bodyTag.appendChild(this.calendarDiv);

            var mainTable = document.createElement("table");
            mainTable.setAttribute("style","width:"+((this.initData.showWeekNumber==1)?250:220)+"px;");
            mainTable.setAttribute("class",this.initData.themePrefix+"-table-style");

            this.calendarDiv.appendChild(mainTable);

            //This is necessary for IE. If you don't create a tbody element, the table will never show up!
            var mainBody = document.createElement("tbody");
            mainTable.appendChild(mainBody);

            var mainRow = document.createElement("tr");
            mainRow.setAttribute("class",this.initData.themePrefix+"-title-background-style");

            mainBody.appendChild(mainRow);

            var mainCell = document.createElement("td");

            mainRow.appendChild(mainCell);

            var contentTable = document.createElement("table");
            contentTable.setAttribute("style","width:"+((this.initData.showWeekNumber==1)?248:218)+"px;");

            var contentBody = document.createElement("tbody");
            contentTable.appendChild(contentBody);

            mainCell.appendChild(contentTable);

            var headerRow = document.createElement("tr");
            contentBody.appendChild(headerRow);

            var captionCell = document.createElement("td");
            captionCell.setAttribute("class",this.initData.themePrefix+"-title-style");
            headerRow.appendChild(captionCell);

            this.captionSpan = document.createElement("span");
            captionCell.appendChild(this.captionSpan);

            var closeButtonCell = document.createElement("td");
            closeButtonCell.setAttribute("style","text-align:right;");
            headerRow.appendChild(closeButtonCell);

            var closeCalendarLink = document.createElement("a");
            closeCalendarLink.setAttribute("href","#");
            Event.observe(closeCalendarLink,"click",function(){
                this._hideCalendar();
                return false;
            }.bind(this),false);

            closeButtonCell.appendChild(closeCalendarLink);

            var closeCalendarSpan = document.createElement("span");
            closeCalendarSpan.setAttribute("id","closeButton");

            closeCalendarLink.appendChild(closeCalendarSpan);

            var contentRow = document.createElement("tr");
            mainBody.appendChild(contentRow);

            var contentCell = document.createElement("td");
            contentCell.setAttribute("class",this.initData.themePrefix+"-body-style");
            contentRow.appendChild(contentCell);

            var contentSpan = document.createElement("span");
            contentSpan.setAttribute("id","popupcalendar_content");
            contentCell.appendChild(contentSpan);

            if(this.initData.showToday==1)
            {
                var todayRow = document.createElement("tr");
                todayRow.setAttribute("class",this.initData.themePrefix+"-today-style");
                mainBody.appendChild(todayRow);

                var todayCell = document.createElement("td");
                todayCell.setAttribute("class",this.initData.themePrefix+"-today-lbl-style");
                todayRow.appendChild(todayCell);

                this.todaySpan = document.createElement("span");
                todayCell.appendChild(this.todaySpan);
            }

            this.selectMonthDiv = document.createElement("div");
            this.selectMonthDiv.setAttribute("class",this.initData.themePrefix+"-div-style");

            bodyTag.appendChild(this.selectMonthDiv);

            this.selectYearDiv = document.createElement("div");
            this.selectYearDiv.setAttribute("class",this.initData.themePrefix+"-div-style");

            bodyTag.appendChild(this.selectYearDiv);

            //document.write ("<div onclick='this.bShow=true' id='calendar' class='"+this.initData.themePrefix+"-div-style'>");
            //document.write ("<table width="+((this.initData.showWeekNumber==1)?250:220)+" class='"+this.initData.themePrefix+"-table-style'>");
            //document.write("<tr class='"+this.initData.themePrefix+"-title-background-style'>");
            //document.write("<td>");
            //document.write("<table width='"+((this.initData.showWeekNumber==1)?248:218)+"'>");
            //document.write("<tr>");
            //document.write("<td class='"+this.initData.themePrefix+"-title-style'><span id='caption'></span></td>");
            //document.write("<td align=right><a href='javascript:_hideCalendar()'><span id='closeButton'></span></a></td>");
            //document.write("</tr></table></td></tr>");
            //document.write("<tr><td class='"+this.initData.themePrefix+"-body-style'><span id='popupcalendar_content'></span></td></tr>")

            //if (this.initData.showToday==1)
            //    document.write ("<tr class='"+this.initData.themePrefix+"-today-style'><td class='"+this.initData.themePrefix+"-today-lbl-style'><span id='lblToday'></span></td></tr>")

            //document.write ("</table></div>");
            //document.write("<div id='selectMonth' class='"+this.initData.themePrefix+"-div-style'></div><div id='selectYear' class='"+this.initData.themePrefix+"-div-style'></div>");

            Event.observe(document,"keypress",this._keypresshandler.bind(this),false);
            Event.observe(document,"click",this._clickhandler.bind(this),false);
        }
    }


    if (!this.ns4){
		if (!this.ie)
			this.yearNow += 1900;

		this._hideCalendar();

		this.monthConstructed=false;
		this.yearConstructed=false;

		if (this.initData.showToday==1)
        {
            this.todaySpan.appendChild(document.createTextNode(this.initData.todayString+" "))

            var todayLink = document.createElement("a");
            todayLink.setAttribute("class",this.initData.themePrefix+"-today-style");
            todayLink.setAttribute("title",this.initData.gotoString);
            todayLink.setAttribute("href","#")
            todayLink.appendChild(document.createTextNode(this._todayIsDate()));
            Event.observe(todayLink,"click",function(){
                this.selectedDate.month=this.monthNow;
                this.selectedDate.year=this.yearNow;
                this._constructCalendar();
                return false;
            }.bind(this),false);
            Event.observe(todayLink,"mousemove",function(){
                window.status=this.initData.gotoString;
            }.bind(this),false);
            Event.observe(todayLink,"mouseout",function(){
                window.status="";
            }.bind(this),false);

            this.todaySpan.appendChild(todayLink);
        }

        this._appendNavToCaption("left");
        this._appendNavToCaption("right");

        //var sHTML1 ="<span id='spanLeft'  class='"+this.initData.themePrefix+"-title-control-normal-style' onmouseover='this._swapImage(\"changeLeft\",\"left2.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-select-style\"; window.status=\""+this.scrollLeftMessage+"\"' onclick='javascript:this._decMonth()' onmouseout='clearInterval(this.intervalID1);this._swapImage(\"changeLeft\",\"left1.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-normal-style\"; window.status=\"\"' onmousedown='clearTimeout(this.timeoutID1);this.timeoutID1=setTimeout(\"this._startDecMonth()\",500)'	onmouseup='clearTimeout(this.timeoutID1);clearInterval(this.intervalID1)'>&#160;<img id='changeLeft' src='"+this.initData.imgDir+"left1.gif' width=10 height=11 border=0 />&#160;</span>&#160;"
		//sHTML1+=    "<span id='spanRight' class='"+this.initData.themePrefix+"-title-control-normal-style' onmouseover='this._swapImage(\"changeRight\",\"right2.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-select-style\"; window.status=\""+this.scrollRightMessage+"\"' onclick='javascript:this._incMonth()' onmouseout='clearInterval(this.intervalID1);this._swapImage(\"changeRight\",\"right1.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-normal-style\"; window.status=\"\"' onmousedown='clearTimeout(this.timeoutID1);this.timeoutID1=setTimeout(\"this._startIncMonth()\",500)'	onmouseup='clearTimeout(this.timeoutID1);clearInterval(this.intervalID1)'>&#160;<img id='changeRight' src='"+this.initData.imgDir+"right1.gif'	width=10 height=11 border=0 />&#160;</span>&#160;"
		//var sHTML1="<span id='spanMonth' class='"+this.initData.themePrefix+"-title-control-normal-style' onmouseover='this._swapImage(\"changeMonth\",\"drop2.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-select-style\"; window.status=\""+this.selectMonthMessage+"\"' onmouseout='this._swapImage(\"changeMonth\",\"drop1.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-normal-style\"; window.status=\"\"' onclick='this._popUpMonth()'></span>&#160;"
		//sHTML1+="<span id='spanYear'  class='"+this.initData.themePrefix+"-title-control-normal-style' onmouseover='this._swapImage(\"changeYear\",\"drop2.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-select-style\"; window.status=\""+this.selectYearMessage+"\"'	onmouseout='this._swapImage(\"changeYear\",\"drop1.gif\"); this.className=\""+this.initData.themePrefix+"-title-control-normal-style\"; window.status=\"\"'	onclick='this._popUpYear()'></span>&#160;"

        this.monthSpan = document.createElement("span");
        this.monthSpan.setAttribute("class",this.initData.themePrefix+"-title-control-normal-style");

        Event.observe(this.monthSpan,"mouseover",function(){
            this._swapImage(this.changeMonthImg,"drop2.gif");
            this.className=this.initData.themePrefix+"-title-control-select-style";
            window.status=this.selectMonthMessage;
        }.bind(this),false);

        Event.observe(this.monthSpan,"mouseout",function(){
            this._swapImage(this.changeMonthImg,"drop1.gif");
            this.className=this.initData.themePrefix+"-title-control-normal-style";
            window.status="";
        }.bind(this),false);

        Event.observe(this.monthSpan,"click",function(){
            this._popUpMonth();
        }.bind(this),false);

        this.captionSpan.appendChild(this.monthSpan);
        this._appendNbsp(this.captionSpan);

        this.yearSpan = document.createElement("span");
        this.yearSpan.setAttribute("class",this.initData.themePrefix+"-title-control-normal-style");

        Event.observe(this.yearSpan,"mouseover",function(){
            this._swapImage(this.changeYearImg,"drop2.gif");
            this.className=this.initData.themePrefix+"-title-control-select-style";
            window.status=this.selectYearMessage;
        }.bind(this),false);

        Event.observe(this.yearSpan,"mouseout",function(){
            this._swapImage(this.changeYearImg,"drop1.gif");
            this.className=this.initData.themePrefix+"-title-control-normal-style";
            window.status="";
        }.bind(this),false);

        Event.observe(this.yearSpan,"click",function(){
            this._popUpYear();
        }.bind(this),false);

        this.captionSpan.appendChild(this.yearSpan);
        this._appendNbsp(this.captionSpan);

        this.bPageLoaded = true;
	}
}

 org_apache_myfaces_PopupCalendar.prototype._appendNavToCaption=function(direction){
    var imgLeft = document.createElement("img");
    imgLeft.setAttribute("src",this.initData.imgDir+direction+"1.gif");
    imgLeft.setAttribute("style","width:10px;height:11px;border:0px;")

    var spanLeft = document.createElement("span");
    spanLeft.setAttribute("class",this.initData.themePrefix+"-title-control-normal-style");
    Event.observe(spanLeft,"mouseover",function(){
        this._swapImage(imgLeft,direction+"2.gif");
        this.className=this.initData.themePrefix+"-title-control-select-style";
        if(direction=="left"){
            window.status=this.scrollLeftMessage;
        } else {
            window.status=this.scrollRightMessage;
        }
    }.bind(this),false);
    Event.observe(spanLeft,"click",function(){
        if(direction=="left"){
            this._decMonth();
        } else{
            this._incMonth();
        }
    }.bind(this),false);
    Event.observe(spanLeft,"mouseout",function(){
        clearInterval(this.intervalID1);
        this._swapImage(imgLeft,direction+"1.gif");
        this.className=""+this.initData.themePrefix+"-title-control-normal-style";
        window.status="";
    }.bind(this),false);
    Event.observe(spanLeft,"mousedown",function(){
        clearTimeout(this.timeoutID1);
        this.timeoutID1=setTimeout((function(){
            if(direction=="left")
            {
                this._startDecMonth();
            }
            else
            {
                this._startIncMonth();
            }
        }).bind(this),500)
    }.bind(this),false);
    Event.observe(spanLeft,"mouseup",function(){
        clearTimeout(this.timeoutID1);
        clearInterval(this.intervalID1);
    }.bind(this),false);

    this._appendNbsp(spanLeft);
    spanLeft.appendChild(imgLeft);
    this._appendNbsp(spanLeft);
    this.captionSpan.appendChild(spanLeft);
    this._appendNbsp(spanLeft);
 }
org_apache_myfaces_PopupCalendar.prototype._appendNbsp=function(element){
    element.appendChild(document.createTextNode(String.fromCharCode(160)));
}
 org_apache_myfaces_PopupCalendar.prototype._todayIsDate=function(){
    var format = new org_apache_myfaces_SimpleDateFormat(this.todayDateFormat,this.dateFormatSymbols);
    return format.format(this.today);
}

org_apache_myfaces_PopupCalendar.prototype._hideCalendar=function(){
	this.crossObj.visibility="hidden"
	if (this.crossMonthObj != null){this.crossMonthObj.visibility="hidden";}
	if (this.crossYearObj != null){this.crossYearObj.visibility="hidden";}

    this._showElement( 'SELECT' );
	this._showElement( 'APPLET' );
}

org_apache_myfaces_PopupCalendar.prototype._padZero=function(num){
	return (num	< 10)? '0' + num : num ;
}

org_apache_myfaces_PopupCalendar.prototype._constructDate=function(d,m,y){
    var format = new org_apache_myfaces_SimpleDateFormat(this.todayDateFormat,this.dateFormatSymbols);
    return format.format(new Date(y,m,d,this.selectedDate.hour,this.selectedDate.min,this.selectedDate.sec));
}

org_apache_myfaces_PopupCalendar.prototype._closeCalendar=function() {
    this._hideCalendar();

    if( this.myFacesCtlType!="x:inputDate" )
    {
        this.ctlToPlaceValue.value = this._constructDate(this.selectedDate.date,this.selectedDate.month,this.selectedDate.year)
        var onchange=this.ctlToPlaceValue.getAttribute("onchange");
        if(onchange)
        {
            eval(onchange);
        }
    }
    else
    {
        document.getElementById(this.myFacesInputDateClientId+".day").value = this.selectedDate.date;
        document.getElementById(this.myFacesInputDateClientId+".month").value = this.selectedDate.month+1;
        document.getElementById(this.myFacesInputDateClientId+".year").value = this.selectedDate.year;
    }
}

/*** Month Pulldown	***/

org_apache_myfaces_PopupCalendar.prototype._startDecMonth=function(){
	this.intervalID1=setInterval((function(){this._decMonth}).bind(this),80);
}

org_apache_myfaces_PopupCalendar.prototype._startIncMonth=function(){
	this.intervalID1=setInterval((function(){this._incMonth}).bind(this),80);
}

org_apache_myfaces_PopupCalendar.prototype._incMonth=function(){
	this.selectedDate.month=this.selectedDate.month+1;
	if (this.selectedDate.month>11) {
		this.selectedDate.month=0;
		this.selectedDate.year++;
	}
	this._constructCalendar();
}

org_apache_myfaces_PopupCalendar.prototype._decMonth=function() {
	this.selectedDate.month=this.selectedDate.month-1;
	if (this.selectedDate.month<0) {
		this.selectedDate.month=11
		this.selectedDate.year--
	}
	this._constructCalendar()
}

org_apache_myfaces_PopupCalendar.prototype._constructMonth=function(){
	this._popDownYear();
	if (!this.monthConstructed) {

        var selectMonthTable = document.createElement("table");
        selectMonthTable.setAttribute("style","width:70px;border-collapse:collapse;")
        selectMonthTable.setAttribute("class",this.initData.themePrefix+"-dropdown-style");
        Event.observe(selectMonthTable,"mouseover",function(){
            clearTimeout(this.timeoutID1);
        }.bind(this),false);
        Event.observe(selectMonthTable,"mouseout",function(event){
            clearTimeout(this.timeoutID1);
            this.timeoutID1=setTimeout((function(){this._popDownMonth()}).bind(this),100);
            event.cancelBubble=true;
        }.bindAsEventListener(this),false);

        //todo:
        for	(i=0; i<12;	i++) {
			var sName = this.initData.monthName[i];
			if (i==this.selectedDate.month)
				sName =	"<b>" +	sName +	"</b>";
			sHTML += "<tr><td id='m" + i + "' onmouseover='this.className=\""+this.initData.themePrefix+"-dropdown-select-style\"' onmouseout='this.className=\""+this.initData.themePrefix+"-dropdown-normal-style\"' onclick='this.monthConstructed=false;this.selectedDate.month=" + i + ";this_constructCalendar();this._popDownMonth();event.cancelBubble=true'>&#160;" + sName + "&#160;</td></tr>";
		}

        this.monthConstructed=true;
	}
}

org_apache_myfaces_PopupCalendar.prototype._popUpMonth=function() {
	this._constructMonth()
	this.crossMonthObj.visibility = (this.dom||this.ie)? "visible"	: "show"
	this.crossMonthObj.left = parseInt(this._formatInt(this.crossObj.left),10) + 50 + "px";
	this.crossMonthObj.top =	parseInt( this._formatInt(this.crossObj.top),10) + 26 + "px";

	this._hideElement( 'SELECT', selectMonthDiv );
	this._hideElement( 'APPLET', selectMonthDiv );
}

org_apache_myfaces_PopupCalendar.prototype._popDownMonth=function()	{
	this.crossMonthObj.visibility= "hidden"
}

/*** Year Pulldown ***/

org_apache_myfaces_PopupCalendar.prototype._incYear=function() {
	for	(i=0; i<7; i++){
		newYear	= (i+this.nStartingYear)+1
		if (newYear==this.selectedDate.year)
		{ txtYear =	"&#160;<B>"	+ newYear +	"</B>&#160;" }
		else
		{ txtYear =	"&#160;" + newYear + "&#160;" }
		document.getElementById("y"+i).innerHTML = txtYear;
	}
	this.nStartingYear++;
	this.bShow=true;
}

org_apache_myfaces_PopupCalendar.prototype._decYear=function() {
	for	(i=0; i<7; i++){
		newYear	= (i+this.nStartingYear)-1
		if (newYear==this.selectedDate.year)
		{ txtYear =	"&#160;<B>"	+ newYear +	"</B>&#160;" }
		else
		{ txtYear =	"&#160;" + newYear + "&#160;" }
		document.getElementById("y"+i).innerHTML = txtYear;
	}
	this.nStartingYear--;
	this.bShow=true;
}

org_apache_myfaces_PopupCalendar.prototype._selectYear=function(nYear) {
	this.selectedDate.year=parseInt( this._formatInt(nYear+this.nStartingYear),10);
	this.yearConstructed=false;
	this._constructCalendar();
	this._popDownYear();
}

org_apache_myfaces_PopupCalendar.prototype._constructYear=function() {
	this._popDownMonth();
	var sHTML =	"";
	if (!this.yearConstructed) {

		sHTML =	"<tr><td align='center'	onmouseover='this.className=\""+this.initData.themePrefix+"-dropdown-select-style\"' onmouseout='clearInterval(this.intervalID1); this.className=\""+this.initData.themePrefix+"-dropdown-normal-style\"' onmousedown='clearInterval(this.intervalID1);this.intervalID1=setInterval(\"_decYear()\",30)' onmouseup='clearInterval(this.intervalID1)'>-</td></tr>";

		var j =	0;
        this.nStartingYear = this.selectedDate.year-3;
		for	(i=this.selectedDate.year-3; i<=(this.selectedDate.year+3); i++) {
			var sName =	i;
			if (i==this.selectedDate.year)
				sName =	"<b>"+sName+"</b>";

			sHTML += "<tr><td id='y"+j+"' onmouseover='this.className=\""+this.initData.themePrefix+"-dropdown-select-style\"' onmouseout='this.className=\""+this.initData.themePrefix+"-dropdown-normal-style\"' onclick='this._selectYear("+j+");event.cancelBubble=true'>&#160;"+sName+"&#160;</td></tr>";
			j++;
		}

		sHTML += "<tr><td align='center' onmouseover='this.className=\""+this.initData.themePrefix+"-dropdown-select-style\"' onmouseout='clearInterval(this.intervalID2); this.className=\""+this.initData.themePrefix+"-dropdown-normal-style\"' onmousedown='clearInterval(this.intervalID2);this.intervalID2=setInterval(\"_incYear()\",30)'	onmouseup='clearInterval(this.intervalID2)'>+</td></tr>";

		selectYearDiv.innerHTML	= "<table width='44' class='"+this.initData.themePrefix+"-dropdown-style' onmouseover='clearTimeout(this.timeoutID2)' onmouseout='clearTimeout(this.timeoutID2);this.timeoutID2=setTimeout(\"_popDownYear()\",100)' cellspacing='0'>"+sHTML+"</table>";

		this.yearConstructed = true;
	}
}

org_apache_myfaces_PopupCalendar.prototype._popDownYear=function() {
	clearInterval(this.intervalID1);
	clearTimeout(this.timeoutID1);
	clearInterval(this.intervalID2);
	clearTimeout(this.timeoutID2);
	this.crossYearObj.visibility= "hidden";
}

org_apache_myfaces_PopupCalendar.prototype._popUpYear=function() {
	var	leftOffset;

	_constructYear();
	this.crossYearObj.visibility = (this.dom||this.ie) ? "visible" : "show";
	leftOffset = parseInt( this._formatInt(this.crossObj.left),10) + document.getElementById("spanYear").offsetLeft;
	if (this.ie)
		leftOffset += 6;
	this.crossYearObj.left =	leftOffset + "px";
	this.crossYearObj.top = parseInt( this._formatInt(this.crossObj.top),10) +	26 + "px";
}

/*** calendar ***/
org_apache_myfaces_PopupCalendar.prototype._weekNbr=function(n) {
	// Algorithm used:
	// From Klaus Tondering's Calendar document (The Authority/Guru)
	// hhtp://www.tondering.dk/claus/calendar.html
	// a = (14-month) / 12
	// y = year + 4800 - a
	// m = month + 12a - 3
	// J = day + (153m + 2) / 5 + 365y + y / 4 - y / 100 + y / 400 - 32045
	// d4 = (J + 31741 - (J mod 7)) mod 146097 mod 36524 mod 1461
	// L = d4 / 1460
	// d1 = ((d4 - L) mod 365) + L
	// WeekNumber = d1 / 7 + 1

	year = n.getFullYear();
	month = n.getMonth() + 1;
	if (this.initData.startAt == 0)
		day = n.getDate() + 1;
	else
		day = n.getDate();

	a = Math.floor((14-month) / 12);
	y = year + 4800 - a;
	m = month + 12 * a - 3;
	b = Math.floor(y/4) - Math.floor(y/100) + Math.floor(y/400);
	J = day + Math.floor((153 * m + 2) / 5) + 365 * y + b - 32045;
	d4 = (((J + 31741 - (J % 7)) % 146097) % 36524) % 1461;
	L = Math.floor(d4 / 1460);
	d1 = ((d4 - L) % 365) + L;
	week = Math.floor(d1/7) + 1;

	return week;
}

org_apache_myfaces_PopupCalendar.prototype._constructCalendar=function() {
	var aNumDays = Array (31,0,31,30,31,30,31,31,30,31,30,31);

	var dateMessage;
	var	startDate =	new	Date (this.selectedDate.year,this.selectedDate.month,1);
	var endDate;

	if (this.selectedDate.month==1){
		endDate	= new Date (this.selectedDate.year,this.selectedDate.month+1,1);
		endDate	= new Date (endDate	- (24*60*60*1000));
		numDaysInMonth = endDate.getDate();
	}else
		numDaysInMonth = aNumDays[this.selectedDate.month];


	datePointer	= 0;
	dayPointer = startDate.getDay() - this.initData.startAt;

	if (dayPointer<0)
		dayPointer = 6;

	var sHTML = "<table border=0 class='"+this.initData.themePrefix+"-body-style'><tr>"

	if (this.initData.showWeekNumber==1)
		sHTML += "<td width=27><b>" + this.initData.weekString + "</b></td><td width=1 rowspan=7 class='"+this.initData.themePrefix+"-weeknumber-div-style'><img src='"+this.initData.imgDir+"divider.gif' width=1></td>";

	for	(i=0; i<7; i++)
		sHTML += "<td width='27' align='right'><b>"+ this.initData.dayName[i]+"</b></td>";

	sHTML +="</tr><tr>";

	if (this.initData.showWeekNumber==1)
		sHTML += "<td align=right>" + this._weekNbr(startDate) + "&#160;</td>";

	for	( var i=1; i<=dayPointer;i++ )
		sHTML += "<td>&#160;</td>";

	for	( datePointer=1; datePointer<=numDaysInMonth; datePointer++ ){
		dayPointer++;
		sHTML += "<td align=right>";

		var sStyle=this.initData.themePrefix+"-normal-day-style"; //regular day

		if ((datePointer==this.dateNow)&&(this.selectedDate.month==this.monthNow)&&(this.selectedDate.year==this.yearNow)) //today
		{ sStyle = this.initData.themePrefix+"-current-day-style"; }
		else if	(dayPointer % 7 == (this.initData.startAt * -1) +1) //end-of-the-week day
		{ sStyle = this.initData.themePrefix+"-end-of-weekday-style"; }

		//selected day
		if ((datePointer==this.saveSelectedDate.date) &&	(this.selectedDate.month==this.saveSelectedDate.month)	&& (this.selectedDate.year==this.saveSelectedDate.year))
		{ sStyle += " "+this.initData.themePrefix+"-selected-day-style"; }

		sHint = ""
		for (k=0;k<this.holidaysCounter;k++)
		{
			if ((parseInt( this._formatInt(this.holidays[k].d),10)==datePointer)&&(parseInt( this._formatInt(this.holidays[k].m),10)==(this.selectedDate.month+1)))
			{
				if ((parseInt( this._formatInt(this.holidays[k].y),10)==0)||((parseInt( this._formatInt(this.holidays[k].y),10)==this.selectedDate.year)&&(parseInt( this._formatInt(this.holidays[k].y),10)!=0)))
				{
					sStyle += " "+this.initData.themePrefix+"-holiday-style";
					sHint+=sHint==""?this.holidays[k].desc:"\n"+this.holidays[k].desc
				}
			}
		}

		var regexp= /\"/g
		sHint=sHint.replace(regexp,"&quot;");

		sSelectStyle = sStyle+" "+this.initData.themePrefix+"-would-be-selected-day-style";
		sNormalStyle = sStyle;

		dateMessage = "onmousemove='window.status=\""+this.initData.selectDateMessage.replace("[date]",this._constructDate(datePointer,this.selectedDate.month,this.selectedDate.year))+"\"' onmouseout='this.className=\""+sNormalStyle+"\"; window.status=\"\"' "

		sHTML += "<a class='"+sStyle+"' "+dateMessage+" title=\"" + sHint + "\" href='javascript:this.selectedDate.date="+datePointer+";this._closeCalendar();' onmouseover='this.className=\""+sSelectStyle+"\";' >&#160;" + datePointer + "&#160;</a>";

		if ((dayPointer+this.initData.startAt) % 7 == this.initData.startAt) {
			sHTML += "</tr><tr>";
			if ((this.initData.showWeekNumber==1)&&(datePointer<numDaysInMonth))
				sHTML += "<td align=right>" + (this._weekNbr(new Date(this.selectedDate.year,this.selectedDate.month,datePointer+1))) + "&#160;</td>";
		}
	}

	document.getElementById("popupcalendar_content").innerHTML = sHTML;
	this.monthSpan.innerHTML = "&#160;" +	this.initData.monthName[this.selectedDate.month] + "&#160;<img id='changeMonth' src='"+this.initData.imgDir+"drop1.gif' width='12' height='10' border=0>";
	this.yearSpan.innerHTML =	"&#160;" + this.selectedDate.year	+ "&#160;<img id='changeYear' src='"+this.initData.imgDir+"drop1.gif' width='12' height='10' border=0>";
	document.getElementById("closeButton").innerHTML = "<img src='"+this.initData.imgDir+"close.gif' width='15' height='13' border='0' alt='Close the Calendar'>";
}

org_apache_myfaces_PopupCalendar.prototype._popUpCalendar=function(ctl, ctl2, format){
    if (this.bPageLoaded){
		if ( this.crossObj.visibility == "hidden" ) {
            this.ctlToPlaceValue = ctl2;
			this.todayDateFormat=format;

            var simpleDateFormat = new org_apache_myfaces_SimpleDateFormat(this.todayDateFormat, this.dateFormatSymbols);
            var dateSelected = simpleDateFormat.parse(ctl2.value);

            if(dateSelected)
            {
                this.selectedDate.sec = dateSelected.getSeconds();
                this.selectedDate.min = dateSelected.getMinutes();
                this.selectedDate.hour = dateSelected.getHours();
                this.selectedDate.date = dateSelected.getDate();
                this.selectedDate.month = dateSelected.getMonth();

                var yearStr = dateSelected.getYear()+"";

                if (yearStr.length < 4)
                {
                      yearStr=(parseInt(yearStr, 10)+1900)+"";
                }

                this.selectedDate.year = parseInt(yearStr, 10);
            }
            else
            {
                this.selectedDate.date = this.dateNow;
				this.selectedDate.month =	this.monthNow;
				this.selectedDate.year = this.yearNow;
			}

			this._popUpCalendar_Show(ctl);
		}else{
			this._hideCalendar();
			if (this.ctlNow!=ctl)
				this._popUpCalendar(ctl, ctl2, format);
		}
		this.ctlNow = ctl;
	}
}

org_apache_myfaces_PopupCalendar.prototype._popUpCalendarForInputDate=function(clientId, format){
	if (this.bPageLoaded){
	    this.myFacesCtlType = "x:inputDate";
		this.myFacesInputDateClientId = clientId;
		this.todayDateFormat=format;

		this.selectedDate.date = parseInt(  this._formatInt(document.getElementById(clientId+".day").value),10);
		this.selectedDate.month = parseInt( this._formatInt( document.getElementById(clientId+".month").value),10)-1;
		this.selectedDate.year = parseInt(  this._formatInt(document.getElementById(clientId+".year").value),10);
		this.ctlNow = document.getElementById(clientId+".day");
		this._popUpCalendar_Show(document.getElementById(clientId+".day"));
	}
}

org_apache_myfaces_PopupCalendar.prototype._popUpCalendar_Show=function(ctl){
	this.saveSelectedDate.date = this.selectedDate.date;
	this.saveSelectedDate.month = this.selectedDate.month;
	this.saveSelectedDate.year = this.selectedDate.year;

	var	leftpos = 0;
	var	toppos = 0;

	var aTag = ctl;
	// Added try-catch to the next loop (MYFACES-870)
	try {
		do {
			aTag = aTag.offsetParent;
			leftpos	+= aTag.offsetLeft;
			toppos += aTag.offsetTop;
		} while(aTag.tagName!="BODY");
	} catch (ex) {
       // ignore
    }

	var leftScrollOffset = 0;
	var topScrollOffset = 0;

	aTag = ctl;
	// Added try-catch (MYFACES-870)
	try {
		do {
			leftScrollOffset += aTag.scrollLeft;
			topScrollOffset += aTag.scrollTop;
			aTag = aTag.parentNode;
		} while(aTag.tagName!="BODY");
	} catch (ex) {
		 // ignore
	}

	var bodyRect = this._getVisibleBodyRectangle();
	var cal = document.getElementById("calendar");
	var top = ctl.offsetTop + toppos - topScrollOffset + ctl.offsetHeight +	2;
	var left = ctl.offsetLeft + leftpos - leftScrollOffset;

	if (left + cal.offsetWidth > bodyRect.right)
	{
		left = bodyRect.right - cal.offsetWidth;
	}
	if (top + cal.offsetHeight > bodyRect.bottom)
	{
		top = bodyRect.bottom - cal.offsetHeight;
	}
	if (left < bodyRect.left)
	{
		left = bodyRect.left;
	}
	if (top < bodyRect.top)
	{
		top = bodyRect.top;
	}

	this.crossObj.left = this.initData.fixedX==-1 ? left + "px": this.initData.fixedX;
	this.crossObj.top = this.initData.fixedY==-1 ? top + "px": this.initData.fixedY;
	this._constructCalendar (1, this.selectedDate.month, this.selectedDate.year);

    this.crossObj.visibility=(this.dom||this.ie)? "visible" : "show";

    this._hideElement( 'SELECT', document.getElementById("calendar") );
	this._hideElement( 'APPLET', document.getElementById("calendar") );

	this.bShow = true;
}

org_apache_myfaces_PopupCalendar.prototype._getVisibleBodyRectangle=function()
{
	var visibleRect = new org_apache_myfaces_Rectangle();

	if (window.pageYOffset != undefined)
	{
		//Most non IE
		visibleRect.top = window.pageYOffset;
		visibleRect.left = window.pageXOffset;
	}
	else if(document.body && document.body.scrollTop )
	{
    	//IE 6 strict mode
    	visibleRect.top = document.body.scrollTop;
    	visibleRect.left = document.body.scrollLeft;
  	}
  	else if(document.documentElement && document.documentElement.scrollTop )
    {
    	//Older IE
    	visibleRect.top = document.documentElement.scrollTop;
    	visibleRect.left = document.documentElement.scrollLeft;
    }

	if( window.innerWidth != undefined )
	{
    	//Most non-IE
    	visibleRect.right = visibleRect.left + window.innerWidth;
    	visibleRect.bottom = visibleRect.top + window.innerHeight;
  	}
  	else if( document.documentElement && document.documentElement.clientHeight )
    {
    	//IE 6 strict mode
    	visibleRect.right = visibleRect.left + document.documentElement.clientWidth;
    	visibleRect.bottom = visibleRect.top + document.documentElement.clientHeight;
  	}
  	else if( document.body && document.body.clientHeight )
  	{
	    //IE 4 compatible
	    visibleRect.right = visibleRect.left + document.body.clientWidth;
	    visibleRect.bottom = visibleRect.top + document.body.clientHeight;
  	}
	return visibleRect;
}

function org_apache_myfaces_Rectangle()
{
	this.top = 0;
	this.left = 0;
	this.bottom = 0;
	this.right = 0;
}

org_apache_myfaces_PopupCalendar.prototype._formatInt=function(str){

    if(typeof str == 'string'){

	  //truncate 0 for number less than 10
      if (str.charAt && str.charAt(0)=="0"){ // <----- Change, added str.charAt for method availability detection (MYFACES)
         return str.charAt(1);
      }

    }
	  return str;

}
