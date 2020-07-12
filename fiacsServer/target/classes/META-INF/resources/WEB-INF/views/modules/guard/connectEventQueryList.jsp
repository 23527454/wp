<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>交接事件查询管理</title>
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
															"确认要导出交接事件数据吗？",
															"系统提示",
															function(v, h, f) {
																if (v == "ok") {
																	$(
																			"#searchForm")
																			.attr(
																					"action",
																					"${ctx}/guard/connectEventQuery/export");
																	$(
																			"#searchForm")
																			.submit();
																	$(
																	"#searchForm")
																	.attr(
																			"action",
																			"${ctx}/guard/connectEventQuery/list");
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
		<li class="active"><a href="${ctx}/guard/connectEventQuery/">交接事件查询列表</a></li>
		<shiro:hasPermission name="guard:connectEventQuery:edit">
			<li><a href="${ctx}/guard/connectEventQuery/form">交接事件查询查看</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="connectEvent"
		action="${ctx}/guard/connectEventQuery/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			<div class="form-group">
				<label>开始时间：</label> <input name="time" type="text"
					readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
					value="${connectEvent.time}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
			
			<div class="form-group">
				<label>截止时间：</label> <input name="taskDateTwo" type="text"
					readonly="readonly" maxlength="20" class="form-control input-sm Wdate"  style="height:auto"
					value="${connectEvent.taskDateTwo}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
			<div class="form-group">
				<label>区域：</label>
				<sys:treeselect id="area" name="areaId" value="${connectEvent.areaId}"
					labelName="areaName" labelValue="${connectEvent.areaName}" title="区域"
					url="/sys/area/treeData" cssClass="form-control input-small" allowClear="true"
					notAllowSelectRoot="true" />
			</div>
			<div class="form-group">
				<label>网点：</label>
				<sys:treeselect id="office" name="officeId" value="${connectEvent.officeId}"
					labelName="officeName" labelValue="${connectEvent.officeName}" title="网点"
					url="/sys/office/treeData" cssClass="form-control input-small" allowClear="true"
					notAllowSelectRoot="true" />
			</div>
			
			<div class="form-group">
				<label>任务类型：</label>
				<form:select path="taskType" class="form-control input-medium">
					<form:option value="" label="所有类型" />
					<form:options items="${fns:getDictList('task_type')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>&nbsp;&nbsp;
			</div>
			
			<%-- <div class="form-group">
				<label>开始时间：</label> <input name="time" type="text"
					readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
					value="${connectEvent.time}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
			
			<div class="form-group">
				<label>截止时间：</label> <input name="taskDateTwo" type="text"
					readonly="readonly" maxlength="20" class="form-control input-sm Wdate"  style="height:auto"
					value="${connectEvent.taskDateTwo}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div> --%>
			
			<div class="form-group">
			<label>排序类型：</label>
				<form:select path="sortType" class="form-control input-sm">
					<form:option value="1" label="网点" />
					<form:option value="2" label="时间" />
					<!-- <form:option value="3" label="押款员" />
					<form:option value="4" label="交接专员" />
					<form:option value="6" label="车辆卡号" />-->
					<form:option value="5" label="车牌号" />

					<form:option value="7" label="任务类型" />
					<form:option value="8" label="班组" />
					<form:option value="9" label="班次" />
					<form:option value="10" label="区域" />
				</form:select>
				<label><input name="sort" type="checkbox"
					value="${connectEventQuery.sort}" class="form-control"
					onclick="this.value=this.checked?1:-1" />升序 </label>
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
				<th>车牌号</th>
				<th>专员</th>
                <th>押款员</th>
                <th>款箱编码</th>
				<!--<th>车辆卡号</th>-->
				<th>任务类型</th>
				<th>班组</th>
				<th>班次</th>
				<th>区域</th>
			</tr>
		</thead>
		<tbody id="eventMessages">
			<tr id="placeHolder"></tr>
			<c:forEach items="${page.list}" var="connectEvent" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>	
					<td><a
						href="${ctx}/guard/connectEventQuery/form?id=${connectEvent.id}">
							${connectEvent.officeName} </a></td>
					<td>${connectEvent.time}</td>
					<td>${connectEvent.carplate}</td>
					<td>${connectEvent.commissionerNames}</td>
                    <td>${connectEvent.eventDetailNames}</td>
                    <td>${connectEvent.moneyboxNums}</td>
					<!--<td>${connectEvent.cardNum}</td>-->
					<td>${connectEvent.taskType}</td>
					<td>${connectEvent.taskName}</td>
					<td>${connectEvent.taskId}</td>
					<td>${connectEvent.areaName}</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--endprint1-->
	<div class="pagination">${page}</div>
</body>
</html>