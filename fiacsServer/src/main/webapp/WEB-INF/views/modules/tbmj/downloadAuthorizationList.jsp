<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>权限设置管理</title>
	<meta name="decorator" content="default" />
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#contentTable tr[is-download='0']").length > 0){
				setInterval(function(){
					if(top.$.jBox.FadeBoxCount ==0){
						$("#searchForm").submit();
					}
				}, 5000);
			}
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
	<li class="active"><a href="${ctx}/tbmj/downloadAuthorization/">权限同步列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="downloadAuthorization"
		   action="${ctx}/tbmj/downloadAuthorization/" method="post"
		   class="breadcrumb form-search form-inline">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
						value="${downloadAuthorization.office.id}" labelName="office.name"
						labelValue="${downloadAuthorization.office.name}" title="网点"
						url="/sys/office/treeData" cssClass="" allowClear="true"
						notAllowSelectRoot="true" />
	</div>
	<div class="form-group">
		<label>开始时间：</label> <input name="downloadTime" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadAuthorization.downloadTime}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
	</div>
	<div class="form-group">
		<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadAuthorization.downloadTimeTwo}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
	</div>

	<div class="form-group">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
	</div>
</form:form>
<sys:message content="${message}" />
<table id="contentTable"
	   class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th style="width: 30px;">序号</th>
		<th>网点</th>
		<th>设备类型</th>
		<th>门号</th>
		<th>员工姓名</th>
		<th>员工工号</th>
		<th>同步时间</th>
		<th>同步类型</th>
		<th>同步状态</th>
		<th style="width: 70px;">操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${page.list}" var="downloadAuthorization" varStatus="varStatus">
		<tr is-download="${downloadAuthorization.isDownload}">
			<td style="text-align: center;">${varStatus.count }</td>
			<td>${downloadAuthorization.office.name}</td>
			<td>${fns:getDictLabel(downloadAuthorization.equipment.siteType, 'site_type', '')}</td>
			<td>${fns:getDictLabel(downloadAuthorization.authorization.doorPos, 'door_pos', '')}</td>
			<td>${downloadAuthorization.staff.name}</td>
			<td>${downloadAuthorization.staff.workNum}</td>
			<td>${downloadTimezoneInfo.downloadTime}</td>
			<td>${fns:getDictLabel(downloadAuthorization.isDownload, 'isDownload', '')}
			</td>
			<c:if test="${downloadAuthorization.downloadType == '0'}">
				<td>添加</td>
			</c:if>
			<c:if test="${downloadAuthorization.downloadType == '1'}">
				<td>更新</td>
			</c:if>
			<c:if test="${downloadAuthorization.downloadType == '2'}">
				<td>删除</td>
			</c:if>
			<%--<td>
				<shiro:hasPermission name="guard:downloadPerson:edit">
					<c:if test="${downloadPerson.isDownload == '0'}">
						<a href="${ctx}/guard/downloadPerson/delete?id=${downloadPerson.id}"
						   onclick="return confirmx('确认要取消该同步记录吗？', this.href)">取消</a>
					</c:if>
				</shiro:hasPermission>
			</td>--%>
			<td>
				<shiro:hasPermission name="tbmj:downloadAuthorization:edit">
					<c:if test="${downloadAuthorization.isDownload == '0'}">
						<a href="${ctx}/tbmj/downloadAuthorization/delete?id=${downloadAuthorization.id}"
						   onclick="return confirmx('确认要取消该同步记录吗？', this.href)">取消</a>
					</c:if>
				</shiro:hasPermission>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>