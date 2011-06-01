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
package org.apache.myfaces.custom.roundedPanel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.myfaces.shared_tomahawk.util.WebConfigParamUtils;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class RoundedPanelPropertiesBuilder
{
    public static final String ROUNDED_PANEL_PATTERN_BASIC = "b";
    
    public static final String SECTION_TOP_LEFT = "tl";
    public static final String SECTION_TOP_RIGHT = "tr";
    public static final String SECTION_BOTTOM_LEFT = "bl";
    public static final String SECTION_BOTTOM_RIGHT = "br";
    
    public boolean isValidResourceName(FacesContext context, String resourceName)
    {
        String roundedPanelPattern = null;
        String imagePart = null;
        String roundedPanelPropertiesText = null;
        int index = resourceName.indexOf('_');
        if (index < 0)
        {
            return false;
        }
        if (!resourceName.endsWith(".png"))
        {
            return false;
        }
        roundedPanelPattern = resourceName.substring(0,index);
        
        //Check valid rounded panel pattern
        if (!ROUNDED_PANEL_PATTERN_BASIC.equals(roundedPanelPattern))
        {
            return false;
        }
        
        int index2 = resourceName.indexOf('_',index+1);
        if (index2 < 0)
        {
            return false;
        }
        imagePart = resourceName.substring(index+1, index2);
        
        //Check valid image part
        if (ROUNDED_PANEL_PATTERN_BASIC.equals(roundedPanelPattern))
        {
            if (!SECTION_TOP_RIGHT.equals(imagePart) &&
                !SECTION_TOP_LEFT.equals(imagePart) &&
                !SECTION_BOTTOM_RIGHT.equals(imagePart) &&
                !SECTION_BOTTOM_LEFT.equals(imagePart))
            {
                return false;
            }
        }
        
        int index3 = resourceName.indexOf('.', index2+1);
        if (index3 < 0)
        {
            return false;
        }
        roundedPanelPropertiesText = resourceName.substring(index2+1,index3);
        
        try
        {
            RoundedPanelProperties properties = new RoundedPanelProperties(roundedPanelPropertiesText);
            
            Integer maxRadius = WebConfigParamUtils.getIntegerInitParameter(context.getExternalContext(), 
                    HtmlRoundedPanelRenderer.INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS, 
                    HtmlRoundedPanelRenderer.INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS_DEFAULT);
            
            if (properties.getRadius() > maxRadius )
            {
                return false;
            }
            
            if (properties.getBackgroundColor() != null &&
                HtmlColorUtils.encodeColorProperty(properties.getBackgroundColor()) == null)
            {
                return false;
            }

            if (properties.getColor() != null &&
                HtmlColorUtils.encodeColorProperty(properties.getColor()) == null)
            {
                return false;
            }

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public String decodeRoundedPanelPattern(String resourceName)
    {
        return resourceName.substring(0, resourceName.indexOf('_'));
    }
    
    public String decodeImagePart(String resourceName)
    {
        int index = resourceName.indexOf('_');
        int index2 = resourceName.indexOf('_',index+1);
        return resourceName.substring(index+1, index2);
    }
    
    public RoundedPanelProperties decodeRoundedPanelProperties(FacesContext context, String resourceName)
    {
        if (isValidResourceName(context, resourceName))
        {
            String roundedPanelPattern = null;
            String imagePart = null;
            String roundedPanelPropertiesText = null;
            int index = resourceName.indexOf('_');
            roundedPanelPattern = resourceName.substring(0,index);
            int index2 = resourceName.indexOf('_',index+1);
            imagePart = resourceName.substring(index+1, index2);
            int index3 = resourceName.indexOf('.', index2+1);
            roundedPanelPropertiesText = resourceName.substring(index2+1,index3);
            return new RoundedPanelProperties(roundedPanelPropertiesText);
        }
        return null;
    }
    
    public void generateRoundedPanelImage(FacesContext facesContext,
                                          OutputStream out,
                                          String resourceName) throws IOException
    {
        generateRoundedPanelImage(facesContext, out, 
                decodeRoundedPanelProperties(facesContext, resourceName), 
                decodeRoundedPanelPattern(resourceName),
                decodeImagePart(resourceName));
    }
    
    public void generateRoundedPanelImage(FacesContext facesContext,
                                          OutputStream out,
                                          RoundedPanelProperties roundedPanelProperties, 
                                          String pattern, 
                                          String imagePart)
        throws IOException
    {
        if (ROUNDED_PANEL_PATTERN_BASIC.equals(pattern))
        {
            ImageIO.write(
                    generateBasicRoundedPanelImage(facesContext, 
                                                   roundedPanelProperties, 
                                                   pattern, 
                                                   imagePart), "png", out);
        }
    }
    
    public BufferedImage generateBasicRoundedPanelImage(
            FacesContext facesContext, 
            RoundedPanelProperties roundedPanelProperties,
            String pattern, 
            String imagePart)
    {
        // create the canvas image
        int w, h;
        
        w = roundedPanelProperties.getRadius();
        h = roundedPanelProperties.getRadius();
        
        BufferedImage canvas = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        Graphics2D g2 = null;

        try
        {
            // either paint the background of the image, or set it to transparent
            Color backgroundColor = null;
            if (roundedPanelProperties.getBackgroundColor() != null)
            {
                backgroundColor = Color.decode(roundedPanelProperties.getBackgroundColor());
            }
            
            paintBackground(g, w, h, backgroundColor);
   
            g2 = (Graphics2D) g;
            // TODO: Apply rendering hints with a property
            RenderingHints renderHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
              renderHints.put(RenderingHints.KEY_RENDERING,
                              RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(renderHints);

            g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            if (roundedPanelProperties.getColor() != null)
            {
                g.setColor(Color.decode(roundedPanelProperties.getColor()));
                g.setPaint(Color.decode(roundedPanelProperties.getColor()));
            }
            else
            {
                g.setColor(Color.BLACK);
                g.setColor(Color.BLACK);
            }
            
            int x=0;
            int y=0;
            
            if (SECTION_TOP_LEFT.equals(imagePart))
            {
                x=0;
                y=0;
            }
            else if (SECTION_TOP_RIGHT.equals(imagePart))
            {
                x=-roundedPanelProperties.getRadius()-2;
                y=0;
            }
            else if (SECTION_BOTTOM_LEFT.equals(imagePart))
            {
                x=0;
                y=-roundedPanelProperties.getRadius()-2;
            }
            else if (SECTION_BOTTOM_RIGHT.equals(imagePart))
            {
                x=-roundedPanelProperties.getRadius()-2;
                y=-roundedPanelProperties.getRadius()-2;
            }      

            Shape circle = new Ellipse2D.Float(x,y, (roundedPanelProperties.getRadius()*2)+1, (roundedPanelProperties.getRadius()*2)+1);
            
            //Shape circle = new RoundRectangle2D.Float(x, y,
            //        (float) (roundedPanelProperties.getRadius()*2)+1, (roundedPanelProperties.getRadius()*2)+1,
            //        (roundedPanelProperties.getRadius()*2)+1, (roundedPanelProperties.getRadius()*2)+1);
            
            g2.fill(circle);
            g2.draw(circle);
            
            return canvas;
        }
        finally
        {
            if (g2 != null)
                try
                {
                    g2.dispose();
                }
                catch (Exception ex)
                {
                }
            g.dispose();
        }
    }
    
    private void paintBackground(Graphics2D g, int width, int height, Color background)
    {
        // treat a null background as fully transparent
        if (background == null)
        {
            BufferedImage img = g.getDeviceConfiguration()
                    .createCompatibleImage(width, height,
                            Transparency.TRANSLUCENT);
            Graphics2D g2 = img.createGraphics();

            // Clear the image so all pixels have zero alpha
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);

            g2.dispose();
        }
        else
        {
            g.setColor(background);
            g.fillRect(0, 0, width, height);
        }
    }
}
