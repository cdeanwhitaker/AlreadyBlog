/*
 * AlreadyBlogServlet.java
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
import alreadyblog.db.BlogDAO;
import alreadyblog.db.Entry;
import alreadyblog.db.User;
import alreadyblog.db.Image;
import alreadyblog.db.LoginManager;
import alreadyblog.guibeans.DayCounterDiv;
import alreadyblog.guibeans.DuJourer;
import alreadyblog.guibeans.GuiBeanDiv;
import alreadyblog.guibeans.LoginDiv;
import alreadyblog.guibeans.RegisterDiv;
import alreadyblog.util.GUIConstants;

/**
 *  Main Servlet
**/
public class AlreadyBlogServlet extends HttpServlet {

	public static BlogConfigIF blogInfo = new BlogConfig();
	public static final String URL = blogInfo.getWebContext();

	private static final String ADD_ENTRY_COMMAND = "cmdAddEntry";
	private static final String REPLY_COMMAND = "cmdReply";
	private static final String REPLYING_COMMAND = "cmdReplying";

	public static final String EMAIL_PARAM = "emailParam";
	private static final String MESSAGE_PARAM = "messageParam";
	private static final String REPLY_ID_PARAM = "replyIdParam";

	private static final String DBA_NAME = blogInfo.getDBAUserName();

	public void doPost ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		debug ("\n\ndoPost()");

		res.setContentType ("text/html");

		String cmd = req.getParameter (GuiBeanDiv.CMD);

		String login = null;
		if ( LoginDiv.LOG_OUT_COMMAND.equals (cmd) ) {
			LoginManager.expireCookie (res, req);
		} else if ( LoginDiv.LOG_IN_COMMAND.equals (cmd) ) {
			debug ("setting cookie from log_in command");
			login = LoginManager.attemptLogin (req, res);
		} else {
			login = LoginManager.establishLoginFromCookie (req);
		} // if
		debug ( "login from cookie:" + login );
		boolean isLoggedIn = (login != null);

		PrintWriter out = res.getWriter();

		boolean isReply = REPLY_COMMAND.equals (cmd);
		Long replyId = null;
		if ( isReply ) {
			String replyIdStr = req.getParameter (REPLY_ID_PARAM);
			if ( replyIdStr != null ) {
				try {
					replyId = new Long (replyIdStr);
				} catch (NumberFormatException nfe) {}
			} else {
				isReply = false;
			} // if
		} // if

		String results = getPage (cmd, login, req, isLoggedIn, isReply, replyId);

		out.println ( getPageHeader() );

		out.println ("<table border=\"0\" width=\"100%\">");
		if ( isLoggedIn ) {
			out.print (" <caption class=\"capt\">Welcome back ");
			out.print ( login );
			out.println ("</caption>");
		} else {
			out.println (" <caption>Must Register and Log In to post or reply</caption>");
		} // if
		out.println (" <tr>");
		out.println ("  <td width=\"10%\" rowspan=\"2\" valign=\"top\">");
		out.println ("   <table border=\"0\" width=\"100%\">");
		out.println ("    <tr><td>");

		LoginDiv lid = new LoginDiv (blogInfo.getWebContext(), User.getLogins());
		out.println ( lid.getDiv() );
		out.println ("    </td></tr>");
		out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		out.println ("    <tr><td>");

		RegisterDiv regd = new RegisterDiv (blogInfo.getWebContext());
		out.println ( regd.getDiv() );
		out.println ("    </td></tr>");
		out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		if ( isLoggedIn && DBA_NAME.equals (login) ) {
			out.print ("    <tr><td><a href=\"");
			out.print ( blogInfo.getWebContext() );
			out.println ("dba\">DBAdmin</a></td></tr>");
			out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		} // if
		out.println ("   </table>");
		out.println ("  </td>");
		out.println ("  <td valign=\"top\" width=\"80%\" align=\"center\">");
		out.println ( results );
		out.println ("  </td>");
		out.println ("  <td width=\"10%\" rowspan=\"2\" align=\"center\" valign=\"top\">");

		out.println ("   <table border=\"0\" width=\"100%\">");
		if ( blogInfo.hasDuJourer() ) {
			out.print ("    <tr><td align=\"center\">");
			out.println ( getDuJourSection() );
			out.println ("    </td></tr>");
			out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		} // if
		if ( blogInfo.hasPictures() ) {
			out.print ("    <tr><td align=\"center\">");
			out.println ( getPicturesSection() );
			out.println ("    </td></tr>");
			out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		} // if
		if ( blogInfo.hasArchives() ) {
			out.println ("    <tr><td align=\"center\">");
			out.println ( getArchivesSection() );
			out.println ("    </td></tr>");
			out.println ("    <tr><td style=\"height: 30px;\"><hr/></td></tr>");
		} // if
		out.println ("   </table>");

		out.println ("  </td></tr>");

		if ( isLoggedIn ) {
			String prepopEntry = null;
			if ( "Blog This Image".equals (cmd) ) {
				String imageIdStr = req.getParameter
					(ImageViewerServlet.IMAGE_ID_PARAM);
				Image image = Image.getImage ( new Long ( imageIdStr ) );
				prepopEntry = "<p><center><img src=\"" + image.path + "\" /></center></p>";
			} // if
			out.println (" <tr><td align=\"center\">");
			out.println ( getEntrySection (isReply, replyId, prepopEntry) );
			out.println (" </td></tr>");
		} // if
		out.println ("</table>");
		out.println ( getPageFooter() );
	} // doPost

	private String getDuJourSection() {
		StringBuffer buf = new StringBuffer();
		DuJourer pdj = new DuJourer();
		buf.append ("<a style=\"text-decoration: none;\"");
		buf.append (" href=\"");
		buf.append ( pdj.getUrl() );
		buf.append ("\">Picture Du Jour</a><p/>\n");
		buf.append ( pdj.getDiv() );
		return buf.toString();
	} // getDuJourSection

	private String getArchivesSection() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<p/>");
		buf.append ("   <a style=\"text-decoration: none;\" href=\"");
		buf.append ( ArchiveServlet.URL );
		buf.append ("\">");
		buf.append ("Archives");
		buf.append ("</a><p/>\n");
		return buf.toString();
	} // getArchivesSection

	private String getPicturesSection() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<a style=\"text-decoration: none;\"");
		buf.append (" href=\"");
		buf.append ( ImageViewerServlet.URL );
		buf.append ("\">Pictures</a><p/>\n");
		return buf.toString();
	} // getPicturesSection

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
		buf.append ( blogInfo.getTitle() );
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
		buf.append ( blogInfo.getWebContext() );
		buf.append ("\"><h2>");
		buf.append ( blogInfo.getTitle() );
		if ( blogInfo.hasCounter() ) {
			buf.append (" <br/>\n");
			buf.append ( (new DayCounterDiv()).getDiv() );
		} // if
		buf.append ("</h2></a>\n");
		buf.append ("  </td>\n\n");
		buf.append (" </tr>\n");
		buf.append (" </table>\n");
		return buf.toString();
	} // getPageHeader

	private String getPageFooter () {
		StringBuffer buf = new StringBuffer();
		buf.append ("	<a href=\"");
		buf.append ( blogInfo.getHomePageURL() );
		buf.append ("\"><img ");
		buf.append ("style=\"border: 0px;\" src=\"");
		buf.append ( blogInfo.getHomePageLinkPic() );
		buf.append ("\"/></a>\n");
		buf.append ("	</center>\n");
		buf.append ("</body>\n");
		buf.append ("</html>\n");
		return buf.toString();
	} // getPageFooter

	private String getPage (String cmd, String login, HttpServletRequest req,
			boolean isLoggedIn, boolean isReply, Long replyId) {
		if ( ADD_ENTRY_COMMAND.equals (cmd) ) {
			String results = addEntry (req, login);
			if ( "success".equals (results) ) {
				return getEntries (login, isLoggedIn, isReply, replyId);
			} else {
				return results;
			} // if
		} else if ( REPLYING_COMMAND.equals (cmd) ) {
			String results = reply (req, login);
			if ( "success".equals (results) ) {
				return getEntries (login, isLoggedIn, isReply, replyId);
			} else {
				return results;
			} // if
		} else if ( RegisterDiv.ADD_USER_COMMAND.equals (cmd) ) {
			String results = addUser (req, login);
			if ( "success".equals (results) ) {
				return "Thank you for registering.  You may log In to start blogging.<br />" +
					getEntries (login, isLoggedIn, isReply, replyId);
			} else {
				return results;
			} // if
		} else {
			return getEntries (login, isLoggedIn, isReply, replyId);
		} // if
	} // getPage

	private String addEntry (HttpServletRequest req, String login) {
		String message = req.getParameter (MESSAGE_PARAM);
		return Entry.add (login, message);
	} // addEntry

	private String reply (HttpServletRequest req, String login) {
			String message = req.getParameter (MESSAGE_PARAM);
			String idStr = req.getParameter (REPLY_ID_PARAM);
			Long parentId = null;
			try {
				parentId = new Long (idStr);
			} catch (NumberFormatException nfe) {}

			if ( parentId == null ) {
				return "parentIdInvalid: " + idStr;
			} else {
				return Entry.reply (login, message, parentId);
			} // if
	} // reply

	private String addUser (HttpServletRequest req, String login) {
		String loginParam = req.getParameter (LoginManager.LOGIN_PARAM);
		String password = req.getParameter (LoginManager.PASSWORD_PARAM);
		String email = req.getParameter (EMAIL_PARAM);
		String status = User.addUser (loginParam, password, email);
		if ( status == null || "null".equals (status) ) {
			return "";
		} else {
			return status;
		} // if
	} // addUser

	private String getEntries (String login,
			boolean isLoggedIn, boolean isReply, Long replyId) {
		return retrieveBlogPage (isLoggedIn, isReply, replyId, login);
	} // getEntries

	private String getEntrySection (boolean isReply, Long replyId, String prepopEntry) {
		StringBuffer buf = new StringBuffer();
		buf.append ("\n<div id=\"inputDiv\">\n");
		buf.append (" <form id=\"inputSection\" method=\"post\" action=\"");
		buf.append ( blogInfo.getWebContext() );
		buf.append ("\">\n");
		if ( isReply ) {
			buf.append ("  <input id=\"cmd\" type=\"hidden\" name=\"cmd\" value=\"");
			buf.append ( REPLYING_COMMAND );
			buf.append ("\" />\n");
			buf.append ("  <input id=\"cmd\" type=\"hidden\" name=\"");
			buf.append ( REPLY_ID_PARAM );
			buf.append ("\" value=\"");
			buf.append ( replyId.toString() );
			buf.append ("\" />\n");
			buf.append ("  <table border=\"0\">\n");
			buf.append ("   <caption class=\"capt\">Add Reply</caption>\n");
			buf.append ("   <tr>\n");
			buf.append ("    <td><textarea class=\"textentry\" rows=\"10\" ");
			buf.append ("cols=\"70\" name=\"");
			buf.append ( MESSAGE_PARAM );
			buf.append ("\">");
			buf.append ("</textarea></td>\n");
			buf.append ("   </tr>\n");
			buf.append ("   <tr><th colspan=\"2\"><input type=\"submit\" ");
			buf.append ("value=\"Add Reply\" /></th></tr>\n");
		} else {
			buf.append ("  <input id=\"cmd\" type=\"hidden\" name=\"cmd\" value=\"");
			buf.append ( ADD_ENTRY_COMMAND );
			buf.append ("\" />\n");
			buf.append ("  <a name=\"ENTRY\" />\n");
			buf.append ("  <table border=\"0\">\n");
			buf.append ("   <caption class=\"capt\">Add New Entry</caption>\n");
			buf.append ("   <tr>\n");
			buf.append ("    <td><textarea class=\"textentry\" rows=\"10\" ");
			buf.append ("cols=\"70\" name=\"");
			buf.append ( MESSAGE_PARAM );
			buf.append ("\">");
			if ( prepopEntry != null ) {
				buf.append ( prepopEntry );
			} // if
			buf.append ("</textarea></td>\n");
			buf.append ("   </tr>\n");
			buf.append ("   <tr><th colspan=\"2\"><input type=\"submit\" ");
			buf.append ("value=\"Add New Entry\" /></th></tr>\n");
		} // if
		buf.append ("  </table>\n");
		buf.append (" </form>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // getEntrySection

	private String retrieveBlogPage (boolean isLoggedIn, boolean isReply, 
			Long replyId, String login) {
		StringBuffer buf = new StringBuffer();
		buf.append ("\n<div id=\"blogDiv\">\n");
		buf.append (" <table border=\"0\" width=\"100%\" cellpadding=\"0\">\n");
		buf.append ("  <tr><th width=\"15%\" class=\"headerTH\">&nbsp;</th>\n");
		buf.append ("   <th width=\"80%\" class=\"headerTH\">&nbsp;</th>\n");
		buf.append ("   <th width=\"5%\" class=\"headerTH\">&nbsp;</th>");

		if ( isReply ) {
			Entry entry = Entry.getEntry (replyId);
			if ( entry == null ) {
				buf.append ("  <tr><td>id:");
				buf.append (replyId);
				buf.append ("</td><td>entry:");
				buf.append (entry);
				buf.append ("</td></tr>");
			} else {
				buf.append ( entry.toHTMLRow (isLoggedIn, false, login,
					blogInfo.getWebContext(), REPLY_COMMAND, REPLY_ID_PARAM) );
			} // if
			buf.append ("\n");
		} else {
			ArrayList entryIds = Entry.getEntryIds();
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
						Entry entry = Entry.getEntry (id);
						if ( entry == null ) {
							buf.append ("  <tr><td>id:");
							buf.append (id);
							buf.append ("</td><td>entry:");
							buf.append (entry);
							buf.append ("</td></tr>");
						} else {
							buf.append ( entry.toHTMLRow (isLoggedIn, false, login,
								blogInfo.getWebContext(), REPLY_COMMAND, REPLY_ID_PARAM) );
	
							ArrayList childEntryIds = Entry.getChildEntryIds (id);
							for (int j=0; j<childEntryIds.size(); j++) {
								Long childId = (Long) childEntryIds.get (j);
								Entry childEntry = Entry.getEntry (childId);
								if ( childEntry == null ) {
									buf.append ("  <tr><td>id:");
									buf.append (id);
									buf.append ("</td><td>entry:");
									buf.append (childEntry);
									buf.append ("</td></tr>");
								} else {
									buf.append ( childEntry.toHTMLRow (isLoggedIn, true, login,
										blogInfo.getWebContext(), REPLY_COMMAND, REPLY_ID_PARAM) );
								} // if
							} // for
						} // if
						buf.append ("\n");
					} // if
				} // for
			} // if
		} // if
		buf.append (" </table>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // retrieveBlogPage

	public void doGet ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		doPost (req, res);
	} // doGet

	private static final boolean DEBUG_ON = false;

	public static void debug (String msg) {
		if ( DEBUG_ON ) {
			System.out.println ("debug - " + msg);
		} // if
	} // debug

	public static void error (String msg) {
		System.err.println ("error - " + msg);
	} // error

} // AlreadyBlogServlet
