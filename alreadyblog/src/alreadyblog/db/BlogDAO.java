/*
 * BlogDAO.java
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
package alreadyblog.db;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import alreadyblog.AlreadyBlogServlet;
import alreadyblog.BlogConfigIF;

public class BlogDAO {

	protected static final BlogConfigIF blogInfo = AlreadyBlogServlet.blogInfo;
	protected static final String NAMESPACE = blogInfo.getDBNamespace();

	private static final String DB_URL = blogInfo.getDbUrl();
	private static final String DB_USER = blogInfo.getDbUser();
	private static final String DB_PASS = blogInfo.getDbPass();
	private static final String DB_DRIVER = blogInfo.getDbDriver();

	public static Connection getConnection() {
		try {
			Class.forName ( DB_DRIVER );
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} // try

		Connection conn = null;
		try {
			conn = DriverManager.getConnection (DB_URL, DB_USER, DB_PASS);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} // try

		return conn;
	} // getConnection

	protected static final int QUERY = 0;
	protected static final int UPDATE = 1;

	protected static String execute (int type, String sql) {
		String msg = "";
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if

		ResultSet rset = null;
		Statement stmt = null;
		String retVal = null;

		try {
			stmt = conn.createStatement();
			if ( type == QUERY ) {
				rset = stmt.executeQuery (sql);
				if ( rset.next() ) {
					retVal = rset.getString (1);
				} // if
			} else if ( type == UPDATE ) {
				stmt.executeUpdate (sql);
			} // if
		} catch (SQLException sqle) {
			msg += sqle.toString();
		} finally {
			try {
				rset.close();
				stmt.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		} // try

		return retVal + msg;
	} // execute

// // // // // // // // // // // // // // // // // // // // // // // // // // //

	private static final String DROP_BLOG_IMAGES_SQL =
		"DROP TABLE IF EXISTS " + NAMESPACE + "_blog_images";
	private static final String DROP_BLOG_ENTRIES_SQL =
		"DROP TABLE IF EXISTS " + NAMESPACE + "_blog_entries";
	private static final String DROP_BLOG_ENTRIES_HIST_SQL =
		"DROP TABLE IF EXISTS " + NAMESPACE + "_blog_entries_hist";
	private static final String DROP_BLOG_USERS_SQL =
		"DROP TABLE IF EXISTS " + NAMESPACE + "_blog_users";

	private static final String CREATE_USERS_SQL =
		"CREATE TABLE " + NAMESPACE + "_blog_users ( " +
		"user_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
		"login VARCHAR(24), " +
		"password VARCHAR(64), " +
		"email VARCHAR(48), " +
		"is_dba TINYINT, " +
		"can_post_images TINYINT " +
		") ENGINE=InnoDB AUTO_INCREMENT=1000000";

	private static final String CREATE_ENTRIES_SQL =
		"CREATE TABLE " + NAMESPACE + "_blog_entries ( " +
		"entry_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
		"parent_entry_id BIGINT UNSIGNED, " +
		"user_id BIGINT UNSIGNED, " +
		"entry TEXT, " +
		"dt DATETIME, " +
		"INDEX (user_id), " +
		"FOREIGN KEY (user_id) " +
		"REFERENCES " + NAMESPACE + "_blog_users (user_id) " +
		"ON DELETE NO ACTION, " +
		"INDEX (parent_entry_id), " +
		"FOREIGN KEY (parent_entry_id) " +
		"REFERENCES " + NAMESPACE + "_blog_entries (entry_id) " +
		"ON DELETE NO ACTION " +
		") ENGINE=InnoDB AUTO_INCREMENT=2000000";

	private static final String CREATE_ENTRIES_HIST_SQL =
		"CREATE TABLE " + NAMESPACE + "_blog_entries_hist ( " +
		"entry_id BIGINT UNSIGNED NOT NULL PRIMARY KEY, " +
		"parent_entry_id BIGINT UNSIGNED, " +
		"user_id BIGINT UNSIGNED, " +
		"entry TEXT, " +
		"dt DATETIME, " +
		"INDEX (user_id), " +
		"FOREIGN KEY (user_id) " +
		"REFERENCES " + NAMESPACE + "_blog_users (user_id) " +
		"ON DELETE NO ACTION, " +
		"INDEX (parent_entry_id), " +
		"FOREIGN KEY (parent_entry_id) " +
		"REFERENCES " + NAMESPACE + "_blog_entries_hist (entry_id) " +
		"ON DELETE NO ACTION " +
		") ENGINE=InnoDB";

	private static final String CREATE_IMAGES_SQL =
		"CREATE TABLE " + NAMESPACE + "_blog_images ( " +
		"image_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
		"user_id BIGINT UNSIGNED, " +
		"path VARCHAR(64), " +
		"caption VARCHAR(64), " +
		"INDEX (user_id), " +
		"FOREIGN KEY (user_id) " +
		"REFERENCES " + NAMESPACE + "_blog_users (user_id) " +
		"ON DELETE NO ACTION " +
		") ENGINE=InnoDB AUTO_INCREMENT=3000000";

	public static String recreate() {
		String msg = "";
		msg += execute (UPDATE, DROP_BLOG_IMAGES_SQL);
		msg += execute (UPDATE, DROP_BLOG_ENTRIES_SQL);
		msg += execute (UPDATE, DROP_BLOG_ENTRIES_HIST_SQL);
		msg += execute (UPDATE, DROP_BLOG_USERS_SQL);
		msg += execute (UPDATE, CREATE_USERS_SQL);
		msg += execute (UPDATE, CREATE_ENTRIES_HIST_SQL);
		msg += execute (UPDATE, CREATE_ENTRIES_SQL);
		msg += execute (UPDATE, CREATE_IMAGES_SQL);
		return msg;
	} // recreate

	public static String getBackupScript() {
		StringBuffer buf = new StringBuffer();
		String sql = null;
		Connection conn = null;
		ResultSet rset = null;
		Statement stmt = null;
		ArrayList rows = null;

		sql = "SELECT user_id, login, password, email, is_dba, can_post_images" +
			" FROM " + NAMESPACE + "_blog_users";
		conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		rows = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				ArrayList cols = new ArrayList();
				cols.add (rset.getString (1));
				cols.add (rset.getString (2));
				cols.add (rset.getString (3));
				cols.add (rset.getString (4));
				cols.add (rset.getString (5));
				cols.add (rset.getString (6));
				rows.add (cols);
			} // while
		} catch (SQLException sqle) {
		} finally {
			try {
				rset.close();
				stmt.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		} // try

		for (int r=0; r<rows.size(); r++) {
			ArrayList cols = (ArrayList) rows.get (r);
			buf.append ("INSERT INTO " + NAMESPACE + "_blog_users (\n");
			buf.append ("user_id, login, password, email, is_dba, can_post_images\n");
			buf.append (") VALUES (\n");
			buf.append ( cols.get(0));
			buf.append (", '");
			buf.append (cols.get(1));
			buf.append ("', '");
			buf.append (cols.get(2));
			buf.append ("', '");
			buf.append (cols.get(3));
			buf.append ("', ");
			buf.append (cols.get(4));
			buf.append (", ");
			buf.append (cols.get(5));
			buf.append ("\n);\n\n");
		} // for

		sql = "SELECT entry_id, parent_entry_id, user_id, entry, dt" +
			" FROM " + NAMESPACE + "_blog_entries";
		conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		rows = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				ArrayList cols = new ArrayList();
				cols.add (rset.getString (1));
				cols.add (rset.getString (2));
				cols.add (rset.getString (3));
				cols.add (rset.getString (4));
				cols.add (rset.getString (5));
				rows.add (cols);
			} // while
		} catch (SQLException sqle) {
		} finally {
			try {
				rset.close();
				stmt.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		} // try

		for (int r=0; r<rows.size(); r++) {
			ArrayList cols = (ArrayList) rows.get (r);
			buf.append ("INSERT INTO ");
			buf.append ( NAMESPACE );
			buf.append ("_blog_entries (\n");
			buf.append ("entry_id, parent_entry_id, user_id, entry, dt\n");
			buf.append (") VALUES (\n");
			buf.append (cols.get(0));
			buf.append (", ");
			buf.append (cols.get(1));
			buf.append (", ");
			buf.append (cols.get(2));
			buf.append (", '");
			buf.append (cols.get(3));
			buf.append ("', '");
			buf.append (cols.get(4));
			buf.append ("'\n);\n\n");
		} // for

		sql = "SELECT entry_id, parent_entry_id, user_id, entry, dt" +
			" FROM " + NAMESPACE + "_blog_entries_hist";
		conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		rows = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				ArrayList cols = new ArrayList();
				cols.add (rset.getString (1));
				cols.add (rset.getString (2));
				cols.add (rset.getString (3));
				cols.add (rset.getString (4));
				cols.add (rset.getString (5));
				rows.add (cols);
			} // while
		} catch (SQLException sqle) {
		} finally {
			try {
				rset.close();
				stmt.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		} // try

		for (int r=0; r<rows.size(); r++) {
			ArrayList cols = (ArrayList) rows.get (r);
			buf.append ("INSERT INTO ");
			buf.append ( NAMESPACE );
			buf.append ("_blog_entries_hist (\n");
			buf.append ("entry_id, parent_entry_id, user_id, entry, dt\n");
			buf.append (") VALUES (\n");
			buf.append (cols.get(0));
			buf.append (", ");
			buf.append (cols.get(1));
			buf.append (", ");
			buf.append (cols.get(2));
			buf.append (", '");
			buf.append (cols.get(3));
			buf.append ("', '");
			buf.append (cols.get(4));
			buf.append ("'\n);\n\n");
		} // for

		return buf.toString();
	} // getBackupScript

	public static boolean canConnect() {
		boolean success = false;
		Connection conn = null;
		try {
			conn = getConnection();
			if ( conn != null ) {
				success = true;
			} // if
		} catch (Exception e) {
			String errorMsg = e.toString();
			System.err.println ("errorMsg: " + errorMsg);
			success = false;
		} finally {
			try {
				conn.close();
			} catch (Exception e) {}
		} // try
		return success;
	} // canConnect

	public static boolean canAccess() {
		boolean success = false;
		String sql = "SELECT IF(COUNT(1)>-1,'null','error') FROM " +
			NAMESPACE + "_blog_users";
		String res = execute (QUERY, sql);
		//System.out.println ("res: " + res);
		success = ( res == null || "null".equals (res) );
		return success;
	} // canAccess

} // BlogDAO
