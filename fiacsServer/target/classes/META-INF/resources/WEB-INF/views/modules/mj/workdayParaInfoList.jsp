<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作日管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery-session/jquery-session.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出门禁时区数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/guard/equipment/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/guard/equipment/list");
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
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/mj/workdayParaInfo/">工作日列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="workdayParaInfo" action="${ctx}/mj/workdayParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" name="eId" value="${eId}">
	<ul class="ul-form">
		<li><label>月份：</label>
			<select name="mon" style="width: 100px">
				<option value="" <c:if test="${mon eq ''}">selected="selected"</c:if>>全部</option>
				<option value="1" <c:if test="${mon eq '1'}">selected="selected"</c:if>>1月</option>
				<option value="2" <c:if test="${mon eq '2'}">selected="selected"</c:if>>2月</option>
				<option value="3" <c:if test="${mon eq '3'}">selected="selected"</c:if>>3月</option>
				<option value="4" <c:if test="${mon eq '4'}">selected="selected"</c:if>>4月</option>
				<option value="5" <c:if test="${mon eq '5'}">selected="selected"</c:if>>5月</option>
				<option value="6" <c:if test="${mon eq '6'}">selected="selected"</c:if>>6月</option>
				<option value="7" <c:if test="${mon eq '7'}">selected="selected"</c:if>>7月</option>
				<option value="8" <c:if test="${mon eq '8'}">selected="selected"</c:if>>8月</option>
				<option value="9" <c:if test="${mon eq '9'}">selected="selected"</c:if>>9月</option>
				<option value="10" <c:if test="${mon eq '10'}">selected="selected"</c:if>>10月</option>
				<option value="11" <c:if test="${mon eq '11'}">selected="selected"</c:if>>11月</option>
				<option value="12" <c:if test="${mon eq '12'}">selected="selected"</c:if>>12月</option>
			</select>
		</li>
		<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
		<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
		<shiro:hasPermission name="mj:workdayParaInfo:edit">
			<input id="btnAdd" class="btn btn-primary" type="button" value="假期添加" />
		</shiro:hasPermission>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>序号</th>
		<th>日期</th>
		<shiro:hasPermission name="mj:workdayParaInfo:edit"><th>操作</th></shiro:hasPermission>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${restDay}" var="day"  varStatus="varStatus">
		<tr>
			<td>
					${varStatus.count}
			</td>
			<td>
					${day.date}
			</td>
			<shiro:hasPermission name="mj:workdayParaInfo:edit"><td>
				<a href="${ctx}/mj/workdayParaInfo/form?id=${day.id}&num=${dayStatus.count}">修改</a>
			</td></shiro:hasPermission>
		</tr>
		<c:forEach items="${month.restDay}" var="day" varStatus="dayStatus">

		</c:forEach>

	</c:forEach>
	</tbody>
</table>
<%--<div class="pagination">${page}</div>--%>
</body>
</html>