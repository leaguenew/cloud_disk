package com.echoii.tv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import android.util.Log;

public class FileOperateImpl {
	public static final String TAG = "FileOperate";
	public static final String SUCCESS = "200";
	public static final String ERROR = "503";

	public FileOperateImpl() {
	}

	public static String copyFolder(String from, String to) {
		String result = null;
		Log.d(TAG, "copyFolder....from = " + from + "; to = " + to);
		File root = new File(from);
		if (!root.exists()) {
			return ERROR;
		}
		File[] currentFiles = root.listFiles();
//		if (currentFiles == null) {
//			File f = new File(to + File.separator + root.getName());
//			if (!f.exists()) {
//				boolean flag = f.mkdirs();
//				if (flag) {
//					result = SUCCESS;
//				}else{
//					result = ERROR;
//				}
//			}
//		}else{
			String toPath = to + File.separator + root.getName();
			File targetDir = new File(toPath);
			if (!targetDir.exists()) {
				boolean flag = targetDir.mkdirs();
				if (flag) {
					result = SUCCESS;
				}else{
					result = ERROR;
				}
				Log.d(TAG, "to path  is --" + toPath + ";flag =" + flag);
			}
			for (int i = 0; i < currentFiles.length; i++) {
				if (currentFiles[i].isDirectory()) {
					copyFolder(currentFiles[i].getPath() , toPath );
				} else {
					result = copyFile(currentFiles[i].getPath(),toPath);
				}
			}
//		}
		
		return result;
	}

	public static String copyFile(String from, String to) {
		String result = null;
		File fromFile = new File(from);
		String fileName = fromFile.getName();
		String toPath = to +File.separator + fileName ; 
		Log.d(TAG, "CopyFile....from = " + from + "; to path = " + toPath);
		File toFile = new File(toPath);
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile); 
			byte buffer[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(buffer)) > 0) {
				fosto.write(buffer, 0, c);
			}
			fosfrom.close();
			fosto.close();
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		} 
		return result;
	}


	public static String deleteFile(String path) {
		Log.d(TAG, "delete file -- path = " + path);
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return ERROR;
		}
		if (file.isFile()) {
			flag = file.delete();
			return flag ? SUCCESS : ERROR;
		}else if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				flag = file.delete();
				return flag ? SUCCESS : ERROR;
			}
			for (File f : childFiles) {
				deleteFile(f.getAbsolutePath());
			}
			flag = file.delete();
			return flag ? SUCCESS : ERROR;
		}
		return ERROR;
	}

	public static String renameFile(String fromPath, String newFileName) {
		File file = new File(fromPath);
		String parentPath = file.getParentFile().getPath();
		File newFile = new File(parentPath + File.separator + newFileName); 
		boolean flag = file.renameTo(newFile);
		return flag ? SUCCESS : ERROR;
	}
}
