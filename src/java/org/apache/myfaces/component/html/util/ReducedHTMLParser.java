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

/**
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 */
public class ReducedHTMLParser
{
    public static final int BODY_TAG = 0;
    public static final int HEAD_TAG = 1;
    public static final int SCRIPT_TAG = 2;

    public static void parse(CharSequence seq, CallbackListener l)
    {
        char[] lastChars = new char[10];
        int currentTagIdentifier = -1;
        boolean openedTag = false;
        boolean openedStartTag=false;
        int openedTagIndex=-1;
        boolean closeSymbolEncountered = false;
        boolean commentMode = false;
        boolean attributeMode = false;
        char attributeOpenChar = 0;
        

        for(int i=0; i<seq.length();i++)
        {
            char c = seq.charAt(i);

            if(!commentMode && !attributeMode &&
                    lastChars[2]=='-' &&
                        lastChars[1]=='-' &&
                            lastChars[0]=='!' &&
                                c=='>')
            {
                commentMode = true;
            }
            else if(commentMode && !attributeMode &&
                    lastChars[1]=='<' &&
                            lastChars[0]=='-' &&
                                c=='-')
            {
                commentMode = false;
            }
            else if (!commentMode)
            {
                if(!attributeMode && !openedTag && c=='<')
                {
                    openedTag = true;
                    openedTagIndex = i;
                }
                else if(!attributeMode && openedTag && c=='>')
                {
                    if(currentTagIdentifier != -1)
                    {
                        if(openedStartTag)
                        {
                            l.closedStartTag(i,currentTagIdentifier);

                            if(closeSymbolEncountered)
                            {
                                l.closedEndTag(i,currentTagIdentifier);
                            }
                        }
                        else
                        {
                            l.closedEndTag(i,currentTagIdentifier);
                        }
                    }

                    openedTagIndex = -1;
                    openedTag = false;
                    openedStartTag = false;
                    closeSymbolEncountered = false;
                    currentTagIdentifier = -1;
                }
                else if(openedTag && !attributeMode && (c=='"' || c=='\''))
                {
                    attributeMode = true;
                    attributeOpenChar = c;
                }
                else if(openedTag && attributeMode && (c=='"' || c=='\'') && lastChars[0]!='\\')
                {
                    if(c==attributeOpenChar)
                    {
                        attributeMode = false;
                    }
                }
                else if(!attributeMode && openedTag && c=='/')
                {
                    closeSymbolEncountered = true;
                }
                else if(!attributeMode && openedTag &&
                     (lastChars[3]=='<' || Character.isWhitespace(lastChars[3]) || lastChars[3]=='/') && // Added this to make sure it's not <tbody> this was messing up in screen with datatable
                        (lastChars[2]=='b' || lastChars[2]=='B') &&
                            (lastChars[1]=='o' || lastChars[1]=='O') &&
                                (lastChars[0]=='d' || lastChars[0]=='D') &&
                                    (c=='y' || c=='Y'))
                {
                    currentTagIdentifier = BODY_TAG;

                    if (lastChars[3] != '/')
                    {
                        openedStartTag = handleTag(closeSymbolEncountered, l, openedTagIndex, openedStartTag,
                                currentTagIdentifier);
                    }
                }
                else if(!attributeMode && openedTag &&
                        (lastChars[3]=='<' || Character.isWhitespace(lastChars[3]) || lastChars[3]=='/' ) && // Added this to make sure it's not <thead> this was messing up in screen with datatable
                        (lastChars[2]=='h' || lastChars[2]=='H') &&
                            (lastChars[1]=='e' || lastChars[1]=='E')&&
                                (lastChars[0]=='a' || lastChars[1]=='A')&&
                                    (c=='d' || c=='D'))
                {
                    currentTagIdentifier = HEAD_TAG;

                    if (lastChars[3] != '/')
                    {
                        openedStartTag = handleTag(closeSymbolEncountered, l, openedTagIndex, openedStartTag,
                                currentTagIdentifier);
                    }
                }
                else if(!attributeMode && openedTag &&
                    (lastChars[5]=='<' || Character.isWhitespace(lastChars[5]) || lastChars[5]=='/' ) && // Added this to make sure it's not type="text/javascript" or language="JavaScript" inside the script tag
                        (lastChars[4]=='s' || lastChars[4]=='S') &&
                            (lastChars[3]=='c' || lastChars[3]=='C') &&
                                (lastChars[2]=='r' || lastChars[2]=='R') &&
                                    (lastChars[1]=='i' || lastChars[1]=='I')&&
                                        (lastChars[0]=='p' || lastChars[0]=='P')&&
                                            (c=='t' || c=='T'))
                {
                    currentTagIdentifier = SCRIPT_TAG;

                    if (lastChars[5] != '/')
                    {
                        openedStartTag = handleTag(closeSymbolEncountered, l,
                                openedTagIndex, openedStartTag, currentTagIdentifier);
                    }
                }
            }

            lastChars[9]=lastChars[8];
            lastChars[8]=lastChars[7];
            lastChars[7]=lastChars[6];
            lastChars[6]=lastChars[5];
            lastChars[5]=lastChars[4];
            lastChars[4]=lastChars[3];
            lastChars[3]=lastChars[2];
            lastChars[2]=lastChars[1];
            lastChars[1]=lastChars[0];
            lastChars[0]=c;
        }
    }

    private static boolean handleTag(boolean closeSymbolEncountered, CallbackListener l,
                                     int openedTagIndex, boolean openedStartTag, int currentTagIdentifier)
    {
        if(closeSymbolEncountered)
        {
            l.openedEndTag(openedTagIndex,currentTagIdentifier);
        }
        else
        {
            l.openedStartTag(openedTagIndex,currentTagIdentifier);
            openedStartTag = true;
        }
        return openedStartTag;
    }

    public static void main(String[] args)
    {
        ReducedHTMLParser.parse(new StringBuffer("<html><head>\n"
                + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/adas.css\" />\n"
                + "        <script src=\"/js/adas.js\" type=\"text/javascript\" language=\"JavaScript\"> </script>\n"
                + "        <title>MyFaces</title>\n"
                + "    </head>\n"
                + "    <body> </body>'x\"><xxx></xxx></html"),new CallbackListener(){
            public void openedStartTag(int charIndex, int tagIdentifier)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void closedStartTag(int charIndex, int tagIdentifier)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void openedEndTag(int charIndex, int tagIdentifier)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void closedEndTag(int charIndex, int tagIdentifier)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void attribute(int charIndex, int tagIdentifier, String key, String value)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}
