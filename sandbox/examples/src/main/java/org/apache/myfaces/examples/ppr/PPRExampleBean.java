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

package org.apache.myfaces.examples.ppr;

import org.apache.myfaces.examples.inputSuggestAjax.Address;

import javax.faces.FacesException;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ernst Fastl
 */
public class PPRExampleBean
{
    private String _textField;

    private String _message;

    private Boolean _checkBoxValue =Boolean.FALSE;

    private String _dropDownValue;

    private List _names;

    public List getNames()
    {
        if(_names == null)
            _names = getListMasterData();
        return _names;
    }

    public void setNames(List names)
    {
        _names = names;
    }

    public String getTextField()
    {
        return _textField;
    }

    public void setTextField(String textField)
    {
        this._textField = textField;
    }

    public String searchNames() {
        _names = getListMasterData();

        if(_textField == null || _textField.equals(""))
            return null;
	
	public String testExceptionAction() {
		throw new FacesException("Test PPR Exception Handling");
	}

        for (Iterator iterator = _names.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            String currentName = (String) o;
            if(-1 == currentName.toLowerCase().indexOf(_textField.toLowerCase()))
            {
                iterator.remove();
            }
        }
        return null;
    }

    public String doTimeConsumingStuff()
    {
        try
        {
            Thread.sleep(4000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List getPeriodicalUpdatedValues()
    {
        List refreshList = new ArrayList();

        for (int i = 0; i < 10; i++)
        {
            Address address = new Address((int) (Math.random()*100), "dummyStreet", "fakeCity",
                                                (int) (Math.random()*10), "noState");

            refreshList.add(address);
        }
        return refreshList;
    }

    private List getListMasterData()
    {
        List refreshList = new ArrayList();

        refreshList.add("robert johnson");
        refreshList.add("alpha romeo");
        refreshList.add("bernd billinger");
        refreshList.add("alfred wine");
        refreshList.add("gino lamberti");
        refreshList.add("michael jackson");
        refreshList.add("michael jordon");
        refreshList.add("arnold schwarzenegger");
        refreshList.add("richard gere");
        refreshList.add("scooby doo");
        refreshList.add("spider man");

        return refreshList;
    }

    public void testValueChangeListener(ValueChangeEvent event){

        _message = "Value Change to: ";
        if(event.getNewValue()!=null)
        {
            _message += event.getNewValue().toString();
        }
    }

    public Boolean getCheckBoxValue() {
        return _checkBoxValue;
    }

    public void setCheckBoxValue(Boolean changeCheckBox) {
        _checkBoxValue = changeCheckBox;
    }

    public String getDropDownValue() {
        return _dropDownValue;
    }

    public void setDropDownValue(String changeDropDown) {
        _dropDownValue = changeDropDown;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
    }


}
