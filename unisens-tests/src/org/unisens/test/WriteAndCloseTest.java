package org.unisens.test;

import org.junit.*;
import org.unisens.*;
import java.io.File;

import static org.junit.Assert.assertTrue;

public class WriteAndCloseTest extends TestBase {

	@Test
	public void testWriteAndClose() throws Exception {
		
		for (int i=0;i<1000;i++)
		{
			UnisensFactory factory = UnisensFactoryBuilder.createFactory();
			Unisens unisens = factory.createUnisens(TEST_DEST);
			SignalEntry signalEntry = unisens.createSignalEntry("testWriteClose.bin", new String[]{"testCh1"}, DataType.DOUBLE, 100); 
			signalEntry.setFileFormat(signalEntry.createBinFileFormat());
			signalEntry.append(new double[][]{{Double.MIN_VALUE, Double.MAX_VALUE},{-1,-1},{0.000000001005,0.6},{1,1},{2,2}});
			signalEntry.close();
			
			//can we access the file an delete it?
			boolean deleted = new File(unisens.getPath()+ System.getProperty("file.separator") + "testWriteClose.bin").delete();
			assertTrue(deleted);
		}	
	}
}
