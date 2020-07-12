<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>异常报警事件查询管理</title>
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
									},
									errorContainer : "#messageBox"
								});
			});
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/alarmEventQuery/">异常报警事件查询列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/alarmEventQuery/form?id=${alarmEventQuery.id}">异常报警事件查询<shiro:hasPermission
					name="guard:alarmEventQuery:edit">${not empty alarmEventQuery.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:alarmEventQuery:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="alarmEventQuery"
		action="${ctx}/guard/alarmEventQuery/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />

		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label col-xs-3">网点：<span
						class="help-inline"></span></label>
					<div class="col-xs-5">
						<form:input path="office.name" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">区域：<span
						class="help-inline"></span></label>
					<div class="col-xs-5">
						<form:input path="area.name" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">任务类型：<span
						class="help-inline"></span></label>
					<div class="col-xs-5">
						<form:input path="eventType" htmlEscape="false"
							class="form-control input-sm required"
							value="${fns:getDictLabel(alarmEventQuery.eventType, 'event_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">时间：</label>
					<div class="col-xs-5">
						<form:input path="time" htmlEscape="false" maxlength="1"
							class="form-control input-sm " />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">班组：</label>
					<div class="col-xs-5">
						<form:input path="classTaskfo.name" htmlEscape="false"
							maxlength="32" class="form-control input-sm " />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="history.go(-1)" />
					</div>
				</div>
			</div>
			<div class="col-xs-6">
				<label class="control-label">款箱列表：</label>
				<table class="table table-striped table-bordered table-condensed"
					style="width: 70%;">
					<thead>
						<tr>
							<th data-options="field:'no'" width="50%">序号</th>
							<th data-options="field:'cardNum'" width="50%">款箱卡号</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${alarmEventQuery.moneyBoxEventDetailList}" var="box" varStatus="varStatus">
							<tr>
								<td style="text-align: center;">${varStatus.count }</td>
								<td>${box.cardNum}</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</div>
</body>
</html>