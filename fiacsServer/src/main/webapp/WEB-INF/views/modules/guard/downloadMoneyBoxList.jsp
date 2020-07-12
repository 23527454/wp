0<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱同步信息管理</title>
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
		<li class="active"><a href="${ctx}/guard/downloadMoneyBox/">款箱同步信息列表</a></li>
		<%-- <shiro:hasPermission name="guard:downloadMoneyBox:edit">
			<li><a href="${ctx}/guard/downloadMoneyBox/form">款箱同步信息添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="downloadMoneyBox"
		action="${ctx}/guard/downloadMoneyBox/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${downloadMoneyBox.office.id}" labelName="office.name"
				labelValue="${downloadMoneyBox.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>款箱编码：</label>
			<form:input path="moneyBox.boxCode" htmlEscape="false" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="downloadTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadMoneyBox.downloadTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadMoneyBox.downloadTimeTwo}"
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
				<th>款箱编码</th>
				<th>款箱卡号</th>
				<th>款箱类型</th>
				<th>同步时间</th>
				<th>同步类型</th>
				<th>同步状态</th>
				<th>网点</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="downloadMoneyBox" varStatus="varStatus">
				<tr is-download="${downloadMoneyBox.isDownload }">
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${downloadMoneyBox.moneyBox.boxCode}</td>
					<td>${downloadMoneyBox.moneyBox.cardNum}</td>
					<td>${fns:getDictLabel(downloadMoneyBox.moneyBox.boxType, 'box_type', '')}</td>
					<td>${downloadMoneyBox.downloadTime}</td>
					<td>${fns:getDictLabel(downloadMoneyBox.isDownload, 'isDownload', '')}
					</td>
					<%-- <td>${fns:getDictLabel(downloadMoneyBox.downloadType, 'downloadType', '')}</td> --%>
					<c:if test="${downloadMoneyBox.downloadType == '0'}">
						<td>添加</td>
							</c:if>
					<c:if test="${downloadMoneyBox.downloadType == '1'}">
						<td>更新</td>
							</c:if>
					<c:if test="${downloadMoneyBox.downloadType == '2'}">
						<td>删除</td>
							</c:if>
					</td>
					
					<td>${downloadMoneyBox.office.name}</td>
					<td>
						<shiro:hasPermission name="guard:downloadMoneyBox:edit">
							<c:if test="${downloadMoneyBox.isDownload == '0'}">
								<a href="${ctx}/guard/downloadMoneyBox/delete?id=${downloadMoneyBox.id}"
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