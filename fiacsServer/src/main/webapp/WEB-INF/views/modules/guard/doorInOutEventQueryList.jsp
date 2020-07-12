<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门禁出入事件查询管理</title>
<meta name="decorator" content="default" />
<style>
@media print {
	a[href]:after {
		content: none !important;
	}
}
</style>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#btnExport")
								.click(
										function() {
											top.$.jBox
													.confirm(
															"确认要导出门禁出入事件数据吗？",
															"系统提示",
															function(v, h, f) {
																if (v == "ok") {
																	$("#searchForm")
																			.attr(
																					"action",
																					"${ctx}/guard/doorInOutEventQuery/export");
																	$("#searchForm")
																			.submit();
																	$("#searchForm")
																	.attr(
																			"action",
																			"${ctx}/guard/doorInOutEventQuery/list");
																}
															},
															{
																buttonsFocus : 1
															});
											top.$('.jbox-body .jbox-icon').css(
													'top', '55px');
										});
					});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function preview(oper) {
		if (oper < 10) {
			var headstr = "<html><head><title>门禁出入事件列表</title></head><body>";
			var footstr = "</body>";
			bdhtml = window.document.body.innerHTML;//获取当前页的html代码 
			sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域 
			eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域 
			prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html 
			prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html 
			window.document.body.innerHTML = headstr + prnhtml + footstr;
			window.print();
			window.document.body.innerHTML = bdhtml;
		} else {
			window.print();
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/doorInOutEventQuery/">门禁出入事件查询列表</a></li>
		<shiro:hasPermission name="guard:doorInOutEventQuery:edit">
			<li><a href="${ctx}/guard/doorInOutEventQuery/form">门禁出入事件查询添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="doorInOutEventQuery"
		action="${ctx}/guard/doorInOutEventQuery/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			<div class="form-group">
			<label>开始时间：</label>
			<input name="time" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${doorInOutEventQuery.time}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label>
			<input name="timeTwo" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${doorInOutEventQuery.timeTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>区域：</label>
			<sys:treeselect id="area" name="area.id"
				value="${doorInOutEventQuery.area.id}" labelName="area.name"
				labelValue="${doorInOutEventQuery.area.name}" title="区域"
				url="/sys/area/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
			value="${doorInOutEventQuery.office.id}" labelName="office.name"
			labelValue="${doorInOutEventQuery.office.name}" title="网点"
			url="/sys/office/treeData" cssClass="" allowClear="true"
			notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>排序类型：</label>
			<form:select path="sortType" class="form-control input-sm">
				<form:option value="1" label="网点" />
				<form:option value="2" label="时间" />
			</form:select>
		</div>
		<div class="form-group">
			<label><input name="sort" type="checkbox"
				value="${doorInOutEventQuery.sort}" onclick="this.value=this.checked?1:-1" />升序
			</label>
		</div>
		
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			<input type="button" class="btn btn-primary" name="Button" value="打印"
				onclick="preview(1)">
		</div>
	</form:form>
	<sys:message content="${message}" />
	<!--startprint1-->
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>网点</th>
				<th>人员</th>
				<th>指纹号</th>
				<th>门号</th>
				<th>进出状态</th>
				<th>事件类型</th>
				<th>认证类型</th>
				<th>发生时间</th>
				<th>处理状态</th>
				<th>处理人</th>
				<th>处理方式</th>
				<th>处理时间</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="doorInOutEventQuery"  varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count }</td>
					<td>
					<a href="${ctx}/guard/doorInOutEventQuery/form?id=${doorInOutEventQuery.id}">
							${doorInOutEventQuery.office.name} </a>
					</td>
					<td>${doorInOutEventQuery.personName}</td>
					<td>${doorInOutEventQuery.fingerNumLabel}</td>
					<td>${fns:getDictLabel(doorInOutEventQuery.doorPos, 'door_pos', '')}</td>
					<td>${fns:getDictLabel(doorInOutEventQuery.accessStatus, 'door_access_status', '')}</td>
					<td>${fns:getDictLabel(doorInOutEventQuery.eventType, 'door_control_event_type', '')}</td>
					<td>${fns:getDictLabel(doorInOutEventQuery.authorType, 'door_open_type', '')}</td>
					<td>${doorInOutEventQuery.time}</td>
					<td>${fns:getDictLabel(doorInOutEventQuery.handleStatus, 'door_handle_status', '')}</td>
					<td>${doorInOutEventQuery.handleUserName}</td>
					<td>
						<c:if test="${doorInOutEventQuery.eventType=='06'}">
						${fns:getDictLabel(doorInOutEventQuery.handleMode, 'door_control_handle_mode', '')}
					</c:if>
					</td>
					<td>${doorInOutEventQuery.handleTime}</td>
					<td>${doorInOutEventQuery.remark}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--endprint1-->
	<div class="pagination">${page}</div>
</body>
</html>