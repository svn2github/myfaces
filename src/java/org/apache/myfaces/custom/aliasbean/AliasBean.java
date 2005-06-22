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
package org.apache.myfaces.custom.aliasbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The aliasBean tag allows you to link a fictive bean to a real bean.
 * 
 * Let's suppose you have a subform you use often but with different beans.
 * <br/>The aliasBean allows you to design the subform with a fictive bean and
 * to include it in all the pages where you use it. You just need to make an
 * alias to the real bean named after the fictive bean before invoking the
 * fictive bean. <br/>This making it possible to have a library of reusable
 * generic subforms.
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AliasBean extends UIComponentBase {
    private static final Log log = LogFactory.getLog(AliasBean.class);

    public static final String COMPONENT_TYPE = "org.apache.myfaces.AliasBean";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.AliasBean";

    private Alias alias;
	private boolean scopeSearched = false;
	private boolean withinScope;

    private transient FacesContext _context = null;

    public AliasBean() {
        setRendererType(DEFAULT_RENDERER_TYPE);
		alias = new Alias( this );
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setAlias(String aliasBeanExpression){
        alias.setAliasBeanExpression( aliasBeanExpression );
    }
    
    public String getValue(){
		String valueExpression = alias.getValueExpression(); 
        if (valueExpression != null)
            return valueExpression;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    public void setValue(String valueExpression){
        alias.setValueExpression( valueExpression );
    }

    public Object saveState(FacesContext context) {
        log.debug("saveState");

        _context = context;

		Object[] state = {super.saveState(context), alias.saveState()};
		return state;
    }

    public void restoreState(FacesContext context, Object state) {
        log.debug("restoreState");

        _context = context;

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
		alias.restoreState(values[1]);
    }

    public Object processSaveState(FacesContext context) {
        if (context == null)
            throw new NullPointerException("context");
        if (isTransient())
            return null;
		
		makeAlias(context);
		
        Map facetMap = null;
        for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (facetMap == null)
                facetMap = new HashMap();
            UIComponent component = (UIComponent) entry.getValue();
            if (!component.isTransient()) {
                facetMap.put(entry.getKey(), component.processSaveState(context));
            }
        }
        List childrenList = null;
        if (getChildCount() > 0) {
            for (Iterator it = getChildren().iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                if (!child.isTransient()) {
                    if (childrenList == null)
                        childrenList = new ArrayList(getChildCount());
                    childrenList.add(child.processSaveState(context));
                }
            }
        }
		
		removeAlias(context);
		
        return new Object[] { saveState(context), facetMap, childrenList };
    }

    public void processRestoreState(FacesContext context, Object state) {
        if (context == null)
            throw new NullPointerException("context");
        Object myState = ((Object[]) state)[0];

        restoreState(context, myState);
        makeAlias(context);

        Map facetMap = (Map) ((Object[]) state)[1];
        List childrenList = (List) ((Object[]) state)[2];
        for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object facetState = facetMap.get(entry.getKey());
            if (facetState != null) {
                ((UIComponent) entry.getValue()).processRestoreState(context, facetState);
            } else {
                context.getExternalContext().log("No state found to restore facet " + entry.getKey());
            }
        }
        if (getChildCount() > 0) {
            int idx = 0;
            for (Iterator it = getChildren().iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                Object childState = childrenList.get(idx++);
                if (childState != null) {
                    child.processRestoreState(context, childState);
                } else {
                    context.getExternalContext().log("No state found to restore child of component " + getId());
                }
            }
        }

        removeAlias(context);
    }

    public void processValidators(FacesContext context) {
		if( withinScope )
			return;

        log.debug("processValidators");
        makeAlias(context);
        super.processValidators(context);
        removeAlias(context);
    }

	public void processDecodes(FacesContext context) {
		log.debug("processDecodes");
		if( withinScope ){
			if( ! alias.isActive() )
				makeAlias(context);

			super.processDecodes(context);
			return;
		}

		makeAlias(context);
		super.processDecodes(context);
		removeAlias(context);
	}
	
    public void processUpdates(FacesContext context) {
		if( withinScope )
			return;

        log.debug("processUpdates");
        makeAlias(context);
        super.processUpdates(context);
        removeAlias(context);
    }

    public void queueEvent(FacesEvent event) {
		super.queueEvent(new FacesEventWrapper(event, this));
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        makeAlias();

        if (event instanceof FacesEventWrapper) {
            FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
            originalEvent.getComponent().broadcast(originalEvent);
        } else {
            super.broadcast(event);
        }

        removeAlias();
    }

    void makeAlias(FacesContext context) {
        _context = context;
        makeAlias();
    }

    private void makeAlias() {
		if( ! scopeSearched ){
			withinScope =  getParent() instanceof AliasBeansScope;
			if( withinScope ){
				AliasBeansScope aliasScope = (AliasBeansScope) getParent();
				aliasScope.addAlias( alias );
			}
			scopeSearched = true;
		}
       	alias.make( _context );
    }

    void removeAlias(FacesContext context) {
        _context = context;
        removeAlias();
    }

    private void removeAlias() {
        if( ! withinScope )
			alias.remove( _context );
    }
}