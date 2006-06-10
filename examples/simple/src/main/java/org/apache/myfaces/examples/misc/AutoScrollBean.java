package org.apache.myfaces.examples.misc;

import java.util.List;
import java.util.ArrayList;

/**
 * Bean to demonstrate the org.apache.myfaces.AUTO_SCROLL init parameter behaviour
 *
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AutoScrollBean
{

    private List numbers;

    public AutoScrollBean()
    {
        this.numbers = new ArrayList(100);

        for (int i=0; i<100; i++)
        {
            numbers.add(String.valueOf(i));
        }
    }

    public List getNumbers()
    {
        return numbers;
    }

}
