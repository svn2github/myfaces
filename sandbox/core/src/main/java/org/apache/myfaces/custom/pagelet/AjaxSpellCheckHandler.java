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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.myfaces.shared_tomahawk.renderkit.html.util.*;

/**
 * @author Thomas Spiegl
 */
public class AjaxSpellCheckHandler {

    private static final String  SPCK2_PARAM_NAME      = "spck[]";
    private static final int     FLAGS                 = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
    private static final Pattern SPAN                  = Pattern.compile("<span(.*?)>(.*?)</span>", FLAGS);
    private static final Pattern URL_PATTERN           = Pattern.compile("(https?|ftp|mailto)://[a-zA-Z0-9\\.@]+\\.[a-zA-Z]{2,3}",
            Pattern.DOTALL);
    private static final Pattern EMAIL_PATTERN         = Pattern.compile("[a-zA-Z0-9_\\.]+@[a-zA-Z0-9_\\.]+\\.[a-zA-Z]{2,3}",
            Pattern.DOTALL);
    private static final Pattern WORDS_PATTERN         = Pattern.compile("[a-zxA-Z]+", Pattern.DOTALL);
    private static final Pattern ENUMERATION_PATTERN   = Pattern.compile("^\\s*[a-zA-Z0-9\\.]+\\)", Pattern.MULTILINE);
    private static final Pattern XHMTL_PATTERN         = Pattern.compile("(<[^>]+>)|(\\&[A-Za-z\\#0-9]+\\;)", Pattern.MULTILINE);
    private static final String  PARAM_NAME            = "cpaint_argument[]";

    public static void main(String[] argv) {
        System.out.println(("<li><a href='#' id=':evt_target_id_corrid_" + 1 + "'>:correctionWord</a></li>").replaceAll("(\\:evt_target_id)", "boogaqloo"));
    }

    public void handleAjaxRequest(FacesContext facesContext) throws ServletException, IOException {
        ServletRequest  servletRequest  = (ServletRequest) facesContext.getExternalContext().getRequest();
        ServletResponse servletResponse = (ServletResponse) facesContext.getExternalContext().getResponse();
        String[]        params          = servletRequest.getParameterValues(PARAM_NAME);
        String[]        spck2params     = servletRequest.getParameterValues(SPCK2_PARAM_NAME);

        if ((params == null) && (spck2params == null))
            return;

        if (spck2params != null) {
            handleAjaxRequest2(facesContext, spck2params);

            return;
        }

        if (params.length == 1) {
            PrintWriter writer = servletResponse.getWriter();
            writer.write(HTMLDecoder.decode(removeSpan(params[0])));
            servletResponse.flushBuffer();
        } else if (params.length > 2) {
            String        value   = params[0];
            String        varname = params[1];
            String        method  = params[2];
            String        textStr = decodeValue(servletRequest, value);
            MethodBinding mb      = getMethodBinding(facesContext, method);

            if ((params.length >= 4) && "CHECK".equals(params[3])) {
                checkSpelling(facesContext, servletResponse, mb, textStr, varname);
            } else {
                suggest(facesContext, servletResponse, mb, textStr, varname);
            }
        } else {
            throw new RuntimeException("unexpected param.length " + params.length);
        }
    }

    public void handleAjaxRequest2(FacesContext facesContext, String[] params) throws ServletException, IOException {
        ServletRequest  servletRequest  = (ServletRequest) facesContext.getExternalContext().getRequest();
        ServletResponse servletResponse = (ServletResponse) facesContext.getExternalContext().getResponse();
        String          command         = params[0];
        String          method          = null;

        String content = servletRequest.getParameter("content");

        if (command.equals("spellcheck")) {
            method  = params[1];
            content = removeSpan2(content);

            MethodBinding mb = getMethodBinding(facesContext, method);

            checkSpelling2(facesContext, servletResponse, mb, content);
        } else if (command.equals("cleanup")) {
            cleanup2(servletResponse, content);
        } else if (command.equals("showsuggestions")) {
            method = params[1];

            MethodBinding mb = getMethodBinding(facesContext, method);
            suggest2(facesContext, servletRequest, servletResponse, content, mb);
        }

    }


    public String removeSpan(String value) {
        StringBuffer buf     = new StringBuffer();
        Matcher      matcher = SPAN.matcher(value);
        int          idx     = 0;

        while (matcher.find()) {
            int start = matcher.start();
            buf.append(value.substring(idx, start));
            buf.append(matcher.group(2));
            idx = matcher.end();
        }

        buf.append(value.substring(idx, value.length()));

        return HTMLEncoder.encode(buf.toString(), true, false, true);
    }

    public String removeSpan2(String value) {
        StringBuffer buf     = new StringBuffer();
        Matcher      matcher = SPAN.matcher(value);
        int          idx     = 0;

        while (matcher.find()) {
            int start = matcher.start();
            buf.append(value.substring(idx, start));
            buf.append(matcher.group(2));
            idx = matcher.end();
        }

        buf.append(value.substring(idx, value.length()));

        return buf.toString();
    }

    public int substitute(Matcher matcher, String value, List words, boolean html) {
        int startIdx = 0;

        if (matcher.find()) {
            int    start  = matcher.start();
            int    end    = matcher.end();
            String before = value.substring(startIdx, start);
            words.add(new Word(before, true, startIdx));

            String subst = value.substring(start, end);

            if (html) {
                words.add(new Word("&lt;" + subst.substring(1, subst.length() - 1) + "&gt;", false, start));
            } else {
                words.add(new Word(subst, false, start));
            }

            if (end > value.length()) {
                startIdx = substitute(matcher, value.substring(startIdx, value.length()), words, html);
            } else {
                startIdx = end;
            }
        }

        return startIdx;
    }

    private static String encodeValue(String value) {
        return HTMLEncoder.encode(value, true, false, true);
    }

    private static List extractWords(String value) {
        List words = new ArrayList();
        words.add(new Word(value, true, 0));

        if (XHMTL_PATTERN.matcher(value).find())
            words = splitWords(XHMTL_PATTERN, words, false);

        if (URL_PATTERN.matcher(value).find())
            words = splitWords(URL_PATTERN, words, false);

        if (EMAIL_PATTERN.matcher(value).find())
            words = splitWords(EMAIL_PATTERN, words, false);

        if (ENUMERATION_PATTERN.matcher(value).find())
            words = splitWords(ENUMERATION_PATTERN, words, false);

        words = splitWords(WORDS_PATTERN, words, true);

        return words;
    }

    private static List splitWords(Pattern pattern, List words, boolean checkSpelling) {
        List newWords = new ArrayList((words.size() < 10) ? 50 : (words.size() * 2));

        for (int i = 0; i < words.size(); i++) {
            Word   word  = (Word) words.get(i);
            String value = word.getValue();

            if (word.checkSpelling) {
                Matcher matcher = pattern.matcher(value);
                int     idx     = 0;

                while (matcher.find()) {
                    int start = matcher.start();
                    int end   = matcher.end();

                    if (start > idx)
                        newWords.add(new Word(value.substring(idx, start), !checkSpelling, idx));

                    newWords.add(new Word(value.substring(start, end), checkSpelling, idx));
                    idx = end;
                }

                if (idx < value.length())
                    newWords.add(new Word(value.substring(idx, value.length()), !checkSpelling, idx));
            } else {
                newWords.add(word);
            }
        }

        return newWords;
    }

    private void checkSpelling(FacesContext facesContext, ServletResponse servletResponse, MethodBinding mb, String textStr, String varname)
        throws IOException {

        if ((textStr == null) || (textStr.length() == 0))
            return;

        List         words = extractWords(textStr);
        StringBuffer buf   = new StringBuffer();
        mb.invoke(facesContext, new Object[] { new Text(words) });

        boolean misspellingFound = false;

        for (int i = 0, sizei = words.size(); i < sizei; i++) {
            Word   word  = (Word) words.get(i);
            String value = word.getValue();
            value = encodeValue(value);

            if (word.isInvalid()) {
                misspellingFound = true;

                String id = varname + "_" + i;
                buf.append("\\u003cspan onclick=\"setCurrentObject(").append(varname).append(");");
                buf.append("showSuggestions('").append(word.getValue()).append("','").append(id).append("');\" ");
                buf.append("id=\"").append(id).append("\" ");
                buf.append("class=\"highlight\"\\u003e");
                buf.append(value);
                buf.append("\\u003c/span\\u003e");
            } else {
                buf.append(value);
            }
        }

        PrintWriter writer = servletResponse.getWriter();
        writer.write(misspellingFound ? '1' : '0');
        writer.write(buf.toString());
        servletResponse.flushBuffer();
    }

    private void checkSpelling2(FacesContext facesContext, ServletResponse servletResponse, MethodBinding mb, String textStr)
        throws IOException {

        if ((textStr == null) || (textStr.length() == 0))
            return;

        List words = extractWords(textStr);
        mb.invoke(facesContext, new Object[] { new Text(words) });

        int          spellErrCnt = 0;
        StringBuffer textBuilder = new StringBuffer(textStr.length() + 3);


        for (int i = 0, sizei = words.size(); i < sizei; i++) {

            Word currentWord = (Word) words.get(i);

            if (currentWord.isInvalid()) {

                textBuilder.append(" <span id='myfaces_SpellErr:" + spellErrCnt + "' class='myfaces_SpellingError'>" + currentWord.getValue() + "</span> ");
                spellErrCnt++;
            } else {
                textBuilder.append(currentWord.getValue());
            }
        }

        //servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html");

        PrintWriter writer = servletResponse.getWriter();
        writer.write(textBuilder.toString());
        servletResponse.flushBuffer();
    }

    private void cleanup2(ServletResponse servletResponse, String content) throws IOException {
        content = removeSpan2(content);

        //servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html");

        PrintWriter writer = servletResponse.getWriter();
        writer.write(content);
        servletResponse.flushBuffer();
    }

    private String decodeValue(ServletRequest servletRequest, String value) throws UnsupportedEncodingException {
        String encoding = servletRequest.getCharacterEncoding();

        if (encoding == null)
            encoding = "iso-8859-1";

        return URLDecoder.decode(URLEncoder.encode(value, encoding), "utf8");
    }

    private MethodBinding getMethodBinding(FacesContext facesContext, String method) {
        MethodBinding mb = null;

        if (UIComponentTag.isValueReference(method)) {
            mb = facesContext.getApplication().createMethodBinding(method, new Class[] { Text.class });

            if (mb == null)
                throw new NullPointerException("Spellchecker Methodbinding must not be null");
        }

        return mb;
    }

    private void suggest(FacesContext facesContext, ServletResponse servletResponse, MethodBinding mb, String textStr, String varname)
        throws IOException {
        Word word = new Word(textStr, true, 0);
        Text text = new Text(word);
        mb.invoke(facesContext, new Object[] { text });

        String[]     alternatives = word.getAlternatives();
        StringBuffer buf          = new StringBuffer();

        if ((alternatives != null) && (alternatives.length > 0)) {

            for (int i = 0; i < alternatives.length; i++) {
                buf.append("\\u003cdiv class=\"suggestion\" ");
                buf.append("onclick=\"replaceWord('").append(varname).append("','").append(alternatives[i]).append("')\"");
                buf.append("\\u003e").append(encodeValue(alternatives[i])).append("\\u003c/div\\u003e");
            }
        } else {
            buf.append("\\u003cdiv class=\"suggestion\" ");
            buf.append("onclick=\"replaceWord('-','-')\"");
            buf.append("\\u003e-\\u003c/div\\u003e");
        }

        PrintWriter writer = servletResponse.getWriter();
        writer.write(buf.toString());
        servletResponse.flushBuffer();
    }

    private void suggest2(FacesContext facesContext, ServletRequest servletRequest, ServletResponse servletResponse, String content,
            MethodBinding mb) throws IOException {
        content = removeSpan2(content);

        String targetid = servletRequest.getParameter("targetid");

        if (targetid == null)
            targetid = "";

        Word word = new Word(content, true, 0);

        //InputSuggestAjaxTest spellchecker = new InputSuggestAjaxTest();

        mb.invoke(facesContext, new Object[] { new Text(word) });
        //spellchecker.checkSpelling(new Text(word));

        StringBuffer contentBuilder = new StringBuffer(254);

        //";" separatet ist of results
        contentBuilder.append("{noEntries: ");
        contentBuilder.append(word.getAlternatives().length);
        contentBuilder.append(",content: \"");

        for (int cnt = 1; cnt <= word.getAlternatives().length; cnt++)
            contentBuilder.append(("<li><div class='myfaces_SuggestEntry' id=':evt_target_id_corrid_" + cnt + "'>:correctionWord</div></li>").replaceAll("\\:evt_target_id", targetid).replaceAll("\\:correctionWord", word.getAlternatives()[
                cnt - 1]));

        contentBuilder.append("\"}");
        //servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html");

        PrintWriter writer = servletResponse.getWriter();
        writer.write(contentBuilder.toString());
        servletResponse.flushBuffer();
    }

}
