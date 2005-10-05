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

var SimpleDateFormat = Class.create();
SimpleDateFormat.prototype = {
    initialize: function(pattern, dateFormatSymbols)
    {
        this.pattern = pattern;
        this.dateFormatSymbols = dateFormatSymbols;
    },

    format: function(date)
    {

    },

    parse: function(dateStr)
    {
        var patternIndex = 0;
        var dateIndex = 0;
        var commentMode = false;
        var lastChar = -1;
        var patternSub = null;

        var year;
        var month;
        var day;
        var hour;
        var min;
        var sec;

        while (patternIndex < pattern.length)
        {
            c = pattern.charAt(patternIndex);

            if (c == '\'' && lastChar!='\\')
            {
                commentMode = !commentMode;
                patternIndex++;
            }
            else
            {
                if(!commentMode)
                {
                    if (c == '\\' && lastChar!='\\')
                    {
                        patternIndex++;
                    }
                    else if(c == lastChar)
                    {
                        patternSub+=c;
                        patternIndex++;
                        dateIndex++;
                    }
                    else
                    {
                        if(patternSub != null)
                        {
                            handlePatternSub(patternSub, dateStr.substring(dateIndex-patternSub.length,dateIndex))
                            patternSub = null;
                        }
                        else
                        {
                            if(pattern.charAt(patternIndex)!=dateStr.charAt(dateIndex))
                            {
                                //invalid character in dateString
                                return null;
                            }
                        }
                    }
                }
                else
                {
                    if(pattern.charAt(patternIndex)!=dateStr.charAt(dateIndex))
                    {
                        //invalid character in dateString
                        return null;
                    }
                }
            }

            lastChar = c;
        }
    },
}