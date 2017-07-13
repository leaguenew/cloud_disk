package com.echoii.tv.constants;

import java.io.File;

import android.os.Environment;

public class Constants {
	
	public static final String SYNC_BASE_PATH = "/mnt";
	public static final String CLOUD_FILE_PATH = "root";
	public static final String SYNC_SDCARD_BASE_PATH = Environment.getExternalStorageDirectory().getPath();
	public static String SYNC_DEFAULT_PATH ;
	public static final String SYNC_USB_BASE_PATH = "/mnt/usb_storage";
	public static final String SYNC_USERNAME = "test@test.com";
	public static final String SYNC_PASSWORD = "1234";
	public static final String SYNC_IP_ADDRESS = "server_ip_address";
	
	public static final int START_JLAN_SERVER = 0x001;
	public static final int STOP_JLAN_SERVER = 0x002;
	
	public static final int SYNC_FILE = 0x003;
	public static final int SYNC_ALL_FILES = 0x004;
	
	public static final int MSG_STORAGE_UNMOUNTED = 0x011;
	public static final int MSG_CLOUD_LIST_NULL = 0x012;
	public static final int MSG_DOWNLOAD_LIST_NULL = 0x013;
	public static final int MSG_CONNECTION_TIMEOUT = 0x014;
	public static final int MSG_NETWORK_ERROR = 0x015;
	public static final int MSG_DOWNLOAD_FILE_NULL = 0x016;
	public static final int MSG_FILE_ERROR = 0x017;
	public static final int MSG_UPDATE_PROGRESS = 0x018;
	
	public static final String SUCCESS = "success";
	public static final String AUTH_ERROR = "auth_error";
	
	public static final String PREFERENCES = "com.echoii.tv.sharedpreferences";
	public static final String LOGIN_STATUS = "login_status";
	public static final String DEVICE_ID = "device_id";
	public static final String DEVICE_PASSWORD = "device_password";
	public static final String PHONE_PASSWORD = "phone_login_password";
	
	public static final String DEVICE_INFO_PATH = SYNC_SDCARD_BASE_PATH + File.separator + "Android" + File.separator + "data";
	
	public static final String KEY_CLOUD_CATEGORY_POSITION = "cloud_category_position";

}
