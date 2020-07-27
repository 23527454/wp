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
										"${ctx}/tbmj/securityParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/tbmj/securityParaInfo/list");
							}
						}, {
							buttonsFocus : 1
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					});


			$("#btnDownload").click(
					function() {
						var oId=$("#oId").val();
						if(oId==null || oId==""){
							alert("请先选择一个机构!");
						}else{
							top.$.jBox.confirm("确认要同步该设备的防盗数据吗？", "系统提示", function(
									v, h, f) {
								if (v == "ok") {
									$("#searchForm").attr("action",
											"${ctx}/tbmj/securityParaInfo/download");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
											"${ctx}/tbmj/securityParaInfo/list");
								}
							}, {
								buttonsFocus : 1
							});
							top.$('.jbox-body .jbox-icon').css('top', '55px');
						}

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
	<li class="active"><a href="${ctx}/tbmj/securityParaInfo/">防盗信息列表</a></li>
</ul>
<sys:message content="${message}"/>
<form:form id="searchForm" modelAttribute="securityParaInfo" action="${ctx}/tbmj/securityParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" name="oId" id="oId" value="${oId}" />
</form:form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>布防离开等待时间</th>
		<th>门磁自动布防时间</th>
		<th>现场未布防提醒时间</th>
		<th>现场未布防提醒告警时间</th>
		<th>同类报警间隔时间</th>
		<shiro:hasPermission name="tbmj:securityParaInfo:edit"><th>操作</th></shiro:hasPermission>
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
				<shiro:hasPermission name="tbmj:securityParaInfo:edit"><td>
					<a href="${ctx}/tbmj/securityParaInfo/form?id=${securityParaInfo.id}">修改</a>
					<a href="#" id="btnDownload">同步</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</c:if>

	</tbody>
</table>
</body>
</html>