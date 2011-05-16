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
package org.apache.myfaces.custom.effect;

public class EffectUtils
{
    
    public static boolean addProperty(StringBuilder sb, String name, Object value, boolean addComma)
    {
        if (value != null)
        {
            if (addComma)
            {
                sb.append(',');
            }
            sb.append(name);
            sb.append(':');
            sb.append(value);
            return true;
        }
        else
        {
            return addComma;
        }
    }
    
    public static boolean addStringProperty(StringBuilder sb, String name, Object value, boolean addComma)
    {
        if (value != null)
        {
            if (addComma)
            {
                sb.append(',');
            }
            sb.append(name);
            sb.append(':');
            sb.append('\'');
            sb.append(value);
            sb.append('\'');
            return true;
        }
        else
        {
            return addComma;
        }
    }    

}
