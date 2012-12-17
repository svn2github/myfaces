window.org = window.org || {};
org.apache = org.apache || {};
org.apache.myfaces = org.apache.myfaces || {};

if (!org.apache.myfaces.Tree) {
    org.apache.myfaces.Tree = function () {
    };

    org.apache.myfaces.Tree.treeNavClick = function (spanId, navImageId, image1, image2, nodeImgId, expandImg, collapseImg, cookieName, nodeId) {
        var navSpan = document.getElementById(spanId);
        var displayStyle = navSpan.style.display;
        if (displayStyle == 'none') {
            displayStyle = 'block'
            org.apache.myfaces.CookieLib.setCookieAttrib(cookieName, nodeId, "x");
        } else {
            displayStyle = 'none';
            org.apache.myfaces.CookieLib.setCookieAttrib(cookieName, nodeId, "c");
        }
        navSpan.style.display = displayStyle;
        if (navImageId != '') {
            var navImage = document.getElementById(navImageId);
            if (navImage.src.indexOf(image1) >= 0) navImage.src = image2; else navImage.src = image1;
        }
        if (nodeImgId != '') {
            var nodeImg = document.getElementById(nodeImgId);
            if (nodeImg.src.indexOf(expandImg) >= 0)
                nodeImg.src = collapseImg;
            else nodeImg.src = expandImg;
        }
    };
}