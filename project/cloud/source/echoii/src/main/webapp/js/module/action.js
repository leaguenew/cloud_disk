define(function(require, exports) {

	var TMPL = require("./template.js");
	var UTIL = require("./util.js");
	var FILE = require("./file.js");
	var SHARE = require("./share.js");
	var STAT = require("./stat.js");
	var AUTH = require("./auth.js");
	var _stat=STAT.getStat();
    
	function fnShowCenter(){
		
	}
	;
    
	function fnShowShareModal() {
		$("#modal-share").modal('show');
		$("#modal-shareid-choice").show();
		$("#modal-share-box").hide();
		$("#modal-share_inputcodeid").hide();
		$("#modal-share-updatebox").hide();
		$("#share-modal-updatecode").hide();
	}
	;
	function fnDownloadFile(fileId) {
		if (!fileId) {
			return;
		}
		var cookies = UTIL.fnGetCookies();
		$("#df-file-id").val(fileId);
		$("#df-user-id").val(cookies.user_id);
		$("#df-token").val(cookies.token);
		$("#df-err-cb").val(LANG.download_wrong);
		$("#download-form").attr("action", UTIL.fnUrl("download"));
		$("#download-form").submit();
	}
	;
	function fnLogout() {
		var para = UTIL.fnGetDefaultPara();
		AUTH.fnLogout(para.user_id, para.token);
		UTIL.fnCookie("token", "");
		UTIL.fnCookie("uid", "");
		UTIL.fnCookie("email", "");
		UTIL.fnRedirect("homepage.html", location.href);
	}
	;
	function fnShowShare() {
		_stat.tabOthersIndex = 0;
		_stat.currentTabId = "main-pane-share";
		_stat.navList = [];
		fnNavIntoFolder(LANG.pane_name_share, "pane-all-share");
		$("#main-pane-share-list-body").empty();
		$("#main-pane-share-thumb-wrapper").empty();
		fnLoadTabShare();

	}
	;
	function fnNavIntoFolder(name, id) {
		_stat.navList.push({
			'name' : name,
			'id' : id
		});

		fnUpdateTopNav();
	}
	;
	function fnUpdateTopNav() {
		$("#top-nav").empty();
		$("#top-nav").append(
				"<li><a id='#nav-home' data-target='home' href='main.jsp'>"
						+ LANG.pane_name_home
						+ "</a> <span class='divider'>/</span></li>");
		$.tmpl(TMPL.fnTopNav(), _stat.navList).appendTo("#top-nav");
	}
	;
	function fnLoadTabShare(folderId) {
		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabOthersIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		SHARE.sharelist(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabOthersIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnShareList(), sorted).appendTo(
					"#main-pane-share-list-body");
			$.tmpl(TMPL.fnShareThumb(), sorted).appendTo(
					"#main-pane-share-thumb-wrapper");
		});
	}
	;
	function fnShowShareModal_update(para) {
		$("#modal-share").modal('show');
		$("#modal-shareid-choice").hide();
		$("#modal-share-box").show();
		$("#modal-share-idbox").hide();
		$("#modal-share-updatebox").show();
		$("#share-modal-updatecode").show();
		var location_host = window.location.host;
		var data = {
			file_id : para.file_id
		};
		var share_url = location_host + "/echoii/share.jsp?sourceid="
				+ data.file_id;
		$("#modal-share-link").val(share_url);
	}
	;
	
	function fnClickInput(checkboxId) {
		var getCheckboxId = $('#'+_stat.findPaneId).find('input[id=' + checkboxId + ']');
		var targetdata = getCheckboxId[0];
		var dataId = getCheckboxId.attr("data-id");
		if (targetdata.checked) {
			fnSelectFile(dataId);
		} else {
			fnUnSelectFile(dataId);
			}
	}
	
	function fnSelectFile(dataId) {
		var samebox = $('#'+_stat.findPaneId).find('input[data-id=' + dataId + ']');
		var targetdata = samebox[0];
		_stat.arrCheckboxDataId.push(dataId);
		if (_stat.arrCheckboxDataId.length >= 2) {
			if (_stat.findPaneId != "main-pane-trash") {
					$("#download-btn,#share-btn,#delete-btn").attr({
						"style" : "display: inline-block;"
					});
			} else if (_stat.findPaneId == "main-pane-trash") {
					$("#delete-btn,#recover-btn").attr({
						"style" : "display: inline-block;"
					});
			}
		}
		if ($(targetdata).attr("data-type") == "folder") {
			if (_stat.findPaneId != "main-pane-trash") {
					$("#download-btn,#share-btn,#delete-btn").attr({
						"style" : "display: inline-block;"
					});
			} else if (_stat.findPaneId == "main-pane-trash") {
					$("#delete-btn,#recover-btn").attr({
						"style" : "display: inline-block;"
					});
			}
		}
		samebox.each(function() {
			$(this).prop("checked", true);
			$(this).parent().parent().addClass("bgcolor");
		});
	}
	
	function fnUnSelectFile(dataId) {
		var samebox = $('#'+_stat.findPaneId).find('input[data-id=' + dataId + ']');
		for (var j = 0; j < _stat.arrCheckboxDataId.length; ++j) {
			if (dataId == _stat.arrCheckboxDataId[j]) {
				_stat.arrCheckboxDataId.splice(j, 1);
				break;
			}
		}
		if (_stat.arrCheckboxDataId.length < 2) {
			if ($('input[data-id='+ _stat.arrCheckboxDataId[0]+ ']').attr("data-type") == "folder") {
				if (_stat.findPaneId != "main-pane-trash") {
						$("#download-btn,#share-btn,#delete-btn").attr({
							"style" : "display: inline-block;"
						});	
				} else if (_stat.findPaneId == "main-pane-trash") {
						$("#delete-btn,#recover-btn").attr({
							"style" : "display: inline-block;"
						});
				}
			} else {
				$("#download-btn,#share-btn,#delete-btn,#recover-btn").attr({
					"style" : "display: none;"
				});
			}
		}
		samebox.each(function() {
			$(this).prop("checked", false);
			$(this).parent().parent().removeClass("bgcolor");
		});
	}
	
	exports.fnDownloadFile=fnDownloadFile;
	exports.fnShowShareModal=fnShowShareModal;
	exports.fnLogout=fnLogout;
	exports.fnShowShare=fnShowShare;
	exports.fnShowShareModal_update=fnShowShareModal_update;
	
	exports.fnNavIntoFolder=fnNavIntoFolder;
	exports.fnUpdateTopNav=fnUpdateTopNav;
	exports.fnLoadTabShare=fnLoadTabShare;
	exports.clickInput=fnClickInput;
	exports.selectFile=fnSelectFile;
	exports.unSelectFile=fnUnSelectFile;
});