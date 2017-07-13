<html>

<body>
	<h3>upload test</h3>

	<input  id="new-file-input"  type="file"  mutiple></input>
	<br>
	<input type="text" name="user_id"></input>
	<button type="submit" onclick="upload()">upload</button>

	<script>
		function upload() {

			console.log("begin upload");

			var formData = new FormData();
			var file = document.getElementById("new-file-input").files[0];
			if (!file) {
				console.log("no file");
				return;
			}
			formData.append('upload', file);
			//formData.append('user_id', "sdfsdfsdfs");
			//formData.append('user_id','965927c07d60ba9cb18b35821a1abb3c');
			formData.append('user_id','965927c07d60ba9cb18b35821a1abb3c');
			formData.append('token', "gggg");
			formData.append('parent_id', "root");

			var xhr = new XMLHttpRequest();
			xhr.open('POST', "upload");

			xhr.onload = function() {
				if (xhr.status === 200) {
					console.log("success");
				} else {
					console.log("error");
				}
			}
			xhr.send( formData );
		}
	</script>
</body>

</html>