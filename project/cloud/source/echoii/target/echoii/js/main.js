define(function(require, exports) {

	// var AUTH = require("./module/auth.js");
	var UTIL = require("./module/util.js");
	var FILE = require("./module/file.js");
	//require("./module/uploader.js");
	
	//link return to tabmain
	$("#mian-tab-link-return").click(function(){
		$('#main-tab-link-return').text("");
		$('#mann-tab-link-show').text("首页");
		$("#main_tab_pane").hide();
		$("#main-btn-toolbar").hide();
		$("#main-pane-first").show();
	});
	
	
	//main nav list , show different pan
	$('#main-cat-nav a').click(function(e) {
		var targetId = e.target.id;

		if (targetId == "main-pane-link-all") {
			fnShowMainPaneAll();
			$('#mian-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("全部文件");
			$("#main_tab_pane ").show();
			$("#main-btn-toolbar").show();
			$("#main-pane-first").hide();

		}

		if (targetId == "main-pane-link-video") {
			fnShowVideo();
			$('#main-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("视频");
			$("#main_tab_pane").show();
			$("#main-btn-toolbar").show();
			$("#main-pane-first").hide();
		}

		if (targetId == "main-pane-link-image") {
			fnShowImage();
			$('#main-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("图片");
			$("#main_tab_pane ").show();
			$("#main-btn-toolbar").show();
			$("#main-pane-first").hide();

		}

		if (targetId == "main-pane-link-music") {
			fnShowMusic();
			$('#main-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("音乐");
			$("#main_tab_pane").show();
			$("#main-btn-toolbar").show();
			$("#main-pane-first").hide();

		}
		if (targetId == "main-pane-link-document") {
			fnShowDocument();
			$('#main-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("文档");
			$("#main_tab_pane").show();
			$("#main-btn-toolbar").show();
			$("#main-pane-first").hide();

		}

		if (targetId == "main-pane-link-others") {
			fnShowOthers();
			$('#mian-tab-link-return').text("返回首页");
			$('#mian-tab-link-show').text("其他");
			$("#main_tab_pane").show();
			$("#main-pane-first").hide();
		}

		if (targetId == "main-pane-link-center") {
			fnShowCenter();
			$('#main-tab-link-return').text("返回首页");
			$('#main-tab-link-show').text("家庭数据中心");
			$("#main_tab_pane").show();
			$("#main-pane-first").hide();
		}

		if (targetId == "main-pane-link-safebox") {
			fnShowSafebox();
		}

		if (targetId == "main-pane-link-trash") {
			fnShowTrash();
		}

		if (targetId == "main-pane-link-sharegroup") {
			fnShowShareGroup();
		}

		if (targetId == "main-pane-link-hotres") {
			fnShowHotRes();
		}

		e.preventDefault();
		$(this).tab('show');
	});

	
	//all  show panes function
	function fnShowMainPaneAll() {
		console.log("show all");
		var cookie = UTIL.fnGetCookies();
		var para = {
			userId : cookie.userId,
			token : cookie.token,
			callback: function(json){
				var data=json.data.length;
				console.log("data length :"+data);
			}
		};
		FILE.fnListAll(para);
		UTIL.fnCookie("test_cookie", new Date());
		
	}

	function fnShowVideo() {
		console.log("show video");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token
		};
		FILE.fnListVideo(para);
	}

	function fnShowImage() {
		console.log("show Image");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token,			
			callback: function(json){
				var data=json.data;
				console.log("data"+data);
				
			}
		};
		
		FILE.fnListImage(para);
	}

	function fnShowMusic() {
		console.log("show Music");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token
		};
		FILE.fnListMusic(para);
	}

	function fnShowDocument() {
		console.log("show Document");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token
		};
		FILE.fnListDoc(para);
	}

	function fnShowOthers() {
		console.log("show Others");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token
		};
		FILE.fnListOther(para);
	}

	function fnShowTrash() {
		console.log("show video");
		var cookie = UTIL.fnGetCookies();

		var para = {
			userId : cookie.userId,
			token : cookie.token
		};
		FILE.fnListTrash(para);
	}
	
	// compute file size
	function formatFileSize(size) {
		if (size > 1024 * 1024) {
			size = (Math.round(size * 100 / (1024 * 1024)) / 100).toString()
					+ 'MB';
		} else {
			size = (Math.round(size * 100 / 1024) / 100).toString() + 'KB';
		}
		return size;
	};
	
	
    // fileupload function
	function fileupload() {

		var filenum = document.getElementById("main-uploadfile").files.length;
		console.log("filenum " + filenum);
		
		//add file-list-html to uploaderbox body
		for ( var i = 0; i < filenum; i++) {
			var file = document.getElementById("main-uploadfile").files[i];
			console.log("file.index: " + file.index + "  file.name : "
					+ file.name + " file.size : " + file.size);
			/*
			 * var itemTemplate = '<li id="${fileID}file"><span
			 * class="filename">${fileName}</span><span
			 * class="progressnum">${fileSize}' + '</span><a
			 * class="delfilebtn">删除</a></li>';
			 */
			var itemTemplate = '<div class="row-fluid"><div class="span4"><li id="${fileID}file"><span class="filename">${fileName}</span><span class="progressnum"></div><div class="span6">${fileSize}'
					+ '</span></div><div class="span2"><a class="delfilebtn">删除</a></li></div>';
			var html = itemTemplate.replace(/\${fileID}/g, i).replace(
					/\${fileName}/g, file.name).replace(/\${fileSize}/g,
					formatFileSize(file.size));
			$("#uploaderbox-filelist").append(html);
			/* 上传 */
		}
		
		//upload file in turn 
		for ( var i = 0; i < filenum; i++) {
			console.log("upload");
			var file = document.getElementById("main-uploadfile").files[i];
			if (!file) {
				console.log("no file");
				return;
			}
			var formData = new FormData();
			var cookie = UTIL.fnGetCookies();
			formData.append('upload', file);
			formData.append('user_id', cookie.userId);
			formData.append('token', cookie.token);
			formData.append('parent_id', "root");

			var xhr = new XMLHttpRequest();
			xhr.open('POST', "upload");
			xhr.onload = function() {
				if (xhr.status === 200) {
					console.log("success");
				} else {
					console.log("error");
				}
			};
			xhr.send(formData);
		}
	}
	
	//createfolder function
	function fncreatefolder() {
		console.log("create new folder");
		var cookie = UTIL.fnGetCookies();
		var foldername = $("#plus-fold-name").val();
		var para = {
			userId : cookie.userId,
			token : cookie.token,
			name : foldername,
			callback: function(json){
				filename=json.data.name;
				fileid=json.data.id;
				filecreatedate=json.data.createDate;
				var itemfolder ='<li  class="span2 " data-path="/" data-password="" data-date="${fileTime}" data-url="" data-title=""'+
				'data-type="folder" data-nid="${fileID}" f-index="${fileIndex}" style="float:left; overflow: hidden ;margin-left: 0px;">'+
				'<div id="" class="tab_folder_base" title="文档 创建时间:${fileTime}" > <img class="icon-folder-base" src="./image/folder_icon.png" /> '+
				'<div class="text-center">${fileName}</div></div><div class=" " style="display: none">'+	
				'</div><div class="" style="display: none">2013-09-2510:06</div></li>';
				var html = itemfolder.replace(/\${fileID}/g, fileid).replace(/\${fileName}/g, filename).replace(/\${fileTime}/g,filecreatedate);			
				$("#tab_folder_list").append(html);
			}
		
		};
		FILE.fncreatefolder(para);
		$("#main-btn-plus-Modal").hide();
		
	}
	
/*	$('.tab_folder_base').mouseover(function() {
		console.log("mouseover");
		$('.tab_folder_base').css('background-color','#99CCCC');
	});
	
	$('.tab_folder_base').mouseout(function()  {
		console.log("mouseout");
		$('.tab_folder_base').css('background-color','#FFFFFF');
	});		
	*/
	
	//create folder 
	$('#plus-modal-save').click(function(e) {
		fncreatefolder();
	});
	// main.jsp logout
	$("#header-logout").click(function() {

		UTIL.fnRedirect("homepage.html", location.href);
	});
	
	
	//btn-return  return to parent folder
	$("#main-btn-return").click(function() {
		$("#main-pane-first").show();
		$("#main-btn-toolbar").hide();
		$("#main_tab_pane").hide();
	});
	
	//file show  btn 
	$("#main-icon-th-list").click(function() {

		$("#tab_folder_list").show();
		$("#tab_folder_view").hide();
	});

	$("#main-icon-th").click(function() {

		$("#tab_folder_list").hide();
		$("#tab_folder_view").show();
	});
	
	//file sort btn
	$('#main-listbar-sort h2 a').mouseover(function() {
		$('#main-listbar-sort-ul').show();
		$('#main-listbar-sort h2 a').hide();
		// $('#loginDoor').addClass("textbox-border");
		// $('#registDoor').removeClass("textbox-border");
	});

	/*
	 * $('#main-listbar-sort h2 a').mouseout(function() {
	 * $('#main-listbar-sort-ul').hide(); $('#main-listbar-sort h2 a').show();
	 * //$('#loginDoor').addClass("textbox-border");
	 * //$('#registDoor').removeClass("textbox-border"); });
	 */
	
	//right click
	$("#main-tab-content").scrollspy();

	$("#main-tab-content").contextMenu('main-sys-menu', {
		bindings : {
			'menu-fold-new' : function() {
				alert("新建文件夹成功！");
			},

			'menu-fold-copy' : function() {
				alert("复制文件夹成功！");
			},

			'menu-fold-paste' : function() {
				alert("复制文件夹成功！");
			}
		}
	});
	
	
	//uploadfile  and modal operation
	$("#main-btn-upload").click(function() {
		$("#main-uploadfile").trigger('click');
	});
	
	
	$("#main-uploadfile").change(function() {
		$("#uploaderbox-modal").modal('show');
		fileupload();
	});

	$("#uploaderbox_part").click(function() {
		$("#uploaderbox-modal").modal('hide');
		$("#uploaderboxPart-modal").modal('show');
	});
	$("#uploaderbox_all").click(function() {
		$("#uploaderboxPart-modal").modal('hide');
		$("#uploaderbox-modal").modal('show');
	});

	$("#uploaderbox_close").click(function() {
		$("#uploaderboxPart-modal").modal('hide');
		$("#uploaderbox-modal").modal('hide');
	});

	/*
	 * $('[data-spy="scroll"]').each(function () { var $spy =
	 * $(this).scrollspy('refresh') });
	 */

});