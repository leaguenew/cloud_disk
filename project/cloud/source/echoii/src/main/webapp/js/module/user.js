define(function(require, exports) {

	var url_userInfo = "service/0/user/userdetail";
	var url_userInfoChange = "service/0/user/update_userdetail";
	var url_userPassword = "service/0/user/update_password";

	function fnInfo(para, callback) {
		var query = fnParsePara(para);
		$.get(url_userInfo, query, callback);
	}

	function fnInfoChange(para, callback) {
		var query = fnParsePara(para);
		$.get(url_userInfoChange, query, callback);
	}

	function fnPasswordChange(para, callback) {
		var query = fnParsePara(para);
		$.get(url_userPassword, query, callback);
	}

	function fnParsePara(para) {
		var query = {
			user_id : para.user_id,
			token : para.token,
			name : para.name,
			nickname : para.nickname,
			idcard : para.idcard,
			gender : para.gender,
			birthday : para.birthday,
			QQ : para.QQ,
			tel : para.tel,
			job : para.job,
			introduction : para.introduction,
			old_password : para.oldpassword,
			password : para.password,
		};
		return query;
	}

	exports.fnInfo = fnInfo;
	exports.fnInfoChange = fnInfoChange;
	exports.fnPasswordChange = fnPasswordChange;
});