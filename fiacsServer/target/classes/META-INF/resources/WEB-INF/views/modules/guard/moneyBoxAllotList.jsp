<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱调拨管理</title>
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
		<li class="active"><a href="${ctx}/guard/moneyBoxAllot/">款箱调拨列表</a></li>
		<shiro:hasPermission name="guard:moneyBoxAllot:edit">
			<li><a href="${ctx}/guard/moneyBoxAllot/index">款箱调拨添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="moneyBoxAllot"
		action="${ctx}/guard/moneyBoxAllot/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${moneyBoxAllot.office.id}" labelName="office.name"
				labelValue="${moneyBoxAllot.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>款箱编码：</label>
			<form:input path="moneyBox.boxCode" htmlEscape="false"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>款箱卡号：</label>
			<form:input path="moneyBox.cardNum" htmlEscape="false"
				class="form-control input-sm" />
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
				<th>款箱编号</th>
				<th>款箱卡号</th>
				<th>款箱类型</th>
				<th>所属网点</th>
				<th>派送日期</th>
				<th>调拨类型</th>
				<th>调拨时间</th>
				<th>下载时间</th>
				<th>调拨状态</th>
				<shiro:hasPermission name="guard:moneyBoxAllot:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="moneyBoxAllot">
				<tr>
					<td>${moneyBoxAllot.moneyBox.boxCode}</td>
					<td>${moneyBoxAllot.moneyBox.cardNum}</td>
					<td>${fns:getDictLabel(moneyBoxAllot.moneyBox.boxType, 'box_type', '')}
					</td>
					<td>${moneyBoxAllot.office.name}</td>
					<td><fmt:formatDate value="${moneyBoxAllot.scheduleTime}"
							pattern="yyyy-MM-dd" /></td>
					<td>${fns:getDictLabel(moneyBoxAllot.allotType, 'allo_type', '')}</td>
					<td><fmt:formatDate value="${moneyBoxAllot.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${moneyBoxAllot.downloadTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${fns:getDictLabel(moneyBoxAllot.delFlag, 'sys_status', '')}</td>
					<shiro:hasPermission name="guard:moneyBoxAllot:edit">
						<td><a
							href="${ctx}/guard/moneyBoxAllot/delete?id=${moneyBoxAllot.id}"
							onclick="return confirmx('确认要删除该款箱调拨吗？', this.href)">删除</a> <a
							href="${ctx}/guard/moneyBoxAllot/download?id=${moneyBoxAllot.id}"
							onclick="return confirmx('是否同步该款箱调拨', this.href)">同步</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>