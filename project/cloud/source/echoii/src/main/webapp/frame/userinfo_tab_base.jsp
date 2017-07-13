<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<form id="userinfo-baseform" class="form-horizontal" method="get">
	<div class="control-group">
		<label class="control-label" for="userinfo-base-email">email</label>
		<div class="controls userinfo-email-div">
			<span id="userinfo-base-email"></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-nickname">昵称</label>
		<div class="controls">
			<input type="text" id="userinfo-nickname" name="nickname" placeholder="请输入您的昵称">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-name">真实姓名</label>
		<div class="controls">
			<input type="text" id="userinfo-name" name="name" placeholder="请输入您的真实姓名">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-idcard">身份证号</label>
		<div class="controls">
			<input type="text" id="userinfo-idcard" name="idcard" placeholder="请输入您的身份证号">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-gender">性别</label>
		<div class="controls">
			<label class="radio inline"> <input type="radio" name="sex"
				value="m">男
			</label> <label class="radio inline"> <input type="radio" name="sex"
				value="f">女
			</label>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-datepicker">生日</label>
		<div class="controls">
			<input type="text" id="userinfo-datepicker"  name="birthday"class="auto-kal"/>
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-tel">手机号</label>
		<div class="controls">
			<input type="text" id="userinfo-tel" name="tel" placeholder="请输入您的手机号">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-qq">QQ号</label>
		<div class="controls">
			<input type="text" id="userinfo-qq" name="qq" placeholder="请输入您的QQ号">
			<span class='help-inline'></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-job">工作单位</label>
		<div class="controls">
			<input type="text" id="userinfo-job" name="job" placeholder="请输入您的工作单位">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="userinfo-introduction">签名</label>
		<div class="controls">
			<textarea rows="3" class="span4" id="userinfo-introduction"
				placeholder="请输入您的个性签名"></textarea>
		</div>
	</div>

	<div class="control-group">
		<div class="controls">
			<!-- <input id="userinfo-change" type="button" data-loading-text="Loading..."
				class="btn  btn-info inline" value="修改个人资料" />  -->
			<input id="userinfo-base-save" type="button"
				data-loading-text="Loading..." class="btn  btn-info inline"
				value="保存" /> <a id="userinfo-base-return">返回网盘首面</a>
		</div>
	</div>

</form>






