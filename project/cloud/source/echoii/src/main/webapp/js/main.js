define(function(require, exports) {

	var UTIL = require("module/util.js");
	var FILE = require("module/file.js");
	var TMPL = require("module/template.js");
	var MUSIC = require("module/music.js");
	var SHARE = require("module/share.js");
	var AUTH = require("module/auth.js");
	var STAT = require("module/stat.js");
	var ACTION = require("module/action.js");
    var SHAREGROUP = require("sharegroups_tab.js");
    var RIGHT = require("module/rightclickmenu.js");
    var UPLOAD = require("module/upload.js");
    var CENTER = require("center_tab.js");

	var _stat = STAT.getStat();

	$.ajaxSetup({
		contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
		cache : true
	});

	// //////////////////// Navigation //////////////////////////////
	$("#top-nav").click(function(e) {
		e.preventDefault();
		if (e.target.nodeName != "A") {
			return;
		}
		var target = $(e.target);
		// to panel page
		var tid = target.attr("data-target");
		if (tid == "home") {
			$("#main-pane-link-home").tab("show");
			_stat.navList = [];
			fnHideToolBar();
			return;

		} else if (tid == "pane-all") {
			fnShowMainPaneAll();
			$("#main-pane-link-all").tab("show");
			return;

		}

		// to folder page
		var len = _stat.navList.length;
		// search for position of folder id in the list
		var p = 0;
		var tmp;
		for ( var i = 0; i < len; i++) {
			tmp = _stat.navList[i];
			if (tmp.id == tid) {
				p = i;
				break;
			}
		}

		if (p <= 0 || p == len - 1) {
			// id not found in list or current folder is clicked, do nothing
			return;
		}

		_stat.navList = _stat.navList.slice(0, p);
		ACTION.fnNavIntoFolder(target.attr('data-name'), tid);
		_stat.currentFolderId = tid;
		fnReloadFolder();

	})





	// handle go back btn
	$("#back-btn").click(function() {
		var len = _stat.navList.length;
		// if is the last one, do nothing
		if (len <= 1) {
			return;
		}

		_stat.navList.pop();

		len--;

		var item = _stat.navList[len - 1];

		if (!item || item.id == "pane-all") {

			fnShowMainPaneAll();
			return;
		} else {
			_stat.currentFolderId = item.id;
		}

		fnReloadFolder();
		ACTION.fnUpdateTopNav();
	});

	// ////////////////////Listen to the browser //////////////////////////////
	function adjustWindowSize() {
       
		var fixed_height = $("#header-top-nav").height()
				+ $("#top-nav-wrapper").height()
				+ $("#toolbar-wrapper").height()
				+ $("#list-table-header").height() - 9;
		var height = $(window).height() - fixed_height;
      
		if (height < 470) {
			height = 470;
			$("#main_tab_pane").css("height", height);

		} else {
			$("#main_tab_pane").css("height", height);
		}

		var mainContentWidth = $("body").width() - 220;
		$("#main-content").css("width", mainContentWidth + "px");
		$("#main-cat-wrapper").css("height",  ($(window).height()- $("#header-top-nav").height())+ "px");
	}

	window.onresize = adjustWindowSize;
	$(document).ready(adjustWindowSize);

	// ////////////////show capacity ,num of picture ,doc,music
	// /////////////////////
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
			$("#tab_home_music_num").text(json.data.musicNumber);
			$("#tab_home_doc_num").text(json.data.docNumber);
			$("#tab_home_video_num").text(json.data.videoNumber);
			$("#tab_home_image_num").text(json.data.pictureNumber);
			$("#tab_home_others_num").text(json.data.othersNumber);

		} else {
			ACTION.fnLogout();
		}

	});

	// ///////////////////////////// view mode btns ///////////////////////////

	$("#list-view-btn").click(function() {
		$(this).removeClass("disabled");
		$("#thumb-view-btn").addClass("disabled");
		$(this).removeClass("view-btn-list");
		$(this).addClass("view-btn-list-visited");
		$("#thumb-view-btn").removeClass("view-btn-thumb-visited");
		$("#thumb-view-btn").addClass("view-btn-thumb");
		/*
		 * $("#" + _stat.currentTabId + "-thumb-wrapper").hide(); $("#" +
		 * _stat.currentTabId + "-list-wrapper").show();
		 */
		$("#main-pane-all-thumb-wrapper").hide();
		$("#main-pane-video-thumb-wrapper").hide();
		$("#main-pane-image-thumb-wrapper").hide();
		$("#main-pane-document-thumb-wrapper").hide();
		$("#main-pane-music-thumb-wrapper").hide();
		$("#main-pane-other-thumb-wrapper").hide();
		$("#main-pane-share-thumb-wrapper").hide();
		$("#main-pane-trash-thumb-wrapper").hide();
		$("#main-pane-all-list-wrapper").show();
		$("#main-pane-video-list-wrapper").show();
		$("#main-pane-image-list-wrapper").show();
		$("#main-pane-document-list-wrapper").show();
		$("#main-pane-music-list-wrapper").show();
		$("#main-pane-other-list-wrapper").show();
		$("#main-pane-share-list-wrapper").show();
		$("#main-pane-trash-list-wrapper").show();
		$(".list-view-col").show();
	});

	$("#thumb-view-btn").click(function() {
		$(this).removeClass("disabled");
		$("#list-view-btn").addClass("disabled");
		$(this).removeClass("view-btn-thumb");
		$(this).addClass("view-btn-thumb-visited");
		$("#list-view-btn").removeClass("view-btn-list-visited");
		$("#list-view-btn").addClass("view-btn-list");
		/*
		 * $("#" + _stat.currentTabId + "-thumb-wrapper").show(); $("#" +
		 * _stat.currentTabId + "-list-wrapper").hide();
		 */
		$("#main-pane-all-thumb-wrapper").show();
		$("#main-pane-video-thumb-wrapper").show();
		$("#main-pane-image-thumb-wrapper").show();
		$("#main-pane-music-thumb-wrapper").show();
		$("#main-pane-document-thumb-wrapper").show();
		$("#main-pane-other-thumb-wrapper").show();
		$("#main-pane-share-thumb-wrapper").show();
		$("#main-pane-trash-thumb-wrapper").show();
		$("#main-pane-all-list-wrapper").hide();
		$("#main-pane-video-list-wrapper").hide();
		$("#main-pane-image-list-wrapper").hide();
		$("#main-pane-music-list-wrapper").hide();
		$("#main-pane-document-list-wrapper").hide();
		$("#main-pane-other-list-wrapper").hide();
		$("#main-pane-share-list-wrapper").hide();
		$("#main-pane-trash-list-wrapper").hide();
		$(".list-view-col").hide();
	});

	// ///////////////////////////// create folder ///////////////////////////
	$("#create-folder-btn").click(function() {
		$("#modal-create-folder").modal('show');
	});

	$('#create-folder-submit').click(function(e) {
		var name = $("#create-folder-name").val();
		if (!UTIL.fnValidateFolderName(name)) {
			alert("name err");
			return;
		}

		var para = UTIL.fnGetDefaultPara();
		para.name = name;
		para.folder_id = _stat.currentFolderId;

		FILE.fnCreateFolder(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				fnReloadFolder();
				$("#modal-create-folder").modal('hide');
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				ACTION.fnlogout();
			} else {
				alert(LANG.creatfolder_error);
			}

		});

	});

	// ///////////////////////////// file list ///////////////////////////
	$("#tab-all-more-btn").click(function() {
		fnLoadTabAllFiles();
	});
	$("#tab-video-more-btn").click(function() {
		fnLoadTabVideo();
	});
	$("#tab-image-more-btn").click(function() {
		fnLoadTabImage();
	});
	$("#tab-music-more-btn").click(function() {
		fnLoadTabMusic();
	});
	$("#tab-doc-more-btn").click(function() {
		fnLoadTabDoc();
	});
	$("#tab-other-more-btn").click(function() {
		fnLoadTabOther();
	});
	$("#tab-share-more-btn").click(function() {
		ACTION.fnLoadTabShare();
	});
	$("#tab-trash-more-btn").click(function() {
		fnLoadTabTrash();
	});
	$("#tab-sharegroup-more-btn").click(function(){
		SHAREGROUP.fnLoadTabShareGroup();
	})

	$("#main_tab_pane").click(function(e) {
		var target = e.target;

		// if( target.nodeName == 'IMG' && target.attr( "data-type" ) ==
		// "img-icon" ){
		if (target.nodeName == 'IMG') {
			target = target.parentNode;
		}

		if (target.nodeName == 'A') {
			fnHandleFileLink(target);
			e.preventDefault();
		}

	});

	
	function fnHandleFileLink(target) {
		target = $(target);
		var para = UTIL.fnGetDefaultPara();
		para.file_id = target.attr("data-id");
		para.name = target.attr("data-name");
		para.src = target.attr("data-src");
		para.folder = target.attr("data-folder");
		para.meta_id = target.attr("data-meta-id");

		_stat.fileId = para.file_id;
		var dataType = target.attr("data-type");

		if (dataType == "folder") {
			ACTION.fnNavIntoFolder(para.name, _stat.fileId);
			fnReloadFolder(_stat.fileId);
		} else if (dataType == "act-download") {
			ACTION.fnDownloadFile(_stat.fileId);

		} else if (dataType == "act-rename") {
			$("#rename-modal-folderid").val(_stat.fileId);
			$("#sourceName").text(para.name);
			$("#modal-rename").modal('show');

		} else if (dataType == "act-remove") {
			var tbodyId = target.closest("tbody").attr("id");
			if (tbodyId == "main-pane-trash-list-body") {
				para.remove_source = "true";
			} else {
				para.remove_source = "false";
			}
			_stat.paraRemove = para ;
			$("#removeInfo").text(para.name);
			$("#modal-remove").modal('show');
			$("#remove-modal-confirmall").attr({
				"style" : "display: none;"
			});
			$("#remove-modal-confirm").attr({
				"style" : "display: inline-block;"
			});
			
		} else if (dataType == "act-recover") {
			_stat.paraRecover = UTIL.fnGetDefaultPara();
			_stat.paraRecover.file_id_list = para.file_id;
			$("#recoverInfo").text(para.name);
			
			$("#modal-recover").modal('show');
			$("#recover-modal-confirmall").attr({
				"style" : "display: none;"
			});
			$("#recover-modal-confirm").attr({
				"style" : "display: inline-block;"
			});

		} else if (dataType == "act-share") {
			ACTION.fnShowShareModal();

		} else if (dataType == "show-image") {
			fnShowImgModal(para);

		} else if (dataType == "play_music") {
			fnPlayMusic(para);

		} else if (dataType == 'flv' || dataType == 'mp4') {
			fnPlayVideo(para);

		} else if (dataType == 'share') {
			para.validCode = target.attr("data-code");
			fnShowShareModal_info(para);

		} else if (dataType == "act-share-download") {
			para.file_id = _stat.fileId;
			SHARE.shareInfo(para, function(json) {
				type = json.data.type;
				if (type == "passwd") {
					$("#modal-share-download").modal("show");
				} else {
					$("#sdf-file-id-nocode").val(json.data.id);
					$("#share-download-form-nocode").attr("action",
							UTIL.fnUrl("share_download"));
					$("#share-download-form-nocode").submit();
				}
			});
		} else if (dataType == "act-cancel-share") {
			SHARE.shareCancel(para, function(json) {
				if (json.status == UTIL.fnStatus("OK")) {
					alert(LANG.cansel_share_success);
				} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
					ACTION.fnlogout();
				} else {
					alert(LANG.cansel_share_fail);
				}
			});
			ACTION.fnShowShare();
		} else if (dataType == "act-update-idcode") {
			ACTION.fnShowShareModal_update(para);

		} else {
			ACTION.fnDownloadFile(_stat.fileId);

		} // if

	} // fnHandleFileLink

	var share_code;


	function fnShowShareModal_info(para) {
		$("#modal-share").modal('show');
		$("#modal-shareid-choice").hide();
		$("#modal-share-box").show();
		$("#modal-share-updatebox").hide();
		$("#share-modal-updatecode").hide();
		var location_host = window.location.host;
		var data = {
			file_id : para.file_id,
			idcode : para.validCode
		};
		var share_url = location_host + "/echoii/share.jsp?sourceid="
				+ data.file_id;
		$("#modal-share-link").val(share_url);

		SHARE.shareInfo(para, function(json) {
			if (json.data.type == "passwd") {
				$("#modal-share-idbox").show();
				$("#modal-share-id").val(json.data.validCode);
			} else {
				$("#modal-share-idbox").hide();
			}
		});
	}
	;


	function createRandNum() {
		var x = 1000;
		var y = 1;
		var rand = String.fromCharCode(Math.floor(Math.random() * 26)
				+ "a".charCodeAt(0))
				+ parseInt(Math.random() * (x - y + 1) + y)
				+ String.fromCharCode(Math.floor(Math.random() * 26)
						+ "A".charCodeAt(0));
		return rand;
	}

	$("input[name='shareid']").bind("click", function() {
		var shareid_choice = $('input:radio[name="shareid"]:checked').val();
		if (shareid_choice == "yes") {
			$("#modal-share_inputcodeid").show();
			$("#modal-share-createid").val(createRandNum());

		} else {
			$("#modal-share_inputcodeid").hide();
		}
	});

	$("#modal-share-butoon").click(
			function() {
				var para = UTIL.fnGetDefaultPara();
				para.file_id_list = _stat.fileId;

				/*
				 * $("#modal-shareid-choice").hide();
				 * $("#modal-share-box").show();
				 */

				var shareid_choice = $('input:radio[name="shareid"]:checked')
						.val();

				if (shareid_choice == "yes") {

					var idcode = $("#modal-share-createid").val();
					$("#modal-share-id").val(idcode);
					if (idcode == "") {
						$("#share-codeid-warn").text(LANG.share_codeid_warn);
					} else {
						$("#modal-shareid-choice").hide();
						$("#modal-share-box").show();
						$("#modal-share-idbox").show();
						para.idcode = idcode;
						var location_host = window.location.host;

						SHARE.sharefiles(para, function(json) {
							var share_url = location_host
									+ "/echoii/share.jsp?sourceid="
									+ json.data.id;
							$("#modal-share-link").val(share_url);
						});
					}
				} else {
					$("#modal-shareid-choice").hide();
					$("#modal-share-box").show();
					$("#modal-share-idbox").hide();
					var location_host = window.location.host;
					SHARE.sharefiles(para, function(json) {
						var share_url = location_host
								+ "/echoii/share.jsp?sourceid=" + json.data.id;
						$("#modal-share-link").val(share_url);
					});
				}

			});

	function fnPlayMusic(para) {
		var songName = para.name;
		var songUrl = para.src + "&user_id=" + para.user_id + "&token="
				+ para.token;

		$("#modal-music").modal('show');
		$("#songName").text(para.name);
		MUSIC.playMusic(songName, songUrl);
	}
	;

	function fnShowImgModal(para) {
		$("#modal-image-disp").attr("src", para.src);
		$("#modal-image").modal('show');
	}
	
	//recover modal
	$("#recover-cancel").click(function(){
		$("#modal-recover").modal("hide");
	});
	$("#recover-modal-confirmall").click(function(){
		fnRecoverFile(_stat.paraRecoverall);
	});
	$("#recover-modal-confirm").click(function(){
		fnRecoverFile(_stat.paraRecover);
	});


	function fnRecoverFile(para) {
		FILE.fnRecover(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				if (_stat.findPaneId == "main-pane-all") {
					fnShowMainPaneAll();
				}
				if (_stat.findPaneId == "main-pane-video") {
					fnShowVideo();
				}
				if (_stat.findPaneId == "main-pane-image") {
					fnShowImage();
				}
				if (_stat.findPaneId == "main-pane-music") {
					fnShowMusic();
				}
				if (_stat.findPaneId == "main-pane-document") {
					fnShowDocument();
				}
				if (_stat.findPaneId == "main-pane-others") {
					fnShowOthers();
				}
				if (_stat.findPaneId == "main-pane-share") {
					ACTION.fnShowShare();
				}
				if (_stat.findPaneId == "main-pane-trash") {
					fnShowTrash();
				}
//				SHAREGROUP.fnShowShareGroup();
				$('#checkAll').attr("checked", false);
				_stat.arrCheckboxDataId = [];
			} else if( json.status == UTIL.fnStatus("AUTH_ERROR")){
				ACTION.fnlogout();
			}else {
				alert(LANG.recover_wrong);
			}
		});
		$("#modal-recover").modal("hide");
	}
	
	//remove modal
	$("#remove-cancel").click(function(){
		$("#modal-remove").modal("hide");
	});
	$("#remove-modal-confirm").click(function(){
		fnRemoveFile(_stat.paraRemove);
	});
	
	function fnRemoveFile(para) {
			FILE.fnDel(para, function(json) {
				if (json.status == UTIL.fnStatus("OK")) {
					if (_stat.findPaneId == "main-pane-all") {
						fnShowMainPaneAll();
					}
					if (_stat.findPaneId == "main-pane-video") {
						fnShowVideo();
					}
					if (_stat.findPaneId == "main-pane-image") {
						fnShowImage();
					}
					if (_stat.findPaneId == "main-pane-music") {
						fnShowMusic();
					}
					if (_stat.findPaneId == "main-pane-document") {
						fnShowDocument();
					}
					if (_stat.findPaneId == "main-pane-others") {
						fnShowOthers();
					}
					if (_stat.findPaneId == "main-pane-share") {
						ACTION.fnShowShare();
					}
					if (_stat.findPaneId == "main-pane-trash") {
						fnShowTrash();
					}
					//SHAREGROUP.fnShowShareGroup();
					$('#checkAll').attr("checked", false);
				} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
					ACTION.fnlogout();
				} else {
					alert(LANG.remove_wrong);
				}
			});
			$("#modal-remove").modal("hide");
	}

	// batch recover file
	$("#recover-btn").click(function() {
		var file_id_list = _stat.arrCheckboxDataId[0];
		var para = UTIL.fnGetDefaultPara();
		for ( var i = 1; i < _stat.arrCheckboxDataId.length; i++) {
			file_id_list = file_id_list + "," + _stat.arrCheckboxDataId[i];
		}
		para.file_id_list = file_id_list;
		_stat.paraRecoverall = para;
		$("#recoverInfo").text("共 "+_stat.arrCheckboxDataId.length+" 个");
		$("#modal-recover").modal('show');
		$("#recover-modal-confirm").attr({
			"style" : "display: none;"
		});
		$("#recover-modal-confirmall").attr({
			"style" : "display: inline-block;"
		});
		
	});
	
	// batch remove file
	$("#delete-btn").click(function() {
		var file_id_list = _stat.arrCheckboxDataId[0];
		var para = UTIL.fnGetDefaultPara();
		for ( var i = 1; i < _stat.arrCheckboxDataId.length; i++) {
			file_id_list = file_id_list + "," + _stat.arrCheckboxDataId[i];
		}
		para.file_id_list = file_id_list;
		if (_stat.findPaneId == "main-pane-trash") {
			para.remove_source = "true";
		} else {
			para.remove_source = "false";
		}
		_stat.paraRemoveall = para ;
		$("#removeInfo").text("共 "+_stat.arrCheckboxDataId.length+" 个");
		$("#modal-remove").modal('show');
		$("#remove-modal-confirm").attr({
			"style" : "display: none;"
		});
		$("#remove-modal-confirmall").attr({
			"style" : "display: inline-block;"
		});
	});

	//remove modal
	$("#remove-modal-confirmall").click(function(){
		fnPackageRemoveFile(_stat.paraRemoveall);
	});
	
	function fnPackageRemoveFile(para) {
			FILE.fnPackDel(para, function(json) {
				if (json.status == UTIL.fnStatus("OK")) {
					if (_stat.findPaneId == "main-pane-all") {
						fnShowMainPaneAll();
					}
					if (_stat.findPaneId == "main-pane-video") {
						fnShowVideo();
					}
					if (_stat.findPaneId == "main-pane-image") {
						fnShowImage();
					}
					if (_stat.findPaneId == "main-pane-music") {
						fnShowMusic();
					}
					if (_stat.findPaneId == "main-pane-document") {
						fnShowDocument();
					}
					if (_stat.findPaneId == "main-pane-others") {
						fnShowOthers();
					}
					if (_stat.findPaneId == "main-pane-share") {
						ACTION.fnShowShare();
					}
					if (_stat.findPaneId == "main-pane-trash") {
						fnShowTrash();
					}
					//SHAREGROUP.fnShowShareGroup();
					$('#checkAll').attr("checked", false);
					_stat.arrCheckboxDataId = [];
				} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
					ACTION.fnlogout();
				} else {
					alert(LANG.remove_wrong);
				}
			});
			$("#modal-remove").modal("hide");
	}

	function fnReloadFolder(folderId) {
		if (!folderId) {
			folderId = _stat.currentFolderId;
		}
		// into folder
		$("#main-pane-all-list-body").empty();
		$("#main-pane-all-thumb-wrapper").empty();
		_stat.currentFolderId = folderId;
		_stat.tabAllFileIndex = 0;

		fnLoadTabAllFiles(folderId);
	}



	function fnPackageDownload(fileIdList) {
		if (!fileIdList) {
			return;
		}
		var cookies = UTIL.fnGetCookies();

		$("#pf-file-id-list").val(fileIdList);
		$("#pf-user-id").val(cookies.user_id);
		$("#pf-token").val(cookies.token);
		$("#packdownload-form").attr("action", UTIL.fnUrl("package_download"));
		$("#packdownload-form").submit();
	}

	function fnPlayVideo(para) {

		var fileUrl = UTIL.fnUrl('videoServer') + para.folder + "/"
				+ para.meta_id;
		var imgUrl = UTIL.fnUrl('imageServer') + "?id=" + para.folder + "/"
				+ para.meta_id + "&type=mov&size=normal";
		jwplayer("video-player").setup({
			file : fileUrl,
			width : '100%',
			aspectratio : '16:9',
			startparam : 'start',
			image: imgUrl,
			title: para.name
		});
		$("#modal-video").modal("show");
	}

	// /////////////////////////////// tool bar ///////////////////////////
	function fnShowToolBar() {
		$("#top-nav-wrapper").show();
		$("#toolbar-wrapper").show();
		$("#list-table-header").show();

	}

	function fnHideToolBar() {
		$("#top-nav-wrapper").hide();
		$("#toolbar-wrapper").hide();
		$("#list-table-header").hide();

	}

	
	// require("./module/uploader.js");
	$('#main-cat-nav a').click(function(e) {
		var targetId = e.target.id;

		if (targetId == "main-pane-link-all") {
			fnShowMainPaneAll();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-all";
		}

		if (targetId == "main-pane-link-video") {
			fnShowVideo();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-video";
		}

		if (targetId == "main-pane-link-image") {
			fnShowImage();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-image";
		}

		if (targetId == "main-pane-link-music") {
			fnShowMusic();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-music";

		}
		if (targetId == "main-pane-link-document") {
			fnShowDocument();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-document";

		}

		if (targetId == "main-pane-link-others") {
			fnShowOthers();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-others";
		}

		if (targetId == "main-pane-link-share") {
			ACTION.fnShowShare();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-share";
		}

		if (targetId == "main-pane-link-safebox") {
			// fnShowSafebox();
		}

		if (targetId == "main-pane-link-trash") {
			fnShowTrash();
			fnShowToolBar();
			_stat.findPaneId = "main-pane-trash";
		}
		
		if (targetId == "main-pane-link-center") {
			fnHideToolBar();
			CENTER.fnShowCenter();
			_stat.findPaneId = "main-pane-center";
		}

		if (targetId == "main-pane-link-sharegroups") {
			fnHideToolBar();
			SHAREGROUP.fnShowShareGroup();
			_stat.findPaneId = "main-pane-sharegroups";
		}

		if (targetId == "main-pane-link-hotres") {
			// fnShowHotRes();
		}

		e.preventDefault();
		$(this).tab('show');
	});

	// all show panes function
	function fnShowMainPaneAll() {
		_stat.navList = [];
		_stat.tabAllFileIndex = 0;
		_stat.currentTabId = "main-pane-all";
		_stat.currentFolderId = 'root';

		ACTION.fnNavIntoFolder(LANG.pane_name_all, "pane-all");

		$("#main-pane-all-list-body").empty();
		$("#main-pane-all-thumb-wrapper").empty();

		fnLoadTabAllFiles("root");

	}

	function fnLoadTabAllFiles(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabAllFileIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		} else {
			para.folder_id = _stat.currentFolderId;
		}

		FILE.fnListAll(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabAllFileIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnFileList(), sorted).appendTo(
					"#main-pane-all-list-body");
			$.tmpl(TMPL.fnFileThumb(), sorted).appendTo(
					"#main-pane-all-thumb-wrapper");
		});
	}

	function fnShowVideo() {
		_stat.tabVideoIndex = 0;
		_stat.currentTabId = "main-pane-video";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_video, "pane-all-video");

		$("#main-pane-video-list-body").empty();
		$("#main-pane-video-thumb-wrapper").empty();
		fnLoadTabVideo();
	}

	function fnLoadTabVideo(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabVideoIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		// FILE.fnListAll(para, function(json) {
		FILE.fnListVideo(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabVideoIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnVideoList(), sorted).appendTo(
					"#main-pane-video-list-body");
			$.tmpl(TMPL.fnVideoThumb(), sorted).appendTo(
					"#main-pane-video-thumb-wrapper");
		});

	}

	function fnShowImage() {
		_stat.tabImageIndex = 0;
		_stat.currentTabId = "main-pane-image";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_image, "pane-all-image");

		$("#main-pane-image-list-body").empty();
		$("#main-pane-image-thumb-wrapper").empty();
		fnLoadTabImage();
	}

	function fnLoadTabImage(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabImageIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		FILE.fnListImage(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabImageIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnImgList(), sorted).appendTo(
					"#main-pane-image-list-body");
			$.tmpl(TMPL.fnImgThumb(), sorted).appendTo(
					"#main-pane-image-thumb-wrapper");
		});
	}

	function fnShowMusic() {
		_stat.tabMusicIndex = 0;
		_stat.currentTabId = "main-pane-music";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_music, "pane-all-music");

		$("#main-pane-music-list-body").empty();
		$("#main-pane-music-thumb-wrapper").empty();
		fnLoadTabMusic();
	}

	function fnLoadTabMusic(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabMusicIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		// FILE.fnListAll(para, function(json) {
		FILE.fnListMusic(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabMusicIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnMusicList(), sorted).appendTo(
					"#main-pane-music-list-body");
			$.tmpl(TMPL.fnMusicThumb(), sorted).appendTo(
					"#main-pane-music-thumb-wrapper");
		});
	}

	function fnShowDocument() {
		_stat.tabDocIndex = 0;
		_stat.currentTabId = "main-pane-document";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_doc, "pane-all-doc");
		$("#main-pane-document-list-body").empty();
		$("#main-pane-document-thumb-wrapper").empty();
		fnLoadTabDoc();
	}

	function fnLoadTabDoc(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabDocIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		FILE.fnListDoc(para, function(json) {
			// FILE.fnListAll(para, function(json) {

			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabDocIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnFileList(), sorted).appendTo(
					"#main-pane-document-list-body");
			$.tmpl(TMPL.fnFileThumb(), sorted).appendTo(
					"#main-pane-document-thumb-wrapper");
		});
	}

	function fnShowOthers() {
		_stat.tabOthersIndex = 0;
		_stat.currentTabId = "main-pane-other";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_other, "pane-all-other");

		$("#main-pane-other-list-body").empty();
		$("#main-pane-other-thumb-wrapper").empty();
		fnLoadTabOther();
	}

	function fnLoadTabOther(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabOthersIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		FILE.fnListOther(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabOthersIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnFileList(), sorted).appendTo(
					"#main-pane-other-list-body");
			$.tmpl(TMPL.fnFileThumb(), sorted).appendTo(
					"#main-pane-other-thumb-wrapper");
		});
	}




	function fnShowTrash() {
		_stat.tabTrashIndex = 0;
		_stat.currentTabId = "main-pane-trash";
		_stat.navList = [];
		ACTION.fnNavIntoFolder(LANG.pane_name_trash, "pane-all-trash");

		$("#main-pane-trash-list-body").empty();
		$("#main-pane-trash-thumb-wrapper").empty();
		fnLoadTabTrash();
	}

	$("#main-cat-nav a[id!='main-pane-link-all']").click(function() {
		$("#top-nav-wrapper").show();
		$("#back-btn").hide();
		$("#upload-btn").hide();
		$("#create-folder-btn").hide();
	});

	$("#main-pane-link-all").click(function() {
		$("#top-nav-wrapper").show();
		$("#back-btn").show();
		$("#upload-btn").show();
		$("#create-folder-btn").show();
	});

	function fnLoadTabTrash(folderId) {

		var para = UTIL.fnGetDefaultPara();
		para.begin = _stat.tabTrashIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		if (folderId) {
			para.folder_id = folderId;
		}

		// FILE.fnListAll(para, function(json) {
		FILE.fnListTrash(para, function(json) {
			if (json.data.length > 30) {
				$("[id$='-more-btn']").attr({
					"style" : "display: inline-block;"
				});
			}
			_stat.tabTrashIndex += json.data.length;
			var sorted = FILE.fnSortFolderAhead(json.data);

			$.tmpl(TMPL.fnTrashList(), sorted).appendTo(
					"#main-pane-trash-list-body");
			$.tmpl(TMPL.fnTrashThumb(), sorted).appendTo(
					"#main-pane-trash-thumb-wrapper");
		});
	}



	// select all input checkbox
	var cbAll = document.getElementById("checkAll");
	var arrSon = [];
	cbAll.onclick = function() {
		var tempState = cbAll.checked;
		arrSon = $('#' + _stat.findPaneId + ' input[type=checkbox]');
		for ( var i = 0; i < arrSon.length; i++) {
			if (arrSon[i].checked != tempState)
				arrSon[i].click();
		}
	};

	// get the input checkbox id
	$("#main_tab_pane").click(function(e) {
		var target = e.target;
		if (target.nodeName == 'INPUT') {
			var checkboxId = $(target).attr("id");
			ACTION.clickInput(checkboxId);
		}
	});
	
	// console show the download data-id
	$("#download-btn").click(function() {
		var file_id_list = _stat.arrCheckboxDataId[0];
		for ( var i = 1; i < _stat.arrCheckboxDataId.length; i++) {
			file_id_list = file_id_list + "," + _stat.arrCheckboxDataId[i];
		}
		fnPackageDownload(file_id_list);
	});

	// console show the share data-id
	$("#share-btn").click(function() {
		var para = UTIL.fnGetDefaultPara();
		var file_id_list = _stat.arrCheckboxDataId[0];
		for ( var i = 1; i < _stat.arrCheckboxDataId.length; i++) {
			file_id_list = file_id_list + "," + _stat.arrCheckboxDataId[i];
		}
		_stat.fileId = file_id_list;
		ACTION.fnShowShareModal();
	});

	// clear the _stat.arrCheckboxDataId[]&checkAll and hide the download-btn and share-btn
	// and -more-btn
	$("#main-cat-nav li").click(function() {
		_stat.arrCheckboxDataId = [];
		$('#checkAll').attr("checked", false);
		arrSon = [];
		$("#download-btn,#share-btn,#delete-btn,#recover-btn").attr({
			"style" : "display: none;"
		});
		$("[id$='-more-btn']").attr({
			"style" : "display: none;"
		});
	});



	// main.jsp logout
	$("#header-logout").click(function() {
		ACTION.fnLogout();
	});

	// btn-return return to folder folder
	$("#main-btn-return").click(function() {
		$("#main-pane-first").show();
		$("#main-btn-toolbar").hide();
		$("#main_tab_pane").hide();
	});

	// file show btn
	$("#main-icon-th-list").click(function() {

		$("#tab_folder_list").show();
		$("#tab_folder_view").hide();
	});

	$("#main-icon-th").click(function() {

		$("#tab_folder_list").hide();
		$("#tab_folder_view").show();
	});

	// file sort btn
	$('#main-listbar-sort h2 a').mouseover(function() {
		$('#main-listbar-sort-ul').show();
		$('#main-listbar-sort h2 a').hide();
		// $('#loginDoor').addClass("textbox-border");
		// $('#registDoor').removeClass("textbox-border");
	});

	// rename modal opertion
	$("#rename-modal-save").click(function() {

		var para = UTIL.fnGetDefaultPara();
		para.file_id = $("#rename-modal-folderid").val();
		para.new_name = $("#rename-fold-name").val();

		FILE.fnRename(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				alert(LANG.rename_success);
				if (_stat.findPaneId == "main-pane-all") {
					fnShowMainPaneAll();
				}
				if (_stat.findPaneId == "main-pane-video") {
					fnShowVideo();
				}
				if (_stat.findPaneId == "main-pane-image") {
					fnShowImage();
				}
				if (_stat.findPaneId == "main-pane-music") {
					fnShowMusic();
				}
				if (_stat.findPaneId == "main-pane-document") {
					fnShowDocument();
				}
				if (_stat.findPaneId == "main-pane-others") {
					fnShowOthers();
				}
				if (_stat.findPaneId == "main-pane-share") {
					ACTION.fnShowShare();
				}
				if (_stat.findPaneId == "main-pane-trash") {
					fnShowTrash();
				}
				$("#modal-rename").modal('hide');
			} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
				ACTION.fnlogout();
			} else {
				alert(LANG.rename_wrong);
			}
		});
	});

	// share modal update idcode operation
	$("#share-modal-updatecode").click(function() {
		var para = UTIL.fnGetDefaultPara();
		para.file_id = _stat.fileId;
		para.idcode = $("#modal-share-updateid").val();
		$("#share-updatecodeid-warn").text("");
		if (para.idcode == "") {
			$("#share-updatecodeid-warn").text(LANG.share_codeid_warn);
		} else {
			SHARE.shareUpdateCode(para, function(json) {
				if (json.status == UTIL.fnStatus("OK")) {
					alert(LANG.updateCode_success);
				} else if (json.status == UTIL.fnStatus("AUTH_ERROR")) {
					ACTION.fnlogout();
				} else {
					alert(LANG.updateCode_wrong);
				}
			});
		}
		$("#modal-share").modal('hide');
		ACTION.fnShowShare();
	});

	// share modal download code operation
	$("#share-download-submit").click(
			function() {
				var code = $("#share-download-code").val();
				if (code == "") {
					$("#share-download-code-error").text(
							LANG.validate_code + LANG.can_not_empty);
				} else {

					$("#sdf-file-id").val(_stat.fileId);
					$("#sdf-idcode").val(code);
					$("#share-download-form").attr("action",
							UTIL.fnUrl("share_download"));
					$("#share-download-form").submit();
					$("#modal-share-download").modal("hide");
				}
			});

	// uploadfile and modal operation
	$("#main-btn-upload").click(function() {
		$("#main-uploadfile").trigger('click');
	});

	// redirect to userinfo.jsp
	$("#header-userinfo").click(function() {

		UTIL.fnRedirect("userinfo.jsp", location.href);
	});

	// return to main.jsp home-page
	$("#header-top-image").click(function() {

		UTIL.fnRedirect("main.jsp", location.href);
	});

	// pause music
	$("#modal-music-button").click(function() {
		MUSIC.pauseMusic();
	});

	$('#modal-image').on('hidden', function() {
		$("#modal-image-disp").attr({
			"style" : "visibility:hidden;"
		});
		$("#loaderimage").attr({
			"style" : "display: block;"
		});
	});

	// tab_home link
	$("#main-pane-home-image").click(function() {
		$("#main-pane-link-image").trigger('click');
	});
	$("#main-pane-home-music").click(function() {
		$("#main-pane-link-music").trigger('click');
	});
	$("#main-pane-home-video").click(function() {
		$("#main-pane-link-video").trigger('click');
	});
	$("#main-pane-home-document").click(function() {
		$("#main-pane-link-document").trigger('click');
	});
	$("#main-pane-home-others").click(function() {
		$("#main-pane-link-others").trigger('click');
	});
	
	//click to order
	$("#list-table-header .name").click(function() {
		if(_stat.order == "desc" && _stat.order_by == "name"){
			_stat.order = "asc";
			$("#list-table-header .name .orderImg").addClass("asc");
			$("#list-table-header .orderImg").removeClass("desc");
			$("#list-table-header th[class!='name'] .orderImg").removeClass("asc");
		}else{
			_stat.order = "desc";
			$("#list-table-header .name .orderImg").addClass("desc");
			$("#list-table-header th[class!='name'] .orderImg").removeClass("desc");
			$("#list-table-header .orderImg").removeClass("asc");
		}
		_stat.order_by = "name";
		if (_stat.findPaneId == "main-pane-all") {
//			fnShowMainPaneAll();
			//操作后保持在当前目录
			var para = UTIL.fnGetDefaultPara();
			UPLOAD.fnReloadUploadFile(para);
		}
		if (_stat.findPaneId == "main-pane-video") {
			fnShowVideo();
		}
		if (_stat.findPaneId == "main-pane-image") {
			fnShowImage();
		}
		if (_stat.findPaneId == "main-pane-music") {
			fnShowMusic();
		}
		if (_stat.findPaneId == "main-pane-document") {
			fnShowDocument();
		}
		if (_stat.findPaneId == "main-pane-others") {
			fnShowOthers();
		}
		if (_stat.findPaneId == "main-pane-share") {
			ACTION.fnShowShare();
		}
		if (_stat.findPaneId == "main-pane-trash") {
			fnShowTrash();
		}
		$('#checkAll').attr("checked", false);//clear the checkbox state
		_stat.arrCheckboxDataId = [];//clear the batch button state
		$("#download-btn,#share-btn,#delete-btn").attr({
			"style" : "display: none;"
		});
	});
	$("#list-table-header .size").click(function() {
		if(_stat.order == "desc" && _stat.order_by == "size"){
			_stat.order = "asc";
			$("#list-table-header .size .orderImg").addClass("asc");
			$("#list-table-header .orderImg").removeClass("desc");
			$("#list-table-header th[class!='size'] .orderImg").removeClass("asc");
		}else{
			_stat.order = "desc";
			$("#list-table-header .size .orderImg").addClass("desc");
			$("#list-table-header th[class!='size'] .orderImg").removeClass("desc");
			$("#list-table-header .orderImg").removeClass("asc");
		}
		_stat.order_by = "size";
		if (_stat.findPaneId == "main-pane-all") {
//			fnShowMainPaneAll();
			//操作后保持在当前目录
			var para = UTIL.fnGetDefaultPara();
			UPLOAD.fnReloadUploadFile(para);
		}
		if (_stat.findPaneId == "main-pane-video") {
			fnShowVideo();
		}
		if (_stat.findPaneId == "main-pane-image") {
			fnShowImage();
		}
		if (_stat.findPaneId == "main-pane-music") {
			fnShowMusic();
		}
		if (_stat.findPaneId == "main-pane-document") {
			fnShowDocument();
		}
		if (_stat.findPaneId == "main-pane-others") {
			fnShowOthers();
		}
		if (_stat.findPaneId == "main-pane-share") {
			ACTION.fnShowShare();
		}
		if (_stat.findPaneId == "main-pane-trash") {
			fnShowTrash();
		}
		$('#checkAll').attr("checked", false);
		_stat.arrCheckboxDataId = [];
		$("#download-btn,#share-btn,#delete-btn").attr({
			"style" : "display: none;"
		});
	});
	$("#list-table-header .date").click(function() {
		if(_stat.order == "desc" && _stat.order_by == "lmf_date"){
			_stat.order = "asc";
			$("#list-table-header .date .orderImg").addClass("asc");
			$("#list-table-header .orderImg").removeClass("desc");
			$("#list-table-header th[class!='date'] .orderImg").removeClass("asc");
		}else{
			_stat.order = "desc";
			$("#list-table-header .date .orderImg").addClass("desc");
			$("#list-table-header th[class!='date'] .orderImg").removeClass("desc");
			$("#list-table-header .orderImg").removeClass("asc");
		}
		_stat.order_by = "lmf_date";
		if (_stat.findPaneId == "main-pane-all") {
//			fnShowMainPaneAll();
			//操作后保持在当前目录
			var para = UTIL.fnGetDefaultPara();
			UPLOAD.fnReloadUploadFile(para);
		}
		if (_stat.findPaneId == "main-pane-video") {
			fnShowVideo();
		}
		if (_stat.findPaneId == "main-pane-image") {
			fnShowImage();
		}
		if (_stat.findPaneId == "main-pane-music") {
			fnShowMusic();
		}
		if (_stat.findPaneId == "main-pane-document") {
			fnShowDocument();
		}
		if (_stat.findPaneId == "main-pane-others") {
			fnShowOthers();
		}
		if (_stat.findPaneId == "main-pane-share") {
			ACTION.fnShowShare();
		}
		if (_stat.findPaneId == "main-pane-trash") {
			fnShowTrash();
		}
		$('#checkAll').attr("checked", false);
		_stat.arrCheckboxDataId = [];
		$("#download-btn,#share-btn,#delete-btn").attr({
			"style" : "display: none;"
		});
	});



 	window.onload = function(){
 		//upload
 		UPLOAD.fnUpload();
 		
 		///////////////////////rightbarmenu/////////////////////////////////////////
	    RIGHT.rightmenu('main_tab_pane','rightbarmenu');
	    document.body.onclick =RIGHT.f_hidden;
	}

});