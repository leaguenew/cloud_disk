package com.echoii.tv.junit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.json.JSONException;

import android.test.ActivityInstrumentationTestCase2;

import com.echoii.tv.EchoiiTVMainActivity;
import com.echoii.tv.HttpHelper;
import com.echoii.tv.model.Device;
import com.echoii.tv.util.CommonUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>单元测试类</p>
 *
 */
public class EchoiiTestCase extends ActivityInstrumentationTestCase2<EchoiiTVMainActivity> {
	
	EchoiiTVMainActivity activity ;
	HttpHelper helper ;

	public EchoiiTestCase() {
		super(EchoiiTVMainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		helper = new HttpHelper(activity, activity.handler);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		activity = null;
		helper = null;
	}
	
	public void testGetFile() {
		Device device = new Device();
		device.setDeviceId(CommonUtil.getLocalMacAddress(activity));
		device.setDeviceToken("878caf0afb53b20dd8620fdcda613126");
		try {
			helper.getDatacenterList(device, "root");
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void testGetAllFile() {
		Device device = new Device();
		device.setDeviceId(CommonUtil.getLocalMacAddress(activity));
		device.setDeviceToken("878caf0afb53b20dd8620fdcda613126");
		try {
			helper.getAllDatacenterList(device);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
