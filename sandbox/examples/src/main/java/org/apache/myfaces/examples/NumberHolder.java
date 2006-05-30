package org.apache.myfaces.examples;

import java.math.BigDecimal;

public class NumberHolder
{
	private short shortNumber;
	private int intNumber;
	private long longNumber;
	private double doubleNumber;
	private BigDecimal bigDecimalNumber;
	private double doubleNumberManual;
	
	public BigDecimal getBigDecimalNumber()
	{
		return bigDecimalNumber;
	}
	public void setBigDecimalNumber(BigDecimal bigDecimalNumber)
	{
		this.bigDecimalNumber = bigDecimalNumber;
	}
	public double getDoubleNumber()
	{
		return doubleNumber;
	}
	public void setDoubleNumber(double doubleNumber)
	{
		this.doubleNumber = doubleNumber;
	}
	public int getIntNumber()
	{
		return intNumber;
	}
	public void setIntNumber(int intNumber)
	{
		this.intNumber = intNumber;
	}
	public long getLongNumber()
	{
		return longNumber;
	}
	public void setLongNumber(long longNumber)
	{
		this.longNumber = longNumber;
	}
	public short getShortNumber()
	{
		return shortNumber;
	}
	public void setShortNumber(short shortNumber)
	{
		this.shortNumber = shortNumber;
	}
	public double getDoubleNumberManual()
	{
		return doubleNumberManual;
	}
	public void setDoubleNumberManual(double doubleNumberManual)
	{
		this.doubleNumberManual = doubleNumberManual;
	}
}
