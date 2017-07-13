define(function(require, exports) {

	var USER = require("./module/user.js");
	var UTIL = require("./module/util.js");
	var HASH = require("./module/hash.js");
	var AUTH = require("./module/auth.js");

	var _stat = {
		currentTabId : "home",
		tabAllFileIndex : 0,
		tabDocIndex : 0,
		currentFolderId : "root",
		navList : [],
		end : 0
	};

	$.ajaxSetup({
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		cache : true
	});

	// /////////// userinfo-cat-nav ////////////////////////
	$('#userinfo-cat-nav a').click(function(e) {
		var targetId = e.target.id;

		if (targetId == "userinfo-pane-link-base") {
			fnShowUserinfoPaneBase();
		}
		if (targetId == "userinfo-pane-link-safe") {
			fnShowUserinfoPaneSafe();
		}

		e.preventDefault();
		$(this).tab('show');
	});

	// all show panes function
	function fnShowUserinfoPaneBase() {
		var para = UTIL.fnGetDefaultPara();
		$("#userinfo-base-email").text(UTIL.fnCookie("email"));
		USER.fnInfo(para, function(json) {
			
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("userido fninfo ");
				$("#userinfo-name").val(json.data.name);
				$("#userinfo-nickname").val(json.data.nickName);
				$("#userinfo-idcard").val(json.data.idCard);
				$("#userinfo-datepicker").val(json.data.birthday);
				$("#userinfo-qq").val(json.data.QQ);
				$("#userinfo-tel").val(json.data.tel);
				$("#userinfo-job").val(json.data.job);
				$("#userinfo-introduction").val(json.data.introduction);
				if (json.data.gender == "m") {
					$('input:radio[name=sex]')[0].checked = true;
				} else {
					$('input:radio[name=sex]')[1].checked = true;
				}
				;
			} else {
				UTIL.fnRedirect("homepage.html", location.href);
			} 
		});

	}

	$(document).ready(fnShowUserinfoPaneBase);

	function fnShowUserinfoPaneSafe() {
		var para = UTIL.fnGetDefaultPara();
		$("#userinfo-safe-email").text(UTIL.fnCookie("email"));
	}
	
	// ///////////////////////userinfo-base form validate ///////////////////
	$('#userinfo-baseform').validate({
		rules : {
			nickname : {
				required : true
			},
			idcard : {
				minlength : 18,
				maxlength : 18
			},
			tel : {
				digits : true,
				maxlength : 11,
				minlength : 11
			},
			birthday : {
				date : true
			},
			qq : {
				digits : true
			}
		},
		messages : {
			nickname : {
				required : LANG.nickname + LANG.can_not_empty
			},
			idcard : {
				minlength : LANG.idcard,
				maxlength : LANG.idcard
			},
			tel : {
				digits : LANG.tel,
				minlength : LANG.tel,
				maxlength : LANG.tel
			},
			birthday : {
				date : LANG.birthday
			},
			qq : {
				digits : LANG.qq
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.siblings("span"));
		}
	});

	
	// //////////////////////userinfo-safe form validate////////////////////
	$('#userinfo-safeform').validate({
		rules : {
			password : {
				required : true
			},
			newpassword : {
				required : true
			},
			repassword : {
				required : true,
				equalTo : "#userinfo-newpassword"
			}
		},
		messages : {
			password : {
				required : LANG.oldpassword + LANG.can_not_empty
			},
			newpassword : {
				required : LANG.newpassword + LANG.can_not_empty
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

	// /////////////////////show capacity and welecome sign////////////////////
	UTIL.fnCapacity(function(json) {
		if (json.status == UTIL.fnStatus("OK")) {
			if (json.data.percents < 15) {
				$("#header-menu .progress .bar").css("width", "15%");
			} else {
				$("#header-menu .progress .bar").css("width",
						json.data.percents + "%");
			}

			$("#header-menu .progress .text").text(json.data.size + "/5T");
			$("#header-userid").text(UTIL.fnCookie("email"));
			
		} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
			fnlogout();
		} 
	});
	
	function adjustWindowSize() {
		$("#userinfo-cat-wrapper").css("height",  ($(window).height()- $("#header-top-nav").height())+ "px");
	}

	window.onresize = adjustWindowSize;
	$(document).ready(adjustWindowSize);
	
	/////////////////logout/////////////////////////////
	function fnlogout(){
		var para = UTIL.fnGetDefaultPara();
		
		AUTH.fnLogout(para.user_id ,para.token);
		UTIL.fnCookie("token", "");
		UTIL.fnCookie("uid", "");
		UTIL.fnCookie("email", "");
		UTIL.fnRedirect("homepage.html", location.href);
	}

	// /////////////////////logout////////////////////
	$("#header-logout").click(function() {
		fnlogout();
	});

	// /////////return to main.js home-page////////////////////////////
	$("#header-top-image").click(function() {

		UTIL.fnRedirect("main.jsp", location.href);
	});
	$("#userinfo-base-return").click(function() {

		UTIL.fnRedirect("main.jsp", location.href);
	});

	$("#userinfo-safe-return").click(function() {

		UTIL.fnRedirect("main.jsp", location.href);
	});

	// /////////////////userinfo base save button///////////////////
	$("#userinfo-base-save").click(function() {
		
		var para = UTIL.fnGetDefaultPara();
		para.name = $("#userinfo-name").val();
		para.nickname = $("#userinfo-nickname").val();
		para.idcard = $("#userinfo-idcard").val();
		para.gender = $('input:radio[name=sex]:checked').val();
		para.birthday = $("#userinfo-datepicker").val();
		para.QQ = $("#userinfo-qq").val();
		para.tel = $("#userinfo-tel").val();
		para.job = $("#userinfo-job").val();
		para.introduction = $("#userinfo-introduction").val();
		
		USER.fnInfoChange(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.userinfo_base_success);
				fnShowUserinfoPaneBase();
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				fnlogout();
			} else {
				alert(LANG.userinfo_base_wrong);
			}
		});
	});

	// /////////////////userinfo safe save button///////////////////
	$("#userinfo-safe-save").click(function() {
		var password = $("#userinfo-newpassword").val();
		var repassword = $("#userinfo-repassword").val();
		if ((password!="") && (password!=null) && (password == repassword)) {
			var prompt_msg = confirm(LANG.userinfo_safe_change_msg);
			if (prompt_msg == true) {
				
				var para = UTIL.fnGetDefaultPara();
				para.oldpassword = HASH.md5($("#userinfo-password").val());
				para.password = HASH.md5($("#userinfo-newpassword").val());
				
				USER.fnPasswordChange(para, function(json) {
					if (json.status == UTIL.fnStatus("OK")) {
						console.log("password ok");
						alert(LANG.userinfo_safe_success);
					} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
						fnlogout();
					} else if (json.status == UTIL.fnStatus("SYS_ERROR")) {
						alert(LANG.oldpassword + LANG.not_correct);
					} else {
						alert(LANG.userinfo_safe_wrong);
					}
				});
			}
		}
		

	});

});