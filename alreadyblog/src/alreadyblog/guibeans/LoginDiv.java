/*
 * LoginDiv.java
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

import java.util.ArrayList;

import alreadyblog.db.LoginManager;
import alreadyblog.util.GUIConstants;

public class LoginDiv implements GuiBeanDiv {

	public static final String LOG_IN_COMMAND = "cmdLogIn";
	public static final String LOG_OUT_COMMAND = "cmdLogOut";

	private ArrayList loginList = null;
	private String blogPage = null;

	public LoginDiv (String newBlogPage, ArrayList newLoginList) {
		loginList = newLoginList;
		blogPage = newBlogPage;
	} // constructor

	private String loginParamName = LoginManager.LOGIN_PARAM;
	private String passwordParamName = LoginManager.PASSWORD_PARAM;

	public String getDiv() {
		StringBuffer buf = new StringBuffer();
		buf.append ("\n<div id=\"loginDiv\">");
		buf.append (" <table border=\"0\">\n");

		buf.append ("  <form id=\"loginSection\" method=\"post\" action=\"");
		buf.append ( blogPage );
		buf.append ("\">\n");
		buf.append ("   <input id=\"");
		buf.append ( CMD );
		buf.append ("\" type=\"hidden\" name=\"cmd\" value=\"");
		buf.append ( LOG_IN_COMMAND );
		buf.append ("\" />\n");
		buf.append ("   <tr>\n");
		buf.append ("    <td>Login</td></tr>\n");
		buf.append ("   <tr><td><select name=\"");
		buf.append (loginParamName);
		buf.append ("\">\n");
		if ( loginList != null ) {
			for (int i=0; i<loginList.size(); i++) {
				String login = (String) loginList.get (i);
				buf.append ("    <option value=\"");
				buf.append ( login );
				buf.append ("\">");
				buf.append ( login );
				buf.append ("</option>\n");
			} // for
		} // if
		buf.append ("    </td>\n");
		buf.append ("   </tr>\n");
		buf.append ("   <tr><td>Password</td></tr>");
		buf.append ("   <tr><td><input class=\"textentry\" type=\"password\" ");
		buf.append ("name=\"");
		buf.append (passwordParamName);
		buf.append ("\" size=\"12\" /></td></tr>\n");
		buf.append ("   <tr><td>&nbsp;</td></tr><tr><th><input type=\"submit\" ");
		buf.append ("value=\"Log In\" /></th></tr>\n");
		buf.append ("  </form>\n");

		buf.append ("  <form id=\"logoutSection\" method=\"post\" action=\"");
		buf.append ( blogPage );
		buf.append ("\">\n");
		buf.append ("   <input id=\"cmd\" type=\"hidden\" name=\"cmd\" value=\"");
		buf.append ( LOG_OUT_COMMAND );
		buf.append ("\" />\n");
		buf.append ("   <tr><td>&nbsp;</td></tr><tr><th><input type=\"submit\" ");
		buf.append ("value=\"Log Out\" /></th></tr>\n");
		buf.append ("  </form>\n");

		buf.append (" </table>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // getDiv

} // LoginDiv
