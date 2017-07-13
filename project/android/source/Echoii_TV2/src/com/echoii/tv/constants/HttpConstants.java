package com.echoii.tv.constants;

public class HttpConstants {
	
	public static String USER_ID = "61f2fe361879166fb14e4b76ab670667";
	public static String PASSWORD = "c861232dedf0e159cafd5c921935921f";
	
	public static String SERVER_IP = "172.21.7.199:81"; //  172.21.7.199:81 //  210.75.252.180:7080
	public static final String SERVER_URL = "http://"+ SERVER_IP +"/echoii/service/0";
	
	public static final String CLOUD_WEB_PAGE = "http://"+ SERVER_IP +"/echoii/homepage.html";
	
	public static final String DEVICE_LIST_ALL_BASE_URL = SERVER_URL + "/device/sync/list_all_file?";
//	public static final String FILE_LIST_BASE_URL = SERVER_URL + "/file/list_totally?user_id=" + USER_ID + "&token=" + TOKEN;
	
	public static final String DEVICE_LIST_FILE_BASE_URL = SERVER_URL + "/device/sync/list_file?";
	
	/**云端 获取AllFile目录下的所有文件URL*/
    public static final String ALLFILE_URL = SERVER_URL + "/file/list?";
    /**获取 image 目录下的文件url     */
    public static final String IMAGE_URL = SERVER_URL + "/file/list_image?";
    /**获取 music 目录下的文件url     */
    public static final String MUSIC_URL = SERVER_URL + "/file/list_music?";
    /**获取 video 目录下的文件url     */
    public static final String VIDEO_URL = SERVER_URL + "/file/list_video?";
    /**获取 bt 目录下的文件url     */
    public static final String BT_URL = SERVER_URL + "/file/list_torrent?";
    /**获取 doc 目录下的文件url     */
    public static final String DOC_URL = SERVER_URL + "/file/list_doc?";
    /**获取 other 目录下的文件url     */
    public static final String OTHER_URL = SERVER_URL + "/file/list_other?";
    /**获取 group 目录下的文件url     */
    public static final String GROUP_URL = SERVER_URL + "/file/share_group?";
    /**获取 share 目录下的文件url     */
    public static final String SHARE_URL = SERVER_URL + "/share/share_files?user_id=";
    
    public static final String CLOUD_LOGIN_URL = SERVER_URL + "/mobile/auth/login?";
	
//	public static final String DOWNLOAD_BASE_URL = SERVER_URL + "/download/mobile?";
	
	public static final String DOWNLOAD_BASE_URL = SERVER_URL + "/download/hdc?";
	
	public static final String DELETE_BASE_URL = SERVER_URL + "/file/del?";
	
	public static final String DEVICE_REG_BASE_URL = SERVER_URL + "/device/reg?";
	
	public static final String DEVICE_LOGIN_BASE_URL = SERVER_URL + "/device/login?";

}
