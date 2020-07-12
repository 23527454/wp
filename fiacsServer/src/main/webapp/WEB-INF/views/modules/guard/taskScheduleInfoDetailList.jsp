<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班明细管理</title>
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
								.click(function() {
											top.$.jBox.confirm("确认要导出排班明细数据吗？","系统提示",function(v, h, f) {
																if (v == "ok") {
																	$("#searchForm").attr("action",
																					"${ctx}/guard/taskScheduleInfoDetail/export");
																	$("#searchForm").submit();
																	$("#searchForm").attr("action","${ctx}/guard/taskScheduleInfoDetail/list");
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
		$("#searchForm").attr("action","${ctx}/guard/taskScheduleInfoDetail/list");
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
		<li class="active"><a href="${ctx}/guard/taskScheduleInfoDetail/">排班明细列表</a></li>
		<shiro:hasPermission name="guard:taskScheduleInfoDetail:edit">
			<li><a href="${ctx}/guard/taskScheduleInfoDetail/form">排班明细添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="taskScheduleInfoDetail"
		action="${ctx}/guard/taskScheduleInfoDetail/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			<div class="form-group">
			<label>开始时间：</label> <input name="taskDate" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto;"
				value="${taskScheduleInfoDetail.taskDate}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</div>
			<div class="form-group">
			<label>截止时间：</label> <input name="taskDateTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto;"
				value="${taskScheduleInfoDetail.taskDateTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</div>
			
			<div class="form-group">
			<label>班组：</label>
			<sys:treeselect id="classTaskfo" name="classId"
				value="${taskScheduleInfoDetail.classId}" labelName="taskName"
				labelValue="${taskScheduleInfoDetail.taskName}" title="班组"
				url="/guard/classTaskInfo/treeData" cssClass="input-small"
				allowClear="true" notAllowSelectRoot="true" />
			</div>
			<div class="form-group">
			<label>线路：</label>
			<sys:treeselect id="line" name="lineId"
				value="${taskScheduleInfoDetail.lineId}" labelName="lineName"
				labelValue="${taskScheduleInfoDetail.lineName}" title="线路"
				url="/guard/line/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />
			</div>
			<div class="form-group">
			<label>任务类型：</label>
			<form:select path="taskType" class="form-control input-sm">
				<form:option value="" label="所有" />
				<form:options items="${fns:getDictList('task_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
			</div>
			<%-- <div class="form-group">
			<label>开始时间：</label> <input name="taskDate" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto;"
				value="${taskScheduleInfoDetail.taskDate}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</div>
			<div class="form-group">
			<label>截止时间：</label> <input name="taskDateTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto;"
				value="${taskScheduleInfoDetail.taskDateTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</div>
			 --%>
			<div class="form-group">
			<label>排序类型：</label>
			<form:select path="sortType" class="form-control input-sm">
				<!-- <form:option value="1" label="班组" />
				<form:option value="2" label="班组日期" />
				<form:option value="3" label="启动时间" />
				<form:option value="4" label="班次" />
				<form:option value="5" label="任务类型" />
				<form:option value="8" label="车牌号" />
				<form:option value="9" label="线路" />
				<form:option value="10" label="车辆确认" />
				<form:option value="11" label="专员确认" /> -->
				<form:option value="1" label="班次" />
				<form:option value="2" label="排班日期" />
				<form:option value="3" label="排班时间" />
				<form:option value="4" label="班组名称" />
				<form:option value="5" label="任务执行日期" />
				<form:option value="8" label="任务执行时间" />
				<form:option value="9" label="任务类型" />
				<form:option value="10" label="任务时间" />
				<!-- <form:option value="11" label="操作" /> -->
			</form:select>
			<label><input name="sort" type="checkbox"
				value="${taskScheduleInfoDetail.sort}"
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
				<!-- <th>班组名称</th>
				<th>班组日期</th>
				<th>启动时间</th>
				<th>班次</th>
				<th>任务类型</th>
				<th>车牌号</th>
				<th>线路名称</th>
				<th>车辆确认</th>
				<th>专员确认</th> -->
				<th>班次</th>
				<th>排班日期</th>
				<th>排班时间</th>
				<th>班组名称</th>
				<th>任务执行日期</th>
				<th>任务执行时间</th>
				<th>任务类型</th>
				<th>任务时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="taskScheduleInfoDetail"  varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count }</td>
					<td>
					<%-- <a
						href="${ctx}/guard/taskScheduleInfoDetail/form?id=${taskScheduleInfoDetail.id}&selectedClassTaskId=${taskScheduleInfoDetail.classTaskId}">				
						${taskScheduleInfoDetail.id}</a> --%>
						<a
						href="${ctx}/guard/taskScheduleInfo/form?id=${taskScheduleInfoDetail.id}&selectedClassTaskId=${taskScheduleInfoDetail.classTaskId}&www=1">
							${taskScheduleInfoDetail.id} </a>
						</td>
					<td>${taskScheduleInfoDetail.allotDate}</td>
					<td>${taskScheduleInfoDetail.allotTime}</td>
					<td>
							${taskScheduleInfoDetail.taskName}</td>
					<td>${taskScheduleInfoDetail.taskDate}</td>
					<td>${taskScheduleInfoDetail.taskTime}</td>
					
					<td>${taskScheduleInfoDetail.taskType}</td>
					<td>
					<c:if test="${taskScheduleInfoDetail.taskTimeClass==0}">
					循环使用
						</c:if>
					<c:if test="${taskScheduleInfoDetail.taskTimeClass==1}">
					执行一次
						</c:if>
						
						
						</td>
					
					
						<td>
						<a href="${ctx}/guard/taskScheduleInfoDetail/form?id=${taskScheduleInfoDetail.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}">查看</a>
						<a href="${ctx}/guard/taskScheduleInfoDetail/delete?id=${taskScheduleInfoDetail.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}" onclick="return confirmx('确认要删除该排班信息吗？', this.href)">删除</a>
						<a href="${ctx}/guard/taskScheduleInfo/download?id=${taskScheduleInfoDetail.id}&selectedClassTaskId=${taskScheduleInfo.classTaskId}" onclick="return confirmx('是否同步该排班', this.href)">同步</a></td>
						</td>
					
					<%-- 
					<td>${taskScheduleInfoDetail.carplate}</td> --%>
				<%-- 	<td>${taskScheduleInfoDetail.lineName}</td> --%>
					<%-- <td>${fns:getDictLabel(taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyCar, 'message_confirm', '')}
					</td>
					<td>${fns:getDictLabel(taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyInterMan, 'message_confirm', '')}
					</td> --%>
<%-- 					<td><a
						href="${ctx}/guard/taskScheduleInfoDetail/form?id=${taskScheduleInfoDetail.id}">
							${taskScheduleInfoDetail.taskName} </a></td>
					<td>${taskScheduleInfoDetail.taskScheduleInfo.taskDate}</td>
					<td>${taskScheduleInfoDetail.taskScheduleInfo.taskTime}</td>
					<td>${taskScheduleInfoDetail.taskId}</td>
					<td>${taskScheduleInfoDetail.taskType}</td>
					<td>${taskScheduleInfoDetail.carplate}</td>
					<td>${taskScheduleInfoDetail.lineName}</td>
					<td>${fns:getDictLabel(taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyCar, 'message_confirm', '')}
					</td>
					<td>${fns:getDictLabel(taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyInterMan, 'message_confirm', '')}
					</td> --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--endprint1-->
	<div class="pagination">${page}</div>
</body>
</html>