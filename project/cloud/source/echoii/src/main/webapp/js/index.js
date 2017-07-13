define(function(require, exports) {

	// function hash() {

	var exp = require("./module/hash");
	var FILE = require("./module/file.js");
	var AUTH = require("./module/auth.js");
	var UTIL = require("./module/util.js");

	$("#btn").click(function(event) {
		var text = $("#text").val().replace(/(^\s+)|(\s+$)/g,"");
		console.log("sssss_"+text);
	
	});


	$("#prelogin-btn").click(
			function() {
				AUTH.fnPreLogin("kkkygh", "pw999", function(json) {
					// console.log( json );
					$("#prelogin-resp").text(
							"status = " + json.status + "data" + json.data);
				});
			});

	$("#fnRedirect-btn").click(function() {
		UTIL.fnRedirect("homepage.html", location.href);
	});

	// $("#datepicker").jdPicker();

	var x = 1000;
	var y = 1;
	// alert(window.location.port);
	// alert(window.location.protocol);
	// alert(window.location.host);

	
	var setting = {
			check : {
				enable : true,
				chkboxType : {
					"Y" : "",
					"N" : "ps"
				}
			},
			data : {
				simpleData : {
					enable : true
				},	
				key: {
					title: "data_id"
				}
			},
			callback: {
				beforeExpand: zTreeBeforeExpand,
				//onMouseDown: zTreeBeforeExpand
			}

		};
	var para = UTIL.fnGetDefaultPara();
	para.begin = 0;
	para.size = 40;
	para.order = "desc";
	para.order_by = "lmf_date";
	var zNodes = [];
	FILE.fnListAll(para, function(json) {

		if (json.status == UTIL.fnStatus("OK")) {
			console.log("json.data length: " + json.data.length);
			var m=0;
			for ( var i = 0; i < json.data.length; i++) {
				zNodes.push({
					id : json.data[i].id,
					pId : 0,
					name : json.data[i].name,
					data_id: json.data[i].id,
				});
				console.log(i + " znode id : " + zNodes[m].id);
				m++;
				if (json.data[i].type == "folder") {
					zNodes[m-1].open ="false";
					console.log("typefolder!");
					zNodes.push({
						id : zNodes[m-1].id+"-empty",
						pId : json.data[i].id,
						name : "正在加载",
						data_id : json.data[i].id,
					});
					console.log(" znode id : " + zNodes[m].id);
					m++;
				}
			}
			$.fn.zTree.init($("#DDDtree"), setting, zNodes);
		}
	});
	
	
	console.log("znodes ； " + zNodes.length);
	console.log("setting ； " + setting.data.simpleData.enable );
	
	function zTreeBeforeExpand(event,treeNode){
		console.log("treeId ："+treeNode.id);
		console.log("treeName ："+treeNode.name);
		console.log("treeNode ："+treeNode);
		console.log("event ："+event);
		var treeObj = $.fn.zTree.getZTreeObj("DDDtree");
		var node=treeObj.getSelectedNodes();
		for (var i=0, l=node.length; i < l; i++) {
			treeObj.removeChildNodes(node[i]);
		};
		para.folder_id=treeNode.id;
		FILE.fnListAll(para, function(json) {
			if (json.status == UTIL.fnStatus("OK")) {
				console.log("json.data length: " + json.data.length);
				var m=0;
				var newNodes= [];
				for ( var i = 0; i < json.data.length; i++) {
					newNodes.push({
						id : json.data[i].id,
						pId : treeNode.id,
						name : json.data[i].name,
						data_id: json.data[i].id,
						isParent:false
					});
					console.log(i + " znode id : " + json.data[i].id);
					console.log(i + " treeNode.id : " + treeNode.name);
					m++;
					if (json.data[i].type == "folder") {
						newNodes[m-1].open ="false";
						console.log("typefolder!");
						newNodes.push({
							id : newNodes[m-1].id+"-empty",
							pId : json.data[i].id,
							name : "正在加载",
							data_id : json.data[i].id,
						});
						m++;
					}
				}
				zNodes=treeObj.addNodes(node[0],newNodes);
			}
		});
		
	};

});