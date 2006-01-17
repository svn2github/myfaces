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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.myfaces.custom.graphicimagedynamic.ImageContext;
import org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Mathias Broekelmann
 *
 */
public class UploadedImageRenderer implements ImageRenderer
{
    private final GraphicImageDynamicBean _graphicImageDynamicBean;

    public UploadedImageRenderer()
    {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ValueBinding vb = currentInstance.getApplication().createValueBinding(
                "#{graphicImageDynamicBean}");
        GraphicImageDynamicBean value = (GraphicImageDynamicBean) vb.getValue(currentInstance);
        if (value == null)
        {
            throw new IllegalStateException("managed bean graphicImageDynamicBean not found");
        }
        _graphicImageDynamicBean = value;
    }

    /**
     * @see org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer#getContentType()
     */
    public String getContentType()
    {
        return _graphicImageDynamicBean.isUploaded() ? _graphicImageDynamicBean.getUpImage()
                .getContentType() : null;
    }

    /**
     * @see org.apache.myfaces.custom.graphicimagedynamic.ImageRenderer#renderImage(javax.faces.context.FacesContext, org.apache.myfaces.custom.graphicimagedynamic.ImageContext, java.io.OutputStream)
     */
    public void renderImage(FacesContext facesContext, ImageContext imageContext, OutputStream out)
            throws IOException
    {
        if (_graphicImageDynamicBean.isUploaded())
        {
            InputStream is = _graphicImageDynamicBean.getUpImage().getInputStream();
            try
            {
                byte[] buffer = new byte[1024];
                int len = is.read(buffer);
                while (len != -1)
                {
                    out.write(buffer, 0, len);
                    len = is.read(buffer);
                }
            }
            finally
            {
                is.close();
            }
        }
    }
}
