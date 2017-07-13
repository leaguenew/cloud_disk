define(function(require,exports) {
	
		var AUTH = require("./auth");
		var HASH = require("./hash"); 
		
		$('#loginDoor').mouseover(function () {
 			$('#left').show();
 			$('#right').hide();
 		});
 		
		$('#registDoor').mouseover(function () {
 			$('#left').hide();
 			$('#right').show();
 		});
    		
    	$('form').validate({
    		 rules:{
    			 id_username:{required:true,email:true,minlength:3},
    			 id_password:{required:true}
    		 },
    		 messages:{
    			 id_username:{
    				 required:"email不能为空",
    				 email:"email地址不正确"
    			 }
    		 },
    	    errorPlacement:function(error,element){
    	    	error.appendTo(element.siblings("span"));
    	    }
    		 
    	 });
    	 
    	 
    	 $('#loginSubmit').click(function(){
    		 
	    		var useremail = $("#login_email").val();
	    		var password = $("#login_password").val();
	    		
	    		console.log("uname :" +useremail);
	    		var emailHash = HASH.md5(useremail);
	    		console.log("before fnprelogin unameHash :" +emailHash);
	    		
	    		AUTH.fnPreLogin(emailHash,function(json){
	    			
	    		   console.log("success, token = " + json.data.pre_login_token);
	    		   
	 			   newPsd = HASH.md5(HASH.md5(password)+json.data.pre_login_token);
	 			   console.log("newPSD: " + newPsd);
	 			   
	 			   idcode = json.data.pre_login_token;
	 			   console.log("idcode: "+idcode);
	 			
	 			   
	 		   });
	    		
	    		console.log("before fnlogin unameHash :"+ unameHash);
	    		AUTH.fnLogin(emailHash,newPsd,idcode,function(json){
	    			
	    			   console.log("success, token = " + json.data.token);
	    		
		        });
	 			  
	    	   
    	 });
    	 
    	 $('#regSubmit').click(function(){
			 
				var useremail = $("#reg_email").val();
				var password = $("#reg_password").val();
				
				console.log("regiter  submit");
				console.log("hash password:" +HASH.md5(password));
				
				var psdHash = HASH.md5(password);
		
				AUTH.fnReg(useremail,psdHash,function(json){
					
					console.log("fnreg  function");
					
					console.log("success ,id :" + json.data.id);
				});
			
		 });
	 	 
    
});
