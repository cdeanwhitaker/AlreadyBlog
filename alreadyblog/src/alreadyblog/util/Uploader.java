/*
 * Uploader.java
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

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import alreadyblog.ImageViewerServlet;
import alreadyblog.BlogConfigIF;

public class Uploader {

	public static final String UPLOAD_COMMAND = "cmdUpload";

	private BlogConfigIF blogInfo = null;
	private Uploader() {} // private contructor`

	public static Uploader uploader = new Uploader();

	public static Uploader getInstance (BlogConfigIF newBlogInfo) {
		uploader.blogInfo = newBlogInfo;
		return uploader;
	} // getInstance

	public String getHTML() {
		StringBuffer buf = new StringBuffer();
		buf.append ("<div id=\"fileUploader\">\n");
		buf.append (" <form action=\"");
		buf.append ( ImageViewerServlet.URL );
		buf.append ("?cmd=");
		buf.append ( UPLOAD_COMMAND );
		buf.append ("\" enctype=\"MULTIPART/FORM-DATA\" method=\"POST\">\n");
		buf.append ("Upload File Path\n");
		buf.append ("  <input type=\"file\" name=\"fileName\" />\n");
		buf.append ("  <input type=\"submit\" name=\"Upload File\" />\n");
		buf.append (" </form>\n");
		buf.append ("</div>\n");
		return buf.toString();
	} // getHTML

	public String doUpload (HttpServletRequest req) throws IOException {
		String savedName = null;
		String fileName = "";
		ServletInputStream in = req.getInputStream();
		byte[] line = new byte[128];
		int i = in.readLine (line, 0, 128);
		int boundaryLength = i - 2;
		String boundary = new String (line, 0, boundaryLength);

		while (i != -1) {
			String newLine = new String (line, 0, i);

			i = in.readLine (line, 0, 128);
			newLine = new String (line, 0, i);

			if (newLine.startsWith ("Content-Disposition: form-data; name=\"")) {
				String s = new String (line, 0, i-2);
				int fileNameStart = s.indexOf ("filename=\"");
				if ( fileNameStart != -1 ) {
					fileNameStart += 10;
					int fileNameEnd = s.length() - 1;
					fileName = s.substring (fileNameStart, fileNameEnd);
				} // if
			} // if

			i = in.readLine (line, 0, 128);
			i = in.readLine (line, 0, 128);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			newLine = new String (line, 0, i);

			while ( i != -1 && ! newLine.startsWith (boundary)) {
				buffer.write (line, 0, i);
				i = in.readLine (line, 0, 128);
				newLine = new String (line, 0, i);
			} // while
			try {
				savedName = blogInfo.getServerPath() + blogInfo.getPicSubdir() + "/" + fileName;
//				savedName = blogInfo.getServerPath() + "WEB-INF/classes" + "/" + fileName;
				RandomAccessFile f = new RandomAccessFile (savedName, "rw");
				byte[] bytes = buffer.toByteArray();
				f.write (bytes, 2, bytes.length - 2);
				f.close();
			} catch (Exception e) {
				savedName = e.toString();
			} // try
			i = in.readLine (line, 0, 128);
		} // while
		//return blogInfo.getPicSubdir() + "/" + fileName;
		return savedName;
		//return fileName;
	} // doUpload

} // Uploader
