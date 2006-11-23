package org.apache.myfaces.custom.loadbundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/**
 * A load-bundle alternative which allows to use load-bundle
 * even on AJAX-enabled pages.
 * <p/>
 * A tag that allows to load bundles not only on rendering, but whenever the
 * page author needs it. By default, it loads it on every lifecycle phase
 * except restore-state and save-state.
 *
 * The core-load-bundle only loads its message-bundle
 * on rendering - this load-bundle does it on every life-cycle,
 * and optionally whenever the method loadBundle is called.

 * @author Martin Marinschek
 */
public class LoadBundleTag extends UIComponentTagBase {
    
    private Log log = LogFactory.getLog(LoadBundleTag.class);

    private String basename;
    private String var;

    public void release() {
        super.release();

        basename=null;
        var=null;

    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, "basename", basename);
        setStringProperty(component, "var", var);
    }

    public String getComponentType() {
        return LoadBundle.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    public void setBasename(String basename){
        this.basename = basename;
    }

    public void setVar(String var){
        this.var = var;
    }

    public int doStartTag() throws JspException
    {
        int retVal= super.doStartTag();

        UIComponent comp = getComponentInstance();

        if(comp instanceof LoadBundle)
        {
            ((LoadBundle) comp).loadBundle();
        }
        else
        {
            log.warn("associated component is no loadbundle.");
        }

        return retVal;
    }
}
