$.validator.setDefaults({  
//    ignore: ".ignore",
    errorElement : 'span',
	errorClass : 'help-block',
	errorPlacement : function(error, element) {
//		element.next().remove();
		element.closest('.form-group').append(error);
	},
	highlight : function(element) {
		$(element).closest('.form-group').addClass('has-error has-feedback');
	},
	success : function(label) {
		var el=label.closest('.form-group').find("input");
//		el.next().remove();
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	}
});

$(function(){
	$("span.error").each(function(idx, elemt){
		$(this).addClass("help-block");
		$(this).closest('.form-group').addClass('has-error has-feedback');
	})
})


function IEVersion() {
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
	var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
	var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
	if(isIE) {
		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
		reIE.test(userAgent);
		var fIEVersion = parseFloat(RegExp["$1"]);
		if(fIEVersion == 7) {
			return 7;
		} else if(fIEVersion == 8) {
			return 8;
		} else if(fIEVersion == 9) {
			return 9;
		} else if(fIEVersion == 10) {
			return 10;
		} else {
			return 6;//IE版本<=7
		}   
	} else if(isEdge) {
		return 'edge';//edge
	} else if(isIE11) {
		return 11; //IE11  
	}else{
		return -1;//不是ie浏览器
	}
}

	
function textToSpeach(textToSpeach, speachRate) {
	try {
		speachRate = speachRate || 2;
		if(IEVersion() != -1){
			if(typeof voiceObj == 'undefined'){
				try {
					voiceObj = new ActiveXObject("Sapi.SpVoice");
				} catch (ex) {
					console.log("Speak Sapi.SpVoice init error "+ex);
				}
			}

			if(typeof voiceObj != 'undefined'){
				voiceObj.Rate=speachRate; //语速
				//voiceObj.Volume=60;
				voiceObj.Speak(textToSpeach,1);
				return true;
			}
		}

		if(typeof speechSU == 'undefined'){
			try {
				speechSU = new SpeechSynthesisUtterance();
			} catch (ex) {
				console.log("Speak SpeechSynthesisUtterance init error "+ex);
			}
		}
		if(typeof speechSU != 'undefined'){
			speechSU.text = textToSpeach;
			speechSU.lang = 'zh-CN';
			speechSU.rate =speachRate;
			window.speechSynthesis.speak(speechSU);
			return true;
		}
	}catch (e) {
		return true;
	}
}

String.prototype.format = function() {  
    if(arguments.length == 0) return this;  
    var obj = arguments[0];  
    var s = this;  
    for(var key in obj) {  
        s = s.replace(new RegExp("\\{" + key + "\\}", "g"), obj[key]);  
    }  
    return s;  
}  


var logoutTipShow = false;
$.ajaxSetup({
    complete:function(XMLHttpRequest,textStatus){  
	    //通过XMLHttpRequest取得响应头，sessionstatus，  
	    var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");   
	    if(sessionstatus=="timeout"){  
	    	 var rootPath=XMLHttpRequest.getResponseHeader("rootPath"); 
	    	 if(!logoutTipShow){
	    		 logoutTipShow = true; 
	    		 alert("未登录或者登录超时，请重新登录");
	    	 }
	         //如果超时就处理 ，指定要跳转的页面  
	         top.location = rootPath+"/logout";
	    }  
    } 
});