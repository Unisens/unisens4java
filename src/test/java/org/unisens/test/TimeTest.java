package org.unisens.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.unisens.ZonedTimestamp;
import org.unisens.Unisens;
import org.unisens.UnisensFactory;
import org.unisens.UnisensFactoryBuilder;
import org.unisens.UnisensParseException;
import org.unisens.ri.util.Utilities;

import org.junit.Test;


public class TimeTest extends TestBase {

	@Test
	public void testZonedTimeStampLocal() {
		
		Date utc = new Date(1568814060352L);
		String timeZoneString="Europe/Berlin";
		String localTimeAsString="2019-09-18T15:41:00.352";

		String localTimeStringTest = new ZonedTimestamp(utc, timeZoneString).format();
		
		assertEquals(localTimeAsString,  localTimeStringTest);
		
	}
	
	@Test
	public void testZonedTimeStampUtc() {
		
		Date utc = new Date(1568814060352L);
		String utcString="2019-09-18T13:41:00.352Z";
		String utcStringTest = new ZonedTimestamp(utc).formatUtc();;
		assertEquals(utcString,  utcStringTest);
	}
	
	@Test
	public void testConvertIso8601StringToDateUTC() {
		Date utc = new Date(1568814060352L);
		String timeZoneString="UTC";
		String utcString="2019-09-18T13:41:00.352Z";
		Date testDate = Utilities.convertIso8601StringToDate(utcString, timeZoneString);
		assertEquals(utc,  testDate);
	}
	
	@Test
	public void testConvertIso8601StringToDateLocal() {
		Date utc = new Date(1568814060352L);
		String timeZoneString="Europe/Berlin";
		String localTimeString="2019-09-18T15:41:00.352";
		Date testDate = Utilities.convertIso8601StringToDate(localTimeString, timeZoneString);
		assertEquals(utc,  testDate);
	}
	
	@Test
	public void testCheckLocalTime() {
		Date utc = new Date(1568814060352L);
		
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Berlin");
		ZonedTimestamp zonedTimestamp = new ZonedTimestamp(utc, timeZone);
		
		String localTimeString="2019-09-18T15:41:00.352";
		assertTrue(Utilities.checkLocalTime(zonedTimestamp, localTimeString));

		localTimeString="2019-09-18T15:41:00";
		assertTrue(Utilities.checkLocalTime(zonedTimestamp, localTimeString));
		
		localTimeString="2019-09-18T17:41:00";
		assertFalse(Utilities.checkLocalTime(zonedTimestamp, localTimeString));
	}
	
	@Test
	public void testTimeFromUnisensUtc() throws UnisensParseException {
		
		String path = TestUtils.copyTestData(TEST_TIME1);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		Date utc = new Date(1568814060352L);
		String timeZoneString="Europe/Berlin";
		String localTimeString="2019-09-18T15:41:00.352";
		
		ZonedTimestamp zonedTimestamp = unisens.getZonedTimestampStart();
		assertEquals(utc, zonedTimestamp.getDate());
		assertEquals(timeZoneString, zonedTimestamp.getTimeZone().getID());
		assertEquals(localTimeString, zonedTimestamp.format());
	}
	
	@Test
	public void testOldFunctionWithNewUnisensDataset() throws UnisensParseException 
	{
		String path = TestUtils.copyTestData(TEST_TIME1);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		String localTimeString="2019-09-18T15:41:00.352";
		
		String testTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(unisens.getTimestampStart());
		
		assertEquals(localTimeString, testTimeString);
	}
	
	@Test
	public void testOldFunctionWithNewUnisensDatasetForeignTimeZone() throws UnisensParseException 
	{
		String path = TestUtils.copyTestData(TEST_TIME6);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		String localTimeString="2019-09-19T02:41:00.352";

		String testTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(unisens.getTimestampStart());
		
		assertEquals(localTimeString, testTimeString);
	}
	
	@Test
	public void testTimeFromUnisensStandard() throws UnisensParseException {
		
		String path = TestUtils.copyTestData(TEST_TIME2);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		String localTimeString="2019-09-18T15:41:00.352";
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String timeString = simpleDateFormat.format(unisens.getTimestampStart());
		
		assertEquals(localTimeString, timeString);
		assertNull(unisens.getTimeZone());
	}
	
	@Test
	public void testWriteReadTimestampOldFunctions() throws UnisensParseException, IOException 
	{
		String path = TestUtils.copyTestData(TEST_TIME7);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		Date now = new Date();
		
		unisens.setTimestampStart(now);
		unisens.save();
		
		unisens = factory.createUnisens(path);
		
		assertEquals(now, unisens.getTimestampStart());
		
	}
	
	@Test
	public void testWriteReadTimestampNewFunctions() throws UnisensParseException, IOException 
	{
		String path = TestUtils.copyTestData(TEST_TIME7);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		Date now = new Date();
		unisens.setTimestampStart(now);
		unisens.save();
		
		unisens = factory.createUnisens(path);
		
		assertEquals(now, unisens.getZonedTimestampStart().getDate());
		assertNull(unisens.getZonedTimestampStart().getTimeZone());
		assertFalse(unisens.getZonedTimestampStart().getUtcValid());
	}
	
	
	
	@Test
	public void testWriteOldReadNewTimestampFunctions() throws UnisensParseException, IOException 
	{
		String path = TestUtils.copyTestData(TEST_TIME7);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
		
		Date now = new Date();
		String timeZoneString = "America/Havana";
		unisens.setZonedTimestampStart(new ZonedTimestamp(now, timeZoneString));
		unisens.save();
		
		unisens = factory.createUnisens(path);
		
		assertEquals(now, unisens.getZonedTimestampStart().getDate());
		assertEquals(timeZoneString, unisens.getZonedTimestampStart().getTimeZone().getID());
		
	}
	

	@Test(expected = UnisensParseException.class)
	public void testTimeFromUnisensInconsistent() throws UnisensParseException {
		
		String path = TestUtils.copyTestData(TEST_TIME3);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);
	}
	
	@Test
	public void testTimeFromUnisensBeforeDSTEnds() throws UnisensParseException {
		
		String path = TestUtils.copyTestData(TEST_TIME4);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);

		Date utc = new Date(1572136200000L);
		String localTimeString="2019-10-27T02:30:00.000"; // Sun Oct 27 2019 00:30:00 UTC
		String timeZoneString="Europe/Berlin";
		
		ZonedTimestamp zonedTimestamp = unisens.getZonedTimestampStart();
		
		assertEquals(timeZoneString, zonedTimestamp.getTimeZone().getID());		
		String timeString = zonedTimestamp.format();
		assertEquals(localTimeString, timeString);
		assertEquals(utc, zonedTimestamp.getDate());
	}
	
	@Test
	public void testTimeFromUnisensAfterDSTEnds() throws UnisensParseException {
		
		String path = TestUtils.copyTestData(TEST_TIME5);
		UnisensFactory factory = UnisensFactoryBuilder.createFactory();
		Unisens unisens = factory.createUnisens(path);

		Date utc = new Date(1572139800000L);
		String localTimeString="2019-10-27T02:30:00.000"; // Sun Oct 27 2019 01:30:00 TC
		String timeZoneString="Europe/Berlin";
		
		ZonedTimestamp zonedTimestamp = unisens.getZonedTimestampStart();
		
		assertEquals(timeZoneString, zonedTimestamp.getTimeZone().getID());		
		String timeString = zonedTimestamp.format();
		assertEquals(localTimeString, timeString);
		assertEquals(utc, zonedTimestamp.getDate());
	}
	
	
	String formatDate(Date date, String format, String timeZoneString)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		if (timeZoneString!=null)
		{
			TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
			simpleDateFormat.setTimeZone(timeZone);
		}
		return simpleDateFormat.format(date);
	}
	
}
