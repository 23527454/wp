<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>门禁参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出门禁参数数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/mj/accessParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/mj/accessParaInfo/list");
							}
						}, {
							buttonsFocus : 1
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					});
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
		<li class="active"><a href="${ctx}/mj/accessParaInfo/">门禁信息列表</a></li>
	</ul>

	<div class="form-group">
		<input id="btnExport" class="btn btn-primary" style="margin: 10px 0 0 10%" type="button" value="导出" />
	</div>
	<sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="accessParaInfo" action="${ctx}/mj/accessParaInfo/" method="post" class="breadcrumb form-search">
		<input:hidden path="id" />
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>门继电器动作时间</th>
				<th>门开延时报警时间</th>
				<th>入库操作时间</th>
				<th>查库操作时间</th>
				<th>验证方式</th>
				<shiro:hasPermission name="mj:accessParaInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:if test="${accessParaInfo!=null}">
			<tr>
				<td>
						${accessParaInfo.doorRelayTime}
				</td>
				<td>
						${accessParaInfo.doorDelayTime}
				</td>
				<td>
						${accessParaInfo.enterOperaTime}
				</td>
				<td>
						${accessParaInfo.checkOperaTime}
				</td>
				<td>
					<c:forEach items="${fns:getDictList('door_open_type')}" var="type">
						<c:if test="${type.value eq accessParaInfo.authType}">${type.label}</c:if>
					</c:forEach>
				</td>
				<shiro:hasPermission name="mj:accessParaInfo:edit"><td>
					<a href="${ctx}/mj/accessParaInfo/form?id=${accessParaInfo.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:if>
		</tbody>
	</table>
</body>
</html>