<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css" media="screen">
<!-- 
<link rel="stylesheet/less" href="css/less/bootstrap.less">
<script src="js/lib/less-1.4.1.min.js"></script>
 -->
</head>
<body>
	<%@ include file="frame/header.jsp"%>

	<div class="container-fluid">
		<div class="span4">
			<ul class="nav nav-list">
				<li class="nav-header">List header</li>
				<li class="active"><a href="#">Home</a></li>
				<li><a href="#">Library</a></li>
			</ul>
		</div>
		
		<div class="span10">
		this is main content
		</div>

	</div>

</body>
</html>