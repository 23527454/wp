<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>交接事件管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {

				$('a[rel=popover]').popover(
						{
							container : 'body',
							html : true,
							trigger : 'hover',
							placement : 'left',
							content : function() {
								return '<img src="' + $(this).data('img') + '" width="256px" height="256px"/>';
							}
						});

				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
			});
	function addRow(list, idx, tpl, row) {
		$(list).append(Mustache.render(tpl, {
			idx : idx,
			delBtn : true,
			row : row
		}));
		$(list + idx).find("select").each(function() {
			$(this).val($(this).attr("data-value"));
		});
		$(list + idx).find("input[type='checkbox'], input[type='radio']").each(
				function() {
					var ss = $(this).attr("data-value").split(',');
					for (var i = 0; i < ss.length; i++) {
						if ($(this).val() == ss[i]) {
							$(this).attr("checked", "checked");
						}
					}
				});
	}
	function delRow(obj, prefix) {
		var id = $(prefix + "_id");
		var delFlag = $(prefix + "_delFlag");
		if (id.val() == "") {
			$(obj).parent().parent().remove();
		} else if (delFlag.val() == "0") {
			delFlag.val("1");
			$(obj).html("&divide;").attr("title", "撤销删除");
			$(obj).parent().parent().addClass("error");
		} else if (delFlag.val() == "1") {
			delFlag.val("0");
			$(obj).html("&times;").attr("title", "删除");
			$(obj).parent().parent().removeClass("error");
		}
	}

	function goBack(){
		window.location.href = document.referrer;
	}
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/connectEvent/list?nodes=${connectEvent.nodes}">交接事件列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/connectEvent/form?id=${connectEvent.id}&nodes=${connectEvent.nodes}">交接事件<shiro:hasPermission
					name="guard:connectEvent:edit">${not empty connectEvent.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:connectEvent:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="connectEvent"
		action="${ctx}/guard/connectEvent/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-5">
				<div class="form-group">
					<label class="control-label col-xs-4">网点名称：</label>
					<div class="col-xs-6">
						<form:input path="officeName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">班组：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">班次：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskId" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">任务类型：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="taskType" htmlEscape="false"
							class="form-control input-sm required"
							value="${fns:getDictLabel(connectEvent.taskType, 'task_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">交接时间：</label>
					<div class="col-xs-6">
						<form:input path="time" htmlEscape="false" maxlength="32"
							class="form-control input-sm " />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">排班时间：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<input htmlEscape="false" maxlength="20"
							class="form-control input-sm required"
							value="${connectEvent.allotDate}&nbsp;&nbsp;${connectEvent.allotTime}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">所属区域：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="areaName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">车辆卡号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="cardNum" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">车牌号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="carplate" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">车辆照片：</label>
					<div class="col-xs-6">
						<a rel="popover" data-img="${ctx}/guard/image/car?id=${connectEvent.carId}">
							<img src="${ctx}/guard/image/car?id=${connectEvent.carId}" style="width: 200px; height: 126px" class="img-thumbnail"/>
						</a>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="goBack();" />
					</div>
				</div>
			</div>
			<div class="col-xs-7">
				<div class="form-group">
					<label class="control-label">款箱列表：</label>
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th data-options="field:'no'"  style="width: 30px">序号</th>
								<th data-options="field:'cardNum'" width="45%">款箱卡号</th>
								<th data-options="field:'cardNum'" width="45%">款箱编码</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${connectEvent.moneyBoxEventDetailList}"
								var="box" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">${varStatus.count}</td>
									<td>${box.cardNum}</td>
									<td>${box.moneyBox.boxCode}</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>

				<div class="form-group">
					<label class="control-label">押款员列表：</label>
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th data-options="field:'x'" width="20%">姓名</th>
								<th data-options="field:'z'" width="20%">指纹号</th>
								<th data-options="field:'k'" style="70px;">认证类型</th>
								<th data-options="field:'zhaop'" width="30%">人员照片</th>
								<th data-options="field:'zhap'" width="30%">抓拍照片</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${connectEvent.eventDetailList}" var="delist">
								<tr>
									<td>${delist.staff.name}</td>
									<td>${delist.fingerNumLabel}</td>
									<td>${fns:getDictLabel(delist.authorType, 'door_open_type', '')}</td>
									<td>
										<a rel="popover" data-img="${ctx}/guard/image/staff?id=${delist.staff.staffImageList[0].id}">
											<img src="${ctx}/guard/image/staff?id=${delist.staff.staffImageList[0].id}" style="width: 100px;height: 100px"/>
										</a>
									</td>
									<td>
									   <a rel="popover" data-img="${ctx}/guard/image?id=${delist.id}">
									   	 <img src="${ctx}/guard/image?id=${delist.id}" style="width: 100px;height: 100px"/>
									   </a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="form-group">
					<label class="control-label">交接专员列表：</label>
					<table class="table table-striped table-bordered table-condensed"
						style="width: 70%;">
						<thead>
							<tr>
								<th data-options="field:'x'" width="30%">姓名</th>
								<th data-options="field:'z'" width="30%">指纹号</th>
								<th data-options="field:'k'" style="70px;">认证类型</th>
								<th data-options="field:'zhaop'" width="40%">人员照片</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${connectEvent.commissionerEventList}"
								var="zylist">
								<tr>
									<td>${zylist.staff.name}</td>
									<td>${zylist.fingerNumLabel}</td>
									<td>${fns:getDictLabel(zylist.authorType, 'door_open_type', '')}</td>
									<td><a rel="popover"
										data-img="${ctx}/guard/image/staff?id=${zylist.staff.staffImageList[0].id}"><img
											src="${ctx}/guard/image/staff?id=${zylist.staff.staffImageList[0].id}"
											height="100" width="100" /></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</form:form>
	<div style="height: 90px;"></div>
</div>
</body>
</html>