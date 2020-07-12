<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱预约管理</title>
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
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/moneyBoxOrder/">款箱预约列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/moneyBoxOrder/form?id=${moneyBoxOrder.id}">款箱预约详情<shiro:lacksPermission
					name="guard:moneyBoxOrder:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="moneyBoxOrder"
		action="${ctx}/guard/moneyBoxOrder/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">款箱编码：<span
				class="help-inline"> </span></label>
			<div class="col-xs-4">
				<form:input path="boxCode" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">款箱卡号：<span
				class="help-inline"> </span></label>
			<div class="col-xs-4">
				<form:input path="boxCode" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">预约ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="moneyBoxOrderId" htmlEscape="false" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">预约类型：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="orderType" htmlEscape="false" maxlength="1" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">回送日期：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<input name="allotReturnTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${moneyBoxOrder.allotReturnTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">预约时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<input name="orderTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${moneyBoxOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上传时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<input name="uploadTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${moneyBoxOrder.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">预约状态：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="orderSysStatus" htmlEscape="false" maxlength="1" class="form-control input-sm required"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</body>
</html>