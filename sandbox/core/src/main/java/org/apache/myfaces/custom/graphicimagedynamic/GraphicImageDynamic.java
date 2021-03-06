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

package org.apache.myfaces.custom.graphicimagedynamic;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.custom.graphicimagedynamic.util.ImageRenderer;
import org.apache.myfaces.shared_tomahawk.util.ClassUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.ValueBinding;

/**
 * Extends standard graphicImage.
 * <p>
 * This tag renders a html img tag and can be used to render dynamic images.                
 * </p>
 * <p>
 * Embedding images into html pages requires a second request to get the binary data 
 * stream of the image. The result is that the state of the view including the state of request
 * scoped beans will not be available when the image is requested.
 * </p>
 * <p>
 * The image data is written by an image renderer which can be defined by the 
 * imageRendererClass attribute.
 * </p>
 * <p>
 * This component is able to use nested f:param elements to pass parameters to the image renderer.              
 * </p>
 * 
 * @JSFComponent
 *   name = "s:graphicImageDynamic"
 *   tagClass = "org.apache.myfaces.custom.graphicimagedynamic.GraphicImageDynamicTag"
 *   
 * @author Sylvain Vieujot (latest modification by $Author$)
 *
 * @version $Revision$ $Date: 2005-05-11 19:57:24 +0200 (Wed, 11 May 2005) $
 * 
 * Warning, this component is far from ready.
 * It's more a proof of concept right now.
 * TODO : Remove the need to include .get for the last part of the method expressions : getBytesMethod="#{graphicImageDynamicBean.upImage.getBytes}"
 * TODO : Make a similar download component to download files 
 * TODO : Use shorter URLs
 */

public class GraphicImageDynamic extends HtmlGraphicImage
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.GraphicImageDynamic";
    public static final String COMPONENT_FAMILY = "javax.faces.Graphic";
    public static final String RENDERER_PARAM = "_renderer";
    public static final String VALUE_PARAM = "_value";
    public static final String WIDTH_PARAM = "_width";
    public static final String HEIGHT_PARAM = "_height";    
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.GraphicImageDynamicRenderer";    
    

    public GraphicImageDynamic()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    private Class _imageRendererClass;

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _imageRendererClass;
        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _imageRendererClass = (Class) values[1];
    }

    /**
     * The class which implements 
     * org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer. 
     * The image renderer is responsible for loading the image. 
     * The class must have a default constructor. 
     * Any request scoped attribute or managed bean is not available 
     * when this image renderer is instantiated and used. 
     * The image renderer must render the binary data for the image by 
     * using the parameters passed by nested f:param elements and/or 
     * using session or application scoped beans.
     * 
     * @JSFProperty
     */
    public void setImageRendererClass(Class imageRendererClass)
    {
        if (imageRendererClass != null && !ImageRenderer.class.isAssignableFrom(imageRendererClass))
        {
            throw new IllegalArgumentException(
                    "imageRendererClass must be null or a class which implements "
                            + ImageRenderer.class.getName());
        }
        _imageRendererClass = imageRendererClass;
    }

    public Class getImageRendererClass()
    {
        if (_imageRendererClass != null)
        {
            return _imageRendererClass;
        }
        ValueBinding vb = getValueBinding("imageRendererClass");
        if (vb != null)
        {
            Object value = vb.getValue(getFacesContext());
            if (value == null)
            {
                return null;
            }
            Class clazz;
            if (value instanceof Class)
            {
                clazz = (Class) value;
            }
            else
            {
                try
                {
                    clazz = ClassUtils.classForName(value.toString());
                }
                catch (ClassNotFoundException e)
                {
                    throw new EvaluationException("Could not load imageRendererClass for "
                            + vb.getExpressionString(), e);
                }
            }
            if (!ImageRenderer.class.isAssignableFrom(clazz))
            {
                throw new EvaluationException("Expected value for " + vb.getExpressionString()
                        + " must be one of null, a fully qualified class name or "
                        + "an instance of a class which implements "
                        + ImageRenderer.class.getName());
            }
            return clazz;
        }
        return null;
    }

    /**
     *  A value binding which will be called to get the instance of an 
     *  org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer.
     * 
     * @JSFProperty
     */
    public Object getValue()
    {
        return super.getValue();
    }
}
