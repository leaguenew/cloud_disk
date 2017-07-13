<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="sharegroup-listpage">
	<div id="sharegroup-toolbar">
		<span id="sharegroup-toolbar-title">我的文件共享群</span> <a
			id="create-group-btn" class="btn offset8" href="#"> 创建群 </a> <a
			id="search-group-btn" class="btn " href="#"> 查找群 </a>
	</div>

	<div class="tabbable">
		<ul class="nav nav-tabs">
			<li class="active"><a id="nav-sharegroup-hot"
				href="#tab-sharegroup-hot" data-toggle="tab">热门分享群</a></li>
			<li><a id="nav-sharegroup-created"
				href="#tab-sharegroup-created" data-toggle="tab">我创建分享群</a></li>
			<li><a id="nav-sharegroup-join" href="#tab-sharegroup-join"
				data-toggle="tab">我加入分享群</a></li>
			<li><a id="nav-sharegroup-others" href="#tab-sharegroup-others"
				data-toggle="tab">其他</a></li>
		</ul>
		<div class="tab-content" id="tab-sharegroup-content">
			<div class="tab-pane active" id="tab-sharegroup-hot">
				<p>热门分享群,欢迎您的加入</p>
			</div>
			<div class="tab-pane " id="tab-sharegroup-created"></div>
			<div class="tab-pane " id="tab-sharegroup-join"></div>
			<div class="tab-pane " id="tab-sharegroup-others"></div>
		</div>
		<div id="sharegroup-picture" class="offset4">
			<img src="./image/sharegroups.png">
		</div>
	</div>
</div>
<div id="sharegroup-contentpage" style="display: none">
	<div id="sharegroup-content-toolbar">
		<div class="sharegroup-content-logo">
			<img src="./image/sharegroup-pic.png">
		</div>
		<div id="sharegroup-content-namediv"></div>
		<div class="sharegroup-content-operate">
			<a id="sharegroup-content-invite" href="#"> 邀请群成员 |</a> <a
				id="sharegroup-content-join" href="#"> 加入群 |</a> <a
				id="sharegroup-content-share" href="#"> 共享文件 |</a> <a
				id="sharegroup-content-setting" href="#"> 群成员信息 | </a> <a
				id="sharegroup-content-quit" href="#"> 退出群 |</a>
		</div>
	</div>
	<div id="sharegroup-content-listmember" class="span12">
		<table id="listmember-table"
			class=" table table-condensed table-hover table-striped">
			<thead id="listmember-table-header">
				<tr>
					<th class="box"><input type="checkbox" id="checkAll" /></th>
					<th class="name"><span class="list-view-col">用户名</span></th>
					<th class="email"><span class="list-view-col">邮箱</span></th>
					<th class="nickName"><span class="list-view-col">昵称</span></th>
				</tr>
			</thead>
			<tbody id="listmember-table-body"></tbody>
		</table>
	</div>
	
	<div id="sharegroup-content-listfile" class="span12">
		<table id="listfile-table"
			class="table table-condensed table-hover table-striped">
			<thead id="listfile-table-header">
				<tr>
					<th class="box"><input type="checkbox" id="checkAll" /></th>
					<th class="name"><span class="list-view-col">文件名</span></th>
					<th class="size"><span class="list-view-col">大小</span></th>
					<th class="date"><span class="list-view-col">日期</span></th>
				</tr>
			</thead>
			<tbody id="listfile-table-body"></tbody>
		</table>
	</div>

</div>