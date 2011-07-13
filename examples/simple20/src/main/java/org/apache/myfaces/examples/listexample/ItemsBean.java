package org.apache.myfaces.examples.listexample;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="itemsBean")
@SessionScoped
public class ItemsBean
{

    private List<String> items;
    
    public List<String> getItems()
    {
        if (items == null)
        {
            items = new ArrayList<String>();
            items.add("A");
            items.add("B");
            items.add("C");
            items.add("D");
        }
        return items;
    }
    
    public void changeToRed(ActionEvent evt)
    {
        evt.getComponent().getAttributes().put("style", "background:red");
    }
}
