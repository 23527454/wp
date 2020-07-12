<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>班组信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport")
		.click(
				function() {
					top.$.jBox
							.confirm(
									"确认要导出班组数据吗？",
									"系统提示",
									function(v, h, f) {
										if (v == "ok") {
											$(
													"#searchForm")
													.attr(
															"action",
															"${ctx}/guard/classTaskInfo/export");
											$(
													"#searchForm")
													.submit();
											$(
											"#searchForm")
											.attr(
													"action",
													"${ctx}/guard/classTaskInfo/list");
										}
									},
									{
										buttonsFocus : 1
									});
					top.$('.jbox-body .jbox-icon').css(
							'top', '55px');
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
		<li class="active"><a href="${ctx}/guard/classTaskInfo/">班组信息列表</a></li>
		<shiro:hasPermission name="guard:classTaskInfo:edit">
			<li><a href="${ctx}/guard/classTaskInfo/form">班组信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="classTaskInfo"
		action="${ctx}/guard/classTaskInfo/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>班组名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="32"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>区域：</label>
			<sys:treeselect id="area" name="area.id" value="${classTaskInfo.area.id}"
				labelName="area.name" labelValue="${classTaskInfo.area.name}" title="区域"
				url="/sys/area/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />&nbsp;
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
				<th>班组名称</th>
				<th>所属区域</th>
				<th>线路名称</th>
				<th>验证车辆</th>
				<th>专员确认</th>
				<th>款箱确认</th>
				<th>超时时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="guard:classTaskInfo:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="classTaskInfo" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a
						href="${ctx}/guard/classTaskInfo/form?id=${classTaskInfo.id}">
							${classTaskInfo.name} </a></td>
					<td>${classTaskInfo.area.name}</td>
					<td>${classTaskInfo.line.lineName}</td>
					<td>${fns:getDictLabel(classTaskInfo.verifyCar, 'verify_car', '')}
					</td>
					<td>${fns:getDictLabel(classTaskInfo.verifyInterMan, 'inter_man', '')}
					</td>
					<td>${fns:getDictLabel(classTaskInfo.verifyLocker, 'verify_locker', '')}
					</td>
					<td>${classTaskInfo.taskTimeout}分钟</td>
					<td><fmt:formatDate value="${classTaskInfo.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<shiro:hasPermission name="guard:classTaskInfo:edit">
						<td><a
							href="${ctx}/guard/classTaskInfo/form?id=${classTaskInfo.id}">修改</a>
							<a
							href="${ctx}/guard/classTaskInfo/delete?id=${classTaskInfo.id}"
							onclick="return confirmx('确认要删除该班组信息吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>