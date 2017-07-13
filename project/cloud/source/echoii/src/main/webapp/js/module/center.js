define(function(require, exports) {

	var center_reg = "service/0/device/reg";
	var center_login = "service/0/device/login";
	var center_device_info = "service/0/device/device_info";
	var center_binding_device = "service/0/device/binding/list_bound_device";
	var center_binding_user = "service/0/device/binding/list_bound_user";
	var center_bind = "service/0/device/binding/bind";
	var center_cancel_binding = "service/0/device/binding/cancel_binding";
	var center_update_status = "service/0/device/binding/update_binding_status";


		
	function fnReg(para, callback) {
		var query = fnParsePara(para);
		$.get(center_reg, query, callback);
	}
	
	function fnLogin(para,callback){
		var query = fnParsePara(para);
		$.get(center_login, query, callback);
	}

	function fnDeviceInfo(para, callback) {
		var query = fnParsePara(para);
		$.get(center_device_info, query, callback);
	}
	
	function fnBindingDevice(para, callback) {
		var query = fnParsePara(para);
		$.get(center_binding_device, query, callback);
	}

	function fnBindingUser(para, callback) {
		var query = fnParsePara(para);
		$.get(center_binding_user, query, callback);
	}

	function fnBind(para, callback) {
		var query = fnParsePara(para);
		$.get(center_bind, query, callback);
	}

	function fnCancelBind(para, callback) {
		var query = fnParsePara(para);
		$.get(center_cancel_binding, query, callback);
	}
	
	function fnUpdateStatus(para, callback) {
		var query = fnParsePara(para);
		$.get(center_update_status, query, callback);
	}
	
	
	function fnParsePara(para) {
		var query = {
			user_id : para.user_id,
			token : para.token,
			device_id : para.device_id,
			status : para.status,
			begin : para.begin,
			size : para.size,
			device_token : para.device_token,
			new_status : para.new_status,
		};
		return query;
	}

	exports.fnReg = fnReg;
	exports.fnLogin = fnLogin;
	exports.fnDeviceInfo = fnDeviceInfo;
	exports.fnBindingDevice = fnBindingDevice;
	exports.fnBindingUser = fnBindingUser;
	exports.fnBind = fnBind;
	exports.fnCancelBind = fnCancelBind;
	exports.fnUpdateStatus = fnUpdateStatus;

});