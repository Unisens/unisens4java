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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.CustomEntry;
import org.unisens.CustomFileFormat;
import org.unisens.FileFormat;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.UnisensParseException;
import org.unisens.ri.config.Constants;


public class CustomEntryImplTest extends TestBase {

	@Test
	public void testGetAttribute() throws UnisensParseException {
		String path = TestUtils.copyTestData(EXAMPLE2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		CustomEntry customEntry = (CustomEntry)unisens.getEntry("entry.custom");
		
		assertEquals("customAttrValue1", customEntry.getAttribute("customAttr1"));
		assertNull(customEntry.getAttribute("unexpectedAttr"));
		assertNull(customEntry.getAttribute(Constants.ENTRY_CONTENTCLASS));
		assertNull(customEntry.getAttribute(Constants.ENTRY_ID));
		assertNull(customEntry.getAttribute(Constants.ENTRY_SOURCE));
		assertNull(customEntry.getAttribute(Constants.ENTRY_SOURCE_ID));
		assertNull(customEntry.getAttribute(Constants.ENTRY_COMMENT));
	}

	@Test
	public void testGetAttributes() throws UnisensParseException {
		String path = TestUtils.copyTestData(EXAMPLE2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		CustomEntry customEntry = (CustomEntry)unisens.getEntry("entry.custom");
		
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("customAttr1", "customAttrValue1");
		expected.put("customAttr2", "customAttrValue2");
		HashMap<String, String> actual = customEntry.getAttributes();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetFileFormat() throws UnisensParseException {
		String path = TestUtils.copyTestData(EXAMPLE2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		CustomEntry customEntry = (CustomEntry)unisens.getEntry("entry.custom");
		
		FileFormat fileFormat = customEntry.getFileFormat();
		assertTrue(fileFormat instanceof CustomFileFormat);
	}

	@Test
	public void testClone() throws UnisensParseException {
		String path = TestUtils.copyTestData(EXAMPLE2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		CustomEntry customEntry = (CustomEntry)unisens.getEntry("entry.custom");
		
		CustomEntry clonedEntry = (CustomEntry)customEntry.clone();
		assertEquals(customEntry.getId(), clonedEntry.getId());
		assertEquals(customEntry.getComment(), clonedEntry.getComment());
		assertEquals(customEntry.getContentClass(), clonedEntry.getContentClass());
		assertEquals(customEntry.getSource(), clonedEntry.getSource());
		assertEquals(customEntry.getSourceId(), clonedEntry.getSourceId());
		assertEquals(customEntry.getFileFormat().getFileFormatName(), clonedEntry.getFileFormat().getFileFormatName());
		assertEquals(customEntry.getAttributes(), clonedEntry.getAttributes());
	}

}
