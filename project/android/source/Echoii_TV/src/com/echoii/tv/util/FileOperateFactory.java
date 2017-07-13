package com.echoii.tv.util;

import java.io.File;
import java.util.List;

import android.util.Log;

public class FileOperateFactory {
	public static final String TAG = "FileOperateFactory";
	
	public static String copyFiles(List<String> fromPath, String toPath) {
		Log.d(TAG, "common util copy files -- from path = " + fromPath + "// to path = " + toPath);
		String result = null;
		for (int i = 0; i < fromPath.size(); i++) {
			String from = fromPath.get(i);
			File f = new File(from);
			if (f.isDirectory()) {
				result = FileOperateImpl.copyFolder(from, toPath);
				if (result == FileOperateImpl.ERROR) {
					return result;
				}
			} else {
				result = FileOperateImpl.copyFile(from, toPath);
				if (result == FileOperateImpl.ERROR) {
					return result;
				}
			}
		}
		return result;
	}

	public static String deleteFiles(List<String> fromPath) {
		String result = null;
		for (int i = 0; i < fromPath.size(); i++) {
			result = FileOperateImpl.deleteFile(fromPath.get(i));
		}
		return result;
	}

	public static String renameFile(String fromPath, String newFileName) {
		String result = null;
		result = FileOperateImpl.renameFile(fromPath,newFileName);
		return result;
	}
}
