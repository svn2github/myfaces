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
package org.apache.myfaces.component.html.ext;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFComponent;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFFacet;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;
import org.apache.myfaces.component.NewspaperTable;
import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.custom.sortheader.HtmlCommandSortHeader;
import org.apache.myfaces.renderkit.html.ext.HtmlTableRenderer;
import org.apache.myfaces.renderkit.html.util.TableContext;

/**
 * The MyFacesDataTable extends the standard JSF DataTable by two
 * important features:
 * <br/>
 * <ul>
 *   <li>Possiblity to save the state of the DataModel.</li>
 * 
 *   <li>Support for clickable sort headers (see SortHeader
 *   component).</li>
 * </ul>
 * <br/>
 * Extended data_table that adds some additional features to the 
 * standard data_table action: see attribute descriptions for 
 * preserveDataModel, sortColumn, sortAscending and preserveSort. 
 * <br/>
 * Unless otherwise specified, all attributes accept static values or EL expressions.
 * 
 * @since 1.1.7
 * @author Thomas Spiegl (latest modification by $Author: lu4242 $)
 * @author Manfred Geiler
 * @version $Revision: 691856 $ $Date: 2008-09-03 21:40:30 -0500 (mié, 03 sep 2008) $
 */
@JSFComponent(
   name = "t:dataTable",
   clazz = "org.apache.myfaces.component.html.ext.HtmlDataTable",
   tagClass = "org.apache.myfaces.generated.taglib.html.ext.HtmlDataTableTag")
public abstract class AbstractHtmlDataTable extends HtmlDataTableHack implements UserRoleAware, NewspaperTable
{
    private static final Log log = LogFactory.getLog(AbstractHtmlDataTable.class);

    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private static final boolean DEFAULT_SORTASCENDING = true;
    private static final boolean DEFAULT_SORTABLE = false;
    private static final boolean DEFAULT_EMBEDDED = false;
    private static final boolean DEFAULT_DETAILSTAMP_EXPANDED = false;
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    private static final Integer DEFAULT_NEWSPAPER_COLUMNS = new Integer(1);
    private static final String DEFAULT_NEWSPAPER_ORIENTATION = "vertical";
    private static final String DEFAULT_DETAILSTAMP_LOCATION = "after";

    /**
     * the property names
     */
    public static final String NEWSPAPER_COLUMNS_PROPERTY = "newspaperColumns";
    public static final String SPACER_FACET_NAME = "spacer";
    public static final String NEWSPAPER_ORIENTATION_PROPERTY = "newspaperOrientation";

    private _SerializableDataModel _preservedDataModel;

    private boolean _isValidChildren = true;

    private Map<Integer, Boolean> _expandedNodes = new HashMap<Integer, Boolean>();

    private Map<String, Object> _detailRowStates = new HashMap<String, Object>();

    private TableContext _tableContext = null;

    public TableContext getTableContext()
    {
        if (_tableContext == null)
        {
            _tableContext = new TableContext();
        }
        return _tableContext;
    }

    @Override
    public String getContainerClientId(FacesContext context)
    {
        String standardClientId = super.getContainerClientId(context);
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
        {
            return standardClientId;
        }

        String forcedIdIndex = getForceIdIndexFormula();
        if (forcedIdIndex == null || forcedIdIndex.length() == 0)
            return standardClientId;

        // we can get the index less client id directly, because only
        // getContainerClientId() adds the row index, getClientId() does not.
        final String indexLessClientId = getClientId(context);

        //noinspection UnnecessaryLocalVariable
        String parsedForcedClientId = indexLessClientId + forcedIdIndex;
        return parsedForcedClientId;
    }

    public UIComponent findComponent(String expr)
    {
        if (expr.length() > 0 && Character.isDigit(expr.charAt(0)))
        {
            char separator = UINamingContainer.getSeparatorChar(getFacesContext());
            int separatorIndex = expr.indexOf(separator);

            String rowIndexStr = expr;
            String remainingPart = null;

            if (separatorIndex != -1)
            {
                rowIndexStr = expr.substring(0, separatorIndex);
                remainingPart = expr.substring(separatorIndex + 1);
            }

            int rowIndex = Integer.valueOf(rowIndexStr).intValue();

            if (remainingPart == null)
            {
                log.error("Wrong syntax of expression : " + expr +
                        " rowIndex was provided, but no component name.");
                return null;
            }

            UIComponent comp = super.findComponent(remainingPart);

            if (comp == null)
                return null;

            //noinspection UnnecessaryLocalVariable
            UIComponentPerspective perspective = new UIComponentPerspective(this, comp, rowIndex);
            return perspective;
        }
        else
        {
            return super.findComponent(expr);
        }
    }
    
    

    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId,
            ContextCallback callback) throws FacesException
    {
        if (context == null || clientId == null || callback == null)
        {
            throw new NullPointerException();
        }

        final String baseClientId = getClientId(context);
        
        // searching for this component?
        boolean returnValue = baseClientId.equals(clientId);

        if (returnValue)
        {
            try
            {
                callback.invokeContextCallback(context, this);
                return true;
            }
            catch (Exception e)
            {
                throw new FacesException(e);
            }
        }

        // Now Look throught facets on this UIComponent
        for (Iterator<UIComponent> it = this.getFacets().values().iterator(); !returnValue && it.hasNext();)
        {
            returnValue = it.next().invokeOnComponent(context, clientId, callback);
        }

        if (returnValue)
        {
            return returnValue;
        }
        
        // is the component an inner component?
        if (clientId.startsWith(baseClientId))
        {
            // Check if the clientId for the component, which we 
            // are looking for, has a rowIndex attached
            char separator = UINamingContainer.getSeparatorChar(context);
            if (clientId.matches(baseClientId + separator+"[0-9]+"+separator+".*"))
            {
                String subId = clientId.substring(baseClientId.length() + 1);
                String clientRow = subId.substring(0, subId.indexOf(separator));
    
                //Now we save the current position
                int oldRow = this.getRowIndex();
                
                // try-finally --> make sure, that the old row index is restored
                try
                {
                    //The conversion is safe, because its already checked on the
                    //regular expresion
                    this.setRowIndex(Integer.parseInt(clientRow));
                    
                    // check, if the row is available
                    if (!isRowAvailable())
                    {
                        return false;
                    }
        
                    for (Iterator<UIComponent> it1 = getChildren().iterator(); 
                            !returnValue && it1.hasNext();)
                    {
                        //recursive call to find the component
                        returnValue = it1.next().invokeOnComponent(context, clientId, callback);
                    }
                }
                finally
                {
                    //Restore the old position. Doing this prevent
                    //side effects.
                    this.setRowIndex(oldRow);
                }
            }
            else
            {
                // MYFACES-2370: search the component in the childrens' facets too.
                // We have to check the childrens' facets here, because in MyFaces
                // the rowIndex is not attached to the clientId for the children of
                // facets of the UIColumns. However, in RI the rowIndex is 
                // attached to the clientId of UIColumns' Facets' children.
                for (Iterator<UIComponent> itChildren = this.getChildren().iterator();
                        !returnValue && itChildren.hasNext();)
                {
                    UIComponent child = itChildren.next();
                    // This is the only part different to UIData.invokeOnComponent. Since we have
                    // an auto wrapping on columns feature, it is necessary to check columns ids
                    // without row for invokeOnComponent, but do not traverse all elements, so
                    // save/restore algorithm could be able to remove / add them.  
                    if (child instanceof UIColumn && clientId.equals(child.getClientId(context)))
                    {
                        try {
                            callback.invokeContextCallback(context, child);
                        } catch (Exception e) {
                            throw new FacesException(e);
                        }
                        returnValue = true;
                    }
                    // process the child's facets
                    for (Iterator<UIComponent> itChildFacets = child.getFacets().values().iterator(); 
                            !returnValue && itChildFacets.hasNext();)
                    {
                        //recursive call to find the component
                        returnValue = itChildFacets.next().invokeOnComponent(context, clientId, callback);
                    }
                }
            }
        }

        return returnValue;
    }

    public void setRowIndex(int rowIndex)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (rowIndex < -1)
        {
            throw new IllegalArgumentException("rowIndex is less than -1");
        }

        UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
        /*Just for obtaining an iterator which must be passed to saveDescendantComponentStates()*/
        Set set = new HashSet();
        set.add(facet);
        if (getRowIndex() != -1 && facet != null)
        {
            _detailRowStates.put(getContainerClientId(facesContext), saveDescendantComponentStates(set.iterator(), false));
        }

        String rowIndexVar = getRowIndexVar();
        String rowCountVar = getRowCountVar();
        String previousRowDataVar = getPreviousRowDataVar();
        if (rowIndexVar != null || rowCountVar != null || previousRowDataVar != null)
        {
            Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

            if (previousRowDataVar != null && rowIndex >= 0) //we only need to provide the previousRowDataVar for a valid rowIndex
            {
                if (isRowAvailable())
                {
                    //previous row is available
                    requestMap.put(previousRowDataVar, getRowData());
                }
                else
                {
                    //no previous row available
                    requestMap.put(previousRowDataVar, null);
                }
            }

            super.setRowIndex(rowIndex);

            if (rowIndex >= 0)
            {
                //regular row index, update request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.put(rowIndexVar, new Integer(rowIndex));
                }

                if (rowCountVar != null)
                {
                    requestMap.put(rowCountVar, new Integer(getRowCount()));
                }
            }
            else
            {
                //rowIndex == -1 means end of loop --> remove request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.remove(rowIndexVar);
                }

                if (rowCountVar != null)
                {
                    requestMap.remove(rowCountVar);
                }

                if (previousRowDataVar != null)
                {
                    requestMap.remove(previousRowDataVar);
                }
            }
        }
        else
        {
            // no extended var attributes defined, no special treatment
            super.setRowIndex(rowIndex);
        }

        if (rowIndex != -1 && facet != null)
        {
            Object rowState = _detailRowStates.get(getContainerClientId(facesContext));

            restoreDescendantComponentStates(set.iterator(),
                    rowState, false);

        }

        if (getStateHelper().get(PropertyKeys.varDetailToggler) != null)
        {
            Map requestMap = getFacesContext().getExternalContext().getRequestMap();
            requestMap.put(getStateHelper().get(PropertyKeys.varDetailToggler), this);
        }
    }

    public void processDecodes(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }

        // We must remove and then replace the facet so that
        // it is not processed by default facet handling code
        //
        Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
        super.processDecodes(context);
        if ( facet != null ) getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME, (UIComponent)facet);

        setRowIndex(-1);
        processColumns(context, PROCESS_DECODES);
        setRowIndex(-1);
        processDetails(context, PROCESS_DECODES);
        setRowIndex(-1);
    }

    /**
     * @param context
     * @param processAction
     */
    private void processDetails(FacesContext context, int processAction)
    {
        UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);

        if (facet != null)
        {
            int first = getFirst();
            int rows = getRows();
            int last;
            if (rows == 0)
            {
                last = getRowCount();
            }
            else
            {
                last = first + rows;
            }
            for (int rowIndex = first; last == -1 || rowIndex < last; rowIndex++)
            {
                setRowIndex(rowIndex);

                //scrolled past the last row
                if (!isRowAvailable())
                {
                    break;
                }

                if (!isCurrentDetailExpanded())
                {
                    continue;
                }

                // If we are in the decode phase, the values restored into our
                // facet in setRowIndex() may be incorrect. This will happen
                // if some input fields are rendered in some rows, but not
                // rendered in others. In this case the input field components
                // will still contain the _submittedValue from the previous row
                // that had that input field and _submittedValue will not be set to
                // null by the process() method if there was no value submitted.
                // Thus, an invalid component state for that row will be saved in
                // _detailRowStates. The validation phase will not put a null into
                // _sumbittedValue either, b/c the component is not rendered, so
                // validation code doesn't run. This erroneous value will propagate all the way
                // to the render phase, and result in all rows on the current page being
                // rendered with the "stuck" _submittedValue, rather than evaluating the
                // value to render for every row.
                //
                // We can fix this by initializing _submittedValue of all input fields in the facet
                // to null before calling the process() method below during the decode phase.
                //
                if (PROCESS_DECODES == processAction)
                {
                    resetAllSubmittedValues(facet);
                }

                process(context, facet, processAction);

                if ( rowIndex == (last - 1) )
                {
                    Set set = new HashSet();
                    set.add(facet);
                    _detailRowStates.put(
                            getContainerClientId(FacesContext.getCurrentInstance()),
                                saveDescendantComponentStates(set.iterator(),false));
                }
            }
        }
    }

    private void resetAllSubmittedValues(UIComponent component)
    {
        if (component instanceof EditableValueHolder)
        {
            ((EditableValueHolder) component).setSubmittedValue(null);
        }

        for (Iterator it = component.getFacetsAndChildren(); it.hasNext();)
        {
            resetAllSubmittedValues((UIComponent) it.next());
        }
    }

    /**
     * @param context
     * @param processAction
     */
    private void processColumns(FacesContext context, int processAction)
    {
        for (Iterator it = getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent) it.next();
            if (child instanceof UIColumns)
            {
                process(context, child, processAction);
            }
        }
    }

    private void process(FacesContext context, UIComponent component, int processAction)
    {
        switch (processAction)
        {
            case PROCESS_DECODES:
                component.processDecodes(context);
                break;
            case PROCESS_VALIDATORS:
                component.processValidators(context);
                break;
            case PROCESS_UPDATES:
                component.processUpdates(context);
                break;
        }
    }

    public void processValidators(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        // We must remove and then replace the facet so that
        // it is not processed by default facet handling code
        //
        Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
        super.processValidators(context);
        if ( facet != null ) getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME,(UIComponent) facet);

        processColumns(context, PROCESS_VALIDATORS);
        setRowIndex(-1);
        processDetails(context, PROCESS_VALIDATORS);
        setRowIndex(-1);

        if (context.getRenderResponse())
        {
            _isValidChildren = false;
        }
    }

    public void processUpdates(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }

        // We must remove and then replace the facet so that
        // it is not processed by default facet handling code
        //
        Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
        super.processUpdates(context);
        if ( facet != null ) getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME,(UIComponent) facet);

        processColumns(context, PROCESS_UPDATES);
        setRowIndex(-1);
        processDetails(context, PROCESS_UPDATES);
        setRowIndex(-1);

        if (isPreserveDataModel())
        {
            updateModelFromPreservedDataModel(context);
        }

        if (context.getRenderResponse())
        {
            _isValidChildren = false;
        }
    }

    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        ValueBinding vb = getValueBinding("value");
        if (vb != null && !vb.isReadOnly(context))
        {
            _SerializableDataModel dm = (_SerializableDataModel) getDataModel();
            Class type = vb.getType(context);
            if (DataModel.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm);
            }
            else if (List.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm.getWrappedData());
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(type))
            {
                List lst = (List) dm.getWrappedData();
                vb.setValue(context, lst.toArray(new Object[lst.size()]));
            }
            else if (ResultSet.class.isAssignableFrom(type))
            {
                throw new UnsupportedOperationException(this.getClass().getName()
                        + " UnsupportedOperationException");
            }
            else
            {
                //Assume scalar data model
                List lst = (List) dm.getWrappedData();
                if (lst!= null && lst.size() > 0)
                {
                    vb.setValue(context, lst.get(0));
                }
                else
                {
                    vb.setValue(context, null);
                }
            }
        }
        _preservedDataModel = null;
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (!isRendered())
            return;

        if (_isValidChildren && !hasErrorMessages(context))
        {
            _preservedDataModel = null;
        }

        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumns)
            {
                // Merge the columns from the tomahawk dynamic component
                // into this object.
                ((UIColumns) component).encodeTableBegin(context);
            }
        }

        //replace facet header content component of the columns, with a new command sort header component
        //if sortable=true, replace it for all, if not just for the columns marked as sortable
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumn)
            {
                UIColumn aColumn = (UIColumn) component;
                UIComponent headerFacet = aColumn.getHeader();

                boolean replaceHeaderFacets = isSortable(); //if the table is sortable, all
                //header facets can be changed if needed
                String columnName = null;
                String propertyName = null;
                boolean defaultSorted = false;

                if (aColumn instanceof HtmlSimpleColumn)
                {
                    HtmlSimpleColumn asColumn = (HtmlSimpleColumn) aColumn;
                    propertyName = asColumn.getSortPropertyName();
                    defaultSorted = asColumn.isDefaultSorted();

                    if (asColumn.isSortable())
                        replaceHeaderFacets = true;
                }

                //replace header facet with a sortable header component if needed
                if (replaceHeaderFacets && isSortHeaderNeeded(aColumn, headerFacet))
                {
                    propertyName = propertyName != null ? propertyName : getSortPropertyFromEL(aColumn);
                    if (propertyName == null)
                        log.warn("Couldn't determine sort property for column [" + aColumn.getId() + "].");

                    if (headerFacet instanceof UIPanel)
                    {
                        // In jsf 2.0, facets allow more than one component. That causes a side effect on
                        // facelets mode when auto wrapping is used, because the algorithm is not aware
                        // we wrap everything inside a HtmlCommandSortHeader. We have to check
                        // here that condition and if so, fix it and threat it correctly. 
                        HtmlCommandSortHeader sortHeader = null;
                        for (UIComponent childHeaderFacet : headerFacet.getChildren())
                        {
                            if (childHeaderFacet instanceof HtmlCommandSortHeader)
                            {
                                sortHeader = (HtmlCommandSortHeader) childHeaderFacet;
                                break;
                            }
                        }
                        if (sortHeader != null)
                        {
                            aColumn.getFacets().remove("header");
                            aColumn.setHeader(sortHeader);
                            headerFacet = sortHeader;
                            
                            //command sort headers are already in place, just store the column name and sort property name
                            columnName = sortHeader.getColumnName();
                            propertyName = sortHeader.getPropertyName();

                            //if the command sort header component doesn't specify a sort property, determine it
                            if (propertyName == null)
                            {
                                propertyName = getSortPropertyFromEL(aColumn);
                                sortHeader.setPropertyName(propertyName);
                            }

                            if (propertyName == null)
                                log.warn("Couldn't determine sort property for column [" + aColumn.getId() + "].");                            
                        }
                    }
                    if (headerFacet != null && isSortHeaderNeeded(aColumn, headerFacet))
                    {
                        // We need to force PreRemoveFromViewEvent on the wrapped facet, so we remove it manually here.
                        // Otherwise the component is just moved on the view and no event is triggered. 
                        aColumn.getFacets().remove("header");
                        
                        HtmlCommandSortHeader sortHeader = createSortHeaderComponent(context, aColumn, headerFacet, propertyName);
                        columnName = sortHeader.getColumnName();

                        aColumn.setHeader(sortHeader);
                        //setParent is called internally!
                        //sortHeader.setParent(aColumn);
                    }
                }
                else if (headerFacet instanceof HtmlCommandSortHeader)
                {
                    //command sort headers are already in place, just store the column name and sort property name
                    HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) headerFacet;
                    columnName = sortHeader.getColumnName();
                    propertyName = sortHeader.getPropertyName();

                    //if the command sort header component doesn't specify a sort property, determine it
                    if (propertyName == null)
                    {
                        propertyName = getSortPropertyFromEL(aColumn);
                        sortHeader.setPropertyName(propertyName);
                    }

                    if (propertyName == null)
                        log.warn("Couldn't determine sort property for column [" + aColumn.getId() + "].");
                }

                //make the column marked as default sorted be the current sorted column
                //When getSortColumn() eval to a ValueExpression and it is a String, it could return
                //null but here it is coerced to "", so we need to assume "" as null.
                String sortColumn = getSortColumn();
                if (defaultSorted &&  (sortColumn == null ? true : sortColumn.length() == 0))
                {
                    setSortColumn(columnName);
                    setSortProperty(propertyName);
                }
            }
        }

        // Now invoke the superclass encodeBegin, which will eventually
        // execute the encodeBegin for the associated renderer.
        super.encodeBegin(context);
    }

    /**
     *
     */
    protected boolean isSortHeaderNeeded(UIColumn parentColumn, UIComponent headerFacet)
    {
        return !(headerFacet instanceof HtmlCommandSortHeader);
    }

    /**
     *
     */
    protected HtmlCommandSortHeader createSortHeaderComponent(FacesContext context, UIColumn parentColumn, UIComponent initialHeaderFacet, String propertyName)
    {
        Application application = context.getApplication();

        HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) application.createComponent(HtmlCommandSortHeader.COMPONENT_TYPE);
        String id = context.getViewRoot().createUniqueId();
        sortHeader.setId(id);
        sortHeader.setColumnName(id);
        sortHeader.setPropertyName(propertyName);
        sortHeader.setArrow(true);
        sortHeader.setImmediate(true); //needed to work when dataScroller is present
        sortHeader.getChildren().add(initialHeaderFacet);
        initialHeaderFacet.setParent(sortHeader);

        return sortHeader;
    }

    /**
     *
     */
    protected String getSortPropertyFromEL(UIComponent component)
    {
        if (getVar() == null)
        {
            log.warn("There is no 'var' specified on the dataTable, sort properties cannot be determined automaticaly.");
            return null;
        }

        for (Iterator iter = component.getChildren().iterator(); iter.hasNext();)
        {
            UIComponent aChild = (UIComponent) iter.next();
            if (aChild.isRendered())
            {
                ValueBinding vb = aChild.getValueBinding("value");
                if (vb != null)
                {
                    String expressionString = vb.getExpressionString();

                    int varIndex = expressionString.indexOf(getVar() + ".");
                    if (varIndex != -1)
                    {
                        int varEndIndex = varIndex + getVar().length();
                        String tempProp = expressionString.substring(varEndIndex + 1, expressionString.length());

                        StringTokenizer tokenizer = new StringTokenizer(tempProp, " +[]{}-/*|?:&<>!=()%");
                        if (tokenizer.hasMoreTokens())
                            return tokenizer.nextToken();
                    }
                }
                else
                {
                    String sortProperty = getSortPropertyFromEL(aChild);
                    if (sortProperty != null)
                        return sortProperty;
                }
            }
        }

        return null;
    }

    /**
     * @return the index coresponding to the given column name.
     */
    protected int columnNameToIndex(String columnName)
    {
        int index = 0;
        for (Iterator<UIComponent> iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent aChild = (UIComponent) iter.next();
            if (aChild instanceof UIColumn)
            {
                UIComponent headerFacet = ((UIColumn) aChild).getHeader();
                if (headerFacet != null && headerFacet instanceof HtmlCommandSortHeader)
                {
                    HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) headerFacet;
                    if (columnName != null && columnName.length()>0 && columnName.equals(sortHeader.getColumnName()))
                        return index;
                }
            }

            index += 1;
        }

        return -1;
    }

    /**
     * @see javax.faces.component.UIData#encodeEnd(javax.faces.context.FacesContext)
     */
    public void encodeEnd(FacesContext context) throws IOException
    {
        super.encodeEnd(context);
        for (Iterator<UIComponent> iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumns)
            {
                ((UIColumns) component).encodeTableEnd(context);
            }
        }
    }

    /**
     * The index of the first row to be displayed, where 0 is the first row.
     * 
     */
    @JSFProperty
    public int getFirst()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return _preservedDataModel.getFirst();
        }
        else
        {
            return super.getFirst();
        }
    }

    public void setFirst(int first)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            _preservedDataModel.setFirst(first);
        }
        super.setFirst(first);
    }

    /**
     *  The number of rows to be displayed. Specify zero for all remaining rows in the table.
     * 
     */
    @JSFProperty
    public int getRows()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return _preservedDataModel.getRows();
        }
        else
        {
            return super.getRows();
        }
    }

    public void setRows(int rows)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            _preservedDataModel.setRows(rows);
        }
        super.setRows(rows);
    }

    public Object saveState(FacesContext context)
    {
        boolean preserveSort = isPreserveSort();

        if (initialStateMarked())
        {
            Object parentSaved = super.saveState(context);
            boolean preserveDataModel = isPreserveDataModel();
            
            if (parentSaved == null && !preserveSort 
                    && !preserveDataModel && isExpandedEmpty())
            {
                //No values
                return null;
            }
            
            Object[] values = new Object[5];
            values[0] = parentSaved;
            
            if (isPreserveDataModel())
            {
                _preservedDataModel = getSerializableDataModel();
                values[1] = saveAttachedState(context, _preservedDataModel);
            }
            else
            {
                values[1] = null;
            }
            
            values[2] = preserveSort ? getSortColumn() : null;
            values[3] = preserveSort ? Boolean.valueOf(isSortAscending()) : null;
            values[4] = _expandedNodes;
            return values;
        }
        else
        {
            Object[] values = new Object[5];
            values[0] = super.saveState(context);

            if (isPreserveDataModel())
            {
                _preservedDataModel = getSerializableDataModel();
                values[1] = saveAttachedState(context, _preservedDataModel);
            }
            else
            {
                values[1] = null;
            }
            
            values[2] = preserveSort ? getSortColumn() : null;
            values[3] = preserveSort ? Boolean.valueOf(isSortAscending()) : null;
            values[4] = _expandedNodes;

            return values;
        }
    }

    /**
     * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#getDataModel()
     */
    protected DataModel getDataModel()
    {
        if (_preservedDataModel != null)
        {
            setDataModel(_preservedDataModel);
            _preservedDataModel = null;
        }

        return super.getDataModel();
    }

    /**
     * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#createDataModel()
     */
    protected DataModel createDataModel()
    {
        DataModel dataModel = super.createDataModel();

        boolean isSortable = isSortable();

        if (!(dataModel instanceof SortableModel))
        {
            //if sortable=true make each column sortable
            //if sortable=false, check to see if at least one column sortable case in which
            //the current model needs to be wrapped by a sortable one.
            for (Iterator iter = getChildren().iterator(); iter.hasNext();)
            {
                UIComponent component = (UIComponent) iter.next();
                if (component instanceof HtmlSimpleColumn)
                {
                    HtmlSimpleColumn aColumn = (HtmlSimpleColumn) component;
                    if (isSortable())
                        aColumn.setSortable(true);

                    if (aColumn.isSortable())
                        isSortable = true;

                    String sortColumn = getSortColumn();
                    
                    if (aColumn.isDefaultSorted() && (sortColumn == null ? true : sortColumn.length() == 0))
                        setSortProperty(aColumn.getSortPropertyName());
                }
            }

            if (isSortable)
                dataModel = new SortableModel(dataModel);
        }

        if (isSortable && getSortProperty() != null)
        {
            SortCriterion criterion = new SortCriterion(getSortProperty(), isSortAscending());
            List criteria = new ArrayList();
            criteria.add(criterion);

            ((SortableModel) dataModel).setSortCriteria(criteria);
        }

        return dataModel;
    }

    public void restoreState(FacesContext context, Object state)
    {
        if (state == null)
        {
            return;
        }
        
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        if (isPreserveDataModel())
        {
            _preservedDataModel = (_SerializableDataModel) restoreAttachedState(context, values[1]);
        }
        else
        {
            _preservedDataModel = null;
        }

        if (isPreserveSort())
        {
            String sortColumn = (String) values[2];
            Boolean sortAscending = (Boolean) values[3];
            if (sortColumn != null && sortAscending != null)
            {
                ValueExpression vb = getValueExpression("sortColumn");
                if (vb != null && !vb.isReadOnly(context.getELContext()))
                {
                    vb.setValue(context.getELContext(), sortColumn);
                }

                vb = getValueExpression("sortAscending");
                if (vb != null && !vb.isReadOnly(context.getELContext()))
                {
                    vb.setValue(context.getELContext(), sortAscending);
                }
            }
        }

        _expandedNodes = (Map) values[4];
    }

    public _SerializableDataModel getSerializableDataModel()
    {
        DataModel dm = getDataModel();
        if (dm instanceof _SerializableDataModel)
        {
            return (_SerializableDataModel) dm;
        }
        return createSerializableDataModel();
    }

    /**
     * @return _SerializableDataModel
     */
    private _SerializableDataModel createSerializableDataModel()
    {
        Object value = getValue();
        if (value == null)
        {
            return null;
        }
        else if (value instanceof DataModel)
        {
            return new _SerializableDataModel(getFirst(), getRows(), (DataModel) value);
        }
        else if (value instanceof List)
        {
            return new _SerializableListDataModel(getFirst(), getRows(), (List) value);
        }
        // accept a Collection is not supported in the Spec
        else if (value instanceof Collection)
        {
            return new _SerializableListDataModel(getFirst(), getRows(), new ArrayList((Collection) value));
        }
        else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
        {
            return new _SerializableArrayDataModel(getFirst(), getRows(), (Object[]) value);
        }
        else if (value instanceof ResultSet)
        {
            return new _SerializableResultSetDataModel(getFirst(), getRows(), (ResultSet) value);
        }
        else if (value instanceof javax.servlet.jsp.jstl.sql.Result)
        {
            return new _SerializableResultDataModel(getFirst(), getRows(),
                    (javax.servlet.jsp.jstl.sql.Result) value);
        }
        else
        {
            return new _SerializableScalarDataModel(getFirst(), getRows(), value);
        }
    }

    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this))
            return false;
        return super.isRendered();
    }

    public void setForceIdIndexFormula(String forceIdIndexFormula)
    {
        getStateHelper().put(PropertyKeys.forceIdIndexFormula, forceIdIndexFormula);
        ValueExpression vb = getValueExpression("forceIdIndexFormula");
        if (vb != null)
        {
            vb.setValue(getFacesContext().getELContext(), forceIdIndexFormula);
            getStateHelper().put(PropertyKeys.forceIdIndexFormula, null);
        }
    }

    /**
     * A formula that overrides the default row index in the 
     * construction of table's body components. 
     * 
     * Example : #{myRowVar.key} Warning, the EL should 
     * evaluate to a unique value for each row !
     * 
     */
    @JSFProperty
    public String getForceIdIndexFormula()
    {
        return (String) getStateHelper().eval(PropertyKeys.forceIdIndexFormula);
    }

    /**
     * Specify what column the data should be sorted on.
     * <p/>
     * Note that calling this method <i>immediately</i> stores the value
     * via any value-binding with name "sortColumn". This is done because
     * this method is called by the HtmlCommandSortHeader component when
     * the user has clicked on a column's sort header. In this case, the
     * the model getter method mapped for name "value" needs to read this
     * value in able to return the data in the desired order - but the
     * HtmlCommandSortHeader component is usually "immediate" in order to
     * avoid validating the enclosing form. Yes, this is rather hacky -
     * but it works.
     */
    public void setSortColumn(String sortColumn)
    {
        getStateHelper().put(PropertyKeys.sortColumn, sortColumn);
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueExpression vb = getValueExpression("sortColumn");
        if (vb != null)
        {
            vb.setValue(getFacesContext().getELContext(), sortColumn);
            getStateHelper().put(PropertyKeys.sortColumn, null);
        }

        setSortColumnIndex(columnNameToIndex(sortColumn));
    }

    /**
     * Value reference to a model property that gives the current 
     * sort column name. The target String property is set to 
     * the "columnName" of whichever column has been chosen 
     * to sort by, and the method which is bound to the "value" 
     * attribute of this table (ie which provides the DataModel used) 
     * is expected to use this property to determine how to sort 
     * the DataModel's contents.
     * 
     */
    @JSFProperty
    public String getSortColumn()
    {
        return (String) getStateHelper().eval(PropertyKeys.sortColumn);
    }

    public void setSortAscending(boolean sortAscending)
    {
        getStateHelper().put(PropertyKeys.sortAscending, sortAscending);
        
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueExpression vb = getValueExpression("sortAscending");
        if (vb != null)
        {
            vb.setValue(getFacesContext().getELContext(), sortAscending);
            getStateHelper().put(PropertyKeys.sortAscending, null);
        }
    }

    /**
     * Value reference to a model property that gives the current 
     * sort direction. The target Boolean property is set to true 
     * when the selected sortColumn should be sorted in ascending 
     * order, and false otherwise. The method which is bound to 
     * the "value" attribute of this table (ie which provides the 
     * DataModel used) is expected to use this property to 
     * determine how to sort the DataModel's contents.
     * 
     */
    @JSFProperty(defaultValue="true")
    public boolean isSortAscending()
    {
        return (Boolean) getStateHelper().eval(PropertyKeys.sortAscending, DEFAULT_SORTASCENDING);
    }

    public abstract void setSortProperty(String sortProperty);
    
    /**
     * The name of a javabean property on which the table is sorted.
     * <p>
     * The datamodel should contain objects that have this property;
     * reflection will be used to sort the datamodel on that property
     * using the default comparator for that type. 
     * <p>
     * This value is part of the component state. However it is not
     * directly settable by users; instead it is set by other components
     * such as a CommandSortHeader.
     */
    @JSFProperty(literalOnly=true,tagExcluded=true)
    public abstract String getSortProperty();

    /**
     * Define if the table is sortable or not
     * 
     */
    @JSFProperty(defaultValue="false")
    public abstract boolean isSortable();

    /**
     * Avoids rendering the html table tags, thus, giving you a 
     * table rendering just rows. You can use this together 
     * with the detailStamp faces of the parent datatable 
     * to render child-tables using the same layout as the parent. 
     * 
     * Notice: You have to ensure both tables do have the same 
     * number of columns. Using the colspan attribute of the 
     * column tag might help alot.
     * 
     */
    @JSFProperty(defaultValue="false")
    public abstract boolean isEmbedded();

    /**
     * true|false - true if the detailStamp should be expanded by default. default: false
     * 
     */
    @JSFProperty(defaultValue="false")
    public abstract boolean isDetailStampExpandedDefault();

    /**
     * before|after - where to render the detailStamp, before the 
     * actual row or after it. default: after
     * 
     */
    @JSFProperty(defaultValue="after")
    public abstract String getDetailStampLocation();

    /**
     * Defines a JavaScript onmouseover event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowMouseOver")
    public abstract String getRowOnMouseOver();

    /**
     * Defines a JavaScript onmouseout event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowMouseOut")
    public abstract String getRowOnMouseOut();

    /**
     * Defines a JavaScript onclick event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowClick")
    public abstract String getRowOnClick();

    /**
     * Defines a JavaScript ondblclick event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowDblClick")
    public abstract String getRowOnDblClick();

    /**
     * Defines a JavaScript onkeydown event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowKeyDown")
    public abstract String getRowOnKeyDown();

    /**
     * Defines a JavaScript onkeypress event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowKeyPress")
    public abstract String getRowOnKeyPress();

    /**
     * Defines a JavaScript onkeyup event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowKeyUp")
    public abstract String getRowOnKeyUp();

    /**
     * Corresponds to the HTML class attribute for the row tr tag.
     * 
     */
    @JSFProperty
    public abstract String getRowStyleClass();

    /**
     * Corresponds to the HTML style attribute for the row tr tag.
     * 
     */
    @JSFProperty
    public abstract String getRowStyle();

    /**
     * Defines a JavaScript onmpusedown event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowMouseDown")
    public abstract String getRowOnMouseDown();

    /**
     * Defines a JavaScript onmousemove event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowMouseMove")
    public abstract String getRowOnMouseMove();

    /**
     * Defines a JavaScript onmouseup event handler for each table row
     * 
     */
    @JSFProperty(clientEvent="rowMouseUp")
    public abstract String getRowOnMouseUp();

    /**
     */
    @JSFProperty(tagExcluded=true)
    protected boolean isValidChildren()
    {
        return _isValidChildren;
    }

    protected void setIsValidChildren(boolean isValidChildren)
    {
        _isValidChildren = isValidChildren;
    }

    protected _SerializableDataModel getPreservedDataModel()
    {
        return _preservedDataModel;
    }

    protected void setPreservedDataModel(_SerializableDataModel preservedDataModel)
    {
        _preservedDataModel = preservedDataModel;
    }


    public boolean isCurrentDetailExpanded()
    {
        Boolean expanded = (Boolean) _expandedNodes.get(new Integer(getRowIndex()));
        if (expanded != null)
        {
            return expanded.booleanValue();
        }

        return isDetailStampExpandedDefault();
    }

    public void setVarDetailToggler(String varDetailToggler)
    {
        getStateHelper().put(PropertyKeys.varDetailToggler, varDetailToggler ); 
    }

    /**
     *  This variable has the boolean property "currentdetailExpanded" 
     *  which is true if the current detail row is expanded and the 
     *  action method "toggleDetail" which expand/collapse the current 
     *  detail row.
     * 
     */
    @JSFProperty
    public String getVarDetailToggler()
    {
        return (String) getStateHelper().eval(PropertyKeys.varDetailToggler);
    }

    /**
     * Corresponds to the HTML style attribute for grouped rows.
     *  
     */
    @JSFProperty
    public abstract String getRowGroupStyle();

    /**
     * StyleClass for grouped rows.
     * 
     */
    @JSFProperty
    public abstract String getRowGroupStyleClass();
    
    /**
     * Corresponds to the HTML style attribute for the table body tag
     * 
     */
    @JSFProperty
    public abstract String getBodyStyle();

    /**
     * Corresponds to the HTML class attribute for the table body tag.
     * 
     */
    @JSFProperty
    public abstract String getBodyStyleClass();

    public AbstractHtmlDataTable()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * Change the status of the current detail row from collapsed to expanded or
     * viceversa.
     */
    public void toggleDetail()
    {
        Integer rowIndex = new Integer(getRowIndex());

        // get the current expanded state of the row
        boolean expanded = isDetailExpanded();
        if (expanded)
        {
            // toggle to "collapsed"

            if (isDetailStampExpandedDefault())
            {
                // if default is expanded we have to override with FALSE here
                _expandedNodes.put(rowIndex, Boolean.FALSE);
            }
            else
            {
                // if default is collapsed we can fallback to this default
                _expandedNodes.remove(rowIndex);
            }
        }
        else
        {
            // toggle to "expanded"

            if (isDetailStampExpandedDefault())
            {
                // if default is expanded we can fallback to this default
                _expandedNodes.remove(rowIndex);
            }
            else
            {
                // if default is collapsed we have to override with TRUE
                _expandedNodes.put(rowIndex, Boolean.TRUE);
            }
        }
    }

    /**
     * Return true if the current detail row is expanded.
     *
     * @return true if the current detail row is expanded.
     */
    public boolean isDetailExpanded()
    {
        Integer rowIndex = new Integer(getRowIndex());

        Boolean expanded = (Boolean) _expandedNodes.get(rowIndex);
        if (expanded == null)
        {
            return isDetailStampExpandedDefault();
        }

        return expanded.booleanValue();
    }

    public int getSortColumnIndex()
    {
        Integer sortColumnIndex = (Integer) getStateHelper().get(PropertyKeys.sortColumnIndex);
        if (sortColumnIndex == null)
        {
            //By default is -1 
            sortColumnIndex = -1;
        }
        if (sortColumnIndex == -1)
            sortColumnIndex = columnNameToIndex(getSortColumn());

        return sortColumnIndex;
    }

    public void setSortColumnIndex(int sortColumnIndex)
    {
        getStateHelper().put(PropertyKeys.sortColumnIndex, sortColumnIndex ); 
    }

    /**
     * The number of columns to wrap the table over. Default: 1
     * 
     * Set the number of columns the table will be divided over.
     * 
     */
    @JSFProperty(defaultValue="1")
    public abstract int getNewspaperColumns();

    /**
     * The orientation of the newspaper columns in the newspaper 
     * table - "horizontal" or "vertical". Default: vertical
     * 
     */
    @JSFProperty(defaultValue = "vertical")
    public abstract String getNewspaperOrientation();

    /**
     * Gets the spacer facet, between each pair of newspaper columns.
     * 
     */
    @JSFFacet
    public UIComponent getSpacer()
    {
        return (UIComponent) getFacets().get(SPACER_FACET_NAME);
    }

    public void setSpacer(UIComponent spacer)
    {
        getFacets().put(SPACER_FACET_NAME, spacer);
    }

    /**
     * Expand all details
     */
    public void expandAllDetails()
    {
        int rowCount = getRowCount();

        _expandedNodes.clear();
        for (int row = 0; row < rowCount; row++)
        {
            _expandedNodes.put(new Integer(row), Boolean.TRUE);
        }
    }

    /**
     * Collapse all details
     */
    public void collapseAllDetails()
    {
        _expandedNodes.clear();
    }

    /**
     * @return true is any of the details is expanded
     */
    public boolean isExpandedEmpty()
    {
        boolean expandedEmpty = true;
        if (_expandedNodes != null)
        {
            expandedEmpty = _expandedNodes.isEmpty();
        }
        return expandedEmpty;
    }

    /**
     * Clears expanded nodes set if expandedEmpty is true
     *
     * @param expandedEmpty
     */
    public void setExpandedEmpty(boolean expandedEmpty)
    {
        if (expandedEmpty)
        {
            if (_expandedNodes != null)
            {
                _expandedNodes.clear();
            }
        }
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataTable";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Table";

    private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
    private static final boolean DEFAULT_PRESERVESORT = true;
    private static final boolean DEFAULT_RENDEREDIFEMPTY = true;


    /**
     * Indicates whether the state of the whole DataModel should 
     * be saved and restored. When set to false, the value-binding 
     * for the "value" attribute of this table is executed each 
     * time the page is rendered. When set to true, that 
     * value-binding is only executed when the component is first 
     * created, and the DataModel state is thereafter saved/restored 
     * automatically by the component. When column sorting is 
     * used for a table this property needs to be false so that 
     * the DataModel can be updated to reflect any changes in the 
     * sort criteria. Default: false
     * 
     */
    @JSFProperty
    public abstract boolean isPreserveDataModel();

    /**
     * Indicates whether the state of the sortColumn and sortAscending 
     * attribute should be saved and restored and written back to the 
     * model during the update model phase. Default: true
     * 
     */
    @JSFProperty(defaultValue = "true")
    public abstract boolean isPreserveSort();

    /**
     * Indicates whether this table should be rendered if the 
     * underlying DataModel is empty. You could as well use 
     * rendered="#{not empty bean.list}", but this one causes 
     * the getList method of your model bean beeing called up 
     * to five times per request, which is not optimal when 
     * the list is backed by a DB table. Using 
     * renderedIfEmpty="false" solves this problem, because 
     * the MyFaces extended HtmlDataTable automatically caches 
     * the DataModel and calles the model getter only once 
     * per request. Default: true
     * 
     */
    @JSFProperty(defaultValue = "true")
    public abstract boolean isRenderedIfEmpty();

    /**
     * A parameter name, under which the current rowIndex is set 
     * in request scope similar to the var parameter.
     * 
     */
    @JSFProperty
    public abstract String getRowIndexVar();
    
    /**
     * A parameter name, under which the rowCount is set in 
     * request scope similar to the var parameter.
     * 
     */
    @JSFProperty
    public abstract String getRowCountVar();

    /**
     * A parameter name, under which the previous RowData Object 
     * is set in request scope similar to the rowIndexVar and 
     * rowCountVar parameters. Mind that the value of this 
     * request scope attribute is null in the first row or 
     * when isRowAvailable returns false for the previous row.
     * 
     */
    @JSFProperty
    public abstract String getPreviousRowDataVar();

    /**
     * A parameter name, under which the a boolean is set in request 
     * scope similar to the var parameter. TRUE for the column that 
     * is currently sorted, FALSE otherwise.
     * 
     */
    @JSFProperty
    public abstract String getSortedColumnVar();
    
    /**
     * HTML: Specifies the horizontal alignment of this element. 
     * Deprecated in HTML 4.01.
     * 
     */
    @JSFProperty
    public abstract String getAlign();

    /**
     * The id to use for
     * 
     */
    @JSFProperty
    public abstract String getRowId();
        
    /**
     * Reserved for future use.
     * 
     */
    @JSFProperty
    public abstract String getDatafld();
    
    /**
     * Reserved for future use.
     * 
     */
    @JSFProperty
    public abstract String getDatasrc();
    
    /**
     * Reserved for future use.
     * 
     */
    @JSFProperty
    public abstract String getDataformatas();
    
    protected enum PropertyKeys
    {
        preservedDataModel
        , forceIdIndexFormula
        , sortColumn
        , sortAscending
        , varDetailToggler
        , expandedNodes
        , sortColumnIndex
    }
}
