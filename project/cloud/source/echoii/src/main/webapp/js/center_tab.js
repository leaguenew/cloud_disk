define(function(require, exports) {
	require("./lib/jquery.treeview.js");
	require("./lib/jquery.ztree.all-3.5.min.js");
	var CENTER = require("./module/center.js");
	var UTIL = require("./module/util.js");
	var AUTH = require("./module/auth.js");
	var FILE = require("./module/file.js");
	var TMPL = require("./module/template.js");
	var _stat = {
		order : "desc",
		order_by : "lmf_date",
		tabAllFileIndex : 0,
		size : 40,
		checked_file : [],

	};
	function fnShowCenter() {
		$("#center-listpage").attr({
			"style" : "display: block;"
		});
		$("#center-contentpage").attr({
			"style" : "display: none;"
		});
		$("#tab-center-approve").empty();
		$("#tab-center-request").empty();
		$("#tab-center-refuse").empty();
		fnLoadTabCenter();
	}

	function fnLoadTabCenter(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = 0;
		para.size = 40;
		para.status = "approve";
		if (folderId) {
			para.folder_id = folderId;
		}

		CENTER.fnBindingDevice(para, function(json) {
			console.log("fnbingdevice!");
			if (json.status == UTIL.fnStatus("OK")) {
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnCenter_approve(), sorted).appendTo(
						"#tab-center-approve");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				alert(LANG.sharegroup_list_wrong);
			}
		});

		para.status = "request";
		CENTER.fnBindingDevice(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnCenter_request(), sorted).appendTo(
						"#tab-center-request");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				// alert(LANG.sharegroup_list_wrong);
			}
		});

		para.status = "refuse";
		CENTER.fnBindingDevice(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnCenter_refuse(), sorted).appendTo(
						"#tab-center-refuse");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				// alert(LANG.sharegroup_list_wrong);
			}
		});
	}

	////////  request center modal //////////////////
	$("#request-center-btn").click(function() {
		$("#modal-center-request").modal('show');
	});

	$("#center-modal-request-btn").click(function() {

		var para = UTIL.fnGetDefaultPara();
		para.device_id = $("#center-request-id").val();

		CENTER.fnBind(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.center_request_success);
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				alert(LANG.center_request_wrong);
			} else if(json.status == UTIL.fnStatus("DEVICE_ID_NOTFOUND")){
				alert(LANG.center_not_found);
			} else if(json.status == UTIL.fnStatus("NAME_DUP")){
				alert(LANG.center_not_found);
			}else {
				alert(LANG.center_name_dup);
			}
			$("#modal-center-request").modal('hide');
		});
	});

	//////////////// cancel band center  ////////////////////////

	$("#center-content-quit").click(function() {
		var para = UTIL.fnGetDefaultPara();
		para.device_id = $("#center-content-name").attr("data-id");
		console.log("quit click");
		CENTER.fnCancelBind(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.center_cancel_success);
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				alert(LANG.center_cancel_wrong);
			} else {
				alert(LANG.center_cancel_wrong);
			}
		});
	});
	
	$("#center-content-cancel").click(function(){
		var para = UTIL.fnGetDefaultPara();
		para.device_id = $("#center-content-name").attr("data-id");
		console.log("quit click");
		CENTER.fnCancelBind(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.center_cancel_success);
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				alert(LANG.center_cancel_wrong);
			} else {
				alert(LANG.center_cancel_wrong);
			}
		});
		
	})


	// //////////////////click group-well into group///////////////
	$("#tab-center-content").click(
			function(e) {
				var target = e.target;
				var center_info = [ {
					data_type : $(target).attr("data-type"),
					data_id : $(target).attr("data-id"),
					data_name : $(target).attr("data-name")
				} ];
				$("#center-content-namediv").empty();
				$.tmpl(TMPL.fnCenter_name(), center_info).appendTo(
						"#center-content-namediv");

				var para = UTIL.fnGetDefaultPara();
				para.device_id = center_info[0].data_id;
				CENTER.fnDeviceInfo(para, function(json) {
					if (json.status == UTIL.fnStatus("OK")) {
						console.log("center info ok");
					} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
						console.log("auth error");
					} else {
						console.log("other error");
					}
				});

				if (center_info[0].data_type == 'approve') {
					$("#center-listpage").hide();
					$("#center-contentpage").show();
					$("#center-content-name").text(
							"家庭数据中心 ：" + $(target).attr("data-name")+"(已绑定)");
					$("#center-content-cancel").hide();

				}

				if (center_info[0].data_type == 'request') {
					$("#center-listpage").hide();
					$("#center-contentpage").show();
					$("#center-content-name").text(
							"家庭数据中心  ：" + $(target).attr("data-name")+"(审批中)");
					$("#center-content-quit").hide();
					$("#center-content-cancel").show();
				}

				if (center_info[0].data_type == 'refuse') {
					$("#center-listpage").hide();
					$("#center-contentpage").show();
					$("#center-content-name").text(
							"家庭数据中心 ：" + $(target).attr("data-name")+"(审批未通过)");
					$("#center-content-quit").hide();
					$("#center-content-cancel").hide();
				}
			});

	// ///////////////logout/////////////////////////////
	function fnlogout() {
		var para = UTIL.fnGetDefaultPara();

		AUTH.fnLogout(para.user_id, para.token);
		UTIL.fnCookie("token", "");
		UTIL.fnCookie("uid", "");
		UTIL.fnCookie("email", "");
		UTIL.fnRedirect("homepage.html", location.href);
	}

	exports.fnShowCenter = fnShowCenter;
	exports.fnLoadTabCenter = fnLoadTabCenter;
});