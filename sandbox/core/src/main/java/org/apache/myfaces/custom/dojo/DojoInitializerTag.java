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

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.html.HtmlOutputTextTagBase;

/**
 * Tag for the dojo intializer code
 * 
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoInitializerTag extends HtmlOutputTextTagBase
{
    String _ioSendTransport      = null;
    String _debug                = null;
    String _baseScriptUri        = null;
    String _allowQueryConfig     = null;
    String _debugContainerId     = null;
    String _searchIds            = null;
    String _parseWidgets         = null;
    String _bindEncoding         = null;
    String _ignoreClassNames     = null;
    String _preventBackButtonFix = null;
    String _require              = null;
    String _provide              = null;
    String _debugConsole         = null;
    String _debugAtAllCosts      = null;
    
    public String getComponentType()
    {
        return DojoInitializer.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return DojoInitializerRenderer.RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
        _ioSendTransport = null;
        _debug = null;
        _baseScriptUri = null;
        _allowQueryConfig = null;
        _debugContainerId = null;
        _searchIds = null;
        _parseWidgets = null;
        _bindEncoding = null;
        _ignoreClassNames = null;
        _preventBackButtonFix = null;
        _require = null;
        _provide = null;
        _debugConsole = null;
        _debugAtAllCosts = null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        super.setBooleanProperty(component, "ioSendTransport", _ioSendTransport);
        super.setBooleanProperty(component, "debug", _debug);
        super.setStringProperty(component, "baseScriptUri", _baseScriptUri);
        super.setBooleanProperty(component, "allowQueryConfig", _allowQueryConfig);
        super.setStringProperty(component, "debugContainerId", _debugContainerId);
        super.setStringProperty(component, "searchIds", _searchIds);
        super.setBooleanProperty(component, "parseWidgets", _parseWidgets);
        super.setBooleanProperty(component, "bindEncoding", _bindEncoding);

        super.setBooleanProperty(component, "ignoreClassNames", _ignoreClassNames);
        super.setBooleanProperty(component, "preventBackButtonFix", _preventBackButtonFix);

        super.setStringProperty(component, "require", _require);
        super.setStringProperty(component, "provide", _provide);
        super.setBooleanProperty(component, "debugConsole", _debugConsole);
        super.setBooleanProperty(component, "debugAtAllCosts", _debugAtAllCosts);
            
    }

    public void setAllowQueryConfig(String allowQueryConfig)
    {
        this._allowQueryConfig = allowQueryConfig;
    }

    public void setBaseScriptUri(String baseScriptUri)
    {
        this._baseScriptUri = baseScriptUri;
    }

    public void setBindEncoding(String bindEncoding)
    {
        this._bindEncoding = bindEncoding;
    }

    public void setDebug(String debug)
    {
        this._debug = debug;
    }

    public void setDebugContainerId(String debugContainerId)
    {
        this._debugContainerId = debugContainerId;
    }

    public void setIgnoreClassNames(String ignoreClassNames)
    {
        this._ignoreClassNames = ignoreClassNames;
    }

    public void setIoSendTransport(String ioSendTransport)
    {
        this._ioSendTransport = ioSendTransport;
    }

    public void setParseWidgets(String parseWidgets)
    {
        this._parseWidgets = parseWidgets;
    }

    public void setPreventBackButtonFix(String preventBackButtonFix)
    {
        this._preventBackButtonFix = preventBackButtonFix;
    }

    public void setSearchIds(String searchIds)
    {
        this._searchIds = searchIds;
    }

    public void setRequire(String require)
    {
        this._require = require;
    }

    public void setProvide(String provide) {
        this._provide = provide;
    }

    public void setDebugConsole(String debugConsole)
    {
        this._debugConsole = debugConsole;
    }

    public void setDebugAtAllCosts(String debugAtAllCosts)
    {
        this._debugAtAllCosts = debugAtAllCosts;
    }
}
