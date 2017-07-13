define(function(require, exports) {
	require("./lib/jquery.treeview.js");
	require("./lib/jquery.ztree.all-3.5.min.js");
	var SHAREGROUPS = require("./module/sharegroups.js");
	var SHARE = require("./module/share.js");
	var UTIL = require("./module/util.js");
	var AUTH = require("./module/auth.js");
	var FILE = require("./module/file.js");
	var TMPL = require("./module/template.js");
	var _stat = {
		order : "desc",
		order_by : "lmf_date",
		tabAllFileIndex : 0,
		size : 40,
		zNodes : [],
		checked_file:[],
		setting : {
			check : {
				enable : true,
				chkboxType : {
					"Y" : "",
					"N" : "ps"
				}
			},
			data : {
				simpleData : {
					enable : true
				},
				key : {
					title : "data_id"
				}
			},
			callback : {
				beforeExpand : zTreeBeforeExpand,
				onCheck: onCheck
			}
		}
	};
	function fnShowShareGroup() {
		$("#sharegroup-listpage").attr({
			"style" : "display: block;"
		});
		$("#sharegroup-contentpage").attr({
			"style" : "display: none;"
		});
		$("#tab-sharegroup-created").empty();
		$("#tab-sharegroup-join").empty();
		$("#tab-sharegroup-others").empty();
		fnLoadTabShareGroup();
	}

	function fnLoadTabShareGroup(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = 0;
		para.size = 40;
		para.mode = "create";
		para.order = "name";
		if (folderId) {
			para.folder_id = folderId;
		}

		SHAREGROUPS.fnList(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("sharegroup list ok");
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnShareGroupThumb_create(), sorted).appendTo(
						"#tab-sharegroup-created");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				alert(LANG.sharegroup_list_wrong);
			}
		});

		para.mode = "join";
		console.log("para.mode ；" + para.mode);
		SHAREGROUPS.fnList(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("sharegroup list ok");
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnShareGroupThumb_join(), sorted).appendTo(
						"#tab-sharegroup-join");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				// alert(LANG.sharegroup_list_wrong);
			}
		});

		para.mode = "all";
		console.log("para.mode ；" + para.mode);
		SHAREGROUPS.fnList(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("sharegroup list ok");
				if (json.data.length > 30) {
					$("[id$='-more-btn']").attr({
						"style" : "display: inline-block;"
					});
				}
				var sorted = FILE.fnSortFolderAhead(json.data);
				$.tmpl(TMPL.fnShareGroupThumb_others(), sorted).appendTo(
						"#tab-sharegroup-others");

			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				// alert(LANG.sharegroup_list_wrong);
			}
		});
	}

	$("#search-group-btn").click(function() {
		$("#modal-sharegroup-search").modal('show');
		$("#sharegroup-modal-search-join").hide();
		$("#sharegroup-search-result").hide();

	});

	$("#create-group-btn").click(function() {
		$("#modal-sharegroup").modal('show');
	});

	// ////////create group modal////////////////
	$("#create-group-modal-btn").click(function() {

		var para = UTIL.fnGetDefaultPara();
		para.name = $("#sharegroup-create-name").val();
		para.desp = $("#sharegroup-create-info").val();
		SHAREGROUPS.fnCreate(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.sharegroup_create_success);
				$("#modal-sharegroup").modal('hide');
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
				// fnlogout();
			} else {
				alert(LANG.sharegroup_create_wrong);
			}
		});
	});

	// /////search group modal//////////////////
	$("#sharegroup-modal-search-btn").click(function() {

		var para = UTIL.fnGetDefaultPara();
		para.group_id = $("#sharegroup-search-groupid").val();
		para.return_detail = "false";

		SHAREGROUPS.fnInfo(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				$("#sharegroup-search-result").show();
				$("#sharegroup-search-groupname").text(json.data.name);
				$("#sharegroup-search-groupdesp").text(json.data.desp);
				$("#sharegroup-modal-search-join").show();
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				console.log("other error");
			}
		});
	});

	// ///////////////////join group//////////////////////////////

	$("#sharegroup-modal-search-join").click(function() {
		var para = UTIL.fnGetDefaultPara();
		para.group_id = $("#sharegroup-search-groupid").val();
		SHAREGROUPS.fnJoin(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.sharegroup_join_success);
				$("#modal-sharegroup-search").modal('hide');
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				console.log("other error");
			}
		});
	});

	$("#sharegroup-content-join").click(function() {
		var para = UTIL.fnGetDefaultPara();
		para.group_id = $("#sharegroup-content-name").attr("data-id");
		SHAREGROUPS.fnJoin(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.sharegroup_join_success);
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				console.log("other error");
			}
		});
	});

	// //////////////////quit group////////////////////////

	$("#sharegroup-content-quit").click(function() {
		var para = UTIL.fnGetDefaultPara();
		para.group_id = $("#sharegroup-content-name").attr("data-id");
		console.log("quit click");
		SHAREGROUPS.fnQuit(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.sharegroup_quit_success);
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				console.log("other error");
			}
		});
	});

	// ////////////////////list member//////////////////////
	$("#sharegroup-content-setting").click(
			function() {
				var para = UTIL.fnGetDefaultPara();
				para.group_id = $("#sharegroup-content-name").attr("data-id");
				$("#listmember-table-body").empty();
				SHAREGROUPS.fnListMember(para, function(json) {
					if (json.status == UTIL.fnStatus("OK")) {
						$("#sharegroup-content-listmember").show();
						var sorted = FILE.fnSortFolderAhead(json.data);
						$.tmpl(TMPL.fnShareGroup_listmember(), sorted)
								.appendTo("#listmember-table-body");

					} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
						console.log("auth error");
					} else {
						console.log("other error");
					}
				});
			});

	// //////////////////upload share files////////////////////

	$("#sharegroup-content-share").click(
			function() {
				var para = UTIL.fnGetDefaultPara();
				para.begin = _stat.tabAllFileIndex;
				para.size = _stat.size;
				para.order = _stat.order;
				para.order_by = _stat.order_by;

				$("#modal-sharegroup-upload").modal('show');

				FILE.fnListAll(para, function(json) {
					if (json.status == UTIL.fnStatus("OK")) {
						var m = 0;
						for ( var i = 0; i < json.data.length; i++) {
							_stat.zNodes.push({
								id : json.data[i].id,
								pId : 0,
								name : json.data[i].name,
								data_id : json.data[i].id,
							});
							m++;
							if (json.data[i].type == "folder") {
								_stat.zNodes[m - 1].open = "false";
								_stat.zNodes.push({
									id : _stat.zNodes[m - 1].id + "-empty",
									pId : json.data[i].id,
									name : "正在加载",
									data_id : json.data[i].id,
								});
								m++;
							}
						}
						$.fn.zTree.init($("#modal-saregroup-filelist-choose"),
								_stat.setting, _stat.zNodes);
					}
				});
			});

	function zTreeBeforeExpand(event, treeNode) {
		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabAllFileIndex;
		para.size = _stat.size;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		var treeObj = $.fn.zTree.getZTreeObj("modal-saregroup-filelist-choose");
		var node = treeObj.getSelectedNodes();
		for ( var i = 0, l = node.length; i < l; i++) {
			treeObj.removeChildNodes(node[i]);
		}
		;
		para.folder_id = treeNode.id;
		FILE.fnListAll(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("json.data length: " + json.data.length);
				var m = 0;
				var newNodes = [];
				for ( var i = 0; i < json.data.length; i++) {
					newNodes.push({
						id : json.data[i].id,
						pId : treeNode.id,
						name : json.data[i].name,
						data_id : json.data[i].id,
						isParent : false
					});
					m++;
					if (json.data[i].type == "folder") {
						newNodes[m - 1].open = "false";
						newNodes.push({
							id : newNodes[m - 1].id + "-empty",
							pId : json.data[i].id,
							name : "正在加载",
							data_id : json.data[i].id,
						});
						m++;
					}
				}
				_stat.zNodes = treeObj.addNodes(node[0], newNodes);
			}
		});
	}
	;
	
	function onCheck(e, treeId, treeNode){
		console.log("oncheck node id ："+treeNode.id);
		_stat.checked_file.push(treeNode.id);
		
	}
	;

	// ////////////////////share file into group/////////////////
	$("#sharegroup-modal-upload-btn").click(function() {
		var file_id_list = _stat.checked_file[0];
		var para = UTIL.fnGetDefaultPara();
		for ( var i = 1; i < _stat.checked_file.length; i++) {
			file_id_list = file_id_list + "," + _stat.checked_file[i];
		}
		para.file_id_list = file_id_list;
		para.group_id = $("#sharegroup-content-name").attr("data-id");
		SHARE.sharegroup(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.sharegroup_upload_success);
				$("#modal-sharegroup-upload").modal('hide');
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				console.log("auth error");
			} else {
				alert(LANG.sharegroup_upload_wrong);
				console.log("other error");
			}
		});
	});

	// //////////////////click group-well into group///////////////
	$("#tab-sharegroup-content").click(
			function(e) {
				var target = e.target;
				var group_info = [ {
					data_type : $(target).attr("data-type"),
					data_id : $(target).attr("data-id"),
					data_name : $(target).attr("data-name")
				} ];
				$("#sharegroup-content-namediv").empty();
				$.tmpl(TMPL.fnShareGroup_name(), group_info).appendTo(
						"#sharegroup-content-namediv");
				$("#sharegroup-content-listmember").hide();
				
				var para = UTIL.fnGetDefaultPara();
				para.begin = 0;
				para.size = 20;
				para.group_id = group_info[0].data_id;
				para.mode = "pending";
				
				$("#listfile-table-body").empty();
				
				SHAREGROUPS.fnFileList(para,function(json){
					if (json.status == UTIL.fnStatus("OK")) {
						console.log("filelist");
						$("#sharegroup-content-listfile").show();
						var sorted = FILE.fnSortFolderAhead(json.data);
						console.log(sorted);
						$.tmpl(TMPL.fnShareGroup_listfile(), sorted)
								.appendTo("#listfile-table-body");
					} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
						console.log("auth error");
					} else {
						console.log("other error");
					}
				});
				
				if (group_info[0].data_type == 'create') {
					$("#sharegroup-listpage").hide();
					$("#sharegroup-contentpage").show();
					$("#sharegroup-content-invite").show();
					$("#sharegroup-content-join").hide();
					$("#sharegroup-content-share").show();
					$("#sharegroup-content-setting").show();
					$("#sharegroup-content-quit").hide();
					$("#sharegroup-content-name").text(
							"我创建的共享群 ：" + $(target).attr("data-name"));

				}

				if (group_info[0].data_type == 'join') {
					$("#sharegroup-listpage").hide();
					$("#sharegroup-contentpage").show();
					$("#sharegroup-content-invite").hide();
					$("#sharegroup-content-join").hide();
					$("#sharegroup-content-share").show();
					$("#sharegroup-content-setting").show();
					$("#sharegroup-content-quit").show();
					$("#sharegroup-content-name").text(
							"我加入的共享群 ：" + $(target).attr("data-name"));
				}

				if (group_info[0].data_type == 'others') {
					$("#sharegroup-listpage").hide();
					$("#sharegroup-contentpage").show();
					$("#sharegroup-content-invite").hide();
					$("#sharegroup-content-join").show();
					$("#sharegroup-content-share").hide();
					$("#sharegroup-content-setting").show();
					$("#sharegroup-content-quit").hide();
					$("#sharegroup-content-name").text(
							"其他共享群 ：" + $(target).attr("data-name"));
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

	exports.fnShowShareGroup = fnShowShareGroup;
	exports.fnLoadTabShareGroup = fnLoadTabShareGroup;
});