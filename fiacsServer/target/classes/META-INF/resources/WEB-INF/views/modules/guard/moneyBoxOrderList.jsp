<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱预约管理</title>
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
		<li class="active"><a href="${ctx}/guard/moneyBoxOrder/">款箱预约列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="moneyBoxOrder"
		action="${ctx}/guard/moneyBoxOrder/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>所属网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${moneyBoxOrder.office.id}" labelName="office.name"
				labelValue="${moneyBoxOrder.office.name}" title="网点"
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
			<label>开始时间：</label> <input name="orderTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
				value="<fmt:formatDate value="${moneyBoxOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss" />"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		
		<div class="form-group">
			<label>截止时间：</label> <input name="orderTimeTo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"  style="height:auto"
				value="<fmt:formatDate value="${moneyBoxOrder.orderTimeTo}" pattern="yyyy-MM-dd HH:mm:ss" />"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
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
				<th>回送日期</th>
				<th>预约类型</th>
				<th>预约时间</th>
<!-- 				<th>上传时间</th> -->
<!-- 				<th>预约状态</th> -->
				<shiro:hasPermission name="guard:moneyBoxOrder:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="moneyBoxOrder">
				<tr>
					<td>${moneyBoxOrder.moneyBox.boxCode}</td>
					<td>${moneyBoxOrder.moneyBox.cardNum}</td>
					<td>${fns:getDictLabel(moneyBoxOrder.moneyBox.boxType, 'box_type', '')}</td>
					<td>${moneyBoxOrder.office.name}</td>
					<td><fmt:formatDate value="${moneyBoxOrder.allotReturnTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${fns:getDictLabel(moneyBoxOrder.orderType, 'order_type', '')}</td>
					<td><fmt:formatDate value="${moneyBoxOrder.orderTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
<%-- 					<td><fmt:formatDate value="${moneyBoxOrder.uploadTime}" --%>
<!-- 							pattern="yyyy-MM-dd HH:mm:ss" /></td> -->
<%-- 					<td>${fns:getDictLabel(moneyBoxOrder.orderSysStatus, 'sys_status', '')}</td> --%>
					<shiro:hasPermission name="guard:moneyBoxOrder:edit">
						<td><a
							href="${ctx}/guard/moneyBoxOrder/delete?id=${moneyBoxOrder.id}"
							onclick="return confirmx('确认要取消该款箱预约吗？', this.href)">取消</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>