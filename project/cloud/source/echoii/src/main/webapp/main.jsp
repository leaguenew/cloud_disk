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
			<div id="main-cat-wrapper">
				<%@ include file="frame/left_nav.jsp"%>
			</div>
			<div id="main-content">
				<%@ include file="frame/toolbar.jsp"%>
				<div id="main_tab_pane" class="tab-content">
					<div id="main-pane-home" class="tab-pane active">
						<%@ include file="frame/tab_home.jsp"%>
					</div>
					<div class="tab-pane" id="main-pane-all">
						<%@ include file="frame/tab_all.jsp"%></div>
					<div class="tab-pane" id="main-pane-video">
						<%@ include file="frame/tab_video.jsp"%></div>
					<div class="tab-pane" id="main-pane-image">
						<%@ include file="frame/tab_image.jsp"%></div>
					<div class="tab-pane" id="main-pane-music">
						<%@ include file="frame/tab_music.jsp"%></div>
					<div class="tab-pane" id="main-pane-document">
						<%@ include file="frame/tab_doc.jsp"%></div>
					<div class="tab-pane" id="main-pane-others">
						<%@ include file="frame/tab_other.jsp"%></div>
					<div class="tab-pane" id="main-pane-center">
						<%@ include file="frame/tab_center.jsp"%></div>
					<!-- <div class="tab-pane" id="main-pane-safebox">safebox</div> -->
					<div class="tab-pane" id="main-pane-share">
						<%@ include file="frame/tab_share.jsp"%></div>
					<div class="tab-pane" id="main-pane-trash">
						<%@ include file="frame/tab_trash.jsp"%></div>
					<div class="tab-pane" id="main-pane-sharegroups">
					    <%@ include file="frame/tab_sharegroups.jsp"%></div>
					<!-- <div class="tab-pane" id="main-pane-hotres">hotresource</div>  -->
				</div>
				<!-- end of main-tab-pane -->
			</div>
			<!-- end of main-content -->
		</div>
		<!-- end main-body -->
	
	<!-- end of row-fluid -->
	<!-- end of container-fluid -->

	<%@ include file="frame/modals.jsp"%>
	<%@ include file="frame/hidden.jsp"%>


	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/jquery.tmpl.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<!--  <script src="./js/lib/jquery.contextmenu.r2.js"></script>-->
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
	<script type="text/javascript" src="jwplayer/jwplayer.js"></script>
	<input id="upload-input-file-modal" type="file" style="display: none" multiple="multiple">
</body>
</html>