define(function(require, exports) {
	
	var _stat = {
			currentTabId : "home",
			tabAllFileIndex : 0,
			tabDocIndex : 0,
			currentFolderId : "root",
			navList : [],
			fileId : 0,
			end : 0,
			arrCheckboxDataId : [],
			findPaneId : "",
			paraRemove : [],
			paraRemoveall : [],
			paraRecover : [],
			paraRecoverall : [],
			order : "desc",
			order_by : "lmf_date",
			files : {}
		};
	
	function getStat(){
		return _stat;
	} 
	
	exports.getStat = getStat;
	
});