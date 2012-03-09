/*
 * DayCounterDiv.java
 *
 * Copyright (c) 2006, C. Dean Whitaker
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THIS COMMON PUBLIC LICENSE
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
 * CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 *
 * You can obtain a current copy of the Common Public License from
 * http://www.ibm.com/developerworks/library/os-cpl.html
 */
package alreadyblog.guibeans;

import java.util.GregorianCalendar;

import alreadyblog.util.GUIConstants;

public class DayCounterDiv implements GuiBeanDiv {

	public String getDiv() {
		GregorianCalendar nowGC = new GregorianCalendar();
		GregorianCalendar thenGC = new GregorianCalendar();
		thenGC.set (GregorianCalendar.YEAR, 2000);
		thenGC.set (GregorianCalendar.MONTH, 0);
		thenGC.set (GregorianCalendar.DAY_OF_MONTH, 31);

		long nowLong = nowGC.getTimeInMillis();
		long thenLong = thenGC.getTimeInMillis();

		long diffLong = nowLong - thenLong;
		long weekNum = diffLong / 1000 / 60 / 60 / 24 / 7;

		StringBuffer buf = new StringBuffer();
		buf.append ("Week ");
		buf.append ( weekNum );
		return buf.toString();
	} // getDiv

} // DayCounterDiv
