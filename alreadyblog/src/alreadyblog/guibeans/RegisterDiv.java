/*
 * RegisterDiv.java
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
package alreadyblog.guibeans;

import alreadyblog.AlreadyBlogServlet;
import alreadyblog.db.LoginManager;
import alreadyblog.util.GUIConstants;

public class RegisterDiv implements GuiBeanDiv {

	public static final String ADD_USER_COMMAND = "cmdAddUser";

	private String blogPage = null;

	public RegisterDiv (String newBlogPage) {
		blogPage = newBlogPage;
	} // constructor

	private String loginParamName = LoginManager.LOGIN_PARAM;
	private String passwordParamName = LoginManager.PASSWORD_PARAM;
	private String emailParamName = AlreadyBlogServlet.EMAIL_PARAM;

	public String getDiv() {
		StringBuffer buf = new StringBuffer();
		buf.append ("\n<div id=\"registerDiv\">\n");
		buf.append (" <form id=\"registerSection\" method=\"post\" action=\"");
		buf.append ( blogPage );
		buf.append ("\">\n");
		buf.append ("  <input id=\"cmd\" type=\"hidden\" name=\"cmd\" value=\"");
		buf.append ( ADD_USER_COMMAND );
		buf.append ("\" />\n");
		buf.append ("  <table border=\"0\">\n");
		buf.append ("   <tr><td>New Login</td></tr>\n");
		buf.append ("   <tr><td><input class=\"textentry\" type=\"text\" ");
		buf.append ("name=\"");
		buf.append (loginParamName);
		buf.append ("\" size=\"12\" /></td></tr>\n");
		buf.append ("   <tr><td>New Password</td></tr>\n");
		buf.append ("   <tr><td><input class=\"textentry\" type=\"password\" ");
		buf.append ("name=\"");
		buf.append (passwordParamName);
		buf.append (" size=\"12\" /></td></tr>\n");
		buf.append ("   <tr><td>Confirm Password</td></tr>\n");
		buf.append ("   <tr><td><input class=\"textentry\" type=\"password\" ");
		buf.append ("name=\"passwordConfirm\" size=\"12\" /></td></tr>\n");
		buf.append ("   <tr><td>Email Address</td></tr>\n");
		buf.append ("   <tr><td><input class=\"textentry\" type=\"text\" ");
		buf.append ("name=\"");
		buf.append (emailParamName);
		buf.append ("\" size=\"24\" /></td></tr>\n");
		buf.append ("   <tr><td>&nbsp;</td></tr><tr><th><input type=\"submit\" ");
		buf.append ("value=\"Register\" /></th></tr>\n");
		buf.append ("  </table>\n");
		buf.append (" </form>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // getDiv

} // RegisterDiv
