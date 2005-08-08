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
package org.apache.myfaces.custom.selectOneCountry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlMenuRenderer;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectOneCountryRenderer extends HtmlMenuRenderer {
	
    private Set getFilterSet(SelectOneCountry component){
        List selectItems = RendererUtils.getSelectItemList(component);
        Set set = new HashSet( selectItems.size() );        
        
        for (Iterator i = selectItems.iterator(); i.hasNext(); )
        	set.add( ((SelectItem)i.next()).getValue().toString().toUpperCase() );

        return set;
    }
    
	protected List getCountriesChoicesAsSelectItemList(SelectOneCountry component){
		//return RendererUtils.getSelectItemList(component);

		Set filterSet = getFilterSet(component);
    	
        String[] availableCountries = Locale.getISOCountries();

        Locale currentLocale;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if( viewRoot != null )
            currentLocale = viewRoot.getLocale();
        else
            currentLocale = facesContext.getApplication().getDefaultLocale();


        TreeMap map = new TreeMap();
        // TreeMap is sorted according to the keys' natural order

        for(int i=0; i<availableCountries.length; i++){
            String countryCode = availableCountries[i];
            if( ! filterSet.isEmpty() && ! filterSet.contains(countryCode))
            	continue;
            Locale tmp = new Locale(countryCode, countryCode);
            map.put(tmp.getDisplayCountry(currentLocale), countryCode);
        }

        List countriesSelectItems = new ArrayList( map.size());

        Integer maxLength = component.getMaxLength();
        int maxDescriptionLength = maxLength==null ? Integer.MAX_VALUE : maxLength.intValue();
        if( maxDescriptionLength < 5 )
            maxDescriptionLength = 5;

        for(Iterator i = map.keySet().iterator(); i.hasNext(); ){
            String countryName = (String) i.next();
            String countryCode = (String) map.get( countryName );
            String label;
            if( countryName.length() <= maxDescriptionLength )
                label = countryName;
            else{
                label = countryName.substring(0, maxDescriptionLength-3)+"...";
            }

            countriesSelectItems.add( new SelectItem(countryCode, label) );
        }

        return countriesSelectItems;
    }
	
	public void encodeEnd(FacesContext facesContext, UIComponent component)
    throws IOException
	{
		RendererUtils.checkParamValidity(facesContext, component, null);
		
		if(HtmlRendererUtils.isDisplayValueOnly(component))
		{
		    HtmlRendererUtils.renderDisplayValueOnlyForSelects(facesContext, component);
		    return;
		}

		ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SELECT_ELEM, component);
        HtmlRendererUtils.writeIdIfNecessary(writer, component, facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, component.getClientId(facesContext), null);

        List selectItemList = getCountriesChoicesAsSelectItemList((SelectOneCountry) component);
        Converter converter = HtmlRendererUtils.findUIOutputConverterFailSafe(facesContext, component);

        writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(1), null);

        HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.SELECT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if ( isDisabled(facesContext, component) ) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        Set lookupSet = HtmlRendererUtils.getSubmittedOrSelectedValuesAsSet(false, component, facesContext, converter);

        HtmlRendererUtils.renderSelectOptions(facesContext, component, converter, lookupSet, selectItemList);
        // bug #970747: force separate end tag
        writer.writeText("", null);
        writer.endElement(HTML.SELECT_ELEM);
	}
}