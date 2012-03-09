/*
 * ImageDAO.java
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

public class ImageDAO extends BlogDAO {

	public static ArrayList getImageIds() {
		String sql =
			"SELECT image_id FROM " + NAMESPACE + "_blog_images ORDER BY image_id";
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
	} // getImageIds

	public static Image getImage (Long id) {
		Image image = null;
		String sql =
			"SELECT user_id, path, caption " +
			"FROM " + NAMESPACE + "_blog_images WHERE image_id = " + id;
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
				image = new Image (id);
				image.userId = new Long ( rset.getString (1) );
				image.path = rset.getString (2);
				image.caption = rset.getString (3);
			} // if
		} catch (Exception e) {
			image = new Image (new Long (-1));
			image.caption = e.toString();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try

		if ( image == null ) {
			image = new Image (new Long (-2));
			image.caption = sql;
		} // if
		return image;
	} // getImage

	public static String addImage (String imageURL, String userLogin) {
		String sql =
			"SELECT user_id FROM " + NAMESPACE + "_blog_users WHERE login = '" +
			userLogin + "'";
		String userId = execute (QUERY, sql);
		sql =
			"INSERT INTO " + NAMESPACE + "_blog_images (user_id, path, caption)" +
			"VALUES ("+userId+", '" + imageURL + "', '" + imageURL + "')";
		Connection conn = getConnection();
		if ( conn == null ) {
			return "null connection";
		} // if
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
		} catch (Exception e) {
			return sql + " : " + e.toString();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return null;
	} // addImage

	public static boolean canEditImages (String login) {
		String sql =
			"SELECT COUNT(1) FROM " + NAMESPACE + "_blog_users " +
			"WHERE login = '" + login + "' AND can_post_images = 1";
		boolean canEdit = false;
		Connection conn = getConnection();
		if ( conn == null ) {
			return false;
		} // if
		ResultSet rset = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery (sql);
			if ( rset.next() ) {
				canEdit = ( "1".equals (rset.getString (1) ) );
			} // if
		} catch (Exception e) {
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
		return canEdit;
	} // canEditImages

	public static void updateImage (String imageId, String newCaption) {
		String sql =
			"UPDATE " + NAMESPACE + "_blog_images SET caption = '" + newCaption +
			"' WHERE image_id = " + imageId;
		Connection conn = getConnection();
		if ( conn == null ) {
			return;
		} // if
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
		} catch (Exception e) {
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
	} // updateImage

	public static void deleteImage (String imageId) {
		String sql =
			"DELETE FROM " + NAMESPACE + "_blog_images WHERE image_id = " + imageId;
		Connection conn = getConnection();
		if ( conn == null ) {
			return;
		} // if
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate (sql);
		} catch (Exception e) {
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {}
		} // try
	} // deleteImage

} // ImageDAO
