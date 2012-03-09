/*
 * DBAServlet.java
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

import alreadyblog.db.BlogDAO;
import alreadyblog.db.Entry;
import alreadyblog.db.LoginManager;

public class DBAServlet extends HttpServlet {

	private static BlogConfigIF blogInfo = AlreadyBlogServlet.blogInfo;
	public static final String URL = blogInfo.getWebContext() + "dba";

	private static final String VERIFY_TEXT_PARAM = "verifyTextParam";

	private static final String CREATE_COMMAND = "cmdCreate";
	private static final String ARCHIVE_COMMAND = "cmdArchive";
	private static final String UPDATE_COMMAND = "cmdUpdate";
	private static final String QUERY_COMMAND = "cmdQuery";

	private static final String DBA_NAME = blogInfo.getDBAUserName();

	public void doPost ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		res.setContentType ("text/html");

		String cmd = req.getParameter ("cmd");

		String login = LoginManager.establishLoginFromCookie (req);
		boolean isLoggedIn = (login != null);

		boolean canConnect =  BlogDAO.canConnect();
		boolean canAccess = BlogDAO.canAccess();

		if ( canConnect && canAccess ) {
			if ( ! isLoggedIn ) {
				forward (req, res, "/" + blogInfo.getWebContext());
			} else {
				if ( ! DBA_NAME.equals (login) ) {
					forward (req, res, "/" + blogInfo.getWebContext());
				} // if
			} // if
		} // if

		PrintWriter out = res.getWriter();

		ArrayList results = null;
		String updateError = null;
		String query = "";
		if ( QUERY_COMMAND.equals (cmd) ) {
			query = req.getParameter ("queryText");
			results = processQuery (query);
		} else if ( UPDATE_COMMAND.equals (cmd) ) {
			query = req.getParameter ("updateText");
			updateError = processUpdate (query);
		} else if ( ARCHIVE_COMMAND.equals (cmd) ) {
			ArrayList list = Entry.getParentEntryIdsToArchive();
			for (int i=0; i<list.size(); i++) {
				Object o = list.get (i);
				if ( o instanceof String ) {
					out.println ( "<p>ERROR: " + ((String) o) +"</p>");
				} else {
					Long id = (Long) o;
					Entry entry = Entry.getEntry (id);
					if ( Entry.archiveEntry (id) ) {
						ArrayList children = Entry.getChildEntryIds (id);
						for (int j=0; j<children.size(); j++) {
							Object oo = children.get (j);
							Long childId = (Long) oo;
							if ( Entry.archiveEntry (childId) ) {
								Entry.deleteEntry (childId);
							} // if
						} // for
						Entry.deleteEntry (id);
					} // if
				} // if
			} // for
			out.println ( "<br />" );

		} else if ( CREATE_COMMAND.equals (cmd) ) {
			String verify = req.getParameter (VERIFY_TEXT_PARAM);
			if ( verify != null && "YES".equals (verify.toUpperCase()) ) {
				BlogDAO.recreate();
				out.println ("New Database created");
			} else {
				out.print ("DID NOT RECREATE. ");
				out.println ("To recreate database, verify by entering YES");
			} // if
		} // if

		out.println ( getPageHeader() );

		out.println ("<p/><div id=\"queryDiv\">");

		if ( canConnect ) {
			out.println ("<p>Can connect to database</p>");
			if ( canAccess ) {
				out.println ("<p>Can access database tables</p>");
			} else {
				out.println ("<p>Can not access database tables");
				out.println ("Verify that the database exists.</p>");
			} // if
		} else {
			out.println ("<p>Can not get connection to database");
			out.println ("Verify the connection string and that the database exists.</p>");
		} // if
		out.print ("<form id=\"queryForm\" method=\"POST\" action=\"");
		out.print ( URL );
		out.println ("\" />");
		out.print ("<textarea class=\"textentry\" rows=\"10\" ");
		out.print ("cols=\"70\" name=\"queryText\">");
		out.println ("</textarea>");
		out.print ("<input type=\"hidden\" name=\"cmd\" value=\"");
		out.print ( QUERY_COMMAND );
		out.println ("\" />");
		out.println ("<input type=\"submit\" value=\"Query\" />");
		out.println ("</form>");
		out.println ("</div>");

		out.println ("<p/><div id=\"resultsDiv\">");
		out.println ("<table border=\"1\">");
		if ( results == null ) {
			out.println ("<caption>No results</caption>");
		} else {
			out.println ("<caption>" + query + "</caption>");
			for (int a=0; a<results.size(); a++) {
				out.println ("<tr>");
				ArrayList inner = (ArrayList) results.get (a);
				for (int b=0; b<inner.size(); b++) {
					out.print ("<td>");
					String value = (String) inner.get (b);
					out.print ( value );
					out.println ("</td>");
				} // for
				out.println ("</tr>");
			} // for
		} // if
		out.println ("<table>");
		out.println ("</div>");

		out.println ("<p/><div id=\"updateDiv\">");
		out.print ("<form id=\"updateForm\" method=\"POST\" action=\"");
		out.print ( URL );
		out.println ("\" />");
		out.print ("<textarea class=\"textentry\" rows=\"10\" ");
		out.print ("cols=\"70\" name=\"updateText\">");
		out.println ("</textarea>");
		out.print ("<input type=\"hidden\" name=\"cmd\" value=\"");
		out.print ( UPDATE_COMMAND );
		out.println ("\" />");
		out.println ("<input type=\"submit\" value=\"Execute\" />");
		out.println ("</form>");
		out.println ("</div>");

		out.println ("<p/><div id=\"successDiv\">");
		if ( UPDATE_COMMAND.equals (cmd) ) {
			if ( updateError == null ) {
				out.println ("Executed Successfully");
			} else {
				out.println ("Executed with Errors<br/>");
				out.println ( updateError );
			} // if
		} else {
			out.println ("nothing doing");
		} // if
		out.println ("</div>");

		if ( "backup".equals (cmd) ) {
			out.println ("<p/><div id=\"backupDiv\">");
			out.println ("<textarea rows=\"140\" cols=\"80\">");
			out.println ( BlogDAO.getBackupScript() );
			out.println ("</textarea>");
			out.println ("</div>");
		} else {
			out.println ("<p/><div id=\"backupDiv\">");
			out.print ("<form id=\"backupForm\" method=\"POST\" action=\"");
			out.print ( URL );
			out.println ("\" />");
			out.println ("<input type=\"hidden\" name=\"cmd\" value=\"backup\" />");
			out.println ("<input type=\"submit\" value=\"Backup\" />");
			out.println ("</form>");
			out.println ("</div>");
		} // if

		out.println ("<p/><div id=\"archiveDiv\">");
		out.print ("<form id=\"archiveForm\" method=\"POST\" action=\"");
		out.print ( URL );
		out.println ("\" />");
		out.print ("<form id=\"archiveForm\" method=\"POST\" action=\"");
		out.print ( URL );
		out.println ("\" />");
		out.print ("<input type=\"hidden\" name=\"cmd\" value=\"");
		out.print ( ARCHIVE_COMMAND );
		out.println ("\" />");
		out.println ("<input type=\"submit\" value=\"Archive\" />");
		out.println ("</form>");

		out.println ("<p/><div id=\"archiveDiv\">");
		out.print ("<form id=\"createForm\" method=\"POST\" action=\"");
		out.print ( URL );
		out.println ("\" />");
		out.print ("<input type=\"hidden\" name=\"cmd\" value=\"");
		out.print ( CREATE_COMMAND );
		out.println ("\" />");
		out.println ("<input type=\"submit\" value=\"Recreate Database\" />");
		out.print ("Are you sure? <input type=\"text\" size=\"5\" name=\"");
		out.print ( VERIFY_TEXT_PARAM );
		out.println ("\" />");
		out.println ("</form>");

		out.println ( getPageFooter() );
	} // doPost

	public void doGet ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		doPost (req, res);
	} // doGet

	private static ArrayList processQuery (String sql) {
		ArrayList a = new ArrayList();

		Connection conn = BlogDAO.getConnection();
		if ( conn == null ) {
			return null;
		} // if

		ResultSet rset = null;
		Statement stmt = null;
		String retVal = null;

		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				ArrayList b = new ArrayList();
				try {
					b.add ( rset.getString (1) );
					b.add ( rset.getString (2) );
					b.add ( rset.getString (3) );
					b.add ( rset.getString (4) );
					b.add ( rset.getString (5) );
					b.add ( rset.getString (6) );
					b.add ( rset.getString (7) );
					b.add ( rset.getString (8) );
					b.add ( rset.getString (9) );
					b.add ( rset.getString (10) );
				} catch (SQLException sqle) {
				} // try
				a.add ( b );
			} // while
		} catch (SQLException sqle) {
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return a;
	} // processQuery

	private static String processUpdate (String sql) {
		String error = null;

		Connection conn = BlogDAO.getConnection();
		if ( conn == null ) {
			return null;
		} // if

		Statement stmt = null;
		String retVal = null;

		try {
			stmt = conn.createStatement();
			stmt.execute (sql);
		} catch (SQLException sqle) {
			return sqle.toString();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return null;
	} // processUpdate

	private String getPageHeader() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<html>\n");
		buf.append ("<head>\n");
		buf.append ("<title>DBA</title>\n");
		buf.append ("</head>\n");
		buf.append ("<body bgcolor=\"#999999\">\n");
		buf.append (" <center>\n");
		buf.append ("  <a href=\"");
		buf.append ( URL );
		buf.append ("\"><h2>DBA</h2></a>\n");
		return buf.toString();
	} // getPageHeader

	private String getPageFooter () {
		StringBuffer buf = new StringBuffer();
		buf.append (" </center>\n");
		buf.append ("</body>\n");
		buf.append ("</html>\n");
		return buf.toString();
	} // getPageFooter

	public void forward (HttpServletRequest req, HttpServletResponse res, String url)
			throws ServletException, java.io.IOException {
		try {
			RequestDispatcher rd = req.getRequestDispatcher (url);
			rd.forward (req, res);
		} catch (IllegalStateException ise) {
		} // try
	} // forward

} // DBAServlet
