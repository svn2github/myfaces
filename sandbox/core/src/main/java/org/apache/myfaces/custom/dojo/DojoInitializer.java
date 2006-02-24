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

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * Default component for the dojo intializer
 * 
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoInitializer extends UIOutput
{
    DojoConfig                 _dojoConfig           = new DojoConfig();
    String                     _require              = null;
    String                     _provide              = null;
    Boolean                    _debugConsole         = null;
    Boolean                    _expanded             = null;
    
    //we handle that specifically to speed things up (we do not want an NxN runtime complexity via enforced
    //reflection in the utils
    boolean                    dojoConfigParamSet    = false;

    public static final String COMPONENT_TYPE        = "org.apache.myfaces.DojoInitializer";
    public static final String DEFAULT_RENDERER_TYPE = DojoInitializerRenderer.RENDERER_TYPE;
    public static final String COMPONENT_FAMILY      = "javax.faces.Output";

    public DojoInitializer()
    {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getRendererType()
    {
        return DojoInitializerRenderer.RENDERER_TYPE;
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public String getComponentType()
    {
        return COMPONENT_TYPE;
    }

    public Object getValue()
    {
        return "DojoInitializers";
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _dojoConfig.setAllowQueryConfig((Boolean) values[1]);
        _dojoConfig.setBaseScriptUri((String) values[2]);
        _dojoConfig.setBindEncoding((Boolean) values[3]);
        _dojoConfig.setDebug((Boolean) values[4]);
        _dojoConfig.setDebugContainerId((String) values[5]);
        _dojoConfig.setIgnoreClassNames((Boolean) values[6]);
        _dojoConfig.setIoSendTransport((Boolean) values[7]);
        _dojoConfig.setParseWidgets((Boolean) values[8]);
        _dojoConfig.setPreventBackButtonFix((Boolean) values[9]);
        _dojoConfig.setSearchIds((String) values[10]);
        _require = (String) values[11];
        _provide = (String) values[12];
        _debugConsole = (Boolean) values[13];
        _dojoConfig.setDebugAtAllCosts((Boolean) values[14]);
        _expanded = (Boolean)values[15];
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[16];
        values[0] = super.saveState(context);
        values[1] = _dojoConfig.getAllowQueryConfig();
        values[2] = _dojoConfig.getBaseScriptUri();
        values[3] = _dojoConfig.getBindEncoding();
        values[4] = _dojoConfig.getDebug();
        values[5] = _dojoConfig.getDebugContainerId();
        values[6] = _dojoConfig.getIgnoreClassNames();
        values[7] = _dojoConfig.getIoSendTransport();
        values[8] = _dojoConfig.getParseWidgets();
        values[9] = _dojoConfig.getPreventBackButtonFix();
        values[10] = _dojoConfig.getSearchIds();
        values[11] = _require;
        values[12] = _provide;
        values[13] = _debugConsole;
        values[14] = _dojoConfig.getDebugAtAllCosts();
        values[15] = _expanded;
        return values;
    }

    public Boolean getAllowQueryConfig()
    {
        return _dojoConfig.getAllowQueryConfig();
    }

    public String getBaseScriptUri()
    {
        return _dojoConfig.getBaseScriptUri();
    }

    public Boolean getBindEncoding()
    {
        return _dojoConfig.getBindEncoding();
    }

    public Boolean getDebug()
    {
        return _dojoConfig.getDebug();
    }

    public String getDebugContainerId()
    {
        return _dojoConfig.getDebugContainerId();
    }

    public Boolean getIgnoreClassNames()
    {
        return _dojoConfig.getIgnoreClassNames();
    }

    public Boolean getIoSendTransport()
    {
        return _dojoConfig.getIoSendTransport();
    }

    public Boolean getParseWidgets()
    {
        return _dojoConfig.getParseWidgets();
    }

    public Boolean getPreventBackButtonFix()
    {
        return _dojoConfig.getPreventBackButtonFix();
    }

    public String getSearchIds()
    {
        return _dojoConfig.getSearchIds();
    }

    public void setAllowQueryConfig(Boolean allowQueryConfig)
    {
        if (allowQueryConfig != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setAllowQueryConfig(allowQueryConfig);
        }
        _dojoConfig.setAllowQueryConfig(allowQueryConfig);
       
    }

    public void setBaseScriptUri(String baseScriptUri)
    {
        if (baseScriptUri != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setBaseScriptUri(baseScriptUri);
        }
        _dojoConfig.setBaseScriptUri(baseScriptUri);
    }

    public void setBindEncoding(Boolean bindEncoding)
    {
        if (bindEncoding != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setBindEncoding(bindEncoding);
        }

        _dojoConfig.setBindEncoding(bindEncoding);
    }

    public void setDebug(Boolean debug)
    {
        if (debug != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebug(debug);
        }
        _dojoConfig.setDebug(debug);
    
    }

    public void setDebugContainerId(String debugContainerId)
    {
        if (debugContainerId != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebugContainerId(debugContainerId);
        }
        _dojoConfig.setDebugContainerId(debugContainerId);
    }

    public void setIgnoreClassNames(Boolean ignoreClassNames)
    {
        if (ignoreClassNames != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setIgnoreClassNames(ignoreClassNames);
        }
        _dojoConfig.setIgnoreClassNames(ignoreClassNames);
    }

    public void setIoSendTransport(Boolean ioSendTransport)
    {
        if (ioSendTransport != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setIoSendTransport(ioSendTransport);
        }
        _dojoConfig.setIoSendTransport(ioSendTransport);
      
    }

    public void setParseWidgets(Boolean parseWidgets)
    {
        if (parseWidgets != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setParseWidgets(parseWidgets);
        }
        _dojoConfig.setParseWidgets(parseWidgets);
    }

    public void setPreventBackButtonFix(Boolean preventBackButtonFix)
    {
        if (preventBackButtonFix != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setPreventBackButtonFix(
                    preventBackButtonFix);
        }
        _dojoConfig.setPreventBackButtonFix(preventBackButtonFix);
    }

    public void setSearchIds(String searchIds)
    {
        if (searchIds != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setSearchIds(searchIds);
        }
        _dojoConfig.setSearchIds(searchIds);
    }

    public DojoConfig getDojoConfig()
    {
        return _dojoConfig;
    }

    public String getRequire()
    {
        return _require;
    }

    public void setRequire(String required)
    {
        this._require = required;
    }

    public String getProvide()
    {
        return _provide;
    }

    public void setProvide(String provide)
    {
        this._provide = provide;
    }

    public Boolean getDebugConsole()
    {
        return _debugConsole;
    }

    public void setDebugConsole(Boolean debugConsole)
    {
        this._debugConsole = debugConsole;
    }

    public boolean isDojoConfigParamSet()
    {
        return dojoConfigParamSet;
    }

    public void setDojoConfigParamSet(boolean dojoConfigParamSet)
    {
        this.dojoConfigParamSet = dojoConfigParamSet;
    }

    public Boolean getDebugAtAllCosts() {
        return _dojoConfig.getDebugAtAllCosts();
    }
    
    public void setDebugAtAllCosts(Boolean debugAtAllCosts)
    {
        if (debugAtAllCosts != null)
        {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebugAtAllCosts(debugAtAllCosts);
        }
        _dojoConfig.setDebugAtAllCosts(debugAtAllCosts);
    }

    public Boolean getExpanded() {
        return _expanded;
    }
    
    public void setExpanded(Boolean expanded) {
        
        //we have a logical or over all expanded tags
        if(expanded != null) {
            dojoConfigParamSet = true;
            DojoUtils.setExpanded(FacesContext.getCurrentInstance(), expanded);
        }
        _expanded = expanded;
    }
    
}
