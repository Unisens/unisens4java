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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.DataType;
import org.unisens.DuplicateIdException;
import org.unisens.Entry;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;

public class SignalEntryImplTest implements TestProperties{
	public static Unisens unisens;
	public static SignalEntry signalEntry;
	public static UnisensFactory factory;
	public static Unisens unisens3;
	public static Unisens unisensUint;
	public static SignalEntry signalEntry3;
	public static SignalEntry signalEntryUint8_2x12;
	public static SignalEntry signalEntryUint16_2x12;
	public static SignalEntry signalEntryUint32_2x12;
	public static SignalEntry signalEntryUint8_1x12;
	public static SignalEntry signalEntryUint16_1x12;
	public static SignalEntry signalEntryUint32_1x12;
	public static SignalEntry signalEntryUint8_1x1;
	public static SignalEntry signalEntryUint16_1x1;
	public static SignalEntry signalEntryUint32_1x1;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(EXAMPLE1);
		signalEntry = (SignalEntry)unisens.getEntry("ecg.bin");
		unisens3 = factory.createUnisens(EXAMPLE3);
		signalEntry3 = (SignalEntry)unisens3.getEntry("test.bin");
		unisensUint = factory.createUnisens(EXAMPLE_UNIT);
		signalEntryUint8_2x12 = (SignalEntry)unisensUint.getEntry("test_2x12_UINT8.bin");
		signalEntryUint16_2x12 = (SignalEntry)unisensUint.getEntry("test_2x12_UINT16.bin");
		signalEntryUint32_2x12 = (SignalEntry)unisensUint.getEntry("test_2x12_UINT32.bin");
		signalEntryUint8_1x12 = (SignalEntry)unisensUint.getEntry("test_1x12_UINT8.bin");
		signalEntryUint16_1x12 = (SignalEntry)unisensUint.getEntry("test_1x12_UINT16.bin");
		signalEntryUint32_1x12 = (SignalEntry)unisensUint.getEntry("test_1x12_UINT32.bin");
		signalEntryUint8_1x1 = (SignalEntry)unisensUint.getEntry("test_1x1_UINT8.bin");
		signalEntryUint16_1x1 = (SignalEntry)unisensUint.getEntry("test_1x1_UINT16.bin");
		signalEntryUint32_1x1 = (SignalEntry)unisensUint.getEntry("test_1x1_UINT32.bin");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
	}
	
	@Test
	public void testReadUint8_1x1() throws IOException{
		signalEntryUint8_1x1.resetPos();
		Object data = signalEntryUint8_1x1.read(1);
		assertTrue(data instanceof short[][]);
		short[][] data1 = (short[][])data;
		short[][] expected = new short[][]{{255}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint16_1x1() throws IOException{
		signalEntryUint16_1x1.resetPos();
		Object data = signalEntryUint16_1x1.read(1);
		assertTrue(data instanceof int[][]);
		int[][] data1 = (int[][])data;
		
		int[][] expected = new int[][]{{65535}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint32_1x1() throws IOException{
		signalEntryUint32_1x1.resetPos();
		Object data = signalEntryUint32_1x1.read(1);
		assertTrue(data instanceof long[][]);
		long[][] data1 = (long[][])data;
		
		long[][] expected = new long[][]{{4294967295L}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint8_1x12() throws IOException{
		signalEntryUint8_1x12.resetPos();
		Object data = signalEntryUint8_1x12.read(12);
		assertTrue(data instanceof short[][]);
		short[][] data1 = (short[][])data;
		
		short[][] expected = new short[][]{{255}, {0}, {127}, {255},{255},{255}, {255}, {255}, {0}, {0}, {0}, {123}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint16_1x12() throws IOException{
		signalEntryUint16_1x12.resetPos();
		Object data = signalEntryUint16_1x12.read(12);
		assertTrue(data instanceof int[][]);
		int[][] data1 = (int[][])data;
		
		int[][] expected = new int[][]{{65535}, {0}, {127}, {32767}, {65535}, {255}, {65535}, {65535}, {0}, {0}, {0}, {123}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint32_1x12() throws IOException{
		signalEntryUint32_1x12.resetPos();
		Object data = signalEntryUint32_1x12.read(12);
		assertTrue(data instanceof long[][]);
		long[][] data1 = (long[][])data;
	
		long[][] expected = new long[][]{{4294967295L}, {0}, {127}, {32767}, {2147483647}, {255}, {65535}, {4294967295L}, {0}, {0}, {0}, {123}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint8_2x12() throws IOException{
		signalEntryUint8_2x12.resetPos();
		Object data = signalEntryUint8_2x12.read(12);
		assertTrue(data instanceof short[][]);
		short[][] data1 = (short[][])data;
		
		short[][] expected = new short[][]{{255, 0}, {0, 0}, {127,0}, {255, 0},{255, 123},{255, 127}, {255, 255}, {255, 255}, {0, 255}, {0, 255}, {0, 255}, {123, 255}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint16_2x12() throws IOException{
		signalEntryUint16_2x12.resetPos();
		Object data = signalEntryUint16_2x12.read(12);
		assertTrue(data instanceof int[][]);
		int[][] data1 = (int[][])data;
		
		int[][] expected = new int[][]{{65535, 0}, {0, 0}, {127,0},{32767,0},{65535, 123},{255, 127}, {65535, 255}, {65535, 32767}, {0, 65535}, {0, 65535}, {0, 65535}, {123, 65535}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}
	
	@Test
	public void testReadUint32_2x12() throws IOException{
		signalEntryUint32_2x12.resetPos();
		Object data = signalEntryUint32_2x12.read(12);
		assertTrue(data instanceof long[][]);
		long[][] data1 = (long[][])data;
	
		long[][] expected = new long[][]{{4294967295L, 0}, {0, 0}, {127,0}, {32767, 0}, {2147483647, 123}, {255, 127}, {65535, 255}, {4294967295L, 32767}, {0, 65535}, {0, 2147483647}, {0, 4294967295L}, {123, 4294967295L}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}

	@Test
	public void testRead() throws IOException{
		signalEntry.resetPos();
		Object data = signalEntry.read(10);
		assertTrue(data instanceof int[][]);
		int[][] data1 = (int[][])data;
		int[][] expected = new int[][]{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{-5,5},{0,-5},{5,26},{42,80}};
		for(int i = 0 ; i < data1.length ; i++)
			assertArrayEquals(expected[i], data1[i]);
	}

	@Test
	public void testResetPos() throws IOException{
		signalEntry.resetPos();
		int[][] expected = new int[][]{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{-5,5},{0,-5},{5,26},{42,80}};
		int[][] data = (int[][])signalEntry.read(10);
		for(int i = 0 ; i < data.length ; i++)
			assertArrayEquals(expected[i], data[i]);
	}
	@Test
	public void testReadPos() throws IOException{
		signalEntry.resetPos();
		signalEntry.read(10);
		int[][] expected = (int[][])signalEntry.read(10);
		signalEntry.resetPos();
		int[][] actuals = (int[][])signalEntry.read(10, 10);
		
		for(int i = 0 ; i < expected.length ; i++)
			assertArrayEquals(expected[i], actuals[i]);
	}
	
	@Test
	public void testReadPos2() throws IOException{
		signalEntry.resetPos();
		int[][] expected = (int[][])signalEntry.read(100000);
		signalEntry.resetPos();
		for (int i = 0; i < expected.length; i += 5000){
			int[][] data = (int[][]) signalEntry.read(i,1);
			assertArrayEquals(expected[i], data[0]);
		}	
	}
	
	@Test
	public void testReadPos3() throws IOException{
		short[][] expected = (short[][])signalEntry3.read((int)signalEntry3.getCount()/100);
		signalEntry3.resetPos();
		for (int i = 0; i < expected.length; i++){
			short[][] data = (short[][]) signalEntry3.read(i*100,1);
			assertArrayEquals(expected[i], data[0]);
		}	
	}

	@Test
	public void testAppend() throws DuplicateIdException, IOException{
		SignalEntry signalEntry = unisens.createSignalEntry("temp", new String[]{"c1","c2"}, DataType.INT32, 400);
		int[][] expecteds = new int[][]{{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{-5,5},{0,-5},{5,26},{42,80}};
		signalEntry.append(expecteds);
		int[][] actuals = (int[][])signalEntry.read(expecteds.length);
		assertArrayEquals(expecteds, actuals);
		unisens.deleteEntry(signalEntry);
	}
	
	@Test
	public void testAppendCsvDimm1()throws DuplicateIdException, IOException{
		SignalEntry signalEntry = unisens.createSignalEntry("temp1.csv", new String[]{"c1"}, DataType.INT32, 400);
		signalEntry.setFileFormat(signalEntry.createCsvFileFormat());
		int[] data = {0,0,0,0,0,0,-5,0,5,42};
		int[][] expecteds = new int[][]{{0},{0},{0},{0},{0},{0},{-5},{0},{5},{42}};
		signalEntry.append(data);
		int[][] actuals = (int[][])signalEntry.read(expecteds.length);
		assertArrayEquals(expecteds, actuals);
		unisens.deleteEntry(signalEntry);
	}
	
	@Test
	public void testCustomAttributes(){
		Entry entry = unisens.getEntry("ecg.bin");
		HashMap<String, String> customEntryAttrs = entry.getCustomAttributes();
		assertTrue(customEntryAttrs != null);
		assertEquals(2, customEntryAttrs.size());
		assertTrue(customEntryAttrs.get("customEntryKey1").equalsIgnoreCase("customEntryValue1"));
		assertTrue(customEntryAttrs.get("customEntryKey2").equalsIgnoreCase("customEntryValue2"));
	}
	

	@Test
	public void testClone() {
		SignalEntry clonedSignalEntry = (SignalEntry)signalEntry.clone();
		assertEquals(signalEntry.getAdcResolution(), clonedSignalEntry.getAdcResolution());
		assertEquals(signalEntry.getAdcZero(), clonedSignalEntry.getAdcZero());
		assertEquals(signalEntry.getBaseline(), clonedSignalEntry.getBaseline());
		assertEquals(signalEntry.getComment(), clonedSignalEntry.getComment());
		assertEquals(signalEntry.getContentClass(), clonedSignalEntry.getContentClass());
		assertEquals(signalEntry.getDataType(), clonedSignalEntry.getDataType());
		assertEquals(signalEntry.getFileFormat().getFileFormatName(), clonedSignalEntry.getFileFormat().getFileFormatName());
		assertEquals(signalEntry.getFileFormat().getComment(), clonedSignalEntry.getFileFormat().getComment());
		assertEquals(signalEntry.getId(), clonedSignalEntry.getId());
		assertEquals(signalEntry.getLsbValue(), clonedSignalEntry.getLsbValue(), 0);
		assertEquals(signalEntry.getSampleRate(), clonedSignalEntry.getSampleRate(), 0);
		assertEquals(signalEntry.getSource(), clonedSignalEntry.getSource());
		assertEquals(signalEntry.getUnisens(), clonedSignalEntry.getUnisens());
		assertEquals(signalEntry.getUnit(), clonedSignalEntry.getUnit());
		assertEquals(signalEntry.getChannelCount(), clonedSignalEntry.getChannelCount());
		assertArrayEquals(signalEntry.getChannelNames(), clonedSignalEntry.getChannelNames());
	}	
	
}
