/*
 * SimpleSortableCarList2.java
 *
 * Created on April 19, 2006, 11:54 AM
 * 
 */

package org.apache.myfaces.examples.listexample;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catalin Kormos
 */
public class SimpleAutoSortableCarList
{
    private List _cars;    
    private String sortColumn = null;
    private boolean sortAscending = true;
    
    public SimpleAutoSortableCarList() 
    {
        _cars = new ArrayList();
        
        _cars.add(new SimpleCar(1, "car B", "blue"));
        _cars.add(new SimpleCar(2, "car A", "red"));        
        _cars.add(new SimpleCar(3, "car D", "yellow"));
        _cars.add(new SimpleCar(4, "car C", "green"));        
        _cars.add(new SimpleCar(5, "car E", "orange"));
        _cars.add(new SimpleCar(6, "car J", "blue"));
        _cars.add(new SimpleCar(7, "car I", "gray"));        
        _cars.add(new SimpleCar(8, "car M", "lightGray"));
        _cars.add(new SimpleCar(9, "car N", "magenta"));        
        _cars.add(new SimpleCar(10, "car K", "unknown"));
        _cars.add(new SimpleCar(11, "car L", "dark blue"));
    }    

    public List getCars() 
    {
        return _cars;
    }

    public void setCars(List cars) 
    {
        this._cars = cars;
    }   
    
    public String getSortColumn() 
    {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) 
    {
        this.sortColumn = sortColumn;
    }
    
    public boolean isSortAscending() 
    {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) 
    {
        this.sortAscending = sortAscending;
    }
}
