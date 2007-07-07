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
 * --
 * $Id$
 */
package org.apache.myfaces.examples.roundeddiv;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

/**
 * Backing bean for the rounded div example.
 *
 * @author Andrew Robinson (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RoundedDivBean implements Serializable
{
    private String borderWidth = "8";
    private String radius = "8";
    private String borderColor = null;
    private String backgroundColor = null;
    private String color = "#CCCCCC";
    private String corners = null;
    private String size = null;
    private String height = "100px";
    private String width = "200px";

    public String getHeight()
    {
        return this.height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getWidth()
    {
        return this.width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getBackgroundColor()
    {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor()
    {
        return this.borderColor;
    }

    public void setBorderColor(String borderColor)
    {
        this.borderColor = borderColor;
    }

    public String getBorderWidth()
    {
        return this.borderWidth;
    }

    public void setBorderWidth(String borderWidth)
    {
        this.borderWidth = borderWidth;
    }

    public String getColor()
    {
        return this.color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getCorners()
    {
        return this.corners;
    }

    public void setCorners(String corners)
    {
        this.corners = corners;
    }

    public String getRadius()
    {
        return this.radius;
    }

    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    public String getSize()
    {
        return this.size;
    }

    public void setSize(String size)
    {
        if ("".equals(size))
        {
            size = null;
        }
        this.size = size;
    }

    public void reset(ActionEvent evt)
    {
        borderWidth = "8";
        radius = "8";
        borderColor = null;
        backgroundColor = null;
        color = "#CCCCCC";
        corners = null;
        size = null;
    }
}
