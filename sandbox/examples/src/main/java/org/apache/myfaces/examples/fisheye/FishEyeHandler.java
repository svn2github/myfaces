/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.examples.fisheye;

import java.io.Serializable;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;

/**
 * Handler for the FishEye example
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FishEyeHandler implements Serializable
{
    private String _actionName;

    public FishEyeHandler()
    {
        this._actionName = "please click on a menu item";
    }

    public String getActionName()
    {
        return _actionName;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException
    {   
        UINavigationMenuItem comp = (UINavigationMenuItem) event.getComponent(); 
        this._actionName = comp.getItemLabel() + " item was clicked";
        
    }
}