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
