define(function(require, exports) {

	var AUTH = require("./module/auth");
	var HASH = require("./module/hash");
	var UTIL = require("./module/util.js");
	
	//checklogin
	var para = UTIL.fnGetDefaultPara();
	AUTH.fnchecklogin(para.user_id,para.token,function(json){
		if (json.status == UTIL.fnStatus("OK")) {
			alert("您的账户：  "+json.data.nickName+" 已经登录！");
			UTIL.fnRedirect("main.jsp", location.href);
		} 
	});
	
	// loginDoor mouseover
	$('#loginDoor').mouseover(function() {
		$('#login-box').show();
		$('#register-box').hide();
		$('#loginDoor').addClass("textbox-border");
		$('#registDoor').removeClass("textbox-border");
	});

	// registerDoor mouseover
	$('#registDoor').mouseover(function() {
		$('#login-box').hide();
		$('#register-box').show();
		$('#registDoor').addClass("textbox-border");
		$('#loginDoor').removeClass("textbox-border");
	});

	// login form validate
	$('#login-form').validate({
		rules : {
			email : {
				required : true,
				email : true
			},
			password : {
				required : true
			}
		},
		messages : {
			email : {
				required : LANG.email + LANG.can_not_empty,
				email : LANG.email + LANG.not_correct
			},

			password : {
				required : LANG.password + LANG.can_not_empty
			},
			
			code : {
				required : LANG.validate_code + LANG.can_not_empty
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});

	// register form validate
	$('#register-form').validate({
		rules : {
			email : {
				required : true,
				email : true
			},
			password : {
				required : true
			},
			repassword : {
				required : true,
				equalTo : "#reg-password"
			}
		},
		messages : {
			email : {
				required : LANG.email + LANG.can_not_empty,
				email : LANG.email + LANG.not_correct
			},
			password : {
				required : LANG.password + LANG.can_not_empty
			},
			repassword : {
				required : LANG.repassword + LANG.can_not_empty,
				equalTo : LANG.repassword + LANG.not_correct
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});

	// input blur to get idcode
	$('#login-email').blur(function() {
		var useremail = $("#login-email").val().replace(/(^\s+)|(\s+$)/g,"");
		// prelogin ,get pre_login_token
		AUTH.fnPreLogin(useremail, function(json) {

			idcode = json.data.pre_login_token;
			$("#login-show-code").html(idcode).addClass("login-show-code");

		});
	});

	// click to get idcode
	$('#get-login-code').click(function() {

		var useremail = $("#login-email").val().replace(/(^\s+)|(\s+$)/g,"");

		// prelogin ,get pre_login_token
		AUTH.fnPreLogin(useremail, function(json) {

			idcode = json.data.pre_login_token;
			$("#login-show-code").html(idcode).addClass("login-show-code");

		});
	});

	// login button function
	$('#loginSubmit')
			.click(
					function() {

						var vv = $('#login-form').valid();
						if (!vv) {
							return;
						}

						var useremail = $("#login-email").val().replace(/(^\s+)|(\s+$)/g,"");
						var password = $("#login-password").val();
						var codevalue = $("#login-code").val();
						if (codevalue.toLocaleLowerCase() == idcode
								.toLocaleLowerCase()) {

							newPsd = HASH.md5(HASH.md5(password) + idcode);

							AUTH.fnLogin(useremail, newPsd, idcode, function(
									json) {

								if (json.status == "200") {
									UTIL.fnCookie("token", json.data.token);
									UTIL.fnCookie("uid", json.data.id);
									UTIL.fnCookie("email", json.data.email);

									url = location.href;

									if (url.indexOf("=") == -1) {
										UTIL.fnRedirect("main.jsp");
									} else {
										source_url = url.substring(url
												.indexOf("=") + 1, url.length);
										UTIL.fnRedirect(source_url);
									}
								} else {
									if (json.status == "401") {
										alert(LANG.alert_login_wrong);
									}
								}
							});
						} else {
							alert(LANG.alert_wrong_code);
						}

					});

	// register button function
	$('#regSubmit').click(function() {

		var val = $("#reg-checkbox").prop("checked");
		var reg_password = $("#reg-password").val();
		var reg_repassword = $("#reg-repassword").val();
		if ((reg_password!="") && (reg_password!=null) && (reg_password == reg_repassword)) {
			if (val == "checked" || val == true) {

				var useremail = $("#reg-email").val().replace(/(^\s+)|(\s+$)/g,"");
				var password = $("#reg-password").val();
				var psdHash = HASH.md5(password);

				AUTH.fnReg(useremail, psdHash, function(json) {
					if (json.status == "200") {
						alert(LANG.reg_success);
						$("#login-email").attr("value", useremail);
					} else if (json.status == "409") {
							alert(LANG.email_exists);
					}
				});
			} else {
				alert(LANG.agree_term);
			}
		} 
		
	});
	
	
	$('#login-code').keydown(function(e){
		if(e.keyCode==13){
			$("#loginSubmit").trigger('click');
		}
		}); 
	
	//forgetpw form validate
	$('#forgetpw-form').validate({
		rules : {
			email : {
				required : true,
				email : true
			}
		},
		messages : {
			email : {
				required : LANG.email + LANG.can_not_empty,
				email : LANG.email + LANG.not_correct
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});
	
	// forgetpw button function
	$('#forgetpwSubmit').click(function() {
		var vv = $('#forgetpw-form').valid();
		if (!vv) {
			return;
		}
		var useremail = $("#forgetpw-email").val().replace(/(^\s+)|(\s+$)/g,"");

		AUTH.fnForgetPassword(useremail, function(json) {
			if (json.status == "200") {
				alert(LANG.sendemail_success);
				} else if (json.status == "401") {
					alert(LANG.sendemail_timeLimit);
				} else if (json.status == "404") {
					alert(LANG.sendemail_noRegister);
				} else if (json.status == "500") {
					alert(LANG.sendemail_sysError);
				}else {
					alert(LANG.sendemail_wrong);
				}
			});
						
		});
	
	// resetpw form validate
	$('#resetpw-form').validate({
		rules : {
			password : {
				required : true
			},
			repassword : {
				required : true,
				equalTo : "#reset-password"
			}
		},
		messages : {
			password : {
				required : LANG.password + LANG.can_not_empty
			},
			repassword : {
				required : LANG.repassword + LANG.can_not_empty,
				equalTo : LANG.repassword + LANG.not_correct
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});
	
	
	// resetpw button function
	$('#resetpwSubmit').click(function() {
		var url = unescape(window.location.href);
		var password = $("#reset-password").val();
		var repassword = $("#reset-repassword").val();
		var psdHash = HASH.md5(password);
		var useremail = url.split("?")[1].split("&")[0].split("=")[1];
		var vcode = url.split("?")[1].split("&")[1].split("=")[1];
		
		if ((password!="") && (password!=null) && (password == repassword)) {
			AUTH.fnResetPassword(useremail,psdHash,vcode, function(json) {
				if (json.status == "200") {
					alert(LANG.resetpw_success);
					window.location.href="homepage.html";
				} else {
					alert(LANG.resetpw_wrong);
				}
			});
		} 
		
	});

});