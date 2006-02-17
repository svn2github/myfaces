package org.apache.myfaces.examples.valueChangeNotifier;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NotifierBean {
	
	private static Log log = LogFactory.getLog(NotifierBean.class);
	
	private String selectedCategory;
	private List categories;
	
	public NotifierBean()
	{
		categories = new ArrayList();
		categories.add(new SelectItem("a", "Category A"));
		categories.add(new SelectItem("b", "Category b"));
	}
	
	public void valueChange(ValueChangeEvent vce) throws AbortProcessingException 
	{
		if(log.isInfoEnabled()){
            log.info("invoked valueChange method with " + vce.getNewValue() + " as its new value");
		}
		
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List getCategories() {
		return categories;
	}

	public void setCategories(List categories) {
		this.categories = categories;
	}

}