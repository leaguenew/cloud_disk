<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<div id="modal-create-folder" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h id="main-btn-plus-Label">新建文件夹</h>
	</div>
	<div class="modal-body">

		<label>文件夹名称: <input id="create-folder-name"
			class="input-xlarge span5" type="text" placeholder="新建文件夹名称" />
		</label>


	</div>
	<div class="modal-footer">
		<button data-dismiss="modal" class="btn">取消</button>
		<button id="create-folder-submit" class="btn btn-primary">保存</button>
	</div>
</div>
<!-- modal-create-folder -->



<div id="modal-upload" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h id="main-uppload-Label">上传文件</h>
	</div>
	<div class="modal-body">
		<button id="upload-add-file" class="btn">添加文件</button>
		<button id="upload-submit-all" class="btn btn-primary pull-right">上传全部文件</button>
		<button id="upload-delete-all" class="btn">删除列表</button>
		<table id="modal-upload-table" class="table">
			<thead>
				<th class="th-name">文件名</th>
				<th class="th-size">大小</th>
				<th class="th-progress">&nbsp;</th>
				<th class="th-act">&nbsp;</th>
			</thead>
			<tbody id="modal-upload-tb"></tbody>
		</table>
	</div>
	<!-- 	<div class="modal-footer">
		<button id="upload-submit-all" class="btn btn-primary pull-right">上传全部文件</button>
		<button id="upload-delete-all" class="btn">删除列表</button>
		<button data-dismiss="modal" class="btn">关闭</button>
	</div> -->
</div>
<!-- modal-upload -->


<div id="modal-rename" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<input id="rename-modal-folderid" type="text" style="display: none" />
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h id="main-btn-rename-Label">文件重命名</h>
	</div>
	<div class="modal-body ">
		<div class="row ">
			<div>
				<span class="span2">原文件名称</span>
				<span class="spanName" id="sourceName"></span>
			</div>
		</div>
		<div class="row ">
			<div class="span2">
				<p>新文件名称</p>
			</div>
			<div class="span3">
				<input id="rename-fold-name" class="input-xlarge" type="text"
					maxlength="250" placeholder="文件名" />

			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button id="rename-modal-save" class="btn btn-primary">保存</button>
	</div>
</div>
<!-- modal-rename -->


<div id="modal-image" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true" data-backdrop="static">
	<div class='image-header-row'>
		<h4>
			<a id='modal-image-close' href='#' class='image-header-btn'
				data-dismiss="modal">关闭</a>
		</h4>
	</div>
	<div class='image-content-row'>
		<img id="loaderimage" align="absmiddle" src="./image/loading.gif">
		<img id="modal-image-disp" align="absmiddle"
			onload="this.style.visibility='visible';loaderimage.style.display='none'" />
	</div>
</div>
<!-- modal-image -->


<div id="modal-music" class="modal hide fade " data-backdrop="false"
	aria-hidden="true" role="dialog" tabindex="-1">
	<div class="modal-header" id="music-modal-header">
		<span id="songName" class="songName">歌名</span>
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true" id="modal-music-button">×</button>
	</div>

	<div class="modal-body ">
		<audio id="musicBox" controls="true"></audio>
	</div>
</div>
<!-- modal-music -->


<div id="modal-share" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h id="main-btn-plus-Label">分享文件</h>
	</div>
	<div class="modal-body">
		<div id="modal-shareid-choice">
			<label class="inline">请问是否需要分享密码？</label> <label class="radio inline">
				<input type="radio" name="shareid" value="yes">是
			</label> <label class="radio inline"> <input type="radio"
				name="shareid" value="no">否
			</label>
			<div id="modal-share_inputcodeid">
				<p class="text-left">请输入分享密码 ：</p>
				<input id="modal-share-createid" class="input-small" type="text" />
				<span id="share-codeid-warn"></span>
			</div>
			<div>
				<input type="button" id="modal-share-butoon" value="创建分享链接" />
			</div>
		</div>

		<div id="modal-share-box">
			<p class="text-left">成功生成分享链接，复制以下链接发给QQ、飞信好友吧！</p>
			<input id="modal-share-link" class="input-xxlarge" type="text" />
			<div id="modal-share-idbox">
				<p class="text-left">分享密码</p>
				<input id="modal-share-id" class="input-small" type="text" />
			</div>
			<div id="modal-share-updatebox" style="display: none">
				<p class="text-left">请输入新的分享密码</p>
				<input id="modal-share-updateid" class="input-small" type="text" />
				<span id="share-updatecodeid-warn"></span>
			</div>
		</div>

	</div>
	<div class="modal-footer">
		<button id="share-modal-updatecode" class="btn btn-primary"
			style="display: none">确认</button>
		<button data-dismiss="modal" class="btn">取消</button>
	</div>
</div>
<!-- modal-share -->

<div id="modal-share-download" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h4 id="main-btn-plus-Label">请输入下载密码</h4>
	</div>
	<div class="modal-body">
		<label>密码: <input id="share-download-code"
			class="input-large " type="text" placeholder="" /> <span
			id="share-download-code-error"></span>
		</label>

	</div>
	<div class="modal-footer">
		<button data-dismiss="modal" class="btn">取消</button>
		<button id="share-download-submit" class="btn btn-primary">确认</button>
	</div>
</div>
<!-- modal-share-down -->



<div id="modal-video" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true" data-backdrop="static">
	<div class='header-row'>
		<h4>
			<a id='modal-video-close' href='#' class='image-header-btn'
				data-dismiss="modal">关闭</a>
		</h4>
	</div>
	<div id="video-player">Loading the player...</div>

</div>

<!-- modal-video -->

<div id="modal-recover" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h id="main-btn-recover-Label">文件还原</h>
	</div>
	<div class="modal-body ">
		<div class="row ">
			<div class="spanRecover">
				<p>确定还原文件：</p>
				<p id="recoverInfo"></p>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button id="recover-cancel" class="btn">取消</button>
		<button id="recover-modal-confirmall" class="btn btn-primary "
			style="display: none">确定</button>
		<button id="recover-modal-confirm" class="btn btn-primary"
			style="display: none">确定</button>
	</div>
</div>
<!-- modal-recover -->

<div id="modal-remove" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-recover-Label">文件删除</h3>
	</div>
	<div class="modal-body ">
		<div class="row ">
			<div class="spanRemove">
				<p>确定删除文件：</p>
				<p id="removeInfo"></p>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button id="remove-cancel" class="btn">取消</button>
		<button id="remove-modal-confirmall" class="btn btn-primary"
			style="display: none">确定</button>
		<button id="remove-modal-confirm" class="btn btn-primary"
			style="display: none">确定</button>
	</div>
</div>
<!-- modal-remove -->

<div id="modal-sharegroup" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-recover-Label">创建文件共享群</h3>
	</div>
	<div class="modal-body ">

		<span class="offset1">创建文件共享群，轻松与他人共享资料、照片！</span>
		<div class="span4">
			<label>群名称</label> <input type="text" id="sharegroup-create-name"
				placeholder="给群起个给力的名字吧">
		</div>
		<div class="span6">
			<label>签名</label>
			<textarea rows="3" class="span6" id="sharegroup-create-info"
				placeholder="群介绍有助于成员更好的了解群哦"></textarea>
		</div>
		<div class="span4">
			<label>入群方式</label> <label class="radio inline"> <input
				type="radio" name="group-type" value="nocode">直接加入群
			</label> <label class="radio inline"> <input type="radio"
				name="group-type" value="code">输入邀请码加入
			</label>
		</div>
		<div>
			<a id="create-group-modal-btn" class="btn span3 offset1" href="#">创建共享群
			</a>
		</div>

	</div>

</div>
<!-- modal-sharegroup -->

<div id="modal-sharegroup-search" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-recover-Label">查找文件共享群</h3>
	</div>
	<div class="modal-body  ">
		<span>输入群号快速查找：</span>
		<div class="input-append">
			<input type="text" class="input-xlarge"
				id="sharegroup-search-groupid" placeholder="请输入要查找的群号！">
			<button id="sharegroup-modal-search-btn" class="btn">查找</button>
		</div>

		<div id="sharegroup-search-result" class="well  sharegroup-thumb "
			style="display: none">
			<div class="sharegroup-header-logo">
				<img src="./image/sharegroup-pic.png">
			</div>
			<div class="sharegroup-search-info">
				<p>
					共享群: <span id="sharegroup-search-groupname" title="sharegroup-name"></span>
					<img src="./image/sharegroup-created.png">
				</p>
				<p>介绍:</p>
				<div>
					<pre id="sharegroup-search-groupdesp"></pre>
				</div>

			</div>
		</div>
		<div>
			<button id="sharegroup-modal-search-join" class="btn">加入该群</button>
		</div>
	</div>
</div>
<!-- modal-sharegroup--search -->

<div id="modal-sharegroup-invite" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-invite-title">邀请用户加入共享群</h3>
	</div>
	<div class="modal-body  ">
		<span>请输入用户的email：</span>
		<div class="input-append">
			<input type="text" class="input-xlarge" id="sharegroup-invite-email"
				placeholder="用户email!">
		</div>
		<div>
			<button id="sharegroup-modal-invite-msgsend" class="btn">发送消息</button>
		</div>
	</div>
</div>
<!-- modal-sharegroup--invite -->


<div id="modal-sharegroup-upload" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true" >
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-upload-title">上传群共享文件</h3>
	</div>
	<div class="modal-body" class="row">
		<div id="modal-sharegroup-upload-choose" class="span6">
			<span>请选择您的云盘文件</span>
			<div class="zTreeDemoBackground left">
				<ul id="modal-saregroup-filelist-choose" class="ztree">
				</ul>
			</div>
			<div >
				<button id="sharegroup-modal-upload-btn" class="btn">上传群共享</button>
			</div>
		</div>
	</div>
</div>
<!-- modal-sharegroup--upload -->



<div id="rightbarmenu">
	<ul>
		<li id="rm_dowload"><a data-type='act-download' href="#"><div
					class="rightbarmenu-download"></div>下载</a></li>
		<li id="rm_recover"><a data-type='act-recover' href="#"><div
					class="rightbarmenu-share"></div>还原</a></li>
		<li id="rm_remove"><a data-type='act-remove' href="#"><div
					class="rightbarmenu-delete"></div>删除</a></li>
		<li id="rm_rename"><a data-type='act-rename' href="#"><div
					class="rightbarmenu-rename"></div>重命名</a></li>
		<li id="rm_share"><a data-type='act-share' href="#"><div
					class="rightbarmenu-share"></div>分享</a></li>
		<!-- 分享页面 -->
		<li id="rm_share_dowload"><a data-type='act-share-download'
			href="#"><div class="rightbarmenu-download"></div>下载</a></li>
		<li id="rm_cancel_share"><a data-type='act-cancel-share' href="#"><div
					class="rightbarmenu-share"></div>取消分享</a></li>
		<li id="rm_update_idcode"><a data-type='act-update-idcode'
			href="#"><div class="rightbarmenu-share"></div>更改密码</a></li>
	</ul>
</div>
<!-- modal-rightbarmenu-->



<div id="modal-center-request" class="modal hide fade" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="main-btn-recover-Label">申请绑定数据中心</h3>
	</div>
	<div class="modal-body  ">
		<span>输入数据中心账号：</span>
		<div class="input-append">
			<input type="text" class="input-xlarge"
				id="center-request-id" placeholder="请输入要绑定的数据中心账号！">
		</div>
		<div>
			<button id="center-modal-request-btn" class="btn span2 offset2">绑定</button>
		</div>
	</div>
</div>


<!-- modal-center-request -->