<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<%-- <link rel="stylesheet" type="text/css" href="${ctxStatic}/colorpicker/css/htmleaf-demo.css"> --%>
	<link href="${ctxStatic}/colorpicker/dist/css/bootstrap-colorpicker.css" rel="stylesheet">
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					/* if(${dict.type}=='logoImage'){
						alert('请上传logo');
						return;
					} */
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dict/">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${dict.id}">字典<shiro:hasPermission name="sys:dict:edit">${not empty dict.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<sys:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<c:choose>
			<c:when test="${dict.type=='sysconfig' && dict.label=='mainColor'}">
				<div class="form-group">
				<label class="control-label col-md-2"><font class="red">*</font>主题色:</label>
				 <div id="cp2" class="input-group colorpicker-component col-md-3" title="点击右侧进行颜色选择">
				  <form:input path="value" class="required  form-control input-sm"  readOnly="true" style="margin-left:15px;width:95%;"/>
				  <span class="input-group-addon" style="height:30px;padding:2px 12px;"><i></i></span>
				</div>
				<input type="file" name="file" style="display:none;"/>
			</div>
			</c:when>
			<c:when test="${dict.type=='sysconfig' && dict.label=='titleIco'}">
			<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>网页ICO:</label>
			<div class="col-md-8">
				<input type="file" name="file" id="fileId"/>
				</br>
				<font size="3px" color="red">只能上传.ico格式的文件</font>
				</br>
				<span>图片转ico地址(尺寸请选择16*16)：<a href="http://www.bitbug.net/" target="_blank">http://www.bitbug.net/</a></span>
			</div>
		</div>
		</c:when>
			<c:when test="${dict.type=='sysconfig' && dict.label=='logoImage'}">
				<div class="form-group">
					<label class="control-label col-md-2"><font class="red">*</font>网页LOGO:</label>
					<div class="col-md-3">
						<input type="file" name="file" id="fileImageId"/>
						</br>
						<font size="3px" color="red">只能上传.JPG和.PNG格式的文件</font>
					</div>
				</div>
			</c:when>
			<c:otherwise>
			<input type="file" name="file" style="display:none;"/>
			<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>键值:</label>
			<div class="col-md-3">
				<form:input path="value" htmlEscape="false" maxlength="50" class="required form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>标签:</label>
			<div class="col-md-3">
				<form:input path="label" htmlEscape="false" maxlength="50" class="required  form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>类型:</label>
			<div class="col-md-3">
				<form:input path="type" htmlEscape="false" maxlength="50" class="required abc  form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>描述:</label>
			<div class="col-md-3">
				<form:input path="description" htmlEscape="false" maxlength="50" class="required  form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font class="red">*</font>排序:</label>
			<div class="col-md-3">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits  form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">备注:</label>
			<div class="col-md-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class=" form-control input-sm"/>
			</div>
		</div>
		</c:otherwise>
		</c:choose>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
			<shiro:hasPermission name="sys:dict:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
	<script src="${ctxStatic}/colorpicker/dist/js/bootstrap-colorpicker.js"></script>
	<script type="text/javascript">
		$(function () {
		    $('#cp2').colorpicker();
		  });
	</script>
</body>
</html>