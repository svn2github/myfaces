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

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class SimpleBean
{
    private String user;
    private long age;
    private Date creationDate;
    private Date birthday;
    private MartialStatus martialStatus;
    private String description;
    private boolean checkedData;

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public long getAge()
    {
        return age;
    }

    public void setAge(long age)
    {
        this.age = age;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    @Temporal(value=TemporalType.DATE)
    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public MartialStatus getMartialStatus()
    {
        return martialStatus;
    }

    public void setMartialStatus(MartialStatus martialStatus)
    {
        this.martialStatus = martialStatus;
    }

    public boolean isCheckedData()
    {
        return checkedData;
    }

    public void setCheckedData(boolean checkedData)
    {
        this.checkedData = checkedData;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
