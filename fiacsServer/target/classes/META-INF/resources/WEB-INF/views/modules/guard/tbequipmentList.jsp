<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>设备信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#btnExport").click(
						function() {
							top.$.jBox.confirm("确认要导出设备数据吗？", "系统提示", function(
									v, h, f) {
								if (v == "ok") {
									$("#searchForm").attr("action",
											"${ctx}/guard/tbequipment/export");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
									"${ctx}/guard/tbequipment/list");
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
		$("#searchForm").attr("action", "${ctx}/guard/tbequipment/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/tbequipment/">设备信息列表</a></li>
		<shiro:hasPermission name="guard:tbequipment:edit"><li><a href="${ctx}/guard/tbequipment/form">设备信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tbequipment" action="${ctx}/guard/tbequipment/" method="post" class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<div class="form-group"><label>id：</label>
				<form:input path="id" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>网点名称：</label>
				<sys:treeselect id="area" name="area.id" value="${tbequipment.area.id}" labelName="area.name" labelValue="${tbequipment.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectRoot="true"/>
			</div>
			<div class="form-group"><label>序列号：</label>
				<form:input path="szserialnum" htmlEscape="false" maxlength="20" class="form-control"/>
			</div>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>IP地址</th>
				<th>端口</th>
				<th>网点名称</th>
				<th>序列号</th>
				<th>网关</th>
				<th>子网掩码</th>
				<th>打印服务器IP</th>
				<th>打印服务器端口</th>
				<th>网点类型</th>
				<th>更新日期</th>
				<th>备注</th>
				<shiro:hasPermission name="guard:tbequipment:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tbequipment">
			<tr>
				<td><a href="${ctx}/guard/tbequipment/form?id=${tbequipment.id}">
					${tbequipment.id}
				</a></td>
				<td>
					${tbequipment.szip}
				</td>
				<td>
					${tbequipment.nport}
				</td>
				<td>
					${tbequipment.area.name}
				</td>
				<td>
					${tbequipment.szserialnum}
				</td>
				<td>
					${tbequipment.sznetgate}
				</td>
				<td>
					${tbequipment.sznetmask}
				</td>
				<td>
					${tbequipment.szprintserverip}
				</td>
				<td>
					${tbequipment.nprintserverport}
				</td>
				<td>
					${fns:getDictLabel(tbequipment.nsitetype, 'site_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${tbequipment.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tbequipment.remarks}
				</td>
				<shiro:hasPermission name="guard:tbequipment:edit"><td>
    				<a href="${ctx}/guard/tbequipment/form?id=${tbequipment.id}">修改</a>
					<a href="${ctx}/guard/tbequipment/delete?id=${tbequipment.id}" onclick="return confirmx('确认要删除该设备信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>

		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>