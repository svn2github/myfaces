package org.apache.myfaces.examples.selectitems;

import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.examples.selectOneRow.SimpleCar;

public class SelectItemsBean {

	private List carList;
	
	private String selectedCarColor;

	public List getCarList() {
		if(carList == null) {
			carList = createCarList();
		}		
		return carList;
	}

	public void setCarList(List list) {
		carList = list;
	}
	
	private List createCarList() {
		List list = new ArrayList();
		list.add(new SimpleCar(1, "Car 1", "blue"));
		list.add(new SimpleCar(2, "Car 2", "white"));
		list.add(new SimpleCar(3, "Car 3", "red"));
		list.add(new SimpleCar(4, "Car 4", "green"));
		return list;
	}

	public String getSelectedCarColor() {
		return selectedCarColor;
	}

	public void setSelectedCarColor(String selectedCarColor) {
		this.selectedCarColor = selectedCarColor;
	}
	
}
