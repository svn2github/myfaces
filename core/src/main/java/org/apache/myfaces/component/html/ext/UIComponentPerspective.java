/*
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
package org.apache.myfaces.component.html.ext;

import org.apache.myfaces.component.ExecuteOnCallback;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Marinschek (latest modification by $Author: mmarinschek $)
 * @version $Revision: 371487 $ $Date: 2006-01-23 09:16:20 +0100 (Mo, 23 Jän 2006) $
 */
public class UIComponentPerspective extends UIComponentBase
{
    private UIData uiData;
    private UIComponent delegate;
    private int rowIndex;
    private int oldRowIndex;

    public UIComponentPerspective(UIData uiData, UIComponent delegate, int rowIndex)
    {
        this.uiData = uiData;
        this.rowIndex = rowIndex;
        this.delegate = delegate;
    }

    public UIData getUiData()
    {
        return uiData;
    }

    public void setUiData(UIData uiData)
    {
        this.uiData = uiData;
    }

    public int getRowIndex()
    {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex)
    {
        this.rowIndex = rowIndex;
    }

    public void executeOn(FacesContext context, ExecuteOnCallback callback)
    {
        setupPerspective();
        callback.execute(context, delegate);
        teardownPerspective();
    }

    private UIComponent innerGetDelegate()
    {
        return delegate;
    }

    public Map getAttributes()
    {
        setupPerspective();
        Map retVal = innerGetDelegate().getAttributes();
        teardownPerspective();
        return retVal;
    }

    protected void teardownPerspective()
    {
        uiData.setRowIndex(oldRowIndex);
    }

    protected void setupPerspective()
    {
        oldRowIndex = uiData.getRowIndex();
        uiData.setRowIndex(rowIndex);
    }

    public ValueBinding getValueBinding(String name)
    {
        setupPerspective();
        ValueBinding retVal = innerGetDelegate().getValueBinding(name);
        teardownPerspective();
        return retVal;
    }

    public void setValueBinding(String name, ValueBinding binding)
    {
        setupPerspective();
        innerGetDelegate().setValueBinding(name, binding);
        teardownPerspective();
    }

    public String getClientId(FacesContext context)
    {
        setupPerspective();
        String retVal = innerGetDelegate().getClientId(context);
        teardownPerspective();
        return retVal;
    }

    public String getFamily()
    {
        setupPerspective();
        String retVal = innerGetDelegate().getFamily();
        teardownPerspective();
        return retVal;
    }

    public String getId()
    {
        setupPerspective();
        String retVal = innerGetDelegate().getId();
        teardownPerspective();
        return retVal;
    }

    public void setId(String id)
    {
        throw new UnsupportedOperationException("you are not allowed to set the id through the perspective.");
    }

    public UIComponent getParent()
    {
        setupPerspective();
        UIComponent retVal = innerGetDelegate().getParent();
        teardownPerspective();
        return retVal;
    }

    public void setParent(UIComponent parent)
    {
        throw new UnsupportedOperationException("you are not allowed to set the parent through the perspective.");
    }

    public boolean isRendered()
    {
        setupPerspective();
        boolean retVal = innerGetDelegate().isRendered();
        teardownPerspective();
        return retVal;
    }

    public void setRendered(boolean rendered)
    {
        setupPerspective();
        innerGetDelegate().setRendered(rendered);
        teardownPerspective();
    }

    public String getRendererType()
    {
        setupPerspective();
        String rendererType = innerGetDelegate().getRendererType();
        teardownPerspective();
        return rendererType;
    }

    public void setRendererType(String rendererType)
    {
        setupPerspective();
        innerGetDelegate().setRendererType(rendererType);
        teardownPerspective();
    }

    public boolean getRendersChildren()
    {
        setupPerspective();
        boolean retVal = innerGetDelegate().getRendersChildren();
        teardownPerspective();
        return retVal;
    }

    public List getChildren()
    {
        setupPerspective();
        List retVal = innerGetDelegate().getChildren();
        teardownPerspective();
        return retVal;
    }

    public int getChildCount()
    {
        setupPerspective();
        int retVal = innerGetDelegate().getChildCount();
        teardownPerspective();
        return retVal;
    }

    public UIComponent findComponent(String expr)
    {
        throw new UnsupportedOperationException("you cannot find components via perspectives.");
    }

    public Map getFacets()
    {
        throw new UnsupportedOperationException("you cannot get facets via perspectives.");
    }

    public UIComponent getFacet(String name)
    {
        throw new UnsupportedOperationException("you cannot get a facet via perspectives.");
    }

    public Iterator getFacetsAndChildren()
    {
        throw new UnsupportedOperationException("you cannot find components via perspectives.");
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        throw new UnsupportedOperationException("you cannot find broadcast via perspectives.");
    }

    public void decode(FacesContext context)
    {
        setupPerspective();
        innerGetDelegate().decode(context);
        teardownPerspective();
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        setupPerspective();
        innerGetDelegate().encodeBegin(context);
        teardownPerspective();
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        setupPerspective();
        innerGetDelegate().encodeChildren(context);
        teardownPerspective();
    }

    public void encodeEnd(FacesContext context) throws IOException
    {
        setupPerspective();
        innerGetDelegate().encodeEnd(context);
        teardownPerspective();
    }

    protected void addFacesListener(FacesListener listener)
    {
        throw new UnsupportedOperationException("you cannot add faces listener via perspectives.");
    }

    protected FacesListener[] getFacesListeners(Class clazz)
    {
        throw new UnsupportedOperationException("you cannot get faces listeners via perspectives.");
    }

    protected void removeFacesListener(FacesListener listener)
    {
        throw new UnsupportedOperationException("you cannot remove faces listener via perspectives.");
    }

    public void queueEvent(FacesEvent event)
    {
        throw new UnsupportedOperationException("you cannot queue events via perspectives.");
    }

    public void processRestoreState(FacesContext context, Object state)
    {
        setupPerspective();
        innerGetDelegate().processRestoreState(context, state);
        teardownPerspective();
    }

    public void processDecodes(FacesContext context)
    {
        setupPerspective();
        innerGetDelegate().processDecodes(context);
        teardownPerspective();
    }

    public void processValidators(FacesContext context)
    {
        setupPerspective();
        innerGetDelegate().processValidators(context);
        teardownPerspective();
    }

    public void processUpdates(FacesContext context)
    {
        setupPerspective();
        innerGetDelegate().processUpdates(context);
        teardownPerspective();
    }

    public Object processSaveState(FacesContext context)
    {
        setupPerspective();
        Object retVal=innerGetDelegate().processSaveState(context);
        teardownPerspective();
        return retVal;
    }


    public Object saveState(FacesContext context)
    {
        setupPerspective();
        Object retVal=innerGetDelegate().saveState(context);
        teardownPerspective();
        return retVal;
    }

    public void restoreState(FacesContext context, Object state)
    {
        setupPerspective();
        innerGetDelegate().restoreState(context,state);
        teardownPerspective();
    }

    public boolean isTransient()
    {
        setupPerspective();
        boolean retVal=innerGetDelegate().isTransient();
        teardownPerspective();
        return retVal;
    }

    public void setTransient(boolean newTransientValue)
    {
        setupPerspective();
        innerGetDelegate().setTransient(newTransientValue);
        teardownPerspective();
    }
}
