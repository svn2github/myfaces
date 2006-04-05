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

import java.io.Serializable;


/**
 * Dojo configuration holder helper
 * this is a helper class to generate
 * a dojo configuration
 * if a null value is set the toString
 * ignores the djconfig
 *
 * the toString method generates a full djconfig
 *
 * Warning if you adjust the dojo config params
 * do it before rendering
 * because this stuff is shared in the request
 * as a singleton value in normal circumstances!
 *
 * (unless you want to influence the dojo config
 * seriously afterwards please, follow this guideline
 * use the decode phase or something similar if not available)
 *
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoConfig implements Serializable {

    /**
     *
     */
    private static final long   serialVersionUID      = 1L;
    private static final String CONFIG_FOOTER         = "}; \n";
    private static final String CONFIG_HEADER         = "var djConfig = { \n";
    public static String        ATTR_DOJO_TYPE        = "dojoType";
    Boolean                     _allowQueryConfig     = null;
    String                      _baseScriptUri        = null;
    Boolean                     _bindEncoding         = null;
    Boolean                     _debug                = null;
    Boolean                     _debugAtAllCosts      = null;
    String                      _debugContainerId     = null;
    Boolean                     _ignoreClassNames     = null;
    Boolean                     _ioSendTransport      = null;
    Boolean                     _parseWidgets         = null;
    Boolean                     _preventBackButtonFix = null;
    String                      _searchIds            = null;

    //getters and setters for the djconfig
    public Boolean getAllowQueryConfig() {
        return _allowQueryConfig;
    }

    public String getBaseScriptUri() {
        return _baseScriptUri;
    }

    public Boolean getBindEncoding() {
        return _bindEncoding;
    }

    public Boolean getDebug() {
        return _debug;
    }

    public Boolean getDebugAtAllCosts() {
        return _debugAtAllCosts;
    }

    public String getDebugContainerId() {
        return _debugContainerId;
    }

    public Boolean getIgnoreClassNames() {
        return _ignoreClassNames;
    }

    public Boolean getIoSendTransport() {
        return _ioSendTransport;
    }

    public Boolean getParseWidgets() {
        return _parseWidgets;
    }

    public Boolean getPreventBackButtonFix() {
        return _preventBackButtonFix;
    }

    public String getSearchIds() {
        return _searchIds;
    }

    public void setAllowQueryConfig(Boolean allowQueryConfig) {
        this._allowQueryConfig = allowQueryConfig;
    }

    public void setBaseScriptUri(String baseScriptUri) {
        this._baseScriptUri = baseScriptUri;
    }

    public void setBindEncoding(Boolean bindEncoding) {
        this._bindEncoding = bindEncoding;
    }

    public void setDebug(Boolean debug) {
        this._debug = debug;
    }

    public void setDebugAtAllCosts(Boolean debugAtAllCosts) {
        this._debugAtAllCosts = debugAtAllCosts;
    }

    public void setDebugContainerId(String debugContainerId) {
        this._debugContainerId = debugContainerId;
    }

    public void setIgnoreClassNames(Boolean ignoreClassNames) {
        this._ignoreClassNames = ignoreClassNames;
    }

    public void setIoSendTransport(Boolean ioSendTransport) {
        this._ioSendTransport = ioSendTransport;
    }

    public void setParseWidgets(Boolean parseWidgets) {
        this._parseWidgets = parseWidgets;
    }

    public void setPreventBackButtonFix(Boolean preventBackButtonFix) {
        this._preventBackButtonFix = preventBackButtonFix;
    }

    public void setSearchIds(String searchIds) {
        this._searchIds = searchIds;
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

    private final void createConfigEntry(StringBuffer target, String name, Object toCheck) {

        if (toCheck == null)
            return;

        if (target.indexOf(":") != -1)
            target.append(",\n");

        target.append(name);
        target.append(":");
        target.append(toCheck);
    }

}
