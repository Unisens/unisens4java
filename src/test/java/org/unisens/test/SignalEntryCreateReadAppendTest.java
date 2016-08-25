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

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.BinFileFormat;
import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.DuplicateIdException;
import org.unisens.Endianess;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.ri.CsvFileFormatImpl;
import org.unisens.ri.XmlFileFormatImpl;

public class SignalEntryCreateReadAppendTest implements TestProperties{
	public static UnisensFactory factory;
	public static Unisens unisens;
	public static double sampleRate = 1000;
	public static Date timestampStart;
	public SignalEntry signalEntry;
	public BinFileFormat signalEntryFileFormat;
	public static byte[][] int8 = new byte[][]{{Byte.MIN_VALUE,Byte.MAX_VALUE},{-1,-1},{0,0},{1,1},{2,2}};
	public static byte[][] int8_n_1 = new byte[][]{{1, 2, 3}};
	public static byte[][] int8_1_1 = new byte[][]{{1}};
	public static short[][] uint8 = new short[][]{{0,0},{1,1},{2,2}, {3,3},{4,4}};
	public static short[][] int16 = new short[][]{{Short.MIN_VALUE,Short.MAX_VALUE},{-1,-1},{0,0},{1,1},{2,2}};
	public static int[][] uint16 = new int[][]{{0,0},{1,1},{2,2}, {3,3},{4,4}};
	public static int[][] int32 = new int[][]{{Integer.MIN_VALUE,Integer.MAX_VALUE},{-1,-1},{0,0},{1,1},{2,2}};
	public static long[][] uint32 = new long[][]{{0,0},{1,1},{2,2}, {3,3},{4,4}};
	public static int[][] uint32_1_1 = new int[][]{{-1}};
	public static float[][] float32 = new float[][]{{Float.MIN_VALUE,Float.MAX_VALUE},{-1,-1},{0,0},{1,1},{2,2}};
	public static double[][] double64 = new double[][]{{Double.MIN_VALUE, Double.MAX_VALUE},{-1,-1},{0.000000001005,0.6},{1,1},{2,2}};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if (new File(TEST_DEST).exists())
		{
			assertTrue(TestUtils.deleteRecursive(new File(TEST_DEST)));
		}
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(TEST_DEST);
		unisens.setTimestampStart(new Date());
		timestampStart = unisens.getTimestampStart();
		unisens.setMeasurementId("Temp signals");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
		unisens.save();
		if (new File(TEST_DEST).exists())
		{
			assertTrue(TestUtils.deleteRecursive(new File(TEST_DEST)));
		}
	}
	
	@Test
	public void testSignalEntry_INT8_LE() throws Exception, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int8_le.bin", new String[]{"a", "b"}, DataType.INT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(int8);
		
		assertArrayEqual(int8, (byte[][])signalEntry.read(int8.length));
	}
	
	@Test
	public void testSignalEntry_INT8_LE_N_CHANNELS_1_SAMPLE() throws Exception, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int8_le_n_1.bin", new String[]{"a", "b", "c"}, DataType.INT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(new byte[]{1, 2, 3});
		
		assertArrayEqual(int8_n_1, (byte[][])signalEntry.read(int8.length));
	}
	
	@Test
	public void testSignalEntry_INT8_LE_1_CHANNEL_1_SAMPLE() throws Exception, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int8_le_1_1.bin", new String[]{"a"}, DataType.INT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append((byte)1);
		
		assertArrayEqual(int8_1_1, (byte[][])signalEntry.read(int8.length));
	}
	
	@Test
	public void testSignalEntry_INT8_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int8_be.bin", new String[]{"a", "b"}, DataType.INT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(int8);
		
		assertArrayEqual(int8, (byte[][])signalEntry.read(int8.length));
	}
	
	@Test
	public void testSignalEntry_UINT8_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint8_le.bin", new String[]{"a", "b"}, DataType.UINT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(uint8);
		assertArrayEqual(uint8, (short[][])signalEntry.read(uint8.length));
	}
	
	@Test
	public void testSignalEntry_UINT8_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint8_be.bin", new String[]{"a", "b"}, DataType.UINT8, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(uint8);
		assertArrayEqual(uint8, (short[][])signalEntry.read(uint8.length));
	}
	
	
	@Test
	public void testSignalEntry_INT16_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int16_le.bin", new String[]{"a", "b"}, DataType.INT16, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(int16);
		assertArrayEqual(int16, (short[][])signalEntry.read(int16.length));
	}
	
	@Test
	public void testSignalEntry_INT16_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int16_be.bin", new String[]{"a", "b"}, DataType.INT16, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(int16);
		assertArrayEqual(int16, (short[][])signalEntry.read(int16.length));
	}
	
	@Test
	public void testSignalEntry_UINT16_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint16_le.bin", new String[]{"a", "b"}, DataType.UINT16, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(uint16);
		assertArrayEqual(uint16, (int[][])signalEntry.read(uint16.length));
	}
	
	@Test
	public void testSignalEntry_UINT16_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint16_be.bin", new String[]{"a", "b"}, DataType.UINT16, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(uint16);
		assertArrayEqual(uint16, (int[][])signalEntry.read(uint16.length));
	}
	
	@Test
	public void testSignalEntry_INT32_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int32_le.bin", new String[]{"a", "b"}, DataType.INT32, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(int32);
		assertArrayEqual(int32, (int[][])signalEntry.read(int32.length));
	}
	
	@Test
	public void testSignalEntry_INT32_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int32_be.bin", new String[]{"a", "b"}, DataType.INT32, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(int32);
		assertArrayEqual(int32, (int[][])signalEntry.read(int32.length));
	}
	
	@Test
	public void testSignalEntry_UINT32_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint32_le.bin", new String[]{"a", "b"}, DataType.UINT32, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(uint32);
		assertArrayEqual(uint32, (long[][])signalEntry.read(uint32.length));
	}
	
	@Test
	public void testSignalEntry_UINT32_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint32_be.bin", new String[]{"a", "b"}, DataType.UINT32, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(uint32);
		assertArrayEqual(uint32, (long[][])signalEntry.read(uint32.length));
	}
	
	@Test
	public void testSignalEntry_UINT32_1x1_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_uint32_1x1_le.bin", new String[]{"a"}, DataType.UINT32, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(uint32_1_1);
		assertArrayEqual(new long[][]{{4294967295L}}, (long[][])signalEntry.read(uint32_1_1.length));
	}
	
	@Test
	public void testSignalEntry_FLOAT_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_float_le.bin", new String[]{"a", "b"}, DataType.FLOAT, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(float32);
		assertArrayEqual(float32, (float[][])signalEntry.read(float32.length));
	}
	
	@Test
	public void testSignalEntry_FLOAT_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_float_be.bin", new String[]{"a", "b"}, DataType.FLOAT, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(float32);
		assertArrayEqual(float32, (float[][])signalEntry.read(float32.length));
	}
	
	@Test
	public void testSignalEntry_DOUBLE_LE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_double_le.bin", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.LITTLE);
		signalEntry.append(double64);
		assertArrayEqual(double64, (double[][])signalEntry.read(double64.length));
	}
	
	@Test
	public void testSignalEntry_DOUBLE_BE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_double_be.bin", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(double64);
		assertArrayEqual(double64, (double[][])signalEntry.read(double64.length));
	}
	
	@Test
	public void testSignalEntryCsv_TAB() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_tab.csv", new String[]{"a", "b"}, DataType.INT16, sampleRate);
		CsvFileFormat csvFileFormat = new CsvFileFormatImpl();
		csvFileFormat.setSeparator("\t");
		signalEntry.setFileFormat(csvFileFormat);
		signalEntry.append(int16);
		assertArrayEqual(int16, (short[][])signalEntry.read(int16.length));
	}
	
	@Test
	public void testSignalEntryCsv_DecimalSeparator() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_sep.csv", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		CsvFileFormat csvFileFormat = new CsvFileFormatImpl();
		csvFileFormat.setDecimalSeparator(",");
		signalEntry.setFileFormat(csvFileFormat);
		signalEntry.append(double64);
		assertArrayEqual(double64, (double[][])signalEntry.read(double64.length));
	}
	
	@Test
	public void testSignalEntryCsv_SHORT() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_short.csv", new String[]{"a", "b"}, DataType.INT16, sampleRate);
		signalEntry.setFileFormat(new CsvFileFormatImpl());
		signalEntry.append(int16);
		assertArrayEqual(int16, (short[][])signalEntry.read(int16.length));
	}
	
	@Test
	public void testSignalEntryCsv_INT() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int.csv", new String[]{"a", "b"}, DataType.INT32, sampleRate);
		signalEntry.setFileFormat(new CsvFileFormatImpl());
		signalEntry.append(int32);
		assertArrayEqual(int32, (int[][])signalEntry.read(int32.length));
	}
	
	@Test
	public void testSignalEntryCsv_DOUBLE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_double.csv", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		signalEntry.setFileFormat(new CsvFileFormatImpl());
		signalEntry.append(double64);
		this.assertArrayEqual(double64, (double[][])signalEntry.read(double64.length));
	}
	
	@Test
	public void testSignalEntryXml_SHORT() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_short.xml", new String[]{"a", "b"}, DataType.INT16, sampleRate);
		signalEntry.setFileFormat(new XmlFileFormatImpl());
		signalEntry.append(int16);
		assertArrayEqual(int16, (short[][])signalEntry.read(int16.length));
	}
	
	@Test
	public void testSignalEntryXml_INT() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_int.xml", new String[]{"a", "b"}, DataType.INT32, sampleRate);
		signalEntry.setFileFormat(new XmlFileFormatImpl());
		signalEntry.append(int32);
		assertArrayEqual(int32, (int[][])signalEntry.read(int32.length));
	}
	
	@Test
	public void testSignalEntryXml_DOUBLE() throws IOException, DuplicateIdException{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_double.xml", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		signalEntry.setFileFormat(new XmlFileFormatImpl());
		signalEntry.append(double64);
		this.assertArrayEqual(double64, (double[][])signalEntry.read(double64.length));
	}
	
	@Test
	public void testSaveUnisens() throws Exception{
		signalEntry = (SignalEntry)unisens.createSignalEntry("se_double_be2.bin", new String[]{"a", "b"}, DataType.DOUBLE, sampleRate);
		signalEntryFileFormat = (BinFileFormat)signalEntry.getFileFormat();
		signalEntryFileFormat.setEndianess(Endianess.BIG);
		signalEntry.append(double64);
		unisens.closeAll();
		unisens.save();
		
		unisens = factory.createUnisens(TEST_DEST);
		assertEquals(timestampStart.toString(), unisens.getTimestampStart().toString());
		signalEntry = (SignalEntry)unisens.getEntry("se_double_be2.bin");
		assertEquals("BIN", signalEntry.getFileFormat().getFileFormatName());
		assertEquals(DataType.DOUBLE, signalEntry.getDataType());
		assertEquals(sampleRate, signalEntry.getSampleRate(), 0);
		assertTrue(signalEntry.getFileFormat() instanceof BinFileFormat);
		assertEquals(Endianess.BIG, ((BinFileFormat)signalEntry.getFileFormat()).getEndianess());
	}
	
	
	private void assertArrayEqual(byte[][] expected, byte[][] actual){
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i]);
		}
	}
	
	private void assertArrayEqual(short[][] expected, short[][] actual){
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i]);
		}
	}
	
	private void assertArrayEqual(int[][] expected, int[][] actual){
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i]);
		}
	}
	
	private void assertArrayEqual(long[][] expected, long[][] actual){
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i]);
		}
	}
	
	private void assertArrayEqual(float[][] expected, float[][] actual){
		assertEquals(expected.length, actual.length);
		assertEquals(expected[0].length, actual[0].length);
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[0].length; j++) {
				assertEquals(expected[i][j], actual[i][j], 0.001);
			}
		}
	}
	
	private void assertArrayEqual(double[][] expected, double[][] actual){
		assertEquals(expected.length, actual.length);
		assertEquals(expected[0].length, actual[0].length);
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[0].length; j++) {
				assertEquals(expected[i][j], actual[i][j], 0.001);
			}
		}
	}
}
