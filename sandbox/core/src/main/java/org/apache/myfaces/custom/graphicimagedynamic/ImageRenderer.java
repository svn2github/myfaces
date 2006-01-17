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
package org.apache.myfaces.custom.graphicimagedynamic;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;

/**
 * The ImageRenderer is used to render the binary data for a html img tag
 * Implementions must have a default constructor
 * 
 * @author Mathias Broekelmann
 * @version $Revision$ $Date$
 */
public interface ImageRenderer
{
    /**
     * The MimeType of the image. This is usally a value of 
     * image/jpeg, image/gif, image/png or anything else which can be 
     * used as an image for html
     *   
     * @return the Mime-Type, not null
     */
    String getContentType();

    /**
     * Called to render the image to the given outputstream
     * 
     * @param facesContext the faces context
     * @param imageContext the image context width aditional image parameters
     * @param out the outputstream which is used to write the binary data of the image
     *  
     * @throws IOException
     */
    void renderImage(FacesContext facesContext, ImageContext imageContext, OutputStream out) throws IOException;
}
