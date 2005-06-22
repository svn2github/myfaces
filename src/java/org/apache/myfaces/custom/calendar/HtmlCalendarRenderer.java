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
package org.apache.myfaces.custom.calendar;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCalendarRenderer
        extends HtmlRenderer
{

    //private static Log log = LogFactory.getLog(HtmlCalendarRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlInputCalendar.class);

        HtmlInputCalendar inputCalendar = (HtmlInputCalendar) component;

        Locale currentLocale = facesContext.getViewRoot().getLocale();


        Date value;

        try
        {
            value = RendererUtils.getDateValue(inputCalendar);
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            value = null;
        }


        Calendar timeKeeper = Calendar.getInstance(currentLocale);
        timeKeeper.setTime(value!=null?value:new Date());

        DateFormatSymbols symbols = new DateFormatSymbols(currentLocale);

        String[] weekdays = mapWeekdays(symbols);
        String[] months = mapMonths(symbols);

        if(inputCalendar.isRenderAsPopup())
        {
            if(inputCalendar.isAddResources())
                addScriptAndCSSResources(facesContext);

            String dateFormat = CalendarDateTimeConverter.createJSPopupFormat(facesContext,
                    inputCalendar.getPopupDateFormat());

            Application application = facesContext.getApplication();

            HtmlInputText inputText = null;

            List li = inputCalendar.getChildren();

            for (int i = 0; i < li.size(); i++)
            {
                UIComponent uiComponent = (UIComponent) li.get(i);

                if(uiComponent instanceof HtmlInputText)
                {
                    inputText = (HtmlInputText) uiComponent;
                    break;
                }
            }

            if(inputText == null)
            {
                inputText = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
            }

            RendererUtils.copyHtmlInputTextAttributes(inputCalendar, inputText);

            inputText.setTransient(true);

            if (value == null && inputCalendar.getSubmittedValue() != null)
            {
                inputText.setValue(inputCalendar.getSubmittedValue());
            }
            else
            {
                inputText.setValue(getConverter(inputCalendar).getAsString(
                        facesContext,inputCalendar,value));
            }
            inputText.setEnabledOnUserRole(inputCalendar.getEnabledOnUserRole());
            inputText.setVisibleOnUserRole(inputCalendar.getVisibleOnUserRole());

            //This is where to components with the same id are in the tree,
            //so make sure that during the rendering the id is unique.

            inputCalendar.setId(facesContext.getViewRoot().createUniqueId());

            inputCalendar.getChildren().add(inputText);

            RendererUtils.renderChild(facesContext, inputText);

            inputCalendar.getChildren().remove(inputText);
                        
            //Set back the correct id to the input calendar
            inputCalendar.setId(inputText.getId());

            ResponseWriter writer = facesContext.getResponseWriter();

            writer.startElement(HTML.SCRIPT_ELEM,null);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
            writer.write("<!--\n");
            writer.writeText(getLocalizedLanguageScript(symbols, months,
                    timeKeeper.getFirstDayOfWeek(),inputCalendar),null);
            writer.writeText(getScriptBtnText(inputCalendar.getClientId(facesContext),
                    dateFormat,inputCalendar.getPopupButtonString()),null);
            writer.write("\n-->");
            writer.endElement(HTML.SCRIPT_ELEM);

/*            writer.startElement(HTML.INPUT_ELEM,null);
            writer.writeAttribute(HTML.TYPE_ATTR,HTML.INPUT_TYPE_BUTTON,null);
            writer.writeAttribute(HTML.ONCLICK_ATTR,"popUpCalendar(this, "+inputText.getClientId(facesContext)+
                    ", \\\"dd.mm.yyyy\\\")",null);
            writer.endElement(HTML.INPUT_TYPE_BUTTON);*/
        }
        else
        {

            int lastDayInMonth = timeKeeper.getActualMaximum(Calendar.DAY_OF_MONTH);

            int currentDay = timeKeeper.get(Calendar.DAY_OF_MONTH);

            if (currentDay > lastDayInMonth)
                currentDay = lastDayInMonth;

            timeKeeper.set(Calendar.DAY_OF_MONTH, 1);

            int weekDayOfFirstDayOfMonth = mapCalendarDayToCommonDay(timeKeeper.get(Calendar.DAY_OF_WEEK));

            int weekStartsAtDayIndex = mapCalendarDayToCommonDay(timeKeeper.getFirstDayOfWeek());

            ResponseWriter writer = facesContext.getResponseWriter();

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);

            writer.startElement(HTML.TABLE_ELEM, component);
            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.UNIVERSAL_ATTRIBUTES);
            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.EVENT_HANDLER_ATTRIBUTES);
            writer.flush();

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);

            writer.startElement(HTML.TR_ELEM, component);

            if(inputCalendar.getMonthYearRowClass() != null)
                writer.writeAttribute(HTML.CLASS_ATTR, inputCalendar.getMonthYearRowClass(), null);

            writeMonthYearHeader(facesContext, writer, inputCalendar, timeKeeper,
                    currentDay, weekdays, months);

            writer.endElement(HTML.TR_ELEM);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);

            writer.startElement(HTML.TR_ELEM, component);

            if(inputCalendar.getWeekRowClass() != null)
                writer.writeAttribute(HTML.CLASS_ATTR, inputCalendar.getWeekRowClass(), null);

            writeWeekDayNameHeader(weekStartsAtDayIndex, weekdays,
                    facesContext, writer, inputCalendar);

            writer.endElement(HTML.TR_ELEM);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);

            writeDays(facesContext, writer, inputCalendar, timeKeeper,
                    currentDay, weekStartsAtDayIndex, weekDayOfFirstDayOfMonth,
                    lastDayInMonth, weekdays);

            writer.endElement(HTML.TABLE_ELEM);
        }
    }

    /**
     * Used by the x:inputDate renderer : HTMLDateRenderer
     * @throws IOException
     */
    static public void addScriptAndCSSResources(FacesContext facesContext) throws IOException{
        // Add the javascript and CSS pages
        AddResource.addStyleSheet(HtmlCalendarRenderer.class, "WH/theme.css", facesContext);
        AddResource.addStyleSheet(HtmlCalendarRenderer.class, "DB/theme.css", facesContext);
        AddResource.addJavaScriptToHeader(HtmlCalendarRenderer.class, "popcalendar.js", facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.write(
                "jscalendarSetImageDirectory(\""
                	+JavascriptUtils.encodeString(
                	        AddResource.getResourceMappedPath(HtmlCalendarRenderer.class, "DB/", facesContext)
                	 )
                +"\")");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    public static String getLocalizedLanguageScript(DateFormatSymbols symbols,
            String[] months, int firstDayOfWeek, HtmlInputCalendar inputCalendar)
    {
        int realFirstDayOfWeek = firstDayOfWeek-1/*Java has different starting-point*/;

        String[] weekDays;

        if(realFirstDayOfWeek==0)
        {
            weekDays = mapWeekdaysStartingWithSunday(symbols);
        }
        else if(realFirstDayOfWeek==1)
        {
            weekDays = mapWeekdays(symbols);
        }
        else
            throw new IllegalStateException("Week may only start with sunday or monday.");

        StringBuffer script = new StringBuffer();
        defineStringArray(script, "jscalendarMonthName", months);
        defineStringArray(script, "jscalendarDayName", weekDays);
        setIntegerVariable(script, "jscalendarStartAt",realFirstDayOfWeek);

        if( inputCalendar != null ){ // To allow null parameter for inputDate tag.
	        if(inputCalendar.getPopupGotoString()!=null)
	            setStringVariable(script, "jscalendarGotoString",inputCalendar.getPopupGotoString());
	        if(inputCalendar.getPopupTodayString()!=null)
	            setStringVariable(script, "jscalendarTodayString",inputCalendar.getPopupTodayString());
	        if(inputCalendar.getPopupWeekString()!=null)
	            setStringVariable(script, "jscalendarWeekString",inputCalendar.getPopupWeekString());
	        if(inputCalendar.getPopupScrollLeftMessage()!=null)
	            setStringVariable(script, "jscalendarScrollLeftMessage",inputCalendar.getPopupScrollLeftMessage());
	        if(inputCalendar.getPopupScrollRightMessage()!=null)
	            setStringVariable(script, "jscalendarScrollRightMessage",inputCalendar.getPopupScrollRightMessage());
	        if(inputCalendar.getPopupSelectMonthMessage()!=null)
	            setStringVariable(script, "jscalendarSelectMonthMessage",inputCalendar.getPopupSelectMonthMessage());
	        if(inputCalendar.getPopupSelectYearMessage()!=null)
	            setStringVariable(script, "jscalendarSelectYearMessage",inputCalendar.getPopupSelectYearMessage());
	        if(inputCalendar.getPopupSelectDateMessage()!=null)
	            setStringVariable(script, "jscalendarSelectDateMessage",inputCalendar.getPopupSelectDateMessage());
        }

        return script.toString();
    }

    private static void setIntegerVariable(StringBuffer script, String name, int value)
    {
        script.append(name);
        script.append(" = ");
        script.append(value);
        script.append(";\n");
    }

    private static void setStringVariable(StringBuffer script, String name, String value)
    {
        script.append(name);
        script.append(" = \"");
        script.append(value);
        script.append("\";\n");
    }

    private static void defineStringArray(StringBuffer script, String arrayName, String[] array)
    {
        script.append(arrayName);
        script.append(" = new Array(");

        for(int i=0;i<array.length;i++)
        {
            if(i!=0)
                script.append(",");

            script.append("\"");
            script.append(array[i]);
            script.append("\"");
        }

        script.append(");");
    }

    private String getScriptBtnText(String clientId, String dateFormat, String popupButtonString)
    {

        StringBuffer script = new StringBuffer();
        script.append("if (!document.layers) {\n");
        script.append("document.write(");
        script.append("\"<input type='button' onclick='jscalendarPopUpCalendar(this,this.form.elements[\\\"");
        script.append(clientId);
        script.append("\\\"],\\\"");
        script.append(dateFormat);
        script.append("\\\")' value='");
        if(popupButtonString==null)
            popupButtonString="...";
        script.append(popupButtonString);
        script.append("'/>\"");
        script.append(");");
        script.append("\n}");

        return script.toString();
    }

    private void writeMonthYearHeader(FacesContext facesContext, ResponseWriter writer, UIInput inputComponent, Calendar timeKeeper,
                                      int currentDay, String[] weekdays,
                                      String[] months)
            throws IOException
    {
        Calendar cal = shiftMonth(facesContext, timeKeeper, currentDay, -1);

        writeCell(facesContext, writer, inputComponent, "<", cal.getTime(), null);

        writer.startElement(HTML.TD_ELEM, inputComponent);
        writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(weekdays.length - 2), null);
        writer.writeText(months[timeKeeper.get(Calendar.MONTH)] + " " + timeKeeper.get(Calendar.YEAR), null);
        writer.endElement(HTML.TD_ELEM);

        cal = shiftMonth(facesContext, timeKeeper, currentDay, 1);

        writeCell(facesContext, writer, inputComponent, ">", cal.getTime(), null);
    }

    private Calendar shiftMonth(FacesContext facesContext,
                                Calendar timeKeeper, int currentDay, int shift)
    {
        Calendar cal = copyCalendar(facesContext, timeKeeper);

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + shift);

        if(currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            currentDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, currentDay);
        return cal;
    }

    private Calendar copyCalendar(FacesContext facesContext, Calendar timeKeeper)
    {
        Calendar cal = Calendar.getInstance(facesContext.getViewRoot().getLocale());
        cal.set(Calendar.YEAR, timeKeeper.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, timeKeeper.get(Calendar.MONTH));
        cal.set(Calendar.HOUR_OF_DAY, timeKeeper.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeKeeper.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, timeKeeper.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, timeKeeper.get(Calendar.MILLISECOND));
        return cal;
    }

    private void writeWeekDayNameHeader(int weekStartsAtDayIndex, String[] weekdays, FacesContext facesContext, ResponseWriter writer, UIInput inputComponent)
            throws IOException
    {
        for (int i = weekStartsAtDayIndex; i < weekdays.length; i++)
            writeCell(facesContext,
                    writer, inputComponent, weekdays[i], null, null);

        for (int i = 0; i < weekStartsAtDayIndex; i++)
            writeCell(facesContext, writer,
                    inputComponent, weekdays[i], null, null);
    }

    private void writeDays(FacesContext facesContext, ResponseWriter writer,
                           HtmlInputCalendar inputComponent, Calendar timeKeeper, int currentDay, int weekStartsAtDayIndex,
                           int weekDayOfFirstDayOfMonth, int lastDayInMonth, String[] weekdays)
            throws IOException
    {
        Calendar cal;

        int space = (weekStartsAtDayIndex < weekDayOfFirstDayOfMonth) ? (weekDayOfFirstDayOfMonth - weekStartsAtDayIndex)
                : (weekdays.length - weekStartsAtDayIndex + weekDayOfFirstDayOfMonth);

        if (space == weekdays.length)
            space = 0;

        int columnIndexCounter = 0;

        for (int i = 0; i < space; i++)
        {
            if (columnIndexCounter == 0)
            {
                writer.startElement(HTML.TR_ELEM, inputComponent);
            }

            writeCell(facesContext, writer, inputComponent, "",
                    null, inputComponent.getDayCellClass());
            columnIndexCounter++;
        }

        for (int i = 0; i < lastDayInMonth; i++)
        {
            if (columnIndexCounter == 0)
            {
                writer.startElement(HTML.TR_ELEM, inputComponent);
            }

            cal = copyCalendar(facesContext, timeKeeper);
            cal.set(Calendar.DAY_OF_MONTH, i + 1);

            String cellStyle = inputComponent.getDayCellClass();

            if((currentDay - 1) == i)
                cellStyle = inputComponent.getCurrentDayCellClass();

            writeCell(facesContext, writer,
                    inputComponent, String.valueOf(i + 1), cal.getTime(),
                    cellStyle);

            columnIndexCounter++;

            if (columnIndexCounter == weekdays.length)
            {
                writer.endElement(HTML.TR_ELEM);
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                columnIndexCounter = 0;
            }
        }

        if (columnIndexCounter != 0)
        {
            for (int i = columnIndexCounter; i < weekdays.length; i++)
            {
                writeCell(facesContext, writer,
                        inputComponent, "", null, inputComponent.getDayCellClass());
            }

            writer.endElement(HTML.TR_ELEM);
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        }
    }

    private void writeCell(FacesContext facesContext,
                           ResponseWriter writer, UIInput component, String content,
                           Date valueForLink, String styleClass)
            throws IOException
    {
        writer.startElement(HTML.TD_ELEM, component);

        if (styleClass != null)
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        if (valueForLink == null)
            writer.writeText(content, JSFAttr.VALUE_ATTR);
        else
        {
            writeLink(content, component, facesContext, valueForLink);
        }

        writer.endElement(HTML.TD_ELEM);
    }

    private void writeLink(String content,
                           UIInput component,
                           FacesContext facesContext,
                           Date valueForLink)
            throws IOException
    {
        Converter converter = getConverter(component);

        Application application = facesContext.getApplication();
        HtmlCommandLink link
                = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(component.getId() + "_" + valueForLink.getTime() + "_link");
        link.setTransient(true);
        link.setImmediate(component.isImmediate());

        HtmlOutputText text
                = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        text.setValue(content);
        text.setId(component.getId() + "_" + valueForLink.getTime() + "_text");
        text.setTransient(true);

        UIParameter parameter
                = (UIParameter)application.createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(component.getId() + "_" + valueForLink.getTime() + "_param");
        parameter.setTransient(true);
        parameter.setName(component.getClientId(facesContext));
        parameter.setValue(converter.getAsString(facesContext, component, valueForLink));

        component.getChildren().add(link);
        link.getChildren().add(parameter);
        link.getChildren().add(text);

        RendererUtils.renderChild(facesContext, link);
    }

    private Converter getConverter(UIInput component)
    {
        Converter converter = component.getConverter();

        if (converter == null)
        {
            converter = new CalendarDateTimeConverter();
        }
        return converter;
    }

    private int mapCalendarDayToCommonDay(int day)
    {
        switch (day)
        {
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }

    private static String[] mapWeekdays(DateFormatSymbols symbols)
    {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getShortWeekdays();

        weekdays[0] = localeWeekdays[Calendar.MONDAY];
        weekdays[1] = localeWeekdays[Calendar.TUESDAY];
        weekdays[2] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[3] = localeWeekdays[Calendar.THURSDAY];
        weekdays[4] = localeWeekdays[Calendar.FRIDAY];
        weekdays[5] = localeWeekdays[Calendar.SATURDAY];
        weekdays[6] = localeWeekdays[Calendar.SUNDAY];

        return weekdays;
    }

    private static String[] mapWeekdaysStartingWithSunday(DateFormatSymbols symbols)
    {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getShortWeekdays();

        weekdays[0] = localeWeekdays[Calendar.SUNDAY];
        weekdays[1] = localeWeekdays[Calendar.MONDAY];
        weekdays[2] = localeWeekdays[Calendar.TUESDAY];
        weekdays[3] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[4] = localeWeekdays[Calendar.THURSDAY];
        weekdays[5] = localeWeekdays[Calendar.FRIDAY];
        weekdays[6] = localeWeekdays[Calendar.SATURDAY];

        return weekdays;
    }

    public static String[] mapMonths(DateFormatSymbols symbols)
    {
        String[] months = new String[12];

        String[] localeMonths = symbols.getMonths();

        months[0] = localeMonths[Calendar.JANUARY];
        months[1] = localeMonths[Calendar.FEBRUARY];
        months[2] = localeMonths[Calendar.MARCH];
        months[3] = localeMonths[Calendar.APRIL];
        months[4] = localeMonths[Calendar.MAY];
        months[5] = localeMonths[Calendar.JUNE];
        months[6] = localeMonths[Calendar.JULY];
        months[7] = localeMonths[Calendar.AUGUST];
        months[8] = localeMonths[Calendar.SEPTEMBER];
        months[9] = localeMonths[Calendar.OCTOBER];
        months[10] = localeMonths[Calendar.NOVEMBER];
        months[11] = localeMonths[Calendar.DECEMBER];

        return months;
    }


    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlInputCalendar.class);

        HtmlRendererUtils.decodeUIInput(facesContext, component);
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlInputCalendar.class);

        UIInput uiInput = (UIInput) uiComponent;

        Converter converter = uiInput.getConverter();

        if(converter==null)
            converter = new CalendarDateTimeConverter();

        if (!(submittedValue == null || submittedValue instanceof String))
        {
            throw new IllegalArgumentException("Submitted value of type String expected");
        }

        return converter.getAsObject(facesContext, uiComponent, (String) submittedValue);
    }

    public static class CalendarDateTimeConverter implements Converter
    {

        public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
        {
            if(s==null || s.trim().length()==0)
                return null;

            DateFormat dateFormat = null;

            if(uiComponent instanceof HtmlInputCalendar && ((HtmlInputCalendar) uiComponent).isRenderAsPopup())
            {
                String popupDateFormat = ((HtmlInputCalendar) uiComponent).getPopupDateFormat();

                dateFormat = new SimpleDateFormat(createJSPopupFormat(facesContext, popupDateFormat));
            }
            else
            {
                dateFormat = createStandardDateFormat(facesContext);
            }

            try
            {
                return dateFormat.parse(s);
            }
            catch (ParseException e)
            {
                ConverterException ex = new ConverterException(e);
                throw ex;
            }
        }

        public static String createJSPopupFormat(FacesContext facesContext, String popupDateFormat)
        {

            if(popupDateFormat == null)
            {
                SimpleDateFormat defaultDateFormat = createStandardDateFormat(facesContext);
                popupDateFormat = defaultDateFormat.toPattern();
            }

            StringBuffer jsPopupDateFormat = new StringBuffer();

            for(int i=0;i<popupDateFormat.length();i++)
            {
                char c = popupDateFormat.charAt(i);

                if(c=='M')
                    jsPopupDateFormat.append('M');
                else if(c=='d')
                    jsPopupDateFormat.append('d');
                else if(c=='y')
                    jsPopupDateFormat.append('y');
                else if(c==' ')
                    jsPopupDateFormat.append(' ');
                else if(c=='.')
                    jsPopupDateFormat.append('.');
                else if(c=='/')
                    jsPopupDateFormat.append('/');
            }
            return jsPopupDateFormat.toString().trim();
        }

        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
        {
            Date date = (Date) o;

            if(date==null)
                return null;

            DateFormat dateFormat = null;

            if(uiComponent instanceof HtmlInputCalendar && ((HtmlInputCalendar) uiComponent).isRenderAsPopup())
            {
                String popupDateFormat = ((HtmlInputCalendar) uiComponent).getPopupDateFormat();

                dateFormat = new SimpleDateFormat(createJSPopupFormat(facesContext, popupDateFormat));
            }
            else
            {
                dateFormat = createStandardDateFormat(facesContext);
            }

            return dateFormat.format(date);
        }

        private static SimpleDateFormat createStandardDateFormat(FacesContext facesContext)
        {
            DateFormat dateFormat;
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                    facesContext.getViewRoot().getLocale());

            if(dateFormat instanceof SimpleDateFormat)
                return (SimpleDateFormat) dateFormat;
            else
                return new SimpleDateFormat("dd.MM.yyyy");
        }

    }
}
