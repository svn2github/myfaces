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
package org.apache.myfaces.custom.security;

/**
 * 
 * @author cagatay
 */
public abstract class SecurityContext {

	public final static int AUTH_MODE_NONE = -1;
	public final static int AUTH_MODE_SINGLE = 0;
	public final static int AUTH_MODE_ALL = 1;
	public final static int AUTH_MODE_ANY = 2;
	public final static int AUTH_MODE_NOT = 3;
	
	private String[] roles;
	private int authMode = AUTH_MODE_NONE;
	
	public abstract String getAuthType();	
	
	public abstract String getRemoteUser();
	
	public abstract boolean ifGranted(String role);
	
	public boolean ifSingleGranted() {
		return ifGranted(roles[0]);
	}
	
	public boolean ifAllGranted() {
		boolean isAuthorized = false;
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			if(ifGranted(role)) {
				isAuthorized = true;
			} else {
				isAuthorized = false;
				break;
			}
		}
		return isAuthorized;
	}
	
	public boolean ifAnyGranted() {
		boolean isAuthorized = false;
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			if(ifGranted(role)) {
				isAuthorized = true;
				break;
			}
		}
		return isAuthorized;
	}
	
	public boolean ifNotGranted() {
		boolean isAuthorized = false;
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			if(ifGranted(role)) {
				isAuthorized = false;
				break;
			} else {
				isAuthorized = true;
			}
		}
		return isAuthorized;
	}
	
	public boolean inAuthMode() {
		return authMode != AUTH_MODE_NONE;
	}
	
	public int getAuthMode() {
		return authMode;
	}
	public void setAuthMode(int authMode) {
		this.authMode = authMode;
	}

	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
}
