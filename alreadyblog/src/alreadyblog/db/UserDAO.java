/*
 * UserDAO.java
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

public class UserDAO extends BlogDAO {

	public static ArrayList getLogins() {
		String sql = "SELECT login FROM " + NAMESPACE + "_blog_users ORDER by login";
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		ArrayList loginList = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				loginList.add ( rset.getString (1) );
			} // while
		} catch (SQLException sqle) {
			loginList.add (sqle.toString());
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return loginList;
	} // getLogins

	public static boolean attemptLogin (String login, String password) {
		boolean success = false;
		String sql =
			"SELECT COUNT(1) FROM " + NAMESPACE + "_blog_users WHERE login = '" + login +
			"' AND password = MD5('" + password + "')";
		String shouldBeOne = execute (QUERY, sql);
		if ( "1".equals (shouldBeOne) ) {
			success = true;
		} // if
		return success;
	} // attemptLogin

	public static String addUser (String login, String password, String email) {
		if ( login == null ) {
			return "null login";
		} // if
		if ( password == null ) {
			return "null password";
		} // if
		if ( email == null ) {
			return "null email";
		} // if
		login = login.replaceAll ("'","");
		email = email.replaceAll ("'","");

		String id = execute (QUERY,
			"SELECT user_id FROM " + NAMESPACE + "_blog_users WHERE login = '" + login + "'");
		if ( id == null || "null".equals (id) ) {

//			final String IS_DBA = "SELECT IF((SELECT COUNT(1) FROM " +
//				NAMESPACE + "_blog_users) > 0, 0, 1)";
			final String IS_DBA =
				"SELECT IF(COUNT(1)>0, 0, 1) FROM " + NAMESPACE + "_blog_users";
			String isDBA = execute (QUERY, IS_DBA);

			final String CREATE_USER_SQL =
				"INSERT INTO " + NAMESPACE + "_blog_users (" +
				"login, password, email, is_dba, can_post_images" +
				") VALUES ('" + login + "', MD5('" + password +
				"'), '" + email + "', " + isDBA + ", " + isDBA + ")";

			String errors = execute (UPDATE, CREATE_USER_SQL);
			if ("null".equals (errors) ) {
				errors = "success";
			} else {
				errors = "SQL: " + CREATE_USER_SQL + " : " + errors;
			} // if
			return errors;
		} else {
			return "User with login " + login + " already exists<br/>" +
				"<a href=\"blog\">Try Again</a>";
		} // if
	} // addUser

	public static String getEncPass (String password) {
		return execute (QUERY, "SELECT MD5('" + password + "')");
	} // getEncPass

	public static boolean passwordMatches ( String login, String encryptedPassword ) {
		String pass = execute (QUERY,
			"SELECT password FROM " + NAMESPACE + "_blog_users WHERE login = '" + login + "'");
		return ( encryptedPassword != null && encryptedPassword.equals (pass) );
	} // passwordMatches

} // UserDAO
