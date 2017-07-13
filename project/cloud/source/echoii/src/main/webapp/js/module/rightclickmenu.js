define(function(require, exports) {

	var UTIL = require("./util.js");
	var SHARE = require("./share.js");
	var STAT = require("./stat.js");
	var ACTION = require("./action.js");
    var _stat=STAT.getStat();
    
	function f_hidden()//隐藏菜单
	{
		//rightbarmenu.style.visibility = "hidden";
		rightbarmenu.style.display = "none";
	}
	

	function rightmenu(elementID,menuID){
		var menu=document.getElementById(menuID);      //获取菜单对象
		var element=document.getElementById(elementID);//获取点击拥有自定义右键的 元素
		element.onmousedown=function(aevent){         //设置该元素的 按下鼠标右键右键的 处理函数
			if(window.event)aevent=window.event;      //解决兼容性
			if(aevent.button==2){                   //当事件属性button的值为2时，表用户按下了右键
				document.oncontextmenu=function(aevent){
					if(window.event){
						aevent=window.event;
						aevent.returnValue=false;         //对IE 中断 默认点击右键事件处理函数
					}else{
						aevent.preventDefault();          //对标准DOM 中断 默认点击右键事件处理函数
					};
				};

				var arrSon = [];
				arrSon = $('#' + _stat.findPaneId + ' input[type=checkbox]');
				for ( var i = 0; i < arrSon.length; i++) {
					if (arrSon[i].checked == true)
						arrSon[i].click();
				}

				
				var target =aevent.target;
				while(target.nodeName != 'TR'&& target.className != 'file-thumb'){
					target = target.parentNode;
				}
				if (target.nodeName == 'TR') {

					var checkboxId=$(target).attr("data-id");
					ACTION.selectFile(checkboxId);//打钩，背景高亮
					var para = UTIL.fnGetDefaultPara();
					para.file_id =$(target).attr("data-id");
					if (_stat.findPaneId== "main-pane-trash"){
						para.name=$('tr[data-id=' + checkboxId + '] .name span').attr("data-name"); 
						para.src =$('tr[data-id=' + checkboxId + '] .name span').attr("data-src");
						para.folder =$('tr[data-id=' + checkboxId + '] .name span').attr("data-folder");
						para.meta_id =$('tr[data-id=' + checkboxId + '] .name span').attr("data-meta-id");
					}else{
						para.name=$('tr[data-id=' + checkboxId + '] .name a').attr("data-name"); 
						para.src =$('tr[data-id=' + checkboxId + '] .name a').attr("data-src");
						para.folder =$('tr[data-id=' + checkboxId + '] .name a').attr("data-folder");
						para.meta_id =$('tr[data-id=' + checkboxId + '] .name a').attr("data-meta-id");
					}
					
					_stat.paraRemove = para ;
					_stat.fileId=checkboxId;
					_stat.paraRecover = UTIL.fnGetDefaultPara();
					_stat.paraRecover.file_id_list = para.file_id;
					
					rightbar_visited(menu,aevent);
				}
				if (target.className == 'file-thumb') {
					
					var checkboxId=$(target).attr("data-id");
					ACTION.selectFile(checkboxId);
					var para = UTIL.fnGetDefaultPara();
					para.file_id = $(target).attr("data-id");
					para.name=$('div[data-id=' + checkboxId + '] .name-row a').attr("data-name"); 
					para.src =$('div[data-id=' + checkboxId + '] .name-row a').attr("data-src");
					para.folder =$('div[data-id=' + checkboxId + '] .name-row a').attr("data-folder");
					para.meta_id =$('div[data-id=' + checkboxId + '] .name-row a').attr("data-meta-id");
					_stat.paraRemove = para ;
					_stat.fileId=checkboxId;
					_stat.paraRecover = UTIL.fnGetDefaultPara();
					_stat.paraRecover.file_id_list = para.file_id;
					
					rightbar_visited(menu,aevent);				
				}
			
				

			}

			function rightbar_visited(menu,aevent){
		 		//右键菜单显示
				if(_stat.findPaneId== "main-pane-trash"){
					$("#rm_dowload").hide();
					$("#rm_recover").show();
					$("#rm_remove").show();
					$("#rm_rename").hide();
					$("#rm_share").hide();
					$("#rm_share_dowload").hide();
					$("#rm_cancel_share").hide();
					$("#rm_update_idcode").hide();
				}else if(_stat.findPaneId== "main-pane-share"){
					$("#rm_dowload").hide();
					$("#rm_recover").hide();
					$("#rm_remove").hide();
					$("#rm_rename").hide();
					$("#rm_share").hide();
					$("#rm_share_dowload").show();
					$("#rm_cancel_share").show();
					$("#rm_update_idcode").show();
				}else{
					$("#rm_dowload").show();
					$("#rm_recover").hide();
					$("#rm_remove").show();
					$("#rm_rename").show();
					$("#rm_share").show();
					$("#rm_share_dowload").hide();
					$("#rm_cancel_share").hide();
					$("#rm_update_idcode").hide();
				}
				
				//menu.style.visibility = "visible";
				menu.style.display = "block";
				var W = document.documentElement.clientWidth; //网页可视 宽度
				 var H = document.documentElement.clientHeight; //网页可视高度
				 var X = aevent.clientX; //当前鼠标X坐标
				 var Y = aevent.clientY; //当前鼠标Y坐标
				var rightW = document.documentElement.clientWidth - aevent.clientX;
				 var bottomH = document.documentElement.clientHeight - aevent.clientY;
				if (rightW <200)//如果从鼠标位置到窗口右边的空间小于菜单的宽度，就定位菜单的左坐标（Left）为当前鼠标位置向左一个菜单宽度
				 {
					menu.style.left = (W - (W - X + menu.offsetWidth)) + "px";
				 }
				 else //否则，就定位菜单的左坐标为当前鼠标位置
				 {
					 menu.style.left = X  +  "px";
				 }
				if (bottomH <150)//如果从鼠标位置到窗口下边的空间小于菜单的高度，就定位菜单的上坐标（Top）为当前鼠标位置向上一个菜单高度
				 {
					menu.style.top = (H - (H - Y + menu.offsetHeight)) +"px";
				 }
				 else //否则，就定位菜单的上坐标为当前鼠标位置
				 {
					 menu.style.top = aevent.clientY+ "px";
				 }
				 $("#modal-shareid-choice").hide();
		 	}
			
			$("#rightbarmenu").click(function(e) {
				var target = e.target;
				var para = _stat.paraRemove;
				if (target.nodeName != 'A'){
					target = target.parentNode;
				}
				if (target.nodeName == 'A'||target.nodeName == 'DIV') {
					target = $(target);
					var dataType = target.attr("data-type");
					if (dataType == "act-download") {
						ACTION.fnDownloadFile(_stat.fileId);

					} else if (dataType == "act-rename") {
						$("#rename-modal-folderid").val(_stat.fileId);
						$("#sourceName").text(para.name);
						$("#modal-rename").modal('show');

					} else if (dataType == "act-remove") {
						
						if (_stat.findPaneId== "main-pane-trash") {
							_stat.paraRemove.remove_source = "true";
						} else {
							_stat.paraRemove.remove_source = "false";
						}
						$("#removeInfo").text(para.name);
						$("#modal-remove").modal('show');
						$("#remove-modal-confirmall").attr({
							"style" : "display: none;"
						});
						$("#remove-modal-confirm").attr({
							"style" : "display: inline-block;"
						});
						}else if (dataType == "act-share") {
							ACTION.fnShowShareModal();
					}else if (dataType == "act-recover") {
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

					}else if (dataType == "act-share-download") {
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

					} 
				}

			});		
				
		}
		
	} 
	exports.rightmenu = rightmenu;
	exports.f_hidden = f_hidden;

});