var obj = document.getElementById("ADST2200AX");

function setServerName(card) {
	var host = $("#new-server-fqdn").val();
	var name = $("#new-server-name").val();
	var displayName = host;

	if (name) {
		displayName = name + " (" + host + ")";
	}
	;

	card.wizard.setSubtitle(displayName);
	card.wizard.el.find(".create-server-name").text(displayName);
}

function delRow(obj, prefix) {
	var id = $(prefix + "_id");
	var delFlag = $(prefix + "_delFlag");
	if (id.val() == "") {
		$(obj).parent().parent().remove();
	} else if (delFlag.val() == "0") {
		delFlag.val("1");
		$(obj).html("&divide;").attr("title", "撤销删除");
		$(obj).parent().parent().addClass("error");
	} else if (delFlag.val() == "1") {
		delFlag.val("0");
		$(obj).html("&times;").attr("title", "删除");
		$(obj).parent().parent().removeClass("error");
	}
}

function displaymessage3() {
	
	document.getElementById("fingerCodeType").innerText ="MAIN";//用来区分是主指纹还是备份指纹
	var ADST2200AX = document.getElementById("ADST2200AX");
	
	var stringFpno = getFingerNum();
	var s = stringFpno;
	var spfpno = Number(s);
	var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno);
	if(w_nRetTmp){
		ADST2200AX.ADST2200AX_RemoveTemplate(spfpno);
	}
	var w_nRetone = ADST2200AX.ADST2200AX_Enroll(spfpno);
	if(w_nRetone==-5){
		alert("此指纹号存在模板！");
		return;
	}
	if(w_nRetone!=0){
		document.getElementById("fingerCodeOne").innerText ="";
		alert("注册指纹失败");
	}
}

function displaymessageSuccess(type){
	var stringFpno = getFingerNum();
	var s = stringFpno;
	var spfpno = Number(s);
	var ADST2200AX = document.getElementById("ADST2200AX");
	if(type=='MAIN'){
		var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno);
		document.getElementById("fingerCodeOne").innerText =w_nRetTmp;
	}else if(type=='COERCE'){
		var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno+499);
		document.getElementById("fingerCodeThree").innerText =w_nRetTmp;
	}else{
		var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno+500);
		document.getElementById("fingerCodeTwo").innerText =w_nRetTmp;
	}
}

function displaymessage4() {
	document.getElementById("fingerCodeType").innerText ="";
	var ADST2200AX = document.getElementById("ADST2200AX");
	
	var stringFpno = getFingerNum();
	var s = stringFpno;
	var spfpno = Number(s)+500;
	//因为存储在其他设备  避免指纹号采集时候冲突 先删除 在采集
	var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno);
	if(w_nRetTmp){
		ADST2200AX.ADST2200AX_RemoveTemplate(spfpno);
	}
	var w_nRetone = ADST2200AX.ADST2200AX_Enroll(spfpno);
	if(w_nRetone==-5){
		alert("此指纹号存在模板！");
		return;
	}
	if(w_nRetone!=0){
		document.getElementById("fingerCodeTwo").innerText ="";
		alert("注册指纹失败");
	}
}

function displaymessage5() {
	document.getElementById("fingerCodeType").innerText ="COERCE";
	var ADST2200AX = document.getElementById("ADST2200AX");

	var stringFpno = getFingerNum();
	var s = stringFpno;
	var spfpno = Number(s)+499;
	//因为存储在其他设备  避免指纹号采集时候冲突 先删除 在采集
	var w_nRetTmp = ADST2200AX.ADST2200AX_GetTemplateData(spfpno);
	if(w_nRetTmp){
		ADST2200AX.ADST2200AX_RemoveTemplate(spfpno);
	}
	var w_nRetone = ADST2200AX.ADST2200AX_Enroll(spfpno);
	if(w_nRetone==-5){
		alert("此指纹号存在模板！");
		return;
	}
	if(w_nRetone!=0){
		document.getElementById("fingerCodeThree").innerText ="";
		alert("注册指纹失败");
	}
}

function Addfp() {
	var spDeviceType = 2;
	var spComPort = 1;
	var spBaudRate = 6;
	var stringFpno = getFingerNum();
	var s = stringFpno;
	var spfpno = Number(s);
	var spfpchar = document.getElementById("fingerCodeOne").value;

	var spfpnoBF = spfpno + 500 + "";
	var spfpcharBF = document.getElementById("fingerCodeTwo").value;

	if (spfpchar == null || spfpchar.length == 0 || spfpchar == "") {
		alert("指纹模板数据不能为空");
	} else {
		var obj = document.getElementById("ZAZFingerActivex");
		obj.spDeviceType = spDeviceType;
		obj.spComPort = spComPort;
		obj.spBaudRate = spBaudRate;
		var spResult = "";
		if(typeof obj.ZAZADDFinger == 'undefined'){
			alert("请使用IE浏览器，并且正确安装ZAZ控件");
			return;
		}
		spResult = obj.ZAZADDFinger(spfpno, spfpchar);
		var spResultBF = "";
		spResultBF = obj.ZAZADDFinger(spfpnoBF, spfpcharBF);
		if (spResult == "0" && spResultBF == "0") {
			alert("注册成功");
		}
	}
}

function Delfp() {
	var ADST2200AX = document.getElementById("ADST2200AX");
	var stringFpno = getFingerNum();
	var spfpno = Number(stringFpno);
	var spfpnoBF = spfpno + 500;
	
	var ret_Del;
	var ret_DelBF;
	var spfpno_Data = ADST2200AX.ADST2200AX_GetTemplateData(spfpno);//判断指纹是否存在  不存在 执行删除 会报错
	if(spfpno_Data){
		 ret_Del = ADST2200AX.ADST2200AX_RemoveTemplate(spfpno);
	}
	var spfpnoBF_Data = ADST2200AX.ADST2200AX_GetTemplateData(spfpnoBF);
	if(spfpnoBF_Data){
		 ret_DelBF = ADST2200AX.ADST2200AX_RemoveTemplate(spfpnoBF);
	}
	if(ret_Del==0||ret_DelBF==0){
		alert("已删除设备指纹");
	}else{
		alert("设备中无此人指纹信息！");
	}
}

function SearchFp() {
	var ADST2200AX = document.getElementById("ADST2200AX");
	 textToSpeach("请按下手指");
	 document.getElementById('fingerMsg').innerHTML = "请按下手指";
	var w_nRet = ADST2200AX.ADST2200AX_IdentifyFree();
}

function validateName(el) {
	var name = el.val();
	var retValue = {};
	if (name == "") {
		retValue.status = false;
		retValue.msg = "姓名不能为空!";
	} else if (name.length > 32) {
		retValue.status = false;
		retValue.msg = "长度不能大于32!";
	} else {
		retValue.status = true;
	}
	return retValue;
}

function validateIdentify(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "证件号码不能为空!";
	} else if (datas.length > 20) {
		retValue.status = false;
		retValue.msg = "长度不能大于20!";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateCompanyId(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "所属机构不能为空!";
	} else if (isNaN(datas)) {
		retValue.status = false;
		retValue.msg = "请输入数字！";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateWorkNum(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "工号不能为空!";
	} else if (datas.length > 10) {
		retValue.status = false;
		retValue.msg = "长度不能大于10!";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateFingerNum(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "指纹号不能为空!";
	} else if (isNaN(datas)) {
		retValue.status = false;
		retValue.msg = "请输入数字！";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateStartDate(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "起始时间不能为空!";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateEndDate(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "截止时间不能为空!";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function validateAreaId(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "设备ID不能为空!";
	} else if (isNaN(datas)) {
		retValue.status = false;
		retValue.msg = "请输入数字！";
	} else {
		retValue.status = true;
	}
	return retValue;
}

function validateRowName(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "家庭成员名称不能为空!";
	} else {
		retValue.status = true;
	}
	return retValue;
}

function validateWorkName(data) {
	var datas = data.val();
	var retValue = {};
	if (datas == "") {
		retValue.status = false;
		retValue.msg = "工作单位不能为空!";
	} else {
		retValue.status = true;
	}
	return retValue;
}
function edit(url) {
	$
			.ajax({
				type : "GET",
				url : url
			})
			.done(
					function(response) {
						var data = JSON.parse(response);
						if (typeof (data.id) != 'undefined') {

							$("#officeButton").addClass("disabled");
							$("#areaButton").addClass("disabled");

							$("#staffImageList").children().remove();
							$("#staffExFamilyList").children().remove();
							$("#staffExWorkList").children().remove();
							wizard.reset();
							$("h3.wizard-title").text('修改人员');
							$("li.wizard-nav-item").addClass('already-visited');

							if (typeof (data.staffExFamilyList) != 'undefined'
									&& typeof (data.staffExFamilyList.length) != 'undefined') {
								for (var i = 0; i < data.staffExFamilyList.length; i++) {
									addRow('#staffExFamilyList',
											staffExFamilyRowIdx,
											staffExFamilyTpl);
									staffExFamilyRowIdx = staffExFamilyRowIdx + 1;
								}

							}
							if (typeof (data.staffImageList) != 'undefined'
									&& typeof (data.staffImageList.length) != 'undefined') {
								for (var i = 0; i < data.staffImageList.length; i++) {
									addRow('#staffImageList', staffImageRowIdx,
											staffImageTpl);
									staffImageRowIdx = staffImageRowIdx + 1;
								}
							}
							if (typeof (data.staffExWorkList) != 'undefined'
									&& typeof (data.staffExWorkList.length) != 'undefined') {
								for (var i = 0; i < data.staffExWorkList.length; i++) {
									addRow('#staffExWorkList',
											staffExWorkRowIdx, staffExWorkTpl);
									staffExWorkRowIdx = staffExWorkRowIdx + 1;
								}
							}

							loadData(data);
							$("#fingerCode3")
									.val(atob($("#fingerCode3").val()));
							$("#fingerCodeTwo").val(
									atob($("#fingerCodeTwo").val()));
							wizard.show();
						}

					}).fail(function() {

			});
}
function loadData(jsonStr) {

	var obj = eval(jsonStr);
	var key, value, tagName, type, arr;
	for (x in obj) {
		key = x;
		value = obj[x];
		if (typeof (value) == 'object') {
			loadList(key, value);
		} else {
			$(
					"form.form-horizontal [name='" + key + "'],[name='" + key
							+ "[]']").each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');
				if (tagName == 'INPUT') {
					if (type == 'radio') {
						$(this).attr('checked', $(this).val() == value);
					} else if (type == 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() == arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$(this).val(value);
					}
				} else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
					$(this).val(value);
				}

			});
		}

	}
}

function loadList(listName, list) {
	var key, value, tagName, type, arr;

	if (list instanceof Array) {
		for (pos in list) {
			for (x in list[pos]) {
				key = x;
				value = list[pos][x];
				if (typeof (value) == 'object') {
					loadList(key, value);
				} else {
					if (key == "imagePath") {
						$("#" + listName + pos + "_" + key + "Preview")
								.children().remove();
						var li, urls = value.split("|");
						for (var i = 0; i < urls.length; i++) {
							if (urls[i] != "") {// <c:if test="${type eq
								// 'thumb' || type eq
								// 'images'}">
								li = "<li><img src=\""
										+ urls[i]
										+ "\" url=\""
										+ urls[i]
										+ "\" style=\"max-width: 100px;max-height:100px;_height:100px;border:0;padding:3px;\">";// </c:if><c:if
								// test="${type
								// ne
								// 'thumb'
								// &&
								// type
								// ne
								// 'images'}">
								// li += "<li><a href=\""+urls[i]+"\"
								// url=\""+urls[i]+"\"
								// target=\"_blank\">"+decodeURIComponent(urls[i].substring(urls[i].lastIndexOf("/")+1))+"</a>";//</c:if>
								li += "&nbsp;&nbsp;</li>";
								$("#" + listName + pos + "_" + key + "Preview")
										.append(li);
							}
						}
						if ($("#" + listName + pos + "_" + key + "Preview")
								.text() == "") {
							$("#" + listName + pos + "_" + key + "Preview")
									.html(
											"<li style='list-style:none;padding-top:5px;'>无</li>");
						}

					}

					$(
							"form.form-horizontal [name='" + listName + "["
									+ pos + "]." + key + "']").each(
							function() {
								tagName = $(this)[0].tagName;
								type = $(this).attr('type');
								if (tagName == 'INPUT') {
									if (type == 'radio') {
										$(this).attr('checked',
												$(this).val() == value);
									} else if (type == 'checkbox') {
										arr = value.split(',');
										for (var i = 0; i < arr.length; i++) {
											if ($(this).val() == arr[i]) {
												$(this).attr('checked', true);
												break;
											}
										}
									} else {
										$(this).val(value);
									}
								} else if (tagName == 'SELECT'
										|| tagName == 'TEXTAREA') {
									$(this).val(value);
								}

							});
				}
			}
		}
	} else {
		for (x in list) {
			key = x;
			value = list[x];

			$("form.form-horizontal [name='" + listName + "." + key + "']")
					.each(
							function() {
								tagName = $(this)[0].tagName;
								type = $(this).attr('type');
								if (tagName == 'INPUT') {
									if (type == 'radio') {
										$(this).attr('checked',
												$(this).val() == value);
									} else if (type == 'checkbox') {
										arr = value.split(',');
										for (var i = 0; i < arr.length; i++) {
											if ($(this).val() == arr[i]) {
												$(this).attr('checked', true);
												break;
											}
										}
									} else {
										$(this).val(value);
									}
								} else if (tagName == 'SELECT'
										|| tagName == 'TEXTAREA') {
									$(this).val(value);
								}

							});
		}
	}

}

var idCardNoUtil = {
		provinceAndCitys: {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",
		31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",
		45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
		65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"},

		powers: ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"],

		parityBit: ["1","0","X","9","8","7","6","5","4","3","2"],

		genders: {male:"男",female:"女"},

		checkAddressCode: function(addressCode){
		var check = /^[1-9]\d{5}$/.test(addressCode);
		if(!check) return false;
		if(idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,2))]){
		return true;
		}else{
		return false;
		}
		},

		checkBirthDayCode: function(birDayCode){
		var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
		if(!check) return false;
		var yyyy = parseInt(birDayCode.substring(0,4),10);
		var mm = parseInt(birDayCode.substring(4,6),10);
		var dd = parseInt(birDayCode.substring(6),10);
		var xdata = new Date(yyyy,mm-1,dd);
		if(xdata > new Date()){
		return false;//生日不能大于当前日期
		}else if ( ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) ){
		return true;
		}else{
		return false;
		}
		},

		getParityBit: function(idCardNo){
		var id17 = idCardNo.substring(0,17);
		var power = 0;
		for(var i=0;i<17;i++){
		power += parseInt(id17.charAt(i),10) * parseInt(idCardNoUtil.powers[i]);
		}
		var mod = power % 11;
		return idCardNoUtil.parityBit[mod];
		},

		checkParityBit: function(idCardNo){
		var parityBit = idCardNo.charAt(17).toUpperCase();
		if(idCardNoUtil.getParityBit(idCardNo) == parityBit){
		return true;
		}else{
		return false;
		}
		},

		checkIdCardNo: function(idCardNo){
		//15位和18位身份证号码的基本校验
		var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
		if(!check) return false;
		//判断长度为15位或18位
		if(idCardNo.length==15){
		return idCardNoUtil.check15IdCardNo(idCardNo);
		}else if(idCardNo.length==18){
		return idCardNoUtil.check18IdCardNo(idCardNo);
		}else{
		return false;
		}
		},
		//校验15位的身份证号码
		check15IdCardNo: function(idCardNo){
		//15位身份证号码的基本校验
		var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
		if(!check) return false;
		//校验地址码
		var addressCode = idCardNo.substring(0,6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if(!check) return false;
		var birDayCode = '19' + idCardNo.substring(6,12);
		//校验日期码
		return idCardNoUtil.checkBirthDayCode(birDayCode);
		},
		//校验18位的身份证号码
		check18IdCardNo: function(idCardNo){
		//18位身份证号码的基本格式校验
		var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
		if(!check) return false;
		//校验地址码
		var addressCode = idCardNo.substring(0,6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if(!check) return false;
		//校验日期码
		var birDayCode = idCardNo.substring(6,14);
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if(!check) return false;
		//验证校检码
		return idCardNoUtil.checkParityBit(idCardNo);
		},
		formateDateCN: function(day){
		var yyyy =day.substring(0,4);
		var mm = day.substring(4,6);
		var dd = day.substring(6);
		return yyyy + '-' + mm +'-' + dd;
		},
		//获取信息
		getIdCardInfo: function(idCardNo){
		var idCardInfo = {
		gender:"", //性别
		birthday:"" // 出生日期(yyyy-mm-dd)
		};
		if(idCardNo.length==15){
		var aday = '19' + idCardNo.substring(6,12);
		idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
		if(parseInt(idCardNo.charAt(14))%2==0){
		idCardInfo.gender=idCardNoUtil.genders.female;
		}else{
		idCardInfo.gender=idCardNoUtil.genders.male;
		}
		}else if(idCardNo.length==18){
		var aday = idCardNo.substring(6,14);
		idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
		if(parseInt(idCardNo.charAt(16))%2==0){
		idCardInfo.gender=idCardNoUtil.genders.female;
		}else{
		idCardInfo.gender=idCardNoUtil.genders.male;
		}
		}
		return idCardInfo;
		},

		getId15:function(idCardNo){
		if(idCardNo.length==15){
		return idCardNo;
		}else if(idCardNo.length==18){
		return idCardNo.substring(0,6) + idCardNo.substring(8,17);
		}else{
		return null;
		}
		},

		getId18: function(idCardNo){
		if(idCardNo.length==15){
		var id17 = idCardNo.substring(0,6) + '19' + idCardNo.substring(6);
		var parityBit = idCardNoUtil.getParityBit(id17);
		return id17 + parityBit;
		}else if(idCardNo.length==18){
		return idCardNo;
		}else{
		return null;
		}
		}
		};
		//验证护照是否正确
		function checknumber(number){
		var str=number;
		//在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
		var Expression=/(P\d{7})|(G\d{8})/;
		var objExp=new RegExp(Expression);
		if(objExp.test(str)==true){
		   return true;
		}else{
		   return false;
		} 
		};

