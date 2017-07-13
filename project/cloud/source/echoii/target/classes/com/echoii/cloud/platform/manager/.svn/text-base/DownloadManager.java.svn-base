package com.echoii.cloud.platform.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;


public class DownloadManager {

	private static volatile DownloadManager MANAGER;
	
	public static DownloadManager getInstance() {
		if (MANAGER == null) {
			synchronized (DownloadManager.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (MANAGER == null) {
					MANAGER = new DownloadManager();
				}
			}
		}
		return MANAGER;
	}

	DownloadManager() {

	}
	
	
	public void download(File file,String filename, HttpServletResponse response, HttpServletRequest request) {
		
		try {
			FileInputStream fis = new FileInputStream(file);

			response.reset();
			response.setContentType(CheckType(filename));
			response.setContentLength((int) file.length());

			if (request.getHeader("User-Agent").contains("MSIE")) {
				response.setHeader(
						"Content-Disposition",
					//	"attachment;filename ="
						"inline;filename ="
								+ java.net.URLEncoder.encode(filename, "UTF-8"));

			} else {
				String enableFileName = "=?UTF-8?B?"
						+ (new String(Base64.encodeBase64(filename
								.getBytes("UTF-8")))) + "?=";
				response.setHeader("Content-Disposition",
						//"attachment;filename =" + enableFileName);
						"inline;filename =" + enableFileName);
			}

			response.flushBuffer();
			OutputStream sos = response.getOutputStream();
	
			long k = 0;
			byte[] bytes = new byte[2048];
			while (k < file.length()) {
				int j = fis.read(bytes, 0, 2048);
				k = k + j;
				sos.write(bytes, 0, j);

			}
			sos.flush();
			if (sos != null) {
				sos.close();
			}
			if (fis != null) {
				fis.close();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		} 
	}
	
    public void errorCallBack( String callback,  HttpServletResponse response)throws ServletException, IOException {
		
		response.reset();
		response.setContentType("text/html");
		String js;
		if( callback.equals("") ){
			js = "<script>alert('file not found!');</script>";
		}else{
		//	callback = java.net.URLDecoder.decode(callback, "UTF-8");
			callback = java.net.URLEncoder.encode(callback, "UTF-8");
			js = "<script>alert('" + callback + "');</script>";
		}
		//response.getWriter().write(js);
		PrintWriter out;
		out = response.getWriter();
		//out.print( java.net.URLEncoder.encode(js, "UTF-8") );
		//js = java.net.URLDecoder.decode(js, "UTF-8");
		out.print(js);
		out.flush();
		out.close();
	}
	
	private String CheckType(String filename)

	{
		String ContentType;
		switch (filename.substring(filename.lastIndexOf(".")).trim().toLowerCase()) {
		case ".asf ":
			ContentType = "video/x-ms-asf";
			break;
		case ".avi ":
			ContentType = "video/avi";
			break;
		case ".doc ":
			ContentType = "application/msword";
			break;
		case ".zip ":
			ContentType = "application/zip";
			break;
		case ".xls ":
			ContentType = "application/vnd.ms-excel";
			break;
		case ".gif ":
			ContentType = "image/gif";
			break;
		case ".jpg ":
			ContentType = "image/jpeg";
			break;
		case "jpeg ":
			ContentType = "image/jpeg";
			break;
		case ".wav ":
			ContentType = "audio/wav";
			break;
		case ".mp3 ":
			ContentType = "audio/mpeg3";
			break;
		case ".mpg ":
			ContentType = "video/mpeg";
			break;
		case ".mepg ":
			ContentType = "video/mpeg";
			break;
		case ".rtf ":
			ContentType = "application/rtf";
			break;
		case ".html ":
			ContentType = "text/html";
			break;
		case ".htm ":
			ContentType = "text/html";
			break;
		case ".txt ":
			ContentType = "text/plain";
			break;
		case ".torrent":
			ContentType = "application/x-bittorrent";
			break;
		default:
			ContentType = "application/octet-stream";
			break;
		}
		return ContentType;
	}

}
