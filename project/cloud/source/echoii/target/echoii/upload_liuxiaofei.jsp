<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WANGPAN</title>
<link rel="stylesheet" href="css/lib/bootstrap.min.css" type="text/css"
	media="screen">
<link rel="stylesheet" href="css/style.css" type="text/css">
</head>

<style type="text/css">
#myModal {
	position: fixed;
	left: 70%;
	bottom: 0px;
}

#myModalPart {
	position: fixed;
	left: 70%;
	bottom: 0px;
}

.modal {
	position: fixed;
	top: 10%;
	left: 50%;
	z-index: 1050;
	width: 500px;
	margin-left: -280px;
	background-color: #fff;
	border: 1px solid #999;
	border: 1px solid rgba(0, 0, 0, 0.3);
	*border: 1px solid #999;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	outline: 0;
	-webkit-box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
	-moz-box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
	box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
	-webkit-background-clip: padding-box;
	-moz-background-clip: padding-box;
	background-clip: padding-box
}

.modal-header-upload {
	padding: 4px 15px;
	/*liuxiaofei*/
	background-color: #2F4F4F;
	/*liuxiaofei*/
	border-bottom: 1px solid #eeel
}

.modal-body-upload {
	position: fixed;
	padding: 15px;
	overflow-y: auto;
}
</style>

<body>

	<!-- Button to trigger modal -->
	<input type="file" class="button" onchange="upload_dialog()" />

	<!-- <!-- 	<a href="#myModal" role="button" class="btn" data-toggle="modal"
		data-keyboard="false">上传</a> -->

	<!-- 	<a role="button" class="btn" data-toggle="modal"
		data-keyboard="false" onchange="upload_dialog()">上传</a> -->
	-->
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />


	<!-- Modal -->

	<!-- <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
		data-backdrop="false"> -->
	<div id="myModal" class="modal hide fade in uploader" data-backdrop="false"
		role="dialog" tabindex="-1"
		style="display: none; bottom: 0px; height: 340px; top: 300px; margin-left: 0px; right: 90px; left: 680px; margin-bottom: 0px; margin-top: 0px; border-top-width: 0px; padding-bottom: 0px; border-bottom-width: 0px;"
		aria-hidden="false">
		<div class="modal-header-upload " align="right">
			<a id="part" href="#" role="button" class="btn" data-toggle="modal">-</a>
			<a href="#myModal" role="button" class="btn" data-toggle="modal">x</a>
		</div>
		<div id="mbody" class="modal-body-upload ">
			<p>body</p>
		</div>
	</div>

	<div aria-hidden="false"
		style=" bottom: 0px; height: 340px; top: 548px; margin-left: 0px; right: 90px; left: 680px; margin-bottom: 0px; margin-top: 0px; border-top-width: 0px; padding-bottom: 0px; border-bottom-width: 0px; display: none;"
		tabindex="-1" role="dialog" data-backdrop="false"
		class="modal hide fade in" id="myModalPart">
		<div align="right" class="modal-header-upload ">
			<a data-toggle="modal" class="btn" role="button" href="#" id="all">+</a>
			<a data-toggle="modal" class="btn" role="button" href="#myModalPart">x</a>
		</div>
	</div>

	<script src="./js/lib/jquery-1.10.2.min.js"></script>
	<script src="./js/lib/bootstrap.min.js"></script>
	<script src="./js/lib/jquery.contextmenu.r2.js"></script>
	<script src="./js/lang/cn.js"></script>
	<script src="./js/lib/sea.js"></script>

	<script style="text/javascript">
		$(function() {
			$("#part").click(function() {
				$("#myModal").modal('hide');
				$("#myModalPart").modal('show');
			})
			$("#all").click(function() {
				$("#myModalPart").modal('hide');
				$("#myModal").modal('show');
			})
		})
	</script>

	<script style="text/javascript">
		function upload_dialog() {
			$("#myModal").modal('show');
		}
	</script>

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