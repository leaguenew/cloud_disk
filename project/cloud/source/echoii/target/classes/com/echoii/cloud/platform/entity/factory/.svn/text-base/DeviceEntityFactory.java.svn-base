/**
 * 
 */
package com.echoii.cloud.platform.entity.factory;

import com.echoii.cloud.platform.entity.DeviceEntity;
import com.echoii.cloud.platform.model.Hdc;

/**
 * @author leaguenew
 *
 */
public class DeviceEntityFactory {
	public final static DeviceEntity getDeviceEntity(Hdc dev){
		if(dev==null){
			return null;
		}
		
		DeviceEntity de = new DeviceEntity();
		
		de.setDeviceId(dev.getDeviceId());
		de.setToken(dev.getToken());
		
		return de;
	}
}
