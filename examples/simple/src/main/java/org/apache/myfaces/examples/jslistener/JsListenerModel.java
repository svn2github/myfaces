package org.apache.myfaces.examples.jslistener;

import javax.faces.model.SelectItem;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Martin Marinschek
 */
public class JsListenerModel
{
    private List options;
    private List optionItems;

    public List getOptions()
    {
        if(options == null)
        {
            options = new ArrayList();
            options.add("o1");
            options.add("o2");
            options.add("o3");
            options.add("o4");

            optionItems = new ArrayList();
            optionItems.add(new SelectItem("o1","Option 1"));
            optionItems.add(new SelectItem("o2","Option 2"));
            optionItems.add(new SelectItem("o3","Option 3"));
            optionItems.add(new SelectItem("o4","Option 4"));            
        }
        return options;
    }

    public void setOptions(List options)
    {
        this.options = options;
    }

    public List getOptionItems()
    {
        return optionItems;
    }
}
