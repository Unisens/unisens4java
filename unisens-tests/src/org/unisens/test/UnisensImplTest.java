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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.CustomEntry;
import org.unisens.DataType;
import org.unisens.DuplicateIdException;
import org.unisens.Entry;
import org.unisens.EventEntry;
import org.unisens.Group;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.ValuesEntry;

public class UnisensImplTest implements TestProperties{
	public static Unisens unisens1;
	public static Unisens unisens2;
	public static UnisensFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		factory = UnisensFactoryBuilder.createFactory();
		unisens1 = factory.createUnisens(EXAMPLE1);
		unisens2 = factory.createUnisens(EXAMPLE2);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens1.closeAll();
		unisens2.closeAll();
	}

	/**
	 * Test method for {@link org.unisens1.ri.unisens1Impl#getCustomAttributes()}.
	 */
	@Test
	public void testGetCustomAttributes() {
		HashMap<String, String> customAttributes = new HashMap<String, String>();
		customAttributes.put("customKey1", "customValue1");
		customAttributes.put("customKey2", "customValue2");
		assertEquals(customAttributes, unisens1.getCustomAttributes());
	}

	/**
	 * Test method for {@link org.unisens1.ri.unisens1Impl#createSignalEntry(java.lang.String, java.lang.String[], org.unisens1.DataType, double)}.
	 */
	@Test
	public void testCreateSignalEntry() throws DuplicateIdException{
		SignalEntry signalEntry = unisens1.createSignalEntry("signalEntry.bin", new String[]{"channel A", "channel B"}, DataType.INT32, 1000);
		assertNotNull(unisens1.getEntry("signalEntry.bin"));
		assertArrayEquals(new String[]{"channel A", "channel B"}, signalEntry.getChannelNames());
		assertEquals(DataType.INT32, signalEntry.getDataType());
		assertEquals(1000, signalEntry.getSampleRate(), 0);
		assertEquals(2, signalEntry.getChannelCount());
		assertNotNull(signalEntry.getUnisens());
	}

	@Test
	public void testCreateGroup() throws DuplicateIdException{
		Group group = unisens1.createGroup("group1");
		assertEquals(0, group.getEntries().size());
		assertEquals(unisens1.getGroup("group1"), group);
	}
	

	@Test
	public void testCreateEventEntry() throws DuplicateIdException{
		EventEntry eventEntry = unisens1.createEventEntry("eventEntry.csv", 400);
		assertNotNull(unisens1.getEntry("eventEntry.csv"));
		assertEquals("eventEntry.csv", eventEntry.getId());
		assertEquals(400, eventEntry.getSampleRate(), 0);
		assertNotNull(eventEntry.getUnisens());
	}

	@Test
	public void testCreateValuesEntry() throws DuplicateIdException{
		ValuesEntry valuesEntry = unisens1.createValuesEntry("valuesEntry.csv", new String[]{"channel 1" , "channel 2"}, DataType.DOUBLE, 400);
		assertNotNull(unisens1.getEntry("valuesEntry.csv"));
		assertArrayEquals(new String[]{"channel 1", "channel 2"}, valuesEntry.getChannelNames());
		assertEquals(DataType.DOUBLE, valuesEntry.getDataType());
		assertEquals(400, valuesEntry.getSampleRate(), 0);
		assertEquals(2, valuesEntry.getChannelCount());
		assertNotNull(valuesEntry.getUnisens());
	}

	@Test
	public void testCreateCustomEntry() throws DuplicateIdException{
		CustomEntry customEntry = unisens1.createCustomEntry("customEntry.txt");
		assertNotNull(unisens1.getEntry("customEntry.txt"));
		assertEquals("customEntry.txt", customEntry.getId());
		assertNotNull(customEntry.getUnisens());
		unisens1.deleteEntry(customEntry);
		assertNull(unisens1.getEntry("customEntry.txt"));
	}

	@Test
	public void testAddGroup() throws DuplicateIdException{
		Group group2 = unisens2.getGroups().get(0);
		Group group1 = unisens1.addGroup(group2, true);
		
		assertNotNull(unisens1.getGroup(group2.getId()));
		assertEquals(group2.getComment(), group1.getComment());
		for(Entry entry : group2.getEntries())
			assertNotNull(unisens1.getEntry(entry.getId()));
	}


	@Test
	public void testAddEntry() throws DuplicateIdException{
		Entry entry = unisens2.createEventEntry("eventEntry2.csv", 400);
		unisens1.addEntry(entry , true);
		assertNotNull(unisens1.getEntry(entry.getId()));
		assertEquals(entry.getFileFormat().getFileFormatName(), unisens1.getEntry(entry.getId()).getFileFormat().getFileFormatName());
	}

	@Test
	public void testGetEntry() {
		assertNotNull(unisens1.getEntry("ecg.bin"));
		assertNull(unisens1.getEntry("12321"));
	}

	@Test
	public void testDeleteEntry() {
		List<Entry> entries = new ArrayList<Entry>(unisens1.getGroup("group2").getEntries());
		for(Entry entry : entries){
			assertNotNull(unisens1.getEntry(entry.getId()));
			unisens1.deleteEntry(entry);
			assertNull(unisens1.getEntry(entry.getId()));
		}
		assertNotNull(unisens1.getEntry("eventEntry.csv"));
		unisens1.deleteEntry(unisens1.getEntry("eventEntry.csv"));
		assertNull(unisens1.getEntry("eventEntry.csv"));
		assertFalse(new File(unisens1.getPath() + System.getProperty("file.separator") + "eventEntry.csv").exists());
		
		assertNotNull(unisens1.getEntry("eventEntry2.csv"));
		unisens1.deleteEntry(unisens1.getEntry("eventEntry2.csv"));
		assertNull(unisens1.getEntry("eventEntry2.csv"));
		assertFalse(new File(unisens1.getPath() + System.getProperty("file.separator") + "eventEntry2.csv").exists());
		
		assertNotNull(unisens2.getEntry("eventEntry2.csv"));
		unisens2.deleteEntry(unisens2.getEntry("eventEntry2.csv"));
		assertNull(unisens2.getEntry("eventEntry2.csv"));
		assertFalse(new File(unisens2.getPath() + System.getProperty("file.separator") + "eventEntry2.csv").exists());
		
		assertNotNull(unisens1.getEntry("valuesEntry.csv"));
		unisens1.deleteEntry(unisens1.getEntry("valuesEntry.csv"));
		assertNull(unisens1.getEntry("valuesEntry.csv"));
		assertFalse(new File(unisens1.getPath() + System.getProperty("file.separator") + "valuesEntry.csv").exists());
		
		
		assertNotNull(unisens1.getEntry("signalEntry.bin"));
		unisens1.deleteEntry(unisens1.getEntry("signalEntry.bin"));
		assertNull(unisens1.getEntry("signalEntry.bin"));
		assertFalse(new File(unisens1.getPath() + System.getProperty("file.separator") + "signalEntry.bin").exists());
		
	}
	
	
	@Test
	public void testDeleteGroup() {
		assertNotNull(unisens1.getGroup("group1"));
		unisens1.deleteGroup(unisens1.getGroup("group1"));
		assertNull(unisens1.getGroup("group1"));
		
		assertNotNull(unisens1.getGroup("group2"));
		unisens1.deleteGroup(unisens1.getGroup("group2"));
		assertNull(unisens1.getGroup("group2"));
	}


	@Test
	public void testSave() {
		//fail("Not yet implemented");
	}
}
