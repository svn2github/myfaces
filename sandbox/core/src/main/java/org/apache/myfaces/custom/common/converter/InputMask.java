package org.apache.myfaces.custom.common.converter;

import javax.faces.context.FacesContext;

/**
 * interface to allow a converter to send code which converts a string to object and vice versa
 * on the client
 *
 * @author imario@apache.org
 */
public interface InputMask
{
    /**
     * A regular expression to check if a character is allowed. The expression will be used on the client.
     * <br />
     * Example:
     * <code>
     * Numerics only: [0-9]
     * Date: [0-9\\-]
     * E-Mail: [a-zA-Z0-9\\.@]
     * </code>
     * As you can see, this is not a full blown regular expression to check if the input is valid. It is
     * more a check to see if the "to be entered" character is allowed.<br />
     * The validation check itself will be done by the client converter or client validator.
     *
     * @return the regular expression
     */
    public String getInputPattern();

    /**
     * A human readable string about the allowed input
     * <br />
     * Example:
     * <code>
     * Numeric input only
     * Date in Format: DD-MM-YYYY
     * Email allows letters, digits, point and @ only
     * </code>
     *
     * @return the human readable string
     */
    public String getDescription();
}
