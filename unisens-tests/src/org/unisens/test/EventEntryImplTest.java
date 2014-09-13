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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;

public class EventEntryImplTest implements TestProperties{
	public static EventEntry eventEntry;	
	public static Unisens unisens;
	public static UnisensFactory factory;
	public static List<Event> expected;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if (new File(TEST_DEST).exists())
		{
			assertTrue(TestUtils.deleteRecursive(new File(TEST_DEST)));
		}
		String path = TestUtils.copyTestData(EXAMPLE2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		unisens = factory.createUnisens(path);
		
		eventEntry = (EventEntry)unisens.getEntry("events.csv");
		expected = new ArrayList<Event>();
		expected.add(new Event(30, "A1", "liegen"));
		expected.add(new Event(500, "A2", "sitzen"));
		expected.add(new Event(1343, "A3", "stehen"));
		expected.add(new Event(3200, "A4", "gehen"));
		expected.add(new Event(5600, "A5", "treppehoch"));
		expected.add(new Event(5600, "A6", ""));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		unisens.closeAll();
		TestUtils.deleteRecursive(new File(TEST_DEST));
	}

	@Test
	public void testRead() throws IOException{
		assertEquals(expected.subList(0, 6), eventEntry.read(0, 6));
		assertEquals(expected.subList(3, 6), eventEntry.read(3, 6));
	}

	@Test
	public void testAppendEvent() throws IOException{
		Event newEvent = new Event(6000, "A7", "liegen");
		expected.add(newEvent);
		eventEntry.append(newEvent);
		assertEquals(expected, eventEntry.read(0,7));
		expected.remove(newEvent);
		eventEntry.empty();
		eventEntry.append(expected);
		assertEquals(expected, eventEntry.read(0, 6));
	}

	@Test
	public void testClone() {
		EventEntry clonedEventEntry = (EventEntry)eventEntry.clone();
		assertEquals(eventEntry.getTypeLength(), clonedEventEntry.getTypeLength());
		assertEquals(eventEntry.getCommentLength(), clonedEventEntry.getCommentLength());
		assertEquals(eventEntry.getFileFormat().getFileFormatName(), clonedEventEntry.getFileFormat().getFileFormatName());
		assertEquals(eventEntry.getFileFormat().getComment(), clonedEventEntry.getFileFormat().getComment());
		assertEquals(eventEntry.getId(), clonedEventEntry.getId());
		assertEquals(eventEntry.getSampleRate(), clonedEventEntry.getSampleRate(), 0);
		assertEquals(eventEntry.getSource(), clonedEventEntry.getSource());
		assertEquals(eventEntry.getUnisens(), clonedEventEntry.getUnisens());
	}

}
