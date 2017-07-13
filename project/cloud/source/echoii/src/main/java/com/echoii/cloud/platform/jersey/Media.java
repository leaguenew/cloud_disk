package com.echoii.cloud.platform.jersey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.DateUtil;

	@Path("media")
	public class Media extends JerseyBase {
		private static Logger log = Logger.getLogger(Download.class);
		private FileService service = FileServiceImpl.getInstance();
		
		@GET
		@Path("/{param}")
		@Produces(MediaType.TEXT_PLAIN)
		public void downloadFile(
				@PathParam("param") String name,
				@QueryParam("user_id") @DefaultValue("no-userid") String userId,
				@QueryParam("token") @DefaultValue("no-token") String token,		
				@QueryParam("file_id") @DefaultValue("no-fileid") String fileId,
				@Context HttpServletRequest request,
				@Context HttpServletResponse response) throws InterruptedException {

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			log.debug("[Auth_log_out]:validation failure.");
			return;
		}

		log.debug("[download]:validation begin!");
		com.echoii.cloud.platform.model.File fl = service.getFileById(fileId);

		if (fl == null) {
			response.setStatus(404);
			return;
		}

		try {
			// read the file
			File file = new File(config.getStringValue("upload.directory",
					"/mnt/moosefs/echoii/data")
					+ "/"
					+ fl.getMetaFolder()
					+ "/" + fl.getMetaId());
			log.debug(config.getStringValue("upload.directory",
					"/mnt/moosefs/echoii/data")
					+ "/"
					+ fl.getMetaFolder()
					+ "/" + fl.getMetaId());

			if (!file.exists()) {
				response.setStatus(404);
				return;
			}

			FileInputStream fis = new FileInputStream(file);
			response.reset();
			response.setContentType(CheckType(name));
			response.setContentLength((int) file.length());
			if (request.getHeader("User-Agent").contains("MSIE")) {
				response.setHeader(
						"Content-Disposition",
						"attachment;filename ="
								+ java.net.URLEncoder.encode(name, "UTF-8"));

			} else {
				String enableFileName = "=?UTF-8?B?"
						+ (new String(Base64.encodeBase64(name
								.getBytes("UTF-8")))) + "?=";
				response.setHeader("Content-Disposition",
						"attachment;filename =" + enableFileName);
			}

			response.flushBuffer();
			OutputStream sos = response.getOutputStream();
			// int count = 0;
			long k = 0;
			byte[] bytes = new byte[1024];
			System.out.println(file.length());
			int count = 0;

			long pre = DateUtil.getDateLong();
			while (k < file.length()) {

				++count;
				int j = fis.read(bytes, 0, 1024);
				k = k + j;
				sos.write(bytes, 0, j);

				if (count % (1024) == 0) {
					long after = DateUtil.getDateLong();
					long time = after - pre;
					if (time < 1000) {
						Thread.sleep((1200 - time));
					}
					pre = after;
				}

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
			ContentType = "audio/wav ";
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
		default:
			ContentType = "application/octet-stream";
			break;
		}
		return ContentType;
	}
}
