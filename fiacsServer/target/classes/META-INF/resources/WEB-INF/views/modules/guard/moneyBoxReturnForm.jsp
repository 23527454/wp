<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱上缴信息管理</title>
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
		<li><a href="${ctx}/guard/moneyBoxReturn/">款箱上缴信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/moneyBoxReturn/form?id=${moneyBoxReturn.id}">款箱上缴信息详情<shiro:lacksPermission
					name="guard:moneyBoxReturn:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="moneyBoxReturn"
		action="${ctx}/guard/moneyBoxReturn/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">款箱ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="moneyBoxId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">设备ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="equipmentId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上缴ID：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="moneyBoxReturnId" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上缴类型：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="returnType" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上缴时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<input name="returnTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${moneyBoxReturn.returnTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上传时间：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<input name="uploadTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${moneyBoxReturn.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">上缴状态：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-xs-4">
				<form:input path="returnSysStatus" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
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