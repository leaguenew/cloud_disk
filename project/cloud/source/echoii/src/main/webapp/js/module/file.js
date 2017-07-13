define(function(require, exports) {

	exports.fnListAll = fnListAll;
	exports.fnListVideo = fnListVideo;
	exports.fnListMusic = fnListMusic;
	exports.fnListImage = fnListImage;
	exports.fnListDoc = fnListDoc;
	exports.fnListTrash = fnListTrash;
	exports.fnListOther = fnListOther;
	exports.fnCreateFolder = fnCreateFolder;
	exports.fnSortFolderAhead = fnSortFolderAhead;
	exports.fnUpload = fnUpload;
	exports.fnDel = fnDel;
	exports.fnPackDel = fnPackDel;
	exports.fnRename = fnRename;
	exports.fnRecover = fnRecover;

	var base_url = "service/0/file/";
	var device_url = "service/0/device";
	var group_url = "service/0/group";

	function fnUpload(para, file, fnProgress, fnComplete, fnErr, fnCancel) {
		if (!file) {
			return;
		}

		var formData = new FormData();
		formData.append('upload', file);
		formData.append('user_id', para.user_id);
		formData.append('token', para.token);
		formData.append('folder_id', para.folder_id);

		var xhr = new XMLHttpRequest();

		if (fnProgress) {
			xhr.upload.addEventListener("progress", fnProgress, false);
		}

		if (fnComplete) {
			xhr.upload.addEventListener("load", fnComplete, false);
		}

		if (fnErr) {
			xhr.upload.addEventListener("error", fnErr, false);
		}

		if (fnCancel) {
			xhr.upload.addEventListener("abort", fnCancel, false);
		}

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

	function fnSortFolderAhead(files) {
		if (!files) {
			return null;
		}
		var l = files.length;
		var folderList = [];
		var fileList = [];
		var tmp;
		for ( var i = 0; i < l; i++) {
			tmp = files[i];
			if (tmp.type == 'folder') {
				folderList.push(tmp);
			} else {
				fileList.push(tmp);
			}
		} // for

		return folderList.concat(fileList);
	}

	function fnCreateFolder(para, callback) {
		var url = base_url + "create_folder";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}

	function fnListAll(para, callback) {
		var url = base_url + "list";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListAll

	function fnListVideo(para, callback) {
		var url = base_url + "list_video";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListVideo

	function fnListImage(para, callback) {
		var url = base_url + "list_image";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListImage

	function fnListMusic(para, callback) {
		var url = base_url + "list_music";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListMusic

	function fnListDoc(para, callback) {
		var url = base_url + "list_doc";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListDoc

	function fnListOther(para, callback) {
		var url = base_url + "list_others";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListOther

	function fnListTrash(para, callback) {
		var url = base_url + "list_trash";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListTrash

	function fnListShareGroup(para, callback) {
		var url = base_url + "share_group";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	} // fnListDoc

	function fnDel(para, callback) {
		var url = base_url + "del";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}// fnDel
	
	function fnPackDel(para, callback) {
		var url = base_url + "del_list";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}// fnPackDel

	function fnRename(para, callback) {
		var url = base_url + "rename";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}
	
	function fnRecover(para, callback) {
		var url = base_url + "recover";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}

	function fnParsePara(para) {
		var query = {
			user_id : para.user_id,
			token : para.token,
			size : para.size,
			begin : para.begin,
			folder_id : para.folder_id,
			order : para.order,
			order_by : para.order_by,
			name : para.name,
			new_name : para.new_name,
			file_id : para.file_id,
			remove_source : para.remove_source,
			file_id_list : para.file_id_list
		};

		return query;
	}

});