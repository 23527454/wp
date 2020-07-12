<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#btnExport")
								.click(
										function() {
											top.$.jBox
													.confirm(
															"确认要导出排班数据吗？",
															"系统提示",
															function(v, h, f) {
																if (v == "ok") {
																	$(
																			"#searchForm")
																			.attr(
																					"action",
																					"${ctx}/guard/taskScheduleInfo/export");
																	$(
																			"#searchForm")
																			.submit();
																	$(
																	"#searchForm")
																	.attr(
																			"action",
																			"${ctx}/guard/taskScheduleInfo/list");
																}
															},
															{
																buttonsFocus : 1
															});
											top.$('.jbox-body .jbox-icon').css(
													'top', '55px');
										});
					});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function gradeChange() {
		$("#btnSubmit").click();
	}
	
	function toAddTaskScheduleInfo(){
		var url = '${ctx}/guard/taskScheduleInfo/form?a=1&www=0';
		//var classTaskInfoId = parent.$("#classTaskfo_id").val();
		url += '&selectedClassTaskId=${taskScheduleInfo.classTaskId}';
		window.location = url;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/taskScheduleInfo/list?&classTaskId=${taskScheduleInfo.classTaskId}&www=0">排班信息列表</a></li>
		<shiro:hasPermission name="guard:taskScheduleInfo:edit">
			<li><a href="javascript:void(0)" onclick="toAddTaskScheduleInfo()">排班信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="taskScheduleInfo"
		action="${ctx}/guard/taskScheduleInfo/list?&classTaskId=${taskScheduleInfo.classTaskId}" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<label>班次：</label>
		<form:input path="id" htmlEscape="false" class="input-medium" />&nbsp;&nbsp;
		<label>班组名称：</label>
		<form:input path="classTaskInfo.name" htmlEscape="false"
			class="input-medium" />&nbsp;&nbsp;
		<label>任务类型：</label>
		<form:select path="taskType" class="input-medium"
			onchange="gradeChange()">
			<form:option value="" label="全部" />
			<form:options items="${fns:getDictList('task_type')}"
				itemLabel="label" itemValue="value" htmlEscape="false" />
		</form:select>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		<input id="btnExport" class="btn btn-primary" type="button" value="导出" />

	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>班次</th>
				<th>排班日期</th>
				<th>排班时间</th>
				<th>班组名称</th>
				<th>任务执行日期</th>
				<th>任务执行时间</th>
				<th>任务类型</th>
				<th>任务时间</th>
<!-- 				<th>班组任务超时</th> -->
<!-- 				<th>车辆确认</th> -->
<!-- 				<th>专员确认</th> -->
<!-- 				<th>款箱确认</th> -->
				<shiro:hasPermission name="guard:taskScheduleInfo:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a
						href="${ctx}/guard/taskScheduleInfo/form?id=${item.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}&www=0">
							${item.id} </a></td>
					<td>${item.allotDate}</td>
					<td>${item.allotTime}</td>
					<td>${item.classTaskInfo.name}</td>
					<td>${item.taskDate}</td>
					<td>${item.taskTime}</td>
					<td>${fns:getDictLabel(item.taskType, 'task_type', '')}
					</td>
					<td>${fns:getDictLabel(item.taskTimeClass, 'task_time_class', '')}
					</td>
<%-- 					<td>${item.taskTimeout}分钟</td> --%>
<%-- 					<td>${fns:getDictLabel(item.verifyCar, 'inter_man', '')} --%>
<!-- 					</td> -->
<%-- 					<td>${fns:getDictLabel(item.verifyInterMan, 'inter_man', '')} --%>
<!-- 					</td> -->
<%-- 					<td>${fns:getDictLabel(item.verifyMoneyBox, 'inter_man', '')} --%>
<!-- 					</td> -->
					<shiro:hasPermission name="guard:taskScheduleInfo:edit">
						<td>
						<a href="${ctx}/guard/taskScheduleInfo/form?id=${item.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}&www=0">查看</a>
						<a href="${ctx}/guard/taskScheduleInfo/delete?id=${item.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}" onclick="return confirmx('确认要删除该排班信息吗？', this.href)">删除</a>
						<%-- <a href="${ctx}/guard/taskScheduleInfo/download?id=${item.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}" onclick="return confirmx('是否同步该排班', this.href)">同步</a></td> --%>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>