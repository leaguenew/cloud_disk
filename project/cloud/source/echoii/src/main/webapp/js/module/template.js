define(function(require, exports) {

	var UTIL = require("./util.js");

	var tmpCache = {};
	var imgServerUrl = UTIL.fnUrl('imageServer');
	var musicServerUrl = UTIL.fnUrl('musicServer');

	function fnShareList() {

		if (tmpCache.shareList) {
			return tmpCache.shareList;
		}
		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='share'/></td>");
		tmp.push("<td class='icon'><div class='file-icon-${type}'></div></td>");
		tmp
				.push("<td class='name'><a  data-id='${id}' data-type='share' data-name='${name}' data-code='${validCode}' href='#'>${name}</a></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-share-download' data-name='${name}'  href='#'>下载</a>"
						+ " | <a data-id='${id}' data-type='act-cancel-share' data-name='${name}' href='#'>取消分享</a>"
						+ " | <a data-id='${id}' data-type='act-update-idcode' data-name='${name}' href='#'>更改密码</a>"
						+ "</div></td>");
		tmp.push("<td class='date'>${createDate}</td>");
		tmp.push("</tr>");

		tmpCache.shareList = tmp.join('');
		return tmpCache.shareList;

	}
	;

	function fnShareThumb() {

		if (tmpCache.shareThumb) {
			return tmpCache.shareThumb;
		}
		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='share'/></div>");
		tmp
				.push("<div class='thumb-row'><div class='file-thumb-icon-${type}'></div></div>");
		tmp
				.push("<div class='name-row'><a data-id='${id}' data-type='share' data-name='${name}' data-code='${validCode}' href='#'>${name}</a></div>");
		tmp.push("</div>");
		tmpCache.shareThumb = tmp.join('');
		return tmpCache.shareThumb;
	}
	;

	function fnMusicList() {

		if (tmpCache.musicList) {
			return tmpCache.musicList;
		}
		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='${type}'/></td>");
		tmp.push("<td class='icon'><div class='file-icon-${type}'></div></td>");
		tmp
				.push("<td class='name'><a  data-id='${id}' data-type='play_music' data-name='${name}' href='#'"
						+ "data-src="
						+ musicServerUrl
						+ "${name}?file_id=${id}>${name}</a></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-download' data-name='${name}'  href='#'>下载</a>"
						+ " | <a data-id='${id}' data-type='act-remove' data-name='${name}' href='#'>删除</a>"
						+ " | <a data-id='${id}' data-type='act-rename' data-name='${name}' href='#'>重命名</a>"
						+ " | <a data-id='${id}' data-type='act-share' data-name='${name}' href='#'>分享</a>"
						+ "</div></td>");
		tmp.push("<td class='date'>${lmf_date}</td>");
		tmp.push("</tr>");

		tmpCache.musicList = tmp.join('');
		return tmpCache.musicList;

	}
	;

	function fnMusicThumb() {

		if (tmpCache.musicThumb) {
			return tmpCache.musicThumb;
		}
		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='${type}'/></div>");
		tmp
				.push("<div class='thumb-row'><div class='file-thumb-icon-${type}'></div></div>");
		tmp
				.push("<div class='name-row'><a data-id='${id}' data-type='play_music' data-name='${name}'  href='#'"
						+ "data-src="
						+ musicServerUrl
						+ "${name}?file_id=${id}'>${name}</a></div>");
		tmp.push("</div>");
		tmpCache.musicThumb = tmp.join('');
		return tmpCache.musicThumb;
	}
	;

	function fnImgThumb() {
		if (tmpCache.imgThumb) {
			return tmpCache.imgThumb;
		}

		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='${type}'/></div>");
		tmp
				.push("<div class='thumb-row'>"
						+ "<a class='file-thumb-icon-${type}' data-id='${id}' data-type='show-image' data-name='${name}' href='#' "
						+ "data-src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=large'>"
						+ "<img data-type='img-icon' class='img-thumb' src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=thumb'></img>"
						+ "</a></div>");
		tmp
				.push("<div class='name-row'>"
						+ "<a data-id='${id}' data-type='show-image' data-name='${name}' href='#' "
						+ "data-src='" + imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=large'>"
						+ "${name}</a></div>");
		tmp.push("</div>");

		tmpCache.imgThumb = tmp.join('');
		return tmpCache.imgThumb;
	}

	function fnImgList() {
		if (tmpCache.imgList) {
			return tmpCache.imgList;
		}

		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='${type}'/></td>");
		tmp
				.push("<td class='icon'>"
						+ "<a class='file-icon-${type}' data-id='${id}' data-type='show-image' data-name='${name}' href='#' "
						+ "data-src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=large'>"

						+ "<img data-type='img-icon' class='img-list-icon' src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=icon'></img>"

						+ "</a>" + "</td>");
		tmp
				.push("<td class='name'><a data-id='${id}' data-type='show-image' data-name='${name}' href='#' "
						+ "data-src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=img&size=large'>${name}</a></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-download' data-name='${name}'  href='#'>下载</a>"
						+ " | <a data-id='${id}' data-type='act-remove' data-name='${name}' href='#'>删除</a>"
						+ " | <a data-id='${id}' data-type='act-rename' data-name='${name}' href='#'>重命名</a>"
						+ " | <a data-id='${id}' data-type='act-share' data-name='${name}' href='#'>分享</a>"
						+ "</div></td>");

		tmp.push("<td class='date'>${lmf_date}</td>");
		tmp.push("</tr>");

		tmpCache.imgList = tmp.join('');
		return tmpCache.imgList;
	}

	function fnVideoList() {
		if (tmpCache.videoList) {
			return tmpCache.videoList;
		}

		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='${type}'/></td>");
		tmp
				.push("<td class='icon'>"
						+ "<a class='file-icon-${type}' data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#'>"
						+ "<img data-type='img-icon' class='img-list-icon' src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=mov&size=icon'></img>"
						+ "</a>" + "</td>");
		tmp
				.push("<td class='name'><a data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#'>${name}</a></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-download' data-name='${name}'  href='#'>下载</a>"
						+ " | <a data-id='${id}' data-type='act-remove' data-name='${name}' href='#'>删除</a>"
						+ " | <a data-id='${id}' data-type='act-rename' data-name='${name}' href='#'>重命名</a>"
						+ " | <a data-id='${id}' data-type='act-share' data-name='${name}' href='#'>分享</a>"
						+ "</div></td>");
		tmp.push("<td class='date'>${lmf_date}</td>");
		tmp.push("</tr>");

		tmpCache.videoList = tmp.join('');
		return tmpCache.videoList;
	}

	function fnVideoThumb() {
		if (tmpCache.videoThumb) {
			return tmpCache.videoThumb;
		}
		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='${type}'/></div>");

		tmp
				.push("<div class='thumb-row'>"
						+ "<a class='file-thumb-icon-${type}' data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#' >"
						+ "<img data-type='img-icon' class='img-thumb' src='"
						+ imgServerUrl
						+ "?id=${metaFolder}/${metaId}&type=mov&size=thumb'></img>"
						+ "</a></div>");

		tmp
				.push("<div class='name-row'><a data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#'>${name}</a></div>");
		tmp.push("</div>");
		tmpCache.videoThumb = tmp.join('');
		return tmpCache.videoThumb;
	}

	function fnFileList() {
		if (tmpCache.fileList) {
			return tmpCache.fileList;
		}

		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='${type}'/></td>");
		tmp
				.push("<td class='icon'>"
						+ "<a class='file-icon-${type}' data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#'></a>"
						+ "</td>");
		tmp
				.push("<td class='name'><a data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-type='${type}' data-name='${name}' href='#'>${name}</a></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-download' data-name='${name}'  href='#'>下载</a>"
						+ " | <a data-id='${id}' data-type='act-remove' data-name='${name}' href='#'>删除</a>"
						+ " | <a data-id='${id}' data-type='act-rename' data-name='${name}' href='#'>重命名</a>"
						+ " | <a data-id='${id}' data-type='act-share' data-name='${name}' href='#'>分享</a>"
						+ "</div></td>");
		tmp.push("<td class='date'>${lmf_date}</td>");
		tmp.push("</tr>");

		tmpCache.fileList = tmp.join('');
		return tmpCache.fileList;
	}

	function fnFileThumb() {
		if (tmpCache.fileThumb) {
			return tmpCache.fileThumb;
		}
		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='${type}'/></div>");
		tmp
				.push("<div class='thumb-row'><div class='file-thumb-icon-${type}'></div></div>");
		tmp
				.push("<div class='name-row'><a data-id='${id}' data-folder='${metaFolder}' data-meta-id='${metaId}' data-name='${name}' data-type='${type}' href='#'>${name}</a></div>");
		tmp.push("</div>");
		tmpCache.fileThumb = tmp.join('');
		return tmpCache.fileThumb;
	}

	function fnTrashList() {

		if (tmpCache.trashList) {
			return tmpCache.trashList;
		}
		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' id='${id}-input-list' data-id='${id}' data-type='${type}'/></td>");
		tmp
				.push("<td class='icon'><span class='file-icon-${type}' data-id='${id}' data-type='${type}' data-name='${name}'></span></td>");
		tmp
				.push("<td class='name'><span data-id='${id}' data-type='${type}' data-name='${name}'>${name}</span></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp
				.push("<td class='act'><div class='file-action'>"
						+ "<a data-id='${id}' data-type='act-recover' data-name='${name}'  href='#'>还原</a>"
						+ " | <a data-id='${id}' data-type='act-remove' data-name='${name}' href='#'>删除</a>"
						+ "</div></td>");
		tmp.push("<td class='date'>${lmf_date}</td>");
		tmp.push("</tr>");

		tmpCache.trashList = tmp.join('');
		return tmpCache.trashList;

	}

	function fnTrashThumb() {
		if (tmpCache.trashThumb) {
			return tmpCache.trashThumb;
		}
		var tmp = [];
		tmp.push("<div class='file-thumb' data-id='${id}'>");
		tmp
				.push("<div class='box-row'><input type='checkbox' value='${id}' id='${id}-input-thumb' data-id='${id}' data-type='${type}'/></div>");
		tmp
				.push("<div class='thumb-row'><div class='file-thumb-icon-${type}'></div></div>");
		tmp
				.push("<div class='name-row'><a data-id='${id}' data-type='${type}' data-name='${name}' href='#'>${name}</a></div>");
		tmp.push("</div>");
		tmpCache.trashThumb = tmp.join('');
		return tmpCache.trashThumb;
	}

	function fnShareGroupThumb_create() {
		if (tmpCache.fnShareGroup_create) {
			return tmpCache.fnShareGroup_create;
		}
		var tmp = [];
		tmp.push("<div class='well  sharegroup-thumb '>");
		tmp
				.push("<div class='sharegroup-header-logo'><img  data-type='create' data-id='${id}' data-name='${name}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='sharegroup-header-info'><p> 群名称: <span  data-type='create'  data-id='${id}' data-name='${name}' >${name}</span><img  data-type='create' data-id='${id}' data-name='${name}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='sharegroup-date' data-id='${id}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnShareGroup_create = tmp.join('');
		return tmpCache.fnShareGroup_create;
	}

	function fnShareGroupThumb_join() {
		if (tmpCache.fnShareGroup_join) {
			return tmpCache.fnShareGroup_join;
		}
		var tmp = [];
		tmp.push("<div class='well  sharegroup-thumb '>");
		tmp
				.push("<div class='sharegroup-header-logo'><img  data-type='join' data-id='${id}' data-name='${name}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='sharegroup-header-info'><p> 群名称: <span  data-type='join'  data-id='${id}' data-name='${name}' >${name}</span><img  data-type='join' data-id='${id}' data-name='${name}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='sharegroup-date' data-id='${id}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnShareGroup_join = tmp.join('');
		return tmpCache.fnShareGroup_join;
	}

	function fnShareGroupThumb_others() {
		if (tmpCache.fnShareGroup_others) {
			return tmpCache.fnShareGroup_others;
		}
		var tmp = [];
		tmp.push("<div class='well  sharegroup-thumb '>");
		tmp
				.push("<div class='sharegroup-header-logo'><img  data-type='others' data-id='${id}' data-name='${name}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='sharegroup-header-info'><p> 群名称: <span  data-type='others'  data-id='${id}' data-name='${name}' >${name}</span><img  data-type='others' data-id='${id}' data-name='${name}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='sharegroup-date' data-id='${id}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnShareGroup_others = tmp.join('');
		return tmpCache.fnShareGroup_others;
	}

	function fnShareGroup_name() {
		if (tmpCache.fnShareGroup_name) {
			return tmpCache.fnShareGroup_name;
		}
		var tmp = [];
		tmp
				.push("<span id='sharegroup-content-name' data-type='${data_type}' data-id='${data_id}' data-name='${data_name}'></span>");
		tmpCache.fnShareGroup_name = tmp.join('');
		return tmpCache.fnShareGroup_name;

	}

	function fnShareGroup_listmember() {

		if (tmpCache.fnShareGroup_listmember) {
			return tmpCache.fnShareGroup_listmember;
		}
		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' data-id='${id}' data-type='sharegroup'/></td>");
		tmp
				.push("<td class='name'><span data-id='${id}' data-type='share' data-name='${name}' href='#'>${name}</span></td>");
		tmp.push("<td class='email'>${email}</td>");
		tmp.push("<td class='nickName'>${nickName}</td>");
		tmp.push("</tr>");

		tmpCache.fnShareGroup_listmember = tmp.join('');
		return tmpCache.fnShareGroup_listmember;

	}
	;
	
	function fnShareGroup_listfile() {

		if (tmpCache.fnShareGroup_listmember) {
			return tmpCache.fnShareGroup_listmember;
		}
		var tmp = [];
		tmp.push("<tr data-id='${id}'>");
		tmp
				.push("<td class='box'><input type='checkbox' value='${id}' data-id='${id}' data-type='sharegroup'/></td>");
		tmp
				.push("<td class='name'><span data-id='${id}' data-type='share' data-name='${name}' href='#'>${name}</span></td>");
		tmp.push("<td class='size'>${size}</td>");
		tmp.push("<td class='date'>${createDate}</td>");
		tmp.push("</tr>");

		tmpCache.fnShareGroup_listmember = tmp.join('');
		return tmpCache.fnShareGroup_listmember;

	}
	;

	function fnUploadModalTable() {
		if (tmpCache.uploadModalTable) {
			return tmpCache.uploadModalTable;
		}

		var tmp = [];
		tmp
				.push("<tr id='upload-row-${file_id}' class='upload-pendding' data-input-id='upload-input-${file_id}'>");
		tmp.push("<td class='upload-row-name'>${name}</td>");
		tmp.push("<td class='upload-row-size'>${size}</td>");
		tmp.push("<td class='upload-row-id' id='progress-${file_id}'></td>");
		tmp
				.push("<td class='upload-row-btn'><button data-act='delete' data-row-id='upload-row-${file_id}' "
						+ "data-input-id='upload-input-${file_id}' class='upload-del-btn btn btn-mini'>删除</button>");

		tmp
				.push("<button id='upload-submit-btn-${file_id}' data-act='submit' data-input-id='upload-input-${file_id}' "
						+ "data-file-id='${file_id}' data-folder-id='${folder_id}' class='upload-submit-btn btn btn-mini btn-primary'>上传</button></td>");

		tmp.push("</tr>");

		tmpCache.uploadModalTable = tmp.join('');
		return tmpCache.uploadModalTable;

	}

	function fnUploadModalTableOverSize() {
		if (tmpCache.uploadModalTableOverSize) {
			return tmpCache.uploadModalTableOverSize;
		}

		var tmp = [];
		tmp
				.push("<tr id='upload-row-${file_id}' class='upload-pendding' data-input-id='upload-input-${file_id}'>");
		tmp.push("<td class='upload-row-name'>${name}</td>");
		tmp.push("<td>${size}</td>");
		tmp.push("<td> " + LANG.over_uploadsize + "</td>");
		tmp
				.push("<td><button data-act='delete' data-row-id='upload-row-${file_id}' "
						+ "data-input-id='upload-input-${file_id}' class='upload-del-btn btn btn-mini'>删除</button>");

		tmp.push("</tr>");

		tmpCache.uploadModalTableOverSize = tmp.join('');
		return tmpCache.uploadModalTableOverSize;

	}

	function fnUploadInput() {
		if (tmpCache.uploadInput) {
			return tmpCache.uploadInput;
		}

		var tmp = "<input id='${id}' type='file' style='display: none' multiple='multiple'/>";

		tmpCache.uploadInput = tmp;
		return tmpCache.uploadInput;
	}

	function fnUploadProgressBar() {
		if (tmpCache.uploadProgressBar) {
			return tmpCache.uploadProgressBar;
		}
		var tmp = "<div class='progress'>"
				+ "<div id='p-bar-${file_id}' class='bar' style='width: 0%'></div>"
				+ "</div>";

		tmpCache.uploadProgressBar = tmp;
		return tmpCache.uploadProgressBar;

	}

	function fnTopNav() {
		if (tmpCache.topNav) {
			return tmpCache.topNav;
		}

		var tmp = "<li><a data-target='${id}' data-name='${name}' href='#'>${name}</a> <span class='divider'>/</span></li>";

		tmpCache.topNav = tmp;
		return tmpCache.topNav;
	}
	
	function fnCenter_approve() {
		if (tmpCache.fnCenter_approve) {
			return tmpCache.fnCenter_approve;
		}
		var tmp = [];
		tmp.push("<div class='well  center-thumb '>");
		tmp
				.push("<div class='center-header-logo'><img  data-type='approve' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='center-header-info'><p> 家庭数据中心: <span  data-type='approve'  data-id='${deviceId}' data-name='${deviceId}' >${deviceId}</span><img  data-type='approve' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='center-date' data-id='${deviceId}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnCenter_approve = tmp.join('');
		return tmpCache.fnCenter_approve;
	}
	
	function fnCenter_refuse() {
		if (tmpCache.fnCenter_refuse) {
			return tmpCache.fnCenter_refuse;
		}
		var tmp = [];
		tmp.push("<div class='well  center-thumb '>");
		tmp
				.push("<div class='center-header-logo'><img  data-type='refuse' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='center-header-info'><p> 家庭数据中心: <span  data-type='refuse'  data-id='${deviceId}' data-name='${deviceId}' >${deviceId}</span><img  data-type='refuse' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='center-date' data-id='${deviceId}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnCenter_refuse = tmp.join('');
		return tmpCache.fnCenter_refuse;
	}
	
	function fnCenter_request() {
		if (tmpCache.fnCenter_request) {
			return tmpCache.fnCenter_request;
		}
		var tmp = [];
		tmp.push("<div class='well  center-thumb '>");
		tmp
				.push("<div class='center-header-logo'><img  data-type='request' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-pic.png'></div>");
		tmp
				.push("<div class='center-header-info'><p> 家庭数据中心: <span  data-type='request'  data-id='${deviceId}' data-name='${deviceId}' >${deviceId}</span><img  data-type='request' data-id='${deviceId}' data-name='${deviceId}' src='./image/sharegroup-created.png'></p> ");
		tmp
				.push("<p> 日期 : <span title='center-date' data-id='${deviceId}'>${createDate}</span></p></div>");
		tmp.push("</div>");

		tmpCache.fnCenter_request = tmp.join('');
		return tmpCache.fnCenter_request;
	}
	
	function fnCenter_name() {
		if (tmpCache.fnCenter_name) {
			return tmpCache.fnCenter_name;
		}
		var tmp = [];
		tmp
				.push("<span id='center-content-name' data-type='${data_type}' data-id='${data_id}' data-name='${data_name}'></span>");
		tmpCache.fnCenter_name = tmp.join('');
		return tmpCache.fnCenter_name;

	}
	
	exports.fnMusicList = fnMusicList;
	exports.fnMusicThumb = fnMusicThumb;
	exports.fnImgList = fnImgList;
	exports.fnFileList = fnFileList;
	exports.fnFileThumb = fnFileThumb;
	exports.fnTrashList = fnTrashList;
	exports.fnTrashThumb = fnTrashThumb;
	exports.fnUploadModalTable = fnUploadModalTable;
	exports.fnUploadInput = fnUploadInput;
	exports.fnUploadProgressBar = fnUploadProgressBar;
	exports.fnTopNav = fnTopNav;
	exports.fnUploadModalTableOverSize = fnUploadModalTableOverSize;
	exports.fnImgThumb = fnImgThumb;
	exports.fnShareList = fnShareList;
	exports.fnShareThumb = fnShareThumb;
	exports.fnVideoList = fnVideoList;
	exports.fnVideoThumb = fnVideoThumb;
	exports.fnShareGroupThumb_create = fnShareGroupThumb_create;
	exports.fnShareGroupThumb_join = fnShareGroupThumb_join;
	exports.fnShareGroupThumb_others = fnShareGroupThumb_others;
	exports.fnShareGroup_name = fnShareGroup_name;
	exports.fnShareGroup_listmember = fnShareGroup_listmember;
	exports.fnShareGroup_listfile = fnShareGroup_listfile;
	exports.fnCenter_approve = fnCenter_approve; 
	exports.fnCenter_refuse = fnCenter_refuse;
	exports.fnCenter_request = fnCenter_request;
	exports.fnCenter_name = fnCenter_name;
	
});
