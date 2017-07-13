<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Hello Sea.js</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/lib/kalendae.css" type="text/css">
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/lib/zTreeStyle.css" type="text/css">
</head>
<body>


	<h2>Jersey RESTful Web Application!</h2>
	<p>
		<a href="webapi/myresource">Jersey resource</a>
	<p>
		Visit <a href="http://jersey.java.net">Project Jersey website</a> for
		more information on Jersey!
	</p>
	
	
	<input type="text" id="text" />
	<input type="button" id="btn">


	<div class="zTreeDemoBackground left">
		<ul id="DDDtree" class="ztree"></ul>
	</div>

 
  
	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/kalendae.standalone.js"></script>
	<script src="./js/lib/sea.js"></script>
	<script src="./js/lib/jquery.ztree.all-3.5.min.js"></script>
	<script>
		// Set configuration
		seajs.config({
			base : "./js/lib/"
		});

		seajs.use([ './js/index' ], function(index) {
			//index.hash();
		});
	</script>
</body>
</html>
<!doctype html>
