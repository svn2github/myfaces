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
package org.apache.myfaces.examples.pagelet;

import java.util.Iterator;

import org.apache.myfaces.custom.pagelet.Text;
import org.apache.myfaces.custom.pagelet.Word;

/**
 * Dummy spellchecker class
 * for demonstrating on how to
 * enable a spellchecker from outside
 * 
 * @author werpu
 *
 */
public class DummySpellchecker {
	public void checkSpelling(Text text) {
		if (text == null)
			return;
        for (Iterator it = text.getWords().iterator(); it.hasNext();)
        {
            Word word = (Word) it.next();
            if (word == null || word.getValue() == null || word.getValue().length() == 0)
                continue;
            if(word.getValue().equals("foobaz")) {
                word.setInvalid(true);
                String[] suggestions = {"fubar","snafu","myfaces"};
                word.setAlternatives(suggestions);
            }
            if(word.getValue().equals("exemple")) {
                word.setInvalid(true);
                String[] suggestions = {"example","easter egg","myfaces"};
                word.setAlternatives(suggestions);
            }
        }
	}
}
