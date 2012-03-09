/*
 * User.java
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

public class User {

	public static ArrayList getLogins() {
		return UserDAO.getLogins();
	} // getLogins

	public static String addUser (String login, String password, String email) {
		return UserDAO.addUser (login, password, email);
	} // addUser

} // User
