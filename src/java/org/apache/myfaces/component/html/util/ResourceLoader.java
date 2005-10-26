/*
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
package org.apache.myfaces.component.html.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface ResourceLoader
{
    /**
     * Called by AddResource to render external resource data 
     * 
     * @param request the request 
     * @param response the response to write the resource content to
     * @param resourceUri contains the uri part after the uri which 
     * is used to identify the resource loader
     * 
     * @throws IOException
     */
    void serveResource(HttpServletRequest request, HttpServletResponse response, String resourceUri) throws IOException;
}