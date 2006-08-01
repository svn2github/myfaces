package org.apache.myfaces.examples.form;

/**
 * @author Martin Marinschek
 */
public class FormBean
{
    public FormBean()
    {
        super();
        System.out.println("Initialize formBean");
    }

    public String testAction()
    {
        System.out.println("test");
        
        return null;
    }
}
