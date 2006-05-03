package org.apache.myfaces.examples.pagelet;

import org.apache.myfaces.custom.pagelet.Pagelet;
import org.apache.myfaces.custom.pagelet.Text;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;


public class ComponentBean {


    public Pagelet getSpellChecker() {
        Pagelet spellChecker = (Pagelet) createComponent(Pagelet.COMPONENT_TYPE);
        spellChecker.setSpellchecker(createMethodBinding("#{spellchecker.checkSpelling}", new Class[] { Text.class }));
        spellChecker.setControlMode("pagelet");

        HtmlPanelGroup   group = createPanelGroup();
        HtmlGraphicImage img   = (HtmlGraphicImage) createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        img.setUrl(skinFolder() + "/img/spellcheck.gif");
        img.setAlt("Check Spelling");
        group.getChildren().add(img);
        spellChecker.getFacets().put("linkSpellchecker", group);

        group = (HtmlPanelGroup) createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        img   = (HtmlGraphicImage) createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        img.setUrl(skinFolder() + "/img/spellcheck.gif");
        img.setAlt("Zoom");
        group.getChildren().add(img);
        spellChecker.getFacets().put("linkZoom", group);

        group = (HtmlPanelGroup) createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        img   = (HtmlGraphicImage) createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        img.setUrl(skinFolder() + "/img/resume.gif");
        img.setAlt("Resume Editing");
        group.getChildren().add(img);
        spellChecker.getFacets().put("linkResume", group);

        group = (HtmlPanelGroup) createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        img   = (HtmlGraphicImage) createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        img.setUrl(skinFolder() + "/img/resize.gif");
        img.setAlt("Bigger");
        group.getChildren().add(img);
        spellChecker.getFacets().put("linkResize", group);

        group = (HtmlPanelGroup) createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        img   = (HtmlGraphicImage) createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        img.setUrl(skinFolder() + "/img/downsize.gif");
        img.setAlt("Smaller");
        group.getChildren().add(img);
        spellChecker.getFacets().put("linkDownsize", group);

        return spellChecker;
    }

    private UIComponent createComponent(String componentType) {
        return FacesContext.getCurrentInstance().getApplication().createComponent(componentType);
    }

    private MethodBinding createMethodBinding(String expression, Class[] parameters) {
        return FacesContext.getCurrentInstance().getApplication().createMethodBinding(expression, parameters);
    }

    private HtmlPanelGroup createPanelGroup() {
        HtmlPanelGroup addressPanel = (HtmlPanelGroup) createComponent(HtmlPanelGroup.COMPONENT_TYPE);

        return addressPanel;
    }

    /* skinning stuff */

    private String skinFolder() {
        return "";
    }
}
