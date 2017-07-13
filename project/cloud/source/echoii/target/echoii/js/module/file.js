define(function(require, exports) {

	exports.fnListAll = fnListAll;
	exports.fnListVideo = fnListVideo;
	exports.fnListMusic = fnListMusic;
	exports.fnListImage = fnListImage;
	exports.fnListDoc = fnListDoc;
	exports.fnListTrash = fnListTrash;
	exports.fnListOther = fnListOther;
	exports.fncreatefolder = fncreatefolder;

	var base_url = "service/0/file/";
	
	function fncreatefolder(para){
		var url = base_url + "create_folder";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	}

	function fnListAll(para) {
		var url = base_url + "list";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListAll

	function fnListVideo(para) {
		var url = base_url + "list_video";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListVideo
	
	function fnListImage(para) {
		var url = base_url + "list_image";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListImage

	function fnListMusic(para) {
		var url = base_url + "list_music";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListMusic

	function fnListDoc(para) {
		var url = base_url + "list_doc";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListDoc
	
	function fnListOther(para) {
		var url = base_url + "list_others";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListOther


	function fnListTrash(para) {
		var url = base_url + "list_torrent";
		var query = fnParsePara(para);
		$.get(url, query, para.callback);
	} // fnListDoc

	
	function fnParsePara(para) {
		var query = {
			user_id : para.userId,
			token : para.token,
			size : para.size,
			begin : para.begin,
			folder_id : para.folder_id,
			order : para.order,
			order_by : para.order_by,
			name : para.name
		};

		return query;
	}

});