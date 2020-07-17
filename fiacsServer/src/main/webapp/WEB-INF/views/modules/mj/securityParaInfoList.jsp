<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>防盗参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出防盗参数数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/mj/securityParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/mj/securityParaInfo/list");
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
	<li class="active"><a href="${ctx}/mj/securityParaInfo/">防盗信息列表</a></li>
</ul>

<div class="form-group">
	<input id="btnExport" class="btn btn-primary" style="margin: 10px 0 0 10%" type="button" value="导出" />
</div>
<sys:message content="${message}"/>
<form:form id="searchForm" modelAttribute="securityParaInfo" action="${ctx}/mj/securityParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" name="eid" value="${eid}" />
</form:form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>布防离开等待时间</th>
		<th>门磁自动布防时间</th>
		<th>现场未布防提醒时间</th>
		<th>现场未布防提醒告警时间</th>
		<th>同类报警间隔时间</th>
		<shiro:hasPermission name="mj:securityParaInfo:edit"><th>操作</th></shiro:hasPermission>
	</tr>
	</thead>
	<tbody>
	<c:if test="${list!=null}">
		<c:forEach items="${list}" var="securityParaInfo" varStatus="aStatus">
			<tr>
				<td>
						${securityParaInfo.leaveRelayTime}
				</td>
				<td>
						${securityParaInfo.doorSensorTime}
				</td>
				<td>
						${securityParaInfo.localTipsTime}
				</td>
				<td>
						${securityParaInfo.tipsAlarmTime}
				</td>
				<td>
						${securityParaInfo.alarmIntervalTime}
				</td>
				<shiro:hasPermission name="mj:securityParaInfo:edit"><td>
					<a href="${ctx}/mj/securityParaInfo/form?id=${securityParaInfo.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</c:if>

	</tbody>
</table>
</body>
</html>