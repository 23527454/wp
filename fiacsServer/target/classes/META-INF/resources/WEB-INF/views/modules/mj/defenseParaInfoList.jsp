<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>防区参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出防区参数数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/mj/defenseParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/mj/defenseParaInfo/list");
							}
						}, {
							buttonsFocus : 1
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					});

			$("#btnDownload").click(
					function() {
						var eid=$("#eid").val();
						if(eid==null || eid==""){
							alert("请先选择一个设备!");
						}else{
							top.$.jBox.confirm("确认要同步该设备的防区数据吗？", "系统提示", function(
									v, h, f) {
								if (v == "ok") {
									$("#searchForm").attr("action",
											"${ctx}/mj/defenseParaInfo/download");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
											"${ctx}/mj/defenseParaInfo/list");
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
	<li class="active"><a href="${ctx}/mj/defenseParaInfo/">防区信息列表</a></li>
</ul>

<div class="form-group">
	<input id="btnExport" class="btn btn-primary" style="margin: 10px 0 0 10%" type="button" value="导出" />
</div>
<sys:message content="${message}"/>
<form:form id="searchForm" action="${ctx}/mj/defenseParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" name="eid" value="${eid}" />
</form:form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>防区名称</th>
		<th>防区类型</th>
		<th>是否旁路</th>
		<th>防区属性</th>
		<th>报警延时时间（秒）</th>
		<shiro:hasPermission name="mj:defenseParaInfo:edit"><th>操作</th></shiro:hasPermission>
	</tr>
	</thead>
	<tbody>
	<c:if test="${list!=null}">
		<c:forEach items="${list}" var="defenseParaInfo" varStatus="aStatus">
			<tr>
				<td>
					<c:forEach items="${fns:getDictList('defence_name')}" var="type">
						<c:if test="${type.value eq defenseParaInfo.defensePos}">${type.label}</c:if>
					</c:forEach>
				</td>
				<td>
					<c:forEach items="${fns:getDictList('defence_type')}" var="type">
						<c:if test="${type.value eq defenseParaInfo.defenseAreaType}">${type.label}</c:if>
					</c:forEach>
				</td>
				<td>
					<c:forEach items="${fns:getDictList('yes_no')}" var="week">
						<c:if test="${week.value eq defenseParaInfo.defenseAreaBypass}">${week.label}</c:if>
					</c:forEach>
				</td>
				<td>
					<c:forEach items="${fns:getDictList('defence_attr')}" var="type">
						<c:if test="${type.value eq defenseParaInfo.defenseAreaAttr}">${type.label}</c:if>
					</c:forEach>
				</td>
				<td>
						${defenseParaInfo.alarmDelayTime}
				</td>
				<shiro:hasPermission name="mj:defenseParaInfo:edit"><td>
					<a href="${ctx}/mj/defenseParaInfo/form?id=${defenseParaInfo.id}">修改</a>
					<a href="#" id="btnDownload">同步</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</c:if>

	</tbody>
</table>
</body>
</html>