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

package org.apache.myfaces.examples.ppr;

import javax.faces.event.ValueChangeEvent;

/**
 * @author Ernst Fastl
 */
public class PPRExampleBean
{
    private String _textField;
    
    private String _message;
    
    private Boolean _partialUpdateConfiguredButton;
    
    private Boolean _partialChangeCheckBox=Boolean.FALSE;
    
    private String _partialChangeDropDown;

    public String getTextField()
    {
        return _textField;
    }

    public void setTextField(String textField)
    {
        this._textField = textField;
    }

	public Boolean getPartialUpdateConfiguredButton() {
		return _partialUpdateConfiguredButton;
	}

	public void setPartialUpdateConfiguredButton(
			Boolean partialUpdateConfiguredButton) {
		this._partialUpdateConfiguredButton = partialUpdateConfiguredButton;
	}
    
	public String testAction() {
		setMessage("testAction called");
		return "test";
	}
	
	public void testValueChangeListener(ValueChangeEvent event){
		
		_message = "Value Change to: ";
		if(event.getNewValue()!=null)
		{
			_message += event.getNewValue().toString();
		}
	}

	public Boolean getPartialChangeCheckBox() {
		return _partialChangeCheckBox;
	}

	public void setPartialChangeCheckBox(Boolean changeCheckBox) {
		_partialChangeCheckBox = changeCheckBox;
	}

	public String getPartialChangeDropDown() {
		return _partialChangeDropDown;
	}

	public void setPartialChangeDropDown(String changeDropDown) {
		_partialChangeDropDown = changeDropDown;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		this._message = message;
	}
    
	
}
