package org.apache.myfaces.examples.toggle;

import java.io.Serializable;

public class ToggleBean implements Serializable
{
	private String _testValue = "default";
	
	public String getTestValue()
	{
		return _testValue;
	}
	
	public void setTestValue(String val)
	{
		_testValue = val;
	}
}
