<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排班明细管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/lineEventQuery/">排班明细列表</a></li>
		<shiro:hasPermission name="guard:lineEventQuery:edit"><li><a href="${ctx}/guard/lineEventQuery/form">排班明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="lineEventQuery" action="${ctx}/guard/lineEventQuery/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<label>编号：</label>
				<form:input path="id" htmlEscape="false" class="input-sm"/>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>线路ID</th>
				<th>节点ID</th>
				<th>设备ID</th>
				<th>事件序号</th>
				<th>设备序号</th>
				<th>时间</th>
				<th>任务ID</th>
				<shiro:hasPermission name="guard:lineEventQuery:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="lineEventQuery">
			<tr>
				<td><a href="${ctx}/guard/lineEventQuery/form?id=${lineEventQuery.id}">
					${lineEventQuery.id}
				</a></td>
				<td>
					${lineEventQuery.line.id}
				</td>
				<td>
					${lineEventQuery.nodesId}
				</td>
				<td>
					${lineEventQuery.office.id}
				</td>
				<td>
					${lineEventQuery.eventId}
				</td>
				<td>
					${lineEventQuery.equipSn}
				</td>
				<td>
					${lineEventQuery.time}
				</td>
				<td>
					${lineEventQuery.taskScheduleInfo.id}
				</td>
				<shiro:hasPermission name="guard:lineEventQuery:edit"><td>
    				<a href="${ctx}/guard/lineEventQuery/form?id=${lineEventQuery.id}">修改</a>
					<a href="${ctx}/guard/lineEventQuery/delete?id=${lineEventQuery.id}" onclick="return confirmx('确认要删除该排班明细吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>