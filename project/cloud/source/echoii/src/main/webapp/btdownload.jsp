<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中科甲骨云</title>
<link rel="stylesheet" href="css/style.css" type="text/css">
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/lib/zTreeStyle.css" type="text/css">

</head>

<body>
	<%@ include file="frame/header.jsp"%>

	<div id="main-body" class="clearfix">
			
	</div>
	<!-- end main-body -->
	


	


	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/jquery.tmpl.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<script src="./js/lang/cn.js"></script>
	<script src="./js/lib/sea.js"></script>
	<script>
		// Set configuration
		seajs.config({
			base : "./js/",
			alias: {
				"lang": "lang/cn.js"
			}
		});

		seajs.use([ 'main', 'lang' ], function(main) {

		});
	</script>
	
</body>
</html>