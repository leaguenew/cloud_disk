<?php
	require_once "phpthumb-latest/ThumbLib.inc.php";
	$specification = array('icon'=>50, 'thumb'=>100, 'small'=>500,       
	'normal'=>800, 'large'=>1200);                            //定义icon、thumb、small、normal、large对应的缩放规格
	
	function needSave($img, $sz) {
	global $specification;
	$imgDimensions = $img->getCurrentDimensions();
	if($imgDimensions['width'] > $specification[$sz] || $imgDimensions['height'] > $specification[$sz])
		return TRUE;
	else
		return FALSE;
	}
	
	$fid = $_GET['id'];
	$fname = "images/". $fid;
	$size = $_GET['size'];
	$name = $_GET['name'];

	// $size默认为 “thumb”
	if($size == NULL)
		$size = 'thumb';
	// $name为空时赋值为$fid
	if($name == NULL)
		$name = $fid;
	$finfo = pathinfo($fname);
	if($size != 'raw')
		$fname_t = "images/thumbnails/". $finfo['filename']. "_$size". ".jpg";
	
	if(! file_exists($fname)) {
		echo "file $fname does not exist. <br />";
		exit -1;
	}
	
	$use_thumb = FALSE;         //指定缩略图是否存在
	if(file_exists($fname_t)) {
		$fname = $fname_t;
		$use_thumb = TRUE;
	}
	$image = PhpThumbFactory::create($fname);
	//原始图片不存在缩略图，保存其缩略图
	if(!$use_thumb) {
		if($size != 'raw') {
			$toSave = needSave($image, $size);
			$image->resize($specification[$size], $specification[$size]);
			if($toSave)
				$image->save($fname_t, "jpg");
		}
	}
	header("name: $name");
	header("Cache-Control: max-age=3600");
	$image->show();
?>