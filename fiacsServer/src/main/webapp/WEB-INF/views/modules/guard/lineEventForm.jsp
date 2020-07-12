<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>线路监控管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
			if($("#contentTable tbody tr:contains('未完成')").length > 0){
				setInterval(function(){
					$("#inputForm").submit();
				}, 5000);
			}
	});

	function goBack(){
		window.location.href = document.referrer;
	}
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/lineEvent/list?nodes=${lineEvent.nodes}">线路监控列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/lineEvent/form?id=${lineEvent.id}&nodes=${lineEvent.nodes}">线路监控<shiro:hasPermission
					name="guard:lineEvent:edit">${not empty lineEvent.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:lineEvent:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="lineEvent"
		action="${ctx}/guard/lineEvent/form" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
	</form:form>
	
	<div class="col-xs-6 col-md-offset-2">
		<div class="form-group">
			<label class="control-label">线路名称：${lineEvent.lineName }</label>
			<table id="contentTable" class="table table-striped table-bordered table-condensed"
				style="width: 100%;">
				<thead>
					<tr>
						<th data-options="field:'name'" width="50%">节点名称</th>
						<th data-options="field:'type'" width="50%">完成状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lineEvent.taskLineInfoList}" var="lin">
						<tr>
							<td>${lin.name}</td>
							<td>${lin.type}</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</div>
	<div class="form-group">
		<div class="col-xs-offset-2 col-xs-10">
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="goBack();" />
		</div>
	</div>
</div>
</body>
</html>