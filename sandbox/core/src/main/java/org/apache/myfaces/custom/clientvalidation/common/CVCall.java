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
package org.apache.myfaces.custom.clientvalidation.common;

import java.io.Serializable;

/**
 * Encapsulates information needed for client side validation/conversation.
 * Instances are added to CVCallsHolder object located at request scope.
 * 
 * @author cagatay (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CVCall implements Serializable{

	private String _id;
	private String _clientId;
	private boolean _required;
	private String _converterScript;
	private String[] _validatorScripts;
	
	public CVCall() {}
	
	public CVCall(String id, String clientId, boolean required) {
		this._id = id;
		this._clientId = clientId;
		this._required = required;
	}
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	
	public String getClientId() {
		return _clientId;
	}
	public void setClientId(String clientId) {
		this._clientId = clientId;
	}
	
	public String getConverterScript() {
		return _converterScript;
	}
	public void setConverterScript(String converterScript) {
		this._converterScript = converterScript;
	}
	
	public boolean isRequired() {
		return _required;
	}
	public void setRequired(boolean required) {
		this._required = required;
	}
	
	public String[] getValidatorScripts() {
		return _validatorScripts;
	}
	public void setValidatorScripts(String[] validatorScripts) {
		this._validatorScripts = validatorScripts;
	}
	
}

