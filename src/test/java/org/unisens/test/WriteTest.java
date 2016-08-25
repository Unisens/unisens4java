package org.unisens.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.DuplicateIdException;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.UnisensParseException;
import org.unisens.Value;
import org.unisens.ValuesEntry;

public class WriteTest extends TestBase {

	private static String datasetPath = TEST_DEST + System.getProperty("file.separator") + "writeTest";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File unisensPath = new File(datasetPath);
		if(unisensPath.exists()){
			for(File file : unisensPath.listFiles())
				if(file.isFile())
					assertTrue(file.delete());
		}
		else
			assertTrue(unisensPath.mkdirs());
	}
	
	@Test
	public void testWrite()
	{
		try {
			signalCsvDouble();
			signalBin();
			signalCsv();
			signalXml();
			
			valuesBin();
			valuesCsv();
			valuesXml();
			
			eventBin();
			eventCsv();
			eventXml();
		} catch (UnisensParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void signalBin() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		SignalEntry se = u.createSignalEntry("signal.bin", new String[]{"A", "B"}, DataType.INT16, 250);
		short[][] A = new short[][]{{1, 2, 3}, { 4, 5, 6}};
		se.append(A);
		u.save();
		u.closeAll();
	}
	
	private void signalXml() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		SignalEntry se = u.createSignalEntry("signal.xml", new String[]{"A", "B"}, DataType.INT16, 250);
		short[][] A = new short[][]{{1, 2, 3}, { 4, 5, 6}};
		se.setFileFormat(se.createXmlFileFormat());
		se.append(A);
		u.save();
		u.closeAll();
	}
	
	private void signalCsv() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		SignalEntry se = u.createSignalEntry("signal.csv", new String[]{"A", "B"}, DataType.INT16, 250);
		short[][] A = new short[][]{{1, 2, 3}, { 4, 5, 6}};
		CsvFileFormat cff = se.createCsvFileFormat();
		cff.setSeparator(";");
		cff.setDecimalSeparator(",");
		se.setFileFormat(cff);
		se.append(A);
		u.save();
		u.closeAll();
	}
	
	private void signalCsvDouble() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		SignalEntry se = u.createSignalEntry("signalD.csv", new String[]{"A", "B"}, DataType.DOUBLE, 250);
		double[][] A = new double[][]{{1.3, 2.5, 3.2}, { 4.3, 5.0, 6.123456}};
		CsvFileFormat cff = se.createCsvFileFormat();
		cff.setSeparator(";");
		cff.setDecimalSeparator(",");
		se.setFileFormat(cff);
		se.append(A);
		u.save();
		u.closeAll();
	}
	
	private void valuesCsv() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		ValuesEntry ve = u.createValuesEntry("values.csv", new String[]{"A", "B"}, DataType.INT16, 250);
		CsvFileFormat cff = ve.createCsvFileFormat();
		cff.setSeparator(";");
		cff.setDecimalSeparator(",");
		ve.setFileFormat(cff);
		ve.append(new Value(1320, new short[]{1, 4}));
		ve.append(new Value(22968, new short[]{2, 5}));
		ve.append(new Value(30232, new short[]{3, 6}));
		u.save();
		u.closeAll();
	}
	
	private void valuesBin() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		ValuesEntry ve = u.createValuesEntry("values.bin", new String[]{"A", "B"}, DataType.INT16, 250);
		ve.setFileFormat(ve.createBinFileFormat());
		ve.append(new Value(1320, new short[]{1, 4}));
		ve.append(new Value(22968, new short[]{2, 5}));
		ve.append(new Value(30232, new short[]{3, 6}));
		u.save();
		u.closeAll();
	}
	
	private void valuesXml() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		ValuesEntry ve = u.createValuesEntry("values.xml", new String[]{"A", "B"}, DataType.INT16, 250);
		ve.setFileFormat(ve.createXmlFileFormat());
		ve.append(new Value(1320, new short[]{1, 4}));
		ve.append(new Value(22968, new short[]{2, 5}));
		ve.append(new Value(30232, new short[]{3, 6}));
		u.save();
		u.closeAll();
	}
	
	private void eventCsv() throws UnisensParseException, DuplicateIdException, IOException 
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		EventEntry ee = u.createEventEntry("event.csv", 1000);
		CsvFileFormat cff = ee.createCsvFileFormat();
		cff.setSeparator(";");
		cff.setDecimalSeparator(".");
		ee.setFileFormat(cff);
		ee.append(new Event(124, "N", "NORMAL"));
		ee.append(new Event(346, "N", "NORMAL"));
		ee.append(new Event(523, "V", "PVC"));
		u.save();
		u.closeAll();
	}
	
	private void eventXml() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		EventEntry ee = u.createEventEntry("event.xml", 1000);
		ee.setFileFormat(ee.createXmlFileFormat());
		ee.append(new Event(124, "N", "NORMAL"));
		ee.append(new Event(346, "N", "NORMAL"));
		ee.append(new Event(523, "V", "PVC"));
		u.save();
		u.closeAll();
	}
	
	private void eventBin() throws UnisensParseException, DuplicateIdException, IOException
	{
		UnisensFactory uf = UnisensFactoryBuilder.createFactory();
		Unisens u = uf.createUnisens(datasetPath);
		EventEntry ee = u.createEventEntry("event.bin", 1000);
		ee.setFileFormat(ee.createBinFileFormat());
		ee.setCommentLength(6);
		ee.setTypeLength(1);
		ee.append(new Event(124, "N", "NORMAL"));
		ee.append(new Event(346, "N", "NORMAL"));
		ee.append(new Event(523, "V", "PVC   "));
		u.save();
		u.closeAll();
	}	
}
