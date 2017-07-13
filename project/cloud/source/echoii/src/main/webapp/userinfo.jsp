<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中科甲骨云用户信息中心</title>
<link rel="stylesheet" href="css/style.css" type="text/css">
<link rel="stylesheet" href="css/lib/kalendae.css" type="text/css">
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">

</head>

<body>
	<%@ include file="frame/header.jsp"%>

	<div id="userinfo-body" class="clearfix">
			<div id="userinfo-cat-wrapper">
				<%@ include file="frame/userinfo_left_nav.jsp"%>
			</div>
			<div id="userinfo-content">
				<div id="userinfo_tab_pane" class="tab-content">
					<%-- <div id="pane-home" class="tab-pane active">
						<%@ include file="frame/userinfo_tab_home.jsp"%></div> --%>
					<div class="tab-pane active" id="userinfo-pane-base">
						<%@ include file="frame/userinfo_tab_base.jsp"%></div>
					<div class="tab-pane" id="userinfo-pane-safe">
						<%@ include file="frame/userinfo_tab_safe.jsp"%></div>

				</div>
				<!-- end of userinfo-tab-pane -->
			</div>
			<!-- end of userinfo-content -->
		</div>
		<!-- end userinfo-body -->
	
	<!-- end of row-fluid -->
	<!-- end of container-fluid -->

	


	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<script src="./js/lib/jquery.validate.js"></script>
	<script src="./js/lib/kalendae.standalone.js"></script>
	<script src="./js/lang/cn.js"></script>
	<script src="./js/lib/sea.js"></script>
	<script>
		// Set configuration
		seajs.config({
			base : "./js/lib/"
		});

		seajs.use([ './js/userinfo' ]);
	</script>
</body>
</html>