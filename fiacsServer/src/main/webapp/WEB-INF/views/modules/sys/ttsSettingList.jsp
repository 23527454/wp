<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>语音配置管理</title>
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
		<li class="active"><a href="${ctx}/sys/ttsSetting/">语音配置列表</a></li>
		<shiro:hasPermission name="sys:ttsSetting:edit"><li><a href="${ctx}/sys/ttsSetting/form">语音配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ttsSetting" action="${ctx}/sys/ttsSetting/" method="post" class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="sys:ttsSetting:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ttsSetting">
			<tr>
				<shiro:hasPermission name="sys:ttsSetting:edit"><td>
    				<a href="${ctx}/sys/ttsSetting/form?id=${ttsSetting.id}">修改</a>
					<a href="${ctx}/sys/ttsSetting/delete?id=${ttsSetting.id}" onclick="return confirmx('确认要删除该语音配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>