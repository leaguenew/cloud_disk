define(function(require, exports) {

	// get cookie： fnCookie("cookie_name")
	// set cookie: fnCookie("cookie_name", "value");
	exports.fnCookie = fnCookie;

	exports.fnGetCookies = fnGetCookies;
	exports.fnGetDefaultPara = fnGetCookies;
	exports.fnRedirect = fnRedirect;
	exports.fnFormatFileSize = fnFormatFileSize;

	exports.fnValidateFolderName = fnValidateFolderName;

	exports.fnStatus = fnStatus;
	exports.fnUrl = fnUrl;
	exports.fnCurrentTimeString = fnCurrentTimeString;
	
	exports.fnCapacity= fnCapacity;

	var URL = {
		package_download : "service/0/download/package_download",
		download : "service/0/download",
		capacity : "service/0/user/userdetail",
		//videoServer : "http://172.21.7.199:81/echoii/media/video/",
		videoServer : "media/video/",
		imageServer: "http://210.75.252.180:7080/echoii/media/image/get_image2",
		musicServer:"service/0/media/",
		share_download : "service/0/download/public"
	};

	function fnUrl(key) {
		return URL[key];
	}

	var STATUS = {
		OK : "200",
		PARA_ERROR : "400",
		AUTH_ERROR : "401",
		NAME_DUP : "409",
		SYS_ERROR : "500",
		OTHER_ERROR : "444",
		FILE_ERROR : "445",
		DEVICE_ERROR : "446",
		DEVICE_ID_NOTFOUND :"404"
	};

	function fnStatus(key) {
		return STATUS[key];
	}

	function fnValidateFolderName(name) {
		if (!name) {
			return false;
		}

		return true;
	}

	function fnGetCookies() {
		var result = {
			user_id : fnCookie("uid"),
			token : fnCookie("token"),
			size : 40,
			order : "desc",
			order_by : "lmf_date"
		};
		return result;
	}
	;

	function fnCookie(key, value, options) {

		// key and at least value given, set cookie...
		if (arguments.length > 1
				&& (!/Object/.test(Object.prototype.toString.call(value))
						|| value === null || value === undefined)) {
			options = $.extend({}, options);

			if (value === null || value === undefined) {
				options.expires = -1;
			}

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setDate(t.getDate() + days);
			}

			value = String(value);

			return (document.cookie = [
					encodeURIComponent(key),
					'=',
					options.raw ? value : encodeURIComponent(value),
					options.expires ? '; expires='
							+ options.expires.toUTCString() : '', // use
					// expires
					// attribute,
					// max-age
					// is not
					// supported
					// by IE
					options.path ? '; path=' + options.path : '',
					options.domain ? '; domain=' + options.domain : '',
					options.secure ? '; secure' : '' ].join(''));
		}

		// key and possibly options given, get cookie...
		options = value || {};
		var decode = options.raw ? function(s) {
			return s;
		} : decodeURIComponent;

		var pairs = document.cookie.split('; ');
		for ( var i = 0, pair; pair = pairs[i] && pairs[i].split('='); i++) {
			if (decode(pair[0]) === key)
				return decode(pair[1] || ''); // IE saves cookies with empty
			// string as "c; ", e.g. without
			// "=" as opposed to EOMB, thus
			// pair[1] may be undefined
		}
		return null;
	}
	;

	function fnRedirect(url, currentUrl) {
		var total = arguments.length;
		if (total == 1) {
			location.href = arguments[0];
		} else {
			if (total == 2) {
				location.href = arguments[0] ;//+ "?source_url=" + arguments[1];
			} else {
				console.log("fnRedirect 参数错误");
			}
		}

	}
	;

	function fnFormatFileSize(size) {
		if (size > 1024 * 1024) {
			size = (Math.round(size * 100 / (1024 * 1024)) / 100).toString()
					+ 'MB';
		} else {
			size = (Math.round(size * 100 / 1024) / 100).toString() + 'KB';
		}
		return size;
	}
	;

	function fnCurrentTimeString() {
		var d = new Date();
		return d.getFullYear().toString() + d.getMonth().toString()
				+ d.getDate().toString() + d.getHours().toString()
				+ d.getMinutes().toString() + d.getSeconds().toString();

	}

	function fnCapacity(callback) {
		var data = {
			user_id : fnCookie("uid"),
			token : fnCookie("token")
		};
		$.get(fnUrl("capacity"), data, callback);

	}

});