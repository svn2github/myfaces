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
package org.apache.myfaces.custom.roundedPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Leonardo Uribe
 *
 */
public class HtmlColorUtils
{
    private static final Map<String, String> COLOR_NAMES_CODE = new HashMap<String, String>();
    
    static
    {
        COLOR_NAMES_CODE.put("aliceblue","#F0F8FF");
        COLOR_NAMES_CODE.put("antiquewhite","#FAEBD7");
        COLOR_NAMES_CODE.put("aqua","#00FFFF");
        COLOR_NAMES_CODE.put("aquamarine","#7FFFD4");
        COLOR_NAMES_CODE.put("azure","#F0FFFF");
        COLOR_NAMES_CODE.put("beige","#F5F5DC");
        COLOR_NAMES_CODE.put("bisque","#FFE4C4");
        COLOR_NAMES_CODE.put("black","#000000");
        COLOR_NAMES_CODE.put("blanchedalmond","#FFEBCD");
        COLOR_NAMES_CODE.put("blue","#0000FF");
        COLOR_NAMES_CODE.put("blueviolet","#8A2BE2");
        COLOR_NAMES_CODE.put("brown","#A52A2A");
        COLOR_NAMES_CODE.put("burlywood","#DEB887");
        COLOR_NAMES_CODE.put("cadetblue","#5F9EA0");
        COLOR_NAMES_CODE.put("chartreuse","#7FFF00");
        COLOR_NAMES_CODE.put("chocolate","#D2691E");
        COLOR_NAMES_CODE.put("coral","#FF7F50");
        COLOR_NAMES_CODE.put("cornflowerblue","#6495ED");
        COLOR_NAMES_CODE.put("cornsilk","#FFF8DC");
        COLOR_NAMES_CODE.put("crimson","#DC143C");
        COLOR_NAMES_CODE.put("cyan","#00FFFF");
        COLOR_NAMES_CODE.put("darkblue","#00008B");
        COLOR_NAMES_CODE.put("darkcyan","#008B8B");
        COLOR_NAMES_CODE.put("darkgoldenrod","#B8860B");
        COLOR_NAMES_CODE.put("darkgray","#A9A9A9");
        COLOR_NAMES_CODE.put("darkgrey","#A9A9A9");
        COLOR_NAMES_CODE.put("darkgreen","#006400");
        COLOR_NAMES_CODE.put("darkkhaki","#BDB76B");
        COLOR_NAMES_CODE.put("darkmagenta","#8B008B");
        COLOR_NAMES_CODE.put("darkolivegreen","#556B2F");
        COLOR_NAMES_CODE.put("darkorange","#FF8C00");
        COLOR_NAMES_CODE.put("darkorchid","#9932CC");
        COLOR_NAMES_CODE.put("darkred","#8B0000");
        COLOR_NAMES_CODE.put("darksalmon","#E9967A");
        COLOR_NAMES_CODE.put("darkseagreen","#8FBC8F");
        COLOR_NAMES_CODE.put("darkslateblue","#483D8B");
        COLOR_NAMES_CODE.put("darkslategray","#2F4F4F");
        COLOR_NAMES_CODE.put("darkstategrey","#2F4F4F");
        COLOR_NAMES_CODE.put("darkturquoise","#00CED1");
        COLOR_NAMES_CODE.put("darkviolet","#9400D3");
        COLOR_NAMES_CODE.put("deeppink","#FF1493");
        COLOR_NAMES_CODE.put("deepskyblue","#00BFFF");
        COLOR_NAMES_CODE.put("dimgray","#696969");
        COLOR_NAMES_CODE.put("dimgrey","#696969");
        COLOR_NAMES_CODE.put("dodgerblue","#1E90FF");
        COLOR_NAMES_CODE.put("firebrick","#B22222");
        COLOR_NAMES_CODE.put("floralwhite","#FFFAF0");
        COLOR_NAMES_CODE.put("forestgreen","#228B22");
        COLOR_NAMES_CODE.put("fuchsia","#FF00FF");
        COLOR_NAMES_CODE.put("gainsboro","#DCDCDC");
        COLOR_NAMES_CODE.put("ghostwhite","#F8F8FF");
        COLOR_NAMES_CODE.put("gold","#FFD700");
        COLOR_NAMES_CODE.put("goldenrod","#DAA520");
        COLOR_NAMES_CODE.put("gray","#808080");
        COLOR_NAMES_CODE.put("grey","#808080");
        COLOR_NAMES_CODE.put("green","#008000");
        COLOR_NAMES_CODE.put("greenyellow","#ADFF2F");
        COLOR_NAMES_CODE.put("honeydew","#F0FFF0");
        COLOR_NAMES_CODE.put("hotpink","#FF69B4");
        COLOR_NAMES_CODE.put("indianred","#CD5C5C");
        COLOR_NAMES_CODE.put("indigo","#4B0082");
        COLOR_NAMES_CODE.put("ivory","#FFFFF0");
        COLOR_NAMES_CODE.put("khaki","#F0E68C");
        COLOR_NAMES_CODE.put("lavender","#E6E6FA");
        COLOR_NAMES_CODE.put("lavenderblush","#FFF0F5");
        COLOR_NAMES_CODE.put("lawngreen","#7CFC00");
        COLOR_NAMES_CODE.put("lemonchiffon","#FFFACD");
        COLOR_NAMES_CODE.put("lightblue","#ADD8E6");
        COLOR_NAMES_CODE.put("lightcoral","#F08080");
        COLOR_NAMES_CODE.put("lightcyan","#E0FFFF");
        COLOR_NAMES_CODE.put("lightgoldenrodyellow","#FAFAD2");
        COLOR_NAMES_CODE.put("lightgreen","#90EE90");
        COLOR_NAMES_CODE.put("lightgray","#D3D3D3");
        COLOR_NAMES_CODE.put("lightgrey","#D3D3D3");
        COLOR_NAMES_CODE.put("lightpink","#FFB6C1");
        COLOR_NAMES_CODE.put("lightsalmon","#FFA07A");
        COLOR_NAMES_CODE.put("lightseagreen","#20B2AA");
        COLOR_NAMES_CODE.put("lightskyblue","#87CEFA");
        COLOR_NAMES_CODE.put("lightslategray","#778899");
        COLOR_NAMES_CODE.put("lightslategrey","#778899");
        COLOR_NAMES_CODE.put("lightsteelblue","#B0C4DE");
        COLOR_NAMES_CODE.put("lightyellow","#FFFFE0");
        COLOR_NAMES_CODE.put("lime","#00FF00");
        COLOR_NAMES_CODE.put("limegreen","#32CD32");
        COLOR_NAMES_CODE.put("linen","#FAF0E6");
        COLOR_NAMES_CODE.put("magenta","#FF00FF");
        COLOR_NAMES_CODE.put("maroon","#800000");
        COLOR_NAMES_CODE.put("mediumaquamarine","#66CDAA");
        COLOR_NAMES_CODE.put("mediumblue","#0000CD");
        COLOR_NAMES_CODE.put("mediumorchid","#BA55D3");
        COLOR_NAMES_CODE.put("mediumpurple","#9370DB");
        COLOR_NAMES_CODE.put("mediumseagreen","#3CB371");
        COLOR_NAMES_CODE.put("mediumslateblue","#7B68EE");
        COLOR_NAMES_CODE.put("mediumspringgreen","#00FA9A");
        COLOR_NAMES_CODE.put("mediumturquoise","#48D1CC");
        COLOR_NAMES_CODE.put("mediumvioletred","#C71585");
        COLOR_NAMES_CODE.put("midnightblue","#191970");
        COLOR_NAMES_CODE.put("mintcream","#F5FFFA");
        COLOR_NAMES_CODE.put("mistyrose","#FFE4E1");
        COLOR_NAMES_CODE.put("moccasin","#FFE4B5");
        COLOR_NAMES_CODE.put("navajowhite","#FFDEAD");
        COLOR_NAMES_CODE.put("navy","#000080");
        COLOR_NAMES_CODE.put("oldlace","#FDF5E6");
        COLOR_NAMES_CODE.put("olive","#808000");
        COLOR_NAMES_CODE.put("olivedrab","#6B8E23");
        COLOR_NAMES_CODE.put("orange","#FFA500");
        COLOR_NAMES_CODE.put("orangered","#FF4500");
        COLOR_NAMES_CODE.put("orchid","#DA70D6");
        COLOR_NAMES_CODE.put("palegoldenrod","#EEE8AA");
        COLOR_NAMES_CODE.put("palegreen","#98FB98");
        COLOR_NAMES_CODE.put("paleturquoise","#AFEEEE");
        COLOR_NAMES_CODE.put("palevioletred","#DB7093");
        COLOR_NAMES_CODE.put("papayawhip","#FFEFD5");
        COLOR_NAMES_CODE.put("peachpuff","#FFDAB9");
        COLOR_NAMES_CODE.put("peru","#CD853F");
        COLOR_NAMES_CODE.put("pink","#FFC0CB");
        COLOR_NAMES_CODE.put("plum","#DDA0DD");
        COLOR_NAMES_CODE.put("powderblue","#B0E0E6");
        COLOR_NAMES_CODE.put("purple","#800080");
        COLOR_NAMES_CODE.put("red","#FF0000");
        COLOR_NAMES_CODE.put("rosybrown","#BC8F8F");
        COLOR_NAMES_CODE.put("royalblue","#4169E1");
        COLOR_NAMES_CODE.put("saddlebrown","#8B4513");
        COLOR_NAMES_CODE.put("salmon","#FA8072");
        COLOR_NAMES_CODE.put("sandybrown","#FAA460");
        COLOR_NAMES_CODE.put("seagreen","#2E8B57");
        COLOR_NAMES_CODE.put("seashell","#FFF5EE");
        COLOR_NAMES_CODE.put("sienna","#A0522D");
        COLOR_NAMES_CODE.put("silver","#C0C0C0");
        COLOR_NAMES_CODE.put("skyblue","#87CEEB");
        COLOR_NAMES_CODE.put("slateblue","#6A5ACD");
        COLOR_NAMES_CODE.put("slategray","#708090");
        COLOR_NAMES_CODE.put("slategrey","#708090");
        COLOR_NAMES_CODE.put("snow","#FFFAFA");
        COLOR_NAMES_CODE.put("springgreen","#00FF7F");
        COLOR_NAMES_CODE.put("steelblue","#4682B4");
        COLOR_NAMES_CODE.put("tan","#D2B48C");
        COLOR_NAMES_CODE.put("teal","#008080");
        COLOR_NAMES_CODE.put("thistle","#D8BFD8");
        COLOR_NAMES_CODE.put("tomato","#FF6347");
        COLOR_NAMES_CODE.put("turquoise","#40E0D0");
        COLOR_NAMES_CODE.put("violet","#EE82EE");
        COLOR_NAMES_CODE.put("wheat","#F5DEB3");
        COLOR_NAMES_CODE.put("white","#FFFFFF");
        COLOR_NAMES_CODE.put("whitesmoke","#F5F5F5");
        COLOR_NAMES_CODE.put("yellow","#FFFF00");
        COLOR_NAMES_CODE.put("yellowgreen","#9ACD32");
    }

    /**
     * Takes a color property and return a rgb color using the notation #RRGGBB.
     * If the entry is invalid o no valid chars are included returns null.
     * 
     * @param color
     * @return
     */
    public static String encodeColorProperty(String color)
    {
        if (color == null)
        {
            return null;
        }
        String encodedColor = color.trim();
        if (encodedColor.length() <= 0)
        {
            return null;
        }
        if (encodedColor.charAt(0) == '#')
        {
            //Check if valid digits are used.
            if (encodedColor.length() == 7)
            {
                for (int i = 1; i < encodedColor.length(); i++)
                {
                    char c = encodedColor.charAt(i);
                    if (( '0' <= c && c <= '9' )||
                        ( 'a' <= c && c <= 'f' )||
                        ( 'A' <= c && c <= 'F' ))
                    {
                    }
                    else
                    {
                        encodedColor = null;
                        break;
                    }
                }
            }
            else
            {
                encodedColor = null;
            }
        }
        else
        {
            encodedColor = COLOR_NAMES_CODE.get(encodedColor); 
        }
        return encodedColor;
    }

}