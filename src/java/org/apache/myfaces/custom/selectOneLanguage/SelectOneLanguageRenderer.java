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
package org.apache.myfaces.custom.selectOneLanguage;

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
 * @version $Revision$ $Date: 2005-05-11 12:14:23 -0400 (Wed, 11 May 2005) $
 */
public class SelectOneLanguageRenderer extends HtmlMenuRenderer {

    private Set getFilterSet(SelectOneLanguage component){
        List selectItems = RendererUtils.getSelectItemList(component);
        Set set = new HashSet( selectItems.size() );        
        
        for (Iterator i = selectItems.iterator(); i.hasNext(); )
        	set.add( ((SelectItem)i.next()).getValue().toString().toLowerCase() );

        return set;
    }
    
	protected List getLanguagesChoicesAsSelectItemList(SelectOneLanguage component){
		//return RendererUtils.getSelectItemList(component);

		Set filterSet = getFilterSet(component);
    	
		String[] availableLanguages = Locale.getISOLanguages();

        Locale currentLocale;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if( viewRoot != null )
            currentLocale = viewRoot.getLocale();
        else
            currentLocale = facesContext.getApplication().getDefaultLocale();


        TreeMap map = new TreeMap();
        // TreeMap is sorted according to the keys' natural order

        for(int i=0; i<availableLanguages.length; i++){
        	String languageCode = availableLanguages[i];
            if( ! filterSet.isEmpty() && ! filterSet.contains(languageCode))
            	continue;
            Locale tmp = new Locale(languageCode);
            map.put(tmp.getDisplayLanguage(currentLocale), languageCode);
        }

        List languagesSelectItems = new ArrayList( map.size() );

        Integer maxLength = component.getMaxLength();
        int maxDescriptionLength = maxLength==null ? Integer.MAX_VALUE : maxLength.intValue();
        if( maxDescriptionLength < 5 )
            maxDescriptionLength = 5;

        for(Iterator i = map.keySet().iterator(); i.hasNext(); ){
        	String languageName = (String) i.next();
            String languageCode = (String) map.get( languageName );
            String label;
            if( languageName.length() <= maxDescriptionLength )
                label = languageName;
            else
                label = languageName.substring(0, maxDescriptionLength-3)+"...";

            languagesSelectItems.add( new SelectItem(languageCode, label) );
        }

        return languagesSelectItems;
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

        List selectItemList = getLanguagesChoicesAsSelectItemList((SelectOneLanguage) component);
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