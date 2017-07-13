package com.echoii.constant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BaseUrl
{
//	public static final String SERVER_BASE_URL = "192.168.0.103:8080";
	public static final String SERVER_BASE_URL = "210.75.252.180:7080";
    public static final String BASE_URL = "http://"+ SERVER_BASE_URL +"/echoii/service/0";  //公网地址
//	public static final String BASE_URL = "http://172.21.7.199:81/echoii/service/0";
    
    public static final String LOGIN_URL = BASE_URL + "/mobile/auth/login";
    
    public static final String REGISTER_URL = BASE_URL + "/mobile/auth/reg";
    
    /**云端 获取AllFile目录下的所有文件URL*/
    public static final String ALLFILE_URL = BASE_URL + "/file/list";
    /**获取 image 目录下的文件url     */
    public static final String IMAGE_URL = BASE_URL + "/file/list_image";
    /**获取 music 目录下的文件url     */
    public static final String MUSIC_URL = BASE_URL + "/file/list_music";
    /**获取 video 目录下的文件url     */
    public static final String VIDEO_URL = BASE_URL + "/file/list_video";
    /**获取 bt 目录下的文件url     */
    public static final String BT_URL = BASE_URL + "/file/list_torrent";
    /**获取 doc 目录下的文件url     */
    public static final String DOC_URL = BASE_URL + "/file/list_doc";
    /**获取 other 目录下的文件url     */
    public static final String OTHER_URL = BASE_URL + "/file/list_other";
    /**获取 group 目录下的文件url     */
    public static final String 	GROUP_URL = BASE_URL + "/file/share_group";
    /**获取 share 目录下的文件url     */
    public static final String SHARE_URL = BASE_URL + "/share/share_files?user_id=";
//    http://172.21.7.199:81/echoii/media/image/get_image2?id=201311/34cefbcf242b89d6f0ff35a576d70eba&type=img&size=raw   
//    http://210.75.252.180:7080/echoii/media/image/get_image2?id=201311/34cefbcf242b89d6f0ff35a576d70eba&type=img&size=raw
//    public static final String IMAGE_BASE_URL = "http://172.21.7.199:81/echoii/media/image/get_image2?";
    public static final String IMAGE_BASE_URL = "http://"+SERVER_BASE_URL+"/echoii/media/image/get_image2?";
    /**重命名url*/
    public static final String RENAME_URL = BASE_URL + "/file/rename?";
    /**删除url*/
    public static final String DEL_URL = BASE_URL + "/file/del?";
    /**复制url*/
    public static final String COPY_URL = BASE_URL + "/file/copy?";
    
    public static final String LIST_ALLFILE = "allfile";
    public static final String LIST_IMAGE = "image";
    public static final String LIST_MUSIC = "music";
    public static final String LIST_VIDEO = "video";
    public static final String LIST_BT = "bt";
    public static final String LIST_DOC = "doc";
    public static final String LIST_OTHER = "other";
    public static final String LIST_GROUP = "group";
    public static final String LIST_SHARE = "share";
    
    public static byte[] readStream(InputStream input)
    {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;
        try
        {
            while ((len = input.read(buffer)) != -1)
            {
                bos.write(buffer, 0, len);
            }
            input.close();
        }
        catch (IOException e)
        {
            return "".getBytes();
        }
        return bos.toByteArray();
    }
}
