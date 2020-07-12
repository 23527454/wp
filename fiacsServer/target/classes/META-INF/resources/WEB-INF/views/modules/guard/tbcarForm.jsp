<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/custom/spectrum.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/docs.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/toc.js'></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
				
				
				$("#full").spectrum(
						{
							allowEmpty : true,
							color : "${tbcar.ncarcolor}",
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
									[ "rgb(0, 0, 0)", "rgb(67, 67, 67)",
											"rgb(102, 102, 102)", /*"rgb(153, 153, 153)","rgb(183, 183, 183)",*/
											"rgb(204, 204, 204)", "rgb(217, 217, 217)", /*"rgb(239, 239, 239)", "rgb(243, 243, 243)",*/
											"rgb(255, 255, 255)" ],
									[ "rgb(152, 0, 0)", "rgb(255, 0, 0)",
											"rgb(255, 153, 0)", "rgb(255, 255, 0)",
											"rgb(0, 255, 0)", "rgb(0, 255, 255)",
											"rgb(74, 134, 232)", "rgb(0, 0, 255)",
											"rgb(153, 0, 255)", "rgb(255, 0, 255)" ],
									[ "rgb(230, 184, 175)", "rgb(244, 204, 204)",
											"rgb(252, 229, 205)", "rgb(255, 242, 204)",
											"rgb(217, 234, 211)", "rgb(208, 224, 227)",
											"rgb(201, 218, 248)", "rgb(207, 226, 243)",
											"rgb(217, 210, 233)", "rgb(234, 209, 220)",
											"rgb(221, 126, 107)", "rgb(234, 153, 153)",
											"rgb(249, 203, 156)", "rgb(255, 229, 153)",
											"rgb(182, 215, 168)", "rgb(162, 196, 201)",
											"rgb(164, 194, 244)", "rgb(159, 197, 232)",
											"rgb(180, 167, 214)", "rgb(213, 166, 189)",
											"rgb(204, 65, 37)", "rgb(224, 102, 102)",
											"rgb(246, 178, 107)", "rgb(255, 217, 102)",
											"rgb(147, 196, 125)", "rgb(118, 165, 175)",
											"rgb(109, 158, 235)", "rgb(111, 168, 220)",
											"rgb(142, 124, 195)", "rgb(194, 123, 160)",
											"rgb(166, 28, 0)", "rgb(204, 0, 0)",
											"rgb(230, 145, 56)", "rgb(241, 194, 50)",
											"rgb(106, 168, 79)", "rgb(69, 129, 142)",
											"rgb(60, 120, 216)", "rgb(61, 133, 198)",
											"rgb(103, 78, 167)", "rgb(166, 77, 121)",
											/*"rgb(133, 32, 12)", "rgb(153, 0, 0)", "rgb(180, 95, 6)", "rgb(191, 144, 0)", "rgb(56, 118, 29)",
											"rgb(19, 79, 92)", "rgb(17, 85, 204)", "rgb(11, 83, 148)", "rgb(53, 28, 117)", "rgb(116, 27, 71)",*/
											"rgb(91, 15, 0)", "rgb(102, 0, 0)",
											"rgb(120, 63, 4)", "rgb(127, 96, 0)",
											"rgb(39, 78, 19)", "rgb(12, 52, 61)",
											"rgb(28, 69, 135)", "rgb(7, 55, 99)",
											"rgb(32, 18, 77)", "rgb(76, 17, 48)" ] ]
						});
				
				
				
			});

</script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/custom/spectrum.css">

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/tbcar/">车辆信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/tbcar/form?id=${tbcar.id}">车辆信息<shiro:hasPermission
					name="guard:tbcar:edit">${not empty tbcar.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:tbcar:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="tbcar"
		action="${ctx}/guard/tbcar/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">车辆编号：</label>
			<div class="controls">
				<form:input path="ncarno" htmlEscape="false"
					class="input-xlarge required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卡号：</label>
			<div class="controls">
				<form:input path="szcardnum" htmlEscape="false" maxlength="20"
					class="input-xlarge required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车牌号：</label>
			<div class="controls">
				<form:input path="szcarplate" htmlEscape="false" maxlength="10"
					class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车辆类型：</label>
			<div class="controls">
				<form:select path="szcarmodel" class="input-xlarge ">
					<form:option value="" label="" />
					<form:options items="${fns:getDictList('car_model')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车辆颜色：</label>
			<div class="controls">
			 <form:input path="ncarcolor" id="full" htmlEscape="false"
					class="input-xlarge " />
			
				<%-- <div class="controls">
				<form:input path="ncarcolor" htmlEscape="false" maxlength="10"
					class="input-xlarge " /> --%>

		

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车辆状态：</label>
			<div class="controls">
				<form:select path="nworkstatus" class="input-xlarge required">
					<form:option value="" label="" />
					<form:options items="${fns:getDictList('work_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人：</label>
			<div class="controls">
				<form:input path="szadmin" htmlEscape="false" maxlength="32"
					class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人联系方式：</label>
			<div class="controls">
				<form:input path="szadminphone" htmlEscape="false" maxlength="20"
					class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车辆品牌：</label>
			<div class="controls">
				<form:select path="carbrand" class="input-xlarge ">
					<form:option value="" label="" />
					<form:options items="${fns:getDictList('car_brand')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">车辆图片：</label>
			<div class="controls">
				<form:hidden id="szcarimage" path="szcarimage" htmlEscape="false"
					maxlength="255" class="input-xlarge" />
				<sys:ckfinder input="szcarimage" type="images"
					uploadPath="/guard/tbcar" selectMultiple="false" maxWidth="100"
					maxHeight="100" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="guard:tbcar:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>