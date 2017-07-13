package com.echoii.tv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.util.Log;

import com.echoii.tv.model.EchoiiFile;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>网络操作工具类，各种网络操作的具体实现封装</p>
 *
 */
public class HttpUtil {
	
	public static final String TAG = "HttpUtil";
	
	private static String doGet(String url) throws SocketTimeoutException,
			IOException {
		Log.d(TAG, "doGet url = " + url);
		HttpURLConnection conn = null;
		BufferedReader reader = null;

		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			LogUtil.d(TAG, "getCloudList : responseCode = " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				return reader.readLine();
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public static String cloudLogin(String url) 
			throws MalformedURLException, SocketTimeoutException, IOException {
		return doGet(url);
	}
	
	public static String datacenterLogin(String url) 
			throws MalformedURLException, SocketTimeoutException, IOException {
		return doGet(url);
	}
	
	public static String datacenterRegister(String url) 
			throws MalformedURLException, SocketTimeoutException, IOException {
		return doGet(url);
	}
	
	public static void uploadFile(EchoiiFile file, String url) {
		
	}
	
	public static String getAllCloudList(String url)
			throws MalformedURLException, SocketTimeoutException, IOException {
		return doGet(url);
	}
	
	public static String getCloudList(String url)
			throws MalformedURLException, SocketTimeoutException, IOException {
		return doGet(url);
	}

}
