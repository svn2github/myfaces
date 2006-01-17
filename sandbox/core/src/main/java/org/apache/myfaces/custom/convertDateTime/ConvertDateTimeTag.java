/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.custom.convertDateTime;

import javax.servlet.jsp.PageContext;

/**
 * Simple tag that overrides the spec ConvertDateTimeTag and uses TimeZone.getDefault() as the
 * base timezone, rather than GMT.
 * <p/>
 * User: treeder
 * Date: Oct 28, 2005
 * Time: 7:10:38 PM
 */
public class ConvertDateTimeTag extends org.apache.myfaces.taglib.core.ConvertDateTimeTag
{
    public ConvertDateTimeTag()
    {
        setConverterId(org.apache.myfaces.custom.convertDateTime.DateTimeConverter.CONVERTER_ID);
    }


    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        setConverterId(DateTimeConverter.CONVERTER_ID);
    }


}
