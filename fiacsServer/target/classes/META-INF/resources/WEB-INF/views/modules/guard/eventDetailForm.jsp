<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>事件详情管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/accessEventDetail/">事件详情列表</a></li>
		<li class="active"><a href="${ctx}/guard/accessEventDetail/form?id=${accessEventDetail.id}">事件详情<shiro:hasPermission name="guard:accessEventDetail:edit">${not empty accessEventDetail.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="guard:accessEventDetail:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="accessEventDetail" action="${ctx}/guard/accessEventDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="control-label col-xs-2">指纹ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="fingerId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">设备ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="equipmentId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">事件ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="recordId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">抓拍图片数据：</label>
				<div class="col-xs-4">
				<form:input path="imageData" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">抓拍图片数据长度：</label>
				<div class="col-xs-4">
				<form:input path="imageSize" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">记录时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="time" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:accessEventDetail:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>