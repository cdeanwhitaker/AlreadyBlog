/*
 * BlogConfigIF.java
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
package alreadyblog;

public interface BlogConfigIF {

	/**
	 *  The blog title, e.g. "Bob's Blog Page"
	**/
	public String getTitle();

	/**
	 *  The url of the home page, e.g. "http://www.foobarboofar.org"
	**/
	public String getHomePageURL();

	/**
	 *  The web-context of the blog page off of the home page,
	 *  e.g. "blog" for the page to be at http://www.foobarboofar.org/blog
	**/
	public String getWebContext();

	/**
	 *  The unique prefix for database tables.
	 *  e.g. "blog" for the page to be at http://www.foobarboofar.org/blog
	**/
	public String getDBNamespace();

	/**
	 *  Name of login for user who is to be admin
	**/
	public String getDBAUserName();

	/**
	 *  The directory for pictures under the web context,
	 *  e.g. "img" for the directory to be at http://www.foobarboofar.org/img
	**/
	public String getPicSubdir();

	/**
	 *  The image at the bottom of each page linking back to the home page
	**/
	public String getHomePageLinkPic();

	/**
	 *  The icon image, displayed in the browser's url line and on tabs
	**/
	public String getIconImage();

	/**
	 *  The background image of the blog page
	**/
	public String getBackgroundImage();

	/**
	 *  The path on the web server under which the picture directory will exist
	 *  e.g. /opt/tomcat/webapps/alreadyblog
	**/
	public String getServerPath();

	/**
	 *  The ODBC url, e.g. jdbc:mysql://localhost/dbname
	**/
	public String getDbUrl();

	/**
	 *  The database user name
	**/
	public String getDbUser();

	/**
	 *  The database user password
	**/
	public String getDbPass();

	/**
	 *  The package path of the database Driver class
	**/
	public String getDbDriver();

	/**
	 *  Page has Picture du Jour
	**/
	public boolean hasDuJourer();

	/**
	 *  Page has link to picture gallery (ImageViewerServlet)
	**/
	public boolean hasPictures();

	/**
	 *  Page has link to archives page (ArchiveServlet)
	**/
	public boolean hasArchives();

	/**
	 *  Age of entry before being eligible for archiving
	**/
	public int getArchiveDays();

	/**
	 *  Get unique cookie id.  Not setting differently for different contexts
	 *    may allow users of one site to be logged in to another
	 *    under the wrong id.
	**/
	public String getCookieName();

	/**
	 *  Age of cookie in seconds before expiring
	**/
	public int getCookieLifeInSecs();

	/**
	 *  Counter in page title
	**/
	public boolean hasCounter();

} // BlogConfigIF

