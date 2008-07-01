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

package org.apache.myfaces.custom.dojo;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;


/**
 * Default component for the dojo intializer
 * <br/>
 * This component is purely non visual, and serves for
 * quick hacking, and prototyping the dojo toolkit within
 * verbatimed html.
 * <br/>
 * Dojo initializer helper tag for debugging and communication flags. 
 * This tag encapsules the Dojo DjConfig meta configuration and also 
 * adds the needed dojo required functionality.
 *
 * @JSFComponent
 *   name = "t:dojoInitializer"
 *   tagClass = "org.apache.myfaces.custom.dojo.DojoInitializerTag"
 *
 * @JSFJspProperty name = "converter" tagExcluded = "true"
 * @JSFJspProperty name = "value" tagExcluded = "true"
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DojoInitializer extends UIOutput {

    public static final String COMPONENT_TYPE        = "org.apache.myfaces.DojoInitializer";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.DojoInitializerRenderer";
    public static final String COMPONENT_FAMILY      = "javax.faces.Output";
    Boolean                    _debugConsole         = null;
    DojoConfig                 _dojoConfig           = new DojoConfig();
    Boolean                    _expanded             = null;
    String                     _provide              = null;
    String                     _require              = null;
    Boolean                    _development          = null;
    
    //we handle that specifically to speed things up (we do not want an NxN runtime complexity via enforced
    //reflection in the utils
    boolean dojoConfigParamSet = false;

    public DojoInitializer() {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * Triggers a standard dojo allowQueryConfig as defined
     * by the <a href="http://dojotoolkit.org/">Dojo Toolkit</a>.
     * If someone has additional infos about this tag,
     * please fill in here.
     * 
     * @JSFProperty
     */
    public Boolean getAllowQueryConfig() {
        return _dojoConfig.getAllowQueryConfig();
    }

    /**
     * Triggers a standard dojo baseScriptUri as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * <br />
     * Allows the alteration of the dojo loading root path
     * used by require.
     * 
     * @JSFProperty
     */
    public String getBaseScriptUri() {
        return _dojoConfig.getBaseScriptUri();
    }

    /**
     * Triggers a standard dojo bindEncoding as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * 
     * Although this control most of the times is not
     * needed one usage in plain jsf applications can be
     * thought of. The changing of the bind encodings, if
     * you check the parameter list of the control
     * <b>ioSendTransport</b>
     * can be used to change this encoding to any encoding
     * parameter needed. This can be used to change the
     * internal ajax request encodings of various controls
     * in a global manner.
     * 
     * @JSFProperty
     */
    public String getBindEncoding() {
        return _dojoConfig.getBindEncoding();
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    /**
     * Triggers a standard dojo debug as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * <br />
     * Enables the dojo debugging framework. The usage of
     * dojo.debug causes output to be displayed in either
     * the current container or a target container, if one
     * is set.
     * 
     * @JSFProperty
     */
    public Boolean getDebug() {
        return _dojoConfig.getDebug();
    }

    /**
     * Triggers a standard dojo debugAtAllCosts as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * Tries to enforce debugging even if the browser
     * version does not allow it to work properlys
     * 
     * @JSFProperty
     */
    public Boolean getDebugAtAllCosts() {
        return _dojoConfig.getDebugAtAllCosts();
    }

    /**
     * Triggers a standard dojo debugConsole as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * <br />
     * This flag pops up a separate in frame floating
     * window which receives the debugging output.
     * <br />
     * <br />
     * Note: This flag only works if debugging is enabled
     * generally via the debug="true" flag set
     * 
     * @JSFProperty
     */
    public Boolean getDebugConsole() {
        return _debugConsole;
    }

    /**
     * Triggers a standard dojo debugContainerId as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * <br />
     * This flag notifies the Dojo toolkit about the id
     * which should receive the debug output. Dojo has a
     * full debugging framework in it which allows to turn
     * on debug output via a logging mechanism @see
     * dojo.debug() for further references.
     * <br />
     * <br />
     * The debugging output is not limited to the current
     * container triggering the debugging statement any
     * container can receive the debugging output this flag
     * is the flag which has to be set for setting a
     * different target id for the debugging output.
     * 
     * @JSFProperty
     */
    public String getDebugContainerId() {
        return _dojoConfig.getDebugContainerId();
    }

    public DojoConfig getDojoConfig() {
        return _dojoConfig;
    }

    /**
     * Triggers a standard dojo development as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * If you can find additional info about this attribute
     * please fill in here
     * 
     * @JSFProperty
     */
    public Boolean getExpanded() {
        return _expanded;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Triggers a standard dojo ignoreClassNames as defined
     * by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * <br />
     * <br />
     * This attribute turns on the case insensitive
     * handling of classnames by require and other Dojo
     * class loader mechanisms.
     * <br />
     * <br />
     * Case sensitivity is by default turned
     * <b>on</b>.
     * 
     * @JSFProperty
     */
    public Boolean getIgnoreClassNames() {
        return _dojoConfig.getIgnoreClassNames();
    }

    /**
     * Dojo in its underlying architecture allows various
     * options of server-client side communication. This
     * setting allows the alteration of the communication
     * layer to a different protocol. Currently only in
     * usage is xmlhttprequest, hence setting this encoding
     * will not change too much. But different encodings
     * like iframe are planned for the future.
     * 
     * @JSFProperty
     */
    public String getIoSendTransport() {
        return _dojoConfig.getIoSendTransport();
    }

    /**
     * Triggers a standard dojo parseWidgets as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * 
     * This parameter is used to turn on or off the dojo
     * widgeting parsing. The parsing is not used by
     * MyFaces for various reasons, but can be used by your
     * own html code.
     * <br />
     * <br />
     * For addititional information regarding the Dojo
     * Toolkit widget parsing, visit the
     * <br />
     * <a href="http://dojotoolkit.org/">Dojo Toolkit Homepage</a>
     * 
     * @JSFProperty
     */
    public Boolean getParseWidgets() {
        return _dojoConfig.getParseWidgets();
    }

    /**
     * Dojo uses internally back button blockers to avoid
     * problems with ajax requests causing the back button,
     * to move out of the back page navigation order.
     * Sometimes this behavior is not wanted and can be
     * turned off with this on a global scale;
     * 
     * @JSFProperty
     */
    public Boolean getPreventBackButtonFix() {
        return _dojoConfig.getPreventBackButtonFix();
    }

    /**
     * Triggers a standard dojo provide as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * 
     * @JSFProperty
     */
    public String getProvide() {
        return _provide;
    }

    public String getRendererType() {
        return DojoInitializerRenderer.RENDERER_TYPE;
    }

    /**
     * Triggers a standard dojo provide as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * 
     * @JSFProperty
     */
    public String getRequire() {
        return _require;
    }

    /**
     * Triggers a standard dojo development as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * If you can find additional info about this attribute
     * please fill in here
     * 
     * @JSFProperty
     */
    public String getSearchIds() {
        return _dojoConfig.getSearchIds();
    }
    
    /**
     * Triggers a standard dojo development as defined by the
     * <a href="http://dojotoolkit.org/">Dojo Toolkit</a>
     * If this parameter is set to true. The expanded
     * debuggable version of dojo is included
     * if set to false the compressed one is included.
     * 
     * @JSFProperty
     */
    public Boolean getDevelopment() {
        return _dojoConfig.getDevelopment();
    }

    public Object getValue() {
        return "DojoInitializers";
    }

    public boolean isDojoConfigParamSet() {
        return dojoConfigParamSet;
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        setAllowQueryConfig((Boolean) values[1]);
        setBaseScriptUri((String) values[2]);
        setBindEncoding((String) values[3]);
        setDebug((Boolean) values[4]);
        setDebugContainerId((String) values[5]);
        setIgnoreClassNames((Boolean) values[6]);
        setIoSendTransport((String) values[7]);
        setParseWidgets((Boolean) values[8]);
        setPreventBackButtonFix((Boolean) values[9]);
        setSearchIds((String) values[10]);
        _require      = (String) values[11];
        _provide      = (String) values[12];
        _debugConsole = (Boolean) values[13];
        setDebugAtAllCosts((Boolean) values[14]);
        _expanded = (Boolean) values[15];
        _development = (Boolean) values[16];
    }

    public Object saveState(FacesContext context) {
        Object[] values = new Object[17];
        values[0]  = super.saveState(context);
        values[1]  = _dojoConfig.getAllowQueryConfig();
        values[2]  = _dojoConfig.getBaseScriptUri();
        values[3]  = _dojoConfig.getBindEncoding();
        values[4]  = _dojoConfig.getDebug();
        values[5]  = _dojoConfig.getDebugContainerId();
        values[6]  = _dojoConfig.getIgnoreClassNames();
        values[7]  = _dojoConfig.getIoSendTransport();
        values[8]  = _dojoConfig.getParseWidgets();
        values[9]  = _dojoConfig.getPreventBackButtonFix();
        values[10] = _dojoConfig.getSearchIds();
        values[11] = _require;
        values[12] = _provide;
        values[13] = _debugConsole;
        values[14] = _dojoConfig.getDebugAtAllCosts();
        values[15] = _expanded;
        values[16] = _development;
        
        return values;
    }

    public void setAllowQueryConfig(Boolean allowQueryConfig) {

        if (allowQueryConfig != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setAllowQueryConfig(allowQueryConfig);
        }

        _dojoConfig.setAllowQueryConfig(allowQueryConfig);

    }

    public void setBaseScriptUri(String baseScriptUri) {

        if (baseScriptUri != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setBaseScriptUri(baseScriptUri);
        }

        _dojoConfig.setBaseScriptUri(baseScriptUri);
    }

    public void setBindEncoding(String bindEncoding) {

        if (bindEncoding != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setBindEncoding(bindEncoding);
        }

        _dojoConfig.setBindEncoding(bindEncoding);
    }

    public void setDebug(Boolean debug) {

        if (debug != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebug(debug);
        }

        _dojoConfig.setDebug(debug);

    }

    public void setDebugAtAllCosts(Boolean debugAtAllCosts) {

        if (debugAtAllCosts != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebugAtAllCosts(debugAtAllCosts);
        }

        _dojoConfig.setDebugAtAllCosts(debugAtAllCosts);
    }

    public void setDebugConsole(Boolean debugConsole) {
        this._debugConsole = debugConsole;
    }

    public void setDebugContainerId(String debugContainerId) {

        if (debugContainerId != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDebugContainerId(debugContainerId);
        }

        _dojoConfig.setDebugContainerId(debugContainerId);
    }

    public void setDojoConfigParamSet(boolean dojoConfigParamSet) {
        this.dojoConfigParamSet = dojoConfigParamSet;
    }

    public void setExpanded(Boolean expanded) {

        //we have a logical or over all expanded tags
        if (expanded != null) {
            dojoConfigParamSet = true;
            DojoUtils.setExpanded(FacesContext.getCurrentInstance(), expanded);
        }

        _expanded = expanded;
    }

    public void setIgnoreClassNames(Boolean ignoreClassNames) {

        if (ignoreClassNames != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setIgnoreClassNames(ignoreClassNames);
        }

        _dojoConfig.setIgnoreClassNames(ignoreClassNames);
    }

    public void setIoSendTransport(String ioSendTransport) {

        if (ioSendTransport != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setIoSendTransport(ioSendTransport);
        }

        _dojoConfig.setIoSendTransport(ioSendTransport);

    }

    public void setParseWidgets(Boolean parseWidgets) {

        if (parseWidgets != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setParseWidgets(parseWidgets);
        }

        _dojoConfig.setParseWidgets(parseWidgets);
    }

    public void setPreventBackButtonFix(Boolean preventBackButtonFix) {

        if (preventBackButtonFix != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setPreventBackButtonFix(preventBackButtonFix);
        }

        _dojoConfig.setPreventBackButtonFix(preventBackButtonFix);
    }

    public void setProvide(String provide) {
        this._provide = provide;
    }

    public void setRequire(String required) {
        this._require = required;
    }

    public void setSearchIds(String searchIds) {

        if (searchIds != null) {
            dojoConfigParamSet = true;
            DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setSearchIds(searchIds);
        }

        _dojoConfig.setSearchIds(searchIds);
    }

 
     public void setDevelopment(Boolean development)
    {
         if (development != null) {
             dojoConfigParamSet = true;
             DojoUtils.getDjConfigInstance(FacesContext.getCurrentInstance()).setDevelopment(development);
         }
         _development = development;
    }

 
}
