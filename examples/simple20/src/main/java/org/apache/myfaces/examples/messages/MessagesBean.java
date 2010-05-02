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
package org.apache.myfaces.examples.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class MessagesBean
{

    public void printMessages(ActionEvent e)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        facesContext.addMessage(null, new FacesMessage("[1 Global Summary Only]"));
        facesContext.addMessage(null, new FacesMessage("[2 Global Summary]","[Global Detail]"));
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "[3 Global Summary]","[3 Global Detail]"));
        
        facesContext.addMessage("mainForm:input1", new FacesMessage("[4 input1 Summary Only]"));
        facesContext.addMessage("mainForm:input1", new FacesMessage("[5 input1 Summary]","[5 input1 Detail]"));
        facesContext.addMessage("mainForm:input2", new FacesMessage(FacesMessage.SEVERITY_FATAL, "[6 input1 Summary]","[6 input1 Detail]"));
    }
}
