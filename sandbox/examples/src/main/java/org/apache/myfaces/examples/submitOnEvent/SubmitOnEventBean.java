package org.apache.myfaces.examples.submitOnEvent;

import java.util.Map;
import java.util.TreeMap;

public class SubmitOnEventBean
{
    private String lastAction;
    private Map strings = new TreeMap();

    public String linkAction()
    {
        lastAction="link";
        return null;
    }

    public String buttonAction()
    {
        lastAction="button";
        return null;
    }

    public String trapAction()
    {
        lastAction="trap - should not happen";
        return null;
    }

    public String getLastAction()
    {
        try
        {
            if (lastAction == null)
            {
                return "no action";
            }
            return lastAction;
        }
        finally
        {
            lastAction = null;
        }
    }

    public Map getStrings()
    {
        return strings;
    }

    public void setStrings(Map strings)
    {
        this.strings = strings;
    }
}
