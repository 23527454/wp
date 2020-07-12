<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员交接明细管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$('a[rel=popover]').popover(
						{
							container : 'body',
							html : true,
							trigger : 'hover',
							placement : 'bottom',
							content : function() {
								return '<img src="' + $(this).data('img')
										+ '" width="256px" height="256px"/>';
							}
						});

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
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/eventDetailQuery/">人员交接明细列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/eventDetailQuery/form?id=${eventDetailQuery.id}">人员交接明细<shiro:hasPermission
					name="guard:eventDetailQuery:edit">${not empty eventDetailQuery.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:eventDetailQuery:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="eventDetailQuery"
		action="${ctx}/guard/eventDetailQuery/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
		<div class="col-xs-5">
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">网点：<span
				class="help-inline"></span></label>
			<div class="col-xs-6">
				<form:input path="officeName" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">时间：<span
				class="help-inline"> </span></label>
			<div class="col-xs-6">
				<form:input path="time" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">押款员：<span
				class="help-inline"> </span></label>
			<div class="col-xs-6">
				<form:input path="staff.name" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">任务类型：<span
				class="help-inline"></span></label>
			<div class="col-xs-6">
				<form:input path="taskType" htmlEscape="false"
					class="form-control input-sm required"
					value="${fns:getDictLabel(eventDetailQuery.taskType, 'task_type', '')}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">班组：</label>
			<div class="col-xs-6">
				<form:input path="className" htmlEscape="false"
					maxlength="32" class="form-control input-sm " />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">班次：<span
				class="help-inline"></span></label>
			<div class="col-xs-6">
				<form:input path="taskId" htmlEscape="false"
					maxlength="20" class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-offset-1 col-xs-2">区域：<span
				class="help-inline"></span></label>
			<div class="col-xs-6">
				<form:input path="areaName" htmlEscape="false" maxlength="20"
					class="form-control input-sm required" />
			</div>
		</div>
<!-- 		<div class="form-group"> -->
<!-- 			<label class="control-label col-xs-offset-1 col-xs-2">照片路径：</label> -->
<!-- 			<div class="col-xs-3"> -->
<%-- 			<a rel="popover" data-img="${eventDetailQuery.staff.staffImageList[0].imagePath}"><img --%>
<%-- 											src="${eventDetailQuery.staff.staffImageList[0].imagePath}" --%>
<!-- 											height="100" width="100" /></a> -->
<!-- 			</div> -->
<!-- 		</div> -->
</div>
	<div class="col-xs-5">
		<div class="form-group">
			<label class="control-label col-xs-2">人员抓拍照：</label>
			<div class="col-xs-6">
				<a rel="popover" data-img="${ctx}/guard/image?id=${eventDetailQuery.id}"><img
											src="${ctx}/guard/image?id=${eventDetailQuery.id}"
											height="200" width="200" /></a>
			</div>
		</div>
	</div>
</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</div>
</body>
</html>


