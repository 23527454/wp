<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>维保员派遣管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
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
		<li class="active"><a href="${ctx}/guard/safeGuardDispatch/">维保员派遣列表</a></li>
		<shiro:hasPermission name="guard:staff:edit">
			<li><a href="${ctx}/guard/safeGuardDispatch/form">维保员派遣新增</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="safeGuardDispatch"
		action="${ctx}/guard/safeGuardDispatch/list" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>任务名称：</label>
			<form:input path="dispatchName" htmlEscape="false" maxlength="32"
				class="form-control input-sm" />
		</div>
        <div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" style="margin-left:10px;"/>
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>派遣任务名称</th>
				<th style="width: 200px;">创建时间</th>
				<shiro:hasPermission name="guard:staff:edit">
					<th style="width: 120px;">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="info" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a
						href="${ctx}/guard/safeGuardDispatch/form?id=${info.id}">
							${info.dispatchName} </a></td>
							<td><fmt:formatDate value="${info.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<shiro:hasPermission name="guard:staff:edit">
						<td>
							<a
							href="${ctx}/guard/safeGuardDispatch/form?id=${info.id}">修改</a>
							<a
							href="${ctx}/guard/safeGuardDispatch/delete?id=${info.id}"
							onclick="return confirmx('确认要删除该派遣任务信息吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>