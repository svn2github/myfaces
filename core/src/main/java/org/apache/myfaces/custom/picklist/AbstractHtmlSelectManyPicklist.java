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
package org.apache.myfaces.custom.picklist;

import org.apache.myfaces.component.html.ext.HtmlSelectManyListbox;

/**
 * A picklist component that allows to select items from one list to another
 * 
 * @JSFComponent
 *   name = "t:selectManyPicklist"
 *   class = "org.apache.myfaces.custom.picklist.HtmlSelectManyPicklist"
 *   tagClass = "org.apache.myfaces.custom.picklist.HtmlSelectManyPicklistTag"
 *   
 * @author Bruno Aranda (latest modification by $Author: lu4242 $)
 * @version $Revision: 663481 $ $Date: 2008-06-05 10:00:34 +0300 (Thu, 05 Jun 2008) $
 */
public class AbstractHtmlSelectManyPicklist extends HtmlSelectManyListbox
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlSelectManyPicklist";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PicklistRenderer";
    
    
}
 