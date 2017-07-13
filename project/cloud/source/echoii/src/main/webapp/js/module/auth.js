define(function(require, exports) {
	
	var url_preLogin = "service/0/auth/pre_login";
	var url_login = "service/0/auth/login";
	var url_reg = "service/0/auth/reg";
	var url_logout = "service/0/auth/log_out";
	var url_checklogin = "service/0/auth/check_login";
	var url_forgetpw = "service/0/auth/forget_password";
	var url_resetpw = "service/0/auth/reset_password";

	function fnPreLogin(email, callback) {
		var preData = {
			email : email
		};
		$.ajax({
			url : url_preLogin,
			async : false,
			type : "GET",
			data : preData,
			success : callback
		});
	}

	function fnLogin(email, psd, code, callback) {
		var myDate = new Date();
		var data = {
			email : email,
			password : psd,
			idcode : code,
			time : myDate.toLocaleTimeString()
		};
		$.get(url_login, data, callback);
	}

	function fnReg(email, psd, callback) {
		var data = {
			email : email,
			password : psd
		};
		$.get(url_reg, data, callback);

	}

	function fnInfo(userId, token, callback) {
		var data = {
			user_id : userId,
			token : token
		};

		$.get(url_reg, data, callback);

	}

	function fnLogout(userId, token, callback) {
		var data = {
			user_id : userId,
			token : token
		};
		$.get(url_logout, data, callback);
	}
	
	function fnchecklogin(userId, token, callback) {
		var data = {
			user_id : userId,
			token : token,
			return_info :"true"
		};
		$.get(url_checklogin, data, callback);
	}
	
	function fnForgetPassword(email, callback) {
		var data = {
			email : email
		};
		$.get(url_forgetpw, data, callback);

	}
	
	function fnResetPassword(email,psd,vcode, callback) {
		var data = {
				email : email,
				password : psd,
				pw_valid_code:vcode
		};
		$.get(url_resetpw, data, callback);

	}

	
	exports.fnPreLogin = fnPreLogin;
	exports.fnLogin = fnLogin;
	exports.fnReg = fnReg;
	exports.fnInfo = fnInfo;
	exports.fnLogout = fnLogout;
	exports.fnchecklogin = fnchecklogin;
	exports.fnForgetPassword = fnForgetPassword;
	exports.fnResetPassword = fnResetPassword;
});