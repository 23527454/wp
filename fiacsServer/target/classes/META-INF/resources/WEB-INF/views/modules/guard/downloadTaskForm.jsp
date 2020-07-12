<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排班同步信息管理</title>
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
		<li><a href="${ctx}/guard/downloadTask/">排班同步信息列表</a></li>
		<li class="active"><a href="${ctx}/guard/downloadTask/form?id=${downloadTask.id}">排班同步信息<shiro:hasPermission name="guard:downloadTask:edit">${not empty downloadTask.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="guard:downloadTask:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="downloadTask" action="${ctx}/guard/downloadTask/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="control-label col-xs-2">任务ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="taskId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:downloadTask:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>