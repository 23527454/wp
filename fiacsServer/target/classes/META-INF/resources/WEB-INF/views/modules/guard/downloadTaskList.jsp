<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班同步信息管理</title>
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
		<li class="active"><a href="${ctx}/guard/downloadTask/">排班同步信息列表</a></li>
		<%-- <shiro:hasPermission name="guard:downloadTask:edit">
			<li><a href="${ctx}/guard/downloadTask/form">排班同步信息添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="downloadTask"
		action="${ctx}/guard/downloadTask/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${downloadTask.office.id}" labelName="office.name"
				labelValue="${downloadTask.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>任务类型：</label>
			<form:select path="task.taskType" class="form-control input-sm">
				<form:option value="" label="全部" />
				<form:options items="${fns:getDictList('task_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="downloadTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadTask.downloadTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadTask.downloadTimeTwo}"
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
				<th>班组名称</th>
				<th>排班日期</th>
				<th>排班时间</th>
				<th>班次</th>
				<th>班组类型</th>
				<th>同步时间</th>
				<th>同步类型</th>
				<th>同步状态</th>
				<th>网点</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="downloadTask" varStatus="varStatus">
				<tr is-download="${downloadTask.isDownload }">
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${downloadTask.classTaskInfo.name}</td>
					<td>${downloadTask.task.allotDate}</td>
					<td>${downloadTask.task.allotTime}</td>
					<td>${downloadTask.task.id}</td>
					<td>${fns:getDictLabel(downloadTask.task.taskType, 'task_type', '')}</td>
					<td>${downloadTask.downloadTime}</td>
					<td>${fns:getDictLabel(downloadTask.isDownload, 'isDownload', '')}
					</td>
				<%-- 	<td>${fns:getDictLabel(downloadTask.downloadType, 'downloadType', '')}
					</td> --%>
					<c:if test="${downloadTask.downloadType == '0'}">
						<td>添加</td>
							</c:if>
					<c:if test="${downloadTaskdownloadTask.downloadType == '1'}">
						<td>更新</td>
							</c:if>
					<c:if test="${downloadTask.downloadType == '2'}">
						<td>删除</td>
							</c:if>
					<td>${downloadTask.office.name}</td>
					<td>
						<shiro:hasPermission name="guard:downloadTask:edit">
							<c:if test="${downloadTask.isDownload == '0'}">
								<a href="${ctx}/guard/downloadTask/delete?id=${downloadTask.id}"
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