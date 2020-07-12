<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员同步信息管理</title>
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
		<li class="active"><a href="${ctx}/guard/downloadPerson/">人员同步信息列表</a></li>
		<%-- <shiro:hasPermission name="guard:downloadPerson:edit">
			<li><a href="${ctx}/guard/downloadPerson/form">人员同步信息添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="downloadPerson"
		action="${ctx}/guard/downloadPerson/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${downloadPerson.office.id}" labelName="office.name"
				labelValue="${downloadPerson.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>姓名：</label>
			<form:input path="staff.name" htmlEscape="false"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="downloadTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadPerson.downloadTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadPerson.downloadTimeTwo}"
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
				<th>姓名</th>
				<th>类型</th>
				<th>同步时间</th>
				<th>同步类型</th>
				<th>同步状态</th>
				<th>网点</th>
				<th  style="width: 70px;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="downloadPerson" varStatus="varStatus">
				<tr is-download="${downloadPerson.isDownload}">
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${downloadPerson.staff.name}</td>
					<td>${fns:getDictLabel(downloadPerson.staff.staffType, 'staff_type', '')}</td>
					<td>${downloadPerson.downloadTime}</td>
					<td>${fns:getDictLabel(downloadPerson.isDownload, 'isDownload', '')}
					</td>
					<c:if test="${downloadPerson.downloadType == '0'}">
						<td>添加</td>
							</c:if>
					<c:if test="${downloadPerson.downloadType == '1'}">
						<td>更新</td>
							</c:if>
					<c:if test="${downloadPerson.downloadType == '2'}">
						<td>删除</td>
					</c:if>
					<%-- <td>${fns:getDictLabel(downloadPerson.downloadType, 'downloadType', '')}
					
					</td> --%>
					<td>${downloadPerson.office.name}</td>
					<td>
						<shiro:hasPermission name="guard:downloadPerson:edit">
							<c:if test="${downloadPerson.isDownload == '0'}">
								<a href="${ctx}/guard/downloadPerson/delete?id=${downloadPerson.id}"
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