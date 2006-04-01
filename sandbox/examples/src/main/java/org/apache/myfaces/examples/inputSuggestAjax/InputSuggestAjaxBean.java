/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.inputSuggestAjax;

import javax.faces.model.SelectItem;
import java.util.List;
import java.util.ArrayList;

/**
 * Backing bean for the input-suggest-ajax component
 *
 * @author Gerald Müllan (latest modification by $Author$)
 * @version $Revision$
 */
public class InputSuggestAjaxBean
{
    private String suggestValue = null;

    public List getAddressList(String cityFragment)
    {
        List addressList = new ArrayList();

        addressList.add(new Address(11,"noname",cityFragment+"nocity",15,"KL"));
        addressList.add(new Address(12,"max",cityFragment+"muster",14,"SJ"));
        addressList.add(new Address(13,"phil",cityFragment+"philadelphia",13,"NW"));
        addressList.add(new Address(14,"new",cityFragment+"new york",12,"IL"));
        addressList.add(new Address(15,"san",cityFragment+"san francisco",11,"NY"));
        addressList.add(new Address(16,"san",cityFragment+"san diego",16,"MH"));

        return addressList;
    }

    public List getAddressList(String cityFragment, Integer maxSize)
    {
        List addressList = new ArrayList();

        if (cityFragment.startsWith("as"))
        {
            addressList.add(new Address(11,"noname",cityFragment+"nocity",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"muster",14,"SJ"));
            addressList.add(new Address(13,"phil",cityFragment+"philadelphia",13,"NW"));
            addressList.add(new Address(14,"new",cityFragment+"new york",12,"IL"));
            addressList.add(new Address(15,"san",cityFragment+"san francisco",11,"NY"));
            addressList.add(new Address(16,"san",cityFragment+"san diego",16,"MH"));
            addressList.add(new Address(11,"noname",cityFragment+"max",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"nomax",14,"SJ"));
            addressList.add(new Address(13,"phil",cityFragment+"detroit",13,"NW"));
            addressList.add(new Address(11,"noname",cityFragment+"nocity",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"muster",14,"SJ"));
            addressList.add(new Address(13,"phil",cityFragment+"philadelphia",13,"NW"));
            addressList.add(new Address(14,"new",cityFragment+"new york",12,"IL"));
            addressList.add(new Address(15,"san",cityFragment+"san francisco",11,"NY"));
            addressList.add(new Address(16,"san",cityFragment+"san diego",16,"MH"));
            addressList.add(new Address(11,"noname",cityFragment+"max",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"nomax",14,"SJ"));
            addressList.add(new Address(13,"phil",cityFragment+"detroit",13,"NW"));
        }
        else
        {
            addressList.add(new Address(11,"noname",cityFragment+"nocity",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"muster",14,"SJ"));
            addressList.add(new Address(13,"phil",cityFragment+"philadelphia",13,"NW"));
            addressList.add(new Address(14,"new",cityFragment+"new york",12,"IL"));
            addressList.add(new Address(15,"san",cityFragment+"san francisco",11,"NY"));
            addressList.add(new Address(16,"san",cityFragment+"san diego",16,"MH"));
            addressList.add(new Address(11,"noname",cityFragment+"max",15,"KL"));
            addressList.add(new Address(12,"max",cityFragment+"nomax",14,"SJ"));
        }

        return addressList;
    }

    public List getItems(String prefix)
    {
        List li = new ArrayList();
        li.add(prefix+1);
        li.add(prefix+2);
        li.add(prefix+3);
        li.add(prefix+4);
        li.add(prefix+5);
        li.add(prefix+6);
        return li;
    }

    public List getItems(String prefix, Integer maxSize) {

    	List li = new ArrayList();

    	for(int i = 0; i < maxSize.intValue(); i++) {
    		li.add(prefix+ " " +(i+1));
    	}

    	return li;
    }

    public String getSuggestValue()
    {
        return suggestValue;
    }

    public void setSuggestValue(String suggestValue)
    {
        this.suggestValue = suggestValue;
    }
}