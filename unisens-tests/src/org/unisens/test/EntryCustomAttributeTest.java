package org.unisens.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import org.junit.Test;
import org.unisens.Entry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.UnisensParseException;

public class EntryCustomAttributeTest extends TestBase
{

	public static String entryId = "ecg.bin";
	public static String key = "TestKey";
	public static String value = "Test Value";

	@Test
	public void testEntryCustomAttributes() throws UnisensParseException, IOException
	{
		String path = TestUtils.copyTestData(EXAMPLE1);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		Entry entry = unisens.getEntry(entryId);
		entry.addCustomAttribute(key, value);
		unisens.save();
		unisens.closeAll();
		
		unisens = factory.createUnisens(path);
		entry = unisens.getEntry(entryId);
		HashMap<String, String> customAttributes = entry.getCustomAttributes();
		
		assertEquals(value, customAttributes.get(key));
	}
}
