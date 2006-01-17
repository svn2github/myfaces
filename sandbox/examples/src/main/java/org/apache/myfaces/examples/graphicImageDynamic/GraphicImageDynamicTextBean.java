/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.examples.graphicImageDynamic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.myfaces.custom.graphicimagedynamic.ImageContext;
import org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer;

import javax.faces.context.FacesContext;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Mathias Broekelmann
 *
 */
public class GraphicImageDynamicTextBean implements ImageRenderer
{
    private String _text;

    public String getText()
    {
        return _text;
    }

    public void setText(String text)
    {
        _text = text;
    }

    public Class getImageRenderer()
    {
        return this.getClass();
    }

    /**
     * @see org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer#getContentType()
     */
    public String getContentType()
    {
        return "image/jpeg";
    }

    /**
     * @see org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer#renderImage(javax.faces.context.FacesContext, org.apache.myfaces.custom.graphicimagedynamic.ImageContext, java.io.OutputStream)
     */
    public void renderImage(FacesContext facesContext, ImageContext imageContext, OutputStream out)
            throws IOException
    {
        Object text = imageContext.getParamters().get("text");
        if (text == null)
        {
            text = "";
        }
        int width = 300;
        int height = 30;
        Integer widthObj = imageContext.getWidth();
        if (widthObj != null)
        {
            width = widthObj.intValue();
        }
        Integer heightObj = imageContext.getHeight();
        if (heightObj != null)
        {
            height = heightObj.intValue();
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = img.getGraphics();
        try
        {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLUE);
            graphics.drawString(text.toString(), 10, 20);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(img);
        }
        finally
        {
            graphics.dispose();
        }
    }

}
