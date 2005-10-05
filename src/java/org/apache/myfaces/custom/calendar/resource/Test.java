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

    public Test(String pattern)
    {
        this.pattern = pattern;
    }

    private Date parse(String dateStr)
    {
        int patternIndex = 0;
        int dateIndex = 0;
        boolean commentMode = false;
        char lastChar = 0;
        char c=0;
        String patternSub = null;

        int year;
        int month;
        int day;
        int hour;
        int min;
        int sec;

        while (patternIndex < pattern.length())
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
                    }
                    else if(patternSub == null)
                    {
                        patternSub = "";
                        patternSub+=c;
                        patternIndex++;
                    }
                    else
                    {
                        /*dateStr.substring(dateIndex-patternSub.length(),dateIndex)*/

                        handlePatternSub(patternSub, dateStr, patternIndex, dateIndex);

                        if(patternSub.equals("yyyy") || patternSub.equals("yy")
                                || patternSub.equals("y"))
                        {

                        }
                        patternSub = "";
                    }
                }
                else
                {
                    if(pattern.charAt(patternIndex)!=dateStr.charAt(dateIndex))
                    {
                        //invalid character in dateString
                        return null;
                    }

                    patternIndex++;
                    dateIndex++;
                }
            }

            lastChar = c;
        }

        return null;
    }

    private void handlePatternSub(String patternSub, String dateStrSub,
                                  int patternIndex, int dateIndex)
    {
        if(patternSub.equals("yyyy"))
        {
            System.out.println(patternSub);
        }
    }

    public static void main(String[] args)
    {
        Test test = new Test("dd.MM.yyyy");

        test.parse("11.06.2005");
    }
}
