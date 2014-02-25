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
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.DataType;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;

public class ValuesEntryImpTest implements TestProperties{
	public static ValuesEntry valuesEntry;	
	public static ValuesEntry tempValuesEntry;
	public static Unisens unisens;
	public static UnisensFactory factory;
	public static Value[] expectedArray;
	public static ValueList expectedValueList;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(EXAMPLE2);
		valuesEntry = (ValuesEntry)unisens.getEntry("bloodpressure.csv");
		expectedArray = new Value[]{new Value(0, new short[]{123, 82}), new Value(600, new short[]{124, 87}), new Value(1200, new short[]{130, 67}), new Value(1800, new short[]{118, 78}), new Value(2400, new short[]{142, 92})};
		expectedValueList = new ValueList();
		expectedValueList.setSamplestamps(new long[]{0, 600, 1200, 1800, 2400});
		expectedValueList.setData(new short[][]{{123, 82}, {124, 87}, {130, 67}, {118, 78}, {142, 92}});
		tempValuesEntry = unisens.createValuesEntry("temp_values.csv", new String[]{"a", "b"}, DataType.INT16, 400);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
	}


	@Test
	public void testRead() throws IOException{
		valuesEntry.resetPos();
		Value[] actuals = valuesEntry.read(5);
		assertArrayEquals(expectedArray, actuals);
	}
	
	@Test
	public void testResetPos() throws IOException{
		valuesEntry.resetPos();
		Value[] expecteds = valuesEntry.read(5);
		valuesEntry.resetPos();
		assertArrayEquals(expecteds, valuesEntry.read(5));
	}
	

	@Test
	public void testReadLongInt() throws IOException{
		Value[] actuals = valuesEntry.read(0, 5);
		assertArrayEquals(expectedArray, actuals);
		Value[] actuals01 = valuesEntry.read(0, 2);
		Value[] actuals234 = valuesEntry.read(2, 5);
		Value[] actuals01234 = new Value[]{actuals01[0], actuals01[1], actuals234[0], actuals234[1], actuals234[2]};
		assertArrayEquals(expectedArray, actuals01234);
	}

	@Test
	public void testReadValuesListInt() throws IOException{
		valuesEntry.resetPos();
		ValueList actualValueList = valuesEntry.readValuesList(5);
		assertEquals(expectedValueList, actualValueList);
	}
	
	
	@Test
	public void testReadValuesListLongInt() throws IOException{
		ValueList actualValueList = valuesEntry.readValuesList(0, 5);
		assertEquals(expectedValueList, actualValueList);
	}

	@Test
	public void testAppendValue() throws IOException{
		Value expected = new Value(100, new short[]{5, 10});
		tempValuesEntry.append(expected);
		Value[] actuals = tempValuesEntry.read(tempValuesEntry.getCount() - 1, 1);
		assertEquals(expected, actuals[0]);
	}

	@Test
	public void testAppendMultipleValues() throws IOException{
		Value expected1 = new Value(100, new short[]{5, 10});
		Value expected2 = new Value(500, new short[]{50, 10});
		Value expected3 = new Value(1000, new short[]{5, 100});
		tempValuesEntry.append(expected1);
		tempValuesEntry.append(expected2);
		tempValuesEntry.append(expected3);
		Value[] actuals = tempValuesEntry.read(tempValuesEntry.getCount() - 3, 3);
		assertEquals(expected1, actuals[0]);
		assertEquals(expected2, actuals[1]);
		assertEquals(expected3, actuals[2]);
	}

	@Test
	public void testAppendValueArray() throws IOException{
		tempValuesEntry.append(expectedArray);
		Value[] actuals = tempValuesEntry.read(tempValuesEntry.getCount() - expectedArray.length, expectedArray.length);
		assertArrayEquals(expectedArray, actuals);
		unisens.deleteEntry(tempValuesEntry);
	}

	@Test
	public void testAppendValuesList() throws IOException{
		tempValuesEntry.appendValuesList(expectedValueList);
		ValueList actuals = tempValuesEntry.readValuesList(tempValuesEntry.getCount() - expectedValueList.getSamplestamps().length, expectedValueList.getSamplestamps().length);
		assertEquals(expectedValueList, actuals);
		unisens.deleteEntry(tempValuesEntry);
	}

	@Test
	public void testClone() {
		ValuesEntry clonedValuesEntry = (ValuesEntry)valuesEntry.clone();
		assertEquals(valuesEntry.getAdcResolution(), clonedValuesEntry.getAdcResolution());
		assertEquals(valuesEntry.getAdcZero(), clonedValuesEntry.getAdcZero());
		assertEquals(valuesEntry.getBaseline(), clonedValuesEntry.getBaseline());
		assertEquals(valuesEntry.getComment(), clonedValuesEntry.getComment());
		assertEquals(valuesEntry.getContentClass(), clonedValuesEntry.getContentClass());
		assertEquals(valuesEntry.getDataType(), clonedValuesEntry.getDataType());
		assertEquals(valuesEntry.getFileFormat().getFileFormatName(), clonedValuesEntry.getFileFormat().getFileFormatName());
		assertEquals(valuesEntry.getFileFormat().getComment(), clonedValuesEntry.getFileFormat().getComment());
		assertEquals(valuesEntry.getId(), clonedValuesEntry.getId());
		assertEquals(valuesEntry.getLsbValue(), clonedValuesEntry.getLsbValue(), 0);
		assertEquals(valuesEntry.getSampleRate(), clonedValuesEntry.getSampleRate(), 0);
		assertEquals(valuesEntry.getSource(), clonedValuesEntry.getSource());
		assertEquals(valuesEntry.getUnisens(), clonedValuesEntry.getUnisens());
		assertEquals(valuesEntry.getUnit(), clonedValuesEntry.getUnit());
		assertEquals(valuesEntry.getChannelCount(), clonedValuesEntry.getChannelCount());
		assertArrayEquals(valuesEntry.getChannelNames(), clonedValuesEntry.getChannelNames());
	}
}
