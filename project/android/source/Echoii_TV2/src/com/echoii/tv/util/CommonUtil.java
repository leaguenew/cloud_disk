package com.echoii.tv.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.model.DownloadObj;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>公共工具类</p>
 *
 */
public class CommonUtil {
	public static final String TAG = "CommonUtil";

	public static String getServerIpAddress(Context context) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wi = wm.getConnectionInfo();
		int ip = wi.getIpAddress();
		LogUtil.d(TAG, "wifi info ip = " + ip);
		// (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
		// + (i >> 24 & 0xFF);
		String ipAddress = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
		return ipAddress;
	}

	/**
	 * ɨ��sdcard�Ķ�ý���ļ��������mediastore Ҳ����ʹ�ã�sendBroadcast(new
	 * Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
	 * Environment.getExternalStorageDirectory())));
	 */
	public static void scanSdCard(Context context) {
		IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	public static boolean isSDcardMounted() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) { 
        	return false;
        } else {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}    
        }   
        return false;   
    }
	
	/**
	 * ȡ�õ�ǰ�豸MAC��ַ
	 * 
	 */
	public static String getLocalMacAddress(Context context) {

		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress().replace(":", "-");
		return mac;

	}
	
	public static boolean isUsbMounted() {
		
		File Usbfile = new File("/proc/partitions");
		if (Usbfile.exists())
		{
			try
			{
				FileReader file = new FileReader("/proc/partitions");
				BufferedReader br = new BufferedReader(file);
				String strLine = "";
				while ((strLine = br.readLine()) != null)
				{
					if (strLine.indexOf("sd") > 0)
					{
						//�Ѿ����أ����ų��ж��
						return true;
					}
				}
				br.close();
				file.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static void handleMessage(Handler handler, int what){
		if (handler == null) {
			LogUtil.d(TAG, "handler is null");
			return;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	public static void handleMessage(Handler handler, DownloadObj obj, int what){
		if (handler == null) {
			LogUtil.d(TAG, "handler is null");
			return;
		}
		Message msg = handler.obtainMessage();
		msg.obj = obj;
		msg.what = what;
		handler.sendMessageDelayed(msg, 200);
	}
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}

	public static void writeFile(String json) throws FileNotFoundException, IOException{
		
		String filePath = Constants.DEVICE_INFO_PATH + File.separator + ".echoii.dev";
		
		File file = new File (filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		FileOutputStream output = null;
		BufferedWriter writer = null;
		try {
			
			output = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(output));
			writer.write(json);
			writer.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (output != null) {
				output.close();
			}
		}
		
	}
	
	public static String readFile() throws FileNotFoundException, IOException{
		
		String json = null;
		String filePath = Constants.DEVICE_INFO_PATH + File.separator + ".echoii.dev";
		
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		
		FileInputStream input = null;
		BufferedReader reader = null;
		try {
			
			input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input));
			json = reader.readLine();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (input != null) {
				input.close();
			}
		}
		
		return json;
	}
	
	public static String setSyncPath(Context context, String path) {
		return path + File.separator + "echoii"  + File.separator + CommonUtil.getLocalMacAddress(context) + File.separator + "data" ;
	}
}


