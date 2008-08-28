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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler redirect to a jsf page when an error occurs.
 * <p>
 * This class is set as a config-parameter org.apache.myfaces.ERROR_HANDLER
 * available on myfaces core jsf. (This does not work with RI)
 * </p>
 * <p>
 * The idea is extends myfaces error handling feature, making possible to redirect
 * to a jsf page when an error occur, using navigation rules.
 * </p>
 * <p>
 * If this handler is not able to handle the error, and alternate error handler
 * could be set in the config-parameter org.apache.myfaces.ERROR_REDIRECT_ALTERNATE_HANDLER
 * </p>
 * <p>
 * The info of the error in the jsf page can be found using:
 * </p>
 * <ul>
 * <li>#{exceptionContext.cause} : Cause retrieved from the exception</li>
 * <li>#{exceptionContext.stackTrace} : Stack trace of the exception</li>
 * <li>#{exceptionContext.exception} : Exception handled by this page </li>
 * <li>#{exceptionContext.tree} : Print the component tree of the page that cause the error</li>
 * <li>#{exceptionContext.vars} : Enviroment variables from the request</li>
 * </ul>
 * 
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ErrorRedirectJSFPageHandler
{
    private static final Log log = LogFactory
            .getLog(ErrorRedirectJSFPageHandler.class);

    /**
     * This param tells if ignore java.lang.* superclass names errors
     * are found. In other words, all java.lang.* exceptions are simply
     * skipped by this handler (so it could be thrown to an alternate handler
     * or the jsp handler).
     */
    private static final String ERROR_HANDLING_IGNORE_BASE_ERROR_REDIRECT = "org.apache.myfaces.ERROR_HANDLING_IGNORE_BASE_ERROR_REDIRECT";
       
    private static final String ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER = "org.apache.myfaces.ERROR_REDIRECT_ALTERNATE_HANDLER";
    
    private static String getErrorRedirectAlternateHandler(FacesContext context)
    {
        String errorHandlerClass = context.getExternalContext().getInitParameter(ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER);
        return errorHandlerClass;
    }
    
    private static boolean isIgnoreBaseErrorRedirect(FacesContext context)
    {
        return getBooleanValue(context.getExternalContext().getInitParameter(
                ERROR_HANDLING_IGNORE_BASE_ERROR_REDIRECT), false);
    }

    private static boolean getBooleanValue(String initParameter,
            boolean defaultVal)
    {
        if (initParameter == null || initParameter.trim().length() == 0)
            return defaultVal;

        return (initParameter.equalsIgnoreCase("on")
                || initParameter.equals("1") || initParameter
                .equalsIgnoreCase("true"));
    }
    
    public static void handleException(FacesContext facesContext, Exception e)
            throws ServletException, IOException
    {
        boolean oldRenderResponse = facesContext.getRenderResponse();
        ExceptionContext exceptionContext = new ExceptionContext(e,
                facesContext.getViewRoot());

        if (!handleCauseOrThrowable(facesContext, exceptionContext, e,
                oldRenderResponse, isIgnoreBaseErrorRedirect(facesContext)))
        {
            //ErrorPageWriter.handleException(facesContext, ex);            
            String errorHandlerClass = getErrorRedirectAlternateHandler(facesContext);
            if (errorHandlerClass == null)
            {
                //Redirect to another handler if it is available
                try
                {
                    Class clazz = Class.forName(errorHandlerClass);

                    Object errorHandler = clazz.newInstance();

                    Method m = clazz.getMethod("handleException", new Class[]{FacesContext.class,Exception.class});
                    m.invoke(errorHandler, new Object[]{facesContext, e});
                }
                catch(ClassNotFoundException ex)
                {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " was not found. Fix your web.xml-parameter : "+ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }
                catch (IllegalAccessException ex)
                {
                    throw new ServletException("Constructor of error-Handler : " 
                            +errorHandlerClass+ " is not accessible. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }
                catch (InstantiationException ex)
                {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " could not be instantiated. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }
                catch (NoSuchMethodException ex)
                {
                    log.error("Error-Handler : " 
                            +errorHandlerClass
                            + " did not have a method with name : handleException and parameters : javax.faces.context.FacesContext, java.lang.Exception. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                    //Try to look if it is implemented more general method handleThrowable
                    handleThrowable(facesContext, ex);
                }
                catch (InvocationTargetException ex)
                {
                    throw new ServletException("Excecution of method handleException in Error-Handler : " 
                            +errorHandlerClass
                            + " caused an exception. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }                
            }
            else
            {
                //throw exception because this redirect handler
                //does not have any rule defined.
                ErrorPageWriter.throwException(e);
            }
        }
    }

    public static void handleThrowable(FacesContext facesContext, Throwable e)
            throws ServletException, IOException
    {
        boolean oldRenderResponse = facesContext.getRenderResponse();
        ExceptionContext exceptionContext = new ExceptionContext(e,
                facesContext.getViewRoot());

        if (!handleCauseOrThrowable(facesContext, exceptionContext, e,
                oldRenderResponse, isIgnoreBaseErrorRedirect(facesContext)))
        {
            //ErrorPageWriter.handleThrowable(facesContext, e);
            String errorHandlerClass = getErrorRedirectAlternateHandler(facesContext);
            if (errorHandlerClass == null)
            {
                try {
                    Class clazz = Class.forName(errorHandlerClass);

                    Object errorHandler = clazz.newInstance();

                    Method m = clazz.getMethod("handleThrowable", new Class[]{FacesContext.class,Throwable.class});
                    m.invoke(errorHandler, new Object[]{facesContext, e});
                }
                catch(ClassNotFoundException ex) {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " was not found. Fix your web.xml-parameter : "+ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (IllegalAccessException ex) {
                    throw new ServletException("Constructor of error-Handler : " 
                            +errorHandlerClass
                            + " is not accessible. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (InstantiationException ex) {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " could not be instantiated. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (NoSuchMethodException ex) {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " did not have a method with name : handleException and parameters : javax.faces.context.FacesContext, java.lang.Exception. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (InvocationTargetException ex) {
                    throw new ServletException("Excecution of method handleException in Error-Handler : " 
                            +errorHandlerClass
                            + " threw an exception. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }                
            }
            else
            {
                //throw exception because this redirect handler
                //does not have any rule defined.
                ErrorPageWriter.throwException(e);
            }
        }
    }

    public static void handleExceptionList(FacesContext facesContext,
            List exceptionList) throws ServletException, IOException
    {
        boolean isHandleCauseOrThrowable = false;

        //Try to check if some exception on the list have a applicable
        //navigation rule.
        for (Iterator it = exceptionList.iterator(); it.hasNext();)
        {
            Exception ex = (Exception) it.next();

            boolean oldRenderResponse = facesContext.getRenderResponse();
            ExceptionContext exceptionContext = new ExceptionContext(ex,
                    facesContext.getViewRoot());

            isHandleCauseOrThrowable = handleCauseOrThrowable(facesContext,
                    exceptionContext, ex, oldRenderResponse,
                    isIgnoreBaseErrorRedirect(facesContext));

            if (isHandleCauseOrThrowable)
                break;
        }

        if (!isHandleCauseOrThrowable)
        {
            //If no navigation happened, handle as exception using 
            //the default ErrorPageWriter
            //ErrorPageWriter.handleException(facesContext,
            //        (Exception) exceptionList.get(0));
            String errorHandlerClass = getErrorRedirectAlternateHandler(facesContext);
            if(errorHandlerClass != null) {
                try {
                    Class clazz = Class.forName(errorHandlerClass);

                    Object errorHandler = clazz.newInstance();

                    Method m = clazz.getMethod("handleExceptionList", new Class[]{FacesContext.class,Exception.class});
                    m.invoke(errorHandler, new Object[]{facesContext, exceptionList});
                }
                catch(ClassNotFoundException ex) {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " was not found. Fix your web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (IllegalAccessException ex) {
                    throw new ServletException("Constructor of error-Handler : " 
                            +errorHandlerClass
                            + " is not accessible. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (InstantiationException ex) {
                    throw new ServletException("Error-Handler : " 
                            +errorHandlerClass
                            + " could not be instantiated. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                } catch (NoSuchMethodException ex) {
                    //Handle in the old way, since no custom method handleExceptionList found,
                    //throwing the first FacesException on the list.
                    throw (FacesException) exceptionList.get(0);
                } catch (InvocationTargetException ex) {
                    throw new ServletException("Excecution of method handleException in Error-Handler : " 
                            +errorHandlerClass
                            + " threw an exception. Error-Handler is specified in web.xml-parameter : "
                            +ERROR_REDIRECT_ALTERNATE_HANDLER_PARAMETER,ex);
                }
            }
            else
            {
                ErrorPageWriter.throwException((Exception) exceptionList.get(0));
            }
        }
    }

    private static String getLifecycleId(FacesContext context)
    {
        String lifecycleId = context.getExternalContext().getInitParameter(
                FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId
                : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

    private static boolean handleCauseOrThrowable(FacesContext context,
            ExceptionContext exceptionContext, Throwable th,
            boolean oldRenderResponse, boolean ignoreBaseException)
    {

        boolean throwableHandled = false;
        //First try to find the most inner child in the exception stack
        if (th.getCause() != null)
        {
            throwableHandled = handleCauseOrThrowable(context,
                    exceptionContext, th.getCause(), oldRenderResponse,
                    ignoreBaseException);
        }

        if (throwableHandled == false)
        {
            Class thClass = th.getClass();
            while (thClass != null)
            {
                // ignore java.lang.* superclass names
                if (ignoreBaseException
                        && thClass.getName().startsWith("java.lang."))
                {
                    return false;
                }
                log.info("Trying to redirect to jsf page:" + thClass.getName());
                NavigationHandler nh = context.getApplication()
                        .getNavigationHandler();
                nh.handleNavigation(context, null, thClass.getName());

                //Check if the navigation happened.
                //to call the render response phase
                if (context.getRenderResponse())//navigationHappened(oldRenderResponse, oldViewId, context)
                {
                    //exceptionHandlingContext.setExceptionHandled();
                    log.error("an error occurred : ", th);

                    ValueBinding vb = context.getApplication()
                            .createValueBinding("#{exceptionContext}");

                    vb.setValue(context, exceptionContext);

                    LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
                            .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
                    Lifecycle _lifecycle = lifecycleFactory
                            .getLifecycle(getLifecycleId(context));
                    _lifecycle.render(context);

                    vb.setValue(context, null);

                    context.responseComplete();
                    return true;
                }
                thClass = thClass.getSuperclass();
            }
        }
        return throwableHandled;
    }
}
