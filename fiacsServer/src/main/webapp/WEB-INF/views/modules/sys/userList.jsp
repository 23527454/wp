<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			top.$.jBox.confirm("确认要导出用户数据吗？", "系统提示", function(v, h, f) {
				if (v == "ok") {
					$("#searchForm").attr("action", "${ctx}/sys/user/export");
					$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			top.$('.jbox-body .jbox-icon').css('top', '55px');
		});
		$("#btnImport").click(function() {
			$.jBox($("#importBox").html(), {
				title : "导入数据",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
			});
		});
	});
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/sys/user/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/user/import" method="post"
			enctype="multipart/form-data" class="form-search"
			style="padding-left: 20px; text-align: center;"
			onsubmit="loading('正在导入，请稍等...');">
			<br /> <input id="uploadFile" name="file" type="file"
				style="width: 330px" /><br /> <br /> <input id="btnImportSubmit"
				class="btn btn-primary" type="submit" value="   导    入   " /> <a
				href="${ctx}/sys/user/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/list">用户列表</a></li>
		<shiro:hasPermission name="sys:user:edit">
			<li><a href="${ctx}/sys/user/form">用户添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="user"
		action="${ctx}/sys/user/list" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>用户名：</label>
			<form:input path="loginName" htmlEscape="false" maxlength="50"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>姓名：</label>
			<form:input path="name" htmlEscape="false" maxlength="50"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"
			onclick="return page();" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>归属区域</th>
				<th>归属网点</th>
				<th class="sort-column login_name">用户名</th>
				<th class="sort-column name">姓名</th>
				<th>电话</th>
				<th>手机</th>
				<%--<th>角色</th> --%>
				<shiro:hasPermission name="sys:user:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="user">
				<tr>
					<td>${user.office.area.name}</td>
					<td>${user.office.name}</td>
					<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
					<td>${user.name}</td>
					<td>${user.phone}</td>
					<td>${user.mobile}</td>
					<%--
				<td>${user.roleNames}</td> --%>
					<shiro:hasPermission name="sys:user:edit">
						<td><a href="${ctx}/sys/user/form?id=${user.id}">修改</a> <a
							href="${ctx}/sys/user/delete?id=${user.id}"
							onclick="return confirmx('确认要删除该用户吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>