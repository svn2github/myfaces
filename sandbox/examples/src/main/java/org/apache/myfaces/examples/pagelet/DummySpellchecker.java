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
