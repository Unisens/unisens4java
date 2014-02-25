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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.BinFileFormat;
import org.unisens.CsvFileFormat;
import org.unisens.Endianess;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.ri.BinFileFormatImpl;
import org.unisens.ri.XmlFileFormatImpl;

public class EventEntryCreateReadAppendTest implements TestProperties {
	public static UnisensFactory factory;
	public static Unisens unisens;
	public static double sampleRate = 1000;
	public static Date timestampStart;
	
	public EventEntry eventEntry;
	public static List<Event> events = new ArrayList<Event>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File unisensPath = new File(EXAMPLE_TEMP_EVENT_ENTRY);
		if(unisensPath.exists()){
			for(File file : unisensPath.listFiles())
				if(file.isFile())
					assertTrue(file.delete());
		}
		else
			assertTrue(unisensPath.mkdirs());
		factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(EXAMPLE_TEMP_EVENT_ENTRY);
		timestampStart = new Date();
		unisens.setTimestampStart(timestampStart);
		unisens.setMeasurementId("Temp events");
		events.add(new Event(30, "A1", "liegen"));
		events.add(new Event(500, "A2", "sitzen"));
		events.add(new Event(1343, "A3", "stehen"));
		events.add(new Event(3200, "A4", "gehen"));
		events.add(new Event(5600, "A5", "treppe hoch"));
		events.add(new Event(5600, "A6", ""));
		events.add(new Event(Integer.MAX_VALUE-1, "A7", ""));
		events.add(new Event(0, "A8", ""));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
		unisens.save();
	}
	
	@Test
	public void testEventEntry_BIN()throws Exception{
		eventEntry = unisens.createEventEntry("ee.bin", sampleRate);
		eventEntry.setTypeLength(2);
		eventEntry.setCommentLength(11);
		eventEntry.setFileFormat(new BinFileFormatImpl());
		eventEntry.append(events);
		assertEventListEquals(events, eventEntry.read(events.size()));
	}
	
	@Test
	public void testEventEntry_CSV()throws Exception{
		eventEntry = unisens.createEventEntry("ee.csv", sampleRate);
		((CsvFileFormat)eventEntry.getFileFormat()).setSeparator("\t");
		eventEntry.append(events);
		assertEventListEquals(events, eventEntry.read(events.size()));
	}
	
	@Test
	public void testEventEntry_XML()throws Exception{
		eventEntry = unisens.createEventEntry("ee.xml", sampleRate);
		eventEntry.setFileFormat(new XmlFileFormatImpl());
		eventEntry.append(events);
		assertEventListEquals(events, eventEntry.read(events.size()));
	}
	
	@Test
	public void testSaveUnisens() throws Exception{
		unisens.closeAll();
		unisens.save();
		unisens = factory.createUnisens(EXAMPLE_TEMP_EVENT_ENTRY);
		assertEquals(timestampStart.toString(), unisens.getTimestampStart().toString());
		eventEntry = (EventEntry)unisens.getEntry("ee.bin");
		assertEquals("BIN", eventEntry.getFileFormat().getFileFormatName());
		assertEquals(sampleRate, eventEntry.getSampleRate(), 0);
		assertTrue(eventEntry.getFileFormat() instanceof BinFileFormat);
		assertEquals(Endianess.LITTLE, ((BinFileFormat)eventEntry.getFileFormat()).getEndianess());
	}
	
	public void assertEventListEquals(List<Event>  expected, List<Event> actual){
		assertTrue(expected.size() == actual.size());
		for(int i = 0 ; i < expected.size() ; i++)
			assertTrue(expected.get(i).equals(actual.get(i)));
	}
}
