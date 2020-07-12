<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>线路监控管理</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/custom/atmosphere.js"></script>
<script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
<script type="text/javascript">
	var msg = "";
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/guard/lineEvent/list?nodes=${nodes}">线路监控列表</a></li>
	</ul>
	<input id="tts" value="${ tts}" style="display: none;">
	<form:form id="searchForm" modelAttribute="lineEvent"
		action="${ctx}/guard/lineEvent/" method="post"
		class="breadcrumb form-inline" style="display:none;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<input id="nodes" name="nodes" value=${nodes } htmlEscape="false"
			class="form-control" />

		<div class="form-group">
			<label>编号：</label>
			<form:input path="id" htmlEscape="false" class="form-control" />
		</div>

		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />


	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px">序号</th>
				<th>班次</th>
				<th>线路名称</th>
				<th>执行时间</th>
				<th>班组名</th>
				<th>任务类型</th>
				<th>车辆确认</th>
				<th>专员确认</th>
			</tr>
		</thead>
		<tbody id="eventMessages">
			<tr id="placeHolder"></tr>
			<c:forEach items="${page.list}" var="lineEvent" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count }</td>
					<td><a
						href="${ctx}/guard/lineEvent/form?id=${lineEvent.id}&nodes=${nodes}">${lineEvent.id}</a></td>
					<td>${lineEvent.lineName}</td>
					<td>${lineEvent.taskDate}${lineEvent.taskTime}</td>
					<td>${lineEvent.classTaskInfo.name}</td>
					<td>${fns:getDictLabel(lineEvent.taskType, 'task_type', '')}</td>
					<td><c:if test="${lineEvent.verifyCar == 1}">
						是
					</c:if> <c:if test="${lineEvent.verifyCar == 0}">
						否
					</c:if></td>
					<td><c:if test="${lineEvent.verifyInterMan == 1}">
						是
					</c:if> <c:if test="${lineEvent.verifyInterMan == 0}">
						否
					</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>