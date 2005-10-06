var DateFormatSymbols = Class.create();
DateFormatSymbols.prototype = {
    initialize: function()
    {
        this.eras = new Array('BC', 'AD');
        this.months = new Array('January', 'February', 'March', 'April',
                'May', 'June', 'July', 'August', 'October',
                'November', 'December', 'Undecimber');
        this.months = new Array('Jan', 'Feb', 'Mar', 'Apr',
                'May', 'Jun', 'Jul', 'Aug', 'Oct',
                'Nov', 'Dec', 'Und');
        this.weekdays = new Array('', 'Sunday', 'Monday', 'Tuesday',
                'Wednesday', 'Thursday', 'Friday', 'Saturday');
        this.shortWeekdays = new Array('', 'Sun', 'Mon', 'Tue',
                'Wed', 'Thu', 'Fri', 'Sat');
        this.ampms = new Array('AM', 'PM');
        this.zoneStrings = new Array(new Array(0, 'long-name', 'short-name'));
    },
}

var SimpleDateFormatParserContext = Class.create();
SimpleDateFormatParserContext.prototype = {
    initialize: function()
    {
        this.newIndex=0;
        this.retValue=0;
        this.year=0;
        this.month=0;
        this.day=0;
        this.hour=0;
        this.min=0;
        this.sec=0;
        this.ampm=0;
        this.dateStr="";
    },
}

var SimpleDateFormat = Class.create();
SimpleDateFormat.prototype = {
    initialize: function(pattern, dateFormatSymbols)
    {
        this.pattern = pattern;
        this.dateFormatSymbols = dateFormatSymbols;
    },


    _handle: function(dateStr, date, parse)
    {
        var patternIndex = 0;
        var dateIndex = 0;
        var commentMode = false;
        var lastChar = 0;
        var currentChar=0;
        var nextChar=0;
        var patternSub = null;

        var context = new SimpleDateFormatParserContext();

        if(date != null)
        {
            var yearStr = date.getYear()+"";

            if (yearStr.length < 4)
            {
                  yearStr=(parseInt(yearStr)+1900)+"";
            }

            context.year = parseInt(yearStr);
            context.month = date.getMonth();
            context.day = date.getDate();
            context.hour = date.getHours();
            context.min = date.getMinutes();
            context.sec = date.getSeconds();
        }

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
                commentMode = !commentMode;
                patternIndex++;
            }
            else
            {
                if(!commentMode)
                {
                    if (currentChar == '\\' && lastChar!='\\')
                    {
                        patternIndex++;
                    }
                    else
                    {
                        if(patternSub == null)
                            patternSub = "";

                        patternSub+=currentChar;

                        if(currentChar != nextChar)
                        {
                            this._handlePatternSub(context, patternSub,
                                    dateStr, dateIndex, parse);

                            patternSub = null;

                            if(context.newIndex<0)
                                break;

                            dateIndex = context.newIndex;
                        }

                        patternIndex++;
                    }
                }
                else
                {
                    if(parse)
                    {
                        if(this.pattern.charAt(patternIndex)!=dateStr.charAt(dateIndex))
                        {
                            //invalid character in dateString
                            return null;
                        }
                    }
                    else
                    {
                        context.dateStr+=this.pattern.charAt(patternIndex);
                    }

                    patternIndex++;
                    dateIndex++;
                }
            }

            lastChar = currentChar;
        }

        this._handlePatternSub(context, patternSub,
                dateStr, dateIndex, parse);

        return context;
    },

    parse: function(dateStr)
    {
        var context = this._handle(dateStr, null, true);

        return new Date(context.year, context.month,
                context.day,context.hour,context.min,context.sec);
    },

    format: function(date)
    {
        var context = this._handle(null, date, false);

        return context.dateStr;
    },

    parseString: function(context, dateStr, dateIndex, strings)
    {
        for(var i=0; i<strings.length;i++)
        {
            var currentStrings = strings[0];

            for (var j = 0; j < currentStrings.length; j++)
            {
                var currentString = currentStrings[j];

                if(dateStr.substring(dateIndex,dateIndex+currentString.length).equals(currentString))
                {
                    context.newIndex=dateIndex+currentString.length;
                    context.retValue=j;
                    return context;
                }
            }
        }

        context.newIndex=-1;
        return context;
    },

    _parseNum: function(context, dateStr, posCount, dateIndex)
    {
        for(var i=posCount;i>0;i--)
        {
            var numStr = dateStr.substring(dateIndex,dateIndex+posCount);

            context.retValue = parseInt(numStr);

            if(context.retValue == Number.NaN)
                continue;

            context.newIndex = dateIndex+posCount;
            return context;
        }

        context.newIndex = -1;
        return context;
    },

    _handlePatternSub: function(context, patternSub, dateStr,
                                  dateIndex, parse)
    {
        if(patternSub==null || patternSub.length==0)
            return;

        if(patternSub.charAt(0)=='y')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,4,dateIndex);
                context.year = context.retValue;
            }
            else
            {
                this._formatNum(context,context.year,patternSub.length);
            }
        }
        else if(patternSub.charAt(0)=='M')
        {
            if(parse)
            {
                this._parseNum(context, dateStr,2,dateIndex);
                context.month = context.retValue-1;
            }
            else
            {
                this._formatNum(context,context.month+1,patternSub.length);
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
                parseString(context, dateStr,dateIndex,new Array(ampms));
                context.ampm = context.retValue;
            }
            else
            {
                this._formatNum(context,context.ampm,patternSub.length);
            }
        }
        else
        {
            if(parse)
            {
                context.newIndex=dateIndex+patternSub.length;
            }
            else
            {
                context.dateStr+=patternSub;
            }

        }
    },

    _formatNum: function (context, num, length)
    {
        var str = num+"";

        while(str.length<length)
            str="0"+str;

        context.dateStr+=str;
    }
}