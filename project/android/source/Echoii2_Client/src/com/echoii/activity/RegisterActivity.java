package com.echoii.activity;

import java.io.FileNotFoundException;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.constant.MessageInfo;
import com.echoii.network.http.RegisterHttp;

public class RegisterActivity extends Activity{
	
	private TextView mTvewBack;
	private TextView mTvewNormal;
	private TextView mTvewFast;
	/**已经有账号*/
	private TextView mTvewAccounted;
	private Button mBtnRegister;
	private EditText mEditAccount;
	private EditText mEditPassWord;
	private TextView mLayoutExpanded;
	private LinearLayout mLlayoutRegisterNormalDetail;
	private LinearLayout mLlayoutMoreData;
	private TextView  mLlayoutRegisterFastDetail;
	private LinearLayout mLlayoutNormal;
	
	private ImageView mImgHead;
    private EditText mEditNick;
    private EditText mEditSex;
    private EditText mEditBrithday;
    private EditText mEditQQ;
    private EditText mEditPhone;
    private EditText mEditMail;
    private EditText mEditMicroBlog;
    private EditText mEditSignature;
	
	private String account;
	private String password;
	private String nick ;
	private String sex ;
	private String birthday ;
	private String qq;
	private String phone;
	private String mail;
	private String microblog;
	private String signature ;
	
	private boolean isFastRegister =  false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		initView();		
		initTwoRegisterBackground();		
		mTvewBack.setOnClickListener(registerOnClickListener);
		mBtnRegister.setOnClickListener(registerOnClickListener);
		mLayoutExpanded.setOnClickListener(registerOnClickListener);
		mTvewAccounted.setOnClickListener(registerOnClickListener);
		mTvewNormal.setOnClickListener(registerOnClickListener);
		mTvewFast.setOnClickListener(registerOnClickListener);
		mImgHead.setOnClickListener(registerOnClickListener);
	}
	
	private void initView()
	{
	    mTvewBack = (TextView)findViewById(R.id.back);
        mTvewNormal = (TextView)findViewById(R.id.tvew_register_normal);
        mTvewFast = (TextView)findViewById(R.id.tvew_register_fast);
        mTvewAccounted = (TextView)findViewById(R.id.tvew_accounted);
        mLayoutExpanded = (TextView)findViewById(R.id.llayout_expanded);
        mBtnRegister = (Button)findViewById(R.id.btn_register);
        mEditAccount = (EditText)findViewById(R.id.edit_account);
        mEditPassWord = (EditText)findViewById(R.id.edit_input_password);
        mLlayoutRegisterNormalDetail = (LinearLayout)findViewById(R.id.llayout_register_normal_detail);
        mLlayoutRegisterFastDetail = (TextView)findViewById(R.id.llayout_register_fast_detail);
        mLlayoutMoreData = (LinearLayout)findViewById(R.id.llayout_register_more);
        mLlayoutNormal = (LinearLayout)findViewById(R.id.llayout_normal);
        
        mImgHead = (ImageView)findViewById(R.id.img_register_head);
        mEditNick = (EditText)findViewById(R.id.edit_register_nick);
        mEditSex = (EditText)findViewById(R.id.edit_register_sex);
        mEditBrithday = (EditText)findViewById(R.id.edit_register_birthday);
        mEditQQ = (EditText)findViewById(R.id.edit_register_qq);
        mEditPhone = (EditText)findViewById(R.id.edit_register_phone);
        mEditMail = (EditText)findViewById(R.id.edit_register_mail);
        mEditMicroBlog = (EditText)findViewById(R.id.edit_register_microblog);
        mEditSignature = (EditText)findViewById(R.id.edit_register_signature);
	}
	
	private void initTwoRegisterBackground()
	{
	    mTvewNormal.setTextColor(getResources().getColor(R.color.fonts_operate_selected));
        mTvewFast.setTextColor(Color.BLACK);
        mBtnRegister.setBackgroundResource(R.drawable.register_button_background);
        mLlayoutRegisterNormalDetail.setVisibility(View.VISIBLE);
        mLlayoutRegisterFastDetail.setVisibility(View.GONE);
        mLlayoutMoreData.setVisibility(View.GONE);
        mTvewAccounted.setVisibility(View.VISIBLE);
        mLlayoutNormal.setVisibility(View.VISIBLE);
        isFastRegister = false;
       
	}
	
	private OnClickListener registerOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId())
			{
				case R.id.back:
				case R.id.tvew_accounted: //已有账号 
				{
					finish();
					break;
				}
				case R.id.llayout_expanded: //填充扩展内容时
				{
				    if (mLlayoutMoreData.isShown())
				    {
				        mLlayoutMoreData.setVisibility(View.GONE);
				    }
				    else
				    {
				        mLlayoutMoreData.setVisibility(View.VISIBLE);
				    }
				}
				case R.id.btn_register:
				{
					if (isFastRegister)
					{
						showToast("此功能当前还未开放，敬请期待。。");
						return;
					}
				    register();				
					break;
				}
				case R.id.tvew_register_normal: //正常注册时
				{
				    initTwoRegisterBackground();
				    break;
				}
				case R.id.tvew_register_fast: //快速注册时
				{
				    mTvewFast.setTextColor(getResources().getColor(R.color.fonts_operate_selected));
			        mTvewNormal.setTextColor(Color.BLACK);
			        mBtnRegister.setBackgroundResource(R.drawable.register_button_fast_background);
			        mLlayoutRegisterFastDetail.setVisibility(View.VISIBLE);
			        mLlayoutRegisterNormalDetail.setVisibility(View.GONE);	
			        mTvewAccounted.setVisibility(View.GONE);
			        mLlayoutNormal.setVisibility(View.GONE);
			        isFastRegister = true;
			        
				    break;
				}
				case R.id.img_register_head:
				{
					selectPicture();
					break;
				}
				default:
					break;
			}
		}
		
	};

	/**
	 * 选择头像
	 */
	private void selectPicture()
	{
		 Intent intent = new Intent();  
         /* 开启Pictures画面Type设定为image */  
         intent.setType("image/*");  
         /* 使用Intent.ACTION_GET_CONTENT这个Action */  
         intent.setAction(Intent.ACTION_GET_CONTENT);   
         /* 取得相片后返回本画面 */  
         startActivityForResult(intent, 1);  
	}
	
	/**
	 * 选择头像回来之后
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mLlayoutMoreData.setVisibility(View.VISIBLE);
		if (resultCode == RESULT_OK) 
		{  
            Uri uri = data.getData();  
            Log.e("uri", uri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try
            {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                /* 将Bitmap设定到ImageView */  
                mImgHead.setImageBitmap(bitmap);  
            }
            catch (FileNotFoundException e) 
            {  
                Log.d("Exception", e.getMessage(),e);  
            }  
        }  
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void register()
	{
	    account = mEditAccount.getText().toString().trim();
        password = mEditPassWord.getText().toString().trim();
        nick = mEditNick.getText().toString();
        sex = mEditSex.getText().toString();
        birthday = mEditBrithday.getText().toString();
        qq = mEditQQ.getText().toString();
        phone = mEditPhone.getText().toString();
        mail = mEditMail.getText().toString();
        microblog = mEditMicroBlog.getText().toString();
        signature = mEditSignature.getText().toString();
        
        if (TextUtils.isEmpty(account))
        {
            Toast.makeText(getApplicationContext(), 
                    getApplicationContext().getResources().getString(R.string.toast_name_null),
                    Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), 
                    getApplicationContext().getResources().getString(R.string.toast_password_null),
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            request = new HashMap<String,Object>();
            request.put("account", account);
            request.put("password", password);
            Log.d("mating","account = " + account + " password = " + password);
            RegisterHttp rhttp = new RegisterHttp(handler,request);
            rhttp.registerRequest();
        }
	}
	
	private  HashMap<String,Object>  request = null;
	
	private Handler handler = new Handler()
	{
	    public void handleMessage(android.os.Message msg) 
	    {
	        if (msg.what == MessageInfo.REGISTER_RESPONSE)
            {
                switch (msg.arg1)
                {
                    case MessageInfo.MESSAGE_SUCCESS:
                    {
                    	showToast(getResources().getString(R.string.register_result));
                        finish();
                        break;
                    }
    				case MessageInfo.RETURN_RESPONSE_FAIL:
    				{
    					showToast("连接错误，请检查wifi或数据连接是否正常。。");
    					break;
    				}
    				case MessageInfo.RETURN_CODE_PARAMS_WRONG:
    				{
    					showToast("参数错误！");
    					break;
    				}
    				case MessageInfo.RETURN_CODE_USERNAME_SAME:
    				{
    					showToast("用户名重名！");
    					break;
    				}
    				case MessageInfo.RETURN_CODE_SYSTEM_WRONG:
    				{
    					showToast("系统错误！");
    					break;
    				}
                    case MessageInfo.MESSAGE_FAIL:
                    {
                    	showToast("对不起，程序异常，未知错误，请谅解。。");
                        break;
                    }
                    default:
                        break;
                }
            }
	    }
	};
	
	private void showToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
