<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中科甲骨云</title>
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/style.css" type="text/css">
</head>

<body>
	<%@ include file="frame/header.jsp"%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<ul id="main-cat-nav" class="nav nav-pills nav-stacked ">
					<li class="active "><a id="main-pane-link-all"
						href="#main-pane-all"> <i class="icon-home"></i>全部文件
					</a></li>
					<li><a id="main-pane-link-video" href="#main-pane-video">
							<i class="icon-film"></i>视频
					</a></li>
					<li><a id="main-pane-link-image" href="#main-pane-image">
							<i class="icon-picture"></i>图片
					</a></li>
					<li><a id="main-pane-link-music" href="#main-pane-music">
							<i class="icon-music"></i>音乐
					</a></li>
					<li><a id="main-pane-link-document" href="#main-pane-document">
							<i class="icon-file"></i>文档
					</a></li>
					<li><a id="main-pane-link-others" href="#main-pane-others">
							<i class="icon-magnet"></i>其他
					</a></li>
					<li><a id="main-pane-link-center" href="#main-pane-center">
							<i class="icon-inbox"></i>家庭数据中心
					</a></li>
					<li><a id="main-pane-link-safebox" href="#main-pane-safebox">
							<i class="icon-lock"></i>文件保险箱
					</a></li>
					<li class="pane-list"><a id="main-pane-link-share"
						href="#main-pane-share"> <i class="icon-share"></i>我的共享
					</a></li>
					<li class="pane-list"><a id="main-pane-link-trash"
						href="#main-pane-trash"> <i class="icon-trash"></i>回收站
					</a></li>
					<li class="pane-list"><a id="main-pane-link-sharegroup"
						href="#main-pane-sharegroup"> <i class="icon-leaf"></i>文件共享群
					</a></li>
					<li class="pane-list"><a id="main-pane-link-hotres"
						href="#main-pane-hotres"> <i class="icon-fire"></i>热门资源
					</a></li>
				</ul>
			</div>
			<!-- end of span3 main-cat-nav div -->

			<div id="main-content" class="span9">
				<div class="row">
					<span id="mian-tab-link" class="span4">
						<a id="main-tab-link-return" href="#"></a>
						<a id="main-tab-link-show" href="#"></a>
					</span>
					<form class="form-search pull-right">
						<div class="input-append">
							<input type="text" class="input-medium" id="search">
							<button type="submit" class="btn">Search</button>
						</div>
					</form>
				</div>
				<!--end of return-fisrt-page-link and search bar  -->
				<div id="main-btn-toolbar" class="span12">
					<div class="row">
						<div class="btn-toolbar span6">
							<a id="main-btn-return" class="btn"> <i
								class="icon-arrow-left"></i>返回
							</a> 
							<input id="main-uploadfile" class="main_uploadfile" style="display:none;" type="file" name="fileselect[]"  multiple/>
							<a id="main-btn-upload" type="file" class="btn"
								href="#uploaderbox-modal" data-toggle="modal"
								aria-labelledby="uploaderbox-modal" aria-hidden="true"><i
								class="icon-arrow-up" multiple></i>上传 </a> 
							<a id="main-btn-plus" type="submit" class="btn" 
								href="#main-btn-plus-Modal" data-toggle="modal" 
								aria-labelledby="main-btn-plus-Label" aria-hidden="true"> <i
								 class="icon-plus"></i>新建文件夹</a> 
								<a type="submit" class="btn"> <i class="icon-arrow-down"></i>离线下载
							</a>
						</div>
						<div class="btn-listbar span6">
							<div class="row">
								<div id="main-listbar-sort" class="span4 offset1">
									<h2>
										<span class="text">排序：</span> <a class="name asc"
											onclick="return false;" href="#"><i class="icon-arrow-up"></i>名称</a>
									</h2>
									<ul id="main-listbar-sort-ul" class="unstyled">
										<li style=""><a class="name asc" onclick="return false;"
											href="###"><i class="icon-arrow-up"></i>名称</a></li>
										<li style=""><a class="fsize asc" onclick="return false;"
											href="###"><i class="icon-arrow-up"></i>大小</a></li>
										<li style=""><a class="new-upload desc"
											onclick="return false;" href="###"><i
												class="icon-arrow-up"></i>最近上传</a></li>
									</ul>
								</div>

								<div class="dropdown span3">
									<button class="btn dropdown-toggle" data-toggle="dropdown">
										排序 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a id="btn-name-up" href="#"><i
												class="icon-arrow-up"> </i>名称</a></li>
										<li><a id="btn-name-down" href="#"><i
												class="icon-arrow-down"></i>名称</a></li>
										<li><a id="btn-size-up" href="#"><i
												class="icon-arrow-up"></i>大小</a></li>
										<li><a id="btn-size-down" href="#"><i
												class="icon-arrow-down"></i>大小</a></li>
										<li><a id="btn-date-up" href="#"><i
												class="icon-arrow-up"></i>修改日期</a></li>
										<li><a id="btn-date-down" href="#"><i
												class="icon-arrow-down"></i>修改日期</a></li>
									</ul>
								</div>
								<div class="span2">
									<button id="main-icon-th-list" class="btn">
										<i class="icon-th-list"></i>
									</button>
								</div>
								<div class="span2">
									<button id="main-icon-th" class="btn">
										<i class="icon-th"></i>
									</button>
								</div>
							</div>
						</div>

					</div>
				</div>
				<!-- end of tool bar -->
				<div id="main-pane-first"><%@ include
						file="frame/tab_main.jsp"%></div>
				<div id="main-tab-content" class="span12" data-spy="scroll"
					data-target=".navbar">

					<div id="main-btn-plus-Modal" class="modal hide fade" tabindex="-1"
						role="dialog" aria-hidden="true" data-backdrop="false"> <%@ include
							file="frame/modal_createfolder.jsp"%></div>
							
					<div id="uploaderbox-modal" class="modal hide fade" tabindex="-1"
						role="dialog" data-backdrop="false">
						<%@ include file="frame/modal_uploader.jsp"%></div>

					<div id="uploaderboxPart-modal" class="modal hide fade"
						tabindex="-1" role="dialog" data-backdrop="false"><%@ include
								file="frame/modal_uploadermin.jsp"%></div>


						<!-- Right click  -->
						<div class="context-menu" id="main-sys-menu">
							<ul>
								<li id="menu-fold-new"><img
									src="./image/page_white_add.png" />新建文件夹</li>
								<li id="menu-fold-copy"><img
									src="./image/page_white_copy.png" />复制</li>
								<li id="menu-fold-paste"><img
									src="./image/page_white_copy.png" />粘贴</li>
								<li id="menu-fold-cut"><img
									src="./image/page_white_cut.png" />剪切</li>
							</ul>
						</div>
						<!-- end of main-sys-menu Right click  -->

						<div id="main_tab_pane" class="tab-content">
							<div class="tab-pane" id="main-pane-all"><%@ include
									file="frame/tab_all.jsp"%></div>
							<div class="tab-pane" id="main-pane-video"><%@ include
									file="frame/tab_video.jsp"%></div>
							<div class="tab-pane" id="main-pane-image"><%@ include
									file="frame/tab_image.jsp"%></div>
							<div class="tab-pane" id="main-pane-music">music</div>
							<div class="tab-pane" id="main-pane-document">document</div>
							<div class="tab-pane" id="main-pane-others">others</div>
							<div class="tab-pane" id="main-pane-center">center</div>
							<div class="tab-pane" id="main-pane-safebox">safebox</div>
							<div class="tab-pane" id="main-pane-trash">trash</div>
							<div class="tab-pane" id="main-pane-sharegroup">sharegroup</div>
							<div class="tab-pane" id="main-pane-hotres">hotresource</div>
						</div>
						<!-- end of main-tab-pane -->
					</div>
					<!-- end of main-tab-content -->
				</div>
				<!-- end main-content -->
			</div>
			<!-- end of row-fluid -->
		</div>
		<!-- end of container-fluid -->

		<script src="./js/lib/jquery-1.10.2.min.js"></script>
		<script src="./js/lib/bootstrap.min.js"></script>
		<script src="./js/lib/jquery.contextmenu.r2.js"></script>
		<script src="./js/lang/cn.js"></script>
		<script src="./js/lib/sea.js"></script>
		<script>
			// Set configuration
			seajs.config({
				base : "./js/lib/"
			});

			seajs.use([ './js/main' ], function(main) {

			});
		</script>
</body>
</html>