<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>防盗设置管理</title>
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
	<li class="active"><a href="${ctx}/tbmj/downloadSecurityParaInfo/">防盗参数同步列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="downloadSecurityParaInfo"
		   action="${ctx}/tbmj/downloadSecurityParaInfo/" method="post"
		   class="breadcrumb form-search form-inline">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
						value="${downloadSecurityParaInfo.office.id}" labelName="office.name"
						labelValue="${downloadSecurityParaInfo.office.name}" title="网点"
						url="/sys/office/treeData" cssClass="" allowClear="true"
						notAllowSelectRoot="true" />
	</div>
	<div class="form-group">
		<label>开始时间：</label> <input name="downloadTime" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadSecurityParaInfo.downloadTime}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
	</div>
	<div class="form-group">
		<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
									readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
									value="${downloadSecurityParaInfo.downloadTimeTwo}"
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
	<c:forEach items="${page.list}" var="downloadSecurityParaInfo" varStatus="varStatus">
		<tr is-download="${downloadSecurityParaInfo.isDownload}">
			<td style="text-align: center;">${varStatus.count }</td>
			<td>${downloadSecurityParaInfo.office.name}</td>
			<td>${fns:getDictLabel(downloadSecurityParaInfo.equipment.siteType, 'site_type', '')}
			</td>
			<td>${downloadSecurityParaInfo.downloadTime}</td>
			<td>${fns:getDictLabel(downloadSecurityParaInfo.isDownload, 'isDownload', '')}
			</td>
			<c:if test="${downloadSecurityParaInfo.downloadType == '0'}">
				<td>添加</td>
			</c:if>
			<c:if test="${downloadSecurityParaInfo.downloadType == '1'}">
				<td>更新</td>
			</c:if>
			<c:if test="${downloadSecurityParaInfo.downloadType == '2'}">
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
				<shiro:hasPermission name="tbmj:downloadSecurityParaInfo:edit">
					<c:if test="${downloadSecurityParaInfo.isDownload == '0'}">
						<a href="${ctx}/tbmj/downloadSecurityParaInfo/delete?id=${downloadSecurityParaInfo.id}"
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