package org.apache.myfaces.custom.inputAjax;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.custom.ajax.api.AjaxComponent;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.ajax.AjaxCallbacks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * User: Travis Reeder
 * Date: Mar 22, 2006
 * Time: 4:37:53 PM
 */
public class HtmlCommandButtonAjax extends HtmlCommandButton implements AjaxComponent, AjaxCallbacks
{
    private static final Log log = LogFactory.getLog(HtmlInputTextAjax.class);
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlCommandButtonAjax";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.CommandButtonAjax";

    private String onSuccess;
    private String onFailure;
    private String onStart;
    private String errorStyleClass;
    private String errorStyle;

    public HtmlCommandButtonAjax()
    {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
        setType("button"); // we don't want this to submit
        setOnclick("");
    }

    /**
     * Decode ajax request and apply value changes
     *
     * @param context
     */
    public void decodeAjax(FacesContext context)
    {
        log.debug("entering HtmlCommandButtonAjax.decodeAjax");

        processDecodes(context);
        processValidators(context);
        processUpdates(context);
        //context.getViewRoot().processApplication(context);
        if (log.isDebugEnabled())
        {
            Object valOb = this.getValue();
            log.debug("value object after decodeAjax: " + valOb);
        }

    }

    public void encodeAjax(FacesContext context) throws IOException
    {
        log.debug("encodeAjax in HtmlCommandButtonAjax");
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        Renderer renderer = getRenderer(context);

        if (renderer != null && renderer instanceof AjaxRenderer)
        {
            ((AjaxRenderer) renderer).encodeAjax(context, this);

        }
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = onSuccess;
        values[2] = onFailure;
        values[3] = onStart;
        values[4] = errorStyleClass;
        values[5] = errorStyle;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        onSuccess = (String) values[1];
        onFailure = (String) values[2];
        onStart = (String) values[3];
        errorStyleClass = (String) values[4];
        errorStyle = (String) values[5];
    }

    public String getOnSuccess()
    {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess)
    {
        this.onSuccess = onSuccess;
    }

    public String getOnFailure()
    {
        return onFailure;
    }

    public void setOnFailure(String onFailure)
    {
        this.onFailure = onFailure;
    }

    public String getOnStart()
    {
        return onStart;
    }

    public void setOnStart(String onStart)
    {
        this.onStart = onStart;
    }


    public String getErrorStyleClass()
    {
        return errorStyleClass;
    }

    public void setErrorStyleClass(String errorStyleClass)
    {
        this.errorStyleClass = errorStyleClass;
    }

    public String getErrorStyle()
    {
        return errorStyle;
    }

    public void setErrorStyle(String errorStyle)
    {
        this.errorStyle = errorStyle;
    }
}
