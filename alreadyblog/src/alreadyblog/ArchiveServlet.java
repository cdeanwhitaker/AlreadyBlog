/*
 * ArchiveServlet.java
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

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import alreadyblog.db.Entry;
import alreadyblog.db.Image;
import alreadyblog.db.LoginManager;
import alreadyblog.guibeans.DuJourer;
import alreadyblog.guibeans.GuiBeanDiv;
import alreadyblog.guibeans.LoginDiv;
import alreadyblog.guibeans.RegisterDiv;
import alreadyblog.util.GUIConstants;

/**
 *  Main Servlet
**/
public class ArchiveServlet extends HttpServlet {

	private static BlogConfigIF blogInfo = AlreadyBlogServlet.blogInfo;
	public static final String URL = blogInfo.getWebContext() + "arch";

	public void doPost ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {

		res.setContentType ("text/html");

		String cmd = req.getParameter (GuiBeanDiv.CMD);

		String login = LoginManager.establishLoginFromCookie (req);
		boolean isLoggedIn = (login != null);

		PrintWriter out = res.getWriter();

		String results = retrieveBlogPage ( isLoggedIn, login );

		out.println ( getPageHeader() );

		out.println ("<table border=\"0\" width=\"100%\">");
		out.println (" <caption>May not reply to archived threads</caption>");
		out.println (" <tr>");
		out.println ("  <td width=\"10%\" rowspan=\"2\" valign=\"top\">");
		out.println ("   <table border=\"0\" width=\"100%\">");
		out.println ("    <tr><td>");

		out.println ("   </table>");
		out.println ("  </td>");
		out.println ("  <td valign=\"top\" width=\"80%\" align=\"center\">");
		out.println ( results );
		out.println ("  </td>");
		out.println ("  <td width=\"10%\" rowspan=\"2\" align=\"center\" valign=\"top\">");
		out.println ("   <table border=\"0\" width=\"100%\">");

		out.println ("   </table>");
		out.println ("  </td></tr>");

		if ( isLoggedIn ) {
			String prepopEntry = null;
			if ( "Blog This Image".equals (cmd) ) {
				String imageIdStr = req.getParameter (ImageViewerServlet.IMAGE_ID_PARAM);
				Image image = Image.getImage ( new Long ( imageIdStr ) );
				prepopEntry = "<p><center><img src=\"" + image.path + "\" /></center></p>";
			} // if
		} // if
		out.println ("</table>");
		out.println ( getPageFooter() );
	} // doPost

	private String getArchivesSection() {
		StringBuffer buf = new StringBuffer();
		buf.append ("Archives<p/>");
		return buf.toString();
	} // getArchivesSection

	public void forward (HttpServletRequest req, HttpServletResponse res, String url)
			throws ServletException, java.io.IOException {
		try {
			RequestDispatcher rd = req.getRequestDispatcher (url);
			rd.forward (req, res);
		} catch (IllegalStateException ise) {
		} // try
	} // forward

	private String getPageHeader() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<html>\n");
		buf.append ("<head>\n");
		buf.append ("<link rel=\"shortcut icon\" href=\"");
		buf.append ( blogInfo.getIconImage() );
		buf.append ("\" />\n");
		buf.append ("<style>\n");
		buf.append ( GUIConstants.getCSS() );
		buf.append ("</style>\n");
		buf.append ("<title>");
		buf.append ( blogInfo.getTitle() + " Archives" );
		buf.append ("</title>\n");
		buf.append ("</head>\n");
		buf.append ("<body background=\"");
		buf.append ( blogInfo.getBackgroundImage() );
		buf.append ("\">\n");
		buf.append (" <center>\n");
		buf.append (" <table width=\"100%\" border=\"0\">\n");
		buf.append (" <tr>\n\n");
		buf.append ("  <td align=\"center\" width=\"60%\" valign=\"top\">\n\n");
		buf.append ("   <a style=\"text-decoration: none;\" href=\"");
		buf.append ( URL );
		buf.append ("\"><h2>");
		buf.append ( blogInfo.getTitle() + " Archives" );
		buf.append ("</h2></a>\n");
		buf.append ("  </td>\n\n");
		buf.append (" </tr>\n");
		buf.append (" </table>\n");
		return buf.toString();
	} // getPageHeader

	private String getPageFooter () {
		StringBuffer buf = new StringBuffer();
		buf.append ("<p><a style=\"text-decoration: none;\" href=\"");
		buf.append ( blogInfo.getWebContext() );
		buf.append ("\"><h2>Back To Blog</h2></a></p>");
		buf.append ("<p><a href=\"");
		buf.append ( blogInfo.getHomePageURL() );
		buf.append ("\"><img ");
		buf.append ("style=\"border: 0px;\" src=\"");
		buf.append ( blogInfo.getHomePageLinkPic() );
		buf.append ("\"/></a>\n");
		buf.append ("</center>\n");
		buf.append ("</body>\n");
		buf.append ("</html>\n");
		return buf.toString();
	} // getPageFooter

	private String retrieveBlogPage (boolean isLoggedIn, String login) {
		StringBuffer buf = new StringBuffer();
		buf.append ("\n<div id=\"blogDiv\">\n");
		buf.append (" <table border=\"0\" width=\"100%\" cellpadding=\"0\">\n");
		buf.append ("  <tr><th width=\"15%\" class=\"headerTH\">&nbsp;</th>\n");
		buf.append ("   <th width=\"80%\" class=\"headerTH\">&nbsp;</th>\n");
		buf.append ("   <th width=\"5%\" class=\"headerTH\">&nbsp;</th>");

			ArrayList entryIds = Entry.getArchivedEntryIds();
			if ( entryIds != null ) {
				for (int i=0; i<entryIds.size(); i++) {
					Object o = entryIds.get (i);
					if ( o instanceof SQLException ) {
						SQLException sqle = (SQLException) o;
						buf.append ("  <tr><td>");
						buf.append ( sqle.toString() );
						buf.append ("</td></tr>");
					} else {
						Long id = (Long) o;
						Entry entry = Entry.getArchivedEntry (id);
						if ( entry == null ) {
							buf.append ("  <tr><td>id:");
							buf.append (id);
							buf.append ("</td><td>entry:");
							buf.append (entry);
							buf.append ("</td></tr>");
						} else {
							buf.append ( entry.toHTMLRow (false, false, login,
								blogInfo.getWebContext(), null, null) );
	
							ArrayList childEntryIds = Entry.getArchivedChildEntryIds (id);
							for (int j=0; j<childEntryIds.size(); j++) {
								Long childId = (Long) childEntryIds.get (j);
								Entry childEntry = Entry.getArchivedEntry (childId);
								if ( childEntry == null ) {
									buf.append ("  <tr><td>id:");
									buf.append (id);
									buf.append ("</td><td>entry:");
									buf.append (childEntry);
									buf.append ("</td></tr>");
								} else {
									buf.append ( childEntry.toHTMLRow
										(false, true, login,
										blogInfo.getWebContext(), null, null) );
								} // if
							} // for
						} // if
						buf.append ("\n");
					} // if
				} // for
			} // if
		buf.append (" </table>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // retrieveBlogPage

	public void doGet ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		doPost (req, res);
	} // doGet

} // ArchiveServlet
