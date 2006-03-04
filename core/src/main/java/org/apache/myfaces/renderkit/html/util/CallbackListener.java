/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.renderkit.html.util;

/**
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 *          <p/>
 *          $Log: $
 */
public interface CallbackListener
{
    void openedStartTag(int charIndex, int tagIdentifier);
    void closedStartTag(int charIndex, int tagIdentifier);
    void openedEndTag(int charIndex, int tagIdentifier);
    void closedEndTag(int charIndex, int tagIdentifier);
    void attribute(int charIndex, int tagIdentifier, String key, String value);
}
