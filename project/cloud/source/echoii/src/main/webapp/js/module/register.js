define(function(require, exports) {

	var AUTH = require("./auth");
	var HASH = require("./hash");

	$('form').validate({
		rules : {
			username : {
				required : true,
				minlength : 6
			},
			password : {
				required : true
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}

	});

	$('#regSubmit').click(function() {

		var username = $("#id_username").val();
		var password = $("#id_password1").val();
		var psdHash = HASH.md5(password);
		AUTH.fnReg(username, psdHash);

	});

});