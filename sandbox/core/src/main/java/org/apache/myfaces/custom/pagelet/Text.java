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
package org.apache.myfaces.custom.pagelet;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Thomas Spiegl
 */
public class Text {

    private List _words;

    Text(List words) {
        _words = words;
    }

    Text(Word word) {
        _words = new ArrayList(1);
        _words.add(word);
    }

    /**
     * @return {@link Word}
     */
    public Collection getWords() {
        List list = new ArrayList();
        for (int i = 0; i < _words.size(); i++)
        {
            Word word = (Word) _words.get(i);
            if (word.checkSpelling)
            {
                list.add(word);
            }
        }
        return list;
    }
}
