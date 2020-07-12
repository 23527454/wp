<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
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
		<li class="active"><a href="${ctx}/firsttable/tbTest/">测试列表</a></li>
		<shiro:hasPermission name="firsttable:tbTest:edit"><li><a href="${ctx}/firsttable/tbTest/form">测试添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tbTest" action="${ctx}/firsttable/tbTest/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>phone：</label>
				<form:input path="phone" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>age：</label>
				<form:input path="age" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>phone</th>
				<th>name</th>
				<th>age</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="firsttable:tbTest:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tbTest">
			<tr>
				<td><a href="${ctx}/firsttable/tbTest/form?id=${tbTest.id}">
					${tbTest.phone}
				</a></td>
				<td>
					${tbTest.name}
				</td>
				<td>
					${tbTest.age}
				</td>
				<td>
					<fmt:formatDate value="${tbTest.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tbTest.remarks}
				</td>
				<shiro:hasPermission name="firsttable:tbTest:edit"><td>
    				<a href="${ctx}/firsttable/tbTest/form?id=${tbTest.id}">修改</a>
					<a href="${ctx}/firsttable/tbTest/delete?id=${tbTest.id}" onclick="return confirmx('确认要删除该测试吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>