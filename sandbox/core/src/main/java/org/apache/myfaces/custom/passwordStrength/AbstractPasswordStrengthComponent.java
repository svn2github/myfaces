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
package org.apache.myfaces.custom.passwordStrength;

import javax.faces.component.html.HtmlInputText;

import org.apache.myfaces.component.AlignProperty;

/**
 * The passwordStrength component is needed by the web sites 
 * which ask the user to enter a powerful password for the 
 * purpose of the registration stuff. 
 * <p>
 * The component enables its user to know the strength of the password 
 * while (he/she) types it before even submit the form to the server 
 * [please see the screenshots].            
 * </p>
 * <p>
 * The component enables its user to define his custom security policy
 * for his password in an easy manner.
 * </p>
 * <p>
 * The component also have 2 types of presenting the password strength.
 * Till now the strength can be represented as text or progressbar.
 * </p>
 * 
 * @JSFComponent
 *   name = "s:passwordStrength"
 *   class = "org.apache.myfaces.custom.passwordStrength.PasswordStrengthComponent"
 *   tagClass = "org.apache.myfaces.custom.passwordStrength.PasswordStrengthTag"
 *   
 */
public abstract class AbstractPasswordStrengthComponent extends HtmlInputText 
    implements AlignProperty{ 

    public static String COMPONENT_TYPE = "org.apache.myfaces.PasswordStrength";

    public static String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PasswordStrength";
    
    public static String COMPONENT_FAMILY = "org.apache.myfaces.PasswordStrength";
        
    /**
     * This flag {true | false} determines whether to show the details (left characters). 
     * default is true
     * 
     * @JSFProperty
     */
    public abstract String getShowDetails();
    
    /**
     * This flag determines the indicator type. It can be {text or bar}. Default is text
     * 
     * @JSFProperty
     */
    public abstract String getStrengthIndicatorType();        

    /**
     * The prefered length of the password
     * 
     * @JSFProperty
     */
    public abstract String getPreferredPasswordLength();
    
    /**
     * The prefix of the component message
     * 
     * @JSFProperty
     */
    public abstract String getPrefixText();
    
    /**
     * The text strength descriptions
     * 
     * @JSFProperty
     */
    public abstract String getTextStrengthDescriptions();
    
    /**
     * This string determines the expression of the custom security rule of the password
     * <p>
     * Note that the expression has the following format :
     * </p>
     * <p>
     * *******************************************************
     * </p>
     * <p>
     * S (Number)  N (Number) A (Number)
     * </p>
     * <ul>
     * <li>Where S stands for Symbols</li>
     * <li>Where N stands for Numbers</li>
     * <li>Where A stands for Alphabets</li>
     * </ul>
     * <p>
     * *******************************************************
     * </p>
     * <p>
     * For example) A4N2S3A2
     * Means that the password will be as following :
     * </p>
     * <ul>
     * <li>4 or more Alphabets followed by</li>
     * <li>2 or more Numbers followed by</li>
     * <li>3 or more Symbols followed by</li>
     * <li>2 or more Alphabets</li>
     * </ul>
     * <p>
     * *******************************************************
     * </p>
     * <p>
     * Note also that the useCustomSecurity should be set to true.
     * </p>
     * 
     * @JSFProperty
     */
    public abstract String getCustomSecurityExpression();

    /**
     * This flag determines whether to user custom security rules instead
     * of just depending on the password length. The default is false.
     * 
     * @JSFProperty
     */
    public abstract String getUseCustomSecurity();
    
    /**
     * This attribute determines the penalty ratio that will decrease the password 
     * Strength if the custom security expression is not met. Note also that the 
     * useCustomSecurity should be set to true to apply this flag. Possible values 
     * from 0 to 100. Default value is 50.
     * 
     * @JSFProperty
     */
    public abstract String getPenaltyRatio();
    
    /**
     * HTML: Specifies the horizontal alignment of this element. Deprecated in HTML 4.01.
     * 
     * @JSFProperty 
     */
    public abstract String getAlign();    
    
}
