/*
Unisens Tests - tests for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV),
				   KIT, Germany

This file is part of the Unisens Tests. For more information, see
<http://www.unisens.org>

The Unisens Tests is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Tests is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Tests. If not, see <http://www.gnu.org/licenses/>. 
*/

package org.unisens.test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.XmlFileFormatImpl;
import org.unisens.ri.CsvFileFormatImpl;

public class ValuesEntryCreateReadAppendTest implements TestProperties{
	public static UnisensFactory factory;
	public static Unisens unisens;
	public ValuesEntry valuesEntry;
	public CsvFileFormat valuesEntryFileFormat;
	public Value[] int16 = {new Value(100, new short[]{1 , 1}), new Value(200, new short[]{2 , 2}), new Value(300, new short[]{3 , 3}), new Value(400, new short[]{4 , 4}), new Value(500, new short[]{5 , 5})};
	public Value[] int32 = {new Value(100, new int[]{1 , 1}), new Value(200, new int[]{2 , 2}), new Value(300, new int[]{3 , 3}), new Value(400, new int[]{4 , 4}), new Value(500, new int[]{5 , 5})};
	public ValueList int32_1_dim = new ValueList(new long[]{100, 200, 300, 400, 500}, new int[]{1, 2, 3, 4, 5});;
	public Value[] double64 = {new Value(100, new double[]{1.1 , 1.1}), new Value(200, new double[]{2.2 , 2.2}), new Value(300, new double[]{3.3 , 3.3}), new Value(400, new double[]{4.4 , 4.4}), new Value(500, new double[]{5.5 , 5.5})};
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File unisensPath = new File(EXAMPLE_TEMP_VALUES_ENTRY);
		if(unisensPath.exists()){
			for(File file : unisensPath.listFiles())
				if(file.isFile())
					assertTrue(file.delete());
		}
		else
			assertTrue(unisensPath.mkdirs());
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(EXAMPLE_TEMP_VALUES_ENTRY);
		unisens.setTimestampStart(new Date());
		unisens.setMeasurementId("Temp values");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
		unisens.save();
	}
	
	@Test
	public void testValuesEntry_INT16()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_int16.csv", new String[]{"a", "b"}, DataType.INT16, 400);
		valuesEntry.setName("Values int 16");
		valuesEntry.append(int16);
		assertArrayEquals(int16, valuesEntry.read(5));
	}
	
	@Test
	public void testValuesEntry_INT32()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_int32.csv", new String[]{"a", "b"}, DataType.INT32, 400);
		valuesEntry.append(int32);
		assertArrayEquals(int32, valuesEntry.read(5));
	}
	
	@Test
	public void testValuesEntry_INT32_1_DIM()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_int32_1_dim.csv", new String[]{"a"}, DataType.INT32, 400);
		valuesEntry.appendValuesList(int32_1_dim);
		assertTrue(int32_1_dim.equals(valuesEntry.readValuesList(5)));
	}
	
	@Test
	public void testValuesEntry_DOUBLE()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_double.csv", new String[]{"a", "b"}, DataType.DOUBLE, 400);
		CsvFileFormat cff = valuesEntry.createCsvFileFormat();
		cff.setSeparator(";");
		cff.setDecimalSeparator(",");
		valuesEntry.setFileFormat(cff);
		valuesEntry.append(double64);
		assertArrayEquals(double64, valuesEntry.read(5));
	}
	
	@Test
	public void testValuesEntry_XML_INT16()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_int16.xml", new String[]{"a", "b"}, DataType.INT16, 400);
		valuesEntry.setFileFormat(new XmlFileFormatImpl());
		valuesEntry.append(int16);
		assertArrayEquals(int16, valuesEntry.read(5));
	}
	
	@Test
	public void testValuesEntry_XML_INT32()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_int32.xml", new String[]{"a", "b"}, DataType.INT32, 400);
		valuesEntry.setFileFormat(new XmlFileFormatImpl());
		valuesEntry.append(int32);
		assertArrayEquals(int32, valuesEntry.read(5));
	}
	
	@Test
	public void testValuesEntry_XML_DOUBLE()throws Exception{
		valuesEntry = unisens.createValuesEntry("ve_double.xml", new String[]{"a", "b"}, DataType.DOUBLE, 400);
		valuesEntry.setFileFormat(new XmlFileFormatImpl());
		valuesEntry.append(double64);
		assertArrayEquals(double64, valuesEntry.read(5));
	}

}
