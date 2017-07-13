define(function(require,exports) {

	 var exp =require("./module/hash");	
	 
	 $("#md5").click(function(event) {
		 window.alert("MD5    :   "+exp.md5("Hello,JavaScript"));
	 });
	 	 
});