package org.apache.myfaces.convert;

import java.util.concurrent.atomic.AtomicLong;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import junit.framework.TestCase;

public class AtomicLongConverterTest extends TestCase{

	public void testGetAsObject() {
		
		Converter converter = new AtomicLongConverter();
		
		assertNull( converter.getAsObject(null, null, null) );
		assertNull( converter.getAsObject(null, null, "") );
		assertNull( converter.getAsObject(null, null, " ") );
		assertTrue( 8 == ((AtomicLong) converter.getAsObject(null, null, " 8")).longValue() );
		assertTrue( 8 == ((AtomicLong) converter.getAsObject(null, null, "8 ")).longValue() );
		assertTrue( 8 == ((AtomicLong) converter.getAsObject(null, null, "8")).longValue() );
		long over = Long.MAX_VALUE + 1;
		assertTrue( over == ((AtomicLong) converter.getAsObject(null, null, over + "")).longValue() );
		long under = Long.MIN_VALUE - 1;
		assertTrue( under == ((AtomicLong) converter.getAsObject(null, null, under + "")).longValue() );
		
		try {
			
			converter.getAsObject(null, null, "NaN");
			
			fail("should only take numbers");
			
		}catch(ConverterException c) { }
		
	}
	
	public void testGetAsString() {

		Converter converter = new AtomicLongConverter();

		assertEquals("", converter.getAsString(null, null, null));
		assertEquals("", converter.getAsString(null, null, ""));
		assertEquals(" ", converter.getAsString(null, null, " "));
		assertEquals("-1", converter.getAsString(null, null, new AtomicLong(-1)));

		try {

			converter.getAsString(null, null, new Long(0));

			fail("should only take atomic ints");

		}catch(ConverterException c) { }		
	}
}