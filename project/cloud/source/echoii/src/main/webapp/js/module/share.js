define(function(require, exports) {

	var base_url = "service/0/share/";

	function fnSharefiles(para, callback) {
		var url = base_url + "share_files";
		var query = fnParsePara(para);
		$.get(url, query, callback);

	}
	;

	function fnSharelist(para, callback) {
		var url = base_url + "list";
		var query = fnParsePara(para);
		$.get(url, query, callback);

	}
	;

	function fnSharefiles_info(para, callback) {
		var url = base_url + "file_info";
		var query = fnParsePara(para);
		$.get(url, query, callback);

	}
	;
	
	function fnShare_cancel(para, callback) {
		var url = base_url + "cancel_share";
		var query = fnParsePara(para);
		$.get(url, query, callback);

	}
	;
	
	function fnShare_update_idcode(para, callback) {
		var url = base_url + "update_idcode";
		var query = fnParsePara(para);
		$.get(url, query, callback);

	}
	;
	
	function fnShare_group(para, callback){
		var url = base_url + "share_group";
		var query = fnParsePara(para);
		$.get(url, query, callback);
	}
	;
	
	function fnParsePara(para) {
		var query = {
			user_id : para.user_id,
			token : para.token,
			file_id_list : para.file_id_list,
			size : para.size,
			begin : para.begin,
			order : para.order,
			order_by : para.order_by,
			idcode : para.idcode,
			file_id : para.file_id,
			group_folder_id :para.group_folder_id,
			group_id : para.group_id
		};

		return query;
	}


	exports.sharefiles = fnSharefiles;
	exports.sharelist = fnSharelist;
	exports.shareInfo = fnSharefiles_info;
	exports.shareCancel = fnShare_cancel;
	exports.shareUpdateCode = fnShare_update_idcode;
	exports.sharegroup = fnShare_group;
});