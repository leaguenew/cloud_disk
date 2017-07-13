define(function(require, exports) {

	var UTIL = require("./util.js");
	var TMPL = require("./template.js");
	var STAT = require("./stat.js");
	var FILE = require("./file.js");
	var _stat = STAT.getStat();
	
	
	function fnUpload(){
		
		$("#upload-btn").click(function() {
			$("#modal-upload").modal('show');
		});

		$("#upload-delete-all").click(function() {
			var rows = $("#modal-upload-tb").children();
			if (rows == null || rows.length < 1) {
				return;
			}
			var row;
			var l = rows.length;
			var tmp;
			for ( var i = 0; i < l; i++) {
				row = $(rows[i]);
				tmp = row.attr("data-input-id");
				$("#" + tmp).remove();
				row.remove();
			}
		});

		$("#upload-submit-all").click(function() {
			var btns = $(".upload-submit-btn");
			if (btns == null || btns.length < 1) {
				return;
			}
			var l = btns.length;
			var btn;
			for ( var i = 0; i < l; i++) {
				btn = btns[i];
				fnHandleUploadBtn($(btn));
			}
			_stat.files = {}; //clear the Object
		});

		$("#upload-add-file").click(
				function() {
					$("#upload-input-file-modal").trigger("click");
				});
		
		$("#upload-input-file-modal").change(
				function() {
					var nid = "file-" + UTIL.fnCurrentTimeString();
					var filelist = document.getElementById("upload-input-file-modal").files;
					for(var i=0;i<filelist.length;i++){
						_stat.files["upload-input-" + nid + i] = filelist[i];
						var data = {
								file : filelist[i],
								name : filelist[i].name,
								size : UTIL.fnFormatFileSize(filelist[i].size),
								folder_id : _stat.currentFolderId,
								file_id : nid + i
							};
						if (filelist[i].size >= 200 * 1024 * 1024) {
							$.tmpl(TMPL.fnUploadModalTableOverSize(), data)
									.prependTo("#modal-upload-tb");
						} else {
							$.tmpl(TMPL.fnUploadModalTable(), data)
									.prependTo("#modal-upload-tb");
						}
					}
				});
		
		$("#modal-upload-tb").click(function(e) {
			e.preventDefault();
			var target = $(e.target);
			var act = target.attr("data-act");

			if (act == "submit") {
				fnHandleUploadBtn(target);
			} else if (act == "delete") {
				fnHandleDeleteBtn(target);
			}
		});

		function fnHandleDeleteBtn(target) {
			var inputId = target.attr("data-input-id");
			$("#" + inputId).remove();
			var rowId = target.attr("data-row-id");
			$("#" + rowId).remove();
		}

		
		function fnHandleUploadBtn(target) {
			target.attr("disabled", "true");
			var file =_stat.files[target.attr("data-input-id")];
			if(!file){
				console.log("no file");
				return;
			}
			var fileId = target.attr("data-file-id");

			var para = UTIL.fnGetDefaultPara();
			para.folder_id = target.attr("data-folder-id");
			para.file_id = fileId;

			$.tmpl(TMPL.fnUploadProgressBar(), para)
					.appendTo("#progress-" + fileId);

			FILE.fnUpload(para, file,
			// handle progress
			function(e) {
				if (!e.lengthComputable) {
					return;
				}

				var w = Math.round(e.loaded / e.total * 100);
				$("#p-bar-" + fileId).attr("style", "width: " + w + "%").html(
						w + "%");
			}, // function

			// handle complete
			function(e) {
				$("#upload-submit-btn-" + fileId).remove();
				$("#progress-" + fileId).html(
						"<span class='label label-success'>success</span>");
				
				fnReloadUploadFile(para);
			});
			
		} // fnHandleUploadBtn
		
	}
	
	//上传文件后重新加载文件列表，并保持在当前目录
	function fnReloadUploadFile(para) {
		if (!para.folder_id) {
			para.folder_id = _stat.currentFolderId;
		}
		_stat.currentFolderId = para.folder_id;
		_stat.tabAllFileIndex = 0;
		
		para.begin = _stat.tabAllFileIndex;
		para.size = 40;
		para.order = _stat.order;
		para.order_by = _stat.order_by;
		
		FILE.fnListAll(para, function(json) {
			$("#main-pane-all-list-body").empty();
			$("#main-pane-all-thumb-wrapper").empty();
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
	exports.fnUpload = fnUpload;
	exports.fnReloadUploadFile = fnReloadUploadFile;
});