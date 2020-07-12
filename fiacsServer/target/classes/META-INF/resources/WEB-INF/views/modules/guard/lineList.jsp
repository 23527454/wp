<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>线路信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
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
		<li class="active"><a href="${ctx}/guard/line/">线路信息列表</a></li>
		<shiro:hasPermission name="guard:line:edit">
			<li><a href="${ctx}/guard/line/form?selectedAreaId=${line.area.id}">线路信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="line"
		action="${ctx}/guard/line/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>线路名称：</label>
			<form:input path="lineName" htmlEscape="false" maxlength="48" class="form-control input-sm" />
		</div>
		<div class="form-group">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
	    </div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>线路名称</th>
				<th>所属区域</th>
				<th>线路顺序</th>
				<th>到下个网点间隔时间</th>
				<th>节点顺序</th>
				<th>更新时间</th>
				<th>备注</th>
				<shiro:hasPermission name="guard:line:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a href="${ctx}/guard/line/form?id=${item.id}&selectedAreaId=${line.area.id}">
						${item.lineName}</a></td>
					<td>${item.area.name}</td>
					<td>${fns:getDictLabel(item.lineOrder, 'line_order', '')}</td>
					<td>${item.nodeIunterval}分钟</td>
					<td>
						<c:forEach items="${item.lineNodesList}" var="l">
						${l.office.name} &gt;
						</c:forEach>
					</td>
					<td><fmt:formatDate value="${item.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${item.remarks}</td>
					<shiro:hasPermission name="guard:line:edit">
						<td><a href="${ctx}/guard/line/form?id=${item.id}&selectedAreaId=${line.area.id}">修改</a> <a
							href="${ctx}/guard/line/delete?id=${item.id}&selectedAreaId=${line.area.id}"
							onclick="return confirmx('确认要删除该线路信息吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>