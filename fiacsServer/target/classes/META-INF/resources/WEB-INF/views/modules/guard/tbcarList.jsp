<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>车辆信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
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
		<li class="active"><a href="${ctx}/guard/tbcar/">车辆信息列表</a></li>
		<shiro:hasPermission name="guard:tbcar:edit"><li><a href="${ctx}/guard/tbcar/form">车辆信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tbcar" action="${ctx}/guard/tbcar/" method="post" class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<div class="form-group"><label>编号：</label>
				<form:input path="id" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>卡号：</label>
				<form:input path="szcardnum" htmlEscape="false" maxlength="20" class="form-control"/>
			</div>
			<div class="form-group"><label>车牌号：</label>
				<form:input path="szcarplate" htmlEscape="false" maxlength="10" class="form-control"/>
			</div>
			<div class="form-group"><label>负责人：</label>
				<form:input path="szadmin" htmlEscape="false" maxlength="32" class="form-control"/>
			</div>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>车辆编号</th>
				<th>卡号</th>
				<th>车牌号</th>
				<th>车辆类型</th>
				<th>负责人</th>
				<th>负责人联系方式</th>
				<th>车辆品牌</th>
				<th>更新时间</th>
				<shiro:hasPermission name="guard:tbcar:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tbcar">
			<tr>
				<td><a href="${ctx}/guard/tbcar/form?id=${tbcar.id}">
					${tbcar.id}
				</a></td>
				<td>
					${tbcar.ncarno}
				</td>
				<td>
					${tbcar.szcardnum}
				</td>
				<td>
					${tbcar.szcarplate}
				</td>
				<td>
					${fns:getDictLabel(tbcar.szcarmodel, 'car_model', '')}
				</td>
				<td>
					${tbcar.szadmin}
				</td>
				<td>
					${tbcar.szadminphone}
				</td>
				<td>
					${fns:getDictLabel(tbcar.carbrand, 'car_brand', '')}
				</td>
				<td>
					<fmt:formatDate value="${tbcar.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="guard:tbcar:edit"><td>
    				<a href="${ctx}/guard/tbcar/form?id=${tbcar.id}">修改</a>
					<a href="${ctx}/guard/tbcar/delete?id=${tbcar.id}" onclick="return confirmx('确认要删除该车辆信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>