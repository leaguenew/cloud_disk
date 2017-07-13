<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<div id="header-top-nav" class="navbar navbar-inverse">
		 <div  class="navbar-inner" >
			<span class="span3" id="header-top-image" ><a href="main.jsp"><img src="./image/logo.gif" ></a></span>
				<ul id="header-menu" class="nav" >
					<li id="header-disk-capacity"  >
						<a id="increase-link">云盘空间</a>
						<div id="increase-progress" class="progress active progress-striped span2">
								<span class="bar" ></span>
								<span class="text"></span>
						</div>
					</li>
					<li class="dropdown" >
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">
							<span id="header-userid"></span><b class="caret"></b>
						</a>
						<ul id="dropdown-menu" class="dropdown-menu">
							<li id="header-userinfo"><a  href="#"><i class="icon-user"></i>&nbsp;&nbsp;修改个人信息</a></li>
							<li id=" "><a href="btdownload.jsp"><i class="icon-download"></i>&nbsp;&nbsp;离线下载</a></li>
						 	<li id="divider" class="divider"></li>
							<li><a id="header-logout"  href="#"><i class="icon-off"></i>&nbsp;&nbsp;退出</a></li>
						</ul>
					</li>
					
				</ul>
		</div>
</div>



  