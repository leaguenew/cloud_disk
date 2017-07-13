<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<form id="userinfo-safeform" class="form-horizontal" method="get">
	<div class="control-group">
		<label class="control-label" for="userinfo-safe-email">email</label>
		<div class="controls userinfo-email-div" >
			<span id="userinfo-safe-email" ></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-password" >原始密码</label>
		<div class="controls">
			<input type="password" id="userinfo-password" name="password" placeholder="请输入您的原始密码">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-newpassword" >新密码</label>
		<div class="controls">
			<input type="password" id="userinfo-newpassword" name="newpassword" placeholder="请输入您的新密码">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-repassword" >确认新密码</label>
		<div class="controls">
			<input type="password" id="userinfo-repassword" name="repassword" placeholder="请确认您的新密码">
			<span class='help-inline'></span>
		</div>
	</div>

	<div class="control-group">
		<div class="controls">
			<!-- <input id="userinfo-change" type="button" data-loading-text="Loading..."
				class="btn  btn-info inline" value="修改个人资料" />  -->
			<input id="userinfo-safe-save"
				type="button" data-loading-text="Loading..."
				class="btn  btn-info inline" value="保存" />
			<a id="userinfo-safe-return" >返回网盘首面</a>
		</div>
	</div>

</form>






