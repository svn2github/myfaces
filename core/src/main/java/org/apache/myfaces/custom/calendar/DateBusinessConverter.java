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
package org.apache.myfaces.custom.calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Provide a bridge between the java.util.Date instance used by a component 
 * that receive date/time values and the "business" value used to represent
 * the value.
 * 
 * @since 1.1.10
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 691856 $ $Date: 2008-09-03 21:40:30 -0500 (mié, 03 sep 2008) $
 */
public interface DateBusinessConverter
{
    /**
     * Convert the java.util.Date instance calculated from submittedValue, 
     * so the resulting object will be used later as the converted value 
     * and validation. 
     * 
     * @param context
     * @param component
     * @param value
     * @return
     */
    public Object getBusinessValue(FacesContext context,
                       UIComponent component,
                       java.util.Date value);

    /**
     * Used to retrieve the value stored in the business bean and convert 
     * it in a representation that the component (t:inputCalendar and 
     * t:inputDate for example)using this class can manipulate. 
     *  
     * @param context
     * @param component
     * @param value
     * @return
     */
    public java.util.Date getDateValue(FacesContext context,
                       UIComponent component,
                       Object value);
}
