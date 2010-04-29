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
package org.apache.myfaces.examples.webapp;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.myfaces.shared_tomahawk.util.ClassUtils;

import java.io.*;

public class SourceCodeServlet extends HttpServlet 
{
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String webPage = req.getServletPath();
        
        // remove the '*.source' suffix that maps to this servlet
        int chopPoint = webPage.indexOf(".source");
        
        String path = webPage.substring(0, chopPoint);
        
        if (path.endsWith(".java"))
        {
            path = path.substring(0,path.length()-5);
            
            if (path.contains("org.apache.myfaces.examples"))
            {
                path = path.replace('.', '/') + ".java";

                InputStream is = ClassUtils.getResourceAsStream(path);
             
                // output an HTML page
                res.setContentType("text/plain");
    
                if (is != null)
                {
                    // print some html
                    ServletOutputStream out = res.getOutputStream();
        
                    // print the file
                    InputStream in = null;
                    try 
                    {
                        in = new BufferedInputStream(is);
                        int ch;
                        while ((ch = in.read()) !=-1) 
                        {
                            out.print((char)ch);
                        }
                    }
                    finally {
                        if (in != null) in.close();  // very important
                    }
                }
                else
                {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            else
            {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        else
        {
            
            webPage = webPage.substring(0, chopPoint - 3);
            webPage += "xhtml"; // replace jsf with xhtml
            
            // get the actual file location of the requested resource
            String realPath = getServletConfig().getServletContext().getRealPath(webPage);

            // output an HTML page
            res.setContentType("text/plain");

            // print some html
            ServletOutputStream out = res.getOutputStream();

            // print the file
            InputStream in = null;
            try 
            {
                in = new BufferedInputStream(new FileInputStream(realPath));
                int ch;
                while ((ch = in.read()) !=-1) 
                {
                    out.print((char)ch);
                }
            }
            finally {
                if (in != null) in.close();  // very important
            }
        }
    }
}
