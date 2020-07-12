<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆事件查询管理</title>
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
															"确认要导出车辆事件数据吗？",
															"系统提示",
															function(v, h, f) {
																if (v == "ok") {
																	$("#searchForm")
																			.attr(
																					"action",
																					"${ctx}/guard/carEventQuery/export");
																	$("#searchForm")
																			.submit();
																	$("#searchForm")
																	.attr(
																			"action",
																			"${ctx}/guard/carEventQuery/list");
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
			var headstr = "<html><head><title>交接事件列表</title></head><body>";
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
		<li class="active"><a href="${ctx}/guard/carEventQuery/">车辆事件查询列表</a></li>
		<shiro:hasPermission name="guard:carEventQuery:edit">
			<li><a href="${ctx}/guard/carEventQuery/form">车辆事件查询添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="carEventQuery"
		action="${ctx}/guard/carEventQuery/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			<div class="form-group">
			<label>开始时间：</label>
			<input name="time" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${carEventQuery.time}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label>
			<input name="timeTwo" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${carEventQuery.timeTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>区域：</label>
			<sys:treeselect id="area" name="area.id"
				value="${carEventQuery.area.id}" labelName="area.name"
				labelValue="${carEventQuery.area.name}" title="区域"
				url="/sys/area/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
		<label>网点：</label>
		<sys:treeselect id="office" name="office.id"
			value="${carEventQuery.office.id}" labelName="office.name"
			labelValue="${carEventQuery.office.name}" title="网点"
			url="/sys/office/treeData" cssClass="" allowClear="true"
			notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
		<label>车牌号：</label>
			<form:input path="car.carplate" htmlEscape="false" class="form-control input-sm" />
		</div>
		<%-- <div class="form-group">
			<label>开始时间：</label>
			<input name="time" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${carEventQuery.time}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label>
			<input name="timeTwo" type="text" readonly="readonly" maxlength="20"
				class="form-control input-sm Wdate" value="${carEventQuery.timeTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div> --%>
		<div class="form-group">
			<label>排序类型：</label>
			<form:select path="sortType" class="form-control input-sm">
				<form:option value="1" label="网点" />
				<form:option value="2" label="时间" />
				<form:option value="3" label="车辆卡号" />
				<form:option value="4" label="车牌号" />
				<form:option value="5" label="负责人" />
			</form:select>
		</div>
		<div class="form-group">
			<label><input name="sort" type="checkbox"
				value="${carEventQuery.sort}" onclick="this.value=this.checked?1:-1" />升序
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
				<th>时间</th>
				<th>车辆卡号</th>
				<th>车牌号</th>
				<th>负责人</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="carEventQuery"  varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${carEventQuery.office.name}</td>
					<td>${carEventQuery.time}</td>
					<td>${carEventQuery.car.cardNum}</td>
					<td>${carEventQuery.car.carplate}</td>
					<td>${carEventQuery.car.admin}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--endprint1-->
	<div class="pagination">${page}</div>
</body>
</html>