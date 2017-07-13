package com.echoii.constant;


public class MessageInfo
{
    public static final int RETURN_CODE_SUCCESS = 200;
    /**参数错误*/
    public static final int RETURN_CODE_PARAMS_WRONG = 400;
    /**认证错误，包括用户名或密码错误*/
    public static final int RETURN_CODE_TOKEN_WRONG = 401;
    /**系统错误*/
    public static final int RETURN_CODE_SYSTEM_WRONG = 500;
    /**重名*/
    public static final int RETURN_CODE_USERNAME_SAME = 409;
    /**解析错误*/
    public static final int PARSE_EXCEPTION = -1;
    
    public static final int MESSAGE_SUCCESS = 1;
    public static final int MESSAGE_FAIL = 2;
    /**获取数据成功，但用户此时没有数据*/
    public static final int MESSAGE_SUCCESS_NULL = 3;
    
    public static final int MESSAGE_SUCCESS_END = 4;
    
    /**文件操作标识*/
	public static final int FILE_OPERATE = 5;
	
	/**暂时还未实现*/
	public static final int MESSAGE_FAIL_UNDO = 6;
    /**连接错误*/
	public static final int RETURN_RESPONSE_FAIL = 54;
    /**登陆响应标志*/
    public static final int LOGIN_RESPONSE = 55;
    /**注册响应标志*/
    public static final int REGISTER_RESPONSE = 56;
    
    /**全部文件*/
    public static final int ALLFILE = 57;
    /**打开文件成功*/
    public static final int DOWN_OPEN_FILE = 58;
    /**打开文件出现错误*/
    public static final int DOWN_OPEN_FILE_ERROE = 59;
    
    public static final int ALLFILE_TOKEN_WRONG = 60;
    
    public static final int PHONE_LIST = 63;
    public static final int PHONE_LIST_SUCCESS_NULL = 64;
    public static final int PHONE_LIST_SUCCESS = 65;
    
    /**重命名响应标识*/
    public static final int RENAME_RESPONSE = 66;
    /**删除响应标识*/
    public static final int DELETE_RESPONSE = 67;
    /**复制响应标识*/
    public static final int COPY_RESPONSE = 68;
    
    /**云端获取图片缩略图标识*/
    public static final int IMAGE_THUMBNAILS = 69;
    
    /**手机端获取图片缩略图标识*/
    public static final int PHONE_IMAGE_THUMBNAILS = 70;
    
    /**手机端获取视频缩略图标识*/
    public static final int PHONE_VIDEO_THUMBNAILS = 71;
    
    /**数据市场-- 我的拍卖按钮操作标识*/
    public static final int MARKET_SALE = 72;
    
    public static final int MARKET_PURCHASE = 73;
}
