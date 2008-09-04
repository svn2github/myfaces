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
package org.apache.myfaces.webapp.filter;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

/**
 * This class is used to retrieve the context paramaters used to initialize 
 * of the MultipartRequestWrapper.
 * 
 * @author Hazem Saleh
 * 
 */
public class MultipartRequestWrapperConfig
{
    
    private int _uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB
    private int _uploadThresholdSize = 1 * 1024 * 1024; // 1 MB
    private String _uploadRepositoryPath = null; //standard temp directory 
    
    private MultipartRequestWrapperConfig() {}
        
    private static int resolveSize(String param, int defaultValue)
    {
        int numberParam = defaultValue;

        if (param != null)
        {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("g"))
            {
                factor = 1024 * 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            }
            else if (param.endsWith("m"))
            {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            }
            else if (param.endsWith("k"))
            {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            numberParam = Integer.parseInt(number) * factor;
        }
        return numberParam;
    }
    
    public int getUploadMaxFileSize()
    {
        return _uploadMaxFileSize;
    }

    public void setUploadMaxFileSize(int uploadMaxFileSize)
    {
        this._uploadMaxFileSize = uploadMaxFileSize;
    }

    public int getUploadThresholdSize()
    {
        return _uploadThresholdSize;
    }

    public void setUploadThresholdSize(int uploadThresholdSize)
    {
        this._uploadThresholdSize = uploadThresholdSize;
    }

    public String getUploadRepositoryPath()
    {
        return _uploadRepositoryPath;
    }

    public void setUploadRepositoryPath(String uploadRepositoryPath)
    {
        this._uploadRepositoryPath = uploadRepositoryPath;
    }    

    public static MultipartRequestWrapperConfig getMultipartRequestWrapperConfig(
            ExternalContext context)
    {
        MultipartRequestWrapperConfig config = new MultipartRequestWrapperConfig();

        ServletContext servletContext = (ServletContext) context.getContext();

        String param = servletContext.getInitParameter("uploadMaxFileSize");

        config._uploadMaxFileSize = resolveSize(param,
                config._uploadMaxFileSize);

        param = servletContext.getInitParameter("uploadThresholdSize");

        config._uploadThresholdSize = resolveSize(param,
                config._uploadThresholdSize);

        config._uploadRepositoryPath = servletContext
                .getInitParameter("uploadRepositoryPath");

        return config;
    }
}
