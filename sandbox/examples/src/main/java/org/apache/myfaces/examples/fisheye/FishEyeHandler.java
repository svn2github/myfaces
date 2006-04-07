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

import javax.faces.event.ActionEvent;

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

    public void calendarClicked(ActionEvent event)
    {
        this._actionName = "Calendar item was clicked";
    }

    public void emailClicked(ActionEvent event)
    {
        this._actionName = "Email item was clicked";
    }

    public String getActionName()
    {
        return _actionName;
    }

    public void textEditorClicked(ActionEvent event)
    {
        this._actionName = "Text Editor item was clicked";
    }

    public void updateClicked(ActionEvent event)
    {
        this._actionName = "Software Update item was clicked";
    }

    public void usersClicked(ActionEvent event)
    {
        this._actionName = "Users item was clicked";
    }

    public void webBrowserClicked(ActionEvent event)
    {
        System.out.println("Web Browser clicked");
        this._actionName = "Web browser item was clicked";
    }
}
