/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.component;

/**
 * Behavioral interface.
 * Components that support user role checking should implement this interface
 * to optimize property access.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface UserRoleAware
{
    public static final String ENABLED_ON_USER_ROLE_ATTR = "enabledOnUserRole";
    public static final String VISIBLE_ON_USER_ROLE_ATTR = "visibleOnUserRole";

    public String getEnabledOnUserRole();
    public void setEnabledOnUserRole(String userRole);

    public String getVisibleOnUserRole();
    public void setVisibleOnUserRole(String userRole);
}
