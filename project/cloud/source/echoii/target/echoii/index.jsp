<html>
<head>
<meta charset="utf-8">
<title>Hello Sea.js</title>
</head>
<body>
	
	 
    <h2>Jersey RESTful Web Application!</h2>
    <p><a href="webapi/myresource">Jersey resource</a>
    <p>Visit <a href="http://jersey.java.net">Project Jersey website</a>
    for more information on Jersey!
    </p> 
    <input type="button" value="md5" id="md5">
    
    <button id="prelogin-btn" >pre-login</button>
    <button id="fnRedirect-btn" >fnRedirect-btn</button>
    <p id="prelogin-resp"></p>
   
    <script src="./js/lib/jquery-1.8.3.min.js"></script>	
	<script src="./js/lib/sea.js"></script>	
		<script>
		
		  // Set configuration
		  seajs.config({
		    base: "./js/lib/"
		  });
          
         seajs.use(['./js/index'],function (index) {           
     			//index.hash();
          });
		</script>	
</body>
</html>
<!doctype html>
