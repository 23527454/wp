<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(
				function() {
					top.$.jBox.confirm("确认要导出设备数据吗？", "系统提示", function(
							v, h, f) {
						if (v == "ok") {
							$("#searchForm").attr("action",
									"${ctx}/guard/moneyBox/export");
							$("#searchForm").submit();
							$("#searchForm").attr("action",
									"${ctx}/guard/moneyBox/list");
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
	
	function toAddMoneyBox(){
		var url = '${ctx}/guard/moneyBox/form?';
		var office_id = parent.$("#office_id").val();
		if(office_id){
			url += 'office.id=' + office_id;
		}
		window.location = url;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/moneyBox/">款箱信息列表</a></li>
		<shiro:hasPermission name="guard:moneyBox:edit">
			<li><a href="javascript:void(0)" onclick="toAddMoneyBox()" >款箱信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="moneyBox"
		action="${ctx}/guard/moneyBox/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<c:if test="${moneyBox.office!= null and moneyBox.office.id != null }">
			<input name="office.id" type="hidden" value="${moneyBox.office.id}" />
		</c:if>
		<div class="form-group">
			<label>款箱编码：</label>
			<form:input path="boxCode" htmlEscape="false" maxlength="16" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>款箱卡号：</label>
			<form:input path="cardNum" htmlEscape="false" maxlength="16" class="form-control input-sm" />
		</div>
		<div class="form-group">	
			<label>款箱类型：</label>
			<form:select path="boxType" class="form-control input-sm">
				<form:option value="" label="所有类型" />
				<form:options items="${fns:getDictList('box_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
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
				<th>款箱编码</th>
				<th>款箱卡号</th>
				<th>款箱类型</th>
				<th>所属网点</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="guard:moneyBox:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a href="${ctx}/guard/moneyBox/form?id=${item.id}">${item.boxCode} </a></td>
					<td>${item.cardNum}</td>
					<td>${fns:getDictLabel(item.boxType, 'box_type', '')}</td>
					<td>${item.office.name}</td>
					<td><fmt:formatDate value="${item.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${item.remarks}</td>
					<shiro:hasPermission name="guard:moneyBox:edit">
						<td><a href="${ctx}/guard/moneyBox/form?id=${item.id}&selectedOfficeId=${moneyBox.office.id}">修改</a>
							<a href="${ctx}/guard/moneyBox/delete?id=${item.id}&selectedOfficeId=${moneyBox.office.id}"
							onclick="return confirmx('确认要删除该款箱信息吗？', this.href)">删除</a> <a
							href="${ctx}/guard/moneyBox/download?id=${item.id}&selectedOfficeId=${moneyBox.office.id}"
							onclick="return confirmx('是否同步该款箱', this.href)">同步</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>