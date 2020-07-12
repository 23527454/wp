<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员交接明细</title>
<meta name="decorator" content="default" />
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
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/connectPersonnel/">人员交接明细列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/connectPersonnel/form?id=${connectPersonnel.id}">人员交接明细查询<shiro:hasPermission
					name="guard:connectPersonnel:edit">${not empty connectPersonnel.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:connectPersonnel:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="connectPersonnel"
		action="${ctx}/guard/connectPersonnel/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">网点：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="office.name" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">时间：<span
				class="help-inline"> </span></label>
			<div class="col-xs-4">
				<form:input path="time" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">押款员：<span
				class="help-inline"> </span></label>
			<div class="col-xs-4">
				<form:input path="staff.name" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">任务类型：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="taskType" htmlEscape="false"
					class="input-xlarge required"
					value="${fns:getDictLabel(connectPersonnel.taskType, 'task_type', '')}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">班组：</label>
			<div class="col-xs-4">
				<form:input path="classTaskfo.name" htmlEscape="false"
					maxlength="32" class="input-xlarge " />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">班次：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="classTaskfo.id" htmlEscape="false" maxlength="20"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">区域：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="area.name" htmlEscape="false" maxlength="20"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">区域：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="staffImage.imagePath" htmlEscape="false"
					maxlength="20" class="input-xlarge required" />
			</div>
		</div>



		<div class="form-group">
			<label class="control-label col-xs-2">照片路径：</label>
			<div class="col-xs-4">
				<img src="${connectPersonnel.staffImage.imagePath}"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:connectPersonnel:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</body>
</html>