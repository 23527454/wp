<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>车辆事件管理</title>
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

	function goBack(){
		window.location.href = document.referrer;
	}
	</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/carEvent/">车辆事件列表</a></li>
		<li class="active"><a href="${ctx}/guard/carEvent/form?id=${carEvent.id}">车辆事件<shiro:hasPermission name="guard:carEvent:edit">${not empty carEvent.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="guard:carEvent:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="carEvent" action="${ctx}/guard/carEvent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="control-label col-xs-2">事件ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="carId" htmlEscape="false" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">设备ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="equipmentId" htmlEscape="false" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">事件序号：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="eventId" htmlEscape="false" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">设备序列号：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="equipSn" htmlEscape="false" maxlength="32" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="time" htmlEscape="false" maxlength="20" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">任务ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="taskId" htmlEscape="false" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:carEvent:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="goBack();"/>
			</div>
		</div>
	</form:form>
</div>
</body>
</html>