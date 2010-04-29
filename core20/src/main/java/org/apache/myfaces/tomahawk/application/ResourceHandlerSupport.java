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
package org.apache.myfaces.tomahawk.application;

import javax.faces.context.FacesContext;

/**
 * A utility class to isolate a ResourceHandler implementation from its
 * underlying implementation
 * 
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 891494 $ $Date: 2009-12-16 19:42:18 -0500 (Mié, 16 Dic 2009) $
 */
public interface ResourceHandlerSupport
{

    /**
     * Calculate the resource base path.
     * 
     * It should extract a string like:
     * 
     * ResourceHandler.RESOURCE_IDENTIFIER + '/' + getResourceName()
     * 
     * For example:
     * 
     * /javax.faces.resource/image.jpg
     * 
     * This is used on ResourceHandler.handleResourceRequest()
     * 
     */
    String calculateResourceBasePath(FacesContext facesContext);
    
    /**
     * Check if the mapping used is done using extensions (.xhtml, .jsf)
     * or if it is not (/faces/*)
     * @return
     */
    boolean isExtensionMapping();
    
    /**
     * Get the mapping used as prefix(/faces) or sufix(.jsf)
     * 
     * @return
     */
    String getMapping();
    
}
