define(function(require, exports) {

	var AUTH = require("./module/auth");
	var HASH = require("./module/hash");
	var UTIL = require("./module/util.js");

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
				required : LANG.password + LANG.can_not_empty,
			},
			code : {
				required : LANG.validate_code + LANG.can_not_empty,
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
		var useremail = $("#login-email").val();

		// prelogin ,get pre_login_token
		AUTH.fnPreLogin(useremail, function(json) {

			idcode = json.data.pre_login_token;
			console.log("idcode: " + idcode);
			$("#login-show-code").html(idcode).addClass("login-show-code");

		});
	});

	// click to get idcode
	$('#get-login-code').click(function() {

		var useremail = $("#login-email").val();

		// prelogin ,get pre_login_token
		AUTH.fnPreLogin(useremail, function(json) {

			idcode = json.data.pre_login_token;
			console.log("idcode: " + idcode);
			$("#login-show-code").html(idcode).addClass("login-show-code");

		});
	});

	// login button function
	$('#loginSubmit').click(function() {

		var useremail = $("#login-email").val();
		var password = $("#login-password").val();
		var codevalue = $("#login-code").val();

		if (codevalue == idcode) {

			newPsd = HASH.md5(HASH.md5(password) + idcode);
			console.log("newPSD: " + newPsd);

			AUTH.fnLogin(useremail, newPsd, idcode, function(json) {
				
			
				if (json.status == "200") {

					UTIL.fnCookie("token", json.data.token);
					UTIL.fnCookie("uid", json.data.id);
					console.log("success, token = " + json.data.token);
					
					url = location.href;
					console.log("url.indexOf('=') " + url.indexOf("="));
					if(url.indexOf("=") == -1){
						UTIL.fnRedirect("main.jsp");
					}
					else{
						source_url=url.substring(url.indexOf("=")+1,url.length);
						console.log("source_url = " + source_url);
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

		console.log(val);

		if (val == "checked" || val == true) {

			var useremail = $("#reg-email").val();
			var password = $("#reg-password").val();

			console.log("regiter  submit");
			console.log("hash password:" + HASH.md5(password));

			var psdHash = HASH.md5(password);

			AUTH.fnReg(useremail, psdHash, function(json) {
				if (json.status == "200") {
					
					alert(LANG.reg_success);
					console.log("fnreg  function");
					console.log("success ,id :" + json.data.email);
					$("#login-email").attr("value", useremail);
				} else {
					if (json.status == "409") {
						alert(LANG.email_exists);
					}
				}
			});
		} else {
			alert(LANG.agree_term);
		}
	});

});
