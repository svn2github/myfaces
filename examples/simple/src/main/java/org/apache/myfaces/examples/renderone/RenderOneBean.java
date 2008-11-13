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
package org.apache.myfaces.examples.renderone;

import java.io.Serializable;

/**
 * @author Andrew Robinson (latest modification by $Author: skitching $)
 * @version $Revision: 676967 $ $Date: 2008-07-15 18:57:23 +0200 (Tue, 15 Jul 2008) $
 */
public class RenderOneBean implements Serializable
{
    private boolean aRendered = false;
    private boolean cRendered = true;
    private boolean dRendered = true;
    private boolean bRendered = true;
    
    private Integer index = new Integer("2");
    
    /**
     * @return the index
     */
    public Integer getIndex()
    {
        return this.index;
    }

    /**
     * @return the aRendered
     */
    public boolean isARendered()
    {
        return this.aRendered;
    }

    /**
     * @param rendered the aRendered to set
     */
    public void setARendered(boolean rendered)
    {
        this.aRendered = rendered;
    }

    /**
     * @return the cRendered
     */
    public boolean isCRendered()
    {
        return this.cRendered;
    }

    /**
     * @param rendered the cRendered to set
     */
    public void setCRendered(boolean rendered)
    {
        this.cRendered = rendered;
    }

    /**
     * @return the dRendered
     */
    public boolean isDRendered()
    {
        return this.dRendered;
    }

    /**
     * @param rendered the dRendered to set
     */
    public void setDRendered(boolean rendered)
    {
        this.dRendered = rendered;
    }

    /**
     * @return the bRendered
     */
    public boolean isBRendered()
    {
        return this.bRendered;
    }

    /**
     * @param rendered the bRendered to set
     */
    public void setBRendered(boolean rendered)
    {
        this.bRendered = rendered;
    }
}
