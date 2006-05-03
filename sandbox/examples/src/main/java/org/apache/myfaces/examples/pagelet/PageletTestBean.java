package org.apache.myfaces.examples.pagelet;



/**
 * @author Thomas Obereder
 * @version Date: 05.11.2005 17:07:32
 */
public class PageletTestBean
{
    //@SuppressWarnings({"FieldCanBeLocal"})
    private String                 _text        = "This is an exemple";
    private String                 _text2       = "";
    private String                 _text3       = "Example text 3";
    private String                 _text4       = "Example text 4";
    private String                 _text5       = "Example text 5"; 
    private String 				   _text7		= "Example text 7";
    

    public PageletTestBean()
    {
    }

    public String getText()
    {
        return _text;
    }

    public void setText(String text)
    {
        _text = text;
    }




    public String getText2()
    {
        return _text2;
    }

    public void setText2(String text2)
    {
        this._text2 = text2;
    }

    public String doaction()
    {
        return "success";
    }

    public String getText3()
    {
        return _text3;
    }

    public void setText3(String text3)
    {
        this._text3 = text3;
    }

    public String getText4()
    {
        return _text4;
    }

    public void setText4(String text4)
    {
        this._text4 = text4;
    }

    public String getText5()
    {
        return _text5;
    }

    public void setText5(String text5)
    {
        this._text5 = text5;
    }

	public String getText7() {
		return _text7;
	}

	public void setText7(String text7) {
		this._text7 = text7;
	}
}
