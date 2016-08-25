package org.unisens.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.unisens.*;

import java.io.File;
import java.util.List;

public class EmptyEventEntryReadTest extends TestBase {

	@Test
	public void testReadEmptyEventEntry() throws Exception {
		
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(TEST_DEST);
		EventEntry eventEntry = unisens.createEventEntry("testEvents.bin", 100);

		assertTrue(new File(TEST_DEST+ System.getProperty("file.separator") + "testEvents.bin").exists());
		
		List<org.unisens.Event> eventList =eventEntry.read(100);

		unisens.save();
		unisens.closeAll();
	}
	

}
