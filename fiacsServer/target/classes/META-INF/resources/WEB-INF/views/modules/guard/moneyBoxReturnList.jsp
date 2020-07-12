<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱上缴信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

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
		<li class="active"><a href="${ctx}/guard/moneyBoxReturn/">款箱上缴信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="moneyBoxReturn"
		action="${ctx}/guard/moneyBoxReturn/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
			value="${moneyBoxReturn.office.id}" labelName="office.name"
			labelValue="${moneyBoxReturn.office.name}" title="网点"
			url="/sys/office/treeData" cssClass="form-control input-small" allowClear="true"
			notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
		<label>款箱编码：</label>
		<form:input path="moneyBox.boxCode" cssClass="form-control input-sm" htmlEscape="false"
			class="form-medium" />
		</div>
		<div class="form-group">
		<label>款箱卡号：</label>
		<form:input path="moneyBox.cardNum" cssClass="form-control input-sm" htmlEscape="false"
			class="form-medium" />
		</div>
		
		<div class="form-group">
			<label>开始时间：</label> <input name="returnTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
				value="<fmt:formatDate value="${moneyBoxReturn.returnTime}" pattern="yyyy-MM-dd HH:mm:ss" />"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		
		<div class="form-group">
			<label>截止时间：</label> <input name="returnTimeTo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"  style="height:auto"
				value="<fmt:formatDate value="${moneyBoxReturn.returnTimeTo}" pattern="yyyy-MM-dd HH:mm:ss" />"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
			
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />


	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>款箱编号</th>
				<th>款箱卡号</th>
				<th>款箱类型</th>
				<th>所属网点</th>
				<th>上缴类型</th>
				<th>上缴时间</th>
<!-- 				<th>上传时间</th> -->
<!-- 				<th>上缴状态</th> -->
				<shiro:hasPermission name="guard:moneyBoxReturn:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="moneyBoxReturn">
				<tr>
					<td>${moneyBoxReturn.moneyBox.boxCode}</td>
					<td>${moneyBoxReturn.moneyBox.cardNum}</td>
					<td>${fns:getDictLabel(moneyBoxReturn.moneyBox.boxType, 'box_type', '')}
					</td>
					<td>${moneyBoxReturn.office.name}</td>
					<td>${fns:getDictLabel(moneyBoxReturn.returnType, 'return_type', '')}</td>
					<td><fmt:formatDate value="${moneyBoxReturn.returnTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
<%-- 					<td><fmt:formatDate value="${moneyBoxReturn.uploadTime}" --%>
<!-- 							pattern="yyyy-MM-dd HH:mm:ss" /></td> -->
<%-- 					<td>${fns:getDictLabel(moneyBoxReturn.returnSysStatus, 'sys_status', '')}</td> --%>
					<shiro:hasPermission name="guard:moneyBoxReturn:edit">
						<td><a
							href="${ctx}/guard/moneyBoxReturn/delete?id=${moneyBoxReturn.id}"
							onclick="return confirmx('确认要取消该款箱上缴信息吗？', this.href)">取消</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>