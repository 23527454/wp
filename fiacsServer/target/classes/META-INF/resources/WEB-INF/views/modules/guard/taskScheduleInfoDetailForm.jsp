<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班明细管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});

				var checkbox1 = document.getElementById("checkbox1");
				var checkbox2 = document.getElementById("checkbox2");
				var v1 = $("#checkbox1").val();
				var v2 = $("#checkbox2").val();
				if (v1 = "1") {
					checkbox1.checked = true;
				}
				if (v2 = "1") {
					checkbox2.checked = true;
				}
			});
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/taskScheduleInfoDetail/">排班明细列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/taskScheduleInfoDetail/form?id=${taskScheduleInfoDetail.id}">排班明细<shiro:hasPermission
					name="guard:taskScheduleInfoDetail:edit">${not empty taskScheduleInfoDetail.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:taskScheduleInfoDetail:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="taskScheduleInfoDetail"
		action="${ctx}/guard/taskScheduleInfoDetail/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />

		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label col-xs-3">班组：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskName" htmlEscape="false"
							class="form-control input-sm " />
					</div>
				</div>
				<!-- <div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务类型：</label>
					<div class="col-xs-8">
						<div class="radio-inline">
							<span onclick="getRadio();"> 
							<input name="radio" type="radio" value="0" id="taskType0" /><label for="taskType0">派送</label>
							<input name="radio" type="radio" value="1" id="taskType1" /><label for="taskType1">回收 </label>
	                        <input name="radio" type="radio" value="2" id="taskType2" /><label for="taskType2">临时派送</label>
	                        <input name="radio" type="radio" value="3" id="taskType3" /><label for="taskType3">临时回收</label>
							<input name="radio" type="radio" value="4" id="taskType4" /><label for="taskType4">贵金属派送</label>
							</span>
						</div>
					</div>
				</div> -->
					<div class="form-group">
					<label class="control-label col-xs-3">任务类型：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskType" htmlEscape="false" maxlength="12"
							class="form-control input-sm "
							value="${fns:getDictLabel(taskScheduleInfoDetail.taskType, 'task_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务执行日期：</label>
					<div class="col-xs-3">
						<input name="taskDate" type="text" readonly="readonly"
							maxlength="12" class="Wdate form-control input-sm "
							value="${taskScheduleInfoDetail.taskDate}" id="date2"
							style="height: auto;width: 150px;"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务执行时间：</label>
					<div class="col-xs-3">
						<input name="taskTime" type="text" readonly="readonly"
							maxlength="8" class="Wdate form-control input-sm "
							value="${taskScheduleInfoDetail.taskTime}" id="time2"
							style="height: auto;width: 150px;"
							onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>使用次数：</label>
					<div class="col-xs-5">
						<form:select path="taskTimeClass" class="form-control input-sm required">
							<form:options items="${fns:getDictList('task_time_class')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>车辆确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyCar" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_car')}"
								id="verifyCar" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>专员确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyInterMan" class="form-control input-sm required">
							<form:options items="${fns:getDictList('inter_man')}"
								id="verifyInterMan" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>款箱确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyMoneyBox" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_locker')}"
								id="verifyMoneyBox" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">最少专员数量：</label>
					<div class="col-xs-5">
						<form:input path="interManNum" htmlEscape="false" id="interManNum"
							class="form-control input-sm digits " 
							value="${taskScheduleInfoDetail.interManNum}"
							readOnly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">最少押款员数量：</label>
					<div class="col-xs-5">
						<form:input path="patrolManNum" htmlEscape="false"
							id="patrolManNum" class="form-control input-sm digits"  readOnly="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务超时：</label>
					<div class="col-xs-5">
						<div class="input-group">
							<form:input path="taskTimeout" htmlEscape="false"
								class="form-control input-sm required digits"/>
							<span class="input-group-addon input-sm">分钟</span>
						</div>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="control-label col-xs-3">班次：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskId" htmlEscape="false" maxlength="12"
							class="form-control input-sm " />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">启动时间：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<input htmlEscape="false" maxlength="20"
							class="form-control input-sm "
							value="${taskScheduleInfoDetail.taskScheduleInfo.taskDate}&nbsp;&nbsp;${taskScheduleInfoDetail.taskScheduleInfo.taskTime}" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-xs-3">任务类型：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskType" htmlEscape="false" maxlength="12"
							class="form-control input-sm "
							value="${fns:getDictLabel(taskScheduleInfoDetail.taskType, 'task_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">车牌号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="carplate" htmlEscape="false" maxlength="18"
							class="form-control input-sm " />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">信息确认：</label>
					<div class="col-xs-6">
						<input name="checkbox1" type="checkbox" id="checkbox1"
							value="${taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyCar}" />车辆确认
						<input name="checkbox2" type="checkbox" id="checkbox2"
							value="${taskScheduleInfoDetail.taskScheduleInfo.classTaskInfo.verifyInterMan}" />专员确认
					</div>
				</div> --%>
				<div class="form-group">
					<label class="control-label col-xs-3">备注：</label>
					<div class="col-xs-5">
						<form:textarea path="remarks" htmlEscape="false" rows="4"
							maxlength="255" class="input-xxlarge" style="width:100%;" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<shiro:hasPermission name="guard:taskScheduleInfoDetail:edit">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="保 存" />&nbsp;</shiro:hasPermission>
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="history.go(-1)" />
					</div>
				</div>
			</div>
			
		 	<div class="col-xs-6">
				<%-- <div class="form-group">
					<label class="control-label ">线路名称：
						${taskScheduleInfoDetail.lineName}</label>
				</div> --%>
				<div class="form-group">
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th style="width: 30px;" align="center">序号</th>
							<th align="center">车辆名称</th>
								<th align="center">车牌号</th>
								<th align="center">车辆卡号</th>
							</tr>
						</thead>
						<tbody>
					
							<c:forEach
								items="${taskScheduleInfoDetail.taskLineInfoList}"
								var="line" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">${varStatus.count}</td>
								
									<td>${line.name}</td>
								
									<td>${line.name}</td>
							
									<td>${line.name}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<br/>
				<!-- <div class="form-group">
					<label class="control-label">押款员信息：</label>
				</div> -->
				<div class="form-group">
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th style="width: 30px;" align="center">序号</th>
							<th align="center">人员名称</th>
								<th align="center">指纹号</th>
								<th align="center">证书号</th>
							</tr>
						</thead>
						<tbody>
										
							<c:forEach
								items="${taskScheduleInfoDetail.taskLineInfoList}"
								var="line" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">${varStatus.count}</td>
								
									<td>${line.name}</td>
								
									<td>${line.name}</td>
							
									<td>${line.name}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
						<div class="form-group">
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th style="width: 30px;" align="center">序号</th>
							<th align="center">营业网点名称</th>
						
							</tr>
						</thead>
						<tbody>
										
							<c:forEach
								items="${taskScheduleInfoDetail.taskLineInfoList}"
								var="line" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">${varStatus.count}</td>											
									<td>${line.name}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div> 

		</div>
	</form:form>
</div>
</body>
</html>