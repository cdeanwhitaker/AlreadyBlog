/*
 * ImageViewerServlet.java
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
package alreadyblog;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;

import alreadyblog.db.ImageDAO;
import alreadyblog.db.Image;
import alreadyblog.db.LoginManager;
import alreadyblog.guibeans.GuiBeanDiv;
import alreadyblog.util.GUIConstants;
import alreadyblog.util.Uploader;

public class ImageViewerServlet extends HttpServlet {

	private static BlogConfigIF blogInfo = AlreadyBlogServlet.blogInfo;
	public static final String URL = blogInfo.getWebContext() + "img";

	private static final String UPLOAD_COMMAND = "cmdUpload";

	public static final String IMAGE_ID_PARAM = "imageIdParam";
	public static final String CAPTION_PARAM = "captionParam";

	public void doPost ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {

		String cmd = req.getParameter (GuiBeanDiv.CMD);
		Uploader uploader = Uploader.getInstance (blogInfo);

		res.setContentType ("text/html");

		String login = LoginManager.establishLoginFromCookie (req);
		AlreadyBlogServlet.debug ( "login from cookie:" + login );
		boolean isLoggedIn = (login != null);
		boolean canEditImages = ImageDAO.canEditImages (login);

		PrintWriter out = res.getWriter();

		out.println ( getPageHeader() );

		if ( UPLOAD_COMMAND.equals (cmd) ) {
//out.println ("<p/>saving file");
			String fileName = uploader.doUpload (req);
//out.println ("<p/>fileName:" + fileName);
			String url = "<img src=\"" + fileName + "\" />";
//out.println ("<p/>ImageDAO.addImage(" + fileName + "," + login +")");
			String errors = ImageDAO.addImage (fileName, login);
//out.println ("<p/>errors:" + errors);
		} else if ( Image.SAVE_CAPTION_COMMAND.equals (cmd) ) {
			String imageId = req.getParameter (IMAGE_ID_PARAM);
			String newCaption = req.getParameter (CAPTION_PARAM);
			ImageDAO.updateImage (imageId, newCaption);
		} else if ( Image.DELETE_IMAGE_COMMAND.equals (cmd) ) {
			String imageId = req.getParameter (IMAGE_ID_PARAM);
			ImageDAO.deleteImage (imageId);
		} // if

		out.println ("<table border=\"0\">");

		out.println ( getImageFiles (isLoggedIn, canEditImages) );
		out.println (" <tr><td>&nbsp;<br/>&nbsp;</td></tr>");

		if ( canEditImages ) {
			out.println ("<tr><td align=\"center\">");
			out.println ( uploader.getHTML() );
			out.println (" </td></tr>");
		} // if

		out.println ("</table>");
		out.println ( getPageFooter() );
	} // doPost


	public void doGet ( HttpServletRequest req,
			HttpServletResponse res ) throws IOException, ServletException {
		doPost (req, res);
	} // doGet

	private String getPageHeader() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<html>\n");
		buf.append ("<head>\n");
		buf.append ("<link rel=\"shortcut icon\" href=\"");
		buf.append ( blogInfo.getIconImage() );
		buf.append ("\" />\n");
		buf.append ("<style>\n");
		buf.append ( GUIConstants.getCSS() );
		buf.append ("</style>\n");
		buf.append ("<title>Image Viewer</title>\n");
		buf.append ("</head>\n");
		buf.append ("<body background=\"");
		buf.append ( blogInfo.getBackgroundImage() );
		buf.append ("\">\n");
		buf.append (" <center>\n");
		buf.append (" <table width=\"100%\" border=\"0\">\n");
		buf.append (" <tr>\n\n");
		buf.append ("  <td align=\"center\" width=\"60%\" valign=\"top\">\n\n");
		buf.append ("   <a style=\"text-decoration: none;\" href=\"");
		buf.append ( URL );
		buf.append ("\"><h2>Image Viewer</h2></a>\n");
		buf.append ("  </td>\n\n");
		buf.append (" </tr>\n");
		buf.append (" </table>\n");
		return buf.toString();
	} // getPageHeader

	private String getPageFooter () {
		StringBuffer buf = new StringBuffer();
		buf.append ("	<p><a style=\"text-decoration: none;\" href=\"");
		buf.append ( blogInfo.getWebContext() );
		buf.append ("\"><h2>Back To Blog</h2></a><br/>");
		buf.append ("	<p><a href=\"");
		buf.append ( blogInfo.getHomePageURL() );
		buf.append ("\"><img ");
		buf.append ("style=\"border: 0px;\" src=\"");
		buf.append ( blogInfo.getHomePageLinkPic() );
		buf.append ("\"/></a>\n");
		buf.append ("	</center>\n");
		buf.append ("</body>\n");
		buf.append ("</html>\n");
		return buf.toString();
	} // getPageFooter


	private String getImageFiles (boolean isLoggedIn, boolean canEditImages) {
		ArrayList imageIdList = ImageDAO.getImageIds();
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<imageIdList.size(); i++) {
			Long id = (Long) imageIdList.get (i);
			Image image = ImageDAO.getImage (id);
			buf.append ( image.toHTMLRow (URL, isLoggedIn, canEditImages) );
			buf.append ("\n");
			buf.append ("<tr><td>&nbsp;<br/>&nbsp;</td></tr>");
		} // for
		return buf.toString();
	} // getImageFiles

} // ImageViewerServlet
