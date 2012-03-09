/*
 * DuJourer.java
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

public class DuJourer implements GuiBeanDiv {

	private static final String [] THUMBNAILS = {
		"/photos/img/albums/Bob/bob_bobbing_for_apples.jpg"
	};

	private static final long MILLISECS_IN_DAY = 1000 * 60 * 60 * 24;

	public String getDiv() {
		long d = System.currentTimeMillis() / MILLISECS_IN_DAY;
		int index = (int) (d % THUMBNAILS.length);
		return "<img src=\"" + THUMBNAILS [index] + "\" />";
	} // getDiv

	public String getUrl() {
		return "photos";
	} // getUrl

} // DuJourer
