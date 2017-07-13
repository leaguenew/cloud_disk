package com.echoii.tv;

import android.content.Context;
import android.os.Handler;
/**
 * 
 * @author Jony Zhang
 * @create time 2014.1
 * 
 * 上下文环境与界面更新基础类，所有需要用到Context和Handler的类可以继承此类
 *
 */
public class BaseContext {
	
	private Context context;
	private Handler handler;
	
	public BaseContext() {
		super();
	}

	public BaseContext(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
