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
package org.apache.myfaces.tomahawk.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * The ExceptionContext class holds all information related to an error
 * ocurred in a jsf environment, handled by a redirection using
 * ErrorRedirectJSFPageHandler.
 * 
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExceptionContext
{

    private Throwable _exception;

    private UIComponent _viewRoot;

    private String _cause;

    private String _tree;
    
    private String _vars;

    public ExceptionContext(Throwable _exception, UIComponent viewRoot)
    {
        super();
        this._exception = _exception;
        this._viewRoot = viewRoot;
    }

    public void setException(Throwable exception)
    {
        this._exception = exception;
    }

    public Throwable getException()
    {
        return _exception;
    }

    public String getCause()
    {
        if (_cause == null)
        {
            StringWriter writer = null;
            try
            {
                writer = new StringWriter(256);
                ErrorPageWriter.writeCause(writer, _exception);
            }
            catch (IOException e)
            {
                //do nothing
            }
            finally
            {
                try
                {
                    if (writer != null)
                        writer.close();
                }
                catch (IOException e)
                {
                    //do nothing
                }
            }
            _cause = writer.toString();
        }
        return _cause;
    }

    public String getStackTrace()
    {
        StringWriter str = new StringWriter(256);
        PrintWriter pstr = new PrintWriter(str);
        _exception.printStackTrace(pstr);
        pstr.close();
        return str.toString();
    }

    public String getNow()
    {
        Date now = new Date();
        return DateFormat.getDateTimeInstance().format(now);
    }

    public UIComponent getViewRoot()
    {
        return _viewRoot;
    }

    public void setViewRoot(UIComponent viewRoot)
    {
        this._viewRoot = viewRoot;
    }

    public String getVars()
    {
        if (_vars == null)
        {
            StringWriter writer = null;
            try
            {
                writer = new StringWriter(256);
                ErrorPageWriter.writeVariables(writer, FacesContext.getCurrentInstance());
                _vars = writer.toString();
            }
            catch (IOException e)
            {
                //do nothing
            }
            finally
            {
                try
                {
                    if (writer != null)
                        writer.close();
                }
                catch (IOException e)
                {
                    //do nothing
                }
            }
        }
        return _vars;
    }
    
    public String getTree()
    {
        if (_tree == null)
        {
            if (_viewRoot != null)
            {
                StringWriter writer = null;
                try
                {
                    writer = new StringWriter(256);
                    ErrorPageWriter.writeComponent(writer, _viewRoot,
                            ErrorPageWriter.getErrorId(_exception));
                    _tree = writer.toString();
                }
                catch (IOException e)
                {
                    //do nothing
                }
                finally
                {
                    try
                    {
                        if (writer != null)
                            writer.close();
                    }
                    catch (IOException e)
                    {
                        //do nothing
                    }
                }
            }
        }
        return _tree;
    }
    
}
