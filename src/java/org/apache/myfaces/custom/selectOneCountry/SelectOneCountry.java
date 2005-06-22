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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.faces.component.UISelectItem;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectOneCountry extends HtmlSelectOneMenu {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.SelectOneCountry";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SelectOneCountryRenderer";

    private Integer _maxLength = null;

    public SelectOneCountry() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

	public Integer getMaxLength() {
		if (_maxLength != null) return _maxLength;
		ValueBinding vb = getValueBinding("length");
		return vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
	}
	public void setMaxLength(Integer maxLength) {
		_maxLength = maxLength;
	}

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _maxLength;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _maxLength = (Integer)values[1];
    }

    // -------- Over ridden UIComponent methods -----------

    public void encodeChildren(FacesContext context){
        // noop
    }

    public int getChildCount(){
        return Locale.getISOCountries().length;
    }

    public List getChildren(){
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
            Locale tmp = new Locale(countryCode, countryCode);
            map.put(tmp.getDisplayCountry(currentLocale), countryCode);
        }

        List countriesSelectItems = new ArrayList(availableCountries.length);

        int maxDescriptionLength = _maxLength==null ? Integer.MAX_VALUE : _maxLength.intValue();
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

            UISelectItem selectItem = new UISelectItem();
            selectItem.setValue( new SelectItem(countryCode, label) );

            countriesSelectItems.add( selectItem );
        }

        return countriesSelectItems;
    }
}