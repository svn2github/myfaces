package org.apache.myfaces.custom.validators;

import javax.faces.component.UIInput;
import javax.faces.validator.ValidatorException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.custom.regexprvalidator.RegExprValidator;

public class RegExprValidatorTestCase extends AbstractValidatorTestCase
{

  public RegExprValidatorTestCase(String arg0) {
    super(arg0);
  }
  
  RegExprValidator validator;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    validator = new RegExprValidator();
    
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public static Test suite()
  {
    return new TestSuite(RegExprValidatorTestCase.class);
  }
  
  /**
   * Test when context is set to null
   */
  public void testNullContext()
  {

    doTestNullContext(component, validator);
  }
  
  public void testRightValue()
  {
    validator.setPattern("\\d{5}");
    
    UIInput comp1 = new UIInput();
    comp1.setValue("12345");
    comp1.setId("comp1");
    facesContext.getViewRoot().getChildren().add(comp1);
    
    validator.validate(facesContext, comp1, comp1.getValue());

  }

  public void testWrongValue()
  {
    try
    {
      validator.setPattern("\\d{12}");
      
      UIInput comp1 = new UIInput();
      comp1.setValue("12345");
      comp1.setId("comp1");
      facesContext.getViewRoot().getChildren().add(comp1);
      
      validator.validate(facesContext, comp1, comp1.getValue());
      
      fail("Expected ValidatorException");
    }
    catch (ValidatorException ve)
    {
      // if exception then fine.
    }

  }

  
}
