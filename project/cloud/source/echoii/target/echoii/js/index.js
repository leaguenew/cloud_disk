define(function(require,exports) {

	// function hash() {
		 
	 var exp =require("./module/hash");	
	 
	 var AUTH = require( "./module/auth.js" );
	 var UTIL = require("./module/util.js");
	 
	 $("#md5").click(function(event) {
		window.alert("MD5    :   "+exp.md5("Hello,JavaScript"));
//		 var req = {
//				 username: "7788",
//				 password:"55555"
//		 };
//		 $.get( "service/0/auth/reg", req, function(json){
//			 alert("success, user id = " + json.data.id);
//		 } )
	 });
	 	 
	// } 
	// exports.hash = hash;
	 
	 $("#prelogin-btn").click( function(){
		 AUTH.fnPreLogin( "kkkygh", "pw999", function( json ){
			 //console.log( json );
			 $("#prelogin-resp").text( "status = " + json.status + "data" + json.data );
		 });
	 });
	 
	 $("#fnRedirect-btn").click( function(){
		 UTIL.fnRedirect("homepage.html", location.href);
	 });

});