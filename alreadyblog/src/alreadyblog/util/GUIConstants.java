/*
 * GUIConstants.java
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
package alreadyblog.util;

public class GUIConstants {

	public static final int maxEntryLen = 225;
	public static final int maxNameLen = 32;

	public static final String BG_COLOR = "#97A0CC";
	public static final String FONT_COLOR = "#662233";

	public static String getCSS() {
		StringBuffer buf = new StringBuffer();
		buf.append ("h2 {\n");
		buf.append ("	color: ");
		buf.append ( FONT_COLOR );
		buf.append (";\n");
		buf.append ("	font-family: URW Gothic L,sans-serif;\n");
		buf.append ("	font-weight: bold;\n");
		buf.append ("	text-decoration: none;\n");
		buf.append ("}\n");
		buf.append ("td {\n");
		buf.append ("	font-family: URW Gothic L,sans-serif;\n");
		buf.append ("	color: ");
		buf.append ( FONT_COLOR );
		buf.append (";\n");
		buf.append ("}\n");
		buf.append ("textentry {\n");
		buf.append ("	background-color: ");
		buf.append ( FONT_COLOR );
		buf.append (";\n");
		buf.append ("}\n");
		buf.append (".capt {\n");
		buf.append ("	font-weight: bold;\n");
		buf.append ("	font-family: URW Gothic L,sans-serif;\n");
		buf.append ("	caption: ");
		buf.append ( FONT_COLOR );
		buf.append (";\n");
		buf.append ("}\n");
		buf.append (".headerTH {\n");
		buf.append ("	border-left: 0px;\n");
		buf.append ("	border-top: 0px;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".nameTD {\n");
		buf.append ("	border-left: 1px solid #000000;\n");
		buf.append ("	border-top: 1px solid #000000;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".nameTDGreen {\n");
		buf.append ("	border-left: 1px solid #00FF00;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyNameTD {\n");
		buf.append ("	border-left: 1px solid #000000;\n");
		buf.append ("	border-top: 0px;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyNameTDGreen {\n");
		buf.append ("	border-left: 1px solid #00FF00;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".msgTD {\n");
		buf.append ("	border-left: 1px solid #000000;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	border-top: 1px solid #000000;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".msgTDGreen {\n");
		buf.append ("	border-left: 1px solid #00FF00;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyMsgTD {\n");
		buf.append ("	border-left: 1px solid #000000;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	border-top: 0px;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyMsgTDGreen {\n");
		buf.append ("	border-left: 1px solid #00FF00;\n");
		buf.append ("	border-right: 0px;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyTD {\n");
		buf.append ("	border-right: 1px solid #000000;\n");
		buf.append ("	border-top: 1px solid #000000;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	border-left: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyTDGreen {\n");
		buf.append ("	border-right: 1px solid #00FF00;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	border-left: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyReplyTD {\n");
		buf.append ("	border-right: 1px solid #000000;\n");
		buf.append ("	border-top: 0px solid #000000;\n");
		buf.append ("	border-bottom: 1px solid #000000;\n");
		buf.append ("	border-left: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		buf.append (".replyReplyTDGreen {\n");
		buf.append ("	border-right: 1px solid #00FF00;\n");
		buf.append ("	border-top: 1px solid #00FF00;\n");
		buf.append ("	border-bottom: 1px solid #00FF00;\n");
		buf.append ("	border-left: 0px;\n");
		buf.append ("	padding: 2px;\n");
		buf.append ("	margin: 0px;\n");
		buf.append ("}\n");
		return buf.toString();
	} // getCSS

} // GUIConstants
