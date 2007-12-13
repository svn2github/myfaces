/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//----------------------------------------------------------------------------
// A javascript implementation of most of the java.text.SimpleDateFormat class,
// written for the purposes of implementing the Apache MyFaces Tomahawk
// calendar control.
//
// This file defines a javascript class, org_apache_myfaces_SimpleDateFormat.
// An instance of this class can be constructed, then used to parse strings
// into dates, and format dates into strings.
//
// Note that there is one difference from SimpleDateFormat in the formatting
// string pattern; this code also supports the JODA "xxxx" pattern for weekYear.
// If this is used, then the ugly java.text.SimpleDateFormat mangling of the
// purpose of the "yyyy" pattern is disabled, allowing formatting patterns like
// "xxxx-ww  yyyy-mm-dd" to behave correctly.
//----------------------------------------------------------------------------

//----------------------------------------------------------------------------
// Return the week# represented by the specified date (1..53).
//
// This implements the ISO-8601 standard for week numbering, as documented in
// Klaus Tondering's Calendar document, version 2.8:
//   http://www.tondering.dk/claus/calendar.html
//
// For dates in January and February, calculate:
//
//    a = year-1
//    b = a/4 - a/100 + a/400
//    c = (a-1)/4 - (a-1)/100 + (a-1)/400
//    s = b-c
//    e = 0
//    f = day - 1 + 31*(month-1)
//
// For dates in March through December, calculate:
//
//    a = year
//    b = a/4 - a/100 + a/400
//    c = (a-1)/4 - (a-1)/100 + (a-1)/400
//    s = b-c
//    e = s+1
//    f = day + (153*(month-3)+2)/5 + 58 + s
//
// Then, for any month continue thus:
//
//    g = (a + b) mod 7
//    d = (f + g - e) mod 7
//    n = f + 3 - d
//
// We now have three situations:
//
//    If n<0, the day lies in week 53-(g-s)/5 of the previous year.
//    If n>364+s, the day lies in week 1 of the coming year.
//    Otherwise, the day lies in week n/7 + 1 of the current year.
//
// This algorithm gives you a couple of additional useful values:
//
//    d indicates the day of the week (0=Monday, 1=Tuesday, etc.)
//    f+1 is the ordinal number of the date within the current year.
//
// Note that ISO-8601 specifies that week1 of a year is the first week in
// which the majority of days lie in that year. An equivalent description
// is that it is the first week including the 4th of january. This means
// that the 1st, 2nd and 3rd of January might lie in the last week of the
// previous year, and that the last week of a year may include the first
// few days of the following year.
//
// ISO-8601 also specifies that the first day of the week is always Monday.
//
// This function returns the week number regardless of which year it lies in.
// That means that asking for the week# of 01/01/yyyy might return 52 or 53,
// and asking for the week# of 31/12/yyyy might return 1.
//----------------------------------------------------------------------------
function org_apache_myfaces_SimpleDateFormat_weekNbr(n)
{
    var year = n.getFullYear();
    var month = n.getMonth() + 1;
    var day = n.getDate();

    var a,b,c,d,e,f,g;

    if (month <= 2)
    {
        a = year - 1;
        b = Math.floor(a/4) - Math.floor(a/100) + Math.floor(a/400);
        c = Math.floor((a-1)/4) - Math.floor((a-1)/100) + Math.floor((a-1)/400);
        s = b - c;
        e = 0;
        f = day - 1 + 31*(month-1);
    }
    else
    {
        a = year;
        b = Math.floor(a/4) - Math.floor(a/100) + Math.floor(a/400);
        c = Math.floor((a-1)/4) - Math.floor((a-1)/100) + Math.floor((a-1)/400);
        s = b - c;
        e = s + 1;
        f = day + Math.floor((153*(month-3) + 2)/5) + 58 + s;       
    }

    g = (a + b) % 7;
    d = (f + g - e) % 7;
    n = f + 3 - d;

    var week;
    if (n<0)
    {
        // previous year
        week = 53 - Math.floor((g-s)/5);
    }
    else if (n > (364+s))
    {
        // next year
        week = 1;
    }
    else
    {
        // current year
        week = Math.floor(n/7) + 1;
    }
    
    return week;
}

//----------------------------------------------------------------------------
// Create a new date using "week in year" rather than "month in year".
// Weeks always begin on a monday (ISO-8601).
//
// This functions like
//   new Date(year,month,day,hour,min,sec)
// except that week (1..53) is used in place of month, and day is (1..7), not 
// 1..31.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat_newDateByWeekYear = function(year, week, day, hour, min, sec)
{
    var MSECS_PER_DAY = 24*60*60*1000;
    
    var date = new Date(
        year, 0, 1,
        hour, min, sec);
    
    // Nudge date to the nearest Monday from the start of the year;
    // weeks always start on a monday. Note that it might be in the
    // previous year. The actual date for the start of the first week
    // in the year is guaranteed to be in the range (29dec-4jan)
    var dow = date.getDay();
    var daysOffset;
    if (dow == 1)
    {
      // first day of year is monday, so no offset needed.
      daysOffset = 0;
    }
    else if (dow <= 4)
    {
      // first day-of-year is tue, wed, thurs so the nearest monday is
      // earlier (in the previous year). The offset will be negative.
      daysOffset = 1 - dow;
    }
    else
    {
      // first day-of-year is fri,sat,sun so the nearest monday
      // is later..
      daysOffset = 8 - dow;
    }
    
    // now add week*7 days
    daysOffset += (week - 1) * 7 + (day - 1);

    // do arithmetic
    var msecsBase = date.getTime();
    var msecsOffset = daysOffset * MSECS_PER_DAY;

    var finalDate = new Date();
    finalDate.setTime(msecsBase + msecsOffset);
    return finalDate;
}

//----------------------------------------------------------------------------
// Constructor for a simple object that contains locale-specific constants
// used for date parsing and formatting.
//----------------------------------------------------------------------------
org_apache_myfaces_DateFormatSymbols = function()
{
        this.eras = new Array('BC', 'AD');
        this.months = new Array('January', 'February', 'March', 'April',
                'May', 'June', 'July', 'August', 'September', 'October',
                'November', 'December', 'Undecimber');
        this.shortMonths = new Array('Jan', 'Feb', 'Mar', 'Apr',
                'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct',
                'Nov', 'Dec', 'Und');
        this.weekdays = new Array('Sunday', 'Monday', 'Tuesday',
                'Wednesday', 'Thursday', 'Friday', 'Saturday');
        this.shortWeekdays = new Array('Sun', 'Mon', 'Tue',
                'Wed', 'Thu', 'Fri', 'Sat');
        this.ampms = new Array('AM', 'PM');
        this.zoneStrings = new Array(new Array(0, 'long-name', 'short-name'));
        var threshold = new Date();
        threshold.setYear(threshold.getYear()-80);
        this.twoDigitYearStart = threshold;
}

//----------------------------------------------------------------------------
// Constructor for a simple object that contains the current parsing or
// formatting state. This encapsulates the properties of a SimpleDateFormat
// which are modified during a parse or format call.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormatParserContext = function()
{
        this.newIndex=0;
        this.retValue=0;
        this.year=0;
        this.ambigousYear=false;
        this.month=0;
        this.day=0;
        this.dayOfWeek=0;
        this.hour=0;
        this.min=0;
        this.sec=0;
        this.ampm=0;
        this.dateStr="";

        this.weekYear=0;
        this.weekOfWeekYear=0;
        this.yearIsWeekYear=false;
}

//----------------------------------------------------------------------------
// Constructor method.
//
// param pattern defines the pattern to be used for parsing or formatting.
//
// param dateSymbols is optional. It defines the "locale"; if not defined
// then default settings (english locale) are used.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat = function(pattern, dateFormatSymbols)
{
        this.pattern = pattern;
        this.dateFormatSymbols = dateFormatSymbols ? dateFormatSymbols :
                new org_apache_myfaces_DateFormatSymbols();
}

//----------------------------------------------------------------------------
// Handle both parsing and formatting of dates, by walking the pattern and
// invoking _handlePatternSub for each "segment" of the pattern.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._handle = function(dateStr, date, parse)
    {
        var patternIndex = 0;
        var commentMode = false;
        var lastChar = 0;
        var currentChar=0;
        var nextChar=0;
        var patternSub = null;
        var ops = [];
        var i;
        var controls="yxMdEHhmsaw";
        var wwPresent = false;
        var xxPresent = false;

        var context = new org_apache_myfaces_SimpleDateFormatParserContext();

        if(date != null)
        {
            context.year = this._fullYearFromDate(date.getYear());
            context.month = date.getMonth();
            context.day = date.getDate();
            context.dayOfWeek = date.getDay();
            context.hour = date.getHours();
            context.min = date.getMinutes();
            context.sec = date.getSeconds();

            context.weekOfWeekYear = this._weekNbr(date);
            if ((context.weekOfWeekYear > 50) && (context.month==0))
                context.weekYear = context.year - 1;
            else if ((context.weekOfWeekYear == 1) && (context.month==12))
                context.weekYear = context.year + 1;
            else
                context.weekYear = context.year;
        }


        // Walk through the date pattern char by char, splitting it up into
        // "comment segments", "pattern segments" and "literal segments". 
        while (patternIndex < this.pattern.length)
        {
            currentChar = this.pattern.charAt(patternIndex);

            if(patternIndex<(this.pattern.length-1))
            {
                nextChar = this.pattern.charAt(patternIndex+1);
            }
            else
            {
                nextChar = 0;
            }


            if (currentChar == '\'' && lastChar!='\\')
            {
                if (patternSub != null)
                {
                    this._append(ops, patternSub);
                    patternSub = null;
                }
                commentMode = !commentMode;
            }
            else if (commentMode)
            {
                if (patternSub == null)
                {
                    patternSub = "c:";
                }
                patternSub += currentChar;
            }
            else
            {
                if (currentChar == '\\' && lastChar!='\\')
                {
                }
                else
                {
                    if(patternSub == null)
                    {
                        if (controls.indexOf(currentChar)<0)
                            patternSub = "l:";
                        else
                            patternSub = "f:";
                    }
                    patternSub+=currentChar;
                    if(currentChar != nextChar)
                    {
                        this._append(ops, patternSub);
                        patternSub = null;
                    }

                }
            }

            patternIndex++;
            lastChar = currentChar;
        }

        if (patternSub != null)
            this._append(ops, patternSub);

        // Ugly hack to make the "yyyy" formatter behave differently if the
        // "ww" formatter is also present in the pattern. This is what
        // java.text.SimpleDateFormat does. The JODA library implements
        // an "xxxx" formatter instead, and allows "yyyy" to behave normally;
        // this is nicer so support that if formatter "xx" is present.
        for(i=0;i<ops.length;++i)
        {
            var s = ops[i];
            wwPresent = wwPresent || ("f:ww" == s.substr(0,4));
            xxPresent = xxPresent || ("f:xx" == s.substr(0,4));
        }
        context.yearIsWeekYear = wwPresent && !xxPresent;

        if (parse)
            context = this._parseOps(context, ops, dateStr);
        else
            context = this._formatOps(context, ops);

        return context;
    };

org_apache_myfaces_SimpleDateFormat.prototype._parseOps = function(context, ops, dateStr)
{
    var dateIndex = 0;
    var i;
    for(i=0;i<ops.length;++i)
    {
        var op = ops[i];
        var optype = op.substr(0,2);
        var opval = op.substr(2);
        if (optype == "f:")
        {
            this._handlePatternSub(context, opval,
                dateStr, dateIndex, true);

            if(context.newIndex<0)
                 break;

            dateIndex = context.newIndex;
        }
        else if (optype == "l:")
        {
            // Just skip over the equivalent number of chars. This is what the previous
            // parsing implementation did...
            dateIndex += opval.length;
        }
        else if (optype == "c:")
        {
            // verify that opval matches the next chars in dateStr, then
            // advance dateIndex by the length.
            var s = dateStr.substr(dateIndex, opval.length);
            if (s != opval)
            {
                context.retValue = -1;
                break;
            }
            dateIndex += opval.length;
        }
    }
    
    return context;
}

org_apache_myfaces_SimpleDateFormat.prototype._formatOps = function(context, ops)
{
    var dateIndex = 0; // not used on formatting, only on parsing
    var dateStr = ""; // not used on formatting, only on parsing
    var i;
    for(i=0;i<ops.length;++i)
    {
        var op = ops[i];
        var optype = op.substr(0,2);
        var opval = op.substr(2);
        if (optype == "f:")
        {
            this._handlePatternSub(context, opval,
                dateStr, dateIndex, false);
        }
        else if (optype == "l:")
        {
            // just output the literal sequence into the result
            context.dateStr += opval;
        }
        else if (optype == "c:")
        {
            // just output the literal sequence into the result
            context.dateStr += opval;
        }
    }
    
    return context;
}

//----------------------------------------------------------------------------
// Parse a string using the configured pattern, and return a normal javascript
// Date object.
//
// Returns null if parsing failed.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype.parse = function(dateStr)
    {
        if(!dateStr || dateStr.length==0)
            return null;

        var context = this._handle(dateStr, null, true);

        if(context.retValue==-1)
            return null;

        this._adjustTwoDigitYear(context);

        return this._createDateFromContext(context);
    };

//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._createDateFromContext=function(context)
    {
        var date;
        if (context.weekOfWeekYear != 0)
        {
            date = org_apache_myfaces_SimpleDateFormat_newDateByWeekYear(
                context.weekYear, context.weekOfWeekYear, context.day,
                context.hour, context.min, context.sec);
        }
        else
        {
            date = new Date(
                context.year, context.month, context.day,
                context.hour,context.min,context.sec);
        }
        return date;
    };

//----------------------------------------------------------------------------
// Accept a normal javascript Date object, and return a String.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype.format = function(date)
    {
        var context = this._handle(null, date, false);

        return context.dateStr;
    };

//----------------------------------------------------------------------------
// dateStr is the full string currently being parsed.
// dateIndex is the offset within dateStr to parse from.
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._parseString = function(context, dateStr, dateIndex, strings)
    {
        var fragment = dateStr.substr(dateIndex);
        var index = this._prefixOf(strings, fragment);
        if (index != -1) {
          context.retValue = index;
          context.newIndex = dateIndex + strings[index].length;
          return context;
        }

        context.retValue=-1;
        context.newIndex=-1;
        return context;
    };

//----------------------------------------------------------------------------
// Convert at most the next posCount characters to numeric (or stop at
// end-of-string), starting from offset dateIndex within dateStr.
//
// dateStr is the full string currently being parsed.
// dateIndex is the offset within dateStr to parse from.
//
// Stores the result in context.retValue
// Updates context.newIndex to contain the next unparsed char within dateStr
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._parseNum = function(context, dateStr, posCount, dateIndex)
    {
        // Try to convert the most possible characters (posCount). If that fails,
        // then try again without the last character. Repeat until successful
        // numeric conversion occurs.
        for(var i=Math.min(posCount,dateStr.length-dateIndex);i>0;i--)
        {
            var numStr = dateStr.substring(dateIndex,dateIndex+i);

            context.retValue = this._parseInt(numStr);

            if(context.retValue == -1)
                continue;

            context.newIndex = dateIndex+i;
            return context;
        }

        context.retValue = -1;
        context.newIndex = -1;
        return context;
    };

//----------------------------------------------------------------------------
// Handles a "segment" of a pattern, for either parsing or formatting.
//
// patternSub contains a sequence of identical chars, eg "yyyy" or "HH".
// returns VOID
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._handlePatternSub = function(context, patternSub, dateStr, dateIndex, parse)
    {
        if(patternSub==null || patternSub.length==0)
            return;

        if(patternSub.charAt(0)=='y')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,4,dateIndex);

                if((context.newIndex-dateIndex)<4)
                {
                    context.year = context.retValue+1900;
                    context.ambigousYear = true;
                }
                else
                {
                    context.year = context.retValue;
                }
                
                if (context.yearIsWeekYear)
                    context.weekYear = context.year;
            }
            else
            {
                if (context.yearIsWeekYear)
                    this._formatNum(context,context.weekYear,patternSub.length <= 3 ? 2 : 4,true);
                else
                    this._formatNum(context,context.year,patternSub.length <= 3 ? 2 : 4,true);
            }
        }
        else if(patternSub.charAt(0)=='x')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,4,dateIndex);

                if((context.newIndex-dateIndex)<4)
                {
                    context.weekYear = context.retValue+1900;
                    context.ambigousYear = true;
                }
                else
                {
                    context.weekYear = context.retValue;
                }
            }
            else
            {
                this._formatNum(context,context.weekYear,patternSub.length <= 3 ? 2 : 4,true);
            }
        }
        else if(patternSub.charAt(0)=='M')
        {
            if(parse)
            {
              if (patternSub.length == 3) {
                var fragment = dateStr.substr(dateIndex, 3);
                var index = this._indexOf(this.dateFormatSymbols.shortMonths, fragment);
                if (index != -1) {
                  context.month = index;
                  context.newIndex = dateIndex + 3;
                }
              } else if (patternSub.length >= 4) {
                var fragment = dateStr.substr(dateIndex);
                var index = this._prefixOf(this.dateFormatSymbols.months, fragment);
                if (index != -1) {
                  context.month = index;
                  context.newIndex = dateIndex + this.dateFormatSymbols.months[index].length;
                }
              } else {
                this._parseNum(context, dateStr,2,dateIndex);
                context.month = context.retValue-1;
              }
            }
            else
            {
                if (patternSub.length == 3) {
                  context.dateStr += this.dateFormatSymbols.shortMonths[context.month];
                } else if (patternSub.length >= 4) {
                  context.dateStr += this.dateFormatSymbols.months[context.month];
                } else {
                  this._formatNum(context,context.month+1,patternSub.length);
                }
            }
        }
        else if(patternSub.charAt(0)=='d')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.day = context.retValue;
            }
            else
            {
                this._formatNum(context,context.day,patternSub.length);
            }
        }
        else if(patternSub.charAt(0)=='E')
        {
            if(parse)
            {
              // XXX dayOfWeek is not used to generate date at the moment
              if (patternSub.length <= 3) {
                var fragment = dateStr.substr(dateIndex, 3);
                var index = this._indexOf(this.dateFormatSymbols.shortWeekdays, fragment);
                if (index != -1) {
                  context.dayOfWeek = index;
                  context.newIndex = dateIndex + 3;
                }
              } else {
                var fragment = dateStr.substr(dateIndex);
                var index = this._prefixOf(this.dateFormatSymbols.weekdays, fragment);
                if (index != -1) {
                  context.dayOfWeek = index;
                  context.newIndex = dateIndex + this.dateFormatSymbols.weekdays[index].length;
                }
              }
            }
            else
            {
              if (patternSub.length <= 3) {
                context.dateStr += this.dateFormatSymbols.shortWeekdays[context.dayOfWeek];
              } else {
                context.dateStr += this.dateFormatSymbols.weekdays[context.dayOfWeek];
              }
            }
        }
        else if(patternSub.charAt(0)=='H' ||
                patternSub.charAt(0)=='h')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.hour = context.retValue;
            }
            else
            {
                this._formatNum(context,context.hour,patternSub.length);
            }
        }
        else if(patternSub.charAt(0)=='m')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.min = context.retValue;
            }
            else
            {
                this._formatNum(context,context.min,patternSub.length);
            }
        }
        else if(patternSub.charAt(0)=='s')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.sec = context.retValue;
            }
            else
            {
                this._formatNum(context,context.sec,patternSub.length);
            }
        }
        else if(patternSub.charAt(0)=='a')
        {
            if(parse)
            {
                this._parseString(context, dateStr,dateIndex,this.dateFormatSymbols.ampms);
                context.ampm = context.retValue;
            }
            else
            {
                context.dateStr += this.dateFormatSymbols.ampms[context.ampm];
            }
        }
        else if(patternSub.charAt(0)=='w')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.weekOfWeekYear = context.retValue;
            }
            else
            {
                this._formatNum(context,context.weekOfWeekYear,patternSub.length);
            }
        }
        else
        {
            if(parse)
            {
                // just skip over it
                context.newIndex=dateIndex+patternSub.length;
            }
            else
            {
                // just copy it to the output
                context.dateStr+=patternSub;
            }

        }
    };

//----------------------------------------------------------------------------
// Write out an integer padded with leading zeros to a specified width
// If ensureLength is set, and the number is longer than length, then display only the rightmost length digits.
//
// modifies member variable context.dateStr, returns VOID
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._formatNum = function (context, num, length, ensureLength)
    {
        var str = num+"";

        while(str.length<length)
            str="0"+str;

        // XXX do we have to distinguish left and right 'cutting'
        //ensureLength - enable cutting only for parameters like the year, the other
        if (ensureLength && str.length > length) {
          str = str.substr(str.length - length);
        }

        context.dateStr+=str;
    };

    // perhaps add to Array.prototype
org_apache_myfaces_SimpleDateFormat.prototype._indexOf = function (array, value)
    {
      for (var i = 0; i < array.length; ++i) {
        if (array[i] == value) {
          return i;
        }
      }
      return -1;
    };

org_apache_myfaces_SimpleDateFormat.prototype._prefixOf = function (array, value)
    {
      for (var i = 0; i < array.length; ++i) {
        if (value.indexOf(array[i]) == 0) {
          return i;
        }
      }
      return -1;
    };

org_apache_myfaces_SimpleDateFormat.prototype._parseInt = function(value)
    {
        var sum = 0;

        for(var i=0;i<value.length;i++)
        {
            var c = value.charAt(i);

            if(c<'0'||c>'9')
            {
                return -1;
            }
            sum = sum*10+(c-'0');
        }

        return sum;
    };
org_apache_myfaces_SimpleDateFormat.prototype._fullYearFromDate = function(year)
    {

        var yearStr = year+"";

        if (yearStr.length < 4)
        {
            return year+1900;
        }

        return year;
    };

org_apache_myfaces_SimpleDateFormat.prototype._adjustTwoDigitYear = function(context)
    {

        if(context.ambigousYear)
        {
            var date = this._createDateFromContext(context);
            var threshold = this.dateFormatSymbols.twoDigitYearStart;

            if(date.getTime()<threshold.getTime())
                context.year += 100;
        }
    };

org_apache_myfaces_SimpleDateFormat.prototype._append = function(array, obj)
    {
        array[array.length] = obj;
    };

//----------------------------------------------------------------------------
// Make static method callable via short name
//----------------------------------------------------------------------------
org_apache_myfaces_SimpleDateFormat.prototype._weekNbr=org_apache_myfaces_SimpleDateFormat_weekNbr;

