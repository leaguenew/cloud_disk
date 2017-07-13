<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="center-listpage">
	<div id="center-toolbar">
		<span id="center-toolbar-title">家庭数据中心</span> <a id="request-center-btn"
			class="btn offset8" href="#"> 绑定数据中心</a>
	</div>

	<div class="tabbable">
		<ul class="nav nav-tabs">
			<li class="active"><a id="nav-center-approve"
				href="#tab-center-approve" data-toggle="tab">已绑定的数据中心</a></li>
			<li><a id="nav-center-request" href="#tab-center-request"
				data-toggle="tab">待审核的数据中心</a></li>
			<li><a id="nav-center-refuse" href="#tab-center-refuse"
				data-toggle="tab">审核失败的数据中心</a></li>
		</ul>
		<div class="tab-content" id="tab-center-content">
			<div class="tab-pane active" id="tab-center-approve"></div>
			<div class="tab-pane " id="tab-center-request"></div>
			<div class="tab-pane " id="tab-center-refuse"></div>
		</div>
	</div>
</div>

<div id="center-contentpage" style="display: none">
	<div id="center-content-toolbar">
		<div class="center-content-logo">
			<img src="./image/sharegroup-pic.png">
		</div>
		<div id="center-content-namediv"></div>
		<div class="center-content-operate">
			<a id="center-content-quit" class="btn " href="#"> 解除绑定</a>
			<a id="center-content-cancel" class="btn " href="#">取消申请</a>
		</div>
	</div>



</div>