<?php

/*
 * title: get_image.php
 * description: 创建图片缩略图或对视频进行截图
 * @author: Hetao wang (wanghetaotao@gmail.com)
 * @date: Oct, 2013
 * @version: 1.0
 */ 

	require_once "phpthumb-latest/ThumbLib.inc.php";
	$specification = array('icon'=>50, 'thumb'=>100, 'small'=>500,       
	'normal'=>800, 'large'=>1200);                            //定义icon、thumb、small、normal、large对应的缩放规格
	//$mov_format = array("rmvb", "mkv", "mpeg", "mpg");          //服务器支持的视频文件格式
	$movies_path = "Data/movies/";                  //视频的保存路径
	$thumbnails_path = "Data/thumbnails/";             //缩略图保存路径
	$images_path = "Data/images/";                //原始图片保存路径
		
	header("name: $name");
	header("Cache-Control: max-age=3600");
	
	function needSave($img, $sz) {
	global $specification;
	$imgDimensions = $img->getCurrentDimensions();
	if($imgDimensions['width'] > $specification[$sz] || $imgDimensions['height'] > $specification[$sz])
		return TRUE;
	else
		return FALSE;
	}
	
	$fid = $_GET['id'];
	//$fname = "Data/images/". $fid;
	$size = $_GET['size'];
	$name = $_GET['name'];
	$ftype = $_GET['type'];                    //指定文件的类型

	// $size默认为 “thumb”
	if($size == NULL)
		$size = 'thumb';
	// $name为空时赋值为$fid
	if($name == NULL)
		$name = $fid;
	// $ftype 默认为图片类型, 其实$ftype是必须的，否则无法判断文件的类型
	if($ftype == NULL)
		$ftype = "img";
	
	if($ftype == "mov") {
		$movie_name = $movies_path. $fid;
		$finfo = pathinfo($fid);
		$fname = $images_path. $finfo['filename']. "_xxmovxx.jpg";
		if(!file_exists($movie_name))                //判断原始视频文件是否存在
			die("file $fid does not exist");
		if(!file_exists($fname)) {
			// 文件名中包含特殊字符，需要转义之后才能传递到shell中
			$file_prefix_sh = $finfo['filename'];
			$file_prefix_sh = str_replace(" ", "\ ", $file_prefix_sh);
			$file_prefix_sh = str_replace("(", "\(", $file_prefix_sh);
			$file_prefix_sh = str_replace(")", "\)", $file_prefix_sh);
			$file_prefix_sh = str_replace("'", "\'", $file_prefix_sh);
			$file_prefix_sh = str_replace('"', '\"', $file_prefix_sh);
			$movname_sh = $movies_path. $file_prefix_sh. ".". $finfo['extension'];
			$fname_sh = $images_path. $file_prefix_sh. "_xxmovxx.jpg";
			$duration = exec("ffmpeg -i $movname_sh 2>&1 | grep -n 'Duration'| awk '{print substr($3,1,11) }' ");
			//echo $duration;
			$times = explode(":", $duration);
			foreach($times as & $t) {                                 // 在视频总时长的一半处截图
				settype($t, "integer");
				$t = (int)($t/2);
			}
			//print_r($times);
			
			$cmd = "ffmpeg -i $movname_sh -f image2 -ss ". implode(":", $times). " -y $fname_sh 2>&1";
			//echo $cmd;
			exec($cmd);
		}
	}
	if($ftype == "img") {
		$fname = $images_path. $fid;
		if(! file_exists($fname))
			die("file $fid does not exist");
	}
	if($size != 'raw') {
		$finfo = pathinfo($fname);
		$fname_t = $thumbnails_path. $finfo['filename']. "_$size". ".jpg";
		
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
	
	$image->show();
?>





