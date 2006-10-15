/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.examples.dynaForm;

import org.apache.myfaces.custom.dynaForm.annot.ui.Temporal;

import javax.persistence.TemporalType;
import java.util.Date;

public class SimpleBean
{
    private String anyString;
    private long anyLong;
    private boolean anyBoolean;
    private Date anyDateTime;
    private Date anyDateOnly;

    public String getAnyString()
    {
        return anyString;
    }

    public void setAnyString(String anyString)
    {
        this.anyString = anyString;
    }

    public long getAnyLong()
    {
        return anyLong;
    }

    public void setAnyLong(long anyLong)
    {
        this.anyLong = anyLong;
    }

    public boolean isAnyBoolean()
    {
        return anyBoolean;
    }

    public void setAnyBoolean(boolean anyBoolean)
    {
        this.anyBoolean = anyBoolean;
    }

    public Date getAnyDateTime()
    {
        return anyDateTime;
    }

    public void setAnyDateTime(Date anyDateTime)
    {
        this.anyDateTime = anyDateTime;
    }

    @Temporal(value=TemporalType.DATE)
    public Date getAnyDateOnly()
    {
        return anyDateOnly;
    }

    public void setAnyDateOnly(Date anyDateOnly)
    {
        this.anyDateOnly = anyDateOnly;
    }
}
