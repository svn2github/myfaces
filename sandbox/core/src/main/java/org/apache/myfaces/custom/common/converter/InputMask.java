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

package org.apache.myfaces.custom.common.converter;

/**
 * interface to allow a converter to send code which converts a string to object and vice versa
 * on the client
 *
 * @author imario@apache.org
 */
public interface InputMask
{
    /**
     * A regular expression to check if a character is allowed. The expression will be used on the client.
     * <br />
     * Example:
     * <code>
     * Numerics only: [0-9]
     * Date: [0-9\\-]
     * E-Mail: [a-zA-Z0-9\\.@]
     * </code>
     * As you can see, this is not a full blown regular expression to check if the input is valid. It is
     * more a check to see if the "to be entered" character is allowed.<br />
     * The validation check itself will be done by the client converter or client validator.
     *
     * @return the regular expression
     */
    public String getInputPattern();

    /**
     * A human readable string about the allowed input
     * <br />
     * Example:
     * <code>
     * Numeric input only
     * Date in Format: DD-MM-YYYY
     * Email allows letters, digits, point and @ only
     * </code>
     *
     * @return the human readable string
     */
    public String getDescription();
}
