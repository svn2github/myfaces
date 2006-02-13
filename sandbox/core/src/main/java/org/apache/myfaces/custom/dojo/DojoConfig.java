/**
 * Copyright 2004 The Apache Software Foundation.
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

package org.apache.myfaces.custom.dojo;

/**
 * Dojo configuration holder helper
 * this is a helper class to generate
 * a dojo configuration
 * if a null value is set the toString 
 * ignores the djconfig
 * 
 * the toString method generates a full djconfig
 * 
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoConfig
{
    private static final String CONFIG_FOOTER = "}; \n";


    private static final String CONFIG_HEADER = "var djConfig = { \n";


    public static String ATTR_DOJO_TYPE = "dojoType";
    
    
    Boolean _ioSendTransport      = null;
    Boolean _debug                = null;
    String  _baseScriptUri        = null;
    Boolean _allowQueryConfig     = null;
    String  _debugContainerId     = null;
    String  _searchIds            = null;
    Boolean _parseWidgets         = null;
    Boolean _bindEncoding         = null;
    Boolean _ignoreClassNames     = null;
    Boolean _preventBackButtonFix = null;
    
    //TODO add this to the initializer tag
    Boolean _debugAtAllCosts      = null;
    
    private final void createConfigEntry(StringBuffer target, String name, Object toCheck) {
            if(toCheck == null) return;
            if(target.indexOf(":") != -1)
                target.append(",\n");
            target.append(name);
            target.append(":");
            target.append(toCheck);
    }    
    
    /**
     * returns a valid djconfig string 
     */
    public String toString() {
        
        StringBuffer configBuilder = new StringBuffer(128);
        configBuilder.append(CONFIG_HEADER);

        createConfigEntry(configBuilder, "ioSendTransport", _ioSendTransport);
        createConfigEntry(configBuilder, "isDebug", _debug);
        createConfigEntry(configBuilder, "baseScriptUri", _baseScriptUri);
        createConfigEntry(configBuilder, "allowQueryConfig", _allowQueryConfig);
        createConfigEntry(configBuilder, "debugContainerId", _debugContainerId);
        createConfigEntry(configBuilder, "searchIds", _searchIds);
        createConfigEntry(configBuilder, "parseWidgets", _parseWidgets);
        createConfigEntry(configBuilder, "bindEncoding", _bindEncoding);
        createConfigEntry(configBuilder, "ignoreClassNames", _ignoreClassNames);
        createConfigEntry(configBuilder, "preventBackButtonFix", _preventBackButtonFix);
        createConfigEntry(configBuilder, "debugAtAllCosts", _debugAtAllCosts);
        
        configBuilder.append("\n");
        configBuilder.append(CONFIG_FOOTER);

        return configBuilder.toString();
    }
    
    //getters and setters for the djconfig
    public Boolean getAllowQueryConfig()
    {
        return _allowQueryConfig;
    }
    public void setAllowQueryConfig(Boolean allowQueryConfig)
    {
        this._allowQueryConfig = allowQueryConfig;
    }
    public String getBaseScriptUri()
    {
        return _baseScriptUri;
    }
    public void setBaseScriptUri(String baseScriptUri)
    {
        this._baseScriptUri = baseScriptUri;
    }
    public Boolean getBindEncoding()
    {
        return _bindEncoding;
    }
    public void setBindEncoding(Boolean bindEncoding)
    {
        this._bindEncoding = bindEncoding;
    }
    public Boolean getDebug()
    {
        return _debug;
    }
    public void setDebug(Boolean debug)
    {
        this._debug = debug;
    }
    public String getDebugContainerId()
    {
        return _debugContainerId;
    }
    public void setDebugContainerId(String debugContainerId)
    {
        this._debugContainerId = debugContainerId;
    }
    public Boolean getIgnoreClassNames()
    {
        return _ignoreClassNames;
    }
    public void setIgnoreClassNames(Boolean ignoreClassNames)
    {
        this._ignoreClassNames = ignoreClassNames;
    }
    public Boolean getIoSendTransport()
    {
        return _ioSendTransport;
    }
    public void setIoSendTransport(Boolean ioSendTransport)
    {
        this._ioSendTransport = ioSendTransport;
    }
    public Boolean getParseWidgets()
    {
        return _parseWidgets;
    }
    public void setParseWidgets(Boolean parseWidgets)
    {
        this._parseWidgets = parseWidgets;
    }
    public Boolean getPreventBackButtonFix()
    {
        return _preventBackButtonFix;
    }
    public void setPreventBackButtonFix(Boolean preventBackButtonFix)
    {
        this._preventBackButtonFix = preventBackButtonFix;
    }
    public String getSearchIds()
    {
        return _searchIds;
    }
    public void setSearchIds(String searchIds)
    {
        this._searchIds = searchIds;
    }

    public Boolean getDebugAtAllCosts()
    {
        return _debugAtAllCosts;
    }

    public void setDebugAtAllCosts(Boolean debugAtAllCosts)
    {
        this._debugAtAllCosts = debugAtAllCosts;
    }

 }
