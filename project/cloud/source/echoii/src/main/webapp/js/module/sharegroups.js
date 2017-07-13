define(function(require, exports) {

	var group_create = "service/0/group/create";
	var group_info = "service/0/group/info";
	var group_list = "service/0/group/list";
	var group_quit = "service/0/user/quit";
	var group_listmember = "service/0/group/list_member";
	var group_join = "service/0/group/join";
	var group_file_list = "service/0/group/file/list";
	var group_file_delete = "service/0/group/file/delete";
	var group_file_copy = "service/0/group/file/copy";

		
	function fnCreate(para, callback) {
		var query = fnParsePara(para);
		$.get(group_create, query, callback);
	}
	
	function fnInfo(para,callback){
		var query = fnParsePara(para);
		$.get(group_info, query, callback);
	}

	function fnList(para, callback) {
		var query = fnParsePara(para);
		$.get(group_list, query, callback);
	}
	
	function fnJoin(para, callback) {
		var query = fnParsePara(para);
		$.get(group_join, query, callback);
	}

	function fnQuit(para, callback) {
		var query = fnParsePara(para);
		$.get(group_quit, query, callback);
	}

	function fnListMember(para, callback) {
		var query = fnParsePara(para);
		$.get(group_listmember, query, callback);
	}

	function fnFileDel(para, callback) {
		var query = fnParsePara(para);
		$.get(group_file_delete, query, callback);
	}
	
	function fnFileList(para, callback) {
		var query = fnParsePara(para);
		$.get(group_file_list, query, callback);
	}
	
	function fnFileCopy(para, callback) {
		var query = fnParsePara(para);
		$.get(group_file_copy, query, callback);
	}
	
	function fnParsePara(para) {
		var query = {
			user_id : para.user_id,
			token : para.token,
			name : para.name,
			desp : para.desp,
			order : para.order,
			begin : para.begin,
			size : para.size,
			mode : para.mode,
			group_id : para.group_id,
			return_detail : para.return_detail,
			group_file_id : para.group_file_id,
			folder_id : para.folder_id,
		};
		return query;
	}

	exports.fnCreate = fnCreate;
	exports.fnInfo = fnInfo;
	exports.fnList = fnList;
	exports.fnQuit = fnQuit;
	exports.fnListMember = fnListMember;
	exports.fnJoin = fnJoin;
	exports.fnFileDel = fnFileDel;
	exports.fnFileList = fnFileList;
	exports.fnFileCopy = fnFileCopy;

});