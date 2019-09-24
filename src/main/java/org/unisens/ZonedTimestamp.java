/*
Unisens Library - library for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV), KIT, Germany
                   movisens GmbH, Karlsruhe

This file is part of the Unisens Library. For more information, see
<http://www.unisens.org>

The Unisens Library is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Library. If not, see <http://www.gnu.org/licenses/>. 
 */

package org.unisens;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * ZonedTimestamp hold a timestamp and a time zone. 
 * 
 * @author Joerg Ottenbacher
 * @author Malte Kirst
 *
 */
public class ZonedTimestamp {
	
	Date timestamp = null;
	TimeZone timeZone = null;
	boolean isUtcValid = false;
	
	/**
	 * ZonedTimestamp Contructor
	 * 
	 * @param timestamp the timestamp as UTC
	 * @param timeZone the time zone corresponding to this timestamp, set to null if time zone is unknown
	 */
	public ZonedTimestamp(Date timestamp, TimeZone timeZone ) {
		this.timestamp = timestamp;
		this.timeZone = timeZone;
		if (timestamp != null & timeZone != null) {
			isUtcValid = true;
		}
	}

	/**
	 * ZonedTimestamp Contructor
	 * 
	 * @param timestamp the timestamp as UTC
	 * @param timeZone the time zone corresponding to this timestamp as string, e.g. "Europe/Berlin"
	 */
	public ZonedTimestamp(Date timestamp, String timeZoneString ) {
		this(timestamp, TimeZone.getTimeZone(timeZoneString));
	}
	
	/**
	 * ZonedTimestamp Contructor
	 * 
	 * @param timestamp the timestamp as UTC. The time zone will be set to the default time zone
	 */
	public ZonedTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		timeZone = TimeZone.getDefault();
		isUtcValid = false;
	}

	/**
	 * Gets the time zone of this ZonedTimestamp
	 * 
	 * @return the time zone of this ZonedTimestamp
	 */
	public TimeZone getTimeZone()
	{
		return timeZone;
	}
	
	/**
	 * Gets the time zone of this ZonedTimestamp
	 * 
	 * @return the time zone of this ZonedTimestamp
	 */
	public Date getDate()
	{
		return timestamp;
	}

	/**
	 * Gets the if the timestamp of this ZonedTimestamp is a valid UTC Date
	 * 
	 * @return true if the timstamp is a valid UTC Date
	 */
	public boolean getUtcValid()
	{
		return this.isUtcValid;
	}
	
	/**
	 * Format the timestamp with a given format. Use the set time zone or use the default time zone of the system if not set.
	 * 
	 * @param format a format like "yyyy-MM-dd' 'HH:mm:ss"
	 */
	public String format(String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		if (timeZone!=null)
		{
			simpleDateFormat.setTimeZone(timeZone);
		}
		return simpleDateFormat.format(timestamp);
	}
	
	/**
	 * Format the timestamp with a predefined format "yyyy-MM-dd'T'HH:mm:ss.SSS". Use the set time zone or use the default time zone of the system if not set.
	 * 
	 */	
	public String format()
	{
		return format("yyyy-MM-dd'T'HH:mm:ss.SSS");
	}

	/**
	 * Format the timestamp as UTC time with a given format.
	 * 
	 * @param format a format like "yyyy-MM-dd' 'HH:mm:ss"
	 */	
	public String formatUtc(String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		TimeZone timeZoneUtc = TimeZone.getTimeZone("UTC");
		simpleDateFormat.setTimeZone(timeZoneUtc);
		return simpleDateFormat.format(timestamp);
	}
	
	/**
	 * Format the timestamp as UTC time with a predefined format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". 
	 * 
	 */	
	public String formatUtc()
	{
		return formatUtc("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}
	
}
