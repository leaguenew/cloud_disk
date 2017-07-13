define(function(require, exports) {

	var AUTH = require("./auth");
	var HASH = require("./hash");

	$('#loginDoor').mouseover(function() {
		$('#left').show();
		$('#right').hide();
	});

	$('#registDoor').mouseover(function() {
		$('#left').hide();
		$('#right').show();
	});

	$('form').validate({
		rules : {
			id_username : {
				required : true,
				email : true,
				minlength : 3
			},
			id_password : {
				required : true
			}
		},
		messages : {
			id_username : {
				required : "email不能为空",
				email : "email地址不正确"
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});

	$('#loginSubmit').click(function() {

		var useremail = $("#login_email").val();
		var password = $("#login_password").val();
		var emailHash = HASH.md5(useremail);

		AUTH.fnPreLogin(emailHash, function(json) {

			console.log("success, token = " + json.data.pre_login_token);

			newPsd = HASH.md5(HASH.md5(password) + json.data.pre_login_token);
			console.log("newPSD: " + newPsd);

			idcode = json.data.pre_login_token;
			console.log("idcode: " + idcode);

		});

		AUTH.fnLogin(emailHash, newPsd, idcode);

	});

	$('#regSubmit').click(function() {

		var useremail = $("#reg_email").val();
		var password = $("#reg_password").val();
		var psdHash = HASH.md5(password);

		AUTH.fnReg(useremail, psdHash);
	});

});
