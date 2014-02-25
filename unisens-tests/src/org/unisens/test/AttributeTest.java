package org.unisens.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.Entry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.Value;
import org.unisens.ValuesEntry;

public class AttributeTest implements TestProperties
{
	public static UnisensFactory factory;
	public static Unisens unisens;
	
	public static String entryId = "ve_int16.csv";
	public static String key = "TestKey";
	public static String value = "Test Value";


	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		File unisensPath = new File(EXAMPLE_TEMP_VALUES_ENTRY);
		if (unisensPath.exists())
		{
			for (File file : unisensPath.listFiles())
				if (file.isFile())
					assertTrue(file.delete());
		}
		else
			assertTrue(unisensPath.mkdirs());
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(EXAMPLE_TEMP_VALUES_ENTRY);
		
		ValuesEntry valuesEntry = unisens.createValuesEntry(entryId, new String[] {
				"a", "b" }, DataType.INT16, 400);
		valuesEntry.setName("Values int 16");
		
		unisens.setTimestampStart(new Date());
		unisens.setMeasurementId("Temp values");
		unisens.closeAll();
		unisens.save();
		
		unisens = factory.createUnisens(EXAMPLE_TEMP_VALUES_ENTRY);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		unisens.closeAll();
		unisens.save();
	}

	@Test
	public void testCustomAttributes() throws Exception
	{
		Entry entry = unisens.getEntry(entryId);
		entry.addCustomAttribute(key, value);
		unisens.save();
		unisens.closeAll();
		
		unisens = factory.createUnisens(EXAMPLE_TEMP_VALUES_ENTRY);
		entry = unisens.getEntry(entryId);
		HashMap<String, String> customAttributes = entry.getCustomAttributes();
		
		assertEquals(value, customAttributes.get(key));
	}
}
