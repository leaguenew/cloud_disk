package com.echoii.tv.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.model.DownloadObj;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>文件工具类</p>
 *
 */
public class FileUtil {
	
	public static final String TAG = "FileUtil";
	
	private static boolean isDownloadFinish = false;
	
	public static boolean isDownloadFinish() {
		return isDownloadFinish;
	}

	public static long getDirSize(File file) { 
		
		long size = 0; 
        if (file.exists()) {     
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                
                if (children == null) {
                	return 0;
				}
                for (File childFile : children){
                    size += getDirSize(childFile);
                }
            } else {
                size = file.length();        
            }     
        }
        return size;     
        
    }     
	
	public static String getFileType(String name) {
		String exp = name.substring(name.lastIndexOf(".") + 1, name.length());
		exp = exp.toLowerCase();
		if (exp.equals("mp3") || exp.equals("ogg") || exp.equals("wav")
				|| exp.equals("wma")) {
			return "music";
		} else if (exp.equals("3gp") || exp.equals("rmvb") || exp.equals("mp4")
				|| exp.equals("mpg4") || exp.equals("wmv") || exp.equals("flv")) {
			return "video";
		} else if (exp.equals("png") || exp.equals("jpg") || exp.equals("jpeg")
				|| exp.equals("bmp") || exp.equals("gif")) {
			return "image";
		} else if (exp.equals("doc") || exp.equals("ppt") || exp.equals("xls")
				|| exp.equals("xlsx") || exp.equals("docx")
				|| exp.equals("txt")) {
			return "document";
		} else {
			return "others";
		}
	}
	
	public static void writeFile(Handler handler, InputStream inputStream, EchoiiFile echoiiFile, DownloadObj obj) throws FileNotFoundException, IOException{

		int sum = 0;
		long totalLength = Long.parseLong(echoiiFile.getSize());
		String path = echoiiFile.getPath();
		isDownloadFinish = false;
		FileOutputStream outputStream = null;
		
		File localFile = new File(path);
		
		try {
			
			if (!localFile.getParentFile().exists()) {
				localFile.getParentFile().mkdirs();
			}
			outputStream = new FileOutputStream(localFile);
			byte[] buffer = new byte[2048];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
				sum += len;
				
				obj.setName(path);
				obj.setProgress((sum * 1.0f / totalLength) * 100);
				CommonUtil.handleMessage(handler, obj, Constants.MSG_UPDATE_PROGRESS);
			}
			isDownloadFinish = true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		
	}

	public static List<EchoiiFile> getFileList(String path) {
		
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		
		if (path == null || "".equals(path)) {
			return null;
		}
		
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files == null) {
				return null;
			}
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.getName().startsWith(".")) {
					continue;
				}
				EchoiiFile echoiiFile = new EchoiiFile();
				echoiiFile.setCreateDate(f.lastModified() + "");
				echoiiFile.setForderId(f.getParentFile().getPath());
				echoiiFile.setId("");
				if (f.isDirectory()) {
					echoiiFile.setIdx(0);
				} else {
					echoiiFile.setIdx(1);
				}
				echoiiFile.setIsCurrentVersion("");
				echoiiFile.setLmfDate(f.lastModified() + "");
				echoiiFile.setMetaFolder("");
				echoiiFile.setMetaId("");
				echoiiFile.setName(f.getName());
				echoiiFile.setPath(f.getPath());
				echoiiFile.setSize(getDirSize(f) + "");
				echoiiFile.setStatus("");
				echoiiFile.setType("");
				echoiiFile.setUserId("");
				echoiiFile.setVersion("");
				list.add(echoiiFile);
			}
		}
		
		return list;
	}

	public static String getParentPath(String currentPath) {
		File file = new File(currentPath);
		if (!file.exists()) {
			return Constants.SYNC_BASE_PATH;
		}
		return file.getParentFile().getPath();
	}

}
