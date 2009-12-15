/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.examples.selectitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.myfaces.examples.listexample.SimpleCar;

public class SelectItemsBean {

    private List carList;
    
    private String selectedCarColor;
    
    private Map carMap;

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
    
    public Map getCarMap() {
        if (carMap == null) {
            carMap = createCarMap();
        }
        return carMap;
    }

    public void setCarMap(Map carMap)
    {
        this.carMap = carMap;
    }

    private Map createCarMap() {
        Map list = new TreeMap();
        list.put(new Integer(1), new SimpleCar(1, "Car 1", "blue"));
        list.put(new Integer(2), new SimpleCar(2, "Car 2", "white"));
        list.put(new Integer(3), new SimpleCar(3, "Car 3", "red"));
        list.put(new Integer(4), new SimpleCar(4, "Car 4", "green"));
        /*
        list.put(new Integer(1), "Car 1");
        list.put(new Integer(2), "Car 2");
        list.put(new Integer(3), "Car 3");
        list.put(new Integer(4), "Car 4");
        */
        return list;
    }
    
    public String getSelectedCarColor() {
        return selectedCarColor;
    }

    public void setSelectedCarColor(String selectedCarColor) {
        this.selectedCarColor = selectedCarColor;
    }
    
}
