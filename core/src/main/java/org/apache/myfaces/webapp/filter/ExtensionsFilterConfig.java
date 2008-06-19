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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.util.xml.MyFacesErrorHandler;
import org.apache.myfaces.shared_tomahawk.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 *  This class is used to retrieve the configuration of the ExtensionsFilter
 * and read it by TomahawkFacesContexWrapper, because it is necessary for
 * initialize properly MultipartRequestWrapper.
 * 
 * For do that, it parse the file /WEB-INF/web.xml
 * 
 * It also uses a mechanism similar to shared WebXml to refresh the params.
 * 
 * @author Leonardo Uribe
 *
 */
public class ExtensionsFilterConfig
{
    
    private int _uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB

    private int _uploadThresholdSize = 1 * 1024 * 1024; // 1 MB

    private String _uploadRepositoryPath = null; //standard temp directory
    
    public int getUploadMaxFileSize()
    {
        return _uploadMaxFileSize;
    }

    protected void setUploadMaxFileSize(int uploadMaxFileSize)
    {
        this._uploadMaxFileSize = uploadMaxFileSize;
    }

    protected int getUploadThresholdSize()
    {
        return _uploadThresholdSize;
    }

    protected void setUploadThresholdSize(int uploadThresholdSize)
    {
        this._uploadThresholdSize = uploadThresholdSize;
    }

    public String getUploadRepositoryPath()
    {
        return _uploadRepositoryPath;
    }

    protected void setUploadRepositoryPath(String uploadRepositoryPath)
    {
        this._uploadRepositoryPath = uploadRepositoryPath;
    }    
    
    //Static stuff
    
    private static final String WEB_XML_PATH = "/WEB-INF/web.xml";
    private static final String WEB_APP_2_2_J2EE_SYSTEM_ID = "http://java.sun.com/j2ee/dtds/web-app_2_2.dtd";
    private static final String WEB_APP_2_2_SYSTEM_ID = "http://java.sun.com/dtd/web-app_2_2.dtd";
    private static final String WEB_APP_2_2_RESOURCE  = "javax/servlet/resources/web-app_2_2.dtd";

    private static final String WEB_APP_2_3_SYSTEM_ID = "http://java.sun.com/dtd/web-app_2_3.dtd";
    private static final String WEB_APP_2_3_RESOURCE  = "javax/servlet/resources/web-app_2_3.dtd";
    
    private static final Log log = LogFactory.getLog(ExtensionsFilterConfig.class);
    
    private long parsingTime;
    
    private static long refreshPeriod;    
    
    protected void setParsingTime(long parsingTime){
        this.parsingTime = parsingTime;
    }
    
    public static ExtensionsFilterConfig parse(ExternalContext context)
    {
        ExtensionsFilterConfigParser parser = new ExtensionsFilterConfigParser(context);
        return parser.parse();
    }
    
    protected static class ExtensionsFilterConfigParser
    {
        private ExternalContext _context;
        private ExtensionsFilterConfig _extensionsFilterConfig;
        
        public ExtensionsFilterConfigParser(ExternalContext context)
        {
            _context = context;
        }
        
        public ExtensionsFilterConfig parse()
        {
            _extensionsFilterConfig = new ExtensionsFilterConfig();

            try
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setIgnoringElementContentWhitespace(true);
                dbf.setIgnoringComments(true);
                dbf.setNamespaceAware(true);
                dbf.setValidating(false);
//                dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

                DocumentBuilder db = dbf.newDocumentBuilder();
                db.setEntityResolver(new _EntityResolver());
                db.setErrorHandler(new MyFacesErrorHandler(log));

                InputSource is = createContextInputSource(null, WEB_XML_PATH);

                if(is==null)
                {
                    URL url = _context.getResource(WEB_XML_PATH);
                    log.debug("No web-xml found at : "+(url==null?" null ":url.toString()));
                    return _extensionsFilterConfig;
                }

                Document document = db.parse(is);

                Element webAppElem = document.getDocumentElement();
                if (webAppElem == null ||
                    !webAppElem.getNodeName().equals("web-app"))
                {
                    throw new FacesException("No valid web-app root element found!");
                }

                readWebApp(webAppElem);

                return _extensionsFilterConfig;
            }
            catch (Exception e)
            {
                log.fatal("Unable to parse web.xml", e);
                throw new FacesException(e);
            }
        }
        
        private void readWebApp(Element webAppElem)
        {
            NodeList nodeList = webAppElem.getChildNodes();
            for (int i = 0, len = nodeList.getLength(); i < len; i++)
            {
                Node n = nodeList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    if (n.getNodeName().equals("filter"))
                    {
                        readFilter((Element)n);
                    }
                }
                else
                {
                    if (log.isDebugEnabled()) log.debug("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
                }
            }
        }
        
        private void readFilter(Element filterElem)
        {
            String filterName = null;
            String filterClass = null;
            Map initParams = new HashMap();
            NodeList nodeList = filterElem.getChildNodes();
            for (int i = 0, len = nodeList.getLength(); i < len; i++)
            {
                Node n = nodeList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    if (n.getNodeName().equals("filter-name"))
                    {
                        filterName = XmlUtils.getElementText((Element)n).trim();
                    }
                    else if (n.getNodeName().equals("filter-class"))
                    {
                        filterClass = org.apache.myfaces.shared_tomahawk.util.xml.XmlUtils.getElementText((Element)n).trim();
                    }
                    else if (n.getNodeName().equals("description") || n.getNodeName().equals("init-param"))
                    {
                        //ignore
                    }
                    else if (n.getNodeName().equals("init-param"))
                    {
                        //Read the extensions filter params!
                        NodeList nodeList2 = n.getChildNodes();
                        String paramName = null;
                        String paramValue =  null;
                        for (int j = 0, len2 = nodeList2.getLength(); j < len2; j++)
                        {
                            Node n2 = nodeList2.item(j);
                            if(n2.getNodeName().equals("param-name"))
                            {
                                paramName = XmlUtils.getElementText((Element)n2).trim();
                            }
                            else if (n2.getNodeName().equals("param-value"))
                            {
                                paramValue = XmlUtils.getElementText((Element)n2).trim();
                            }
                        }
                        
                        if (paramName != null)
                        {
                            initParams.put(paramName, paramValue);
                        }
                    }
                    else
                    {
                        if (log.isDebugEnabled()) log.debug("Ignored element '" + n.getNodeName() + "' as child of '" + filterElem.getNodeName() + "'.");
                    }
                }
                else
                {
                    if (log.isDebugEnabled()) log.debug("Ignored node '" + n.getNodeName() + "' of type " + n.getNodeType());
                }
            }
            
            if ("org.apache.myfaces.webapp.filter.ExtensionsFilter".equals(filterClass))
            {
                //Read extensions filter params
                String param = (String)initParams.get("uploadMaxFileSize");

                _extensionsFilterConfig._uploadMaxFileSize = resolveSize(param, _extensionsFilterConfig._uploadMaxFileSize);

                param = (String)initParams.get("uploadThresholdSize");

                _extensionsFilterConfig._uploadThresholdSize = resolveSize(param, _extensionsFilterConfig._uploadThresholdSize);

                _extensionsFilterConfig._uploadRepositoryPath = (String)initParams.get("uploadRepositoryPath");
            }            
            initParams.clear();
            //_webXml.addFilter(filterName, filterClass);
        }
        
        private int resolveSize(String param, int defaultValue) {
            int numberParam = defaultValue;

            if (param != null) {
                param = param.toLowerCase();
                int factor = 1;
                String number = param;

                if (param.endsWith("g")) {
                    factor = 1024 * 1024 * 1024;
                    number = param.substring(0, param.length() - 1);
                } else if (param.endsWith("m")) {
                    factor = 1024 * 1024;
                    number = param.substring(0, param.length() - 1);
                } else if (param.endsWith("k")) {
                    factor = 1024;
                    number = param.substring(0, param.length() - 1);
                }

                numberParam = Integer.parseInt(number) * factor;
            }
            return numberParam;
        }
        
                        
        private class _EntityResolver implements EntityResolver
        {
            public InputSource resolveEntity(String publicId, String systemId) throws IOException
            {
                if (systemId == null)
                {
                    throw new UnsupportedOperationException("systemId must not be null");
                }

                if (systemId.equals(WEB_APP_2_2_SYSTEM_ID) ||
                    systemId.equals(WEB_APP_2_2_J2EE_SYSTEM_ID))
                {
                    //Load DTD from servlet.jar
                    return createClassloaderInputSource(publicId,WEB_APP_2_2_RESOURCE);
                }
                else if (systemId.equals(WEB_APP_2_3_SYSTEM_ID))
                {
                    //Load DTD from servlet.jar
                    return createClassloaderInputSource(publicId,WEB_APP_2_3_RESOURCE);
                }
                else
                {
                    //Load additional entities from web context
                    return createContextInputSource(publicId, systemId);
                }
            }
        }
        
        private InputSource createContextInputSource(String publicId, String systemId)
        {
            InputStream inStream = _context.getResourceAsStream(systemId);
            if (inStream == null)
            {
                // there is no such entity
                return null;
            }
            InputSource is = new InputSource(inStream);
            is.setPublicId(publicId);
            is.setSystemId(systemId);
            //the next line was removed - encoding should be determined automatically out of the inputStream
            //DEFAULT_ENCODING was ISO-8859-1
            //is.setEncoding(DEFAULT_ENCODING);
            return is;
        }
        
        private InputSource createClassloaderInputSource(String publicId, String systemId)
        {
            InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(systemId);
            if (inStream == null)
            {
                // there is no such entity
                return null;
            }
            InputSource is = new InputSource(inStream);
            is.setPublicId(publicId);
            is.setSystemId(systemId);
            //the next line was removed - encoding should be determined automatically out of the inputStream
            //encoding should be determined automatically out of the inputStream
            //DEFAULT_ENCODING was ISO-8859-1
            //is.setEncoding(DEFAULT_ENCODING);
            return is;
        }
    }
    
    protected boolean isOld(ExternalContext context) {
        if (refreshPeriod > 0) {
            long ttl = this.parsingTime + refreshPeriod;
            if (System.currentTimeMillis() > ttl) {
                long lastModified = ExtensionsFilterConfig.getWebXmlLastModified(context);
                return lastModified == 0 || lastModified > ttl;
            }
        }
        return false;
    }
    
    public static long getWebXmlLastModified(ExternalContext context) {
        try {
            URL url = context.getResource(WEB_XML_PATH);
            if (url != null)
                return url.openConnection().getLastModified();
        } catch (IOException e) {
            log.error("Could not find web.xml in path " + WEB_XML_PATH);
        }
        return 0L;
    }
            
    private static final String EXTENSIONS_FILTER_CONFIG = ExtensionsFilterConfig.class.getName();
    
    public static ExtensionsFilterConfig getExtensionsFilterConfig(ExternalContext context)
    {
        ExtensionsFilterConfig configuration = (ExtensionsFilterConfig)context.getApplicationMap().get(EXTENSIONS_FILTER_CONFIG);
        if (configuration == null)
        {
            init(context);
            configuration = (ExtensionsFilterConfig)context.getApplicationMap().get(EXTENSIONS_FILTER_CONFIG);
        }
        return configuration;
    }

    /**
     * should be called when initialising Servlet
     * @param context
     */
    public static void init(ExternalContext context)
    {
        ExtensionsFilterConfig configuration = ExtensionsFilterConfig.parse(context);
        context.getApplicationMap().put(EXTENSIONS_FILTER_CONFIG, configuration);
        long configRefreshPeriod = MyfacesConfig.getCurrentInstance(context).getConfigRefreshPeriod();
        configuration.setParsingTime(System.currentTimeMillis());
        refreshPeriod = (configRefreshPeriod * 1000);
    }
    public static void update(ExternalContext context){
        if (getExtensionsFilterConfig(context).isOld(context)){
            ExtensionsFilterConfig.init(context);
        }
    }

}
