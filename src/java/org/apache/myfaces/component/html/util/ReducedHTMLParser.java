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
package org.apache.myfaces.component.html.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class which detects the open/close tags in an HTML document and reports
 * them to a listener class. 
 * <p>
 * This is unfortunately necessary when using JSF with JSP, as tags in the body
 * of the document can need to output commands into the document at points
 * earlier than the tag occurred (particularly into the document HEAD section).
 * This can only be implemented by buffering the response and post-processing
 * it to find the relevant HTML tags and modifying the buffer as needed.
 * 
 * @version $Revision$ $Date$
 */
public class ReducedHTMLParser
{
    // IMPLEMENTATION NOTE:
    //
    // Many of the methods on this class are package-scope. This is intended
    // solely for the purpose of unit-testing. This class does not expect
    // other classes in this package to access its methods.

    private static final Log log = LogFactory.getLog(ReducedHTMLParser.class);

    public static final int BODY_TAG = 0;
    public static final int HEAD_TAG = 1;
    public static final int SCRIPT_TAG = 2;

    private static final int STATE_READY = 0;
    private static final int STATE_IN_COMMENT = 1;
    private static final int STATE_IN_TAG = 2;
    
    private int offset;
    private int lineNumber;
    private CharSequence seq;
    private CallbackListener listener;
    
    public static void parse(CharSequence seq, CallbackListener l)
    {
        new ReducedHTMLParser(seq, l).parse();
    }
    
    /**
     * Constructor, package-scope for unit testing.
     * 
     * @param s is the sequence of chars to parse.
     * @param l is the listener to invoke callbacks on.
     */
    ReducedHTMLParser(CharSequence s, CallbackListener l) {
        seq = s;
        listener = l;
    }

    /**
     * Return true if there are no more characters to parse.
     */
    boolean isFinished() {
        return offset >= seq.length();
    }

    int getCurrentLineNumber() {
         return lineNumber;
    }

    /**
     * Advance the current parse position over any whitespace characters.
     */
    void consumeWhitespace() {
        boolean crSeen = false;

        while (offset < seq.length()) {
            char c = seq.charAt(offset);
            if (!Character.isWhitespace(c)) {
                break;
            }

            // Track line number for error messages.
            if (c == '\r') {
                ++lineNumber;
                crSeen = true;
            } else if ((c == '\n') && !crSeen) {
                ++lineNumber;
            } else {
                crSeen = false;
            }

            ++offset;
        }
    }

    /**
     * Eat up a sequence of non-whitespace characters and return them.
     */
    String consumeNonWhitespace() {
        int wordStart = offset;
        while (offset < seq.length()) {
            char c = seq.charAt(offset);
            if (Character.isWhitespace(c)) {
                break;
            }
            ++offset;
        }
        if (wordStart == offset) {
            return null;
        } else {
            return seq.subSequence(wordStart, offset).toString();
        }
    }

    /**
     * If the next chars in the input sequence exactly match the specified
     * string then skip over them and return true.
     * <p>
     * If there is not a match then leave the current parse position 
     * unchanged and return false.
     * 
     * @param s is the exact string to match.
     * @return true if the input contains exactly the param s
     */
    boolean consumeMatch(String s) {
        if (offset + s.length() > seq.length()) {
            // seq isn't long enough to contain the specified string
            return false;
        }

        int i = 0;
        while (i < s.length()) {
            if (seq.charAt(offset+i) == s.charAt(i)) {
                ++i;
            } else {
                return false;
            }
        }
        
        offset += i;
        return true;
    }

    /**
     * Eat up a sequence of chars which form a valid XML element name.
     * <p>
     * TODO: implement this properly in compliance with spec
     */
    String consumeElementName() {
        consumeWhitespace();
        int nameStart = offset;
        while (!isFinished()) {
            boolean ok = false;
            char c = seq.charAt(offset);
            if (Character.isLetterOrDigit(seq.charAt(offset))) {
                ok = true;
            } else if (c == '_') {
                ok = true;
            } else if (c == '-') {
                ok = true;
            } else if (c == ':') {
                ok = true;
            }
            
            if (!ok) {
                break;
            }

            ++offset;
        }
        
        if (nameStart == offset) {
            return null;
        } else {
            return seq.subSequence(nameStart, offset).toString();
        }
    }

    /**
     * Eat up a sequence of chars which form a valid XML attribute name. 
     * <p>
     * TODO: implement this properly in compliance with spec
     */
    String consumeAttrName() {
        // for now, assume elements and attributes have same rules
        return consumeElementName();
    }

    /**
     * Eat up a string which is terminated with the specified quote
     * character. This means handling escaped quote chars within the
     * string.
     * <p>
     * This method assumes that the leading quote has already been
     * consumed.
     */
    String consumeString(char quote) {
        // TODO: should we consider a string to be terminated by a newline?
        // that would help with runaway strings but I think that multiline
        // strings *are* allowed...

         //
         // TODO: detect newlines within strings and increment lineNumber.
         // This isn't so important, though; they aren't common and being a
         // few lines out in an error message isn't serious either.
        StringBuffer stringBuf = new StringBuffer();
        boolean escaping = false;
        while (!isFinished()) {
            char c = seq.charAt(offset);
            ++offset;
            if (c == quote) {
                if (!escaping) {
                    break;
                } else {
                    stringBuf.append(c);
                    escaping = false;
                }
            } else if (c == '\\') {
                if (escaping) {
                    // append a real backslash
                    stringBuf.append(c);
                    escaping = false;
                } else {
                    escaping = true;
                }
            } else {
                stringBuf.append(c);
            }
        }
        return stringBuf.toString();
    }

    /**
     * Assuming we have already encountered "attrname=", consume the
     * value part of the attribute definition. Note that unlike XML,
     * HTML doesn't have to quote its attribute values.
     * 
     * @return the attribute value. If the attr-value was quoted, 
     * the returned value will not include the quote chars.
     */
    String consumeAttrValue() {
        consumeWhitespace();
        char singleQuote = '\'';
        
        if (consumeMatch("'")) {
            return consumeString('\'');
        } else if (consumeMatch("\"")) {
            return consumeString('"');
        } else {
            return consumeNonWhitespace();
        }
    }

    /**
     * Discard all characters in the input until one in the specified
     * string (character-set) is found.
     * 
     * @param s is a set of characters that should not be discarded.
     */
    void consumeExcept(String s) {
         boolean crSeen = false;

        while (offset < seq.length()) {
            char c = seq.charAt(offset);
            if (s.indexOf(c) >= 0) {
                // char is in the exception set
                return;
            }

             // Track line number for error messages.
             if (c == '\r') {
                 ++lineNumber;
                 crSeen = true;
             } else if ((c == '\n') && !crSeen) {
                 ++lineNumber;
             } else {
                 crSeen = false;
             }
            
            ++offset;
        }
    }

    /**
     * Process the entire input buffer, invoking callbacks on the listener
     * object as appropriate.
     */
    void parse() {
        int state = STATE_READY;
        
        int currentTagStart = -1;
        String currentTagName = null;

        lineNumber = 1;
        offset = 0;
        while (offset < seq.length())
        {
            if (state == STATE_READY) {
                // in this state, nothing but "<" has any significance
                consumeExcept("<");
                if (isFinished()) {
                    break;
                }

                if (consumeMatch("<!--")) {
                    // VERIFY: can "< ! --" start a comment?
                    state = STATE_IN_COMMENT;
                 } else if (consumeMatch("<!")) {
                     // xml processing instruction or <!DOCTYPE> tag
                     // we don't need to actually do anything here
                     log.debug("PI found at line " + getCurrentLineNumber());
                } else if (consumeMatch("</")) {
                    // VERIFY: is "< / foo >" a valid end-tag?

                    int tagStart = offset - 2;
                    String tagName = consumeElementName();
                    consumeWhitespace();
                    if (!consumeMatch(">")) {
                        throw new Error("Malformed end tag");
                    }

                    // We can't verify that the tag names balance because this is HTML
                    // we are processing, not XML.

                    // stay in state READY
                    state = STATE_READY;

                    // inform user that the tag has been closed
                    closedTag(tagStart, offset, tagName);
                } else if (consumeMatch("<")) {
                    // We can't tell the user that the tag has closed until after we have
                    // processed any attributes and found the real end of the tag. So save
                    // the current info until the end of this tag.
                    currentTagStart = offset - 1;
                    currentTagName = consumeElementName();
                    if (currentTagName == null) {
                        log.warn("Invalid HTML; bare lessthan sign found at line "
                            + getCurrentLineNumber());
                        // remain in STATE_READY; this isn't really the start of
                        // an xml element.
                    } else {
                        state = STATE_IN_TAG;
                    }
                } else {
                    // should never get here
                    throw new Error("Internal error at line " + getCurrentLineNumber());
                }
                
                continue;
            }

            if (state == STATE_IN_COMMENT) {
                // VERIFY: does "-- >" close a comment?

                // in this state, nothing but "-->" has any significance
                consumeExcept("-");
                if (isFinished()) {
                    break;
                }

                if (consumeMatch("-->")) {
                    state = STATE_READY;
                } else  {
                    // false call; hyphen is not end of comment
                    consumeMatch("-");
                }
                
                continue;
            }
            
            if (state == STATE_IN_TAG) {
                consumeWhitespace();
                
                if (consumeMatch("/>")) {
                    // ok, end of element
                    state = STATE_READY;
                    closedTag(currentTagStart, offset, currentTagName);
                    
                    // and reset vars just in case...
                    currentTagStart = -1;
                    currentTagName = null;
                } else if (consumeMatch(">")) {
                    // end of open tag, but not end of element
                    state = STATE_READY;
                    openedTag(currentTagStart, offset, currentTagName);
                    
                    // and reset vars just in case...
                    currentTagStart = -1;
                    currentTagName = null;
                } else {
                    // xml attribute
                    String attrName = consumeAttrName();
                    consumeWhitespace();
                    
                    // html can have "stand-alone" attributes with no following equals sign
                    if (consumeMatch("=")) {
                        String attrValue = consumeAttrValue();
                    }
                }
                
                continue;
            }
        }
    }

    /**
     * Invoke a callback method to inform the listener that we have found a start tag.
     * 
     * @param startOffset
     * @param endOffset
     * @param tagName
     */
    void openedTag(int startOffset, int endOffset, String tagName) {
        //log.debug("Found open tag at " + startOffset + ":" + endOffset + ":" + tagName);

        if ("head".equalsIgnoreCase(tagName)) {
            listener.openedStartTag(startOffset, HEAD_TAG);
            listener.closedStartTag(endOffset, HEAD_TAG);
        } else if ("body".equalsIgnoreCase(tagName)) {
            listener.openedStartTag(startOffset, BODY_TAG);
            listener.closedStartTag(endOffset, BODY_TAG);
        } else if ("script".equalsIgnoreCase(tagName)) {
            listener.openedStartTag(startOffset, SCRIPT_TAG);
            listener.closedStartTag(endOffset, SCRIPT_TAG);
        }
    }

    void closedTag(int startOffset, int endOffset, String tagName) {
        //log.debug("Found close tag at " + startOffset + ":" + endOffset + ":" + tagName);
        
        if ("head".equalsIgnoreCase(tagName)) {
            listener.openedEndTag(startOffset, HEAD_TAG);
            listener.closedEndTag(endOffset, HEAD_TAG);
        } else if ("body".equalsIgnoreCase(tagName)) {
            listener.openedEndTag(startOffset, BODY_TAG);
            listener.closedEndTag(endOffset, BODY_TAG);
        } else if ("script".equalsIgnoreCase(tagName)) {
            listener.openedEndTag(startOffset, SCRIPT_TAG);
            listener.closedEndTag(endOffset, SCRIPT_TAG);
        }
    }
}
