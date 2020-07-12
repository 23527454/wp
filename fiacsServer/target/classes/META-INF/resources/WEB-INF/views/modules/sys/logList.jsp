<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(
			function() {	
				$("#btnExport").click(
						function() {
							top.$.jBox.confirm("确认要导出日志数据吗？", "系统提示", function(
									v, h, f) {
								if (v == "ok") {
									$("#searchForm").attr("action",
											"${ctx}/sys/log/export");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
											"${ctx}/sys/log/list");
								}
							}, {
								buttonsFocus : 1
							});
							top.$('.jbox-body .jbox-icon').css('top', '55px');
						});
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
<!-- 	<ul class="nav nav-tabs"> -->
<%-- 		<li class="active"><a href="${ctx}/sys/log/">日志列表</a></li> --%>
<!-- 	</ul> -->
	<form:form id="searchForm" action="${ctx}/sys/log/" method="post" class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>操作用户：</label><input id="createBy.name" name="createBy.name" type="text" maxlength="50" class="form-control input-sm" value="${log.createBy.name}"/>
		</div>
		<div class="form-group">
			<label>操作时间：&nbsp;</label><input id="beginDate" name="beginDate" type="text" style="height: auto; width: 100px;"  readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>到&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" style="height: auto;width: 100px;" readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>&nbsp;&nbsp;
		</div>
		<div class="form-group">
			<label for="exception"><input id="exception" name="exception" type="checkbox"${log.exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th>操作信息</th>
			<th>操作用户</th>
			<th>用户IP</th>
			<th>操作时间</th>
			</tr>
		</thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:forEach items="${page.list}" var="log">
			<tr>
				<td>${log.title}</td>
				<td>${log.createBy.name}(${log.createBy.loginName})</td>
				<td>${log.remoteAddr}</td>
				<td><fmt:formatDate value="${log.createDate}" type="both"/></td>
			</tr>
			<c:if test="${not empty log.exception}"><tr>
				<td colspan="8" style="word-wrap:break-word;word-break:break-all;">
<%-- 					用户代理: ${log.userAgent}<br/> --%>
<%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
					异常信息: <br/>
					${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}</td>
			</tr></c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>