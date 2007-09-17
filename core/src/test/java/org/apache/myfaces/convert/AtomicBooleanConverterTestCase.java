package org.apache.myfaces.convert;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import junit.framework.TestCase;

public class AtomicBooleanConverterTestCase extends TestCase {

	public void testGetAsObject() {
		
		Converter converter = new AtomicBooleanConverter();
		
		assertNull(converter.getAsObject(null, null, null));
		assertNull(converter.getAsObject(null, null, ""));
		assertNull(converter.getAsObject(null, null, " "));
		assertTrue(((AtomicBoolean)converter.getAsObject(null, null, "true")).get());
		assertTrue(((AtomicBoolean)converter.getAsObject(null, null, "true ")).get());
		assertTrue(((AtomicBoolean)converter.getAsObject(null, null, " true")).get());
		assertFalse(((AtomicBoolean)converter.getAsObject(null, null, "false")).get());
		assertFalse(((AtomicBoolean)converter.getAsObject(null, null, "false ")).get());
		assertFalse(((AtomicBoolean)converter.getAsObject(null, null, " false")).get());
		assertFalse(((AtomicBoolean)converter.getAsObject(null, null, " boom ")).get());
		
	}
	
	public void testGetAsString() {
		
		Converter converter = new AtomicBooleanConverter();
		
		assertEquals("", converter.getAsString(null, null, null));
		assertEquals("", converter.getAsString(null, null, ""));
		assertEquals("true", converter.getAsString(null, null, new AtomicBoolean(true)));
		assertEquals("false", converter.getAsString(null, null, new AtomicBoolean(false)));
		
		try {
			
			converter.getAsString(null, null, new Boolean(true) );
			
			fail();
			
		}catch(ConverterException c) {	}
		
	}
	
}
