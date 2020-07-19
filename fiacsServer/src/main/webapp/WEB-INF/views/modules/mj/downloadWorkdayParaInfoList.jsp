<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作日设置管理</title>
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
	<li class="active"><a href="${ctx}/mj/downloadWorkdayParaInfo/">门禁参数同步列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="downloadWorkdayParaInfo"
		   action="${ctx}/mj/downloadWorkdayParaInfo/" method="post"
		   class="breadcrumb form-search form-inline">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
						value="${downloadWorkdayParaInfo.office.id}" labelName="office.name"
						labelValue="${downloadWorkdayParaInfo.office.name}" title="网点"
						url="/sys/office/treeData" cssClass="" allowClear="true"
						notAllowSelectRoot="true" />
	</div>
	<div class="form-group">
		<label>开始时间：</label> <input name="downloadTime" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadWorkdayParaInfo.downloadTime}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
	</div>
	<div class="form-group">
		<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadWorkdayParaInfo.downloadTimeTwo}"
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
		<th>同步时间</th>
		<th>同步类型</th>
		<th>同步状态</th>
		<th style="width: 70px;">操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${page.list}" var="downloadWorkdayParaInfo" varStatus="varStatus">
		<tr is-download="${downloadWorkdayParaInfo.isDownload}">
			<td style="text-align: center;">${varStatus.count }</td>
			<td>${downloadWorkdayParaInfo.office.name}</td>
			<td>${fns:getDictLabel(downloadWorkdayParaInfo.equipment.siteType, 'site_type', '')}
			</td>
			<td>${downloadWorkdayParaInfo.downloadTime}</td>
			<td>${fns:getDictLabel(downloadWorkdayParaInfo.isDownload, 'isDownload', '')}
			</td>
			<c:if test="${downloadWorkdayParaInfo.downloadType == '0'}">
				<td>添加</td>
			</c:if>
			<c:if test="${downloadWorkdayParaInfo.downloadType == '1'}">
				<td>更新</td>
			</c:if>
			<c:if test="${downloadWorkdayParaInfo.downloadType == '2'}">
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
				<shiro:hasPermission name="mj:downloadWorkdayParaInfo:edit">
					<c:if test="${downloadWorkdayParaInfo.isDownload == '0'}">
						<a href="${ctx}/mj/downloadWorkdayParaInfo/delete?id=${downloadWorkdayParaInfo.id}"
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