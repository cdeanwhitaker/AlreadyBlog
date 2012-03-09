/*
 * EntryDAO.java
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

public class EntryDAO extends BlogDAO {

	public static ArrayList getParentEntryIdsToArchive() {
		String sql = "SELECT entry_id FROM " + NAMESPACE + "_blog_entries " +
			"WHERE parent_entry_id IS NULL AND " +
			"(NOW() > DATE_ADD( dt, INTERVAL " + blogInfo.getArchiveDays() + " DAY ) )";
//			"DATEDIFF(NOW(), dt) > " + blogInfo.getArchiveDays();
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		ArrayList idList = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				String idStr = rset.getString (1);
				idList.add (new Long (idStr));
			} // while
		} catch (SQLException sqle) {
			idList.add ("sql: " + sql + " " + sqle);
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return idList;
	} // getEntryIdsToArchive

	public static boolean archiveEntry (Long entryId) {
		boolean success = false;
		String sql =
			"INSERT INTO " + NAMESPACE + "_blog_entries_hist " +
				"(SELECT * from " + NAMESPACE + "_blog_entries WHERE entry_id = " + entryId + ")";
		Connection conn = getConnection();
		if ( conn == null ) {
			return false;
		} // if
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
			success = true;
		} catch (SQLException sqle) {
			System.err.println (sqle.toString());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return success;
	} // archiveEntry

	public static void deleteEntry (Long entryId) {
		String sql =
			"DELETE FROM " + NAMESPACE + "_blog_entries WHERE entry_id = " + entryId +
			" AND parent_entry_id IS NOT NULL";
		Connection conn = getConnection();
		if ( conn == null ) {
			return;
		} // if
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
		sql =
			"DELETE FROM " + NAMESPACE + "_blog_entries WHERE entry_id = " + entryId +
			" AND parent_entry_id IS NULL";
			stmt.close();
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
		} catch (SQLException sqle) {
			System.err.println (sqle.toString());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
	} // deleteEntry


	public static ArrayList getEntryIds() {
		return getEntryIds (false);
	} // getEntryIds

	public static ArrayList getArchivedEntryIds() {
		return getEntryIds (true);
	} // getArchivedEntryIds


	public static ArrayList getArchivedChildEntryIds (Long parentId) {
		return getChildEntryIds (parentId, true);
	} // getChildEntryId

	public static ArrayList getChildEntryIds (Long parentId) {
		return getChildEntryIds (parentId, false);
	} // getChildEntryId


	public static Entry getArchivedEntry (Long id) {
		return getEntry (id, true);
	} // getArchivedEntry

	public static Entry getEntry (Long id) {
		return getEntry (id, false);
	} // getArchivedEntry


	private static ArrayList getEntryIds (boolean useArchived) {
		String sql =
			"SELECT entry_id FROM " + NAMESPACE + "_blog_entries" +
			(useArchived ? "_hist" : "") +
			" WHERE parent_entry_id IS NULL ORDER by entry_id DESC";
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		ArrayList idList = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				String idStr = rset.getString (1);
				idList.add (new Long (idStr));
			} // while
		} catch (SQLException sqle) {
			idList.add (sqle);
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return idList;
	} // getEntryIds

	private static ArrayList getChildEntryIds
			(Long parentId, boolean useArchived) {

		String sql =
			"SELECT entry_id FROM " + NAMESPACE + "_blog_entries" +
			(useArchived ? "_hist" : "") +
			" WHERE parent_entry_id = " +
			parentId + " ORDER by entry_id DESC";
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		ArrayList idList = new ArrayList();
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			while ( rset.next() ) {
				String idStr = rset.getString (1);
				idList.add (new Long (idStr));
			} // while
		} catch (SQLException sqle) {
			idList.add (sqle);
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return idList;
	} // getChildEntryIds

	private static Entry getEntry (Long id, boolean useArchived) {
		Entry entry = null;
		String sql =
			"SELECT be.parent_entry_id, bu.login, be.entry, " +
			"DATE_FORMAT(be.dt, '%c/%e/%Y %T') " +
			"FROM " + NAMESPACE + "_blog_entries" +
			(useArchived ? "_hist" : "") +
			" be INNER JOIN " + NAMESPACE + "_blog_users bu ON be.user_id = bu.user_id " +
			"WHERE be.entry_id = " + id;
		Connection conn = getConnection();
		if ( conn == null ) {
			return null;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			if ( rset.next() ) {
				entry = new Entry (id);
				String parentId = rset.getString (1);
				if ( (parentId != null) && ( ! parentId.equals ("null") ) ) {
					entry.parent_id = id;
				} // if
				entry.login = rset.getString (2);
				entry.message = rset.getString (3);
				entry.dateTime = rset.getString (4);
			} // if
		} catch (Exception e) {
			entry = new Entry (new Long (-1));
			entry.login = "ERROR";
			entry.message = e.toString();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try

		if ( entry == null ) {
			entry = new Entry (new Long (-2));
			entry.login = "ERROR";
			entry.message = sql;
		} // if
		return entry;
	} // getEntry

	public static String add (String login, String message) {
		if ( login != null ) {
			if ( message != null ) {
				message = message.replaceAll ("'","");
				String id = execute (QUERY,
					"SELECT user_id FROM " + NAMESPACE + "_blog_users WHERE login = '" + login + "'");
				String errors = execute (UPDATE,
					"INSERT INTO " + NAMESPACE + "_blog_entries (user_id, entry, dt) VALUES (" +
					id + ", '" + message + "', NOW())");
				if ("null".equals (errors) ) {
					errors = "success";
				} // if
				return errors;
			} else {
				return "Null message";
			} // if
		} else {
			return "Null login";
		} // if
	} // add

	public static String reply (String login, String message, Long parentId) {
		if ( login != null ) {
			if ( message != null ) {
				message = message.replaceAll ("'","");
				String id = execute (QUERY,
					"SELECT user_id FROM " + NAMESPACE + "_blog_users WHERE login = '" + login + "'");
				String errors = execute (UPDATE,
					"INSERT INTO " + NAMESPACE + "_blog_entries (parent_entry_id, user_id, " +
						"entry, dt) VALUES (" + parentId + ", " + id + ", '" +
						message + "', NOW())");
				if ("null".equals (errors) ) {
					errors = "success";
				} // if
				return errors;
			} else {
				return "Null message";
			} // if
		} else {
			return "Null login";
		} // if
	} // reply

} // EntryDAO
