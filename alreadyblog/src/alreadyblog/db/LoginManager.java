/*
 * LoginManager.java
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

import javax.servlet.*;
import javax.servlet.http.*;

import alreadyblog.AlreadyBlogServlet;
import alreadyblog.BlogConfigIF;

public class LoginManager extends HttpServlet {

	private static final BlogConfigIF blogInfo = AlreadyBlogServlet.blogInfo;

	public static final String LOGIN_PARAM = "loginParam";
	public static final String PASSWORD_PARAM = "passwordParam";

	private static final String COOKIE_NAME = blogInfo.getCookieName();
	private static final int COOKIE_LIFE = blogInfo.getCookieLifeInSecs();

	/**
	 *  On a login attempt, verify the login name and password in the DB.
	 *    If login succeeds, set the login cookie.
	**/
	public static String attemptLogin (HttpServletRequest req, HttpServletResponse res) {
		String tryLogin = req.getParameter (LOGIN_PARAM);
		String tryPassword = req.getParameter (PASSWORD_PARAM);
		boolean isLoggedIn = UserDAO.attemptLogin (tryLogin, tryPassword);
		if ( isLoggedIn ) {
			makeLoginCookie (res, tryLogin, tryPassword);
			String encryptedPassword = UserDAO.getEncPass (tryPassword);
			if ( UserDAO.passwordMatches ( tryLogin, encryptedPassword ) ) {
				return tryLogin;
			} // if
		} // if
		return null;
	} // attemptLogin

	public static String establishLoginFromCookie (HttpServletRequest req) {
		Cookie [] cookies = req.getCookies();
		if ( cookies != null ) {
			for (int i=0; i<cookies.length; i++) {
				Cookie cookie = cookies[i];
				if ( cookie != null ) {
					if ( COOKIE_NAME.equals (cookie.getName()) ) {
						String value = cookie.getValue();
						if ( value != null ) {
							String newLogin = value.substring ( 0, value.indexOf (":::") );
							String encryptedPassword = value.substring
								( value.indexOf (":::") + 3 );
							if ( UserDAO.passwordMatches ( newLogin, encryptedPassword ) ) {
								//login = newLogin;
								return newLogin;
							} // if
						} // if
					} // if
				} // if
			} // for
		} // if
		return null;
	} // establishLoginFromCookie
	
	/**
	 *  Expire the browser cookie if it exists and reset login info
	**/
	public static void expireCookie (HttpServletResponse res, HttpServletRequest req) {
		try {
			Cookie [] cookies = req.getCookies();
			if ( cookies != null ) {
				for (int i=0; i<cookies.length; i++) {
					Cookie cookie = cookies[i];
					if ( cookie != null ) {
						if ( COOKIE_NAME.equals (cookie.getName()) ) {
							cookie.setMaxAge (0);
							res.addCookie ( cookie );
						} // if
					} // if
				} // for
			} // if
		} catch (NullPointerException npe) {
			System.err.println (npe);
		}
	} // expireCookie

	/**
	 *  Add browser cookie with the login and encrypted password
	**/
	private static void makeLoginCookie (HttpServletResponse res,
			String login, String password) {
		String encryptedPassword = UserDAO.getEncPass (password);
		Cookie loginCookie = new Cookie (COOKIE_NAME, login + ":::" + encryptedPassword);
		loginCookie.setMaxAge (COOKIE_LIFE);
		res.addCookie (loginCookie);
	} // makeLoginCookie

} // LoginManager
