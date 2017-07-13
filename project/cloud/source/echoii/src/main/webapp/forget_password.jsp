<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中科甲骨云-找回密码</title>
<link rel="stylesheet" href="css/style.css" type="text/css">
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">

</head>

<body>
	<div id="header-top-nav" class="navbar navbar-inverse">
		<div class="navbar-inner">
			<span class="span3" id="header-top-image"><img
				src="./image/logo.gif"></span>
			<ul id="header-menu" class="nav">
				<li><a href="homepage.html">登录</a></li>
			</ul>
		</div>
	</div>
	<div id="forgetpw-titlebg">
		<div id="forgetpw-title">
			<p>找回密码</p>
		</div>
	</div>
	
	<div class="forgetpw-step">
		
		<p><span>请填写邮箱获取重置密码链接</span></p>
		<hr/>
	</div>
	
	<div id="forgetpw-box">
		<form id="forgetpw-form" method="get">
			<fieldset>
				<div class="input">
					<input id="forgetpw-email" type="text" name="email" maxlength="30"
						placeholder="请输入您的注册邮箱" /> <span class='help-inline'></span>
				</div>
				<div>
					<input id="forgetpwSubmit" type="button" class="btn btn-primary" value="发送验证邮件" />
				</div>
			</fieldset>
		</form>
	</div>

	
	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<script src="./js/lib/jquery.validate.js"></script>
	<script src="./js/lib/jquery.cookie.js"></script>
	<script src="./js/lang/cn.js"></script>
	<script src="./js/lib/sea.js"></script>
	<script>
		// Set configuration
		seajs.config({
			base : "./js/lib/"
		});
		
		seajs.use([ './js/homepage' ]);
	</script>
</body>
</html>