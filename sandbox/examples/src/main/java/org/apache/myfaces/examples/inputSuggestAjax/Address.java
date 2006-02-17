package org.apache.myfaces.examples.inputSuggestAjax;

/**
 * @author Gerald Muellan
 *         Date: 12.02.2006
 *         Time: 23:30:40
 */
public class Address
{
    private int _streetNumber;
    private String _streetName;
    private String _city;
    private int _zip;

    public Address(int streetNumber,
                   String streetName,
                   String city,
                   int zip)
    {
        _streetNumber = streetNumber;
        _streetName = streetName;
        _city = city;
        _zip = zip;
    }

    public int getStreetNumber()
    {
        return _streetNumber;
    }

    public void setStreetNumber(int streetNumber)
    {
        _streetNumber = streetNumber;
    }

    public String getStreetName()
    {
        return _streetName;
    }

    public void setStreetName(String streetName)
    {
        _streetName = streetName;
    }

    public String getCity()
    {
        return _city;
    }

    public void setCity(String city)
    {
        _city = city;
    }

    public int getZip()
    {
        return _zip;
    }

    public void setZip(int zip)
    {
        _zip = zip;
    }
}
