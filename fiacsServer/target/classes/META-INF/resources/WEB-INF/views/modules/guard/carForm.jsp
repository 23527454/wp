<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/custom/spectrum.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/docs.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/toc.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/fileinput.js'></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						
						var car_name = $("#car_name").val();
						var company_id = parent.$("#company_id").val();
						
						$("#selectedCompanyId").val(company_id);
						var company_shortName = parent.$("#company_shortName")
								.val();
						if (car_name == "" || typeof (car_name) == "undefined") {
							if (company_id != ""
									&& typeof (company_id) != "undefined") {
								$
										.ajax(
												{
													type : "GET",
													url : "${ctx}/guard/company/getArea?id="
															+ company_id
												})
										.done(
												function(response) {
													var data = JSON
															.parse(response);
													var data_areaId = data.areaId;
													$("#companyId").val(
															company_id);
													$("#companyName").val(
															company_shortName);
													$("#areaId").val(
															data_areaId);
												}).fail(function() {
										});
							}
						}

						if (document.all['radio_hidden'].value == '0') {
							document.all['radio'][0].checked = true;
						} else if (document.all['radio_hidden'].value == '1') {
							document.all['radio'][1].checked = true;
						} else {
							document.all['radio'][0].checked = true;
							$("#radio_hidden").val("0");
						}

						//$("#name").focus();
						$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});

						$("#full").spectrum(
								{
									allowEmpty : true,
									color : "${car.color}",
									showInput : true,
									containerClassName : "full-spectrum",
									showInitial : true,
									showPalette : true,
									showSelectionPalette : true,
									showAlpha : true,
									maxPaletteSize : 10,
									preferredFormat : "hex",
									localStorageKey : "spectrum.demo",
									move : function(color) {
										updateBorders(color);
									},
									show : function() {

									},
									beforeShow : function() {

									},
									hide : function(color) {
										updateBorders(color);
									},

									palette : [
											[ "rgb(0, 0, 0)",
													"rgb(67, 67, 67)",
													"rgb(102, 102, 102)", /*"rgb(153, 153, 153)","rgb(183, 183, 183)",*/
													"rgb(204, 204, 204)",
													"rgb(217, 217, 217)", /*"rgb(239, 239, 239)", "rgb(243, 243, 243)",*/
													"rgb(255, 255, 255)" ],
											[ "rgb(152, 0, 0)",
													"rgb(255, 0, 0)",
													"rgb(255, 153, 0)",
													"rgb(255, 255, 0)",
													"rgb(0, 255, 0)",
													"rgb(0, 255, 255)",
													"rgb(74, 134, 232)",
													"rgb(0, 0, 255)",
													"rgb(153, 0, 255)",
													"rgb(255, 0, 255)" ],
											[ "rgb(230, 184, 175)",
													"rgb(244, 204, 204)",
													"rgb(252, 229, 205)",
													"rgb(255, 242, 204)",
													"rgb(217, 234, 211)",
													"rgb(208, 224, 227)",
													"rgb(201, 218, 248)",
													"rgb(207, 226, 243)",
													"rgb(217, 210, 233)",
													"rgb(234, 209, 220)",
													"rgb(221, 126, 107)",
													"rgb(234, 153, 153)",
													"rgb(249, 203, 156)",
													"rgb(255, 229, 153)",
													"rgb(182, 215, 168)",
													"rgb(162, 196, 201)",
													"rgb(164, 194, 244)",
													"rgb(159, 197, 232)",
													"rgb(180, 167, 214)",
													"rgb(213, 166, 189)",
													"rgb(204, 65, 37)",
													"rgb(224, 102, 102)",
													"rgb(246, 178, 107)",
													"rgb(255, 217, 102)",
													"rgb(147, 196, 125)",
													"rgb(118, 165, 175)",
													"rgb(109, 158, 235)",
													"rgb(111, 168, 220)",
													"rgb(142, 124, 195)",
													"rgb(194, 123, 160)",
													"rgb(166, 28, 0)",
													"rgb(204, 0, 0)",
													"rgb(230, 145, 56)",
													"rgb(241, 194, 50)",
													"rgb(106, 168, 79)",
													"rgb(69, 129, 142)",
													"rgb(60, 120, 216)",
													"rgb(61, 133, 198)",
													"rgb(103, 78, 167)",
													"rgb(166, 77, 121)",
													/*"rgb(133, 32, 12)", "rgb(153, 0, 0)", "rgb(180, 95, 6)", "rgb(191, 144, 0)", "rgb(56, 118, 29)",
													"rgb(19, 79, 92)", "rgb(17, 85, 204)", "rgb(11, 83, 148)", "rgb(53, 28, 117)", "rgb(116, 27, 71)",*/
													"rgb(91, 15, 0)",
													"rgb(102, 0, 0)",
													"rgb(120, 63, 4)",
													"rgb(127, 96, 0)",
													"rgb(39, 78, 19)",
													"rgb(12, 52, 61)",
													"rgb(28, 69, 135)",
													"rgb(7, 55, 99)",
													"rgb(32, 18, 77)",
													"rgb(76, 17, 48)" ] ]
								});
					});

	function AreaTreeselectCallBack(id, v, h, f) {
		if (v == "ok") {
			if (id != "" && typeof (id) != "undefined") {
				$.ajax({
					type : "GET",
					url : "${ctx}/guard/company/getArea?id=" + id
				}).done(function(response) {
					var data = JSON.parse(response);
					var data_areaId = data.areaId;
					$("#areaId").val(data_areaId);
				}).fail(function() {
				});
			}
		}
		return true;
	}

	function getRadio() {
		e = event.srcElement;
		if (e.type == "radio" && e.name == "radio") {
			if (e.value == 0) {
				$("#radio_hidden").val(0);
			} else if (e.value == 1) {
				$("#radio_hidden").val(1);
			}
		}
	}
	//检测手机号是否正确  
	jQuery.validator.addMethod("isMobile", function(value, element) {  
	    var length = value.length;  
	    var regPhone = /^1([3578]\d|4[57])\d{8}$/;  
	    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
	}, "请正确填写手机号码");  
	
	//检测手机号是否正确  
	jQuery.validator.addMethod("car_plate", function(value, element) {  
	    var reg = /^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/;  
	    return this.optional(element) || (reg.test( value )) ;    
	}, "请正确填写车牌号,例如：京A00001");  
	
</script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/custom/spectrum.css">
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/car/">车辆信息列表</a></li>
		<li class="active"><a href="${ctx}/guard/car/form?id=${car.id}">车辆信息<shiro:hasPermission
					name="guard:car:edit">${not empty car.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:car:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="car"
		enctype="multipart/form-data" action="${ctx}/guard/car/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<input type="hidden" id="selectedCompanyId" name="selectedCompanyId"/>
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>车辆名称：</label>
			<div class="col-xs-3">
				<form:input path="name" htmlEscape="false" maxlength="32"
					class="form-control input-sm required" id="car_name" />
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>所属公司：</label>
			<div class="col-xs-3">
				<sys:treeselect id="company" name="company.id"
					value="${car.company.id}" labelName="company.shortName"
					labelValue="${car.company.shortName}" title="公司"
					url="/guard/company/treeData" cssClass="required" allowClear="true"
					notAllowSelectRoot="true" />
			</div>
		</div>

		<input id="areaId" name="area.id" type="hidden"
			value="${car.area.id } ">

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>车辆卡号：</label>
			<div class="col-xs-3">
				<form:input path="cardNum" htmlEscape="false" maxlength="20"
					class="form-control input-sm required" />
			</div>
			<form:errors path="cardNum" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>车牌号：</label>
			<div class="col-xs-3">
				<form:input path="carplate" htmlEscape="false" maxlength="10"
					class="form-control input-sm required car_plate" />
			</div>
			<form:errors path="carplate" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">车辆颜色：</label>
			<div class="col-xs-3">
				<form:input path="color" htmlEscape="false" maxlength="10" id="full"
					class="form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>负责人：</label>
			<div class="col-xs-3">
				<form:input path="admin" htmlEscape="false" maxlength="32"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>联系方式：</label>
			<div class="col-xs-3">
				<form:input path="phone" htmlEscape="false" maxlength="18"
					class="form-control input-sm required isMobile" />
			</div>
		</div>

		<form:input path="workStatus" id="radio_hidden" type="hidden" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>工作状态：</label>
			<div class="col-xs-3">
				<div class="radio-inline">
					<span onclick="getRadio();"> 
						<input type="radio" name="radio" value="0"><label>正常</label>
						<input type="radio" name="radio" value="1"><label>维修</label>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">车辆照片：</label>
			<div class="col-xs-3" id="car_imgagePath">
				<form:hidden id="imagePath" path="carImage.imagePath" htmlEscape="false" maxlength="255" class="form-control input-sm" />
				<!--[if lt IE 10]>
				<div class="picbtn"><img id="img_id" src= "${ctx}/guard/image/car?id=${car.id}"  onerror="errorImg(this)"  style="width:150px" /></div>
				<input type="file" name="file" id="up">
				<![endif]-->
				<!--[if gte IE 10]>
					<div class="kv-avatar" style="width: 200px">
					<input id="avatar-2" name="file" type="file" class="file-loading">
				</div>
				<![endif]-->
				<!--[if !IE]><!-->
				<div class="kv-avatar" style="width: 200px">
					<input id="avatar-2" name="file" type="file" class="file-loading">
				</div>
				<p class="help-block">点击上面图片上传新图片。</p>
				<!--<![endif]-->
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注信息：</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="255" class="input-xxlarge " />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:car:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</div>
	<script>
	//头像上传预览
    $("#up").change(function() {
        var $file = $(this);
        var fileObj = $file[0];
        var windowURL = window.URL || window.webkitURL;
        var dataURL;
        var $img = $("#img_id");
        if(fileObj && fileObj.files && fileObj.files[0]){
        dataURL = windowURL.createObjectURL(fileObj.files[0]);
        $img.attr('src',dataURL);
        }else{
        dataURL = $file.val();
        var subDataUrl= dataURL.substring(dataURL.length-3,dataURL.length);
        if(subDataUrl!="jpg"){
        	alert("只能选择JPG格式的图片");			        	
        }
        var element = document.getElementById('img_id');
        element.src = dataURL;
        $("#imagePath").val(dataURL);
        }
    });
	
		　//图像加载出错时的处理
		function errorImg(img) {
		img.src = "${ctxStatic}/images/car_img.jpg";
		img.onerror = null;
		}
		
		var carId=${car.id}+"";
		if(carId=="" || typeof (carId) == "undefined"){
			carId= "0" ;
		}
		var preview2 = '<img src="${ctx}/guard/image/car?id=' + carId
				+ '" onerror="errorImg(this)"  style="width:150px">';

		$("#avatar-2").fileinput({
			overwriteInitial : false,
			maxFileSize : 1500,
			showCaption : false,
			showBrowse : false,
			autoReplace : true,
			browseOnZoneClick : true,
			removeLabel : '',
			showRemove : false,
			removeTitle : '取消或重新修改',
			elErrorContainer : '#kv-avatar-errors-2',
			msgErrorClass : 'alert alert-block alert-danger',
			defaultPreviewContent : preview2,
			removeFromPreviewOnError : true,
			layoutTemplates : {
				main2 : '{preview} {remove} {browse}'
			},

			allowedFileExtensions : [ "jpg","bmp","gif","png", "jpeg", "ico"],
			previewSettings : {
				image : {
					width : "auto",
					height : "100px"
				}
			}
		});
	</script>
</body>
</html>