<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(
				function() {
					top.$.jBox.confirm("确认要导出设备数据吗？", "系统提示", function(
							v, h, f) {
						if (v == "ok") {
							$("#searchForm").attr("action",
									"${ctx}/guard/car/export");
							$("#searchForm").submit();
							$("#searchForm").attr("action",
									"${ctx}/guard/car/list");
						}
					}, {
						buttonsFocus : 1
					});
					top.$('.jbox-body .jbox-icon').css('top', '55px');
				});
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
		<li class="active"><a href="${ctx}/guard/car/">车辆信息列表</a></li>
		<shiro:hasPermission name="guard:car:edit">
			<li><a href="${ctx}/guard/car/form">车辆信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="car"
		action="${ctx}/guard/car/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<c:if test="${car.company != null and car.company.id != null }">
			<input name="company.id" type="hidden" value="${car.company.id}" />
		</c:if>
		<div class="form-group">
			<label>车辆名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="32" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>车牌号：</label>
			<form:input path="carplate" htmlEscape="false" maxlength="10" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>车辆卡号：</label>
			<form:input path="cardNum" htmlEscape="false" maxlength="10" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>车辆名称</th>
				<th>所属公司</th>
				<th>所属区域</th>
				<th>车辆卡号</th>
				<th>车牌号</th>
				<th>负责人</th>
				<th>联系方式</th>
				<th>工作状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="guard:car:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a href="${ctx}/guard/car/form?id=${item.id}">
							${item.name} </a></td>
					<td>${item.company.shortName}</td>
					<td>${item.area.name}</td>
					<td>${item.cardNum}</td>
					<td>${item.carplate}</td>
					<td>${item.admin}</td>
					<td>${item.phone}</td>
					<td>${fns:getDictLabel(item.workStatus, 'work_status', '')}</td>
					<td><fmt:formatDate value="${item.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${item.remarks}</td>
					<shiro:hasPermission name="guard:car:edit">
						<td><a href="${ctx}/guard/car/form?id=${item.id}">修改</a> <a
							href="${ctx}/guard/car/delete?id=${item.id}&selectedCompanyId=${car.company.id}"
							onclick="return confirmx('确认要删除该车辆信息吗？', this.href)">删除</a> <a
							href="${ctx}/guard/car/download?id=${item.id}&selectedCompanyId=${car.company.id}"
							onclick="return confirmx('是否同步该车辆', this.href)">同步</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>