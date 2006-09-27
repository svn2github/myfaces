package org.apache.myfaces.examples.ppr;

import javax.faces.event.ValueChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 20.08.2006
 * Time: 18:44:10
 * To change this template use File | Settings | File Templates.
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
