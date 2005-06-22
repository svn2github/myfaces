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
package org.apache.myfaces.custom.jsvalueset;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlJsValueSetRenderer
        extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlJsValueSet.class);

        HtmlJsValueSet htmlJsValueSet = (HtmlJsValueSet) component;

        Object value = htmlJsValueSet.getValue();
        String name = htmlJsValueSet.getName();


        ResponseWriter writer = getFacesContext().getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM,null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);

        if(value instanceof String ||
                value instanceof Number || value instanceof Boolean || value == null)
        {
            writer.writeText("var " + name+"="+getValueString(value)+";",null);
        }
        else if(value instanceof Object[])
        {
            Object[] objs = (Object[]) value;

            startArray(writer, name);

            for (int i = 0; i < objs.length; i++)
            {
                Object obj = objs[i];
                writeArrayElement(writer, name, i, obj);
            }
        }
        else if(value instanceof Collection)
        {
            Collection coll = (Collection) value;

            startArray(writer, name);

            int i=0;

            for (Iterator iterator = coll.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                writeArrayElement(writer, name, i, obj);
                i++;
            }
        }
        else if(value instanceof Map)
        {
            Map map = (Map) value;
            Iterator it = map.entrySet().iterator();

            startArray(writer,name);

            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                writeArrayElement(writer, name, entry.getKey(), entry.getValue());
            }
        }
        else
            throw new IOException("Type of value not handled. Allowed are String, Number, Boolean, Object[], Collection, Map. Type of value : "+value.getClass());


        writer.endElement(HTML.SCRIPT_ELEM);
    }

    private void writeArrayElement(ResponseWriter writer, String name, Object key, Object obj)
        throws IOException
    {
        String prefix = name+"["+getValueString(key)+"]";

        if(obj instanceof Map)
        {
            writer.writeText(prefix + "= new Array();",null);

            Iterator it = ((Map) obj).entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();

                writeArrayElement(writer,prefix,entry.getKey(),entry.getValue());
            }
        }
        else
        {
            writer.writeText(prefix + "="+getValueString(obj)+";",null);
        }
    }

    private void writeArrayElement(ResponseWriter writer, String name, int i, Object obj)
            throws IOException
    {
        writer.writeText(name+"["+i+"]="+getValueString(obj)+";",null);
    }

    private void startArray(ResponseWriter writer, String name)
            throws IOException
    {
        writer.writeText("var " + name+"=new Array();",null);
    }

    private String getValueString(Object value) throws IOException
    {
        if(value instanceof String)
        {
            return "'"+value+"'";
        }
        else if(value instanceof Number)
        {
            return value.toString();
        }
        else if(value instanceof Boolean)
        {
            return value.toString();
        }
        else if(value == null)
        {
            return "null";
        }
        else
        {
            throw new IOException("value : "+value+" is of type : "+value.getClass()+
                    ", provide a method to convert this.");
        }
    }

    protected Application getApplication()
    {
        return getFacesContext().getApplication();
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

}
