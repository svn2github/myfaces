/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.mock.tomahawk;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;

/**
 * @author Mathias Brökelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MockExternalContext extends ExternalContext
{
    private Map _appMap = new HashMap();
    private String _authType;
    private Object _context;
    private Map _initParameterMap = new HashMap();
    private String _remoteUser;
    private Object _request;
    private String _requestContextPath;
    private Map _requestCookieMap = new HashMap();
    private Map _requestHeaderMap = new HashMap();
    private Map _requestHeaderValuesMap = new HashMap();
    private Locale _requestLocale;
    private List _requestLocales = new ArrayList();
    private Map _requestMap = new HashMap();
    private Map _requestParameterMap = new HashMap();
    private Map _requestParameterValuesMap = new HashMap();
    private String _requestPathInfo;
    private String _requestServletPath;
    private Object _response;
    private Object _session;
    private Map _sessionMap = new HashMap();
    private Principal _userPrincipal;
    private Collection _roles = new HashSet();

    public void dispatch(String path) throws IOException
    {
    }

    public String encodeActionURL(String url)
    {
        return url;
    }

    public String encodeNamespace(String name)
    {
        return name;
    }

    public String encodeResourceURL(String url)
    {
        return url;
    }

    public Map getApplicationMap()
    {
        return _appMap;
    }

    public void setAuthType(String authType)
    {
        _authType = authType;
    }

    public String getAuthType()
    {
        return _authType;
    }

    public void setContext(Object context)
    {
        _context = context;
    }

    public Object getContext()
    {
        return _context;
    }

    public String getInitParameter(String name)
    {
        return (String) _initParameterMap.get(name);
    }

    public void setInitParameter(String name, String value)
    {
        _initParameterMap.put(name, value);
    }

    public Map getInitParameterMap()
    {
        return _initParameterMap;
    }

    public void setRemoteUser(String remoteUser)
    {
        _remoteUser = remoteUser;
    }

    public String getRemoteUser()
    {
        return _remoteUser;
    }

    public Object getRequest()
    {
        return _request;
    }

    public void setRequest(Object request)
    {
        _request = request;
    }

    public String getRequestContextPath()
    {
        return _requestContextPath;
    }

    public void setRequestContextPath(String requestContextPath)
    {
        _requestContextPath = requestContextPath;
    }

    public void setRequestCookieMap(Map requestCookieMap)
    {
        _requestCookieMap = requestCookieMap;
    }

    public Map getRequestCookieMap()
    {
        return _requestCookieMap;
    }

    public void setRequestHeaderMap(Map requestHeaderMap)
    {
        _requestHeaderMap = requestHeaderMap;
    }

    public Map getRequestHeaderMap()
    {
        return _requestHeaderMap;
    }

    public void setRequestHeaderValuesMap(Map requestHeaderValuesMap)
    {
        _requestHeaderValuesMap = requestHeaderValuesMap;
    }

    public Map getRequestHeaderValuesMap()
    {
        return _requestHeaderValuesMap;
    }

    public Locale getRequestLocale()
    {
        return _requestLocale;
    }

    public void setRequestLocale(Locale requestLocale)
    {
        _requestLocale = requestLocale;
    }

    public Iterator getRequestLocales()
    {
        return _requestLocales.iterator();
    }

    public void setRequestLocales(List requestLocales)
    {
        _requestLocales = requestLocales;
    }

    public void setRequestMap(Map requestMap)
    {
        _requestMap = requestMap;
    }

    public Map getRequestMap()
    {
        return _requestMap;
    }

    public void setRequestParameterMap(Map requestParameterMap)
    {
        _requestParameterMap = requestParameterMap;
    }

    public Map getRequestParameterMap()
    {
        return _requestParameterMap;
    }

    public Iterator getRequestParameterNames()
    {
        return _requestParameterMap.keySet().iterator();
    }

    public void setRequestParameterValuesMap(Map requestParameterValuesMap)
    {
        _requestParameterValuesMap = requestParameterValuesMap;
    }

    public Map getRequestParameterValuesMap()
    {
        return _requestParameterValuesMap;
    }

    public void setRequestPathInfo(String requestPathInfo)
    {
        _requestPathInfo = requestPathInfo;
    }

    public String getRequestPathInfo()
    {
        return _requestPathInfo;
    }

    public void setRequestServletPath(String requestServletPath)
    {
        _requestServletPath = requestServletPath;
    }

    public String getRequestServletPath()
    {
        return _requestServletPath;
    }

    public URL getResource(String path) throws MalformedURLException
    {
        return null;
    }

    public InputStream getResourceAsStream(String path)
    {
        return null;
    }

    public Set getResourcePaths(String path)
    {
        return Collections.EMPTY_SET;
    }

    public void setResponse(Object response)
    {
        _response = response;
    }

    public Object getResponse()
    {
        return _response;
    }

    public void setSession(Object session)
    {
        _session = session;
    }

    public Object getSession(boolean create)
    {
        return _session;
    }

    public Map getSessionMap()
    {
        return _sessionMap;
    }
    
    public void setUserPrincipal(Principal userPrincipal)
    {
        _userPrincipal = userPrincipal;
    }

    public Principal getUserPrincipal()
    {
        return _userPrincipal;
    }
    
    public void setUserRoles(Collection roles)
    {
        _roles = roles;
    }
    
    public void addUserRole(String role)
    {
        _roles.add(role);
    }
    
    public boolean isUserInRole(String role)
    {
        return _roles.contains(role);
    }

    public void log(String message)
    {
    }

    public void log(String message, Throwable exception)
    {
    }

    public void redirect(String url) throws IOException
    {
    }

}
