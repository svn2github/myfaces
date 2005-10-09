package org.apache.myfaces.component.html.util;

/**
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
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
