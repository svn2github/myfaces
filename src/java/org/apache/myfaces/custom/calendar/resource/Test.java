package org.apache.myfaces.custom.calendar.resource;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: developer
 * Date: 05.10.2005
 * Time: 14:53:18
 * To change this template use File | Settings | File Templates.
 */
public class Test
{
    private String pattern;

    private String[] ampms = new String[]{"AM","PM"};

    public Test(String pattern)
    {
        this.pattern = pattern;
    }

    private Date parse(String dateStr)
    {
        Context context = handle(dateStr, null, true);

        return new Date(context.year, context.month,
                context.day,context.hour,context.min,context.sec);
    }

    private Context handle(String dateStr, Date date, boolean parse)
    {
        int patternIndex = 0;
        int dateIndex = 0;
        boolean commentMode = false;
        char lastChar = 0;
        char currentChar=0;
        char nextChar=0;
        String patternSub = null;

        Context context = new Context();

        if(date != null)
        {
            context.year = date.getYear();
            context.month = date.getMonth();
            context.day = date.getDate();
            context.hour = date.getHours();
            context.min = date.getMinutes();
            context.sec = date.getSeconds();
        }

        while (patternIndex < pattern.length())
        {
            currentChar = pattern.charAt(patternIndex);

            if(patternIndex<(pattern.length()-1))
            {
                nextChar = pattern.charAt(patternIndex+1);
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
                            handlePatternSub(context, patternSub,
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
                        if(pattern.charAt(patternIndex)!=dateStr.charAt(dateIndex))
                        {
                            //invalid character in dateString
                            return null;
                        }
                    }
                    else
                    {
                        context.dateStr+=pattern.charAt(patternIndex);
                    }

                    patternIndex++;
                    dateIndex++;
                }
            }

            lastChar = currentChar;
        }

        handlePatternSub(context, patternSub,
                dateStr, dateIndex, parse);

        return context;
    }

    private Context parseString(Context context, String dateStr, int dateIndex, String[][] strings)
    {
        for(int i=0; i<strings.length;i++)
        {
            String currentStrings[] = strings[0];

            for (int j = 0; j < currentStrings.length; j++)
            {
                String currentString = currentStrings[j];

                if(dateStr.substring(dateIndex,dateIndex+currentString.length()).equals(currentString))
                {
                    context.newIndex=dateIndex+currentString.length();
                    context.retValue=j;
                    return context;
                }
            }
        }

        context.newIndex=-1;
        return context;
    }

    private Context parseNum(Context context, String dateStr, int posCount, int dateIndex)
    {
        for(int i=posCount;i>0;i--)
        {
            String numStr = dateStr.substring(dateIndex,dateIndex+posCount);

            try
            {
                context.retValue = Integer.parseInt(numStr);
                context.newIndex = dateIndex+posCount;
                return context;
            }
            catch(NumberFormatException ex)
            {
            }
        }

        context.newIndex = -1;
        return context;
    }

    private void handlePatternSub(Context context, String patternSub, String dateStr,
                                  int dateIndex, boolean parse)
    {
        if(patternSub==null || patternSub.length()==0)
            return;

        if(patternSub.charAt(0)=='y')
        {
            if(parse)
            {
                parseNum(context, dateStr,4,dateIndex);
                context.year = context.retValue;
            }
            else
            {
                formatNum(context,context.year,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='M')
        {
            if(parse)
            {
                parseNum(context, dateStr,2,dateIndex);
                context.month = context.retValue-1;
            }
            else
            {
                formatNum(context,context.month+1,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='d')
        {
            if(parse)
            {
                parseNum(context, dateStr,2,dateIndex);
                context.day = context.retValue;
            }
            else
            {
                formatNum(context,context.day,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='H' ||
                patternSub.charAt(0)=='h')
        {
            if(parse)
            {
                parseNum(context, dateStr,2,dateIndex);
                context.hour = context.retValue;
            }
            else
            {
                formatNum(context,context.hour,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='m')
        {
            if(parse)
            {
                parseNum(context, dateStr,2,dateIndex);
                context.min = context.retValue;
            }
            else
            {
                formatNum(context,context.min,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='s')
        {
            if(parse)
            {
                parseNum(context, dateStr,2,dateIndex);
                context.sec = context.retValue;
            }
            else
            {
                formatNum(context,context.sec,patternSub.length());
            }
        }
        else if(patternSub.charAt(0)=='a')
        {
            if(parse)
            {
                parseString(context, dateStr,dateIndex,new String[][]{ampms});
                context.ampm = context.retValue;
            }
            else
            {
                formatNum(context,context.ampm,patternSub.length());
            }
        }
        else
        {
            if(parse)
            {
                context.newIndex=dateIndex+patternSub.length();
            }
            else
            {
                context.dateStr+=patternSub;
            }

        }
    }

    private void formatNum(Context context, int num, int length)
    {
        String str = num+"";

        while(str.length()<length)
            str="0"+str;

        context.dateStr+=str;
    }

    private String format(Date date)
    {
        Context context = handle(null, date, false);

        return context.dateStr;
    }


    private static class Context
    {
        public int newIndex;
        public int retValue;
        public int year;
        public int month;
        public int day;
        public int hour;
        public int min;
        public int sec;
        public int ampm=0;
        String dateStr="";
    }

    public static void main(String[] args)
    {
        Test test = new Test(" dd/MM/yyyy \\'yyyy\\' HH:mm");

        Date date = test.parse(" 11/06/2005 yyyy 00:30");

        System.out.println(test.format(date));
    }

}
