<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门禁定时设置管理</title>
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
		<li class="active"><a href="${ctx}/guard/tbDownloadDoorTimeZone/">门禁定时同步列表</a></li>
		<%-- <shiro:hasPermission name="guard:downloadPerson:edit">
			<li><a href="${ctx}/guard/downloadPerson/form">人员同步信息添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="tbDownloadDoorTimeZone"
		action="${ctx}/guard/tbDownloadDoorTimeZone/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${tbDownloadDoorTimeZone.office.id}" labelName="office.name"
				labelValue="${tbDownloadDoorTimeZone.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="downloadTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${tbDownloadDoorTimeZone.downloadTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${tbDownloadDoorTimeZone.downloadTimeTwo}"
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
				<th>门号</th>
				<th>周几</th>
				<th>同步时间</th>
				<th>同步类型</th>
				<th>同步状态</th>
				<th  style="width: 70px;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="tbDownloadDoorTimeZone" varStatus="varStatus">
				<tr is-download="${tbDownloadDoorTimeZone.isDownload}">
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${tbDownloadDoorTimeZone.office.name}</td>
					<td>${fns:getDictLabel(tbDownloadDoorTimeZone.doorPos, 'door_pos', '')}</td>
					<td>${fns:getDictLabel(tbDownloadDoorTimeZone.weekNumber, 'week_number', '')}</td>
					<td>${tbDownloadDoorTimeZone.downloadTime}</td>
					<td>${fns:getDictLabel(tbDownloadDoorTimeZone.isDownload, 'isDownload', '')}
					</td>
					<c:if test="${tbDownloadDoorTimeZone.downloadType == '0'}">
						<td>添加</td>
							</c:if>
					<c:if test="${tbDownloadDoorTimeZone.downloadType == '1'}">
						<td>更新</td>
							</c:if>
					<c:if test="${tbDownloadDoorTimeZone.downloadType == '2'}">
						<td>删除</td>
					</c:if>
					<%-- <td>${fns:getDictLabel(downloadPerson.downloadType, 'downloadType', '')}
					
					</td> --%>
					<td>
						<shiro:hasPermission name="guard:downloadPerson:edit">
							<c:if test="${tbDownloadDoorTimeZone.isDownload == '0'}">
								<a href="${ctx}/guard/tbDownloadDoorTimeZone/delete?id=${tbDownloadDoorTimeZone.id}"
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