define(function(require,exports) {
	
	   var url_preLogin = "service/0/auth/pre_login";
	   var url_login = "service/0/auth/login";
	   var url_reg = "service/0/auth/reg";

	   function fnPreLogin(email,callback){
		   
		   	   console.log("fnPreLogin");
			   //var myDate = new Date();
			   
			   var preData = {
					   email: email,
					   //time:myDate.toLocaleTimeString()
			   };
			   
			   $.ajax({
				   url: url_preLogin, 
				   async: false,
				   type: "GET",
				   data:  preData,
				   success: callback
			   });
			   
			  // $.get(url_preLogin, preData, callback);
		   
		}  
	   
	   function fnLogin(email,psd,code,callback){
		   
		   	   console.log("fnLogin");
			   var myDate = new Date();
			   
			   var data = {
					   
						email: email,
						password: psd,
						idcode: code,
						time:myDate.toLocaleTimeString()
			   };
			   console.log("uname :"+email);
			   console.log("password :"+psd);
			   console.log("code :" +code);
			   $.get(url_login, data, callback);
		} 
	   
	   
	   function fnReg(email,psd,callback){
		   
		   console.log("fnReg");   
		   
		   var data = {
					email: email,
					password: psd
		   };
		   
		   $.get(url_reg, data, callback);
	   
	   }

	   
	   exports.fnPreLogin = fnPreLogin;
	   exports.fnLogin = fnLogin;
	   exports.fnReg=fnReg;
});