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

package org.apache.myfaces.custom.tree2;


import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import java.util.HashMap;

/**
 * @author Sean Schofield
 * @author Chris Barlow
 * @author Hans Bergsten (Some code taken from an example in his O'Reilly JavaServer Faces book. Copied with permission)
 * @version $Revision$ $Date$
 */
public class HtmlTreeRenderer extends Renderer
{
    protected static final String TOGGLE_SPAN = "org.apache.myfaces.tree.TOGGLE_SPAN";
    protected static final String ROOT_NODE_ID = "0";

    private static final String JAVASCRIPT_ENCODED = "org.apache.myfaces.tree.JAVASCRIPT_ENCODED";
    private static final String NAV_COMMAND = "org.apache.myfaces.tree.NAV_COMMAND";
    private static final String ENCODING = "UTF-8";
    private static final String ATTRIB_DELIM = ";";
    private static final String ATTRIB_KEYVAL = "=";
    private static final String NODE_STATE_EXPANDED = "x";
    private static final String NODE_STATE_CLOSED = "c";
    private final static String SEPARATOR = String.valueOf(NamingContainer.SEPARATOR_CHAR);

    private static final int NOTHING = 0;
    private static final int CHILDREN = 1;
    private static final int EXPANDED = 2;
    private static final int LINES = 4;
    private static final int LAST = 8;

    // see superclass for documentation
    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext context, UIComponent component)
    {
        super.decode(context, component);

        // see if one of the nav nodes was clicked, if so, then toggle appropriate node
        String nodeId = null;
        HtmlTree tree = (HtmlTree)component;
        String originalNodeId = tree.getNodeId();

        if (getBoolean(component, JSFAttr.CLIENT_SIDE_TOGGLE, true))
        {
            if (!getBoolean(component, JSFAttr.PRESERVE_TOGGLE, true))
            {
                return;
            }

            Map cookieMap = context.getExternalContext().getRequestCookieMap();
            Cookie treeCookie = (Cookie)cookieMap.get(component.getId());
            if (treeCookie == null || treeCookie.getValue() == null)
            {
                return;
            }

            String nodeState = null;
            Map attrMap = getCookieAttr(treeCookie);
            Iterator i = attrMap.keySet().iterator();
            while (i.hasNext())
            {
                nodeId = (String)i.next();
                nodeState = (String)attrMap.get(nodeId);

                if (NODE_STATE_EXPANDED.equals(nodeState))
                {
                    tree.setNodeId(nodeId);
                    if (!tree.isNodeExpanded())
                    {
                        tree.toggleExpanded();
                    }
                    tree.setNodeId(originalNodeId);
                }
                else if (NODE_STATE_CLOSED.equals(nodeState))
                {
                    tree.setNodeId(nodeId);
                    if (tree.isNodeExpanded())
                    {
                        tree.toggleExpanded();
                    }
                    tree.setNodeId(originalNodeId);
                }
            }
        }
        else
        {
            nodeId = (String)context.getExternalContext().getRequestParameterMap().get(tree.getId() + SEPARATOR + NAV_COMMAND);

            if (nodeId == null || nodeId.equals(""))
            {
                return;
            }

            tree.setNodeId(nodeId);
            tree.toggleExpanded();
            tree.setNodeId(originalNodeId);
        }
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException
    {
        // write javascript functions
        encodeJavascript(context, component);
    }

    /**
     * Renders the whole tree.  It generates a <code>&lt;span></code> element with an <code>id</code>
     * attribute if the component has been given an explicit ID.  The model nodes are rendered
     * recursively by the private <code>encodeNodes</code> method.
     *
     * @param context FacesContext
     * @param component The component whose children are to be rendered
     * @throws IOException
     */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        HtmlTree tree = (HtmlTree)component;
        boolean showRootNode = getBoolean(tree, JSFAttr.SHOW_ROOT_NODE, true);

        if (!component.isRendered()) return;

        if (tree.getValue() == null) return;

        ResponseWriter out = context.getResponseWriter();
        String clientId = null;

        if (component.getId() != null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
        {
            clientId = component.getClientId(context);
        }

        boolean isOuterSpanUsed = false;

        if (clientId != null)
        {
            isOuterSpanUsed = true;
            out.startElement("span", component);
            out.writeAttribute("id", clientId, "id");
        }

        if (showRootNode)
        {
            // encode the tree (starting with the root node)
            encodeTree(context, out, tree, null, 0);
        }
        else
        {
            tree.setNodeId("0");
            TreeNode rootNode = tree.getNode();
            List rootChildren = rootNode.getChildren();
            int kidId = 0;

            for (int i = 0; i < rootChildren.size(); i++)
            {
                encodeTree(context, out, tree, ROOT_NODE_ID, kidId++);
            }
        }

        tree.setNodeId(null);

        if (isOuterSpanUsed)
        {
            out.endElement("span");
        }
    }

    /**
     * Encodes the tree and its children.
     *
     * @param context FacesContext
     * @param out ResponseWriter
     * @param tree HtmlTree
     * @param parentId The parent's node id (where parent is the parent of the node we are about to render.)
     * @param childCount If this node is a child of another node, the count indicates which child number it is
     *  (used to construct the id of the next node to render.)
     * @throws IOException
     */
    protected void encodeTree(FacesContext context, ResponseWriter out, HtmlTree tree, String parentId, int childCount)
        throws IOException
    {
        boolean clientSideToggle = getBoolean(tree, JSFAttr.CLIENT_SIDE_TOGGLE, true);

        String nodeId = (parentId != null) ? parentId + NamingContainer.SEPARATOR_CHAR + childCount : ROOT_NODE_ID;
        String spanId = TOGGLE_SPAN + ":" + tree.getId() + ":" + nodeId;

        tree.setNodeId(nodeId);
        TreeNode node = tree.getNode();

        // encode the current node
        HtmlRendererUtils.writePrettyLineSeparator(context);
        beforeNodeEncode(context, out, tree);
        encodeCurrentNode(context, out, tree);
        afterNodeEncode(context, out);

        // only encode the children if clientSideToggle is true or if this node is expanded (regardless of clientSideToggle)
        if (clientSideToggle == true || tree.isNodeExpanded())
        {
            int kidId = 0;
            String currId = tree.getNodeId();
            List children = node.getChildren();

            // if client side toggling is on, add a span to be used for displaying/hiding children
            if (clientSideToggle)
            {
                out.startElement(HTML.SPAN_ELEM, tree);
                out.writeAttribute(HTML.ID_ATTR, spanId, null);

                if (tree.isNodeExpanded())
                {
                    out.writeAttribute(HTML.STYLE_ATTR, "display:block", null);
                }
                else
                {
                    out.writeAttribute(HTML.STYLE_ATTR, "display:none", null);
                }
            }

            for (int i = 0; i < children.size(); i++)
            {
                encodeTree(context, out, tree, currId, kidId++);
            }

            if (clientSideToggle)
            {
                out.endElement(HTML.SPAN_ELEM);
            }
        }
    }

    /**
     * Encodes the current node.  It is protected so that custom {@link Renderer}s can extend it.  That might be useful
     * if you would like to render additional per node information besides the tree node.
     *
     * @param context FacesContext
     * @param out ResponseWriter
     * @param tree HtmlTree
     * @throws IOException
     */
    protected void encodeCurrentNode(FacesContext context, ResponseWriter out, HtmlTree tree)
        throws IOException
    {
        TreeNode node = tree.getNode();

        // set configurable values
        boolean showRootNode = getBoolean(tree, JSFAttr.SHOW_ROOT_NODE, true);
        boolean showNav = getBoolean(tree, JSFAttr.SHOW_NAV, true);
        boolean showLines = getBoolean(tree, JSFAttr.SHOW_LINES, true);
        boolean clientSideToggle = getBoolean(tree, JSFAttr.CLIENT_SIDE_TOGGLE, true);

        if (clientSideToggle)
        {
            // we must show the nav icons if client side toggle is enabled (regardless of what user says)
            showNav = true;
        }

        UIComponent nodeTypeFacet = tree.getFacet(node.getType());
        UIComponent nodeImgFacet = null;

        if (nodeTypeFacet == null)
        {
            throw new IllegalArgumentException("Unable to locate facet with the name: " + node.getType());
        }

        // render node padding
        String[] pathInfo = tree.getPathInformation(tree.getNodeId());
        int paddingLevel = pathInfo.length - 1;

        for (int i = (showRootNode ? 0 : 1); i < paddingLevel; i++)
        {
            boolean lastChild = tree.isLastChild((String)pathInfo[i]);
            String lineSrc = (!lastChild && showLines)
                             ? getImageSrc(context, tree, "line-trunk.gif")
                             : getImageSrc(context, tree, "spacer.gif");

            out.startElement(HTML.TD_ELEM, tree);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "100%", null);
            out.writeURIAttribute("background", lineSrc, null);
            out.startElement(HTML.IMG_ELEM, tree);
            out.writeURIAttribute(HTML.SRC_ATTR, lineSrc, null);
            out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
            out.writeAttribute(HTML.HEIGHT_ATTR, "18", null);
            out.writeAttribute(HTML.BORDER_ATTR, "0", null);
            out.endElement(HTML.IMG_ELEM);
            out.endElement(HTML.TD_ELEM);
        }

        if (showNav)
        {
            nodeImgFacet = encodeNavigation(context, out, tree);
        }

        // render node
        out.startElement(HTML.TD_ELEM, tree);
        if (nodeImgFacet != null)
        {
            RendererUtils.renderChild(context, nodeImgFacet);
        }
        RendererUtils.renderChild(context, nodeTypeFacet);
        out.endElement(HTML.TD_ELEM);
    }

    protected void beforeNodeEncode(FacesContext context, ResponseWriter out, HtmlTree tree)
        throws IOException
    {
        out.startElement(HTML.TABLE_ELEM, null);
        out.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        out.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        out.writeAttribute(HTML.BORDER_ATTR, "0", null);
        out.startElement(HTML.TR_ELEM, null);
    }

    protected void afterNodeEncode(FacesContext context, ResponseWriter out)
        throws IOException
    {
        out.endElement(HTML.TR_ELEM);
        out.endElement(HTML.TABLE_ELEM);
    }

    /**
     * Handles the encoding related to the navigation functionality.
     *
     * @param context FacesContext
     * @param out ResponseWriter
     * @param tree HtmlTree
     * @return The additional navigation image to display inside the node (if any).  Only used with client-side toggle.
     * @throws IOException
     */
    private UIComponent encodeNavigation(FacesContext context, ResponseWriter out, HtmlTree tree)
        throws IOException
    {
        TreeNode node = tree.getNode();
        String nodeId = tree.getNodeId();
        String spanId = TOGGLE_SPAN + ":" + tree.getId() + ":" + nodeId;//TOGGLE_SPAN + nodeId;
        boolean showLines = getBoolean(tree, JSFAttr.SHOW_LINES, true);
        boolean clientSideToggle = getBoolean(tree, JSFAttr.CLIENT_SIDE_TOGGLE, true);
        UIComponent nodeTypeFacet = tree.getFacet(node.getType());
        String navSrc = null;
        String altSrc = null;
        UIComponent nodeImgFacet = null;

        int bitMask = NOTHING;
        bitMask += (node.getChildCount()>0) ? CHILDREN : NOTHING;
        bitMask += (tree.isNodeExpanded()) ? EXPANDED : NOTHING;
        bitMask += (tree.isLastChild(tree.getNodeId())) ? LAST : NOTHING;
        bitMask += (showLines) ? LINES : NOTHING;

        switch (bitMask)
        {
            case (NOTHING):

            case (LAST):
                navSrc = "spacer.gif";
                break;

            case (LINES):
                navSrc = "line-middle.gif";
                break;

            case (LINES + LAST):
                navSrc = "line-last.gif";
                break;

            case (CHILDREN):

            case (CHILDREN + LAST):
                navSrc = "nav-plus.gif";
                altSrc = "nav-minus.gif";
                break;

            case (CHILDREN + LINES):

                navSrc = "nav-plus-line-middle.gif";
                altSrc = "nav-minus-line-middle.gif";
                break;

            case (CHILDREN + LINES + LAST):

                navSrc = "nav-plus-line-last.gif";
                altSrc = "nav-minus-line-last.gif";
                break;

            case (CHILDREN + EXPANDED):

            case (CHILDREN + EXPANDED + LAST):
                navSrc = "nav-minus.gif";
                altSrc = "nav-plus.gif";
                break;

            case (CHILDREN + EXPANDED + LINES):
                navSrc = "nav-minus-line-middle.gif";
                altSrc = "nav-plus-line-middle.gif";
                break;

            case (CHILDREN + EXPANDED + LINES + LAST):
                navSrc = "nav-minus-line-last.gif";
                altSrc = "nav-plus-line-last.gif";
                break;

            default:
                throw new IllegalArgumentException("Invalid bit mask of " + bitMask);
        }

        // adjust navSrc and altSrc so that the images can be retrieved using the extensions filter
        String navSrcUrl = getImageSrc(null, tree, navSrc);
        navSrc = getImageSrc(context, tree, navSrc);
        altSrc = getImageSrc(context, tree, altSrc);

        // render nav cell
        out.startElement(HTML.TD_ELEM, tree);
        out.writeAttribute(HTML.WIDTH_ATTR, "19", null);
        out.writeAttribute(HTML.HEIGHT_ATTR, "100%", null);
        out.writeAttribute("valign", "top", null);

        if ((bitMask & LINES)!=0 && (bitMask & LAST)==0)
        {
            out.writeURIAttribute("background", getImageSrc(context, tree, "line-trunk.gif"), null);
        }

        // add the appropriate image for the nav control
        UIGraphic image = new UIGraphic();
        image.setId(context.getViewRoot().createUniqueId());
        image.setUrl(navSrcUrl);
        Map imageAttrs = image.getAttributes();
        imageAttrs.put(HTML.WIDTH_ATTR, "19");
        imageAttrs.put(HTML.HEIGHT_ATTR, "18");
        imageAttrs.put(HTML.BORDER_ATTR, "0");

        if (clientSideToggle)
        {
            /**
             * With client side toggle, user has the option to specify open/closed images for the node (in addition to
             * the navigtion ones provided by the component.)
             */
            String expandImgSrc = "";
            String collapseImgSrc = "";
            String nodeImageId = "";

            UIComponent expandFacet = nodeTypeFacet.getFacet("expand");
            if (expandFacet != null)
            {
                UIGraphic expandImg = (UIGraphic)expandFacet;
                expandImgSrc = expandImg.getUrl();
                if (expandImg.isRendered())
                {
                    expandImg.setId(context.getViewRoot().createUniqueId());
                    nodeImageId = expandImg.getClientId(context);
                    nodeImgFacet = expandFacet;
                }
            }

            UIComponent collapseFacet = nodeTypeFacet.getFacet("collapse");
            if (collapseFacet != null)
            {
                UIGraphic collapseImg = (UIGraphic)collapseFacet;
                collapseImgSrc = collapseImg.getUrl();
                if (collapseImg.isRendered())
                {
                    collapseImg.setId(context.getViewRoot().createUniqueId());
                    nodeImageId = collapseImg.getClientId(context);
                    nodeImgFacet = collapseFacet;
                }
            }

            if (node.getChildCount() > 0)
            {
                String onClick = new StringBuffer()
                    .append("treeNavClick('")
                    .append(spanId)
                    .append("', '")
                    .append(image.getClientId(context))
                    .append("', '")
                    .append(navSrc)
                    .append("', '")
                    .append(altSrc)
                    .append("', '")
                    .append(nodeImageId)
                    .append("', '")
                    .append(expandImgSrc)
                    .append("', '")
                    .append(collapseImgSrc)
                    .append("', '")
                    .append(tree.getId())
                    .append("', '")
                    .append(nodeId)
                    .append("');")
                    .toString();

                imageAttrs.put(HTML.ONCLICK_ATTR, onClick);
                imageAttrs.put(HTML.STYLE_ATTR, "cursor:hand;cursor:pointer");
            }
            RendererUtils.renderChild(context, image);
        }
        else
        {
            // set up the expand control and remove whatever children (if any) this control had previously
            UICommand expandControl = tree.getExpandControl();
            expandControl.setId(context.getViewRoot().createUniqueId());
            expandControl.getChildren().clear();

            UIParameter param = new UIParameter();
            param.setName(tree.getId() + NamingContainer.SEPARATOR_CHAR + NAV_COMMAND);
            param.setValue(tree.getNodeId());
            expandControl.getChildren().add(param);
            expandControl.getChildren().add(image);

            RendererUtils.renderChild(context, expandControl);
        }
        out.endElement(HTML.TD_ELEM);

        return nodeImgFacet;
    }

    /**
     * Encodes any stand-alone javascript functions that are needed.  Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws IOException
     */
    private void encodeJavascript(FacesContext context, UIComponent component) throws IOException
    {
        // check to see if javascript has already been written (which could happen if more than one tree on the same page)
        if (context.getExternalContext().getRequestMap().containsKey(JAVASCRIPT_ENCODED))
        {
            return;
        }

        // render javascript function for client-side toggle (it won't be used if user has opted for server-side toggle)
        ResponseWriter out = context.getResponseWriter();
        String javascriptLocation = (String)component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        if (javascriptLocation == null)
        {
            AddResource.addJavaScriptHere(HtmlTreeRenderer.class, "javascript/tree.js", context);
            AddResource.addJavaScriptHere(HtmlTreeRenderer.class, "javascript/cookielib.js", context);
        }
        else
        {
            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
            out.writeAttribute(HTML.SRC_ATTR,
                               javascriptLocation + "/tree.js", null);
            out.endElement(HTML.SCRIPT_ELEM);

            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
            out.writeAttribute(HTML.SRC_ATTR,
                               javascriptLocation + "/cookielib.js", null);
            out.endElement(HTML.SCRIPT_ELEM);
        }

        context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
    }

    /**
     * Get the appropriate image src location.  Uses the extension filter mechanism for images if no image
     * location has been specified.
     *
     * @param context The {@link FacesContex}.  NOTE: If <code>null</code> then context path information
     *    will not be used with extensions filter (assuming no imageLocation specified.)
     * @param component UIComponent
     * @param imageName The name of the image file to use.
     * @return The image src information.
     */
    private String getImageSrc(FacesContext context, UIComponent component, String imageName)
    {
        String imageLocation = (String)component.getAttributes().get(JSFAttr.IMAGE_LOCATION);
        if (imageLocation == null)
        {
            return AddResource.getResourceMappedPath(HtmlTreeRenderer.class,
                "images/" + imageName, context);
        }
        else
        {
            return imageLocation + "/" + imageName;
        }
    }

    /**
     * Helper method for getting the boolean value of an attribute.  If the attribute is not specified,
     * then return the default value.
     *
     * @param component The component for which the attributes are to be checked.
     * @param attributeName The name of the boolean attribute.
     * @param defaultValue The default value of the attribute (to be returned if no value found).
     * @return boolean
     */
    protected boolean getBoolean(UIComponent component, String attributeName, boolean defaultValue)
    {
        Boolean booleanAttr = (Boolean)component.getAttributes().get(attributeName);

        if (booleanAttr == null)
        {
            return defaultValue;
        }
        else
        {
            return booleanAttr.booleanValue();
        }
    }

    private Map getCookieAttr(Cookie cookie)
    {
        Map attribMap = new HashMap();
        try
        {
            String cookieValue = URLDecoder.decode(cookie.getValue(),ENCODING);
            String[] attribArray = cookieValue.split(ATTRIB_DELIM);
            for (int j = 0; j < attribArray.length; j++)
            {
                int index = attribArray[j].indexOf(ATTRIB_KEYVAL);
                String name = attribArray[j].substring(0, index);
                String value = attribArray[j].substring(index + 1);
                attribMap.put(name, value);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Error parsing tree cookies", e);
        }
        return attribMap;
    }
}
