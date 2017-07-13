define(function(require, exports) {

	var UTIL = require("./module/util.js");
	var SHARE = require("./module/share.js");
	var AUTH = require("./module/auth.js");

	$.ajaxSetup({
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		cache : true
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
			$("#header-userid  a").text(UTIL.fnCookie("email"));

		} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
			fnlogout();
		}
	});

	function adjustWindowSize() {
		$("#userinfo-cat-nav").css("height",
				($(window).height() - $("#header-top-nav").height()) + "px");
	}

	window.onresize = adjustWindowSize;
	$(document).ready(adjustWindowSize);

	// ///////////////logout/////////////////////////////
	function fnlogout() {
		var para = UTIL.fnGetDefaultPara();
		AUTH.fnLogout(para.user_id, para.token);
		UTIL.fnCookie("token", "");
		UTIL.fnCookie("uid", "");
		UTIL.fnCookie("email", "");
		UTIL.fnRedirect("homepage.html", location.href);
	}

	// get url para
	$.extend({
		getUrlVars : function() {
			var vars = [], hash;
			var hashes = window.location.href.slice(
					window.location.href.indexOf('?') + 1).split('&');
			for ( var i = 0; i < hashes.length; i++) {
				hash = hashes[i].split('=');
				vars.push(hash[0]);
				vars[hash[0]] = hash[1];
			}
			return vars;
		},
		getUrlVar : function(name) {
			return $.getUrlVars()[name];
		}
	});

	var sourceid = $.getUrlVar('sourceid');
	var para = {
		file_id : sourceid,
	};

	var type;
	SHARE.shareInfo(para, function(json) {
		if (json.status == UTIL.fnStatus("OK")) {
			$("#username").text("共享作者：" + json.data.userName);
			$("#sourcename").text(json.data.name);
			$("#size").text(json.data.size);
			$("#date").text(json.data.createDate);
			type = json.data.type;
			if (type == "passwd") {
				$("#share_code_box").show();
			} else {
				$("#share_code_box").hide();
			}
		} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
			fnlogout();
		}

	});

	$("#file-btns-down").click(
			function() {
				if (type == "passwd") {
					para.idcode = $("#modal-share-id").val();
					console.log("para.idcode" + para.idcode);
					$("#sdf-file-id").val(para.file_id);
					$("#sdf-idcode").val(para.idcode);
					$("#share-download-form").attr("action",
							UTIL.fnUrl("share_download"));
					$("#share-download-form").submit();
				} else {
					$("#sdf-file-id-nocode").val(para.file_id);
					$("#share-download-form-nocode").attr("action",
							UTIL.fnUrl("share_download"));
					$("#share-download-form-nocode").submit();
				}
			});

	$(".btn-wrapper")
			.attr(
					{
						"style" : "width:"
								+ $(window).width()
								+ "px;height:"
								+ $(window).width()
								* 0.045
								+ "px;"
								+ "margin-left:-240px;margin-top:-15px;background-color:#f0f0f0;"
					});

	$("#header-top-image").click(function() {
		UTIL.fnRedirect("main.jsp", location.href);
	});
});
