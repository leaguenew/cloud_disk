define(function(require, exports) {
	// /////////////////全局变量////////////////////
	var song;
	exports.playMusic = toPlay;
	exports.pauseMusic = toPause;

	// ////////////////toPlay/////////////////////////////
	function toPlay(musicName, musicLink) {

		var songName = document.getElementById("songName"), musicBox = document
				.getElementById("musicBox");

		songName.innerHTML = musicName;
		musicBox.setAttribute("src", musicLink);
		musicBox.play();
	}

	function toPause() {
		musicBox = document.getElementById("musicBox");
		musicBox.pause();
	}

});

// html5里有一个新标签audio，该标签用以定义声音，比如音乐或其他音频流。
// 既然audio标签可以播放音频，那我们可以轻而易举的用其来打造一个音乐播放器。
// audio 有几个属性：
// src:String型，所播放音频的 url。
// autoplay:bool，如果是 true，则音频在就绪后马上播放。默认为false。
// controls:bool，如果是 true，则向用户显示控件，比如播放按钮。默认为false。
// 更多详细属性：http://www.w3school.com.cn/html5/html5_audio.asp

// audio 有几个事件：
// onended:当媒介已抵达结尾时运行脚本，也就是当前歌曲播放完了，这里的“媒介”是指audio标签。
// onloadstart:当浏览器开始加载媒介数据时运行脚本。
// onplay:当媒介数据将要开始播放时运行脚本。这里的“媒介数据”是指播放的文件。
// onplaying:当媒介数据已开始播放时运行脚本。
// onpause:当媒介数据暂停时运行脚本。
// onerror:当加载媒介数据出错时运行的脚本。(w3school不是这样解释的)
// 更多详细事件：http://www.w3school.com.cn/html5/html5_ref_eventattributes.asp#Media_Events
