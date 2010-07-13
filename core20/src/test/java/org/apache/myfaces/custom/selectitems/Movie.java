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
package org.apache.myfaces.custom.selectitems;

/**
 * @author cagatay (latest modification by $Author: lu4242 $)
 * @version $Revision: 783163 $ $Date: 2009-06-09 18:37:19 -0500 (Mar, 09 Jun 2009) $
 * Simple test entity
 */
public class Movie {
    private String name;
    
    private String director;
    
    private boolean disabled;
    
    private Boolean escaped;
    
    public Movie() {}
    
    public Movie(String name, String director) {
        this.name = name;
        this.director = director;
        this.disabled = false;
        this.escaped = Boolean.TRUE;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public Boolean getEscaped()
    {
        return escaped;
    }

    public void setEscaped(Boolean escaped)
    {
        this.escaped = escaped;
    }
}
