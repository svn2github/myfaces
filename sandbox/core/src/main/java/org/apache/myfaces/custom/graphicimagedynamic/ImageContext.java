/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.graphicimagedynamic;

import java.util.Map;


/**
 * The ImageContext class holds additional objects and values which can be used 
 * to determine which or how an image should be rendererd. 
 * 
 * @author Mathias Broekelmann
 * @version $Revision$ $Date$
 *
 */
public interface ImageContext
{
    /**
     * Returns a map with the values of the used parameters
     * 
     * @return not null, Map contains instances of String for keys and values
     */
    Map getParamters();
    
    /**
     * Returns the desired width of the image
     * 
     * @return null if no width is defined
     */
    Integer getWidth();

    /**
     * Returns the desired height of the image
     * 
     * @return null if no height is defined
     */
    Integer getHeight();
}
