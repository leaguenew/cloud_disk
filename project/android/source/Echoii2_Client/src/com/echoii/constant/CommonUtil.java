package com.echoii.constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;

import com.echoii.uploadfile.UploadFile;

public class CommonUtil
{
	public static final String TAG = "CommonUtil";
	public static final String NEED_TOKENID = "user_token";
	public static final String USER_ID = "user_id";
	public static final String USER_TOKEN = "token";
	public static final String SDCARD_ROOT_PATH = "/mnt";
	public static final int COMPRESS_FORMAT = 50;
	
	//拍照上传
	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "echoii" + File.separator + "phone" + File.separator ;
//	public static String UPLOAD_PHOTO_URL = "http://172.21.7.199:81/echoii/upload";
	public static String UPLOAD_PHOTO_URL = "http://"+BaseUrl.SERVER_BASE_URL+"/echoii/upload";

	/**
	 * 
	 * 格式化大小
	 * <p>
	 * Description: 这里用一句话描述这个方法的作用
	 * <p>
	 * 
	 * @date 2013-10-25
	 * @param size
	 * @return
	 */
	public static String formatSize(long size) {
		DecimalFormat fecimalFormat = new DecimalFormat("#.00");
		String strText = "";
		if (size < 1024)// Bytes
		{
			strText = fecimalFormat.format((double) size) + "B";
		} else if (size < 1024 * 1024)// KB
		{
			strText = fecimalFormat.format((double) size / 1024) + "KB";
		} else if (size < 1024 * 1024 * 1024)// M
		{
			strText = fecimalFormat.format((double) size / (1024 * 1024))
					+ "MB";
		} else if (size < 1024 * 1024 * 1024 * 1024)// GB
		{
			strText = fecimalFormat
					.format((double) size / (1024 * 1024 * 1024)) + "GB";
		} else
		// TB
		{
			strText = fecimalFormat.format((double) size
					/ (1024 * 1024 * 1024 * 1024))
					+ "TB";
		}
		return strText;
	}

	public static String formatSize(float size) {
		long kb = 1024;
		long mb = (kb * 1024);
		long gb = (mb * 1024);
		if (size < kb) {
			return String.format("%d B", (int) size);
		} else if (size < mb) {
			return String.format("%.0f KB", size / kb); // 保留两位小数
		} else if (size < gb) {
			return String.format("%.2f MB", size / mb);
		} else {
			return String.format("%.2f GB", size / gb);
		}
	}

	public static String formatFileSize(long strFileSize){
		if (strFileSize == -1) {
			return "";
		}
		DecimalFormat fecimalFormat = new DecimalFormat("#.00");
		String showSize = "";

		if (strFileSize >= 0 && strFileSize < 1024)

		{
			showSize = strFileSize + "B";
		} else if (strFileSize >= 1024 && strFileSize < (1024 * 1024))

		{
			showSize = fecimalFormat.format((double)strFileSize / 1024) + "KB";
		} else if (strFileSize >= (1024 * 1024) && strFileSize < (1024 * 1024 * 1024))

		{
			showSize = fecimalFormat.format((double)strFileSize / (1024 * 1024)) + "MB";
		} else if (strFileSize >= (1024 * 1024 * 1024))

		{
			showSize = fecimalFormat.format((double)strFileSize / (1024 * 1024 * 1024)) + "GB";
		}
		return showSize;
	}

	/**
	 * 
	 * transformTime
	 * <p>
	 * Description: 将毫秒数的时间转换为字符串格式
	 * <p>
	 * 
	 * @date 2013-10-25
	 * @param milliseconds
	 *            与1970年1月1日0点的时间差(单位: 毫秒)
	 * @return 时间的字符串格式(e.g: 2012年12月17日 10:14:32.101)
	 */
	public static String transformTime(long milliseconds) {
		long currentMilliseconds = milliseconds % 1000;// 当前毫秒数

		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date(milliseconds));

		String strYear = String.valueOf(cal.get(Calendar.YEAR));
		String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String strDate = String.valueOf(cal.get(Calendar.DATE));
		String strHour = String.valueOf(cal.get(Calendar.HOUR));
		String strMinute = String.valueOf(cal.get(Calendar.MINUTE));
		String strSecond = String.valueOf(cal.get(Calendar.SECOND));

		if (cal.get(Calendar.MONTH) < 9) {
			strMonth = "0" + strMonth;
		}

        StringBuffer strBuff = new StringBuffer();
        strBuff.append(strYear).append("年");
        strBuff.append(strMonth).append("月");
        strBuff.append(strDate).append("日 ");
        strBuff.append(strHour).append(":");
        strBuff.append(strMinute).append(":");
        strBuff.append(strSecond).append(".");
        strBuff.append(currentMilliseconds);
        return strBuff.toString();
    }
	
	public static String formatTime(long milliseconds) {
		if (milliseconds == -1) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatTime = sdf.format(new Date(milliseconds));
		return formatTime;
	}
    public static void largeClickRange(View view)
    {
    	 Rect bounds = new Rect();
//         mBackBtn.setEnabled(true);

         view.getHitRect(bounds);
         bounds.right += 50;
         bounds.bottom += 50;
         bounds.left -= 50;
         bounds.top -= 50;

         TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

         if (View.class.isInstance(view.getParent()))
         {
             ((View) view.getParent()).setTouchDelegate(touchDelegate);
         }
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
		}
		else if (exp.equals("torrent"))
		{
			return "torrent";
		}
		else {
			return "others";
		}
	}
		
		public static String getOpenFileType(String type)
		{
			if (type.equals("mp3") || type.equals("wav") || type.equals("wma")
				|| type.equals("ogg") || type.equals("acc") || type.equals("mid"))
			{
				return BaseUrl.LIST_MUSIC;
			}
			else if (type.equals("3gp") || type.equals("rmvb") 
					|| type.equals("mp4") || type.equals("mpg4") || type.equals("wmv")
					|| type.equals("flv"))
			{
				return BaseUrl.LIST_VIDEO;						
			}
			else if (type.equals("png") || type.equals("jpg")
					|| type.equals("jpeg") || type.equals("bmp") || type.equals("gif"))
			{
				return BaseUrl.LIST_IMAGE;
			}
			else if (type.equals("doc") || type.equals("ppt") || type.equals("xls") 
					|| type.equals("xlsx") || type.equals("docx") || type.equals("txt"))
			{
				return BaseUrl.LIST_DOC;
			}
			else 
			{
				return BaseUrl.LIST_OTHER;
			}
					
		}
    
    /**
     * getBitmapByUrlEx
     * @Description: 获取网络图片Bitmap
     * @param strUrl
     * @return
     */
    public static Bitmap getBitmapByUrlEx(String strUrl)
    {
        if ((null == strUrl) || ("".equals(strUrl)))
        {
            throw new NullPointerException("input param strUrl cann't be null");
        }
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try
        {
            HttpGet httpRequest = new HttpGet(strUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            httpRequest.setParams(httpParams);
            HttpResponse response = (HttpResponse) httpClient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream instream = bufHttpEntity.getContent();
            BufferedInputStream bi = new BufferedInputStream(instream);
            inputStream = bi;
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /**
     * 打开文件
     * @param filePath
     * @return
     */
    public static Intent openFile(String filePath){  
    	  
        File file = new File(filePath);  
        if(!file.exists()){
        	Log.d(TAG, "open file --> file is not exists " );
        	return null;  
        }
        /* 取得扩展名 */  
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1,
        		file.getName().length()).toLowerCase();
        Log.d(TAG, "file expand = " + end);
        /* 依扩展名的类型决定MimeType */  
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
                end.equals("xmf")||end.equals("ogg")||end.equals("wav") || end.equals("acc"))
        {  
            return getAudioFileIntent(filePath);  
        }
        else if(end.equals("3gp")||end.equals("mp4")||end.equals("rmvb")||end.equals("mpg4")||end.equals("wmv")||end.equals("flv"))
        {  
            return getVideoFileIntent(filePath);  
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
                end.equals("jpeg")||end.equals("bmp"))
        {  
            return getImageFileIntent(filePath);  
        }
        else if(end.equals("apk"))
        {  
            return getApkFileIntent(filePath);  
        }
        else if(end.equals("ppt"))
        {  
            return getPptFileIntent(filePath);  
        }else if(end.equals("xls")){  
            return getExcelFileIntent(filePath);  
        }
        else if(end.equals("doc"))
        {  
            return getWordFileIntent(filePath);  
        }
        else if(end.equals("pdf"))
        {  
            return getPdfFileIntent(filePath);  
        }
        else if(end.equals("chm"))
        {  
            return getChmFileIntent(filePath);  
        }
        else if(end.equals("txt"))
        {  
            return getTextFileIntent(filePath,false);  
        }
        else
        {  
            return getAllIntent(filePath);  
        }  
    }  
    
    /**
     * 获取一个用于打开VIDEO文件的intent  
     * @param param
     * @return
     */
    public static Intent getVideoFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "video/*");  
        return intent;  
    }  
    
    /**
     * 获取一个用于打开所有文件的intent  
     * */
    public static Intent getAllIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"*/*");   
        return intent;  
    }  
    /**
     * 获取一个用于打开APK文件的intent  
     * @param param
     * @return
     */
    public static Intent getApkFileIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"application/vnd.android.package-archive");   
        return intent;  
    } 
    /**
     * 获取一个用于打开AUDIO文件的intent  
     * @param param
     * @return
     */
    public static Intent getAudioFileIntent( String param ){  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "audio/*");  
        return intent;  
    }  
  
    /**
     * 获取一个用于打开Html文件的intent     
     * @param param
     * @return
     */
    public static Intent getHtmlFileIntent( String param ){  
  
        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.setDataAndType(uri, "text/html");  
        return intent;  
    }  
  
    /**获取一个用于打开图片文件的intent  
     * 
     * @param param
     * @return
     */
    public static Intent getImageFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(new File(param));  
        intent.setDataAndType(uri, "image/*");  
        return intent;  
    }  
  
    /**
     * 获取一个用于打开PPT文件的intent     
     * @param param
     * @return
     */
    public static Intent getPptFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");     
        return intent;     
    }     
  
    /**
     * 获取一个用于打开Excel文件的intent     
     * @param param
     * @return
     */
    public static Intent getExcelFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-excel");     
        return intent;     
    }     
  
    /**
     * 获取一个用于打开Word文件的intent     
     * @param param
     * @return
     */
    public static Intent getWordFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/msword");     
        return intent;     
    }     
  
    /**
     * 获取一个用于打开CHM文件的intent     
     * @param param
     * @return
     */
    public static Intent getChmFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/x-chm");     
        return intent;     
    }     
  
   /**
    * 获取一个用于打开文本文件的intent     
    * @param param
    * @param paramBoolean
    * @return
    */
    public static Intent getTextFileIntent( String param, boolean paramBoolean){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        if (paramBoolean){     
            Uri uri1 = Uri.parse(param );     
            intent.setDataAndType(uri1, "text/plain");     
        }else{     
            Uri uri2 = Uri.fromFile(new File(param ));     
            intent.setDataAndType(uri2, "text/plain");     
        }     
        return intent;     
    }    
    /**
     * Android获取一个用于打开PDF文件的intent     
     * @param param
     * @return
     */
    public static Intent getPdfFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/pdf");     
        return intent;     
    }  


	public static String getFilePathByType(UploadFile downloadFile) {
		if (downloadFile != null) {
			StringBuffer path = new StringBuffer(Environment.getExternalStorageDirectory().getPath() + File.separator + "echoii" + File.separator + "data_center");
			String type = getFileType(downloadFile.getFileName());
			path.append(File.separator).append(type).append(File.separator).append(downloadFile.getFileName());
			return path.toString();
		}else{
			return null;
		}
	}
    
	/**
	 * 判断是否是手机号码
	 * @param str
	 * @return
	 */
	 public static boolean isPhoneNumber(String str)
     {
        String regex = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
     }
	 
	 public static String getClientIpAddress(Activity activity){
			WifiManager wm = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wi = wm.getConnectionInfo();
			int ip = wi.getIpAddress();
			Log.d(TAG, "wifi info ip = " + ip);
			//(i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
			String ipAddress = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
			return ipAddress;
		}
	 

}
