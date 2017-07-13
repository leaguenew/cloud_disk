<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="top-nav-wrapper" class="hide">

	<ul id="top-nav" class="breadcrumb">
		<li><a id="#nav-home" data-target="home" href="main.jsp">首页</a> <span class="divider">/</span></li>
	</ul>

</div>

<div id="toolbar-wrapper" class="hide">
	<button id="back-btn" class="btn">返回</button>
<!-- 	<button id="create-folder-btn" class="btn"><img class="logo" src="../image/file_type_icons.png">新建文件夹</button>
	<button id="upload-btn" class="btn">上传文件</button>
	<button id="download-btn" class="btn" style="display: none;">批量下载</button> -->
	<a id="upload-btn"  class="btn" href="#">
		<span class="toolbar-upload"></span>上传文件
	</a>
	<a id="create-folder-btn" class="btn" href="#">
		<span class="toolbar-addfile"></span>新建文件夹
	</a>
	<a id="download-btn" class="btn" href="#"  style="display: none;">
		<span class="toolbar-download"></span>批量下载
	</a>
	<a id="share-btn" class="btn" href="#"  style="display: none;">
		<span class="toolbar-share"></span>共享
	</a>
	<a id="delete-btn" class="btn" href="#"  style="display: none;">
		<span class="toolbar-delete"></span>批量删除
	</a>
	<a id="recover-btn" class="btn" href="#"  style="display: none;">
		<span class="toolbar-recover"></span>批量还原
	</a>
	
	<div class="view-btn-thumb disabled" id="thumb-view-btn">
	</div>
	<div class="view-btn-list-visited" id="list-view-btn">
	</div>
</div>


<table id="list-table-header" class="hide table table-condensed table-hover table-striped">
		<thead>
			<tr>
				<th class="box"><input type="checkbox" id="checkAll"/></th>
				<th class="icon"></th>
				<th class="name"><span class="list-view-col">文件名</span><span class="orderImg"></span></th>
				<th class="size"><span class="list-view-col">大小</span><span class="orderImg"></span></th>
				<th class="act"><span class="list-view-col">操作</span></th>
				<th class="date"><span class="list-view-col">日期</span><span class="orderImg"></span></th>
			</tr>
		</thead>
</table>