<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>异常报警事件管理</title>
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

	function goBack(){
		window.location.href = document.referrer;
	}
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/alarmEvent/list?nodes=${alarmEvent.nodes}">异常报警事件列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/alarmEvent/form?id=${alarmEvent.id}&nodes=${alarmEvent.nodes}">异常报警事件<shiro:hasPermission
					name="guard:alarmEvent:edit">${not empty alarmEvent.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:alarmEvent:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="alarmEvent"
		action="${ctx}/guard/alarmEvent/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-5">

				<div class="form-group">
					<label class="control-label col-xs-3">网点：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="officeName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">区域：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="areaName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">事件类型：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="eventType" htmlEscape="false"
							class="form-control input-sm required"
							value="${fns:getDictLabel(alarmEvent.eventType, 'event_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">时间：</label>
					<div class="col-xs-6">
						<form:input path="time" htmlEscape="false" maxlength="1"
							class="form-control input-sm " />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">班组：</label>
					<div class="col-xs-6">
						<form:input path="taskName" htmlEscape="false" maxlength="32"
							class="form-control input-sm " />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<shiro:hasPermission name="guard:alarmEvent:edit">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="保 存" />&nbsp;</shiro:hasPermission>
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="goBack();" />
					</div>
				</div>
			</div>
			
			<div class="col-xs-6">
				<label class="control-label">款箱列表：</label>
				<table class="table table-striped table-bordered table-condensed"
					style="width: 70%;">
					<thead>
						<tr>
							<th data-options="field:'no'" style="width: 30px" >序号</th>
							<th data-options="field:'cardNum'" width="45%">款箱卡号</th>
							<th data-options="field:'time'" width="45%">款箱编码</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${alarmEvent.moneyBoxEventDetailList}"
							var="box" varStatus="varStatus">
							<tr>
								<td style="text-align: center;">${varStatus.count}</td>
								<td>${box.cardNum}</td>
								<td>${box.moneyBox.boxCode}</td>
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