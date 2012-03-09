/*
 * BlogConfig.java
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

public class BlogConfig implements BlogConfigIF {

	/**
	 *  The blog title
	**/
	public String getTitle() {
		return "Bob's Blog Page";
	} // getTitle

	/**
	 *  The url of the home page
	**/
	public String getHomePageURL() {
		return "http://www.foobarboofar.com";
	} // getHomePageURL

	/**
	 *  The web-context of the blog page off of the home page,
	 *  e.g. "blog" for the page to be at http://www.foobarboofar.org/blog
	**/
	public String getWebContext() {
		return "bobsblog";
	} // getWebContext

	/**
	 *  The unique prefix for database tables (underscore will be appended to it).
	 *  e.g. "blog" for the page to be at http://www.foobarboofar.org/blog
	**/
	public String getDBNamespace() {
		return "bobs";
	} // getWebContext

	/**
	 *  Name of login for user who is to be admin
	**/
	public String getDBAUserName() {
		return "bob";
	} // getDBAUserName

	/**
	 *  The directory for pictures under the web context,
	 *  e.g. "img" for the directory to be at http://www.foobarboofar.org/img
	**/
	public String getPicSubdir() {
		return "img";
	} // getPicSubdir

	/**
	 *  The image at the bottom of each page linking back to the home page
	**/
	public String getHomePageLinkPic() {
		return getPicSubdir() + "/bobs-home.png";
	} // getHomePageLinkPic

	/**
	 *  The icon image, displayed in the browser's url line and on tabs
	**/
	public String getIconImage() {
		return getPicSubdir() + "/bobs.ico";
	} // getIconImage

	/**
	 *  The background image of the blog page
	**/
	public String getBackgroundImage() {
		return getPicSubdir() + "/bobs-bak.png";
	} // getBackgroundImage

	/**
	 *  The path on the web server under which the picture directory will exist
	 *  e.g. /opt/tomcat/webapps/alreadyblog
	**/
	public String getServerPath() {
		return "/home/content/b/o/b/bob/html/";
	} // getServerPath

	/**
	 *  The ODBC url, e.g. jdbc:mysql://localhost/dbname
	**/
	public String getDbUrl() {
		return "jdbc:mysql://mysql808.secureserver.net/bobbobsdb";
	} // getDbUrl

	/**
	 *  The database user name
	**/
	public String getDbUser() {
		return "bobadm";
	} // getDbUser

	/**
	 *  The database user password
	**/
	public String getDbPass() {
		return "bobpwd";
	} // getDbPass

	/**
	 *  The package path of the database Driver class
	**/
	public String getDbDriver() {
		return "org.gjt.mm.mysql.Driver";
	} // getDbDriver

	/**
	 *  Page has Picture du Jour
	**/
	public boolean hasDuJourer() {
		return true;
	} // hasDuJourer

	/**
	 *  Page has link to picture gallery (ImageViewerServlet)
	**/
	public boolean hasPictures() {
		return true;
	} // hasPictures

	/**
	 *  Page has link to archives page (ArchiveServlet)
	**/
	public boolean hasArchives() {
		return true;
	} // hasArchives

	/**
	 *  Age of entry before being eligible for archiving
	**/
	public int getArchiveDays() {
		return 30;
	} // getArchiveDays

	/**
	 *  Get unique cookie id.  Not setting differently for different contexts
	 *    may allow users of one site to be logged in to another
	 *    under the wrong id.
	**/
	public String getCookieName() {
		return "bobsLoginCookie";
	} // getCookieName

	/**
	 *  Age of cookie in seconds before expiring
	**/
	public int getCookieLifeInSecs() {
		return 60 * 30;
	} // getCookieLifeInSecs

	/**
	 *  Counter in page title
	**/
	public boolean hasCounter() {
		return true;
	} // hasCounter

} // BlogConfig
