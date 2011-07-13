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
