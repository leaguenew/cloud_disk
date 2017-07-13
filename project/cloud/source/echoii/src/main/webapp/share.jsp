<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中科甲骨云</title>
<link rel="stylesheet" href="css/share.css" type="text/css">
<link rel="stylesheet" href="css/style.css" type="text/css">
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">

</head>

<body>
	<%@ include file="frame/header.jsp"%>
	
				<div id="share" class="row ">
					<div class="share-main">
						<div class="span12 offset3">
							<div class="btn-wrapper">
								<div class="file-btns">
									<a id="file-btns-down" href="#" class="btn btn-block btn-primary">分享下载</a>
								</div>
								<div id="username" class="file-owner span11 text-right"></div>
							</div style="clear:both">
							<div class="file-info">
									<ul class="row inline">
										<li class="span5">文件名</li>
										<li class="span2">大小</li>
										<li class="span2">创建时间</li>
									</ul>
									<hr/>
									<ul class="row inline">
										<li id="sourcename" class="span5"></li>
										<li id="size" class="span2"></li>
										<li id="date" class="span2"></li>
									</ul>
									<hr/>
							</div>
							<div id="share_code_box" class="text-left" >
								请输入下载密码 ：<input id="modal-share-id" class="input-small" type="text" />
							</div>
							
						</div>
					</div>
				</div>
    <%@ include file="frame/hidden.jsp"%>
	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<script src="./js/lang/cn.js"></script>
	<script src="./js/lib/sea.js"></script>
	<script>
		// Set configuration
		seajs.config({
			base : "./js/lib/"
		});

		seajs.use([ './js/share_page' ]);
	</script>
</body>
</html>