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

import java.util.List;
import java.util.ArrayList;

/**
 * Backing bean for the input-suggest-ajax component
 *
 * @author Gerald Mullan (latest modification by $Author$)
 * @version $Revision$
 */
public class InputSuggestAjaxBean
{
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
}