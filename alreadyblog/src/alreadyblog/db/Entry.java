/*
 * Entry.java
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

import java.util.ArrayList;

public class Entry {

	public Long id = null;

	public Long parent_id = null;

	public String login = null;

	public String message = null;

	public String dateTime = null;

	public Entry (Long newId) {
		id = newId;
	} // constructor

	public boolean isReply() {
		return ( parent_id != null );
	} // isReply

	public Long getEntryId() {
		return id;
	} // getEntryId

	public String toHTMLRow (boolean isLoggedIn, boolean isReply, String theLogin,
			String action, String replyCmd, String replyIdParam) {

		boolean isByLoginUser = ( login != null && login.equals (theLogin) );
		StringBuffer buf = new StringBuffer();

		buf.append ("<tr>\n");
		buf.append (" <form id=\"blogForm\" method=\"post\" action=\"");
		buf.append ( action );
		buf.append ("\">\n");
		if ( isReply ) {
			if ( isByLoginUser && isLoggedIn ) {
				buf.append ("  <td class=\"replyNameTDGreen\" valign=\"top\">Reply by<p/>\n");
			} else {
				buf.append ("  <td class=\"replyNameTD\" valign=\"top\">Reply by<p/>\n");
			} // if
			buf.append ( login );
			buf.append ("    <p/>\n");
			buf.append ( dateTime );
			buf.append ("  </td>\n");
			if ( isByLoginUser && isLoggedIn ) {
				buf.append ("  <td class=\"replyMsgTDGreen\" valign=\"top\">\n");
			} else {
				buf.append ("  <td class=\"replyMsgTD\" valign=\"top\">\n");
			} // if
			buf.append ( message );
			buf.append ("  </td>\n");
			if ( isByLoginUser && isLoggedIn ) {
				buf.append ("<td class=\"replyReplyTDGreen\" valign=\"top\">&nbsp;</td>\n");
			} else {
				buf.append ("<td class=\"replyReplyTD\" valign=\"top\">&nbsp;</td>\n");
			} // if
		} else {
			if ( isByLoginUser && isLoggedIn ) {
				buf.append ("  <td class=\"nameTDGreen\" valign=\"top\">\n");
			} else {
				buf.append ("  <td class=\"nameTD\" valign=\"top\">\n");
			} // if
			buf.append ( login );
			buf.append ("    <p/>\n");
			buf.append ( dateTime );
			buf.append ("  </td>\n");
			if ( isByLoginUser && isLoggedIn ) {
				buf.append ("  <td class=\"msgTDGreen\" valign=\"top\">\n");
			} else {
				buf.append ("  <td class=\"msgTD\" valign=\"top\">\n");
			} // if
			buf.append ( message );
			buf.append ("  </td>\n");
			if ( isLoggedIn ) {
				if ( isByLoginUser ) {
					buf.append ("  <td class=\"replyTDGreen\" valign=\"top\"><input type=\"submit\" value=\"Reply\" /></td>\n");
				} else {
					buf.append ("  <td class=\"replyTD\" valign=\"top\"><input type=\"submit\" value=\"Reply\" /></td>\n");
				} // if
				buf.append ("  <input type=\"hidden\" name=\"cmd\" value=\"");
				buf.append ( replyCmd );
				buf.append ("\" />\n");
				buf.append ("  <input type=\"hidden\" name=\"");
				buf.append ( replyIdParam );
				buf.append ("\" value=\"");
				buf.append ( id );
				buf.append ("\" />\n");
			} else {
				buf.append ("<td class=\"replyTD\" valign=\"top\">&nbsp;</td>\n");
			} // if
		} // if
		buf.append (" </form>\n");
		buf.append ("</tr>\n");
		return buf.toString();
	} // toHTMLRow

	public static String add (String login, String message) {
		return EntryDAO.add (login, message);
	} // add

	public static String reply (String login, String message, Long parentId) {
		return EntryDAO.reply (login, message, parentId);
	} // reply

	public static Entry getEntry (Long id) {
		return EntryDAO.getEntry (id);
	} // getEntry

	public static ArrayList getEntryIds() {
		return EntryDAO.getEntryIds();
	} // getEntryIds

	public static ArrayList getChildEntryIds (Long parentId) {
		return EntryDAO.getChildEntryIds (parentId);
	} // getChildEntryIds

	public static ArrayList getArchivedEntryIds() {
		return EntryDAO.getArchivedEntryIds();
	} // getArchivedEntryIds

	public static ArrayList getArchivedChildEntryIds (Long parentId) {
		return EntryDAO.getArchivedChildEntryIds (parentId);
	} // getChildEntryId

	public static Entry getArchivedEntry (Long id) {
		return EntryDAO.getArchivedEntry (id);
	} // getArchivedEntry

	public static ArrayList getParentEntryIdsToArchive() {
		return EntryDAO.getParentEntryIdsToArchive();
	} // getParentEntryIdsToArchive

	public static boolean archiveEntry (Long entryId) {
		return EntryDAO.archiveEntry (entryId);
	} // archiveEntry

	public static void deleteEntry (Long entryId) {
		EntryDAO.deleteEntry (entryId);
	} // deleteEntry

} // Entry
