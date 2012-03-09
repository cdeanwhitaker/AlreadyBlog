/*
 * Image.java
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

import alreadyblog.AlreadyBlogServlet;
import alreadyblog.ImageViewerServlet;

public class Image {

	public static final String DELETE_IMAGE_COMMAND = "Delete Image";
	public static final String SAVE_CAPTION_COMMAND = "Save Caption";

	public Long id = null;

	public Long userId = null;

	public String caption = null;

	public String path = null;

	public Image (Long newId) {
		id = newId;
	} // constructor

	public String toHTMLRow (String action, boolean isLoggedIn, boolean isEditable) {
		StringBuffer buf = new StringBuffer();
		buf.append ("<tr>\n");
		buf.append ("  <td class=\"image\" align=\"center\" valign=\"top\">\n");
		buf.append ("<table>");
		if ( isLoggedIn ) {
			buf.append ("<tr><td align=\"center\">\n");
			buf.append (" <form id=\"blogImageForm\" method=\"post\" action=\"");
			buf.append ( AlreadyBlogServlet.URL );
			buf.append ( "#ENTRY" );
			buf.append ("\">\n");
			buf.append (" <input type=\"hidden\" name=\"");
			buf.append ( ImageViewerServlet.IMAGE_ID_PARAM );
			buf.append ("\" value=\"");
			buf.append ( id );
			buf.append ("\" />\n");
			buf.append ("<input type=\"submit\" name=\"cmd\" value=\"Blog This Image\" />\n");
			buf.append (" </form>\n");
			buf.append (" </td></tr>");
		} // if
		if ( isEditable ) {
			buf.append ("<tr><td align=\"center\">\n");
			buf.append (" <form id=\"imageForm\" method=\"post\" action=\"");
			buf.append ( action );
			buf.append ("#ENTRY\">\n");
			buf.append ("		<img src=\"");
			buf.append ( path );
			buf.append ("\" />");
			buf.append ("<br />");
			buf.append ("<input type=\"submit\" name=\"cmd\" value=\"Delete Image\" />\n");
			buf.append ("<input type=\"text\" size=\"48\" ");
			buf.append ("name=\"captionParam\" value=\"");
			buf.append ( caption );
			buf.append ("\" />\n");
			buf.append ("<input type=\"submit\" name=\"cmd\" value=\"Save Caption\" />\n");
			buf.append (" <input type=\"hidden\" name=\"imageIdParam\" value=\"");
			buf.append ( id );
			buf.append ("\" />\n");
			buf.append (" </form>\n");
			buf.append ("</td></tr>");
		} else {
			buf.append ("<tr><td align=\"center\">\n");
			buf.append ("		<img src=\"");
			buf.append ( path );
			buf.append ("\" />");
			buf.append ("</td></tr><tr><td align=\"center\">");
			buf.append ( caption );
			buf.append ("</td></tr>");
		} // if
		buf.append ("</table>\n");
		buf.append ("</td></tr>");
		return buf.toString();
	} // toHTMLRow

	public static Image getImage (Long id) {
		return ImageDAO.getImage (id);
	} // getImage

} // Image
