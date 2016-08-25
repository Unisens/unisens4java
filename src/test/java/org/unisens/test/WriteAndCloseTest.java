package org.unisens.test;

import org.junit.*;
import org.unisens.*;

import static org.junit.Assert.assertTrue;

public class WriteAndCloseTest extends TestBase {

	/**
	 * This test is disabled because it causes a Exception on Windows:
	Caused by: java.io.IOException: Zugriff verweigert
	at java.io.WinNTFileSystem.createFileExclusively(Native Method)
	at java.io.File.createNewFile(File.java:1012)
	at org.unisens.ri.io.BufferedFileWriter.<init>(BufferedFileWriter.java:55)
	at org.unisens.ri.io.bin.SignalBinWriter.open(SignalBinWriter.java:47)
	at org.unisens.ri.io.bin.SignalBinWriter.<init>(SignalBinWriter.java:43)
	
	See: http://stackoverflow.com/questions/32583049/creating-new-files-concurrently
	 */
	//@Test
	public void testWriteAndClose() throws Exception {
		
		for (int i=0;i<1000;i++)
		{
			UnisensFactory factory = UnisensFactoryBuilder.createFactory();
			Unisens unisens = factory.createUnisens(TEST_DEST);
			SignalEntry signalEntry = unisens.createSignalEntry("testWriteClose.bin", new String[]{"testCh1"}, DataType.DOUBLE, 100); 
			signalEntry.setFileFormat(signalEntry.createBinFileFormat());
			signalEntry.append(new double[][]{{Double.MIN_VALUE, Double.MAX_VALUE},{-1,-1},{0.000000001005,0.6},{1,1},{2,2}});
			signalEntry.close();
			unisens.deleteEntry(signalEntry);

			System.out.println("Iteration: " + i);
		}	
	}
}
