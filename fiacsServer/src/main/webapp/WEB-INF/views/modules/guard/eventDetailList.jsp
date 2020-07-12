<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>事件详情管理</title>
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
		<li class="active"><a href="${ctx}/guard/eventDetail/">事件详情列表</a></li>
		<shiro:hasPermission name="guard:eventDetail:edit"><li><a href="${ctx}/guard/eventDetail/form">事件详情添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="eventDetail" action="${ctx}/guard/eventDetail/" method="post" class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<div class="form-group"><label>编号：</label>
				<form:input path="id" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>指纹ID：</label>
				<form:input path="fingerId" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>设备ID：</label>
				<form:input path="equipmentId" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>事件ID：</label>
				<form:input path="recordId" htmlEscape="false" class="form-control"/>
			</div>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>指纹ID</th>
				<th>设备ID</th>
				<th>事件ID</th>
				<th>抓拍图片数据</th>
				<th>抓拍图片数据长度</th>
				<th>记录时间</th>
				<shiro:hasPermission name="guard:eventDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="eventDetail">
			<tr>
				<td><a href="${ctx}/guard/eventDetail/form?id=${eventDetail.id}">
					${eventDetail.id}
				</a></td>
				<td>
					${eventDetail.fingerId}
				</td>
				<td>
					${eventDetail.equipmentId}
				</td>
				<td>
					${eventDetail.recordId}
				</td>
				<td>
					${eventDetail.imageData}
				</td>
				<td>
					${eventDetail.imageSize}
				</td>
				<td>
					${eventDetail.time}
				</td>
				<shiro:hasPermission name="guard:eventDetail:edit"><td>
    				<a href="${ctx}/guard/eventDetail/form?id=${eventDetail.id}">修改</a>
					<a href="${ctx}/guard/eventDetail/delete?id=${eventDetail.id}" onclick="return confirmx('确认要删除该事件详情吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>