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
package org.apache.myfaces.custom.date;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputDate extends UIInput implements UserRoleAware {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputDate";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Date";
    private static final boolean DEFAULT_DISABLED = false;
    
	private Boolean _readonly = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    
    /**
     * Same as for f:convertDateTime 
     * Specifies what contents the string value will be formatted to include, or parsed expecting.
     * Valid values are "date", "time", and "both". Default value is "date".
     */
    private String _type = null;
    private Boolean _popupCalendar = null;
    
    
    private Boolean _disabled = null;
    
    // Values to hold the data entered by the user
    private UserData _userData = null;
    
    public HtmlInputDate() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }
    
    public void setUserData(UserData userData){
        this._userData = userData;
    }

    public UserData getUserData(Locale currentLocale){
        if( _userData == null )
            _userData = new UserData((Date) getValue(), currentLocale);
        return _userData;
    }
    
	public String getType() {
		if (_type != null) return _type;
		ValueBinding vb = getValueBinding("type");
		return vb != null ? (String)vb.getValue(getFacesContext()) : "date";
	}
	public void setType(String string) {
		_type = string;
	}
	
    public boolean isPopupCalendar(){
   		if (_popupCalendar != null)
   		    return _popupCalendar.booleanValue();
   		ValueBinding vb = getValueBinding("popupCalendar");
   		return vb != null ? ((Boolean)vb.getValue(getFacesContext())).booleanValue() : false;
    }
    public void setPopupCalendar(boolean popupCalendar){
        this._popupCalendar = Boolean.valueOf(popupCalendar);
    }

    public boolean isReadonly(){
        if (_readonly != null) return _readonly.booleanValue();
        ValueBinding vb = getValueBinding("readonly");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }
    public void setReadonly(boolean readonly){
        _readonly = Boolean.valueOf(readonly);
    }

    public void setEnabledOnUserRole(String enabledOnUserRole){
        _enabledOnUserRole = enabledOnUserRole;
    }
    public String getEnabledOnUserRole(){
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole){
        _visibleOnUserRole = visibleOnUserRole;
    }
    public String getVisibleOnUserRole(){
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public boolean isRendered(){
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }
	
    public boolean isDisabled(){
        if (_disabled != null) return _disabled.booleanValue();
        ValueBinding vb = getValueBinding("disabled");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_DISABLED;
    }
    public void setDisabled(boolean disabled) {
        _disabled = Boolean.valueOf(disabled);
    }
	
    public Object saveState(FacesContext context) {
        Object values[] = new Object[8];
        values[0] = super.saveState(context);
        values[1] = _type;
        values[2] = _popupCalendar;
        values[3] = _userData;
        values[4] = _disabled;
		values[5] = _readonly;
        values[6] = _enabledOnUserRole;
        values[7] = _visibleOnUserRole;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _type = (String)values[1];
        _popupCalendar = (Boolean)values[2];
        _userData = (UserData)values[3];
        _disabled = (Boolean)values[4];
		_readonly = (Boolean)values[5];
        _enabledOnUserRole = (String)values[6];
        _visibleOnUserRole = (String)values[7];
    }
    
    public static class UserData implements Serializable {
        private String day;
        private String month;
        private String year;
        private String hours;
        private String minutes;
        private String seconds;
        
        public UserData(Date date, Locale currentLocale){
            if( date == null )
                date = new Date();
            
            Calendar calendar = Calendar.getInstance(currentLocale);
            calendar.setTime( date );
            day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            month = Integer.toString(calendar.get(Calendar.MONTH)+1);
            year = Integer.toString(calendar.get(Calendar.YEAR));
            hours = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
            minutes = Integer.toString(calendar.get(Calendar.MINUTE));
            seconds = Integer.toString(calendar.get(Calendar.SECOND));
        }
        
        public Date parse() throws ParseException{
            SimpleDateFormat fullFormat = new SimpleDateFormat( "dd MM yyyy HH mm ss" );
            return fullFormat.parse(day+" "+month+" "+year+" "+hours+" "+minutes+" "+seconds);
        }
        
        private String formatedInt(String toFormat){
            if( toFormat == null )
                return null;
            
            int i = -1;
            try{
                i = Integer.parseInt( toFormat );
            }catch(NumberFormatException nfe){
                return toFormat;
            }
            if( i >= 0 && i < 10 )
                return "0"+i;
            return Integer.toString(i);
        }
        
        
        public String getDay() {
            return formatedInt( day );
        }
        public void setDay(String day) {
            this.day = day;
        }

        public String getMonth() {
            return month;
        }
        public void setMonth(String month) {
            this.month = month;
        }
        
        public String getYear() {
            return year;
        }
        public void setYear(String year) {
            this.year = year;
        }
        
        public String getHours() {
            return formatedInt( hours );
        }
        public void setHours(String hours) {
            this.hours = hours;
        }
        public String getMinutes() {
            return formatedInt( minutes );
        }
        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getSeconds() {
            return formatedInt( seconds );
        }
        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }
    }
}