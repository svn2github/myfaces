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
package org.apache.myfaces.custom.roundedPanel;

import org.apache.myfaces.shared_tomahawk.util.StringUtils;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelProperties
{

    private int radius;
    private String backgroundColor;
    private String color;
    
    public RoundedPanelProperties(String encodedRoundedPanelProperties)
    {
        String[] array = StringUtils.splitLongString(encodedRoundedPanelProperties, '_');
        
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].startsWith("r"))
            {
                radius = Integer.parseInt(array[i].substring(1));
            }
            else if (array[i].startsWith("c"))
            {
                color = "#"+array[i].substring(1);
            }
            else if (array[i].startsWith("b"))
            {
                backgroundColor = "#"+array[i].substring(1);
            }
            else
            {
                throw new IllegalStateException("Invalid encodedRoundedPanelProperties");
            }
        }
    }

    public RoundedPanelProperties(int radius)
    {
        this.radius = radius;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    
    public String getBackgroundColor()
    {
        return backgroundColor;
    }

    public String getColor()
    {
        return color;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getEncodedRoundedPanelProperties()
    {
        return ("_r"+Integer.toString(radius))+
            (this.backgroundColor == null ? "" : "_b" + encodeColor(this.backgroundColor.trim()))+
            (this.color == null ? "" : "_c" + encodeColor(this.color.trim()));
    }
    
    private String encodeColor(String color)
    {
        if (color.charAt(0) == '#')
        {
            return color.substring(1);
        }
        return color;
    }

    @Override
    public String toString()
    {
        return "["+Integer.toString(radius)+"]";
    }
}
