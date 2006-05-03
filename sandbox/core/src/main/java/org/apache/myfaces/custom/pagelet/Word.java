package org.apache.myfaces.custom.pagelet;

/**
 * @author Thomas Spiegl
 */
public class Word {
    boolean          checkSpelling;
    int              _originalPosition = 0;
    private String[] _alternatives;
    private boolean  _invalid;
    private String   _value;

    Word(String value, boolean checkSpelling, int originalPosition) {
        _value                = value;
        this.checkSpelling    = checkSpelling;
        this._originalPosition = originalPosition;
    }

    public String[] getAlternatives() {
        return _alternatives;
    }

    public int getOriginalPosition() {
        return _originalPosition;
    }

    public String getValue() {
        return _value;
    }

    public boolean isInvalid() {
        return _invalid;
    }

    public void setAlternatives(String[] alternatives) {

        if (!checkSpelling)
            throw new IllegalArgumentException("checkSpelling if false");

        _alternatives = alternatives;
    }

    public void setInvalid(boolean invalid) {

        if (!checkSpelling)
            throw new IllegalArgumentException("checkSpelling if false");

        _invalid = invalid;
    }

    public void setOriginalPosition(int originalPosition) {
        this._originalPosition = originalPosition;
    }
}
